package com.losing.weight.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.losing.weight.R
import com.losing.weight.utils.dp
import com.losing.weight.utils.hideKeyboard

class SearchCardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val LAYOUT_ID = R.layout.search_card_view
    private val CORNER_RADIUS_DP = 16f


    private var onTextChangeListener: ((s: String, isClickSearchBtn: Boolean) -> Unit)? = null
    private var speakOnClickListener: OnClickListener? = null

    fun setOnTextChangeListener(onTextChangeListener: ((s: String, isClickSearchBtn: Boolean) -> Unit)?){
        this.onTextChangeListener = onTextChangeListener
    }

    fun setSpeakOnClickListener(speakOnClickListener: OnClickListener?){
        this.speakOnClickListener = speakOnClickListener
    }

    private val editText: EditText
    private val cleanEditText: ImageView
    private val speak: ImageView

    private var hint = ""
    private var speakEnable = false

    init {
        inflate(context, LAYOUT_ID, this)
        radius = dp(context, CORNER_RADIUS_DP).toFloat()
        editText = findViewById(R.id.searchText)
        cleanEditText = findViewById(R.id.actionCleanText)
        speak = findViewById(R.id.actionSpeak)

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.SearchCardView,
                0, 0).apply {
            try {
                hint = getString(R.styleable.SearchCardView_hint) ?: ""
                speakEnable = getBoolean(R.styleable.SearchCardView_speakEnable, false)
            } finally {
                recycle()
            }
        }

        speak.visibility = if (speakEnable) View.VISIBLE else View.GONE
        speak.setOnClickListener { speakOnClickListener?.onClick(it) }

        editText.hint = hint
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cleanEditText.visibility = if (s?.length ?: 0 > 0) View.VISIBLE else View.GONE
                onTextChangeListener?.invoke(s.toString(), false)
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                onTextChangeListener?.invoke(editText.text.toString(), true)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        cleanEditText.setOnClickListener {
            editText.text = null
            hideKeyboard()
        }
    }
}