package com.wsoteam.diet.presentation.activity;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.collection.SparseArrayCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.wsoteam.diet.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  final static int VIEW_TYPE_UNKNOWN = -1;
  final static int VIEW_TYPE_SEARCH = R.layout.item_rounded_search_view;
  final static int VIEW_TYPE_ACTIVITY = R.layout.item_user_activity_view;
  final static int VIEW_TYPE_HEADER = R.layout.item_activity_section_header;

  // utility for headers
  private int headers = 0;

  // search row index
  private final int searchRow = headers++;

  // search row included immediately
  private int total = headers;

  // id to section association
  private final SparseArrayCompat<Section> sections = new SparseArrayCompat<>();
  private final List<AdapterItemsClickListener> clickListeners = new ArrayList<>();

  private RecyclerView attachedRoot;

  private Section head;
  private Section tail;

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

  public void dispatchSectionClick(HeaderView view, int adapterPosition) {
    if (clickListeners.isEmpty()) {
      return;
    }

    final Section section = getSectionAt(adapterPosition  - headers);

    if (section == null) {
      throw new IllegalStateException("section on pos " + adapterPosition + " not found");
    }

    for (AdapterItemsClickListener listener : clickListeners) {
      listener.onSectionClick(view, section.titleRes, adapterPosition);
    }
  }

  public void dispatchItemClick(RecyclerView.ViewHolder view, int adapterPosition) {
    if (clickListeners.isEmpty()) {
      return;
    }

    final Section section = getSectionAt(adapterPosition - headers);

    if (section == null) {
      throw new IllegalStateException("section on pos " + adapterPosition + " not found");
    }

    for (AdapterItemsClickListener listener : clickListeners) {
      listener.onItemClick(view, section.titleRes, adapterPosition);
    }
  }

  public void createSection(@StringRes int title) {
    if (sections.containsKey(title)) {
      throw new IllegalArgumentException(
          "section #" + Integer.toHexString(title) + " already used");
    }

    final Section newSection = new Section(title);

    if (head == null) {
      head = newSection;
    }

    if (tail != null) {
      tail.tail = newSection;
    }

    tail = newSection;
    sections.put(title, newSection);

    total += 1;

    notifyItemInserted(total - 1);
  }

  @Nullable
  protected Section getSectionAt(int position) {
    return head.findRelatedSection(position);
  }

  @Nullable
  protected UserActivityExercise getItemAt(int position) {
    return head.getItemAt(position);
  }

  public int getSectionOffset(int adapterPosition) {
    return getSectionOffset(getSectionAt(adapterPosition - headers));
  }

  public int getSectionOffset(Section section) {
    if (section == head) {
      return 0;
    } else if (section == tail) {
      return total - tail.total() - headers;
    } else {
      Section cur = head;

      int skipped = head.total();
      while (cur != null && cur.tail != section) {
        skipped += cur.total();
        cur = cur.tail;
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

  public void addItem(@StringRes int sectionId, UserActivityExercise item) {
    addItems(sectionId, Collections.singletonList(item));
  }

  public void addItems(@StringRes int sectionId, List<UserActivityExercise> items) {
    final Section section = sections.get(sectionId);

    if (section == null) {
      throw new IllegalArgumentException(
          "section with id #" + Integer.toHexString(sectionId) + " not found");
    }

    final int index = getSectionOffset(section);
    final int size = section.total();

    section.items.addAll(items);
    total += items.size();

    notifyItemRangeInserted(index + size, items.size());
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
      final UserActivityExercise model = getItemAt(position);

      if (model == null) {
        throw new IllegalStateException("expected user-activity on position = " + position);
      }

      ((UserActivityView) holder).bind(model);
    }
  }

  @Override public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    holder.itemView.setOnClickListener(v -> {
      if (holder instanceof HeaderView) {
        dispatchSectionClick((HeaderView) holder, holder.getAdapterPosition());
      } else if (holder instanceof UserActivityView) {
        dispatchItemClick(holder, holder.getAdapterPosition());
      }
    });
  }

  @Override public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
  }

  @Override public int getItemCount() {
    return total;
  }

  public interface AdapterItemsClickListener {
    void onSectionClick(@NonNull HeaderView view, int sectionId, int position);

    void onItemClick(@NonNull RecyclerView.ViewHolder view, int sectionId, int position);
  }

  static class SearchView extends RecyclerView.ViewHolder {

    private final EditText searchView;

    public SearchView(@NonNull View itemView) {
      super(itemView);


      final Context c = itemView.getContext();
      final VectorDrawableCompat d = VectorDrawableCompat.create(c.getResources(),
          R.drawable.search_icon, c.getTheme());

      d.setTint(ContextCompat.getColor(c, R.color.search_icon_grey));

      searchView = itemView.findViewById(R.id.search_view);
      searchView.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
    }
  }

  static class HeaderView extends RecyclerView.ViewHolder {

    private final TextView title;

    public HeaderView(@NonNull View itemView) {
      super(itemView);
      this.title = itemView.findViewById(R.id.title);
    }

    public void bind(Section section) {
      title.setText(section.titleRes);
    }
  }

  static class UserActivityView extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView duration;
    private final TextView effectiveness;

    public UserActivityView(@NonNull View itemView) {
      super(itemView);

      final Context c = itemView.getContext();
      final VectorDrawableCompat d = VectorDrawableCompat.create(c.getResources(),
          R.drawable.ic_access_time, c.getTheme());

      d.setTint(ContextCompat.getColor(c, R.color.search_icon_grey));


      title = itemView.findViewById(R.id.activity_name);
      duration = itemView.findViewById(R.id.activity_duration);
      duration.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
      effectiveness = itemView.findViewById(R.id.activity_effectivity);
    }

    public void bind(UserActivityExercise item) {
      title.setText(item.title());
      duration.setText(DateUtils.formatElapsedTime(item.duration()));
      effectiveness.setText(item.burned() + " ккал");
    }
  }

  static class Section {
    private Section tail;
    private boolean expanded = true;

    private final int titleRes;
    private final List<UserActivityExercise> items = new ArrayList<>();

    Section(int titleRes) {
      this.titleRes = titleRes;
    }

    public int viewTypeOf(int position) {
      if (position == 0) {
        return VIEW_TYPE_HEADER;
      } else if (position >= 1 && position < total()) {
        return VIEW_TYPE_ACTIVITY;
      } else {
        return VIEW_TYPE_UNKNOWN;
      }
    }

    public int total() {
      if (expanded) {
        return items.size() + 1;
      } else {
        return 1;
      }
    }

    @Nullable
    public UserActivityExercise getItemAt(int position) {
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
