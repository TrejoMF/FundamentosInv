package com.multimarca.tae.voceadorestae.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by erick on 12/22/15. Multimarca
 */
public class ThreeTwoImageView extends ImageView {
    public ThreeTwoImageView(Context context) {
        super(context);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec) * 2 / 3;
        int threTwoHeightSpec =
                MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, threTwoHeightSpec);
    }
}
