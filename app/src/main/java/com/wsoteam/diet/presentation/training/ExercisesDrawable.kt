package com.wsoteam.diet.presentation.training

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.wsoteam.diet.R

class ExercisesDrawable{
    companion object{

        fun setImage(type: String?, context: Context, imageView: ImageView): AnimatedVectorDrawableCompat?{
            if(mapGif.containsKey(type)){
                Glide.with(context)
                        .load(mapGif[type])
                        .into(imageView)
                return null
            } else{
                try {
                    val animated = AnimatedVectorDrawableCompat.create(context, get(type))
                    animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            imageView?.post { animated?.start() }
                        }

                    })
                    imageView?.setImageDrawable(animated)
                    animated?.start()

                    return animated
                }catch (e: Exception){
                    e.printStackTrace()
                    return null
                }

            }

        }

        fun get(key: String?): Int = map[key] ?: R.drawable.btn_elements_prem


        private val map = mapOf(
                "advanced_scissor_kicks" to R.drawable.advanced_scissor_kicks,
                "alternating_superman" to R.drawable.alternating_superman,
                "balanced_crunch" to R.drawable.balanced_crunch,
                "bent_knee_raise" to R.drawable.straight_leg_dead_lifts,
                "butt_lift_bridge" to R.drawable.butt_lift_bridge,
                "floor_back_extension" to R.drawable.floor_back_extension,
                "flutter_kicks" to R.drawable.flutter_kicks,
                "glute_kickback" to R.drawable.glute_kickback,
                "inner_thigh_lifts" to R.drawable.inner_thigh_lifts,
                "lying_back_extension" to R.drawable.lying_back_extension,
                "lying_butterfly" to R.drawable.lying_butterfly,
                "lying_leg_raise" to R.drawable.lying_leg_raise,
                "lying_side_leg_ift" to R.drawable.lying_side_leg_lift,
                "mountain_climbers" to R.drawable.mountain_climbers,
                "plank" to R.drawable.exercise_wall_push_up,
                "single_leg_push_up" to R.drawable.single_leg_push_up,
                "squat" to R.drawable.squat,
                "standing_leg_kick_back" to R.drawable.standing_leg_kick_back,
                "sumo_squats" to R.drawable.sumo_squats,
                "wall_push_up" to R.drawable.wall_push_up
        )

        val mapGif = mapOf(
                "advanced_scissor_kicks" to "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/exercises%2FAdvanced%20Scissor%20Kicks.gif?alt=media&token=a3a819fe-d743-4dce-bde5-d858d0e2c470",
                "bent_knee_raise" to "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/exercises%2FStraight%20Leg%20Dead%20Lifts.gif?alt=media&token=59364d30-b125-4637-951e-1d196ff5ae42",
                "butt_lift_bridge" to "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/exercises%2FButt%20Lift%20Bridge.gif?alt=media&token=dd9d77d3-439d-4b6c-8b23-d6afadefef7e",
                "lying_leg_raise" to "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/exercises%2FLying%20Leg%20Raise1.gif?alt=media&token=a9585d74-8f4b-4386-a447-cc67b56d2676",
                "squat" to "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/exercises%2FSquat1.gif?alt=media&token=3f973868-a06c-43ef-b078-8dfc008cda97",
                "mountain_climbers" to "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/exercises%2FMountain%20Climbers.gif?alt=media&token=72874282-c253-4082-a40f-59b4f9cff855"
        )
    }
}