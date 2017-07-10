package com.multimarca.tae.voceadorestae.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by erick on 12/22/15. Multimarca
 */
public class TwoThreeImageView extends ImageView {
    public TwoThreeImageView(Context context) {
        super(context);
    }

    public TwoThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoThreeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ThreeTwoHeight = MeasureSpec.getSize(heightMeasureSpec) * 2 / 3;
        int ThreeTwoHeightSpec =
                MeasureSpec.makeMeasureSpec(ThreeTwoHeight, MeasureSpec.EXACTLY);
        super.onMeasure(ThreeTwoHeightSpec, heightMeasureSpec);
    }
}
