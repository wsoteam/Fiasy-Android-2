package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wsoteam.diet.R;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>{

    Context context;
    List<String> instructions;
    InstructionAdapter instructionAdapter = this;

    public InstructionAdapter(Context context, List<String> instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adding_recipe_instruction_step, viewGroup, false);
        return new InstructionAdapter.InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder instructionViewHolder, int i) {
        instructionViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }


    class InstructionViewHolder extends RecyclerView.ViewHolder {

        TextView positionTextView;
        TextView instructionTextView;
        ImageButton deleteButton;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);

            positionTextView = itemView.findViewById(R.id.tvPosition);
            instructionTextView = itemView.findViewById(R.id.tvInstruction);
            deleteButton = itemView.findViewById(R.id.btnDel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddInstructionAlertDialog.init(instructionAdapter, context, instructions, getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    instructions.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        void bind(int position){
            positionTextView.setText(String.valueOf(position + 1));
            instructionTextView.setText(instructions.get(position));
        }
    }
}
