package com.github.angads25.labeledswitch;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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

import com.github.angads25.labeledswitch.interfaces.OnToggledListener;

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
    private int borderColor;

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

    private final String MAX_CHAR = "N";

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

        this.textSize = 12;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            borderColor = colorOn = getResources().getColor(R.color.colorAccent, getContext().getTheme());
        } else {
            borderColor = colorOn = getResources().getColor(R.color.colorAccent);
        }
        this.colorOff = Color.parseColor("#FFFFFF");

        paint = new Paint();
        paint.setAntiAlias(true);

        leftBgArc = new RectF();
        rightBgArc = new RectF();

        leftFgArc = new RectF();
        rightFgArc = new RectF();
        thumbBounds = new RectF();
    }

    private void initProperties(AttributeSet attrs) {
        TypedArray tarr = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.LabeledSwitch,0,0);
        final int N = tarr.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = tarr.getIndex(i);
            if (attr == R.styleable.LabeledSwitch_on) {
                isOn = tarr.getBoolean(R.styleable.LabeledSwitch_on, false);
            } else if (attr == R.styleable.LabeledSwitch_offColor) {
                colorOff = tarr.getColor(R.styleable.LabeledSwitch_offColor, Color.parseColor("#FFFFFF"));
            } else if (attr == R.styleable.LabeledSwitch_borderColor) {
                int accentColor;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    accentColor = getResources().getColor(R.color.colorAccent, getContext().getTheme());
                } else {
                    accentColor = getResources().getColor(R.color.colorAccent);
                }
                borderColor = tarr.getColor(R.styleable.LabeledSwitch_borderColor, accentColor);
            } else if (attr == R.styleable.LabeledSwitch_onColor) {
                int accentColor;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    accentColor = getResources().getColor(R.color.colorAccent, getContext().getTheme());
                } else {
                    accentColor = getResources().getColor(R.color.colorAccent);
                }
                colorOn = tarr.getColor(R.styleable.LabeledSwitch_onColor, accentColor);
            } else if (attr == R.styleable.LabeledSwitch_offText) {
                labelOff = tarr.getString(R.styleable.LabeledSwitch_offText);
            } else if (attr == R.styleable.LabeledSwitch_onText) {
                labelOn = tarr.getString(R.styleable.LabeledSwitch_onText);
            } else if (attr == R.styleable.LabeledSwitch_textSize) {
                textSize = tarr.getInteger(R.styleable.LabeledSwitch_textSize, 12);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scaledSizeInPixels = textSize * getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledSizeInPixels);

//      Drawing Switch background here
        {
            paint.setColor(borderColor);
            canvas.drawArc(leftBgArc, 90, 180, false, paint);
            canvas.drawArc(rightBgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, 0, (width - outerRadii), height, paint);

            paint.setColor(colorOff);

            canvas.drawArc(leftFgArc, 90, 180, false, paint);
            canvas.drawArc(rightFgArc, 90, -180, false, paint);
            canvas.drawRect(outerRadii, padding / 10, (width - outerRadii), height - (padding / 10), paint);

            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            int onColor = Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
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
        float textCenter = paint.measureText(MAX_CHAR) / 2;
        if(isOn) {
            int alpha = (int)(((thumbBounds.centerX() - (width / 2)) / (thumbOnCenterX - (width / 2))) * 255);
            int offColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            int maxSize = width - (3 * padding) - (2 * thumbRadii);
            float extraSpace = (maxSize - paint.measureText(labelOn)) / 2;

            canvas.drawText(labelOn, padding + (extraSpace > 0 ? extraSpace : 0), (height / 2) + textCenter, paint);
            paint.setColor(colorOff);
        } else {
            int alpha = (int)((((width / 2) - thumbBounds.centerX()) / ((width / 2) - thumbOffCenterX)) * 255);
            int onColor = Color.argb(alpha < 0 ? 0 : alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            paint.setColor(onColor);

            int maxSize = width - (3 * padding) - (2 * thumbRadii);
            float extraSpace = (maxSize - paint.measureText(labelOff)) / 2;

            canvas.drawText(labelOff, padding + (2 * thumbRadii) + (extraSpace > 0 ? extraSpace : 0), (height / 2) + textCenter, paint);
            paint.setColor(colorOn);
        }

//      Drawing Switch Thumb here
        {
            int alpha = (int) (((thumbBounds.centerX() - thumbOffCenterX) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            int offColor = Color.argb(alpha, Color.red(colorOff), Color.green(colorOff), Color.blue(colorOff));
            paint.setColor(offColor);

            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);

            alpha = (int) (((thumbOnCenterX - thumbBounds.centerX()) / (thumbOnCenterX - thumbOffCenterX)) * 255);
            int onColor = Color.argb(alpha, Color.red(colorOn), Color.green(colorOn), Color.blue(colorOn));
            paint.setColor(onColor);

            canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
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
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if(isEnabled()) {
            float x = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startTime = System.currentTimeMillis();
                    return true;
                }

                case MotionEvent.ACTION_MOVE: {
                    // TODO: MOVE THUMB TO THIS POSITION
                    if (x - (thumbRadii / 2) > padding && x + (thumbRadii / 2) < width - padding) {
                        if (x >= width / 2) {
                            isOn = true;
                        } else {
                            isOn = false;
                        }
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
                        // TODO: JUST TOGGLE THE CURRENT SELECTION
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
                    } else {
                        if (x >= width / 2) {
                            //TODO: MOVE SWITCH TO RIGHT
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
                            //TODO: MOVE SWITCH TO LEFT
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
}
