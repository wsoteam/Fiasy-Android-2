package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wsoteam.diet.R;

import java.util.List;

public class AddInstructionAlertDialog {

    public static AlertDialog init(RecyclerView.Adapter adapter, Context context, List<String> instruction) {
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

    public static AlertDialog init(RecyclerView.Adapter adapter, Context context, List<String> instruction, int position) {
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
                instruction.set(position, editText.getText().toString().trim());
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
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

        editText.setText(instruction.get(position));

        return alertDialog;
    }
}
