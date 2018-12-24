package io.awesdroid.asrdemokt.viewmodel

import dagger.Component
import io.awesdroid.asrdemokt.BaseAppModule
import io.awesdroid.asrdemokt.db.DatabaseModule

import javax.inject.Singleton

/**
 * @author awesdroid
 */
@Singleton
@Component(modules = [RepositoryModule::class, DatabaseModule::class, BaseAppModule::class])
interface RepositoryComponent {
    fun inject(viewModel: MyViewModel)
}
