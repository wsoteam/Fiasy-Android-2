package com.wsoteam.diet.Recipes.v2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.EmptyViewHolder;
import com.wsoteam.diet.Recipes.POJO.ListRecipes;
import com.wsoteam.diet.Recipes.POJO.RecipeItem;
import com.wsoteam.diet.Recipes.RecipeUtils;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.utils.Img;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class GroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ListRecipes> groupsRecipes;
    Context context;
    Fragment groupsFragment;
    FragmentTransaction transaction;
    int containerID;

    private final int EMPTY_VH = 0, DEFF_VH = 1;

    public GroupsAdapter(List<ListRecipes> groupsRecipes, Fragment groupsFragment, int containerID) {
        this.groupsRecipes = sortRecipe(groupsRecipes);
        this.groupsFragment = groupsFragment;
        this.containerID = containerID;
        this.transaction = groupsFragment.getActivity().getSupportFragmentManager().beginTransaction();
    }

    private List<ListRecipes> sortRecipe(List<ListRecipes> groups){
        HashSet<RecipeItem> buffer = new LinkedHashSet<>();

        for (ListRecipes listRecipe: groups) {
            List<RecipeItem> recipes = listRecipe.getListrecipes();

            for (int i = 0; i < recipes.size(); i++){
                if (!recipes.get(i).isPremium() && !buffer.contains(recipes.get(i))){

                    RecipeItem item = recipes.get(i);
                    recipes.remove(recipes.get(i));
                    recipes.add(0, item);
                    buffer.add(item);
                    break;
                }
            }


            Iterator itr = recipes.iterator();
            boolean firstItem = true;
            while (itr.hasNext())
            {
                RecipeItem recipeItem = (RecipeItem) itr.next();
                if (buffer.contains(recipeItem) && !firstItem){
                    itr.remove();
                }
                firstItem = false;
            }

        }

        return groups;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.context == null) this.context = parent.getContext();


        switch (viewType) {
            case EMPTY_VH:
                return new EmptyViewHolder(parent);
            default:
                return new GroupsAdapter.GroupsViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.recipes_group_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) return;
        ((GroupsViewHolder) holder).bind(position - 1);
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
        List<ConstraintLayout> premiumList;


        public GroupsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGroupName);

            imageViewList = new ArrayList<>();
            textViewList = new ArrayList<>();
            viewList = new ArrayList<>();
            textViewsKK = new ArrayList<>();
            backList = new ArrayList<>();
            likeList = new ArrayList<>();
            premiumList = new ArrayList<>();

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

            for (int i = 0; i < viewList.size(); i++) {
                imageViewList.add(viewList.get(i).findViewById(R.id.imageRecipe));
                textViewList.add(viewList.get(i).findViewById(R.id.textRecipe));
                textViewsKK.add(viewList.get(i).findViewById(R.id.tvRecipeKK));
                backList.add(viewList.get(i).findViewById(R.id.llBackground));
                likeList.add(viewList.get(i).findViewById(R.id.ivLike));
                premiumList.add(viewList.get(i).findViewById(R.id.premiumLabel));
            }

            for (int i = 0; i < 5; i++) {
                int y = i;
                viewList.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        int bais = getAdapterPosition() * 5;
//                        int bais = getAdapterPosition();
                        int bais = 0;
                        RecipeUtils.startDetailActivity(getContext(),
                                groupsRecipes.get(getAdapterPosition() - 1).getListrecipes().get(y + bais));

                    }
                });
            }

        }

        private Context getContext(){
            return itemView.getContext();
        }

        private boolean checkFavorite(RecipeItem recipeItem) {
            if (UserDataHolder.getUserData() != null &&
                    UserDataHolder.getUserData().getFavoriteRecipes() != null) {

                for (Map.Entry<String, RecipeItem> e : UserDataHolder.getUserData().getFavoriteRecipes().entrySet()) {
                    if (recipeItem.getName().equals(e.getValue().getName())) {
                        return true;
                    }
                }
            }
            return false;
        }

        void bind(int listIndex) {

            if (groupsRecipes.get(listIndex).getListrecipes() == null
                    || groupsRecipes.get(listIndex).getListrecipes().size() == 0) {

                itemView.setVisibility(View.GONE);
                return;
            } else {
                itemView.setVisibility(View.VISIBLE);
            }



            tvTitle.setText(groupsRecipes.get(listIndex).getName());
            //int bais = getAdapterPosition() * 5;
//            int bais = getAdapterPosition();
            int bais = 0;
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

                setImg(imageViewList.get(i), url, backList.get(i));

                if (checkFavorite(groupsRecipes.get(listIndex).getListrecipes().get(i))) {
                    likeList.get(i).setImageResource(R.drawable.ic_like_on);
                } else {
                    likeList.get(i).setImageResource(R.drawable.ic_like_off);
                }

                textViewList.get(i).setText(name);
                textViewsKK.get(i).setText(String.valueOf(kk));
                premiumList.get(i).setVisibility(
                        groupsRecipes.get(listIndex).getListrecipes().get(i).isPremium() ?
                        View.VISIBLE : View.GONE);
            }
        }

        private void setImg(ImageView img, String url, LinearLayout layout){
            Picasso.get()
                    .load(url)
                    .resizeDimen(R.dimen.receipt_container_width, R.dimen.receipt_container_height)
                    .centerCrop()
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

    }
}

