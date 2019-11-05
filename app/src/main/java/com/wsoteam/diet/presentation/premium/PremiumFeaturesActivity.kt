package com.wsoteam.diet.presentation.premium

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.asCurrency
import com.wsoteam.diet.utils.dp
import com.wsoteam.diet.views.DotIndicatorView
import com.wsoteam.diet.views.PremiumPlanCardView

class PremiumFeaturesActivity : AppCompatActivity() {

  private val isDarkTheme: Boolean
    get() = intent.getBooleanExtra("darkMode", true)

  public override fun onCreate(savedInstanceState: Bundle?) {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

    if (isDarkTheme) {
      setTheme(R.style.AppTheme_Premium)
      window.setBackgroundDrawableResource(R.color.premium_background_color_dark)
    } else {
      setTheme(R.style.AppTheme_Premium_Light)
      window.setBackgroundDrawableResource(R.color.premium_background_color_light)
    }

    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_premium_dark)

    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener {
      finish()
    }

    val tariffsContainer = findViewById<RecyclerView>(R.id.tarifs)
    tariffsContainer.adapter = PlansAdapter()

    val tariffsSnap = PagerSnapHelper()
    tariffsSnap.attachToRecyclerView(tariffsContainer)

    val adapter = ReviewsAdapter()

    val dotsIndicator = findViewById<DotIndicatorView>(R.id.reviews_page_indicator)
    dotsIndicator.setCirclesCount(adapter.itemCount)

    val reviewsContainer = findViewById<RecyclerView>(R.id.reviews)
    reviewsContainer.adapter = adapter
    reviewsContainer.addOnScrollListener(object : OnScrollListener() {
      override fun onScrollStateChanged(p: RecyclerView, newState: Int) {
        super.onScrollStateChanged(p, newState)

        val lm = p.layoutManager as LinearLayoutManager

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          dotsIndicator.setActiveIndex(lm.findFirstCompletelyVisibleItemPosition())
        }
      }
    })

    val reviewsSnap = PagerSnapHelper()
    reviewsSnap.attachToRecyclerView(reviewsContainer)

    findViewById<View>(R.id.action_open_subscriptions).setOnClickListener {
      supportFragmentManager
        .beginTransaction()
        .add(android.R.id.content, SubscriptionsListFragment())
        .addToBackStack(null)
        .commit()
    }
  }

  data class PremiumPlan(
    val key: String,
    val price: Int,
    val duration: Int,
    val diamondStyleId: Int,
    val monthlyPrice: Int
  )

  public class PlanHolder(val view: PremiumPlanCardView) : ViewHolder(view)

  open class PlansAdapter : Adapter<PlanHolder>() {
    val plans = arrayOf(
        PremiumPlan("premium_year",
            949,
            R.string.premium_subscription_1_year,
            R.drawable.ic_blue_diamond,
            79),
        PremiumPlan("premium_year",
            1450,
            R.string.premium_subscription_6_month,
            R.drawable.ic_gold_diamond,
            241),
        PremiumPlan("premium_year",
            949,
            R.string.premium_subscription_3_month,
            R.drawable.ic_silver_diamond,
            316)
    )

    open fun getLayoutParams(parent: ViewGroup, viewType: Int): LayoutParams {
      val width = parent.context.resources.displayMetrics.widthPixels

      return MarginLayoutParams(width - dp(parent.context, 64f), WRAP_CONTENT)
        .apply {
          leftMargin = dp(parent.context, 16f)
          rightMargin = dp(parent.context, 16f)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanHolder {
      val view = PremiumPlanCardView(parent.context)
      view.layoutParams = getLayoutParams(parent, viewType)
      return PlanHolder(view)
    }

    override fun getItemCount(): Int {
      return plans.size
    }

    override fun onBindViewHolder(holder: PlanHolder, position: Int) {
      if (position == 0) {
        holder.view.setBackgroundResource(R.drawable.premium_annual_plan_background)
      } else {
        holder.view.setBackgroundResource(R.color.white)
      }

      holder.view.diamondStyle.setImageResource(plans[position].diamondStyleId)
      holder.view.duration.setText(plans[position].duration)
      holder.view.price.text = plans[position].price.asCurrency
      holder.view.helper.text = holder.view.context
        .getString(R.string.premium_subscription_per_month, plans[position].monthlyPrice.asCurrency)
    }

  }

  private class ReviewHolder(private val image: ImageView) : RecyclerView.ViewHolder(image) {
    fun bind(imageId: Int) {
      image.setImageResource(imageId)
    }
  }

  private class ReviewsAdapter : RecyclerView.Adapter<ReviewHolder>() {
    private val reviews = intArrayOf(
        R.drawable.review_1,
        R.drawable.review_2,
        R.drawable.review_3
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
      val params = MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
      params.leftMargin = dp(parent.context, 16f)
      params.rightMargin = dp(parent.context, 16f)

      val view = ImageView(parent.context)
      view.layoutParams = params
      view.scaleType = CENTER_INSIDE

      return ReviewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
      holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
      return reviews.size
    }
  }
}
