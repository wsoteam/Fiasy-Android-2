package com.wsoteam.diet.presentation.profile.help;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.wsoteam.diet.R;

@InjectViewState
public class HelpPresenter extends MvpPresenter<HelpView> {
    private Context context;

    public
    HelpPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(HelpView view) {
        super.attachView(view);
        getViewState().sendArrayNames(context.getResources().getStringArray(R.array.names_items_help));
    }
}
