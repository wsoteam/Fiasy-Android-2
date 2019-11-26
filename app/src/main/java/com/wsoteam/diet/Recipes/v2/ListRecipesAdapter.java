package com.wsoteam.diet.Recipes.v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.EmptyViewHolder;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.utils.Img;
import com.wsoteam.diet.utils.Subscription;

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ListRecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecipeItem> listRecipes;
    private Context context;

    private final int EMPTY_VH = 0, DEFF_VH = 1;

    public ListRecipesAdapter(List<RecipeItem> listRecipes) {
        this.listRecipes = listRecipes;

    }

    public void setData(List<RecipeItem> listRecipes){
        this.listRecipes = listRecipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) this.context = parent.getContext();

        switch (viewType){
            case EMPTY_VH: return new EmptyViewHolder(parent);
            default:
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.recipe_single, parent, false);
                return new ListRecipesAdapter.ListRecipeViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) return;
        ((ListRecipeViewHolder)holder).bind(position - 1);

    }

    @Override
    public int getItemCount() {
        if (listRecipes == null) return 1;
        return listRecipes.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return EMPTY_VH;
        else return DEFF_VH;
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
                    if (Subscription.check(context)) {
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


            textView.setText(name);
            textViewKK.setText(String.valueOf(kk));
            setImg(imageView, url, background);

        }

        private void setImg(ImageView img, String url, LinearLayout layout){
            Picasso.get()
                    .load(url)
                    .fit().centerCrop()
                    .into(img, new Callback() {
                        @Override
                        public void onSuccess() {
                            Img.setBackGround(img.getDrawable(), layout);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
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
    }
}

