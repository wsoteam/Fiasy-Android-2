package com.losing.weight.presentation.premium

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.losing.weight.Config
import com.losing.weight.InApp.ActivitySubscription
import com.losing.weight.R
import com.losing.weight.Recipes.POJO.RecipeItem
import com.losing.weight.Recipes.v2.HorizontalRecipesAdapter
import kotlinx.android.synthetic.main.fragment_anastasia_story.*
import java.util.*


class AnastasiaStoryFragment : AppCompatActivity(R.layout.fragment_anastasia_story) {

    private val adapter = HorizontalRecipesAdapter(getRecipes())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar2.setNavigationIcon(R.drawable.arrow_back_gray)
        toolbar2.setNavigationOnClickListener { onBackPressed() }
        val box = intent.getSerializableExtra(Config.TAG_BOX)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        anastasia_recycler.layoutManager = linearLayoutManager
        anastasia_recycler.adapter = adapter


        appbar.setLiftable(true)
        scroll.setOnScrollChangeListener {
            v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            appbar.setLiftable(scrollY == 0) }

        next.setOnClickListener {
            var intent = Intent(this, ActivitySubscription::class.java)
            intent.putExtra(Config.TAG_BOX, box)
            startActivity(intent)
        }
    }


    private fun getRecipes(): List<RecipeItem>{
        val list = LinkedList<RecipeItem>()
        list.add(RecipeItem("https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F230%2F239.jpg?alt=media&token=2d015c38-fb4d-49ff-814e-fcc2f688db98",
                "Куриные филе с картофельным салатом", 650, false))
        list.add(RecipeItem("https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F190%2F205.jpg?alt=media&token=1abc5b37-6310-4ee5-ad6b-56b538d45c60",
                "Свежие весенние рулеты", 521, false))
        return list
    }
}
