package com.losing.weight.presentation.global;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface BaseView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgress(boolean show);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);
}
