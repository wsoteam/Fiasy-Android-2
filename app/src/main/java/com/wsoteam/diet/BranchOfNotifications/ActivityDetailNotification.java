package com.wsoteam.diet.BranchOfNotifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.POJOForDB.ObjectForNotification;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Services.AlarmManagerBR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ActivityDetailNotification extends AppCompatActivity {
    private EditText edtText, edtDate, edtTime, edtRepeat;
    private ImageView ivChoiseIconForNotification;
    private Button btnSave;
    private ObjectForNotification objectForNotification;
    final static String TAG_OF_ACTIVITY = "ActivityDetailNotification";
    private int idOfSelectedItem = 0, idROfSelectedIcon = R.drawable.list_of_choise_notification_icon_1;
    private ArrayList<ObjectForNotification> notificationArrayList;
    private long tempId;
    private AlertDialog choiseIconAlertDialog;
    InterstitialAd interstitialAd;

    private Integer[] urlOfIconsNotifications = new Integer[]{R.drawable.list_of_choise_notification_icon_1,
            R.drawable.list_of_choise_notification_icon_2,
            R.drawable.list_of_choise_notification_icon_3,
            R.drawable.list_of_choise_notification_icon_4,
            R.drawable.list_of_choise_notification_icon_5,
            R.drawable.list_of_choise_notification_icon_6,
            R.drawable.list_of_choise_notification_icon_7,
            R.drawable.list_of_choise_notification_icon_8,
            R.drawable.list_of_choise_notification_icon_9,
            R.drawable.list_of_choise_notification_icon_10,
            R.drawable.list_of_choise_notification_icon_11,
            R.drawable.list_of_choise_notification_icon_12,
            R.drawable.list_of_choise_notification_icon_13,
            R.drawable.list_of_choise_notification_icon_14,
            R.drawable.list_of_choise_notification_icon_15,
            R.drawable.list_of_choise_notification_icon_16,
            R.drawable.list_of_choise_notification_icon_17,
            R.drawable.list_of_choise_notification_icon_18,
            R.drawable.list_of_choise_notification_icon_19,
            R.drawable.list_of_choise_notification_icon_20};


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);

        edtText = findViewById(R.id.edtActivityDetailNotificationTextOfNotification);
        edtDate = findViewById(R.id.edtActivityDetailNotificationDateOfNotification);
        edtTime = findViewById(R.id.edtActivityDetailNotificationTimeOfNotification);
        edtRepeat = findViewById(R.id.edtActivityDetailNotificationRepeatOfNotification);
        btnSave = findViewById(R.id.btnActivityDetailNotificationSaveNotification);
        ivChoiseIconForNotification = findViewById(R.id.ivActivityDetailNotificationIconOfNotification);

        final Calendar calendar = new GregorianCalendar();

        Calendar cal = Calendar.getInstance();

        calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 54);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        idOfSelectedItem = getIntent().getIntExtra(TAG_OF_ACTIVITY, 0);


        //-1 - new object(create), else - open early saved object

        if (idOfSelectedItem == -1) {
            objectForNotification = new ObjectForNotification();

            tempId = cal.getTimeInMillis();
            edtDate.setText(writeWithNull(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)))
                    + "." + writeWithNull(String.valueOf(cal.get(Calendar.MONTH) + 1))
                    + "." + writeWithNull(String.valueOf(cal.get(Calendar.YEAR))));
            edtTime.setText(writeWithNull(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)))
                    + ":" + writeWithNull(String.valueOf(cal.get(Calendar.MINUTE))));
            edtRepeat.setText(getString(R.string.repeat_none));
        } else {
            notificationArrayList = (ArrayList<ObjectForNotification>) ObjectForNotification.listAll(ObjectForNotification.class);
            objectForNotification = notificationArrayList.get(idOfSelectedItem);

            tempId = objectForNotification.getOwnId();
            edtRepeat.setText(objectForNotification.getRepeat());
            edtTime.setText(writeWithNull(String.valueOf(objectForNotification.getHour()))
                    + ":" + writeWithNull(String.valueOf(objectForNotification.getMinute())));
            edtDate.setText(writeWithNull(String.valueOf(objectForNotification.getDay())) + "."
                    + writeWithNull(String.valueOf(objectForNotification.getMonth())) + "."
                    + writeWithNull(String.valueOf(objectForNotification.getYear())));
            edtText.setText(objectForNotification.getText());
            Glide.with(this).load(objectForNotification.getIdROfIcon()).into(ivChoiseIconForNotification);
            idROfSelectedIcon = objectForNotification.getIdROfIcon();
        }

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDateAlertDialog();
            }
        });

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTimeAlertDialog();
            }
        });

        edtRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRepeatCountAlertDialog();
            }
        });

        ivChoiseIconForNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIconChoiseAlertDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtText.getText().toString().equals("") || edtText.getText().toString().equals(" ")) {
                    Toast.makeText(ActivityDetailNotification.this, "Вы забыли про текст напоминания", Toast.LENGTH_SHORT).show();
                } else {
                    saveObject();
                }
            }
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private String writeWithNull(String raw) {
        if (raw.length() == 1) {
            raw = "0" + raw;
        }
        return raw;
    }

    private void createIconChoiseAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        choiseIconAlertDialog = builder.create();
        View view = View.inflate(ActivityDetailNotification.this, R.layout.alert_dialog_list_of_icons, null);
        RecyclerView rvListOfIcons = view.findViewById(R.id.rvAlertDialogListOfIcons);
        rvListOfIcons.setLayoutManager(new GridLayoutManager(ActivityDetailNotification.this, 5));
        rvListOfIcons.setAdapter(new IconAdapter(urlOfIconsNotifications));
        choiseIconAlertDialog.setView(view);
        choiseIconAlertDialog.show();

    }


    private void saveObject() {
        try {
            fillObjectFromFields();
            if (idOfSelectedItem == -1) {
                objectForNotification.save();
            } else {
                ObjectForNotification.deleteAll(ObjectForNotification.class);
                notificationArrayList.remove(idOfSelectedItem);
                objectForNotification.save();
                for (int i = 0; i < notificationArrayList.size(); i++) {
                    notificationArrayList.get(i).save();
                }
                Intent intent = new Intent(ActivityDetailNotification.this, AlarmManagerBR.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityDetailNotification.this,
                        (int) objectForNotification.getOwnId(), intent, 0);
                pendingIntent.cancel();
            }
            createAlarmSchedule(objectForNotification);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Что-то пошло не так, попробуйте позже", Toast.LENGTH_SHORT).show();
            Log.e("Error", e.getLocalizedMessage());
        }
    }

    private void fillObjectFromFields() {
        String[] date = edtDate.getText().toString().split("\\.");
        String[] time = edtTime.getText().toString().split(":");
        objectForNotification.setText(edtText.getText().toString());
        objectForNotification.setDay(Integer.parseInt(date[0]));
        objectForNotification.setMonth(Integer.parseInt(date[1]));
        objectForNotification.setYear(Integer.parseInt(date[2]));
        objectForNotification.setMinute(Integer.parseInt(time[1]));
        objectForNotification.setHour(Integer.parseInt(time[0]));
        objectForNotification.setRepeat(edtRepeat.getText().toString());
        objectForNotification.setOwnId(tempId);
        objectForNotification.setIdROfIcon(idROfSelectedIcon);
    }


    private void createAlarmSchedule(ObjectForNotification objectForNotification) {
        final Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.YEAR, objectForNotification.getYear());
        calendar.set(Calendar.MONTH, objectForNotification.getMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, objectForNotification.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, objectForNotification.getHour());
        calendar.set(Calendar.MINUTE, objectForNotification.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ActivityDetailNotification.this, AlarmManagerBR.class);
        intent.putExtra(AlarmManagerBR.TAG_FOR_TEXT, objectForNotification.getText());
        intent.putExtra(AlarmManagerBR.TAG_FOR_ID, objectForNotification.getOwnId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityDetailNotification.this,
                (int) objectForNotification.getOwnId(), intent, 0);

        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_none))) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_hour))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3600000, pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_two_hours))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7200000, pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_three_hours))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10800000, pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_four_hours))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 14400000, pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_day))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_weak))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 604800000, pendingIntent);
        }
        if (objectForNotification.getRepeat().equals(getString(R.string.repeat_month))) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 108000000, pendingIntent);
        }

    }

    private void createRepeatCountAlertDialog() {
        final RadioGroup rgRepeatCount;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View view = View.inflate(this, R.layout.alert_dialog_repeat_count, null);
        rgRepeatCount = view.findViewById(R.id.rgRepeatCount);
        builder.setView(view);

        builder.setPositiveButton("ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RadioButton checkedRadioButton = view.findViewById(rgRepeatCount.getCheckedRadioButtonId());
                edtRepeat.setText(checkedRadioButton.getText());
            }
        });
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void createTimeAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(true);
        builder.setView(timePicker);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtTime.setText(writeWithNull(String.valueOf(timePicker.getCurrentHour())) + ":"
                        + writeWithNull(String.valueOf(timePicker.getCurrentMinute())));
            }
        });
        builder.setNeutralButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void createDateAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DatePicker datePicker = new DatePicker(this);
        builder.setView(datePicker);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtDate.setText(writeWithNull(String.valueOf(datePicker.getDayOfMonth())) + "."
                        + writeWithNull(String.valueOf(datePicker.getMonth() + 1)) + "."
                        + writeWithNull(String.valueOf(datePicker.getYear())));
            }
        });
        builder.setNeutralButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    private class IconHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivIcon;
        private int idOfCurrentIcon;

        public IconHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_of_list_icon_choise, viewGroup, false));
            ivIcon = itemView.findViewById(R.id.ivItemOfListIconChoise);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Glide.with(ActivityDetailNotification.this).load(idOfCurrentIcon).into(ivChoiseIconForNotification);
            idROfSelectedIcon = idOfCurrentIcon;
            choiseIconAlertDialog.cancel();
        }

        public void bind(int idsOfIcon) {
            idOfCurrentIcon = idsOfIcon;
            Glide.with(ActivityDetailNotification.this).load(idsOfIcon).into(ivIcon);
        }
    }

    private class IconAdapter extends RecyclerView.Adapter<IconHolder> {
        Integer[] idsOfIcons;

        public IconAdapter(Integer[] idsOfIcons) {
            this.idsOfIcons = idsOfIcons;
        }

        @NonNull
        @Override
        public IconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityDetailNotification.this);
            return new IconHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull IconHolder holder, int position) {
            holder.bind(idsOfIcons[position]);
        }

        @Override
        public int getItemCount() {
            return idsOfIcons.length;
        }
    }
}
