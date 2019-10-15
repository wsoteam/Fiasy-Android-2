package com.wsoteam.diet.presentation.activity;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.wsoteam.diet.BuildConfig;
import com.wsoteam.diet.R;
import com.wsoteam.diet.utils.Metrics;
import com.wsoteam.diet.utils.RichTextUtils;
import com.wsoteam.diet.utils.ViewsExtKt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotlin.collections.CollectionsKt;

public class ActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  final static int VIEW_TYPE_UNKNOWN = -1;
  final static int VIEW_TYPE_SEARCH = R.layout.item_rounded_search_view;
  final static int VIEW_TYPE_ACTIVITY = R.layout.item_user_activity_view;
  final static int VIEW_TYPE_HEADER = R.layout.item_activity_section_header;
  final static int VIEW_TYPE_EMPTY_VIEW = R.layout.item_activity_empty_section;

  private final static int INTERACTION_SECTION_CLICK = 0;
  private final static int INTERACTION_ITEM_CLICK = 1;
  private final static int INTERACTION_ITEM_OVERFLOW_CLICK = 2;

  // utility for headers
  private int headers = 0;

  // search row index
  private final int searchRow = headers++;

  // search row included immediately
  private int total = headers;

  // id to section association
  private final SparseArrayCompat<Section> sections = new SparseArrayCompat<>();
  private final List<AdapterItemsClickListener> clickListeners = new ArrayList<>();

  private OnSearchQueryChanged searchListener;

  private RecyclerView attachedRoot;

  private Section head;
  private Section tail;

  private TextWatcher searchWatcher = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      if (searchListener != null) {
        searchListener.onSearch(s.toString());
      }
    }
  };

  @Override public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    attachedRoot = recyclerView;
  }

  @Override public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    attachedRoot = null;
  }

  @NonNull
  private Context getContext() {
    return attachedRoot.getContext();
  }

  /**
   * Attach listener to listen for search events
   *
   * @param searchListener Listener that will trigger when user interacts with Search bar
   */
  public void setSearchListener(@Nullable OnSearchQueryChanged searchListener) {
    this.searchListener = searchListener;
  }

  public void addItemClickListener(AdapterItemsClickListener listener) {
    if (listener != null) {
      this.clickListeners.add(listener);
    }
  }

  public void removeItemClickListener(AdapterItemsClickListener listener) {
    if (listener != null) {
      this.clickListeners.remove(listener);
    }
  }

  /**
   * Dispatches received event to listeners
   *
   * @param view ViewHolder interacted with
   * @param interactionType one of interactions:
   * {@link #INTERACTION_ITEM_CLICK},
   * {@link #INTERACTION_SECTION_CLICK},
   * {@link #INTERACTION_ITEM_OVERFLOW_CLICK}
   */
  public void dispatchInteractionEvent(RecyclerView.ViewHolder view, int interactionType) {
    if (clickListeners.isEmpty()) {
      return;
    }

    final Section section = getSectionAt(view.getAdapterPosition() - headers);

    if (section == null) {
      throw new IllegalStateException("section on pos " + view.getAdapterPosition() + " not found");
    }

    for (AdapterItemsClickListener listener : clickListeners) {
      switch (interactionType) {
        case INTERACTION_SECTION_CLICK:
          listener.onSectionClick((HeaderView) view, section.titleRes);
          break;
        case INTERACTION_ITEM_CLICK:
          listener.onItemClick(view, section.titleRes);
          break;
        case INTERACTION_ITEM_OVERFLOW_CLICK:
          listener.onItemMenuClick(view, section.titleRes);
          break;
      }
    }
  }

  public void createSection(@StringRes int id) {
    createSection(id, true);
  }

  public void createSection(@StringRes int id, boolean expanded) {
    if (sections.containsKey(id)) {
      return;
    }

    final Section newSection = new Section(id);
    newSection.expanded = expanded;

    if (head == null) {
      head = newSection;
    }

    if (tail != null) {
      tail.tail = newSection;
    }

    tail = newSection;
    sections.put(id, newSection);

    int initialAdded = newSection.total();
    total += initialAdded;

    notifyItemRangeInserted(total - initialAdded, initialAdded);
  }

  public void clearSection(@StringRes int id) {
    if (!sections.containsKey(id)) {
      throw new IllegalArgumentException("section doesn't exist");
    }

    final Section section = sections.get(id);

    if (section.items.isEmpty()) {
      return;
    }

    final int offset = headers + getSectionOffset(section);
    final int currentSize = section.total();

    section.items.clear();
    section.expanded = true;

    final int diff = currentSize - section.total();

    total -= diff;

    if (BuildConfig.DEBUG) {
      Log.d("ActivitiesList", String.format("section_cleared = %d, total = %d", diff, total));
    }

    notifyItemChanged(offset);
    notifyItemRangeRemoved(offset + 1, diff);
  }

  @Nullable
  protected Section getSectionAt(int position) {
    return head.findRelatedSection(position);
  }

  public List<ActivityModel> getItemsBySection(@StringRes int sectionId) {
    final Section section = sections.get(sectionId);
    return section.expanded ? section.items : Collections.emptyList();
  }

  @Nullable
  private ActivityModel getItemAt(int position) {
    return head.getItemAt(position);
  }

  public ActivityModel getItem(int position) {
    return getItemAt(position - headers);
  }

  public int getSectionsHeader(@StringRes int sectionId) {
    return 1;
  }

  public int getSectionOffset(int adapterPosition) {
    return getSectionOffset(getSectionAt(adapterPosition - headers));
  }

  /**
   * @return Offset to section position, offset -> [section -> items...]
   */
  public int getSectionOffset(@NonNull Section section) {
    if (section == head) {
      return 0;
    } else {
      int skipped = 0;
      Section cur = head;

      while (cur.titleRes != section.titleRes) {
        skipped += cur.total();
        cur = cur.tail;

        if (cur == null) {
          throw new IllegalArgumentException("section not found");
        }
      }

      return skipped;
    }
  }

  public void toggleSection(int sectionId) {
    if (isExpanded(sectionId)) {
      collapse(sectionId);
    } else {
      expand(sectionId);
    }
  }

  public void collapse(int sectionId) {
    if (isExpanded(sectionId)) {
      final Section section = sections.get(sectionId);
      final int offset = headers + getSectionOffset(section);
      final int currentSize = section.total();

      section.expanded = false;

      final int diff = currentSize - section.total();

      total -= diff;

      if (BuildConfig.DEBUG) {
        Log.d("ActivitiesList", String.format("section=%s, offset=%d, collapsed = %d, total = %d",
            getContext().getString(sectionId), offset, diff, total));
      }

      notifyItemChanged(offset);
      notifyItemRangeRemoved(offset + 1, diff);
    }
  }

  public void expand(int sectionId) {
    if (!isExpanded(sectionId)) {
      final Section section = sections.get(sectionId);
      final int offset = headers + getSectionOffset(section);
      final int currentSize = section.total();

      section.expanded = true;

      final int diff = section.total() - currentSize;

      total += diff;

      if (BuildConfig.DEBUG) {
        Log.d("ActivitiesList", String.format("section=%s, expanded=%d, offset=%d, total=%d",
            getContext().getString(sectionId), diff, offset, total));
      }

      notifyItemChanged(offset);
      notifyItemRangeInserted(offset + 1, diff);
    }
  }

  public boolean isExpanded(int sectionId) {
    if (sections.containsKey(sectionId)) {
      return sections.get(sectionId).expanded;
    } else {
      throw new IllegalArgumentException(
          "section id " + Integer.toHexString(sectionId) + " not found");
    }
  }

  public void addItems(@StringRes int sectionId, List<ActivityModel> items,
      DiffUtil.DiffResult difference) {
    final Section section = sections.get(sectionId);

    if (section == null) {
      throw new IllegalArgumentException(
          "section with id #" + Integer.toHexString(sectionId) + " not found");
    }

    final int currentSize = section.total();

    final int offset = headers + getSectionOffset(section);

    section.expanded = true;
    section.items.clear();
    section.items.addAll(items);

    final int diff = currentSize - section.total();

    total -= diff;

    notifyItemChanged(offset);

    final int index = offset + 1;

    difference.dispatchUpdatesTo(new ListUpdateCallback() {
      @Override public void onInserted(int position, int count) {
        notifyItemRangeInserted(index + position, count);
      }

      @Override public void onRemoved(int position, int count) {
        notifyItemRangeRemoved(index + position, count);
      }

      @Override public void onMoved(int fromPosition, int toPosition) {
        notifyItemMoved(index + fromPosition, index + toPosition);
      }

      @Override public void onChanged(int position, int count, @Nullable Object payload) {
        notifyItemRangeChanged(index + position, count, payload);
      }
    });
  }

  public void updateItemAt(@StringRes int sectionId, @NonNull ActivityModel item) {
    final Section section = sections.get(sectionId);

    if (section == null) {
      throw new IllegalArgumentException(
          "section with id #" + Integer.toHexString(sectionId) + " not found");
    }

    final int index = headers + getSectionOffset(section);

    int itemId = CollectionsKt.indexOfFirst(section.items, exercise -> {
      return exercise.getWhen() == item.getWhen();
    });

    if (itemId < 0) {
      throw new Resources.NotFoundException(String.format("activity with timestamp=%d, not found",
          item.getWhen()));
    }

    section.items.set(itemId, item);
    notifyItemChanged(index + itemId + 1);
  }

  public void addItem(@StringRes int sectionId, @NonNull ActivityModel item) {
    addItems(sectionId, Collections.singletonList(item), 0);
  }

  public void addItems(@StringRes int sectionId, @NonNull List<ActivityModel> items) {
    addItems(sectionId, items, -1);
  }

  public void addItems(@StringRes int sectionId, @NonNull List<ActivityModel> items,
      int pushIndex) {

    if (items.isEmpty()) {
      Log.d("ActivitiesList", String.format("section=%s, nothing changed",
          getContext().getResources().getString(sectionId)));
      return;
    }

    final Section section = sections.get(sectionId);

    if (section == null) {
      throw new IllegalArgumentException(
          "section with id #" + Integer.toHexString(sectionId) + " not found");
    }

    final int index = headers + getSectionOffset(section);
    final int oldSize = section.total();

    final boolean prepend = pushIndex >= 0;

    if (pushIndex < 0) {
      pushIndex = section.items.size();
    }

    int headers = 0;

    if (section.items.isEmpty()) {
      headers = oldSize - 1; // total items - section header => gives headers size
    }

    section.items.addAll(pushIndex, items);

    if (!section.expanded) {
      Log.d("ActivitiesList", "section=%s, collapsed, skipping");
      return;
    }

    total += section.total() - oldSize;

    if (headers > 0) {
      notifyItemRangeRemoved(index + 1, headers);
    }

    if (BuildConfig.DEBUG) {
      Log.d("ActivitiesList", String.format("section=%s, items_added=%d, total=%d",
          getContext().getResources().getString(sectionId), items.size(), total));
    }

    if (prepend) {
      notifyItemRangeInserted(index + 1, items.size());
    } else {
      notifyItemRangeInserted(index + (oldSize - headers), items.size());
    }
  }

  public void removeItem(@StringRes int sectionId, int position) {
    final Section section = sections.get(sectionId);

    if (section == null) {
      throw new IllegalArgumentException(
          "section with id #" + Integer.toHexString(sectionId) + " not found");
    }

    final int offset = getSectionOffset(section);
    final int id = position - offset - 1 - headers;

    section.items.remove(id);

    if (section.items.isEmpty()) {
      notifyItemChanged(position);
    } else {
      total -= 1;
      notifyItemRemoved(position);
    }
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_UNKNOWN) {
      throw new IllegalStateException("cannot initiate unknown view");
    }

    final View target = LayoutInflater.from(parent.getContext())
        .inflate(viewType, parent, false);

    if (viewType == VIEW_TYPE_ACTIVITY) {
      return new UserActivityView(target);
    } else if (viewType == VIEW_TYPE_HEADER) {
      return new HeaderView(target);
    } else if (viewType == VIEW_TYPE_SEARCH) {
      return new SearchView(target);
    } else if (viewType == VIEW_TYPE_EMPTY_VIEW) {
      return new EmptyView(target);
    }

    return null;
  }

  @Override public int getItemViewType(int position) {
    if (position == searchRow) {
      return VIEW_TYPE_SEARCH;
    }

    position = position - headers;

    if (head != null) {
      final Section section = head.findRelatedSection(position);

      if (section == null) {
        throw new IllegalStateException("section not found for position=" + position);
      } else {
        final int type = section.viewTypeOf(position - getSectionOffset(section));

        if (type == VIEW_TYPE_UNKNOWN) {
          throw new IllegalStateException(
              String.format("unknown state for position=%d, section=%s",
                  position, getContext().getResources().getResourceName(section.titleRes)));
        } else {
          return type;
        }
      }
    }

    throw new IllegalStateException("View type not defined for position =" + position);
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (position >= headers) {
      position -= headers;
    }

    if (holder instanceof HeaderView) {
      final Section section = getSectionAt(position);

      if (section == null) {
        throw new IllegalStateException("expected section on position = " + position);
      }

      ((HeaderView) holder).bind(section);
    } else if (holder instanceof UserActivityView) {
      final ActivityModel model = getItemAt(position);

      if (model == null) {
        throw new IllegalStateException("expected user-activity on position = " + position);
      }

      ((UserActivityView) holder).bind(model);
    } else if (holder instanceof EmptyView) {
      final Section section = getSectionAt(position);

      if (section == null) {
        throw new IllegalStateException("expected section on position = " + position);
      }

      ((EmptyView) holder).bind(section);
    }
  }

  @Override public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);

    holder.itemView.setOnClickListener(v -> {
      if (holder instanceof HeaderView) {
        dispatchInteractionEvent(holder, INTERACTION_SECTION_CLICK);
      } else if (holder instanceof UserActivityView) {
        dispatchInteractionEvent(holder, INTERACTION_ITEM_CLICK);
      }
    });

    if (holder instanceof UserActivityView) {
      final UserActivityView v = (UserActivityView) holder;
      v.overflowMenu.setOnClickListener(target -> {
        dispatchInteractionEvent(holder, INTERACTION_ITEM_OVERFLOW_CLICK);
      });
    }

    if (holder instanceof SearchView) {
      final SearchView view = ((SearchView) holder);
      view.searchView.addTextChangedListener(searchWatcher);
      view.searchView.setOnEditorActionListener((v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          ViewsExtKt.hideKeyboard(v);

          searchListener.onSearch(v.getText().toString());
          return true;
        }
        return false;
      });
    }
  }

  @Override public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);

    holder.itemView.setOnClickListener(null);

    if (holder instanceof UserActivityView) {
      final UserActivityView v = (UserActivityView) holder;
      v.overflowMenu.setOnClickListener(null);
    }

    if (holder instanceof SearchView) {
      final SearchView view = ((SearchView) holder);
      view.searchView.removeTextChangedListener(searchWatcher);
      view.searchView.setOnEditorActionListener(null);
    }
  }

  @Override public int getItemCount() {
    return total;
  }

  public interface OnSearchQueryChanged {
    void onSearch(CharSequence query);
  }

  public interface AdapterItemsClickListener {
    void onSectionClick(@NonNull HeaderView view, int sectionId);

    void onItemClick(@NonNull RecyclerView.ViewHolder view, int sectionId);

    void onItemMenuClick(@NonNull RecyclerView.ViewHolder view, int sectionId);
  }

  static class SearchView extends RecyclerView.ViewHolder {

    private final EditText searchView;
    private final View actionClear;
    private final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        actionClear.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
      }

      @Override public void afterTextChanged(Editable s) {

      }
    };

    public SearchView(@NonNull View itemView) {
      super(itemView);

      final Context c = itemView.getContext();
      final VectorDrawableCompat d = VectorDrawableCompat.create(c.getResources(),
          R.drawable.search_icon, c.getTheme());

      d.setTint(ContextCompat.getColor(c, R.color.search_icon_grey));

      searchView = itemView.findViewById(R.id.search_view);
      searchView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
      searchView.addTextChangedListener(watcher);

      actionClear = itemView.findViewById(R.id.action_clear);
      actionClear.setOnClickListener(v -> searchView.setText(""));
    }
  }

  static class HeaderView extends RecyclerView.ViewHolder {

    private final TextView title;
    private final ImageView icon;

    public HeaderView(@NonNull View itemView) {
      super(itemView);
      this.title = itemView.findViewById(R.id.title);
      this.icon = itemView.findViewById(R.id.icon);
    }

    public void bind(Section section) {
      title.setText(section.titleRes);

      if (section.expanded) {
        icon.setRotation(0);
      } else {
        icon.setRotation(180);
      }
    }
  }

  static class EmptyView extends RecyclerView.ViewHolder {
    private final TextView emptyView;

    public EmptyView(@NonNull View itemView) {
      super(itemView);

      emptyView = (TextView) itemView;
      emptyView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void bind(Section section) {
      final Context context = itemView.getContext();

      final int targetVectorId;
      if (section.titleRes == R.string.user_activity_section_my) {
        targetVectorId = R.drawable.ic_add_black_24dp;
      } else {
        targetVectorId = R.drawable.ic_open_menu;
      }

      final VectorDrawableCompat target = (VectorDrawableCompat)
          VectorDrawableCompat.create(context.getResources(), targetVectorId, context.getTheme())
              .mutate();

      target.setBounds(0, 0, Metrics.dp(context, 16), Metrics.dp(context, 16));
      target.setTint(ContextCompat.getColor(context, R.color.orange));

      final ImageSpan span = new ImageSpan(target, DynamicDrawableSpan.ALIGN_BOTTOM);
      final CharSequence iconChar = RichTextUtils.replaceWithIcon("icon", span);

      if (section.titleRes == R.string.user_activity_section_my) {
        emptyView.setText(TextUtils.concat("Вы пока не добавили ни одну "
                + "активность.\nДля того, чтобы добавить активность",
            new RichTextUtils.RichText(TextUtils.concat("\nнажмите на ", iconChar))
                .colorRes(itemView.getContext(), R.color.orange)
                .text()));
      } else {
        emptyView.setText(TextUtils.concat("Вы пока не добавили активность в избранное. "
                + "Чтобы добавить нажмите на",
            new RichTextUtils.RichText(TextUtils.concat("\nнажмите на ", iconChar))
                .colorRes(itemView.getContext(), R.color.orange)
                .text()));
      }

      emptyView.invalidate();
    }
  }

  static class Section {
    private Section tail;
    private boolean expanded = true;

    private final int titleRes;
    private final List<ActivityModel> items = new ArrayList<>();

    Section(int titleRes) {
      this.titleRes = titleRes;
    }

    public int viewTypeOf(int position) {
      if (position == 0) {
        return VIEW_TYPE_HEADER;
      } else if (items.isEmpty() && position == 1 && position < total()) {
        return VIEW_TYPE_EMPTY_VIEW;
      } else if (position >= 1 && position < total()) {
        return VIEW_TYPE_ACTIVITY;
      } else {
        return VIEW_TYPE_UNKNOWN;
      }
    }

    public int total() {
      if (expanded) {
        if (!items.isEmpty()) {
          return items.size() + 1;
        } else {
          return 1 + 1; // header + empty_view
        }
      } else {
        return 1;
      }
    }

    @Nullable
    public ActivityModel getItemAt(int position) {
      int vt = viewTypeOf(position);

      if (vt == VIEW_TYPE_ACTIVITY) {
        return items.get(position - 1);
      } else if (vt == VIEW_TYPE_HEADER) {
        throw new IllegalArgumentException("Section header position given");
      } else if (tail != null) {
        return tail.getItemAt(position - total());
      } else {
        return null;
      }
    }

    @Nullable
    public Section findRelatedSection(int position) {
      if (viewTypeOf(position) != VIEW_TYPE_UNKNOWN) {
        return this;
      } else if (tail != null) {
        return tail.findRelatedSection(position - total());
      } else {
        return null;
      }
    }
  }
}
