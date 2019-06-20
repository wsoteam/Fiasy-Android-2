package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructionsFragment extends Fragment {

    @BindView(R.id.svContainerInstructions) LinearLayout parentLinearLayout;
    @BindView(R.id.svInstructions) ScrollView scrollView;

    List<View> listView;
    RecipeItem recipeItem;
    List<String> listInstructions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_instructions,
                container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity)getActivity()).getRecipeItem();
        listInstructions = new ArrayList<>();
        recipeItem.setInstruction(listInstructions);

        List<String> instructions = recipeItem.getInstruction();
        listView = new LinkedList<>();

        if (instructions != null ){
            for (String str:
                 instructions) {
               onAddField();
            }

           for(int i = 0; i < listView.size(); i++){
               Log.d("testresult"," " + i);
               initRow(listView.get(i), instructions.get(i));
               Log.d("testresult", instructions.get(i));
           }
        }


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
        EditText editText = rowView.findViewById(R.id.etInstruction);



        listView.add(rowView);
        int index = listView.indexOf(rowView);
        listInstructions.add("");


        positionTextView.setText(String.valueOf(listView.indexOf(rowView) + 1));
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete(v);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listInstructions.set(index, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        int index = listView.indexOf(v.getParent());
        parentLinearLayout.removeView((View) v.getParent());
        listView.remove(v.getParent());
        listInstructions.remove(index);
        updatePositions(listView);
    }

    private void initRow(View v, String str){
        EditText editText = v.findViewById(R.id.etInstruction);
        editText.setText(str);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("testresult", "412345");
        List<String>  list = new ArrayList<>();
        for (View view:
             listView) {
            EditText editText = view.findViewById(R.id.etInstruction);
            String text = editText.getText().toString();
            if (text.length() > 0) {
                list.add(text);
                Log.d("testresult", text);
            }
        }
        recipeItem.setInstruction(list);
    }
}
