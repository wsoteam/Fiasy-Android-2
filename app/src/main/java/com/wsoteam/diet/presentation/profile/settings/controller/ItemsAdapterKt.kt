package com.wsoteam.diet.presentation.profile.settings.controller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wsoteam.diet.AmplitudaEvents
import com.wsoteam.diet.Authenticate.POJO.Box
import com.wsoteam.diet.Config
import com.wsoteam.diet.EntryPoint.ActivitySplash
import com.wsoteam.diet.InApp.ActivitySubscription
import com.wsoteam.diet.OtherActivity.AnyFragmentActivity
import com.wsoteam.diet.R
import com.wsoteam.diet.Sync.UserDataHolder
import com.wsoteam.diet.common.Analytics.EventProperties
import com.wsoteam.diet.common.Analytics.Events
import com.wsoteam.diet.presentation.auth.AuthStrategy
import com.wsoteam.diet.presentation.profile.about.AboutActivity
import com.wsoteam.diet.presentation.profile.help.HelpActivity
import com.wsoteam.diet.presentation.profile.norm.BlockedNormFragment
import com.wsoteam.diet.presentation.profile.norm.ChangeNormActivity
import com.wsoteam.diet.presentation.promo.PromoFormActivity
import java.util.*

class ItemsAdapterKt(val context: Context, isNotPrem: Boolean): RecyclerView.Adapter<ItemsViewHolders>() {

    val PREMIUM = Pair(R.string.premium_account, R.drawable.ic_left_settings_diamond)
    val PROMO = Pair(R.string.promotional_codes, R.drawable.ic_left_settings_item_promo)
    val PERSONAL = Pair(R.string.personal_data, R.drawable.ic_left_settings_item_man)
    val KCAL = Pair(R.string.calorie_intake, R.drawable.ic_left_settings_item_kcal_en)
    val HELP = Pair(R.string.help_activity_title, R.drawable.ic_left_settings_item_help)
    val LOGOUT = Pair(R.string.exit, R.drawable.ic_left_settings_item_exit)


    private val listItems = mutableListOf(PREMIUM, PROMO, PERSONAL, KCAL, HELP, LOGOUT)

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
                context.resources.getColor(getTextColorForPosition(position)),
                getDrawableArrowForPosition(position),
                (position == listItems.size - 1)
        )
    }


    private fun getTextColorForPosition(position: Int) = when(listItems[position]){
        PREMIUM -> R.color.prem_settings
        LOGOUT -> R.color.logout_settings
        else -> R.color.unprem_settings
    }

    private fun getDrawableArrowForPosition(position: Int) = when(listItems[position]){
        PREMIUM -> R.drawable.ic_settings_item_prem
        LOGOUT -> R.drawable.ic_settings_item_arrow_normal
        else -> R.drawable.ic_settings_item_arrow_normal
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