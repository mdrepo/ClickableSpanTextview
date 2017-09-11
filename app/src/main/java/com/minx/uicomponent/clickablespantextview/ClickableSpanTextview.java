package com.minx.uicomponent.clickablespantextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

import com.minx.uicomponent.clickablespantextview.R;

/**
 * Created by Mayur on 11/9/17.
 */

public class ClickableSpanTextview extends AppCompatTextView {
    // default color
    private int mHighlightColor;
    private String mHighlightText;
    private String mText;
    private OnHighlightClickListener mListener;

    public ClickableSpanTextview(Context context) {
        super(context);
    }

    public ClickableSpanTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClickableSpanTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setListener(OnHighlightClickListener mListener) {
        this.mListener = mListener;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ClickableTextView, 0, 0);

        mHighlightText = a.getString(R.styleable.ClickableTextView_highlightText);
        mHighlightColor = a.getColor(R.styleable.ClickableTextView_highlightTextColor,
                ContextCompat.getColor(context,R.color.accent_blue));
        mText = getText().toString();
        setMovementMethod(new LinkMovementMethod());
        setText(mText, BufferType.SPANNABLE);
        a.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        SpannableString spannableString = new SpannableString(text);
        if (shouldHighlight()) {
            int index = text.toString().indexOf(mHighlightText);
            if (index != -1) {
                spannableString = getHighlightedSpannable(text, index, mHighlightText);
                spannableString.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (mListener != null) {
                            mListener.onHighlightClicked(mHighlightText);
                        }
                    }
                }, index, index + mHighlightText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(spannableString, BufferType.SPANNABLE);
    }

    private SpannableString getHighlightedSpannable(CharSequence text, int index, String highlightText) {
        SpannableString str1 = new SpannableString(text);
        str1.setSpan(new ForegroundColorSpan(mHighlightColor),
                index, index + highlightText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str1;
    }

    public boolean shouldHighlight() {
        return mHighlightText != null && mHighlightText.length() > 0;
    }

    String getHighlightText() {
        return mHighlightText;
    }

    public interface OnHighlightClickListener {
        public void onHighlightClicked(String highlightTerm);
    }
}
