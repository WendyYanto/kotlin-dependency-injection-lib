package dev.wendyyanto.manual_di_sample.module

import dev.wendyyanto.manual_di_sample.annotation.EntryPoint
import dev.wendyyanto.manual_di_sample.annotation.Provides
import dev.wendyyanto.manual_di_sample.di.InjectorModule
import dev.wendyyanto.manual_di_sample.presenter.MainPresenter
import dev.wendyyanto.manual_di_sample.presenter.MainPresenterImpl
import dev.wendyyanto.manual_di_sample.utils.MainUtils
import dev.wendyyanto.manual_di_sample.utils.Signature
import dev.wendyyanto.manual_di_sample.utils.StringUtils

class MainModule: InjectorModule {

  @Provides
  fun provideMainUtils(signature: Signature): MainUtils {
    return MainUtils(signature)
  }

  @Provides
  fun provideSignature(): Signature {
    return Signature()
  }

  @Provides
  @EntryPoint
  fun provideMainPresenter(mainUtils: MainUtils, stringUtils: StringUtils): MainPresenter {
    return MainPresenterImpl(mainUtils, stringUtils)
  }

  @Provides
  fun provideStringUtils(): StringUtils {
    return StringUtils()
  }
}