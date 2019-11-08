package com.wsoteam.diet.presentation.search.basket;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.search.basket.controller.BasketAdapter;
import com.wsoteam.diet.presentation.search.basket.controller.BasketHeaderVH;
import com.wsoteam.diet.presentation.search.basket.controller.BasketItemVH;
import com.wsoteam.diet.presentation.search.basket.db.BasketEntity;
import com.wsoteam.diet.presentation.search.results.controllers.BasketUpdater;
import java.util.List;

public class BasketActivity extends MvpAppCompatActivity implements BasketView {
  @BindView(R.id.rvBasket) RecyclerView rvBasket;
  @BindView(R.id.tvCounter) TextView tvCounter;
  @BindView(R.id.undoCard) CardView undoCard;
  @BindView(R.id.cancel) TextView cancel;
  @BindView(R.id.cvBasket) CardView cvBasket;
  private BasketPresenter presenter;
  private BasketAdapter adapter;
  private Animation hide, show, finalSave;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basket);
    setContinueMark();
    ButterKnife.bind(this);
    presenter = new BasketPresenter(this);
    presenter.attachView(this);
    rvBasket.setLayoutManager(new LinearLayoutManager(this));
    presenter.getBasketLists();
    finalSave = AnimationUtils.loadAnimation(this, R.anim.anim_meas_update);
    loadAnimations();
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.END | ItemTouchHelper.START,
        0) {
      @Override public boolean onMove(@NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (target.getAdapterPosition() > 0) {
          int from = viewHolder.getAdapterPosition();
          int to = target.getAdapterPosition();
          adapter.moveItem(from, to);
          return false;
        } else {
          return true;
        }
      }

      @Override public int getMovementFlags(@NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof BasketHeaderVH) {
          return 0;
        } else {
          return super.getMovementFlags(recyclerView, viewHolder);
        }
      }

      @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

      }
    });
    itemTouchHelper.attachToRecyclerView(rvBasket);
  }

  private void deleteContinueMark() {
    getSharedPreferences(Config.BASKET_CONTINUE, MODE_PRIVATE).edit()
        .putBoolean(Config.BASKET_CONTINUE, false)
        .commit();
  }

  private void setContinueMark() {
    getSharedPreferences(Config.BASKET_CONTINUE, MODE_PRIVATE).edit()
        .putBoolean(Config.BASKET_CONTINUE, true)
        .commit();
  }

  private void loadAnimations() {
    hide = AnimationUtils.loadAnimation(this, R.anim.anim_hide_undo);
    show = AnimationUtils.loadAnimation(this, R.anim.anim_show_undo);
  }

  private void hideUndo() {
    if (undoCard.getAnimation() == null && undoCard.getVisibility() == View.VISIBLE) {
      undoCard.setAnimation(hide);
      undoCard.setVisibility(View.GONE);
    }
  }

  private void showUndo() {
    if (undoCard.getAnimation() == null && undoCard.getVisibility() == View.GONE) {
      undoCard.setAnimation(show);
      undoCard.setVisibility(View.VISIBLE);
    }
  }

  @Override public void getSortedData(List<List<BasketEntity>> allFood) {
    adapter = new BasketAdapter(allFood, getResources().getStringArray(R.array.srch_eating),
        new BasketUpdater() {
          @Override public void getCurrentSize(int size) {
            updateBasket(size);
          }

          @Override public void handleUndoCard(boolean isShow) {
            if (isShow) {
              showUndo();
            } else {
              hideUndo();
            }
            loadAnimations();
          }

          @Override public int getCurrentEating() {
            return 0;
          }
        }, this);
    rvBasket.setAdapter(adapter);
  }

  private void updateBasket(int size) {
    if (size > 0) {
      if (cvBasket.getVisibility() == View.GONE) {
        cvBasket.setVisibility(View.VISIBLE);
      }
      tvCounter.setText(getPaintedString(size));
    } else if (cvBasket.getVisibility() == View.VISIBLE) {
      onBackPressed();
    }
  }

  private Spannable getPaintedString(int size) {
    String string = getResources().getString(R.string.srch_basket_card, size);
    int positionPaint = string.indexOf(" ") + 1;
    Spannable spannable = new SpannableString(string);
    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.srch_painted_string)),
        positionPaint,
        string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  @OnClick({ R.id.cancel, R.id.tvAddToBasket }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.cancel:
        adapter.cancelRemove();
        break;
      case R.id.tvAddToBasket:
        deleteContinueMark();
        adapter.saveFood(getIntent().getStringExtra(Config.INTENT_DATE_FOR_SAVE));
        runCountdown();
        break;
    }
  }

  private void runCountdown() {
    Toast toast = new Toast(this);
    toast.setView(LayoutInflater.from(this).inflate(R.layout.toast_meas_update, null));
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.setDuration(Toast.LENGTH_SHORT);
    TextView title = toast.getView().findViewById(R.id.title);
    ImageView ellipse = toast.getView().findViewById(R.id.ivEllipse);
    title.setText(getResources().getString(R.string.srch_save_list));
    ellipse.setAnimation(finalSave);
    toast.show();
    CountDownTimer timer = new CountDownTimer(2000, 100) {
      @Override public void onTick(long l) {

      }

      @Override public void onFinish() {
        finish();
      }
    }.start();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == RESULT_OK){
      presenter.getBasketLists();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
}
