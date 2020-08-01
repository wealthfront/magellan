package com.wealthfront.magellan.sample

import android.content.Context
import com.wealthfront.magellan.navigation.LoggingNavigableListener
import com.wealthfront.magellan.navigation.NavigationTraverser
import com.wealthfront.magellan.view.MenuProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

  @Provides
  @Singleton
  fun provideExpedition(): Expedition {
    return Expedition()
  }

  @Provides
  @Singleton
  fun provideNavigationTraverser(expedition: Expedition): NavigationTraverser {
    return NavigationTraverser(expedition)
  }

  @Provides
  @Singleton
  fun provideLoggingNavigableListener(navigationTraverser: NavigationTraverser): LoggingNavigableListener {
    return LoggingNavigableListener(navigationTraverser)
  }

  @Provides
  @Singleton
  fun provideMenuProvider(): MenuProvider {
    return MenuProvider()
  }

  @Provides
  fun provideContext() = context
}
