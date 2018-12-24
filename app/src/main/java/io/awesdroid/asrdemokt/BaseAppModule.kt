package io.awesdroid.asrdemokt

import dagger.Module
import dagger.Provides

/**
 * @author Awesdroid
 */
@Module
class BaseAppModule {
    @Provides
    internal fun BaseAppModule(): BaseApp {
        return BaseApp.instance
    }
}
