/*
 * Copyright (C) 2018 Angad Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

class NStateSwitch extends StateView {
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

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getResources().getDimensionPixelSize(R.dimen.labeled_default_width);
        int desiredHeight = getResources().getDimensionPixelSize(R.dimen.labeled_default_height);

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

        int sectionWidth = (width - (2 * (padding + thumbRadii))) / states.length;

    }
}
