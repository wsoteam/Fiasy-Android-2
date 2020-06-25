package com.losing.weight.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.losing.weight.R;
import com.losing.weight.presentation.training.TrainingActivity;
import com.losing.weight.utils.Metrics;
import com.losing.weight.utils.RecyclerExtKt;
import com.losing.weight.utils.ViewsExtKt;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import static com.losing.weight.presentation.activity.ActivitiesAdapter.VIEW_TYPE_ACTIVITY;

public class UserActivityFragment extends DialogFragment implements
    // Loool
    Toolbar.OnMenuItemClickListener,
    PopupMenu.OnMenuItemClickListener,
    OnActivityCreated {

  private final static int CREATE_CUSTOM_ACTIVITY = 0;
  private final static int ADD_ACTIVITY_2_DIARY = 1;

  private Toolbar toolbar;
  private AppBarLayout appbar;

  private RecyclerView container;
  private ActivitiesAdapter adapter;
  private ExercisesSource diarySource = DiaryActivitiesSource.INSTANCE;

  private final SparseArrayCompat<ExercisesSource> sources = new SparseArrayCompat<>();
  private final CompositeDisposable disposables = new CompositeDisposable();

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_user_activity, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    toolbar = view.findViewById(R.id.toolbar);
    toolbar.inflateMenu(R.menu.fragment_user_activity_toolbar_menu);
    toolbar.setOnMenuItemClickListener(this);
    toolbar.setNavigationOnClickListener(v -> dismissAllowingStateLoss());

    appbar = view.findViewById(R.id.appbar);
    container = view.findViewById(R.id.container);

    container.setAdapter(adapter = new ActivitiesAdapter(Locale.getDefault().getLanguage().equals("ru"), getContext()));
    container.addItemDecoration(new DividerDecoration(requireContext()));
    container.setLayoutManager(new LinearLayoutManager(requireContext()));

    RecyclerExtKt.appBarLiftable(container, appbar);


    adapter.setSearchListener(this::search);
    adapter.setBannerListener(v ->
            startActivity(new Intent(getContext(), TrainingActivity.class)));
    adapter.addItemClickListener(new ActivitiesAdapter.AdapterItemsClickListener() {
      @Override
      public void onSectionClick(ActivitiesAdapter.HeaderView view, int sectionId) {
        adapter.toggleSection(sectionId);

        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit()
            .putBoolean("section_expanded_" + sectionId, adapter.isExpanded(sectionId))
            .apply();
      }

      @Override
      public void onItemClick(RecyclerView.ViewHolder view, int sectionId) {
        final ActivityModel e = adapter.getItem(view.getAdapterPosition());
        requestAddUserActivity(e, false);
      }

      @Override public void onItemMenuClick(@NonNull RecyclerView.ViewHolder view, int sectionId) {
        final UserActivityView v = (UserActivityView) view;

        final PopupMenu menu =
            new PopupMenu(v.overflowMenu.getContext(), v.overflowMenu, Gravity.BOTTOM);

        if (sectionId == R.string.user_activity_section_defaults) {
          menu.getMenu().add(0, R.id.action_make_favorite, 1, R.string.action_add_to_favorite);
        } else if (sectionId == R.string.favorites) {
          menu.getMenu().add(0, R.id.action_delete, 1, R.string.contextMenuDelete);
        } else if (sectionId == R.string.user_activity_section_my) {
          menu.getMenu().add(0, R.id.action_delete, 1, R.string.contextMenuDelete);
        }

        menu.setOnMenuItemClickListener(item -> {
          final ActivityModel target = adapter.getItem(v.getAdapterPosition());

          switch (item.getItemId()) {
            case R.id.action_make_favorite:
              if (sources.containsKey(R.string.favorites)) {
                disposables.add(sources.get(R.string.favorites)
                    .add(target)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(e -> {
                      adapter.addItem(R.string.favorites, e);
                      Toast.makeText(requireContext(), getString(R.string.added_favorite), Toast.LENGTH_SHORT).show();
                    })
                    .subscribe());
              }
              break;

            case R.id.action_edit:
              requestAddUserActivity(target, true);
              break;

            case R.id.action_add_user_activity:
              requestAddUserActivity(target, false);
              break;

            case R.id.action_delete:
              final ActivityModel exercise = adapter.getItem(v.getAdapterPosition());

              adapter.removeItem(sectionId, v.getAdapterPosition());

              if (sources.containsKey(sectionId)) {
                disposables.add(sources.get(sectionId)
                    .remove(exercise)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe());
              }
              break;
          }
          return true;
        });
        menu.show();
      }
    });

    //final GoogleFitSource googleFitSource = new GoogleFitSource(requireContext());
    //googleFitSource.ensurePermission(this);
    //sources.put(R.string.user_activity_section_google_fit, googleFitSource);

    sources.put(R.string.user_activity_section_my,
        new ActivitiesSyncedSource(ActivitiesSyncedSource.ACTIVITIES));

    sources.put(R.string.favorites,
        new FavoriteSource());

    sources.put(R.string.user_activity_section_defaults,
        new ExercisesSource.AssetsSource(getResources().getAssets()));

    int[] sections = {
        R.string.user_activity_section_my,
        R.string.favorites,
        R.string.user_activity_section_defaults,
    };

    for (int i = 0; i < sections.length; i++) {
      final int sectionId = sections[i];

      final boolean expanded = PreferenceManager.getDefaultSharedPreferences(requireContext())
          .getBoolean("section_expanded_" + sectionId, true);

      adapter.createSection(sectionId, expanded);
    }

    fetchSources();
  }

  private void search(CharSequence q) {
    disposables.clear();

    final List<Single<List<ActivityModel>>> streams = new ArrayList<>();

    for (int i = 0; i < sources.size(); i++) {
      final int sourceId = sources.keyAt(i);

      streams.add(
          sources.valueAt(i).search(q)
              .subscribeOn(Schedulers.io())
              .onErrorReturn(error -> {
                error.printStackTrace();

                // default for now
                return Collections.emptyList();
              })
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSuccess(
                  exercises -> {
                    adapter.clearSection(sourceId);
                    adapter.addItems(sourceId, exercises);

                    if (exercises.isEmpty() && !TextUtils.isEmpty(q)) {
                      adapter.collapse(sourceId);
                    } else {
                      adapter.expand(sourceId);
                    }
                  }
              )
      );
    }

    disposables.add(Single.concat(streams).subscribe());
  }

  private void fetchSources() {
    disposables.clear();

    final List<Single<List<ActivityModel>>> streams = new ArrayList<>();

    for (int i = 0; i < sources.size(); i++) {
      final int sourceId = sources.keyAt(i);

      streams.add(
          sources.valueAt(i).all()
              .subscribeOn(Schedulers.io())
              .onErrorReturn(error -> {
                error.printStackTrace();

                // default for now
                return Collections.emptyList();
              })
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSuccess(
                  exercises -> {
                    adapter.clearSection(sourceId);
                    adapter.addItems(sourceId, exercises);

                    final boolean expanded = PreferenceManager.getDefaultSharedPreferences(requireContext())
                        .getBoolean("section_expanded_" + sourceId, true);

                    if (expanded) {
                      adapter.expand(sourceId);
                    }
                  }
              )
      );
    }

    disposables.add(Single.concat(streams).subscribe());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    disposables.clear();
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_open_menu:
        final PopupMenu menu = new PopupMenu(requireContext(),
            toolbar.findViewById(R.id.action_open_menu));

        menu.inflate(R.menu.fragment_user_activity_menu);
        menu.setOnMenuItemClickListener(this);
        menu.show();
        break;

      case R.id.action_add_user_activity:
        requestCreateCustomActivity();
        break;

      default:
        return false;
    }

    return true;
  }

  private void requestCreateCustomActivity() {
    if (getView() != null) {
      ViewsExtKt.hideKeyboard(getView());
    }

    final CreateUserActivityFragment target = new CreateUserActivityFragment();
    target.setTargetFragment(this, CREATE_CUSTOM_ACTIVITY);

    getActivity().getSupportFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, target, target.getClass().getSimpleName())
        .addToBackStack(null)
        .commitAllowingStateLoss();
  }

  private void requestAddUserActivity(@Nullable ActivityModel exercise, boolean edit) {
    if (getView() != null) {
      ViewsExtKt.hideKeyboard(getView());
    }

    final EditUserActivityFragment f = new EditUserActivityFragment();
    f.setTargetFragment(this, !edit ? ADD_ACTIVITY_2_DIARY : CREATE_CUSTOM_ACTIVITY);
    f.setSelected(exercise);

    if (edit) {
      f.setEditMode(true);
    } else {
      f.setDiaryMode(true);
    }

    getActivity().getSupportFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, f, f.getClass().getSimpleName())
        .addToBackStack(null)
        .commitAllowingStateLoss();
  }

  @Override public void didCreateActivity(@NotNull UserActivityExercise exercise,
      final boolean edited,
      final int requestCode) {

    final boolean createCustomActivity = requestCode == CREATE_CUSTOM_ACTIVITY;

    if (createCustomActivity) {
      if (edited) {
        disposables.add(sources.get(R.string.user_activity_section_my)
            .edit(exercise)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                added -> adapter.updateItemAt(R.string.user_activity_section_my, added),
                Throwable::printStackTrace
            ));
      } else {
        disposables.add(sources.get(R.string.user_activity_section_my)
            .add(exercise)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                added -> adapter.addItem(R.string.user_activity_section_my, added),
                Throwable::printStackTrace
            ));
      }
    } else {
      disposables.add(diarySource.add(exercise)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              Functions.emptyConsumer(),
              Throwable::printStackTrace
          ));
    }
  }

  static class DividerDecoration extends RecyclerView.ItemDecoration {
    final int dividerHeight;
    final Paint p = new Paint();

    public DividerDecoration(Context context) {
      dividerHeight = Metrics.dp(context, 1f);
      p.setColor(0xFFD3D3D3);
      p.setStrokeWidth(dividerHeight);
      p.setStyle(Paint.Style.STROKE);
    }

    @Override public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state) {
      super.onDrawOver(c, parent, state);

      for (int i = 0; i < parent.getChildCount(); i++) {
        final View view = parent.getChildAt(i);
        final RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);

        if (holder.getItemViewType() != VIEW_TYPE_ACTIVITY) {
          continue;
        }

        c.drawLine(parent.getLeft(), view.getBottom(), parent.getRight(), view.getBottom(), p);
      }
    }

    @Override public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
        @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
      super.getItemOffsets(outRect, view, parent, state);
      outRect.set(0, 0, 0, dividerHeight);
    }
  }
}