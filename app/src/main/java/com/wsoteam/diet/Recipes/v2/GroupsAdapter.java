package com.wsoteam.diet.Recipes.v2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.EmptyViewHolder;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Sync.UserDataHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.text.TextUtils.concat;

public class GroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ListRecipes> groupsRecipes;
    Context context;
    Fragment groupsFragment;
    FragmentTransaction transaction;
    int containerID;

    private final int EMPTY_VH = 0, DEFF_VH = 1;

    public GroupsAdapter(List<ListRecipes> groupsRecipes, Fragment groupsFragment, int containerID) {
        this.groupsRecipes = groupsRecipes;
        this.groupsFragment = groupsFragment;
        this.containerID = containerID;
        this.transaction = groupsFragment.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.context == null) this.context = parent.getContext();


        switch (viewType) {
            case EMPTY_VH: return new  EmptyViewHolder(parent);
            default:
                return new GroupsAdapter.GroupsViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.recipes_group_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) return;
        ((GroupsViewHolder)holder).bind(position - 1);
    }

    @Override
    public int getItemCount() {
        if (groupsRecipes != null) return groupsRecipes.size() + 1;
        else return 1;
    }

    @Override
    public int getItemViewType(int position) {
       if (position == 0) return EMPTY_VH;
       else return DEFF_VH;
    }

    class GroupsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        List<ImageView> imageViewList;
        List<TextView> textViewList;
        List<View> viewList;
        List<TextView> textViewsKK;
        List<LinearLayout> backList;
        List<ImageView> likeList;


        public GroupsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGroupName);

            imageViewList = new ArrayList<>();
            textViewList = new ArrayList<>();
            viewList = new ArrayList<>();
            textViewsKK = new ArrayList<>();
            backList = new ArrayList<>();
            likeList = new ArrayList<>();

            TextView detailTextView = itemView.findViewById(R.id.tvAllRecipes);
            detailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Events.RECIPES_BUNDLE, getAdapterPosition());
//
//                    ListRecipesFragment fragment = new ListRecipesFragment();
//                    fragment.setArguments(bundle);
//
//                    transaction.replace(containerID, fragment);
//                    transaction.addToBackStack(Events.RECIPE_BACK_STACK);
//                    transaction.commit();
                    Intent intent = new Intent(context, ListRecipesActivity.class);
                    intent.putExtra(Config.RECIPES_BUNDLE, getAdapterPosition() - 1);
                    groupsFragment.startActivity(intent);
                }
            });

            viewList.add(itemView.findViewById(R.id.view1));
            viewList.add(itemView.findViewById(R.id.view2));
            viewList.add(itemView.findViewById(R.id.view3));
            viewList.add(itemView.findViewById(R.id.view4));
            viewList.add(itemView.findViewById(R.id.view5));

            for (int i = 0 ; i < viewList.size(); i++){
                imageViewList.add(viewList.get(i).findViewById(R.id.imageRecipe));
                textViewList.add(viewList.get(i).findViewById(R.id.textRecipe));
                textViewsKK.add(viewList.get(i).findViewById(R.id.tvRecipeKK));
                backList.add(viewList.get(i).findViewById(R.id.llBackground));
                likeList.add(viewList.get(i).findViewById(R.id.ivLike));
            }

            for (int i = 0; i < 5; i++) {
                int y = i;
                viewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
//                        int bais = getAdapterPosition() * 5;
                        int bais = getAdapterPosition();
                        if (checkSubscribe()){
                            intent = new Intent(groupsFragment.getActivity(), RecipeActivity.class);
                        }else {
                            intent = new Intent(groupsFragment.getActivity(), BlockedRecipeActivity.class);
                        }

                        intent.putExtra(Config.RECIPE_INTENT, groupsRecipes.get(getAdapterPosition() - 1).getListrecipes().get(y + bais));
                        groupsFragment.getActivity().startActivity(intent);
                    }
                });
            }

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

        void bind(int listIndex) {

            if (groupsRecipes.get(listIndex).getListrecipes() == null
                || groupsRecipes.get(listIndex).getListrecipes().size() == 0){

                itemView.setVisibility(View.GONE);
                return;
            } else {
                itemView.setVisibility(View.VISIBLE);
            }

            tvTitle.setText(groupsRecipes.get(listIndex).getName());
            //int bais = getAdapterPosition() * 5;
            int bais = getAdapterPosition();
            int border = 5;
            int listSize = groupsRecipes.get(listIndex).getListrecipes().size();
            if (listSize < 5) {
                border = listSize;

                for (int i = border; i < 5; i++) {
                    viewList.get(i).setVisibility(View.GONE);
                }
            }


            for (int i = 0; i < border; i++) {
                String name = groupsRecipes.get(listIndex).getListrecipes().get(i + bais).getName();
                String url = groupsRecipes.get(listIndex).getListrecipes().get(i + bais).getUrl();
                int kk = groupsRecipes.get(listIndex).getListrecipes().get(i + bais).getCalories();

                if (url == null || url.equals("link")) {
                    url = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/loading.jpg?alt=media&token=f1b6fe6d-57e3-4bca-8be3-9ebda9dc715e";
                }

                Picasso.get()
                        .load(url)
                        .fit().centerCrop()
                        .into(imageViewList.get(i));

                setBackGround(url, backList.get(i));
               if (checkFavorite(groupsRecipes.get(listIndex).getListrecipes().get(i))){
                   likeList.get(i).setImageResource(R.drawable.ic_like_on);
               } else {
                   likeList.get(i).setImageResource(R.drawable.ic_like_off);
               }

                textViewList.get(i).setText(name);
                textViewsKK.get(i).setText(String.valueOf(kk));
            }
        }

        private void setBackGround(String url, LinearLayout layout){
            Picasso.get().load(url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Palette p = Palette.from(bitmap).generate();
                                int mainColor = p.getMutedColor(0);
                                int alphaColor = 191;
                                layout.setBackgroundColor(ColorUtils.setAlphaComponent(mainColor, alphaColor));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
        }


        private boolean checkSubscribe() {
            SharedPreferences sharedPreferences = groupsFragment.getActivity().getSharedPreferences(Config.STATE_BILLING, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Config.STATE_BILLING, false)) {
                return true;
            } else {
                return false;
            }
        }

    }
}

