package io.awesdroid.asrdemokt.db

import dagger.Module
import dagger.Provides
import io.awesdroid.asrdemokt.BaseApp

/**
 * @author awesdroid
 */
@Module
class DatabaseModule {
    @Provides
    internal fun provideDatabase(app: BaseApp): MyDatabase? {
        return MyDatabase.getInstance(app)
    }
}
