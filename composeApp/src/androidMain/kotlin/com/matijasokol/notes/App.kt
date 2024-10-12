package com.matijasokol.notes

import android.app.Application
import com.matijasokol.notes.di.coreModule
import com.matijasokol.notes.di.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

  override fun onCreate() {
    super.onCreate()

    initKoin {
      androidContext(this@App)
      modules(coreModule)
    }
  }
}
