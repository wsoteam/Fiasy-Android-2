package com.losing.weight.presentation.profile.settings.controller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.losing.weight.AmplitudaEvents
import com.losing.weight.Authenticate.POJO.Box
import com.losing.weight.Config
import com.losing.weight.EntryPoint.ActivitySplash
import com.losing.weight.InApp.ActivitySubscription
import com.losing.weight.OtherActivity.AnyFragmentActivity
import com.losing.weight.R
import com.losing.weight.Sync.UserDataHolder
import com.losing.weight.common.Analytics.EventProperties
import com.losing.weight.common.Analytics.Events
import com.losing.weight.presentation.auth.AuthStrategy
import com.losing.weight.presentation.profile.about.AboutActivity
import com.losing.weight.presentation.profile.help.HelpActivity
import com.losing.weight.presentation.profile.norm.BlockedNormFragment
import com.losing.weight.presentation.profile.norm.ChangeNormActivity
import com.losing.weight.presentation.promo.PromoFormActivity
import com.losing.weight.utils.IntentUtils
import java.util.*

class ItemsAdapterKt(val context: Context, isNotPrem: Boolean): RecyclerView.Adapter<ItemsViewHolders>() {

    val PREMIUM = Pair(R.string.premium_account, R.drawable.ic_left_settings_diamond)
    val PROMO = Pair(R.string.promotional_codes, R.drawable.ic_left_settings_item_promo)
    val PERSONAL = Pair(R.string.personal_data, R.drawable.ic_left_settings_item_man)
    val KCAL = Pair(R.string.calorie_intake, R.drawable.ic_left_settings_item_kcal_en)
    val HELP = Pair(R.string.help_activity_title, R.drawable.ic_left_settings_item_help)
    val POLICY = Pair(R.string.policy_action_title, R.drawable.ic_mdi_ballot)
    val LOGOUT = Pair(R.string.exit, R.drawable.ic_left_settings_item_exit)


    private val listItems = mutableListOf(PREMIUM, PROMO, PERSONAL, KCAL, HELP, POLICY, LOGOUT)

    init {
        if (!isNotPrem) listItems.remove(PREMIUM)
        if (Locale.getDefault().language != "ru") listItems.remove(HELP)
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true) listItems.remove(LOGOUT)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemsViewHolders
            = ItemsViewHolders(LayoutInflater.from(context), viewGroup)

    override fun onBindViewHolder(itemsViewHolders: ItemsViewHolders, position: Int) {
        itemsViewHolders.bind(context.getString(listItems[position].first),
                listItems[position].second,
                ContextCompat.getColor(context, getTextColorForPosition(position))
        )
    }


    private fun getTextColorForPosition(position: Int) = when(listItems[position]){
        PREMIUM -> R.color.prem_settings
        LOGOUT -> R.color.logout_settings
        else -> R.color.unprem_settings
    }


    override fun getItemCount() = listItems.size

    override fun onViewAttachedToWindow(holder: ItemsViewHolders) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener { goToItemSettings(holder.adapterPosition) }
    }

    override fun onViewDetachedFromWindow(holder: ItemsViewHolders) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }


    private fun goToItemSettings(position: Int) {
        when (listItems[position]) {
            PREMIUM -> openPremium()
            PROMO -> context.startActivity(Intent(context, PromoFormActivity::class.java))
            PERSONAL -> context.startActivity(Intent(context, AboutActivity::class.java))
            KCAL -> openNormal()
            HELP -> context.startActivity(Intent(context, HelpActivity::class.java))
            LOGOUT -> openLogOutAlert()
            POLICY -> openPolicy()
        }
    }

    private fun exitUser() {
        AuthStrategy.signOut(context)
        UserDataHolder.clearObject()
        val intent = Intent(context, ActivitySplash::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    private fun openNormal(){
        if(FirebaseAuth.getInstance().currentUser?.isAnonymous == true){
            context.startActivity(AnyFragmentActivity.getIntent(context, BlockedNormFragment.newInstance()))
        }else{
            context.startActivity(Intent(context, ChangeNormActivity::class.java))
        }
    }

    private fun openPolicy(){
        IntentUtils.openWebLink(context, context.getString(R.string.url_privacy_police))
    }

    private fun openPremium(){
        val box = Box()
        box.comeFrom = AmplitudaEvents.view_prem_settings
        box.buyFrom = EventProperties.trial_from_settings
        box.isOpenFromPremPart = true
        box.isOpenFromIntrodaction = false
        val intent = Intent(context, ActivitySubscription::class.java)
        intent.putExtra(Config.TAG_BOX, box)
        context.startActivity(intent)
    }

    private fun openLogOutAlert(){
        Events.logLogout()
        AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.exit))
                .setMessage(context.getString(R.string.log_off_question))
                .setPositiveButton(context.getString(R.string.yes)) { _, _-> exitUser() }
                .setNegativeButton(context.getString(R.string.no), null)
                .show()
    }
}