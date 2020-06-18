package com.losing.weight.Recipes.adding.pages;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.losing.weight.R;
import com.losing.weight.Recipes.POJO.RecipeItem;
import com.losing.weight.Recipes.adding.AddingRecipeActivity;
import com.losing.weight.Recipes.adding.InstructionAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructionsFragment extends Fragment {

    @BindView(R.id.rvInstructions) RecyclerView recyclerView;

    private RecipeItem recipeItem;
    private List<String> instructions;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adding_recipe_instructions,
                container, false);
        ButterKnife.bind(this, view);

        recipeItem = ((AddingRecipeActivity) getActivity()).getRecipeItem();
        instructions = recipeItem.getInstruction();
        adapter = new InstructionAdapter(getContext(), instructions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @OnClick({R.id.btnAddStep})
    public void onViewClicked(View view) {

        if (instructions.size() < 99) {
           alertDialog(adapter, getContext(), instructions);
        }
    }

    private AlertDialog alertDialog(RecyclerView.Adapter adapter, Context context, List<String> instruction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_add_instruction, null);
        Button cancelButton = view.findViewById(R.id.btnCancel);
        Button addButton = view.findViewById(R.id.btnAdd);
        EditText editText = view.findViewById(R.id.etInstruction);

        alertDialog.setView(view);
        alertDialog.show();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instruction.add(editText.getText().toString().trim());
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
                ((AddingRecipeActivity) getActivity()).updateUI();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3 && s.toString().trim().length() > 3){
                    addButton.setEnabled(true);
                    addButton.setTextColor(Color.parseColor("#ef7d02"));
                } else {
                    addButton.setEnabled(false);
                    addButton.setTextColor(Color.parseColor("#8a000000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return alertDialog;

    }
}
