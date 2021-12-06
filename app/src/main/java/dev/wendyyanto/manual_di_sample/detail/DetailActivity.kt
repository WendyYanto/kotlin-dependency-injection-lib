package dev.wendyyanto.manual_di_sample.detail

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.wendyyanto.dependency_lib.di.Injectors
import dev.wendyyanto.manual_di_sample.R
import dev.wendyyanto.manual_di_sample.detail.module.DetailModule

class DetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Injectors.inject(
      DetailModule::class,
      this
    )

    setContentView(R.layout.activity_main)

    findViewById<TextView>(R.id.tv_test).text = "Detail Page"
  }
}