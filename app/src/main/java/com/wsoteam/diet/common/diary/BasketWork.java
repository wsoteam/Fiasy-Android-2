package com.wsoteam.diet.common.diary;

import android.util.Log;

import com.wsoteam.diet.App;
import com.wsoteam.diet.presentation.search.basket.db.BasketDAO;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class BasketWork {
    private static BasketDAO dao = App.getInstance().getFoodDatabase().basketDAO();

    public static void addToBasket(BasketEntity entity) {
        dao.
                getSameEntity(entity.getServerId(), entity.getDeepId(), entity.getEatingType())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<BasketEntity>() {
                    @Override
                    public void onSuccess(BasketEntity basketEntity) {
                        basketEntity.append(entity);
                        defaultSave(basketEntity);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        defaultSave(entity);
                    }
                });
    }

    private static void defaultSave(BasketEntity entity) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                dao.insert(entity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}

