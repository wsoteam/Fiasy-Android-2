package com.losing.weight.presentation.activity;

import android.content.res.AssetManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import com.losing.weight.Sync.UserDataHolder;
import com.losing.weight.utils.csv.CSVParser;
import com.losing.weight.utils.csv.CSVStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.text.StringsKt;
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

        final List<ActivityModel> exercises = new ArrayList<>();

        // lines to skip from start
        boolean skip = true;

        final Reader stream;
        String lang = Locale.getDefault().getLanguage();
        switch (lang){
          case "ru":
          case "de":
          case "pt":
          case "es":
          case "is":
          {
            stream = new InputStreamReader(assets.open("user_activity_table_" + lang + ".csv"));
            break;
          }
          default:{
            stream = new InputStreamReader(assets.open("user_activity_table_en.csv"));
            break;
          }
        }

        final CSVParser parser = new CSVParser(stream, CSVStrategy.EXCEL_STRATEGY);

        while (true) {
          String[] cols = parser.getLine();

          if (skip) {
            skip = false;
            continue;
          }

          if (cols == null) {
            break;
          }

          double weight = 1;

          if (UserDataHolder.getUserData() != null
              && UserDataHolder.getUserData().getProfile() != null) {
            weight = UserDataHolder.getUserData().getProfile().getWeight();
          }

          final int duration = 30;
          final double calories = Integer.parseInt(cols[1]) * weight * 0.5f;

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
