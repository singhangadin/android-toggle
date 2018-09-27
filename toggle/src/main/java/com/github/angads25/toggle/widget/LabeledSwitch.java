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

package com.github.angads25.toggle.widget;

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
import android.view.animation.AccelerateDecelerateInterpolator;

import com.github.angads25.toggle.R;
import com.github.angads25.toggle.model.ToggleableView;

/**
 * <p>
 * Created by Angad Singh on 27/1/18.
 * </p>
 */

public class LabeledSwitch extends ToggleableView {
    private int padding;

    private int colorOn;
    private int colorOff;
    private int colorBorder;
    private int colorDisabled;

    private int textSize;

    private int outerRadii;
    private int thumbRadii;

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

    /**
     * Simple constructor to use when creating a switch from code.
     * @param context The Context the switch is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public LabeledSwitch(Context context) {
        super(context);
        initView();
    }

    /**
     * Constructor that is called when inflating a switch from XML.
     *
     * @param context The Context the switch is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the switch.
     */
    public LabeledSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initProperties(attrs);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute.
     *
     * @param context The Context the switch is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the switch.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the switch. Can be 0 to not look for defaults.
     */
    public LabeledSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initProperties(attrs);
    }

    private void initView() {
        this.isOn = false;
        this.labelOn = "ON";
        this.labelOff = "OFF";

        this.enabled = true;
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
            } else if(attr == R.styleable.Toggle_android_enabled) {
                enabled = tarr.getBoolean(R.styleable.Toggle_android_enabled, false);
            }
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
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
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
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
            int alpha = (int)((((width >>> 1) - thumbBounds.centerX()) / ((width >>> 1) - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            int onColor = Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            paint.setColor(onColor);

            float centerX = (width - padding - ((padding + (padding >>> 1)) + (thumbRadii << 1))) >>> 1;
            canvas.drawText(labelOff, (padding + (padding >>> 1)) + (thumbRadii << 1) + centerX - (paint.measureText(labelOff) / 2), (height >>> 1) + textCenter, paint);

            alpha = (int)(((thumbBounds.centerX() - (width >>> 1)) / (thumbOnCenterX - (width >>> 1))) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            int offColor = Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            int maxSize = width - (padding << 1) - (thumbRadii << 1);

            centerX = (((padding >>> 1) + maxSize) - padding) >>> 1;
            canvas.drawText(labelOn, padding + centerX - (paint.measureText(labelOn) / 2), (height >>> 1) + textCenter, paint);
        } else {
            int alpha = (int)(((thumbBounds.centerX() - (width >>> 1)) / (thumbOnCenterX - (width >>> 1))) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            int offColor = Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            int maxSize = width - (padding << 1) - (thumbRadii << 1);
            float centerX = (((padding >>> 1) + maxSize) - padding) >>> 1;
            canvas.drawText(labelOn, padding + centerX - (paint.measureText(labelOn) / 2), (height >>> 1) + textCenter, paint);

            alpha = (int)((((width >>> 1) - thumbBounds.centerX()) / ((width >>> 1) - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            int onColor;
            if(isEnabled()) {
                onColor = Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            } else {
                onColor = Color.argb(alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(onColor);

            centerX = (width - padding - ((padding + (padding >>> 1)) + (thumbRadii << 1))) >>> 1;
            canvas.drawText(labelOff, (padding + (padding >>> 1)) + (thumbRadii << 1) + centerX - (paint.measureText(labelOff) / 2), (height >>> 1) + textCenter, paint);
        }

//      Drawing Switch Thumb here
        {
            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            int offColor = Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);
            alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            int onColor;
            if(isEnabled()) {
                onColor = Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            } else {
                onColor = Color.argb(alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(onColor);
            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);
        }
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

        outerRadii = Math.min(width, height) >>> 1;
        thumbRadii = (int) (Math.min(width, height) / (2.88f));
        padding = (height - thumbRadii) >>> 1;

        thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        thumbOnCenterX = thumbBounds.centerX();

        thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        thumbOffCenterX = thumbBounds.centerX();

        if(isOn) {
            thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        } else {
            thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        }

        leftBgArc.set(0,0, outerRadii << 1, height);
        rightBgArc.set(width - (outerRadii << 1),0, width, height);

        leftFgArc.set(padding / 10,padding / 10, (outerRadii << 1)- (padding / 10), height - (padding / 10));
        rightFgArc.set(width - (outerRadii << 1) + (padding / 10),padding / 10, width - (padding / 10), height - (padding / 10));
    }

    /**
     * Call this view's OnClickListener, if it is defined.  Performs all normal
     * actions associated with clicking: reporting accessibility event, playing
     * a sound, etc.
     *
     * @return True there was an assigned OnClickListener that was called, false
     *         otherwise is returned.
     */
    @Override public final boolean performClick() {
        super.performClick();
        if (isOn) {
            ValueAnimator switchColor = ValueAnimator.ofFloat(width - padding - thumbRadii, padding);
            switchColor.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                invalidate();
            });
            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
            switchColor.setDuration(250);
            switchColor.start();
        } else {
            ValueAnimator switchColor = ValueAnimator.ofFloat(padding, width - padding - thumbRadii);
            switchColor.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                invalidate();
            });
            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
            switchColor.setDuration(250);
            switchColor.start();
        }
        isOn =! isOn;
        if(onToggledListener != null) {
            onToggledListener.onSwitched(this, isOn);
        }
        return true;
    }

    /**
     * Method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override public final boolean onTouchEvent(MotionEvent event) {
        if(isEnabled()) {
            float x = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startTime = System.currentTimeMillis();
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {
                    if (x - (thumbRadii >>> 1) > padding && x + (thumbRadii >>> 1) < width - padding) {
                        thumbBounds.set(x - (thumbRadii >>> 1), thumbBounds.top, x + (thumbRadii >>> 1), thumbBounds.bottom);
                        invalidate();
                    }
                    return true;
                }

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    long endTime = System.currentTimeMillis();
                    long span = endTime - startTime;
                    if (span < 200) {
                        performClick();
                    } else {
                        if (x >= width >>> 1) {
                            ValueAnimator switchColor = ValueAnimator.ofFloat((x > (width - padding - thumbRadii) ? (width - padding - thumbRadii) : x), width - padding - thumbRadii);
                            switchColor.addUpdateListener(animation -> {
                                float value = (float) animation.getAnimatedValue();
                                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                                invalidate();
                            });
                            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
                            switchColor.setDuration(250);
                            switchColor.start();
                            isOn = true;
                        } else {
                            ValueAnimator switchColor = ValueAnimator.ofFloat((x < padding ? padding : x), padding);
                            switchColor.addUpdateListener(animation -> {
                                float value = (float) animation.getAnimatedValue();
                                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                                invalidate();
                            });
                            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
                            switchColor.setDuration(250);
                            switchColor.start();
                            isOn = false;
                        }
                        if(onToggledListener != null) {
                            onToggledListener.onSwitched(this, isOn);
                        }
                    }
                    invalidate();
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

    /**
     * <p>Returns the color value for colorOn.</p>
     *
     * @return color value for label and thumb in off state and background in on state.
     */
    public int getColorOn() {
        return colorOn;
    }

    /**
     * <p>Changes the on color value of this Switch.</p>
     *
     * @param colorOn color value for label and thumb in off state and background in on state.
     */
    public void setColorOn(int colorOn) {
        this.colorOn = colorOn;
        invalidate();
    }

    /**
     * <p>Returns the color value for colorOff.</p>
     *
     * @return color value for label and thumb in on state and background in off state.
     */
    public int getColorOff() {
        return colorOff;
    }

    /**
     * <p>Changes the off color value of this Switch.</p>
     *
     * @param colorOff color value for label and thumb in on state and background in off state.
     */
    public void setColorOff(int colorOff) {
        this.colorOff = colorOff;
        invalidate();
    }

    /**
     * <p>Returns text label when switch is in on state.</p>
     *
     * @return text label when switch is in on state.
     */
    public String getLabelOn() {
        return labelOn;
    }

    /**
     * <p>Changes text label when switch is in on state.</p>
     *
     * @param labelOn text label when switch is in on state.
     */
    public void setLabelOn(String labelOn) {
        this.labelOn = labelOn;
        invalidate();
    }

    /**
     * <p>Returns text label when switch is in off state.</p>
     *
     * @return text label when switch is in off state.
     */
    public String getLabelOff() {
        return labelOff;
    }

    /**
     * <p>Changes text label when switch is in off state.</p>
     *
     * @param labelOff text label when switch is in off state.
     */
    public void setLabelOff(String labelOff) {
        this.labelOff = labelOff;
        invalidate();
    }

    /**
     * <p>Returns the typeface for Switch on/off labels.</p>
     *
     * @return the typeface for Switch on/off labels..
     */
    public Typeface getTypeface() {
        return typeface;
    }

    /**
     * <p>Changes the typeface for Switch on/off labels.</p>
     *
     * @param typeface the typeface for Switch on/off labels.
     */
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        paint.setTypeface(typeface);
        invalidate();
    }

    /**
     * <p>Changes the boolean state of this Switch.</p>
     *
     * @param on true to turn switch on, false to turn it off.
     */
    @Override public void setOn(boolean on) {
        super.setOn(on);
        if(isOn) {
            thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        } else {
            thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        }
        invalidate();
    }

    /**
     * <p>Returns the color value for Switch disabled state.</p>
     *
     * @return color value used by background, border and thumb when switch is disabled.
     */
    public int getColorDisabled() {
        return colorDisabled;
    }

    /**
     * <p>Changes the color value for Switch disabled state.</p>
     *
     * @param colorDisabled color value used by background, border and thumb when switch is disabled.
     */
    public void setColorDisabled(int colorDisabled) {
        this.colorDisabled = colorDisabled;
        invalidate();
    }

    /**
     * <p>Returns the color value for Switch border.</p>
     *
     * @return color value used by Switch border.
     */
    public int getColorBorder() {
        return colorBorder;
    }

    /**
     * <p>Changes the color value for Switch disabled state.</p>
     *
     * @param colorBorder color value used by Switch border.
     */
    public void setColorBorder(int colorBorder) {
        this.colorBorder = colorBorder;
        invalidate();
    }

    /**
     * <p>Returns the text size for Switch on/off label.</p>
     *
     * @return text size for Switch on/off label.
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * <p>Changes the text size for Switch on/off label.</p>
     *
     * @param textSize text size for Switch on/off label.
     */
    public void setTextSize(int textSize) {
        this.textSize = (int)(textSize * getResources().getDisplayMetrics().scaledDensity);
        invalidate();
    }
}
