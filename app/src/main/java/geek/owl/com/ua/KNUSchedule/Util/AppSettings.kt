package geek.owl.com.ua.KNUSchedule.Util

import android.content.Context
import android.content.SharedPreferences

class AppSettings : SharedPreferences {
  private val userSettings: SharedPreferences = geek.owl.com.ua.KNUSchedule.AppClass.INSTANCE.getSharedPreferences("KNU", Context.MODE_PRIVATE)

  override fun getBoolean(p0: String?, p1: Boolean): Boolean {
    return userSettings.getBoolean(p0, p1)
  }

  override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
    return userSettings.unregisterOnSharedPreferenceChangeListener(p0)
  }

  override fun getInt(p0: String?, p1: Int): Int {
    return userSettings.getInt(p0, p1)
  }

  override fun getAll(): MutableMap<String, *> {
    return userSettings.all
  }

  override fun edit(): SharedPreferences.Editor {
    return userSettings.edit()
  }

  override fun getLong(p0: String?, p1: Long): Long {
    return userSettings.getLong(p0, p1)
  }

  override fun getFloat(p0: String?, p1: Float): Float {
    return userSettings.getFloat(p0, p1)
  }

  override fun getStringSet(p0: String?, p1: MutableSet<String>?): MutableSet<String>? {
    return userSettings.getStringSet(p0, p1)
  }

  override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
    userSettings.registerOnSharedPreferenceChangeListener(p0)
  }

  override fun getString(p0: String?, p1: String?): String? {
    return userSettings.getString(p0, p1)
  }

  override fun contains(p0: String?): Boolean {
    return userSettings.contains(p0)
  }

}