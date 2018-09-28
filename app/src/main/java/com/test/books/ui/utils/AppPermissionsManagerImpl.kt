package com.test.books.ui.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.test.books.ui.view.base.BaseActivity
import javax.inject.Inject

interface IOAppPermission {
    fun permission(permission: AppPermissionsManagerImpl.Permission, enable: Boolean)
}

interface AppPermissionManager {
    fun requestPermissions(permissionsList: List<AppPermissionsManagerImpl.Permission>)
    fun requestContactPermission(permissionListener: IOAppPermission)
    fun requestAccountPermission(permissionListener: IOAppPermission)
    fun requestContactPermission()
    fun requestCameraPermission(permissionListener: IOAppPermission)
    fun requestCameraPermission()
    fun requestGalleryPermission()
    fun requestGalleryPermission(permissionListener: IOAppPermission)
    fun requestLocationPermission(permissionListener: IOAppPermission)
    fun requestLocationPermission()
    fun requestSmsPermission(permissionListener: IOAppPermission)
    fun requestSmsAndAccountPermission(ioAppPermission: IOAppPermission)
    fun requestSmsPermission()
    fun checkContactPermissions(): Boolean
    fun checkLocationPermissions(): Boolean
    fun checkCameraPermissions(): Boolean
    fun checkAccountPermissions(): Boolean
    fun checkCameraCropPermissions(): Boolean
    fun checkGalleryPermissions(): Boolean
    fun checkSmsPermissions(): Boolean
    fun checkSmsAndAccountPermissions(): Boolean
    fun getContext(): Context?
    fun setContext(context: Context)
    fun setActivity(activity: Activity)
}

class AppPermissionsManagerImpl @Inject constructor(private var activity: Activity) :  AppPermissionManager{

   companion object{
        val CODE_REQUEST_LOCATION:Int = 255
        val CODE_REQUEST_CAMERA = 245
        val CODE_REQUEST_SMS = 235
        val CODE_REQUEST_GALLERY = 225
        val CODE_REQUEST_READ_CONTACTS = 200
        val CODE_REQUEST_ALL = 100
        private val CODE_REQUEST_ACCOUNT = 666
        private val CODE_REQUEST_SMS_ACCOUNT = 0x01

        val CONTACTOS_PERMISSIONS:Array<String> = arrayOf(Manifest.permission
                .READ_CONTACTS)
        val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        val GALLERY_PERMISSIONS = arrayOf(Manifest.permission
                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        val LOCATION_PERMISSIONS = arrayOf(Manifest.permission
                .ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        val SMS_PERMISSIONS = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)

        val ALL_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS, Manifest
                .permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission
                .RECEIVE_SMS)
        val ACCOUNT_PERMISSIONS = arrayOf(Manifest.permission.GET_ACCOUNTS)
        val SMS_ACCOUNT_PERMISSIONS = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.GET_ACCOUNTS)
    }

    enum class Permission private constructor(val permissions: Array<String>, val code: Int) {

        CONTACTOS(CONTACTOS_PERMISSIONS, CODE_REQUEST_READ_CONTACTS),
        CAMERA(CAMERA_PERMISSIONS, CODE_REQUEST_CAMERA),
        GALLERY(GALLERY_PERMISSIONS, CODE_REQUEST_GALLERY),
        LOCATION(LOCATION_PERMISSIONS, CODE_REQUEST_LOCATION),
        SMS(SMS_PERMISSIONS, CODE_REQUEST_SMS),
        ACCOUNT(ACCOUNT_PERMISSIONS, CODE_REQUEST_ACCOUNT),
        SMS_ACCOUNT(SMS_ACCOUNT_PERMISSIONS, CODE_REQUEST_SMS_ACCOUNT),
        ALL(ALL_PERMISSIONS, CODE_REQUEST_ALL),
        UNKNOW(arrayOf<String>(), -1);

        companion object {

            fun getPermissionFromCode(code: Int): Permission {
                when (code) {
                    CODE_REQUEST_READ_CONTACTS -> return CONTACTOS
                    CODE_REQUEST_ACCOUNT -> return ACCOUNT
                    CODE_REQUEST_LOCATION -> return LOCATION
                    CODE_REQUEST_CAMERA -> return CAMERA
                    CODE_REQUEST_SMS -> return SMS
                    CODE_REQUEST_GALLERY -> return GALLERY
                    CODE_REQUEST_ALL -> return ALL
                    else -> return UNKNOW
                }
            }
        }
    }

    private var context: Context? = null

    protected fun requestPermissions(permission: Array<String>, code: Int, permissionListener: IOAppPermission?) {
        if (activity != null) {
            if (permissionListener != null && activity is BaseActivity<*>) {
                (activity as BaseActivity<*>).permissionListener = permissionListener
            }
            ActivityCompat.requestPermissions(activity!!, permission, code)
        }
    }

    protected fun checkPermissions(param: Array<String>): Boolean {
        for (permission in param) {
            if (ContextCompat.checkSelfPermission(getContext()!!,
                            permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    protected fun requestPermissions(permission: Permission, permissionListener: IOAppPermission?) {
        requestPermissions(permission.permissions, permission.code, permissionListener)
    }

    //Public Methods

    override fun requestPermissions(permissionsList: List<Permission>) {
        for (permission in permissionsList) {
            requestPermissions(permission, null)
        }
    }

    override fun requestContactPermission(permissionListener: IOAppPermission) {
        requestPermissions(Permission.CONTACTOS, permissionListener)
    }

    override fun requestAccountPermission(permissionListener: IOAppPermission) {
        requestPermissions(Permission.ACCOUNT, permissionListener)
    }

    override fun requestContactPermission() {
        requestPermissions(Permission.CONTACTOS, null)
    }

    override fun requestCameraPermission(permissionListener: IOAppPermission) {
        requestPermissions(Permission.CAMERA, permissionListener)
    }

    override fun requestCameraPermission() {
        requestPermissions(Permission.CAMERA, null)
    }

    override fun requestGalleryPermission() {
        requestPermissions(Permission.GALLERY, null)
    }

    override fun requestGalleryPermission(permissionListener: IOAppPermission) {
        requestPermissions(Permission.GALLERY, permissionListener)
    }

    override fun requestLocationPermission(permissionListener: IOAppPermission) {
        requestPermissions(Permission.LOCATION, permissionListener)
    }

    override fun requestLocationPermission() {
        requestPermissions(Permission.LOCATION, null)
    }

    override fun requestSmsPermission(permissionListener: IOAppPermission) {
        requestPermissions(Permission.SMS, permissionListener)
    }

    override fun requestSmsAndAccountPermission(ioAppPermission: IOAppPermission) {
        requestPermissions(Permission.SMS_ACCOUNT, ioAppPermission)
    }

    override fun requestSmsPermission() {
        requestPermissions(Permission.SMS, null)
    }

    override fun checkContactPermissions(): Boolean {
        return checkPermissions(Permission.CONTACTOS.permissions)
    }

    override fun checkLocationPermissions(): Boolean {
        return checkPermissions(Permission.LOCATION.permissions)
    }

    override fun checkCameraPermissions(): Boolean {
        return checkPermissions(Permission.CAMERA.permissions)
    }

    override fun checkAccountPermissions(): Boolean {
        return checkPermissions(Permission.ACCOUNT.permissions)
    }

    override fun checkCameraCropPermissions(): Boolean {
        return checkPermissions(Permission.CAMERA.permissions)
    }

    override fun checkGalleryPermissions(): Boolean {
        return checkPermissions(Permission.GALLERY.permissions)
    }

    override fun checkSmsPermissions(): Boolean {
        return checkPermissions(Permission.SMS.permissions)
    }

    override fun checkSmsAndAccountPermissions(): Boolean {
        return checkPermissions(Permission.SMS_ACCOUNT.permissions)
    }

    override fun getContext(): Context? {
        return if (context != null) context else activity
    }

    override fun setContext(context: Context) {
        this.context = context
    }

    override fun setActivity(activity: Activity) {
        this.activity = activity
    }
}