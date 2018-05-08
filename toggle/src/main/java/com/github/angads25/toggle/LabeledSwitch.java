/*
 * Copyright (C) 2018 Angad Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.angads25.toggle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.angads25.toggle.interfaces.OnToggledListener;

/**
 * <p>
 * Created by Angad Singh on 27/1/18.
 * </p>
 */

public class LabeledSwitch extends View {
    private int width;
    private int height;
    private int padding;

    private int colorOn;
    private int colorOff;
    private int colorBorder;
    private int colorDisabled;

    private int textSize;

    private int outerRadii;
    private int thumbRadii;

    private boolean isOn;

    private Paint paint;

    private long startTime;

    private String labelOn;
    private String labelOff;

    private RectF thumbBounds;

    private RectF leftBgArc;
    private RectF rightBgArc;

    private RectF leftFgArc;
    private RectF rightFgArc;

    private Typeface typeface;

    private float thumbOnCenterX;
    private float thumbOffCenterX;

    private OnToggledListener onToggledListener;

    public LabeledSwitch(Context context) {
        super(context);
        initView();
    }

    public LabeledSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initProperties(attrs);
    }

    public LabeledSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initProperties(attrs);
    }

    private void initView() {
        this.isOn = false;
        this.labelOn = "ON";
        this.labelOff = "OFF";

        this.textSize = (int)(12f * getResources().getDisplayMetrics().scaledDensity);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorBorder = colorOn = getResources().getColor(R.color.colorAccent, getContext().getTheme());
        } else {
            colorBorder = colorOn = getResources().getColor(R.color.colorAccent);
        }

        paint = new Paint();
        paint.setAntiAlias(true);

        leftBgArc = new RectF();
        rightBgArc = new RectF();

        leftFgArc = new RectF();
        rightFgArc = new RectF();
        thumbBounds = new RectF();

        this.colorOff = Color.parseColor("#FFFFFF");
        this.colorDisabled = Color.parseColor("#D3D3D3");
    }

    private void initProperties(AttributeSet attrs) {
        TypedArray tarr = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Toggle,0,0);
        final int N = tarr.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = tarr.getIndex(i);
            if (attr == R.styleable.Toggle_on) {
                isOn = tarr.getBoolean(R.styleable.Toggle_on, false);
            } else if (attr == R.styleable.Toggle_colorOff) {
                colorOff = tarr.getColor(R.styleable.Toggle_colorOff, Color.parseColor("#FFFFFF"));
            } else if (attr == R.styleable.Toggle_colorBorder) {
                int accentColor;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    accentColor = getResources().getColor(R.color.colorAccent, getContext().getTheme());
                } else {
                    accentColor = getResources().getColor(R.color.colorAccent);
                }
                colorBorder = tarr.getColor(R.styleable.Toggle_colorBorder, accentColor);
            } else if (attr == R.styleable.Toggle_colorOn) {
                int accentColor;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    accentColor = getResources().getColor(R.color.colorAccent, getContext().getTheme());
                } else {
                    accentColor = getResources().getColor(R.color.colorAccent);
                }
                colorOn = tarr.getColor(R.styleable.Toggle_colorOn, accentColor);
            } else if (attr == R.styleable.Toggle_colorDisabled) {
                colorDisabled = tarr.getColor(R.styleable.Toggle_colorOff, Color.parseColor("#D3D3D3"));
            } else if (attr == R.styleable.Toggle_textOff) {
                labelOff = tarr.getString(R.styleable.Toggle_textOff);
            } else if (attr == R.styleable.Toggle_textOn) {
                labelOn = tarr.getString(R.styleable.Toggle_textOn);
            } else if (attr == R.styleable.Toggle_android_textSize) {
                int defaultTextSize = (int)(12f * getResources().getDisplayMetrics().scaledDensity);
                textSize = tarr.getDimensionPixelSize(R.styleable.Toggle_android_textSize, defaultTextSize);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        float scaledSizeInPixels = textSize * getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(textSize);

//      Drawing Switch background here
        {
            if(isEnabled()) {
                paint.setColor(colorBorder);
            } else {
                paint.setColor(colorDisabled);
            }
            canvas.drawArc(leftBgArc, 90, 180, false, paint);
            canvas.drawArc(rightBgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, 0, (width - outerRadii), height, paint);

            paint.setColor(colorOff);

            canvas.drawArc(leftFgArc, 90, 180, false, paint);
            canvas.drawArc(rightFgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, padding / 10, (width - outerRadii), height - (padding / 10), paint);

            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            int onColor;
            if(isEnabled()) {
                onColor = Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            } else {
                onColor = Color.argb(alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(onColor);

            canvas.drawArc(leftBgArc, 90, 180, false, paint);
            canvas.drawArc(rightBgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, 0, (width - outerRadii), height, paint);

            alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            int offColor = Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            canvas.drawArc(leftFgArc, 90, 180, false, paint);
            canvas.drawArc(rightFgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, padding / 10, (width - outerRadii), height - (padding / 10), paint);
        }

//      Drawing Switch Labels here
        String MAX_CHAR = "N";
        float textCenter = paint.measureText(MAX_CHAR) / 2;
        if(isOn) {
            int alpha = (int)(((thumbBounds.centerX() - (width / 2)) / (thumbOnCenterX - (width / 2))) * 255);
            int offColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            int maxSize = width - (2 * padding) - (2 * thumbRadii);

//            float extraSpace = (maxSize - paint.measureText(labelOn)) / 2;

//            Effective label text area
//            canvas.drawRect(padding, padding, (padding / 2) + maxSize, height - padding, paint);
//            int onColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
//            paint.setColor(onColor);

            float centerX = (((padding / 2) + maxSize) - padding) / 2;
            canvas.drawText(labelOn, padding + centerX - (paint.measureText(labelOn) / 2), (height / 2) + textCenter, paint);
        } else {
            int alpha = (int)((((width / 2) - thumbBounds.centerX()) / ((width / 2) - thumbOffCenterX)) * 255);
            int onColor;
            if(isEnabled()) {
                onColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            } else {
                onColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(onColor);

//            int maxSize = width - (2 * thumbRadii);
//            float extraSpace = (maxSize - paint.measureText(labelOff)) / 2;

//            Effective label text area
//            canvas.drawRect((padding + (padding / 2)) + (2 * thumbRadii), padding, width - padding, height - padding, paint);
//            int offColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
//            paint.setColor(offColor);

            float centerX = (width - padding - ((padding + (padding / 2)) + (2 * thumbRadii))) / 2;
            canvas.drawText(labelOff, (padding + (padding / 2)) + (2 * thumbRadii) + centerX - (paint.measureText(labelOff) / 2), (height / 2) + textCenter, paint);
        }

//      Drawing Switch Thumb here
        {
            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            int offColor = Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);
            alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);

            int onColor;
            if(isEnabled()) {
                onColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            } else {
                onColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(onColor);
            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);
        }
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

        outerRadii = Math.min(width, height) / 2;
        thumbRadii = (int) (Math.min(width, height) / (2.88f));
        padding = (height - thumbRadii) / 2;

        thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        thumbOnCenterX = thumbBounds.centerX();

        thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        thumbOffCenterX = thumbBounds.centerX();

        if(isOn) {
            thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        } else {
            thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        }

        leftBgArc.set(0,0, 2 * outerRadii, height);
        rightBgArc.set(width - (2 * outerRadii),0, width, height);

        leftFgArc.set(padding / 10,padding / 10, (2 * outerRadii)- (padding / 10), height - (padding / 10));
        rightFgArc.set(width - (2 * outerRadii) + (padding / 10),padding / 10, width - (padding / 10), height - (padding / 10));
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if (isOn) {
            ValueAnimator switchColor = ValueAnimator.ofFloat(width - padding - thumbRadii, padding);
            switchColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                    invalidate();
                }
            });
            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
            switchColor.setDuration(250);
            switchColor.start();
        } else {
            ValueAnimator switchColor = ValueAnimator.ofFloat(padding, width - padding - thumbRadii);
            switchColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                    invalidate();
                }
            });
            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
            switchColor.setDuration(250);
            switchColor.start();
        }
        isOn =! isOn;
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isEnabled()) {
            float x = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startTime = System.currentTimeMillis();
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {
                    // MOVE THUMB TO THIS POSITION
                    if (x - (thumbRadii / 2) > padding && x + (thumbRadii / 2) < width - padding) {
//                         Uncommenting this toggle switch back to last state on quick swipe actions.
//                        if (x >= width / 2) {
//                            isOn = true;
//                        } else {
//                            isOn = false;
//                        }
                        thumbBounds.set(x - (thumbRadii / 2), thumbBounds.top, x + (thumbRadii / 2), thumbBounds.bottom);
                        invalidate();
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    long endTime = System.currentTimeMillis();
                    long span = endTime - startTime;
                    if (span < 200) {
                        // JUST TOGGLE THE CURRENT SELECTION
                        // USING PERFORM CLICK FOR ACCESSIBILITY SUPPORT
                        performClick();
                    } else {
                        if (x >= width / 2) {
                            // MOVE SWITCH TO RIGHT
                            ValueAnimator switchColor = ValueAnimator.ofFloat((x > (width - padding - thumbRadii) ? (width - padding - thumbRadii) : x), width - padding - thumbRadii);
                            switchColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                                    invalidate();
                                }
                            });
                            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
                            switchColor.setDuration(250);
                            switchColor.start();
                            isOn = true;
                        } else {
                            // MOVE SWITCH TO LEFT
                            ValueAnimator switchColor = ValueAnimator.ofFloat((x < padding ? padding : x), padding);
                            switchColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                                    invalidate();
                                }
                            });
                            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
                            switchColor.setDuration(250);
                            switchColor.start();
                            isOn = false;
                        }
                    }
                    invalidate();
                    if(onToggledListener != null) {
                        onToggledListener.onSwitched(this, isOn);
                    }
                    return true;
                }

                default: {
                    return super.onTouchEvent(event);
                }
            }
        } else {
            return false;
        }
    }

    public void setOnToggledListener(OnToggledListener onToggledListener) {
        this.onToggledListener = onToggledListener;
    }

    public int getColorOn() {
        return colorOn;
    }

    public void setColorOn(int colorOn) {
        this.colorOn = colorOn;
        invalidate();
    }

    public int getColorOff() {
        return colorOff;
    }

    public void setColorOff(int colorOff) {
        this.colorOff = colorOff;
        invalidate();
    }

    public String getLabelOn() {
        return labelOn;
    }

    public void setLabelOn(String labelOn) {
        this.labelOn = labelOn;
        invalidate();
    }

    public String getLabelOff() {
        return labelOff;
    }

    public void setLabelOff(String labelOff) {
        this.labelOff = labelOff;
        invalidate();
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        paint.setTypeface(typeface);
        invalidate();
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
        if(isOn) {
            thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        } else {
            thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        }
        invalidate();
    }

    public int getColorDisabled() {
        return colorDisabled;
    }

    public void setColorDisabled(int colorDisabled) {
        this.colorDisabled = colorDisabled;
        invalidate();
    }
}
