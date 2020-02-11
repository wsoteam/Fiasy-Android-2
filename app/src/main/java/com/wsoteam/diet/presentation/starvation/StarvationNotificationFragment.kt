package com.wsoteam.diet.presentation.starvation


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.wsoteam.diet.R
import com.wsoteam.diet.presentation.starvation.notification.AlarmNotificationReceiver
import kotlinx.android.synthetic.main.fragment_starvation_notification.*
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.os.Build
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_fragment_abort_exercise.*


class StarvationNotificationFragment : Fragment() {

    private var nDialog : AlertDialog? = null
    private var isSettingOpen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_starvation_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar3.setNavigationOnClickListener { fragmentManager?.popBackStack() }

        switchBasic.isChecked = SharedPreferencesUtility.isBasicNotification(context)
        switchAdvance.isChecked = SharedPreferencesUtility.isAdvanceNotification(context)

        switchBasic.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtility.setBasicNotification(context, isChecked)
        }

        switchAdvance.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferencesUtility.setAdvanceNotification(context, isChecked)
        }
    }

    override fun onDetach() {
        super.onDetach()

        AlarmNotificationReceiver.update(context)
    }

    private fun checkNotification(context: Context?) {
        context?.apply {
            val mNotificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
            val areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled()
            Log.d("kkk", "check = ${!areNotificationsEnabled}")
            if (!areNotificationsEnabled) notificationDialog(context)
        }
    }

    private fun openNotificationSettingsForApp(context: Context) {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                intent.action = ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(EXTRA_APP_PACKAGE, context.packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            }
            else -> {
                intent.action = ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + context.packageName)
            }
        }
        context.startActivity(intent)
    }

    private fun notificationDialog(context: Context): AlertDialog? {

        if (nDialog == null) {
            Log.d("kkk", "nDialog == null")
            val dialog = AlertDialog.Builder(context)
                    .setTitle(R.string.starvation_notification_alert_title)
                    .setMessage(R.string.starvation_notification_alert_txt)
                    .setPositiveButton(R.string.starvation_setting) { _, _ ->
                        nDialog = null
                        isSettingOpen = true
                        openNotificationSettingsForApp(context)
                    }
                    .setNegativeButton(R.string.starvation_cancle) { _, _ ->
                        nDialog = null
                        fragmentManager?.popBackStack()
                    }
                    .create()

            dialog.setCanceledOnTouchOutside(false)

            dialog.show()


            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setTextColor(Color.parseColor("#8a000000"))

            dialog.setOnCancelListener {
                Log.d("kkk", "setOnCancelListener")
                nDialog = null

                fragmentManager?.popBackStack()

            }

            nDialog = dialog
            return dialog
        } else {
            Log.d("kkk", "nDialog != null")
            return null
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("kkk", "res++++")
        checkNotification(context)
    }
}
