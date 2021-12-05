package dev.wendyyanto.manual_di_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import dev.wendyyanto.dependency_lib.annotation.Inject
import dev.wendyyanto.dependency_lib.di.Injectors
import dev.wendyyanto.manual_di_sample.module.MainModule
import dev.wendyyanto.manual_di_sample.presenter.MainPresenter
import dev.wendyyanto.manual_di_sample.utils.StringUtils

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var mainPresenter: MainPresenter

  @Inject
  lateinit var stringUtils: StringUtils

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Injectors.inject(
      MainModule::class,
      this
    )

    setContentView(R.layout.activity_main)

    Toast.makeText(this, "ID: ${mainPresenter.getId()}", Toast.LENGTH_SHORT).show()

    findViewById<TextView>(R.id.tv_test).setOnClickListener {
      Toast.makeText(this, stringUtils.test(), Toast.LENGTH_SHORT).show()
    }
  }
}