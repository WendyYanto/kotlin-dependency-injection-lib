package dev.wendyyanto.manual_di_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dev.wendyyanto.manual_di_sample.annotation.Inject
import dev.wendyyanto.manual_di_sample.di.Injectors
import dev.wendyyanto.manual_di_sample.module.MainModule
import dev.wendyyanto.manual_di_sample.presenter.MainPresenter

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var mainPresenter: MainPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Injectors.inject(
      kClass = MainModule::class,
      entryPointClazz = this)

    setContentView(R.layout.activity_main)

    Toast.makeText(this, "ID: ${mainPresenter.getId()}", Toast.LENGTH_SHORT).show()
  }
}