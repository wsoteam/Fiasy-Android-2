package com.wsoteam.diet.presentation.activity;

import android.content.res.AssetManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.text.StringsKt;
import okio.BufferedSource;
import okio.Okio;

public abstract class ExercisesSource {

  /**
   * @return All exercises available in this source
   */
  public abstract Single<List<UserActivityExercise>> all();

  /**
   * Add exercise to the current repository
   *
   * @param exercise Exercise to be added
   */
  public abstract Single<UserActivityExercise> add(@NonNull UserActivityExercise exercise);

  /**
   * Remove exercise from current repository
   *
   * @param exercise Exercise to be removed
   */
  public abstract Single<UserActivityExercise> edit(@NonNull UserActivityExercise exercise);

  /**
   * Remove exercise from current repository
   *
   * @param exercise Exercise to be removed
   */
  public abstract Single<UserActivityExercise> remove(@NonNull UserActivityExercise exercise);

  public Single<List<UserActivityExercise>> search(@Nullable CharSequence query) {
    return all().observeOn(Schedulers.io())
        .flatMap(exercises -> Flowable.fromIterable(exercises)
            .filter(e -> StringsKt.contains(e.getTitle(), query, true))
            .toList());
  }

  public static DiffUtil.DiffResult calculateDiff(
      final List<UserActivityExercise> old,
      final List<UserActivityExercise> latest) {

    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
      @Override public int getOldListSize() {
        return old.size();
      }

      @Override public int getNewListSize() {
        return latest.size();
      }

      @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        final UserActivityExercise l = old.get(oldItemPosition);
        final UserActivityExercise r = latest.get(newItemPosition);

        return l.getTitle().equals(r.getTitle());
      }

      @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldItemPosition, newItemPosition);
      }
    });
  }

  public static class AssetsSource extends ExercisesSource {

    private final AssetManager assets;

    private final CopyOnWriteArrayList<UserActivityExercise> cached =
        new CopyOnWriteArrayList<>();

    public AssetsSource(AssetManager assets) {
      this.assets = assets;
    }

    @Override public Single<List<UserActivityExercise>> all() {
      if (!cached.isEmpty()) {
        return Single.just(new ArrayList<>(cached));
      }

      return Single.fromCallable(() -> {
        final BufferedSource source =
            Okio.buffer(Okio.source(assets.open("user_activity_table.csv")));

        final List<UserActivityExercise> exercises = new ArrayList<>();

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

          final int burnsPerMinute = Integer.parseInt(cols[1]);

          final UserActivityExercise e =
              new UserActivityExercise(cols[0], burnsPerMinute * 30, 30 * 60);
          exercises.add(e);
        }

        if (!exercises.isEmpty()) {
          cached.addAll(exercises);
        }

        return exercises;
      });
    }

    @Override public Single<UserActivityExercise> add(UserActivityExercise exercise) {
      throw new UnsupportedOperationException("cannot add to default list");
    }

    @Override public Single<UserActivityExercise> edit(UserActivityExercise exercise) {
      throw new UnsupportedOperationException("cannot edit to default list");
    }

    @Override public Single<UserActivityExercise> remove(UserActivityExercise exercise) {
      throw new UnsupportedOperationException("cannot remove from default list");
    }
  }
}
