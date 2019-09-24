package com.wsoteam.diet.presentation.activity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.utils.DateUtils;

public class UserActivityView extends RecyclerView.ViewHolder {

  public final TextView title;
  public final TextView duration;
  public final TextView effectiveness;
  public final View overflowMenu;

  public UserActivityView(@NonNull View itemView) {
    super(itemView);

    final Context c = itemView.getContext();
    final VectorDrawableCompat d = VectorDrawableCompat.create(c.getResources(),
        R.drawable.ic_access_time, c.getTheme());

    d.setTint(ContextCompat.getColor(c, R.color.search_icon_grey));

    overflowMenu = itemView.findViewById(R.id.action_edit_activity);

    title = itemView.findViewById(R.id.activity_name);
    duration = itemView.findViewById(R.id.activity_duration);
    duration.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
    effectiveness = itemView.findViewById(R.id.activity_effectivity);
  }

  public void bind(ActivityModel item) {
    int weight = 1;

    if (item instanceof UserActivityExercise) {
      final UserData user = UserDataHolder.getUserData();

      if (user != null && user.getProfile() != null) {
        weight = (int) user.getProfile().getWeight();
      }
    }

    title.setText(item.getTitle());
    duration.setText(DateUtils.formatElapsedTime(duration.getContext(), item.getDuration() * 60));

    effectiveness.setText(effectiveness.getContext()
        .getString(R.string.user_activity_burned, weight * item.getCalories()));

    overflowMenu.setVisibility(View.VISIBLE);
  }
}