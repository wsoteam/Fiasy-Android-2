package com.wsoteam.diet.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.CancellationException;

public class RxFirebase {

  public static <T> Single<T> from(Task<T> googleTask) {
    return Single.create(new SingleGoogleTask<>(googleTask));
  }

  public static Flowable<DataSnapshot> from(Query query) {
    return Flowable.create(new QueryObserver(query), BackpressureStrategy.LATEST);
  }

  private static class QueryObserver
      implements FlowableOnSubscribe<DataSnapshot>, ValueEventListener {
    private final Query query;

    private FlowableEmitter<DataSnapshot> emitter;

    private QueryObserver(Query query) {
      this.query = query;
    }

    @Override public void subscribe(FlowableEmitter<DataSnapshot> emitter) {
      this.emitter = emitter;
      this.query.addValueEventListener(this);

      emitter.setDisposable(new Disposable() {
        private boolean disposed = false;

        @Override public void dispose() {
          release();

          disposed = true;
        }

        @Override public boolean isDisposed() {
          return disposed;
        }
      });
    }

    private void release() {
      emitter = null;
      query.removeEventListener(QueryObserver.this);
    }

    @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
      if (emitter == null) {
        query.removeEventListener(this);
        return;
      }

      emitter.onNext(dataSnapshot);
      emitter.onComplete();
    }

    @Override public void onCancelled(@NonNull DatabaseError databaseError) {
      emitter.tryOnError(databaseError.toException());
    }
  }

  public static class SingleGoogleTask<T> implements SingleOnSubscribe<T> {

    private final Task<T> googleTask;

    public SingleGoogleTask(Task<T> googleTask) {
      this.googleTask = googleTask;
    }

    @Override public void subscribe(SingleEmitter<T> observer) {
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
