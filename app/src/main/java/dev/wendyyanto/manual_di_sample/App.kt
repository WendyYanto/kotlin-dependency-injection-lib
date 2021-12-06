package dev.wendyyanto.manual_di_sample

import android.app.Application
import dev.wendyyanto.dependency_lib.di.Injectors
import dev.wendyyanto.manual_di_sample.module.AppModule

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    Injectors.injectApp(AppModule::class)
  }
}