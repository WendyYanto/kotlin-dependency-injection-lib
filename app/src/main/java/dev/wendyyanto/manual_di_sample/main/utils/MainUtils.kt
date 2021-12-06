package dev.wendyyanto.manual_di_sample.main.utils

class MainUtils constructor(private val signature: Signature) {

  fun formatValue(value: String): String {
    return "formatted $value by ${signature.get()}"
  }
}