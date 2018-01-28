package com.github.angads25.labeledswitch;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

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

    private int outerRadii;
    private int thumbRadii;

    private boolean isOn;

    private Paint paint;
    private Context context;

    private long startTime;

    private String labelOn;
    private String labelOff;

    private RectF thumbBounds;

    private Typeface typeface;

    private final String MAX_CHAR = "W";

    public LabeledSwitch(Context context) {
        super(context);
        initView(context);
    }

    public LabeledSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LabeledSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.isOn = false;
        this.context = context;
        this.colorOn = Color.parseColor("#05CAA5");
        this.colorOff = Color.parseColor("#FFFFFF");

        paint = new Paint();
        paint.setAntiAlias(true);

        thumbBounds = new RectF();

        int spSize = 12;
        float scaledSizeInPixels = spSize * getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledSizeInPixels);

        this.labelOn = "NEED";
        this.labelOff = "WANT";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isOn) {
            paint.setColor(colorOn);
        } else {
            paint.setColor(colorOff);
        }
        canvas.drawCircle(outerRadii, outerRadii, outerRadii, paint);
        canvas.drawCircle(width - outerRadii, outerRadii, outerRadii, paint);
        canvas.drawRect(outerRadii, 0, width - outerRadii, height, paint);
        float textCenter = paint.measureText(MAX_CHAR) / 16;
        if(isOn) {
            paint.setColor(colorOff);
            canvas.drawText(labelOn, padding, (padding / 2) + (height / 2) + textCenter, paint);
        } else {
            paint.setColor(colorOn);
            canvas.drawText(labelOff, padding + (2 * thumbRadii), (padding / 2) + (height / 2) + textCenter, paint);
        }
        canvas.drawCircle(thumbBounds.centerX(), thumbBounds.centerY(), thumbRadii, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        outerRadii = Math.min(width, height) / 2;
        thumbRadii = (int) (Math.min(width, height) / (2.88f));
        padding = (height - thumbRadii) / 2;

        if(isOn) {
            thumbBounds.set(width - padding - thumbRadii, padding, width - padding, height - padding);
        } else {
            thumbBounds.set(padding, padding, padding + thumbRadii, height - padding);
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startTime = System.currentTimeMillis();
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                // TODO: MOVE INNER CIRCLE HERE
                if(x - (thumbRadii / 2) > padding && x + (thumbRadii / 2) < width - padding) {
                    if(x >= width / 2) {
                        isOn = true;
                    } else {
                        isOn = false;
                    }
                    thumbBounds.set(x - (thumbRadii / 2), thumbBounds.top, x + (thumbRadii / 2), thumbBounds.bottom);
                    invalidate();
                }
                return true;
            }

            case MotionEvent.ACTION_UP: {
                long endTime = System.currentTimeMillis();
                long span = endTime - startTime;
                if(span < 300) {
                    // TODO: JUST TOGGLE THE CURRENT SELECTION
                    if(isOn) {
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
                    if(x >= width / 2) {
                        //TODO: LOOP TO RIGHT
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
                        //TODO: LOOP TO LEFT
                        ValueAnimator switchColor = ValueAnimator.ofFloat((x < padding? padding : x), padding);
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
                return true;
            }

            default: {
                return super.onTouchEvent(event);
            }
        }
    }

    public int getColorOn() {
        return colorOn;
    }

    public void setColorOn(int colorOn) {
        this.colorOn = colorOn;
    }

    public int getColorOff() {
        return colorOff;
    }

    public void setColorOff(int colorOff) {
        this.colorOff = colorOff;
    }

    public String getLabelOn() {
        return labelOn;
    }

    public void setLabelOn(String labelOn) {
        this.labelOn = labelOn;
    }

    public String getLabelOff() {
        return labelOff;
    }

    public void setLabelOff(String labelOff) {
        this.labelOff = labelOff;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
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
    }
}
