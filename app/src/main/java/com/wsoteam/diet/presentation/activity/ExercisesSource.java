package com.wsoteam.diet.presentation.activity;

import android.content.res.AssetManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import com.wsoteam.diet.Sync.POJO.UserData;
import com.wsoteam.diet.Sync.UserDataHolder;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.text.StringsKt;
import okio.BufferedSource;
import okio.Okio;
import org.jetbrains.annotations.NotNull;

public abstract class ExercisesSource {

  /**
   * @return All exercises available in this source
   */
  public abstract Single<List<ActivityModel>> all();

  /**
   * Add exercise to the current repository
   *
   * @param exercise Exercise to be added
   */
  public abstract Single<ActivityModel> add(@NonNull ActivityModel exercise);

  /**
   * Remove exercise from current repository
   *
   * @param exercise Exercise to be removed
   */
  public abstract Single<ActivityModel> edit(@NonNull ActivityModel exercise);

  /**
   * Remove exercise from current repository
   *
   * @param exercise Exercise to be removed
   */
  public abstract Single<ActivityModel> remove(@NonNull ActivityModel exercise);

  public Single<List<ActivityModel>> search(@Nullable CharSequence query) {
    return all().observeOn(Schedulers.io())
        .flatMap(exercises -> Flowable.fromIterable(exercises)
            .filter(e -> StringsKt.contains(e.getTitle(), query, true))
            .toList());
  }

  public static DiffUtil.DiffResult calculateDiff(
      final List<ActivityModel> old,
      final List<ActivityModel> latest) {

    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
      @Override public int getOldListSize() {
        return old.size();
      }

      @Override public int getNewListSize() {
        return latest.size();
      }

      @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        final ActivityModel l = old.get(oldItemPosition);
        final ActivityModel r = latest.get(newItemPosition);

        return l.getTitle().equals(r.getTitle());
      }

      @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldItemPosition, newItemPosition);
      }
    });
  }

  public static class AssetsSource extends ExercisesSource {

    private final AssetManager assets;

    private final CopyOnWriteArrayList<ActivityModel> cached =
        new CopyOnWriteArrayList<>();

    public AssetsSource(AssetManager assets) {
      this.assets = assets;
    }

    @Override public Single<List<ActivityModel>> all() {
      if (!cached.isEmpty()) {
        return Single.just(new ArrayList<>(cached));
      }

      return Single.fromCallable(() -> {
        final BufferedSource source =
            Okio.buffer(Okio.source(assets.open("user_activity_table.csv")));

        final List<ActivityModel> exercises = new ArrayList<>();

        // lines to skip from start
        int skip = 1;

        while (!source.exhausted()) {
          final String line = source.readUtf8Line();

          if (skip > 0) {
            skip--;
            continue;
          }

          final String[] tmp = line.split(",");
          final String[] cols;

          if (tmp.length > 2) {
            cols = new String[2];
            cols[1] = tmp[tmp.length - 1];

            final StringBuilder b = new StringBuilder();
            for (int i = 0; i < tmp.length - 1; i++) {
              b.append(tmp[i]);
            }

            cols[0] = StringsKt.removeSurrounding(b.toString(), "\"");
          } else {
            cols = tmp;
          }

          final int duration = 30;
          final double calories = Integer.parseInt(cols[1]) * 0.5;

          final UserActivityExercise e = new UserActivityExercise(
              "asset", // id
              cols[0], // title
              0,
              (int) calories, // calories
              duration, // per 30 minute
              false // favorite
          );

          exercises.add(e);
        }

        if (!exercises.isEmpty()) {
          cached.addAll(exercises);
        }

        return exercises;
      });
    }

    @Override public Single<ActivityModel> add(@NotNull ActivityModel exercise) {
      throw new UnsupportedOperationException("cannot add to default list");
    }

    @Override public Single<ActivityModel> edit(@NotNull ActivityModel exercise) {
      throw new UnsupportedOperationException("cannot edit to default list");
    }

    @Override public Single<ActivityModel> remove(@NotNull ActivityModel exercise) {
      throw new UnsupportedOperationException("cannot remove from default list");
    }
  }
}
