package dev.wendyyanto.manual_di_sample.detail.module

import dev.wendyyanto.dependency_lib.annotation.Provides
import dev.wendyyanto.dependency_lib.di.InjectorModule
import dev.wendyyanto.manual_di_sample.common_utils.AppUtils
import dev.wendyyanto.manual_di_sample.detail.presenter.DetailPresenter
import dev.wendyyanto.manual_di_sample.detail.presenter.DetailPresenterImpl

class DetailModule : InjectorModule {

  @Provides
  fun provideDetailPresenter(appUtils: AppUtils): DetailPresenter {
    return DetailPresenterImpl(appUtils)
  }
}