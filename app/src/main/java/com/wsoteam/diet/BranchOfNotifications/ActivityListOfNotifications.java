package com.wsoteam.diet.BranchOfNotifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.wsoteam.diet.POJOForDB.ObjectForNotification;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Services.AlarmManagerBR;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

public class ActivityListOfNotifications extends AppCompatActivity {
    private static final String TAG_OF_FIRST_RUN = "TAG_OF_FIRST_RUN_ActivityListOfNotifications";
    private RecyclerView recyclerView;
    private FloatingActionButton fabCreateNewNotification;
    private ArrayList<ObjectForNotification> notificationArrayList;
    private ItemAdapter itemAdapter;
    private SharedPreferences isEmptyListOfNotification;
    InterstitialAd interstitialAd;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_notification);

        isEmptyListOfNotification = getPreferences(MODE_PRIVATE);

        fabCreateNewNotification = findViewById(R.id.fabActivityListOfNotificationCreate);
        recyclerView = findViewById(R.id.rvActivityListOfNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setTitle(getString(R.string.my_reminders));

        updateUI();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                itemAdapter.removeItem(viewHolder.getAdapterPosition());
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        fabCreateNewNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YandexMetrica.reportEvent("Открыт экран: Детализация задачи - добавление");
                Intent intent = new Intent(ActivityListOfNotifications.this, ActivityDetailNotification.class);
                intent.putExtra(ActivityDetailNotification.TAG_OF_ACTIVITY, -1);
                startActivity(intent);
            }
        });

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        YandexMetrica.reportEvent("Открыт экран: Задачи");
    }

    private void updateUI() {
        notificationArrayList = (ArrayList<ObjectForNotification>) ObjectForNotification.listAll(ObjectForNotification.class);
        if (notificationArrayList.size() == 0 && isEmptyListOfNotification.getBoolean(TAG_OF_FIRST_RUN, true)) {
            new ObjectForNotification(0,
                    0, 12, 1, 1, 2019, "Пить воду",
                    "Однократно", R.drawable.list_of_choise_notification_icon_18).save();
            new ObjectForNotification(1,
                    0, 12, 1, 1, 2019, "Записать в дневник свои параметры и вес",
                    "Однократно", R.drawable.list_of_choise_notification_icon_4).save();
            new ObjectForNotification(2,
                    0, 12, 1, 1, 2019, "Взять обед с собой",
                    "Однократно", R.drawable.list_of_choise_notification_icon_11).save();
            new ObjectForNotification(3,
                    0, 12, 1, 1, 2019, "Спланировать диету на следующую неделю",
                    "Однократно", R.drawable.list_of_choise_notification_icon_8).save();
            new ObjectForNotification(4,
                    0, 12, 1, 1, 2019, "Сходить на тренировку",
                    "Однократно", R.drawable.list_of_choise_notification_icon_16).save();
            notificationArrayList = (ArrayList<ObjectForNotification>) ObjectForNotification.listAll(ObjectForNotification.class);
            SharedPreferences.Editor editor = isEmptyListOfNotification.edit();
            editor.putBoolean(TAG_OF_FIRST_RUN, false);
            editor.commit();
        }
        itemAdapter = new ItemAdapter(notificationArrayList);
        recyclerView.setAdapter(itemAdapter);
    }

    private String writeWithNull(String raw) {
        if (raw.length() == 1) {
            raw = "0" + raw;
        }
        return raw;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle, tvSubTitle;
        private ImageView ivIcon;

        public ItemViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_of_list_activity_list_of_notification, viewGroup, false));

            tvTitle = itemView.findViewById(R.id.tvItemOfListNotificationTitle);
            tvSubTitle = itemView.findViewById(R.id.tvItemOfListNotificationSubTitle);
            ivIcon = itemView.findViewById(R.id.ivListOfNotificationIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            YandexMetrica.reportEvent("Открыт экран: Детализация задачи - изменение");
            Intent intent = new Intent(ActivityListOfNotifications.this, ActivityDetailNotification.class);
            intent.putExtra(ActivityDetailNotification.TAG_OF_ACTIVITY, getAdapterPosition());
            startActivity(intent);
        }

        public void bind(ObjectForNotification objectForNotification) {
            tvTitle.setText(objectForNotification.getText());
            tvSubTitle.setText(objectForNotification.getRepeat()
                    + " в " + writeWithNull(String.valueOf(objectForNotification.getHour()))
                    + ":" + writeWithNull(String.valueOf(objectForNotification.getMinute()))
                    + ", начиная с  " + writeWithNull(String.valueOf(objectForNotification.getDay()))
                    + "." + writeWithNull(String.valueOf(objectForNotification.getMonth()))
                    + "." + writeWithNull(String.valueOf(objectForNotification.getYear())));
            Glide.with(ActivityListOfNotifications.this).load(objectForNotification.getIdROfIcon()).into(ivIcon);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        ArrayList<ObjectForNotification> notificationArrayList;

        public ItemAdapter(ArrayList<ObjectForNotification> notificationArrayList) {
            this.notificationArrayList = notificationArrayList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityListOfNotifications.this);
            return new ItemViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.bind(notificationArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return notificationArrayList.size();
        }

        public void removeItem(int adapterPosition) {
            Intent intent = new Intent(ActivityListOfNotifications.this, AlarmManagerBR.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityListOfNotifications.this,
                    (int) notificationArrayList.get(adapterPosition).getOwnId(), intent, 0);
            pendingIntent.cancel();

            notificationArrayList.remove(adapterPosition);
            ObjectForNotification.deleteAll(ObjectForNotification.class);
            for (int i = 0; i < notificationArrayList.size(); i++) {
                notificationArrayList.get(i).save();
            }
            notifyItemRemoved(adapterPosition);
        }
    }
}
