package com.test.books.di.module

import android.app.Activity
import com.test.books.di.PerFragment
import com.test.books.ui.utils.AppImagePickerManager
import com.test.books.ui.utils.AppImagePickerManagerImpl
import com.test.books.ui.utils.AppPermissionManager
import com.test.books.ui.utils.AppPermissionsManagerImpl
import dagger.Module
import dagger.Provides

@Module
class ManagerModule(private val activity: Activity) {

    @Provides
    @PerFragment
    internal fun providePermissionManager(): AppPermissionManager {
        return AppPermissionsManagerImpl(activity)
    }

    @Provides
    @PerFragment
    internal fun provideImagePicker(appPermissionManager: AppPermissionManager): AppImagePickerManager {
        return AppImagePickerManagerImpl(activity, appPermissionManager)
    }
}
