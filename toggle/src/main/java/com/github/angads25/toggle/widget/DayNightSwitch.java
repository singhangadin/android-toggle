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
import android.graphics.Path;
import android.graphics.RectF;
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

public class DayNightSwitch extends ToggleableView {
    private int padding;

    private int centerX;
    private int centerY;

    private int cloudCenterX;
    private int cloudCenterY;

    private int outerRadii;
    private int thumbRadii;

    private int colorDisabled;

    private Paint paint;

    private long startTime;

    private RectF thumbBounds;

    private RectF leftBgArc;
    private RectF rightBgArc;

    private RectF leftFgArc;
    private RectF rightFgArc;

    private float thumbOnCenterX;
    private float thumbOffCenterX;

    private Path outerCloud;

    private int stars = Color.parseColor("#FCFCFC");
    private int sunInner = Color.parseColor("#F5EB42");
    private int sunOuter = Color.parseColor("#E4C74D");
    private int moonInner = Color.parseColor("#FFFDF2");
    private int moonOuter = Color.parseColor("#DEE1C5");
    private int cloudInner = Color.parseColor("#FFFFFF");
    private int cloudOuter = Color.parseColor("#D4D4D2");
    private int craterInner = Color.parseColor("#EFEEDA");
    private int parentOuter = Color.parseColor("#81C0D5");
    private int parentInner = Color.parseColor("#C0E6F6");
    private int parentDarkOuter = Color.parseColor("#202020");
    private int parentDarkInner = Color.parseColor("#484848");

    /**
     * Simple constructor to use when creating a switch from code.
     * @param context The Context the switch is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public DayNightSwitch(Context context) {
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
    public DayNightSwitch(Context context, AttributeSet attrs) {
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
    public DayNightSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initProperties(attrs);
    }

    private void initView() {
        this.isOn = false;
        this.enabled = true;

        paint = new Paint();
        paint.setAntiAlias(true);

        outerCloud = new Path();

        leftBgArc = new RectF();
        rightBgArc = new RectF();

        leftFgArc = new RectF();
        rightFgArc = new RectF();
        thumbBounds = new RectF();

        this.colorDisabled = Color.parseColor("#D3D3D3");
    }

    private void initProperties(AttributeSet attrs) {
        TypedArray tarr = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Toggle,0,0);
        final int N = tarr.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = tarr.getIndex(i);
            if (attr == R.styleable.Toggle_on) {
                isOn = tarr.getBoolean(R.styleable.Toggle_on, false);
            } else if(attr == R.styleable.Toggle_android_enabled) {
                enabled = tarr.getBoolean(R.styleable.Toggle_android_enabled, false);
            }
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        {
            if(isEnabled()) {
                int borderColor;
                int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
                alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
                borderColor = Color.argb(alpha, Color.red(parentOuter), Color.green(parentOuter), Color.blue(parentOuter));
                paint.setColor(borderColor);

                canvas.drawArc(leftBgArc, 90, 180, false, paint);
                canvas.drawArc(rightBgArc, 90, -180, false, paint);
                canvas.drawRect(outerRadii, 0, (width - outerRadii), height, paint);

                canvas.drawArc(leftFgArc, 90, 180, false, paint);
                canvas.drawArc(rightFgArc, 90, -180, false, paint);
                canvas.drawRect(outerRadii, padding >>> 2, (width - outerRadii), height - (padding >>> 2), paint);

                alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
                alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
                borderColor = Color.argb(alpha, Color.red(parentDarkOuter), Color.green(parentDarkOuter), Color.blue(parentDarkOuter));
                paint.setColor(borderColor);

                canvas.drawArc(leftBgArc, 90, 180, false, paint);
                canvas.drawArc(rightBgArc, 90, -180, false, paint);
                canvas.drawRect(outerRadii, 0, (width - outerRadii), height, paint);

                canvas.drawArc(leftFgArc, 90, 180, false, paint);
                canvas.drawArc(rightFgArc, 90, -180, false, paint);
                canvas.drawRect(outerRadii, padding >>> 2, (width - outerRadii), height - (padding >>> 2), paint);
            } else {
                paint.setColor(colorDisabled);
                canvas.drawArc(leftBgArc, 90, 180, false, paint);
                canvas.drawArc(rightBgArc, 90, -180, false, paint);
                canvas.drawRect(outerRadii, 0, (width - outerRadii), height, paint);
            }

            int onColor;
            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));

            if(isEnabled()) {
                onColor = Color.argb(alpha, Color.red(parentInner), Color.green(parentInner), Color.blue(parentInner));
            } else {
                onColor = Color.argb(alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(onColor);

            canvas.drawArc(leftFgArc, 90, 180, false, paint);
            canvas.drawArc(rightFgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, padding >>> 2, (width - outerRadii), height - (padding >>> 2), paint);

            alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));

            int offColor = Color.argb(alpha, Color.red(parentDarkInner), Color.green(parentDarkInner), Color.blue(parentDarkInner));
            paint.setColor(offColor);

            canvas.drawArc(leftFgArc, 90, 180, false, paint);
            canvas.drawArc(rightFgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, padding >>> 2, (width - outerRadii), height - (padding >>> 2), paint);
        }

        {
            int alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));

            int starColor = Color.argb(alpha, Color.red(stars), Color.green(stars), Color.blue(stars));
            paint.setColor(starColor);

            float nightAnimScale = alpha / 255f;
            canvas.drawCircle(
                    (centerX) + ((thumbRadii >>> 3) * nightAnimScale),
                    (centerY) + ((height >>> 2) + (thumbRadii >>> 3) * nightAnimScale),
                    (thumbRadii >>> 3) * nightAnimScale,
                    paint
            );
            canvas.drawCircle(
                    (centerX) + ((thumbRadii / 10) * nightAnimScale),
                    (((centerY)) >>> 1) - ((thumbRadii / 10) * nightAnimScale),
                    (thumbRadii / 10) * nightAnimScale,
                    paint
            );
            canvas.drawCircle(
                    (centerX) + ((centerX) - (thumbRadii / 1.5f) * nightAnimScale),
                    (centerY) - ((thumbRadii >>> 3) * nightAnimScale),
                    (thumbRadii >>> 3) * nightAnimScale,
                    paint
            );
            canvas.drawCircle(
                    (centerX) + ((centerX) - (thumbRadii) * nightAnimScale),
                    (centerY) - ((centerY) - (thumbRadii / 1.75f) * nightAnimScale),
                    (thumbRadii / 10) * nightAnimScale,
                    paint
            );
            canvas.drawCircle(
                    (centerX) + ((centerX) - (thumbRadii / 1.25f) * nightAnimScale),
                    (centerY) + ((centerY) - (thumbRadii / 1.25f) * nightAnimScale),
                    (thumbRadii / 10) * nightAnimScale,
                    paint
            );
            canvas.drawCircle(
                    (centerX) + ((thumbRadii / 1.5f) * nightAnimScale),
                    (centerY) - ((thumbRadii >>> 2) * nightAnimScale),
                    (thumbRadii >>> 4) * nightAnimScale,
                    paint
            );
            canvas.drawCircle(
                    (centerX) + ((thumbRadii) * nightAnimScale),
                    (centerY) + ((thumbRadii >>> 2) * nightAnimScale),
                    (thumbRadii >>> 4) * nightAnimScale,
                    paint
            );
        }

        {
            canvas.save();
            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));

            canvas.rotate((alpha / 255f) * 360, thumbBounds.centerX(), thumbBounds.centerY());

            int sunOuterColor = Color.argb(alpha, Color.red(sunOuter), Color.green(sunOuter), Color.blue(sunOuter));
            paint.setColor(sunOuterColor);
            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);

            int sunInnerColor = Color.argb(alpha, Color.red(sunInner), Color.green(sunInner), Color.blue(sunInner));
            paint.setColor(sunInnerColor);
            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii - (thumbRadii / 6), paint);

            alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));

            int moonOuterColor;
            if(isEnabled()) {
                moonOuterColor = Color.argb(alpha, Color.red(moonOuter), Color.green(moonOuter), Color.blue(moonOuter));
            } else {
                moonOuterColor = Color.argb(alpha, Color.red(colorDisabled), Color.green(colorDisabled), Color.blue(colorDisabled));
            }
            paint.setColor(moonOuterColor);
            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);

            int moonInnerColor = Color.argb(alpha, Color.red(moonInner), Color.green(moonInner), Color.blue(moonInner));
            paint.setColor(moonInnerColor);
            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii - (thumbRadii / 6), paint);

            int craterBorderColor = Color.argb(alpha, Color.red(moonOuter), Color.green(moonOuter), Color.blue(moonOuter));
            paint.setColor(craterBorderColor);
            canvas.drawCircle(thumbBounds.centerX() - (thumbRadii >>> 1), thumbBounds.centerY() - (thumbRadii >>> 1), thumbRadii >>> 2, paint);

            int craterInnerColor = Color.argb(alpha, Color.red(craterInner), Color.green(craterInner), Color.blue(craterInner));
            paint.setColor(craterInnerColor);
            canvas.drawCircle(thumbBounds.centerX() - (thumbRadii >>> 1), thumbBounds.centerY() - (thumbRadii >>> 1), thumbRadii >>> 4, paint);

            paint.setColor(craterBorderColor);
            canvas.drawCircle(thumbBounds.centerX() + (thumbRadii / 3), thumbBounds.centerY() + (thumbRadii >>> 1), thumbRadii >>> 2, paint);

            paint.setColor(craterInnerColor);
            canvas.drawCircle(thumbBounds.centerX() + (thumbRadii / 3), thumbBounds.centerY() + (thumbRadii >>> 1), thumbRadii >>> 4, paint);

            paint.setColor(craterBorderColor);
            canvas.drawCircle(thumbBounds.centerX() + (thumbRadii >>> 1), thumbBounds.centerY() - (thumbRadii / 2.75f), thumbRadii / 3, paint);

            paint.setColor(craterInnerColor);
            canvas.drawCircle(thumbBounds.centerX() + (thumbRadii >>> 1), thumbBounds.centerY() - (thumbRadii / 2.75f), thumbRadii / 6, paint);

            canvas.restore();
        }

        {
            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            alpha = (alpha < 0 ? 0 : (alpha > 255 ? 255 : alpha));
            float dayAnimScale = alpha / 255f;

            initOuterCloud(dayAnimScale);
            int cloudOuterColor = Color.argb(alpha, Color.red(cloudOuter), Color.green(cloudOuter), Color.blue(cloudOuter));
            paint.setColor(cloudOuterColor);
            canvas.drawPath(outerCloud, paint);

            int cloudInnerColor = Color.argb(alpha, Color.red(cloudInner), Color.green(cloudInner), Color.blue(cloudInner));
            paint.setColor(cloudInnerColor);

            canvas.save();
            canvas.scale(0.8f, 0.8f, cloudCenterX + ((1 - dayAnimScale) * (width >>> 2)), cloudCenterY);
            canvas.drawPath(outerCloud, paint);
            canvas.restore();
        }
    }

    private void initOuterCloud(float dayAnimScale) {
        float translationScale = 1 - dayAnimScale;
        outerCloud.reset();
        outerCloud.moveTo(
                (cloudCenterX) + (width >>> 3) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 3.5f) - (centerY >>> 2)
        );
        outerCloud.lineTo(
                (cloudCenterX) - (width >>> 3) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 3.5f) - (centerY >>> 2)
        );
        outerCloud.cubicTo(
                (cloudCenterX) - (width / 5) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 3.5f) - (centerY >>> 2),
                (cloudCenterX) - (width / 5) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 20) - (centerY >>> 2),
                (cloudCenterX) - (width >>> 3) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 20) - (centerY >>> 2)
        );
        outerCloud.cubicTo(
                (cloudCenterX) - (width >>> 3) + (translationScale * (width >>> 2)),
                (cloudCenterY) - (height >>> 3) - (centerY >>> 2),
                (cloudCenterX) + (translationScale * (width >>> 2)),
                (cloudCenterY) - (height >>> 3) - (centerY >>> 2),
                (cloudCenterX) + (translationScale * (width >>> 2)),
                (cloudCenterY) - (centerY >>> 2)
        );
        outerCloud.cubicTo(
                (cloudCenterX) + (translationScale * (width >>> 2)),
                (cloudCenterY) - (height / 12) - (centerY >>> 2),
                (cloudCenterX) + (width / 10) + (translationScale * (width >>> 2)),
                (cloudCenterY) - (height / 12) - (centerY >>> 2),
                (cloudCenterX) + (width / 10) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height >>> 4) - (centerY >>> 2)
        );
        outerCloud.cubicTo(
                (cloudCenterX) + (width / 6) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height >>> 4) - (centerY >>> 2),
                (cloudCenterX) + (width / 6) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 3.5f) - (centerY >>> 2),
                (cloudCenterX) + (width >>> 3) + (translationScale * (width >>> 2)),
                (cloudCenterY) + (height / 3.5f) - (centerY >>> 2)
        );
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getResources().getDimensionPixelSize(R.dimen.day_night_default_width);
        int desiredHeight = getResources().getDimensionPixelSize(R.dimen.day_night_default_height);

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

        centerX = width >>> 1;
        centerY = height >>> 1;

        outerRadii = Math.min(centerX, centerY);
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

        leftFgArc.set(padding >>> 2,padding >>> 2, (outerRadii << 1) - (padding >>> 2), height - (padding >>> 2));
        rightFgArc.set(width - (outerRadii << 1) + (padding >>> 2),padding >>> 2, width - (padding >>> 2), height - (padding >>> 2));

        cloudCenterX = centerX;
        cloudCenterY = centerY + (centerY >>> 2);
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
            switchColor.setDuration(150);
            switchColor.start();
        } else {
            ValueAnimator switchColor = ValueAnimator.ofFloat(padding, width - padding - thumbRadii);
            switchColor.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                invalidate();
            });
            switchColor.setInterpolator(new AccelerateDecelerateInterpolator());
            switchColor.setDuration(150);
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
                        if (x >= centerX) {
                            ValueAnimator thumbAnimator = ValueAnimator.ofFloat((x > (width - padding - thumbRadii) ? (width - padding - thumbRadii) : x), width - padding - thumbRadii);
                            thumbAnimator.addUpdateListener(animation -> {
                                float value = (float) animation.getAnimatedValue();
                                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                                invalidate();
                            });
                            thumbAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                            thumbAnimator.setDuration(150);
                            thumbAnimator.start();
                            isOn = true;
                        } else {
                            ValueAnimator thumbAnimator = ValueAnimator.ofFloat((x < padding ? padding : x - thumbRadii), padding);
                            thumbAnimator.addUpdateListener(animation -> {
                                float value = (float) animation.getAnimatedValue();
                                thumbBounds.set(value, thumbBounds.top, value + thumbRadii, thumbBounds.bottom);
                                invalidate();
                            });
                            thumbAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                            thumbAnimator.setDuration(150);
                            thumbAnimator.start();
                            isOn = false;
                        }
                        if(onToggledListener != null) {
                            onToggledListener.onSwitched(this, isOn);
                        }
                    }
                    invalidate();
                    return true;
                }

                default: return super.onTouchEvent(event);
            }
        } else {
            return false;
        }
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
}
