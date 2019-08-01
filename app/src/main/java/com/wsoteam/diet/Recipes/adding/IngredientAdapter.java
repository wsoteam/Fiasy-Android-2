package com.wsoteam.diet.Recipes.adding;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.Food;
import com.wsoteam.diet.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>{

    private List<Food> foods;
    private Context context;

    public IngredientAdapter(Context context, List<Food> foods){

        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adding_recipe_ingredients_item, viewGroup, false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int i) {
        ingredientViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView caloriesTextView;
        TextView weightTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tvName);
            caloriesTextView = itemView.findViewById(R.id.tvCalories);
            weightTextView = itemView.findViewById(R.id.tvPortion);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    foods.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        void bind(int position) {

            nameTextView.setText(foods.get(position).getName());
            caloriesTextView.setText((int)foods.get(position).getCalories() + " Ккал");
            weightTextView.setText((int)foods.get(position).getPortion() + " г");
        }
    }
}
