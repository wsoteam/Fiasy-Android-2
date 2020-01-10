package com.wsoteam.diet.presentation.training.training


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.concat
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.R
import com.wsoteam.diet.common.Analytics.EventProperties
import com.wsoteam.diet.presentation.training.Training
import kotlinx.android.synthetic.main.fragment_training_blocked.*


class TrainingBlockedFragment : Fragment(R.layout.fragment_training_blocked) {

    private var training: Training? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        activity?.window?.statusBarColor = resources.getColor(R.color.toolbar_title)

        toolbarTBF.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbarTBF.setNavigationOnClickListener { activity?.onBackPressed() }
        goPrem.setOnClickListener { startPremium() }

        arguments?.apply {
            training = getParcelable<Training>(Training().javaClass.simpleName)
            training?.apply { updateUi(this) }
        }
    }

    private fun updateUi(training: Training){

        val days = 5
        val progressMax = 16
        val progressCurrent = 5

        setImg(imageTBF, training.url)
        daysTBF.text = concat(days.toString(), " ", resources.getQuantityText(R.plurals.day_plurals, days))
        progressTBF.text = String.format(getString(R.string.training_progress), progressCurrent, progressMax )
        nameTBF.text = training.name

    }

    private fun setImg(img: ImageView, url: String?) {
        if(url == null) return
        Picasso.get()
                .load(url)
                .into(img)
    }
    private fun startPremium(){
        val box = Box()
        box.isSubscribe = false
        box.isOpenFromPremPart = true
        box.isOpenFromIntrodaction = false
        box.comeFrom = AmplitudaEvents.view_prem_content
        box.buyFrom = EventProperties.trial_from_articles //TODO поменять ивент проперти
        val intent = Intent(context, ActivitySubscription::class.java).putExtra(Config.TAG_BOX, box)
        startActivity(intent)
    }
}
