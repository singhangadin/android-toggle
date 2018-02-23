package com.github.angads25.toggle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.github.angads25.toggle.interfaces.OnToggledListener;

/**
 * <p>
 * Created by Angad Singh on 24/2/18.
 * </p>
 */

public class ToggleableView extends View {
    protected int width;
    protected int height;

    protected boolean isOn;
    protected boolean enabled;

    protected OnToggledListener onToggledListener;

    public ToggleableView(Context context) {
        super(context);
    }

    public ToggleableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setOnToggledListener(OnToggledListener onToggledListener) {
        this.onToggledListener = onToggledListener;
    }
}
