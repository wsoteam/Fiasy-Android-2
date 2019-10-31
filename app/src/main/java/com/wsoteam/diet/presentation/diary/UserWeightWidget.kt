package com.wsoteam.diet.presentation.diary

import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder

class UserWeightWidget(itemView: View) : WidgetsAdapter.WidgetView(itemView), OnClickListener {
  private val weightLabel = itemView.findViewById<TextView>(R.id.weight)
  private val updatedLabel = itemView.findViewById<TextView>(R.id.last_update)
  private val actionRefresh = itemView.findViewById<TextView>(R.id.action_refresh)

  override fun onClick(v: View) {
    when (v.id) {
      R.id.action_refresh -> updateWeight()
    }
  }

  fun updateWeight() {
    UserDataHolder.getUserData()?.profile?.let {
      weightLabel.text = "${it.weight} кг"
    }
  }

  override fun onBind(parent: RecyclerView, position: Int) {
    super.onBind(parent, position)

    updateWeight()
  }

  override fun onAttached(parent: RecyclerView) {
    super.onAttached(parent)
    actionRefresh.setOnClickListener(this)
  }

  override fun onDetached(parent: RecyclerView) {
    super.onDetached(parent)
    actionRefresh.setOnClickListener(null)
  }
}
