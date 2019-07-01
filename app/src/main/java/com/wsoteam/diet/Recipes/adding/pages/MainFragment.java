package com.wsoteam.diet.Recipes.adding.pages;

        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Switch;

        import com.wsoteam.diet.R;
        import com.wsoteam.diet.Recipes.POJO.RecipeItem;
        import com.wsoteam.diet.Recipes.adding.AddingRecipeActivity;

        import butterknife.BindView;
        import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    @BindView(R.id.tvNameRecipe) EditText nameEditText;
    @BindView(R.id.etPortionsRecipe) EditText portionsEditText;
    @BindView(R.id.etTimeRecipe) EditText timeEditText;
    @BindView(R.id.sComplexityRecipe) Spinner complexitySpinner;
    @BindView(R.id.switchShareRecipe) Switch shareSwitch;

    private RecipeItem recipeItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_main, container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity) getActivity()).getRecipeItem();
        String[] spinnerVlaue = getResources().getStringArray(R.array.recipeComplexity);

        if (recipeItem.getName() != null) {
            nameEditText.setText(recipeItem.getName());
        }

        if (recipeItem.getTime() != 0) {
            timeEditText.setText(String.valueOf(recipeItem.getTime()));
        }

        if (recipeItem.getPortions() != 0) {
            portionsEditText.setText(String.valueOf(recipeItem.getPortions()));
        }

        if (recipeItem.getComplexity() != null) {
            for (int i = 0; i < spinnerVlaue.length; i++) {
                if (recipeItem.getComplexity().equals(spinnerVlaue[i])) {
                    complexitySpinner.setSelection(i);
                }
            }
        }

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recipeItem.setName(s.toString());
                ((AddingRecipeActivity) getActivity()).updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        portionsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    recipeItem.setPortions(Integer.parseInt(s.toString()));
                } else {
                    recipeItem.setPortions(0);
                }
                ((AddingRecipeActivity) getActivity()).updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        timeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    recipeItem.setTime(Integer.parseInt(s.toString()));
                } else {
                    recipeItem.setTime(0);
                }
                ((AddingRecipeActivity) getActivity()).updateUI();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        complexitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recipeItem.setComplexity(complexitySpinner.getSelectedItem().toString());
                ((AddingRecipeActivity) getActivity()).updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        shareSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) getActivity());

        return view;
    }

    private void setSelection() {

        if (nameEditText != null && nameEditText.getText().length() == 0) {
            nameEditText.requestFocus();
        } else if (portionsEditText != null && portionsEditText.getText().length() == 0) {
            portionsEditText.requestFocus();
        } else if (timeEditText != null && timeEditText.getText().length() == 0) {
            timeEditText.requestFocus();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            setSelection();
        }
    }

}
