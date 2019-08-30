package com.wsoteam.diet.utils

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

fun Fragment.args(): Bundle {
  if (arguments == null) {
    arguments = Bundle()
  }

  return arguments!!
}

inline fun <T> Fragment.argument() = FragmentArgument<T>()

class FragmentArgument<T> : ReadWriteProperty<Fragment, T?> {

  @Suppress("IMPLICIT_CAST_TO_ANY")
  override fun getValue(target: Fragment, property: KProperty<*>): T? {
    val bundle = target.args()

    return when(property.returnType){
      Parcelable::class -> bundle.getParcelable<Parcelable>(property.name)
      Boolean::class -> bundle.getBoolean(property.name)

      else -> throw IllegalStateException("cannot deserialize ${property.returnType}")
    } as T?
  }

  override fun setValue(target: Fragment, property: KProperty<*>, value: T?) {
    val bundle = target.args()

    when(property.returnType){
      Parcelable::class -> bundle.putParcelable(property.name, value as Parcelable)
      Boolean::class -> bundle.putBoolean(property.name, value as Boolean)

      else -> throw IllegalStateException("cannot serialize ${property.returnType}")
    }
  }
}