package com.wsoteam.diet.utils;

import com.google.android.gms.tasks.Task;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import java.util.concurrent.CancellationException;

public class RxFirebase {

  public static <T> Single<T> from(Task<T> googleTask) {
    return Single.create(new SingleGoogleTask<>(googleTask));
  }

  public static class SingleGoogleTask<T> implements SingleOnSubscribe<T> {

    private final Task<T> googleTask;

    public SingleGoogleTask(Task<T> googleTask) {
      this.googleTask = googleTask;
    }

    @Override public void subscribe(SingleEmitter<T> observer) throws Exception {
      googleTask.addOnCompleteListener(task -> {
        if (task.isSuccessful() && !observer.isDisposed()) {
          observer.onSuccess(task.getResult());
        } else {
          if (task.isCanceled()) {
            observer.tryOnError(new CancellationException());
          } else {
            observer.tryOnError(task.getException());
          }
        }
      });
    }
  }
}
