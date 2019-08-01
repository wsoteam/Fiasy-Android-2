package com.wsoteam.diet.BranchOfExercises.FragmentsOfMainScreen;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsoteam.diet.BranchOfExercises.ActivitiesArticles.ActivityDetailAcrticles;
import com.wsoteam.diet.BranchOfExercises.ObjectHolder;
import com.wsoteam.diet.POJOSExercises.AllWholeArticles;
import com.wsoteam.diet.POJOSExercises.WholeArticle;
import com.wsoteam.diet.R;

import java.util.ArrayList;



public class FragmentsArticles extends Fragment {
    private static String TAG_OF_FRAGMENT = "FragmentsArticles";
    private RecyclerView listOfArticlesRecyclerView;
    private AllWholeArticles allWholeArticles;

    /*public static FragmentsArticles newInstance(AllWholeArticles allWholeArticles) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG_OF_FRAGMENT, allWholeArticles);

        FragmentsArticles fragmentsArticles = new FragmentsArticles();
        fragmentsArticles.setArguments(bundle);
        return fragmentsArticles;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ex_fragment_articles, container, false);
        //allWholeArticles = (AllWholeArticles) getArguments().getSerializable(TAG_OF_FRAGMENT);
        allWholeArticles = ObjectHolder.getGlobalObject().getWholeArticles();
        listOfArticlesRecyclerView = view.findViewById(R.id.ex_rvListOfArticles);
        listOfArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listOfArticlesRecyclerView.setAdapter(new ArticleAdapter((ArrayList<WholeArticle>) allWholeArticles.getWholeArticleList()));

        return view;
    }

    private class ArticleVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleOfArticle;

        public ArticleVH(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.ex_item_fragment_articles_list, viewGroup, false));
            titleOfArticle = itemView.findViewById(R.id.ex_tvTitleOfArticleItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ActivityDetailAcrticles.class);
            intent.putExtra(ActivityDetailAcrticles.TAG, getAdapterPosition());
            startActivity(intent);
        }

        public void bind(WholeArticle wholeArticle) {
            titleOfArticle.setText(wholeArticle.getTitle());
        }
    }

    private class ArticleAdapter extends RecyclerView.Adapter<ArticleVH> {
        ArrayList<WholeArticle> wholeArticles;

        public ArticleAdapter(ArrayList<WholeArticle> wholeArticles) {
            this.wholeArticles = wholeArticles;
        }

        @NonNull
        @Override
        public ArticleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ArticleVH(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleVH holder, int position) {
            holder.bind(wholeArticles.get(position));
        }

        @Override
        public int getItemCount() {
            return wholeArticles.size();
        }
    }
}
