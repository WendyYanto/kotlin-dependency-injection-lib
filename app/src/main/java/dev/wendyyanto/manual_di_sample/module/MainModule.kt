package dev.wendyyanto.manual_di_sample.module

import dev.wendyyanto.dependency_lib.annotation.Provides
import dev.wendyyanto.dependency_lib.di.InjectorModule
import dev.wendyyanto.manual_di_sample.presenter.MainPresenter
import dev.wendyyanto.manual_di_sample.presenter.MainPresenterImpl
import dev.wendyyanto.manual_di_sample.utils.AppUtils
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
  fun provideMainPresenter(mainUtils: MainUtils, stringUtils: StringUtils): MainPresenter {
    return MainPresenterImpl(mainUtils, stringUtils)
  }

  @Provides
  fun provideStringUtils(appUtils: AppUtils): StringUtils {
    return StringUtils(appUtils)
  }
}