package com.wsoteam.diet.presentation.activity;

import android.content.res.AssetManager;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import kotlin.text.StringsKt;
import okio.BufferedSource;
import okio.Okio;

public interface ExercisesSource {
  Single<List<UserActivityExercise>> getExercises();

  class AssetsSource implements ExercisesSource {

    private final AssetManager assets;

    public AssetsSource(AssetManager assets) {
      this.assets = assets;
    }

    @Override public Single<List<UserActivityExercise>> getExercises() {
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

          final UserActivityExercise e = new UserActivityExercise(cols[0], burnsPerMinute * 30, 30);
          exercises.add(e);
        }

        return exercises;
      });
    }
  }
}
