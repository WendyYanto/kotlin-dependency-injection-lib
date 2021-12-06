package dev.wendyyanto.manual_di_sample.utils

import dev.wendyyanto.dependency_lib.annotation.Inject

class StringUtils @Inject constructor(private val appUtils: AppUtils) {

  fun test(): String {
    return "StringUtils by ${appUtils.getAppId()}"
  }
}