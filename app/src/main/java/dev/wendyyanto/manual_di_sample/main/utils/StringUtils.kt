package dev.wendyyanto.manual_di_sample.main.utils

import dev.wendyyanto.dependency_lib.annotation.Inject
import dev.wendyyanto.manual_di_sample.common_utils.AppUtils

class StringUtils @Inject constructor(private val appUtils: AppUtils) {

  fun test(): String {
    return "StringUtils by ${appUtils.getAppId()}"
  }
}