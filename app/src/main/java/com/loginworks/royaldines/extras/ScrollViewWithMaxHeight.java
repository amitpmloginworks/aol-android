package com.loginworks.royaldines.extras;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.loginworks.royaldines.R;

/**
 * Created by ujjwal on 5/15/2017.
 */

public class ScrollViewWithMaxHeight extends ScrollView {

    private int maxHeight;
    private final int defaultHeight = 200;

    public ScrollViewWithMaxHeight(Context context) {
        super(context);
    }

    public ScrollViewWithMaxHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public ScrollViewWithMaxHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollViewWithMaxHeight(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
            //200 is a defualt value
            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, defaultHeight);

            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
