package com.losing.weight.presentation.premium

import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.losing.weight.Authenticate.POJO.Box
import com.losing.weight.Config
import com.losing.weight.InApp.ActivitySubscription
import com.losing.weight.R
import kotlinx.android.synthetic.main.dialog_fragment_fifty_discount.*

class FiftyDiscountDialogFragment : DialogFragment() {

  companion object {
    private const val FRAGMENT_TAG = "new_training_dialog"

    fun newInstance(box: Box): FiftyDiscountDialogFragment {
      val bundle = Bundle()
      bundle.putSerializable("FiftyDiscountDialogFragment", box)
      val fragment = FiftyDiscountDialogFragment()
      fragment.arguments = bundle
      return fragment
    }

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.dialog_fragment_fifty_discount, container, false)

  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    val box = arguments?.getSerializable("FiftyDiscountDialogFragment")
    next.setOnClickListener {
      val intent = Intent(activity, ActivitySubscription::class.java)
      intent.putExtra(Config.TAG_BOX, box)
      startActivity(intent)

      dismiss()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    activity?.finish()
  }
}
