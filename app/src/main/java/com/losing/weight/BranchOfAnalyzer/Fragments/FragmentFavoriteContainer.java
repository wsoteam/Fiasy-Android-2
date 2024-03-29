package com.losing.weight.BranchOfAnalyzer.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.losing.weight.BranchOfAnalyzer.TabsFragment;
import com.losing.weight.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentFavoriteContainer extends Fragment implements TabsFragment {
    @BindView(R.id.flContainer) FrameLayout flContainer;
    Unbinder unbinder;

    @Override
    public void sendString(String searchString) {
        ((TabsFragment) getChildFragmentManager().findFragmentById(R.id.flContainer)).sendString(searchString);
    }

    @Override
    public void sendClearSearchField() {
        sendString("");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_container, container, false);
        unbinder = ButterKnife.bind(this, view);
        getChildFragmentManager().beginTransaction().replace(R.id.flContainer, new FragmentFavorites()).commit();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            sendString("");
        }
    }



    @OnClick({R.id.rbtnFavorite, R.id.rbtnOwnFood})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rbtnFavorite:
                if (checked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.flContainer, new FragmentFavorites()).commit();
                }
                break;
            case R.id.rbtnOwnFood:
                if (checked) {
                    getChildFragmentManager().beginTransaction().replace(R.id.flContainer, new FragmentCustomFoods()).commit();
                }
                break;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
