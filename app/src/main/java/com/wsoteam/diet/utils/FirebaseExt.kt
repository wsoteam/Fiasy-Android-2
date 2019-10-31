package com.wsoteam.diet.utils

import com.google.firebase.database.DataSnapshot

inline fun <reified T> DataSnapshot.valueOf(childName: String): T? {
  return child(childName).getValue(T::class.java)
}