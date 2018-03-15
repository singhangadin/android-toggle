package com.github.angads25.toggle.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.github.angads25.toggle.R;
import com.github.angads25.toggle.model.StateView;

/**
 * <p>
 * Created by Angad Singh on 25/2/18.
 * </p>
 */

public class NStateSwitch extends StateView {
    private int padding;

    private Paint paint;

    private int thumbRadii;

    public NStateSwitch(Context context) {
        super(context);
        initView();
    }

    public NStateSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NStateSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getResources().getDimensionPixelSize(R.dimen.default_width);
        int desiredHeight = getResources().getDimensionPixelSize(R.dimen.default_height);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);

        thumbRadii = (int) (Math.min(width, height) / (2.88f));
        padding = (height - thumbRadii) >>> 1;

        int sectionWidth = (width - (2 * (padding + thumbRadii)))/ states.length;

    }
}
