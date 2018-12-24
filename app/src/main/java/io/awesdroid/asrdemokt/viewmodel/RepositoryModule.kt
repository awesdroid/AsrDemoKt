package io.awesdroid.asrdemokt.viewmodel

import dagger.Module
import dagger.Provides
import io.awesdroid.asrdemokt.db.MyDatabase

import javax.inject.Singleton

/**
 * @author awesdroid
 */
@Module
class RepositoryModule {
    @Singleton
    @Provides
    internal fun provideRepository(database: MyDatabase?): MyRepository {
        return MyRepository(database)
    }
}
