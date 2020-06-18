package com.losing.weight.presentation.plans.detail;

import com.transferwise.sequencelayout.SequenceAdapter;
import com.transferwise.sequencelayout.SequenceStep;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class StringSequenceAdapter extends SequenceAdapter<String> {

  private List<String> stringList;


  public StringSequenceAdapter(List<String> stringList) {
    this.stringList = stringList;
  }

  @Override public void bindView(@NotNull SequenceStep sequenceStep, @NotNull String s) {
    sequenceStep.setTitle(s);
    sequenceStep.setAnchorMaxWidth(60);
  }

  @Override public int getCount() {
    return stringList.size();
  }

  @NotNull @Override public String getItem(int i) {
    return stringList.get(i);
  }
}
