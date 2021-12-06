package dev.wendyyanto.manual_di_sample.main.presenter

import dev.wendyyanto.dependency_lib.annotation.Inject
import dev.wendyyanto.manual_di_sample.main.utils.MainUtils
import dev.wendyyanto.manual_di_sample.main.utils.StringUtils

class MainPresenterImpl @Inject constructor(
  private val mainUtils: MainUtils,
  private val stringUtils: StringUtils
) : MainPresenter {

  override fun getId(): String {
    return mainUtils.formatValue("ID") + " " + stringUtils.test()
  }
}