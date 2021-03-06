package com.arcao.opencaching4locus

import android.arch.lifecycle.ViewModelProvider
import com.arcao.opencaching4locus.ui.authentication.AuthenticationActivity
import com.arcao.opencaching4locus.ui.authentication.AuthenticationModule
import com.arcao.opencaching4locus.ui.base.lifecycle.viewmodel.AppViewModelFactory
import com.arcao.opencaching4locus.ui.dashboard.DashboardActivity
import com.arcao.opencaching4locus.ui.dashboard.DashboardModule
import com.arcao.opencaching4locus.ui.error.ErrorActivity
import com.arcao.opencaching4locus.ui.livemap.LiveMapJobService
import com.arcao.opencaching4locus.ui.livemap.LiveMapModule
import com.arcao.opencaching4locus.ui.livemap.receiver.LiveMapBroadcastReceiver
import com.arcao.opencaching4locus.ui.settings.SettingsActivity
import com.arcao.opencaching4locus.ui.settings.SettingsModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidBindingModule {
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [AuthenticationModule::class])
    abstract fun authenticationActivity(): AuthenticationActivity

    @ContributesAndroidInjector(modules = [DashboardModule::class])
    abstract fun dashboardActivity(): DashboardActivity

    @ContributesAndroidInjector
    abstract fun errorActivity(): ErrorActivity

    @ContributesAndroidInjector(modules = [LiveMapModule::class])
    abstract fun liveMapBroadcastReceiver(): LiveMapBroadcastReceiver

    @ContributesAndroidInjector(modules = [LiveMapModule::class])
    abstract fun liveMapJobService(): LiveMapJobService

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun settingsActivity(): SettingsActivity
}