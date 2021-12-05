package dev.wendyyanto.manual_di_sample.presenter

import android.util.Log
import dev.wendyyanto.manual_di_sample.annotation.Inject
import dev.wendyyanto.manual_di_sample.utils.MainUtils
import dev.wendyyanto.manual_di_sample.utils.StringUtils

class MainPresenterImpl @Inject constructor(
  private val mainUtils: MainUtils,
  private val stringUtils: StringUtils
) : MainPresenter {

  init {
    Log.v("INJECT", "created MainPresenter")
  }

  override fun getId(): String {
    return mainUtils.formatValue("ID") + " " + stringUtils.test()
  }
}