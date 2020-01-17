package com.wsoteam.diet.presentation.training.training


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.concat
import androidx.fragment.app.Fragment
import android.view.View
import com.squareup.picasso.Picasso
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.R
import com.wsoteam.diet.common.Analytics.EventProperties
import com.wsoteam.diet.presentation.training.OnBackPressed
import com.wsoteam.diet.presentation.training.Training
import com.wsoteam.diet.presentation.training.TrainingViewModel
import kotlinx.android.synthetic.main.fragment_training_blocked.*


class TrainingBlockedFragment : Fragment(R.layout.fragment_training_blocked), OnBackPressed {

    companion object{
        private const val TRAINING_BUNDLE_KEY = "TRAINING_B_BUNDLE_KEY"
        fun newInstance(training: Training?) :TrainingBlockedFragment{
            val fragment = TrainingBlockedFragment()
            val bundle = Bundle()
            bundle.putParcelable(TRAINING_BUNDLE_KEY, training)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var training: Training? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarTBF.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbarTBF.setNavigationOnClickListener { fragmentManager?.popBackStack() }
        goPrem.setOnClickListener { startPremium() }

        arguments?.apply {
            training = getParcelable<Training>(TRAINING_BUNDLE_KEY)
            training?.apply { updateUi(this) }
        }
    }

    private fun updateUi(training: Training){

        val days = training.days?.size ?: 0
        val progressMax = training.days?.size ?: 28
        val progressCurrent = TrainingViewModel.getTrainingResult().value?.get(training.uid)?.finishedDays ?: 0

        daysTBF.text = concat(days.toString(), " ", resources.getQuantityText(R.plurals.day_plurals, days))
        progressTBF.text = String.format(getString(R.string.training_progress), progressCurrent, progressMax )
        nameTBF.text = training.name

        Picasso.get()
                .load(training.url)
                .into(imageTBF)

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

    override fun onBackPressed() {
        fragmentManager?.popBackStack()
    }
}
