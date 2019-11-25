package com.wsoteam.diet.presentation.premium

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.wsoteam.diet.R
import com.wsoteam.diet.common.Analytics.Events
import com.wsoteam.diet.presentation.premium.PremiumFeaturesActivity.Companion
import com.wsoteam.diet.presentation.premium.PremiumFeaturesActivity.PlanHolder
import com.wsoteam.diet.presentation.premium.PremiumFeaturesActivity.PlansAdapter
import com.wsoteam.diet.utils.IntentUtils
import com.wsoteam.diet.utils.RichTextUtils.RichText
import com.wsoteam.diet.utils.dp
import io.reactivex.disposables.CompositeDisposable

class SubscriptionsListFragment : Fragment() {

  private var selectedId: Int = -1
  private val withTrial: Boolean
    get() = FirebaseRemoteConfig.getInstance().getBoolean("premium_with_trial")

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.activity_premium_subscriptions_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener {
      requireFragmentManager().popBackStack()
      Events.logPushButton("back", "premium")
    }

    val title = view.findViewById<TextView>(R.id.title)

    title.text = TextUtils.concat(title.text,
        RichText(getString(R.string.subscriptions_list_title__premium))
          .bold()
          .colorRes(view.context, R.color.orange_light)
          .text())

    val container = view.findViewById<RecyclerView>(R.id.container)

    val isDarkMode = "dark" == FirebaseRemoteConfig.getInstance().getString("premium_theme")

    container.adapter = object : PlansAdapter(isDarkMode = isDarkMode, withTrial = withTrial) {
      override fun getLayoutParams(parent: ViewGroup, viewType: Int): LayoutParams {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
          .apply {
            bottomMargin = dp(parent.context, 16f)
            leftMargin = dp(parent.context, 18f)
            rightMargin = dp(parent.context, 18f)
          }
      }

      override fun onBindViewHolder(holder: PlanHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        if (position == selectedId) {
          holder.view.isSelected = true
        }
      }

      override fun onViewAttachedToWindow(holder: PlanHolder) {
        super.onViewAttachedToWindow(holder)

        holder.view.setOnClickListener {
          if (selectedId == holder.adapterPosition) {
            return@setOnClickListener
          }

          it.isSelected = true

          if (selectedId >= 0) {
            container.findViewHolderForAdapterPosition(selectedId)?.let { vh ->
              vh.itemView.isSelected = false
            }
          }

          selectedId = holder.adapterPosition

          (requireActivity() as PremiumFeaturesActivity)
            .purchase(PremiumFeaturesActivity.plans[selectedId].key)
        }
      }

      override fun onViewDetachedFromWindow(holder: PlanHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.view.setOnClickListener(null)
      }
    }
  }
}
