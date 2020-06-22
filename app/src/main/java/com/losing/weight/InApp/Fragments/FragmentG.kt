package com.losing.weight.InApp.Fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.losing.weight.Authenticate.POJO.Box
import com.losing.weight.InApp.BasePremiumFragment
import com.losing.weight.R
import com.losing.weight.utils.RichTextUtils
import com.losing.weight.utils.formatSpannable
import kotlinx.android.synthetic.main.fragment_g.*


class FragmentG : BasePremiumFragment(R.layout.fragment_g) {

    companion object {
        @JvmStatic
        fun newInstance(box: Box?) : Fragment{
            val bundle = Bundle()
            bundle.putSerializable(TAG_BOX, box);
            val fragment = FragmentG()
            fragment.arguments = bundle;
            return fragment
        }
    }

    override fun getCurrentSKU(): String = "premium"
    override fun setPrice(sku: com.android.billingclient.api.SkuDetails?) {
        sku?.also {
            priceText.text = getString(R.string.mount_premium, it.price)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardViewPremG.setBackgroundResource(R.drawable.prem_card_backgound)
        premiumGClose.setOnClickListener {
            activity?.onBackPressed()
        }

        buyPremiumG.setOnClickListener {
            buy(getCurrentSKU())

        }

        val actionSignIn = RichTextUtils.RichText(getString(R.string.premium_feature_title__span).toUpperCase())
                .bold()
        val spannable = getString(R.string.premium_text_g).formatSpannable(actionSignIn.text())
        prem_g_main_txt.text = spannable
    }
}