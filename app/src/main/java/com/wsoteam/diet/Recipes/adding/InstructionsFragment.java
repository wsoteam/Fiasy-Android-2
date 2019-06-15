package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wsoteam.diet.R;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructionsFragment extends Fragment {

    @BindView(R.id.svContainerInstructions) LinearLayout parentLinearLayout;
    @BindView(R.id.svInstructions) ScrollView scrollView;

    List<View> listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_instructions,
                container, false);
        ButterKnife.bind(this, view);

        listView = new LinkedList<>();

        return view;
    }

    @OnClick({R.id.btnAddStep})
    public void onViewClicked(View view) {
        onAddField();
    }

    private void onAddField(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adding_recipe_instruction_step, null);
        ImageButton delButton = rowView.findViewById(R.id.btnDel);
        TextView positionTextView = rowView.findViewById(R.id.tvPosition);

        listView.add(rowView);
        positionTextView.setText(String.valueOf(listView.indexOf(rowView) + 1));
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete(v);
            }
        });
        parentLinearLayout.addView(rowView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void updatePositions(List<View> views){

        for (int i = 0; i < views.size(); i++){
            TextView textView = views.get(i).findViewById(R.id.tvPosition);
            textView.setText(String.valueOf(i + 1));
        }

    }

    private void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
        listView.remove(v.getParent());
        updatePositions(listView);
    }

}
