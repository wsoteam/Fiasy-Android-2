package com.wsoteam.diet.presentation.activity;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.util.List;

public class FavoriteSource extends ExercisesSource {

  private SharedPreferences preferences;
  private AssetsSource origin;

  public FavoriteSource(Context context) {
    this.preferences = context.getSharedPreferences("favorite_exercises", Context.MODE_PRIVATE);
    this.origin = new AssetsSource(context.getAssets());
  }

  @Override public Single<List<UserActivityExercise>> all() {
    return origin.all()
        .flatMapPublisher(Flowable::fromIterable)
        .filter(e -> preferences.getBoolean(e.getTitle().toString(), false))
        .toList();
  }

  @Override public Single<UserActivityExercise> add(@NonNull UserActivityExercise exercise) {
    preferences.edit()
        .putBoolean(exercise.getTitle().toString(), true)
        .apply();

    return Single.just(exercise);
  }

  @Override public Single<UserActivityExercise> edit(@NonNull UserActivityExercise exercise) {
    throw new UnsupportedOperationException("can't edit favorite items");
  }

  @Override public Single<UserActivityExercise> remove(@NonNull UserActivityExercise exercise) {
    preferences.edit()
        .remove(exercise.getTitle().toString())
        .apply();

    return Single.just(exercise);
  }
}
