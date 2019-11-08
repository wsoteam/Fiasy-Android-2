package com.wsoteam.diet.Recipes.v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ListRecipesAdapter extends RecyclerView.Adapter<ListRecipesAdapter.ListRecipeViewHolder> {

    private List<RecipeItem> listRecipes;
    private Context context;


    public ListRecipesAdapter(List<RecipeItem> listRecipes) {
        this.listRecipes = listRecipes;

    }

    public void setData(List<RecipeItem> listRecipes){
        this.listRecipes = listRecipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListRecipesAdapter.ListRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_single, parent, false);
        return new ListRecipesAdapter.ListRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipesAdapter.ListRecipeViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        if (listRecipes == null) return 0;
        return listRecipes.size();
    }

    class ListRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textViewKK;
        LinearLayout background;
        ImageView like;

        RecipeItem recipeItem;

        public ListRecipeViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageRecipe);
            textView = itemView.findViewById(R.id.textRecipe);
            textViewKK = itemView.findViewById(R.id.tvRecipeKK);
            background = itemView.findViewById(R.id.llBackground);
            like = itemView.findViewById(R.id.ivLike);

            textViewKK.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if (checkSubscribe()) {
                        intent = new Intent(context, RecipeActivity.class);

                    } else {
                        intent = new Intent(context, BlockedRecipeActivity.class);
                    }

//                    intent.putExtra(Config.RECIPE_INTENT, listRecipes.get(getAdapterPosition()));
                    intent.putExtra(Config.RECIPE_INTENT, recipeItem);

                    context.startActivity(intent);
                }
            });
        }

        void bind(int position) {
            this.recipeItem = listRecipes.get(position);

            String name = listRecipes.get(position).getName();
            String url = listRecipes.get(position).getUrl();
            int kk = listRecipes.get(position).getCalories();

            if (url == null || url.equals("link")) {
                url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
            }

            if (checkFavorite(listRecipes.get(position))){
                like.setImageResource(R.drawable.ic_like_on);
            } else {
                like.setImageResource(R.drawable.ic_like_off);
            }

            setBackGround(listRecipes.get(position).getUrl(), background);
            textView.setText(name);
            textViewKK.setText(String.valueOf(kk));
            Glide
                    .with(context)
                    .load(url)
                    .into(imageView);
        }

        private boolean checkFavorite(RecipeItem recipeItem){
            if (UserDataHolder.getUserData() != null &&
                    UserDataHolder.getUserData().getFavoriteRecipes() != null){
                for (Map.Entry<String, RecipeItem> e : UserDataHolder.getUserData().getFavoriteRecipes().entrySet()) {
                    if (recipeItem.getName().equals(e.getValue().getName())){
                        return true;
                    }
                }
            }
            return false;
        }

        private void setBackGround(String url, LinearLayout layout){
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Palette p = Palette.from(resource).generate();
                            int mainColor = p.getMutedColor(0);
                            int alphaColor = 191;
                            layout.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));

                        }
                    });
        }

        private boolean checkSubscribe() {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
                return true;
            } else {
                return false;
            }
        }
    }
}

