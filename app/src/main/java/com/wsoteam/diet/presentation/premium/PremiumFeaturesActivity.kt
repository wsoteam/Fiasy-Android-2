package com.wsoteam.diet.presentation.premium

import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wsoteam.diet.R
import com.wsoteam.diet.utils.IntentUtils
import com.wsoteam.diet.utils.RichTextUtils
import com.wsoteam.diet.utils.asCurrency
import com.wsoteam.diet.utils.dp
import com.wsoteam.diet.views.DotIndicatorView
import com.wsoteam.diet.views.PremiumPlanCardView
import io.reactivex.disposables.CompositeDisposable

class PremiumFeaturesActivity : AppCompatActivity() {
  companion object {
    val plans = arrayOf(
        PremiumPlan(key = "premium_year",
            price = 949,
            duration = R.string.premium_subscription_1_year,
            diamondStyleId = R.drawable.ic_blue_diamond,
            monthlyPrice = 79,
            benefitPercentage = 83,
            benefitTintColor = Color.WHITE,
            benefitTextColor = Color.BLACK,
            prevMonthlyPrice = 316
        ),

        PremiumPlan(key = "premium_6month",
            price = 1490,
            duration = R.string.premium_subscription_6_month,
            diamondStyleId = R.drawable.ic_gold_diamond,
            monthlyPrice = 248,
            benefitPercentage = 50,
            benefitTintColor = 0xFFEFB476.toInt(),
            benefitTextColor = Color.WHITE,
            prevMonthlyPrice = 316
        ),

        PremiumPlan(key = "premium_3month",
            price = 990,
            duration = R.string.premium_subscription_3_month,
            diamondStyleId = R.drawable.ic_silver_diamond,
            monthlyPrice = 330,
            benefitPercentage = 29,
            benefitTintColor = 0xFFEFB476.toInt(),
            benefitTextColor = Color.WHITE,
            prevMonthlyPrice = -1
        )
    )
  }

  private val disposables = CompositeDisposable()
  private var purchasedId: String? = null

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

    disposables.add(SubscriptionManager.purchases()
      .filter { it.sku == purchasedId }
      .doOnNext {
        finish()

        IntentUtils.openMainActivity(this)
      }
      .subscribe())

    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    toolbar.setNavigationOnClickListener {
      finish()
    }

    val buyButton = findViewById<View>(R.id.action_buy)
    val tariffsContainer = findViewById<RecyclerView>(R.id.tarifs)

    buyButton.setOnClickListener {
      val lm = tariffsContainer.layoutManager as LinearLayoutManager
      val id = lm.findFirstCompletelyVisibleItemPosition()

      purchase(plans[id].key)
    }

    tariffsContainer.adapter = PlansAdapter(isDarkMode = isDarkTheme)
    tariffsContainer.addOnScrollListener(object : OnScrollListener() {
      override fun onScrollStateChanged(p: RecyclerView, newState: Int) {
        super.onScrollStateChanged(p, newState)

        val lm = p.layoutManager as LinearLayoutManager

        val color: ColorStateList?
        if (newState == RecyclerView.SCROLL_STATE_IDLE &&
            lm.findFirstCompletelyVisibleItemPosition() == 0) {
          color = ContextCompat.getColorStateList(p.context, R.color.premium_button_buy_gold_color)
        } else {
          color = ContextCompat.getColorStateList(p.context, R.color.premium_button_buy_ordinary_color)
        }

        ViewCompat.setBackgroundTintList(buyButton, color)
      }
    })

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

  fun purchase(subscriptionId: String){
    purchasedId = subscriptionId

    disposables.add(SubscriptionManager
      .buy(this, purchasedId ?: "premium_year")
      .subscribe())
  }

  override fun onDestroy() {
    super.onDestroy()

    disposables.clear()
  }

  data class PremiumPlan(
    val key: String,
    val price: Int,
    val duration: Int,
    val diamondStyleId: Int,
    val monthlyPrice: Int,
    val benefitPercentage: Int,
    val prevMonthlyPrice: Int,
    val benefitTintColor: Int,
    val benefitTextColor: Int
  )

  public class PlanHolder(val view: PremiumPlanCardView) : ViewHolder(view)

  open class PlansAdapter(val isDarkMode: Boolean = false) : Adapter<PlanHolder>() {
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
        if (isDarkMode) {
          holder.view.setBackgroundResource(R.drawable.premium_annual_plan_gold_background)
        } else {
          holder.view.setBackgroundResource(R.drawable.premium_annual_plan_background)
        }
      } else {
        holder.view.setBackgroundResource(R.color.white)
      }

      holder.view.diamondStyle.setImageResource(plans[position].diamondStyleId)
      holder.view.duration.setText(plans[position].duration)
      holder.view.price.text = plans[position].price.asCurrency
      holder.view.helper.text = holder.view.context
        .getString(R.string.premium_subscription_per_month, plans[position].monthlyPrice.asCurrency)

      if (plans[position].prevMonthlyPrice > 0) {
        val message = holder.view.context.getString(R.string.premium_subscription_per_month,
            plans[position].prevMonthlyPrice.asCurrency)

        holder.view.helper2.text = RichTextUtils.strikethrough(message)
      } else {
        holder.view.helper2.text = ""
      }

      holder.view.setBenefitsPercentage(
          plans[position].benefitPercentage,
          plans[position].benefitTintColor,
          plans[position].benefitTextColor
      )
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
