package com.wsoteam.diet.presentation.premium

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.common.Analytics.EventProperties
import kotlinx.android.synthetic.main.fragment_wheel_fortune.*
import kotlin.random.Random

class WheelFortuneFragment : AppCompatActivity(R.layout.fragment_wheel_fortune) {

  private val fragment = this
  private val requestCode = R.layout.fragment_wheel_fortune

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startWheel.setOnClickListener {

      val round = Random.nextInt(1, 3)
      val position = Random.nextInt(45, 82)

      val animation =
        ObjectAnimator.ofFloat(arrow_fortune, "rotation", 0.0f, (360f * round) + position)
      animation.duration = 2400L * round
      animation.repeatCount = 0
      animation.interpolator = AccelerateDecelerateInterpolator()
      animation.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
          val fm = supportFragmentManager
          val dialogFragment = FiftyDiscountDialogFragment.newInstance()
          //dialogFragment.setTargetFragment(fragment, requestCode)
          fm?.let { dialogFragment.show(fm, dialogFragment.javaClass.name) }
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }
      })
      animation.start()
      startWheel.isEnabled = false
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        requestCode -> {
          var box = Box()
          box.isOpenFromPremPart = false
          box.isOpenFromIntrodaction = true
          box.buyFrom = EventProperties.trial_from_wheel
          box.comeFrom = EventProperties.trial_from_wheel
          var intent = Intent(this, ActivitySubscription::class.java)
          intent.putExtra(Config.TAG_BOX, box)
          startActivity(intent)
        }
      }

    }
  }
}
