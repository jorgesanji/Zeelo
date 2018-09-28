package com.test.books.ui.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.test.books.R
import com.test.books.business.usecase.base.BaseSubscriber
import com.test.books.business.usecase.base.BaseUseCaseImpl
import rx.Observable
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

interface IOPickerImageSelector {
    fun onMultiImagesSelected(images: List<String>)
    fun onImageSelected(path: String)
    fun loading()
    fun onCancel()
    fun onError()
}

interface AppImagePickerManager {
    var listener: IOPickerImageSelector
    fun setMultipleSelection(multipleSelection: Boolean)
    fun setOwner(owner: Any)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun launchCamera()
    fun launchGallery(isMultiChoice: Boolean)
}

class AppImagePickerManagerImpl : AppImagePickerManager, IOAppPermission {

    val IMAGE_MAX = 6
    val COMPRESS_QUALITY = 80
    val IMAGE_ORIGINAL = "bitmapOriginal"
    val CAMERA_CAPTURE = 2000
    private var FILEPROVIDER: String?
    private var Owner: Any? = null
    private var multipleSelection = false
    private val permissionsManager: AppPermissionManager
    private var output: File? = null
    private var activity: Activity
    override lateinit var listener: IOPickerImageSelector

    @Inject
    constructor(activity: Activity, appPermissionsManager: AppPermissionManager) {
        this.permissionsManager = appPermissionsManager
        this.activity = activity
        FILEPROVIDER = appPermissionsManager.getContext()!!.getString(R.string.file_provider_authority)
    }

    private fun getActivity(): AppCompatActivity? {
        return if (Owner is AppCompatActivity) {
            Owner as AppCompatActivity?
        } else null
    }

    private fun getFragment(): Fragment? {
        return if (Owner is Fragment) {
            Owner as Fragment?
        } else null
    }

    private fun getContext(): Activity? {
        return if (getActivity() != null)
            getActivity()
        else if (getFragment() != null)
            getFragment()!!
                    .activity
        else
            null
    }

    private fun selectImages(data: Intent) {
        val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
        if (images != null && images.size != 0) {
            val imageSingle = images[0]
            val uris = ArrayList<String>()
            if (multipleSelection) {
                for (image in images) {
                    uris.add(BitmapUtils.compressAndSaveImageOnPath(image.path, COMPRESS_QUALITY))
                }
                listener!!.onMultiImagesSelected(uris)
            } else {
                listener!!.onImageSelected(BitmapUtils.compressAndSaveImageOnPath(imageSingle.path, COMPRESS_QUALITY))
            }
        } else if (listener != null) {
            listener!!.onError()
        }
    }

    override fun setMultipleSelection(multipleSelection: Boolean) {
        this.multipleSelection = multipleSelection
    }

    override fun setOwner(owner: Any) {
        this.Owner = owner
        this.permissionsManager.setContext(getContext()!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) {
            if (listener != null) {
                listener!!.onCancel()
            }
        } else if (requestCode == CAMERA_CAPTURE) {
            if (output != null) {
                if (listener != null) {
                    listener!!.loading()
                }
                val useCaseFixOrientation = FixImageOrientation()
                useCaseFixOrientation.setParameters(output!!.absolutePath)
                useCaseFixOrientation.subscribe(object : BaseSubscriber<String>() {

                    override fun onError(throwable: Throwable?) {
                        super.onError(throwable)
                        listener!!.onError()
                    }

                    override fun onNext(s: String) {
                        super.onNext(s)
                        if (listener != null) {
                            listener!!.onImageSelected(
                                    BitmapUtils.compressAndSaveImageOnPath(s, COMPRESS_QUALITY))
                            output = null
                        }
                    }
                })
            } else if (listener != null) {
                listener!!.onError()
            }
        } else if (data != null) {
            if (requestCode == Config.RC_PICK_IMAGES) {
                selectImages(data)
            }
        }else if (listener != null) {
            listener!!.onError()
        }
    }

    //https://developer.android.com/training/camera/photobasics.html
    override fun launchCamera() {
        if (permissionsManager.checkCameraPermissions()) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(getContext()!!.packageManager) != null) {
                try {
                    this.output = ImageUtils.createImageFile(getContext()!!, IMAGE_ORIGINAL)
                    val photoURI = FileProvider.getUriForFile(getContext()!!, FILEPROVIDER!!, output!!)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    val resolvedIntentActivities = getContext()!!.packageManager
                            .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolvedIntentInfo in resolvedIntentActivities) {
                        val packageName = resolvedIntentInfo.activityInfo.packageName
                        getContext()!!.grantUriPermission(packageName, photoURI, Intent
                                .FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    if (getFragment() != null) {
                        getFragment()!!.startActivityForResult(cameraIntent, CAMERA_CAPTURE)
                    } else {
                        getActivity()!!.startActivityForResult(cameraIntent, CAMERA_CAPTURE)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            permissionsManager.requestCameraPermission(this)
        }
    }

    override fun permission(permission: AppPermissionsManagerImpl.Permission, enable: Boolean) {
        if (permission == AppPermissionsManagerImpl.Permission.CAMERA && enable) {
            launchCamera()
        }
    }

    override fun launchGallery(isMultiChoice: Boolean) {
        setMultipleSelection(isMultiChoice)
        val activity = getContext()
        val fragment = getFragment()
        ImagePicker.with(fragment!!)
                .setShowCamera(false)
                .setFolderTitle(activity!!.getString(R.string.picker_title))
                .setImageTitle(activity.getString(R.string.picker_select_images))
                .setDoneTitle(activity.getString(R.string.action_done))
                .setLimitMessage(activity.getString(R.string.max_images_picked, IMAGE_MAX))
                .setMultipleMode(isMultiChoice)
                .setMaxSize(if (isMultiChoice) IMAGE_MAX else 1)
                .start()
    }


    class FixImageOrientation : BaseUseCaseImpl<String>() {

        private var path: String? = null

        override fun buildUseCaseObservable(): Observable<String> {
            return Observable.fromCallable {
                BitmapUtils.fixOrientationIfNeeded(path)
                path
            }
        }

        fun setParameters(path: String) {
            this.path = path
        }
    }
}