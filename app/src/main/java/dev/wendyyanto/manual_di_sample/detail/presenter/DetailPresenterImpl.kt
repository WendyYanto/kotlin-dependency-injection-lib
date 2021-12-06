package dev.wendyyanto.manual_di_sample.detail.presenter

import dev.wendyyanto.dependency_lib.annotation.Inject
import dev.wendyyanto.manual_di_sample.common_utils.AppUtils

class DetailPresenterImpl @Inject constructor(private val appUtils: AppUtils) : DetailPresenter {

  override fun getDetail(): String {
    return "getDetail with ${appUtils.getAppId()}"
  }
}