package com.wsoteam.diet.presentation.premium

import android.animation.Animator
import android.os.Bundle
import com.wsoteam.diet.R
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_wheel_fortune.*
import kotlin.random.Random


class WheelFortuneActivity : AppCompatActivity(R.layout.activity_wheel_fortune) {

    val dialogFragment = FiftyDiscountDialogFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startWheel.setOnClickListener {

            val round = 3
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
                    FiftyDiscountDialogFragment.newInstance()
                            .show(supportFragmentManager, dialogFragment.javaClass.name)
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
}
