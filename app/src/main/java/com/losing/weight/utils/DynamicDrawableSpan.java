
/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.losing.weight.utils;

import android.text.style.ReplacementSpan;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;

/**
 * Span that replaces the text it's attached to with a {@link Drawable} that can be aligned with
 * the bottom or with the baseline of the surrounding text.
 * <p>
 * For an implementation that constructs the drawable from various sources (<code>Bitmap</code>,
 * <code>Drawable</code>, resource id or <code>Uri</code>) use {@link ImageSpan}.
 * <p>
 * A simple implementation of <code>DynamicDrawableSpan</code> that uses drawables from resources
 * looks like this:
 * <pre>
 * class MyDynamicDrawableSpan extends DynamicDrawableSpan {
 *
 * private final Context mContext;
 * private final int mResourceId;
 *
 * public MyDynamicDrawableSpan(Context context, @DrawableRes int resourceId) {
 *     mContext = context;
 *     mResourceId = resourceId;
 * }
 *
 * {@literal @}Override
 * public Drawable getDrawable() {
 *      Drawable drawable = mContext.getDrawable(mResourceId);
 *      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
 *      return drawable;
 * }
 * }</pre>
 * The class can be used like this:
 * <pre>
 * SpannableString string = new SpannableString("Text with a drawable span");
 * string.setSpan(new MyDynamicDrawableSpan(context, R.mipmap.ic_launcher), 12, 20, Spanned
 * .SPAN_EXCLUSIVE_EXCLUSIVE);</pre>
 * <img src="{@docRoot}reference/android/images/text/style/dynamicdrawablespan.png" />
 * <figcaption>Replacing text with a drawable.</figcaption>
 */
public abstract class DynamicDrawableSpan extends ReplacementSpan {

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the bottom of the surrounding text, i.e., at the same level as the
     * lowest descender in the text.
     */
    public static final int ALIGN_BOTTOM = 0;

    /**
     * A constant indicating that the bottom of this span should be aligned
     * with the baseline of the surrounding text.
     */
    public static final int ALIGN_BASELINE = 1;

    protected final int mVerticalAlignment;

    public int rotateDegree;

    private WeakReference<Drawable> mDrawableRef;

    /**
     * Creates a {@link DynamicDrawableSpan}. The default vertical alignment is
     * {@link #ALIGN_BOTTOM}
     */
    public DynamicDrawableSpan() {
        mVerticalAlignment = ALIGN_BOTTOM;
    }

    /**
     * Creates a {@link DynamicDrawableSpan} based on a vertical alignment.\
     *
     * @param verticalAlignment one of {@link #ALIGN_BOTTOM} or {@link #ALIGN_BASELINE}
     */
    protected DynamicDrawableSpan(int verticalAlignment) {
        mVerticalAlignment = verticalAlignment;
    }

    /**
     * Returns the vertical alignment of this span, one of {@link #ALIGN_BOTTOM} or
     * {@link #ALIGN_BASELINE}.
     */
    public int getVerticalAlignment() {
        return mVerticalAlignment;
    }

    /**
     * Your subclass must implement this method to provide the bitmap
     * to be drawn.  The dimensions of the bitmap must be the same
     * from each call to the next.
     */
    public abstract Drawable getDrawable();

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
            @IntRange(from = 0) int start, @IntRange(from = 0) int end,
            @Nullable Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
            @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x,
            int top, int y, int bottom, @NonNull Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
        canvas.rotate(rotateDegree);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null) {
            d = wr.get();
        }

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }
}

