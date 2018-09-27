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

package com.github.angads25.toggle.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;

import com.github.angads25.toggle.interfaces.OnStateChangedListener;
import com.github.angads25.toggle.interfaces.OnToggledListener;

/**
 * <p>
 * Created by Angad Singh on 24/2/18.
 * </p>
 */

public class ToggleableView extends View {
    protected int width;
    protected int height;

    /**
     * Field to determine whether switch is on/off.
     *
     * @see #isOn()
     * @see #setOn(boolean)
     */
    protected boolean isOn;

    /**
     * Field to determine whether switch is enabled/disabled.
     *
     * @see #isEnabled()
     * @see #setEnabled(boolean)
     */
    protected boolean enabled;

    /**
     * Listener used to dispatch switch events.
     *
     * @see #setOnToggledListener(OnToggledListener)
     */
    protected OnToggledListener onToggledListener;

    /**
     * Simple constructor to use when creating a switch from code.
     * @param context The Context the switch is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public ToggleableView(Context context) {
        super(context);
    }

    /**
     * Constructor that is called when inflating a switch from XML.
     *
     * @param context The Context the switch is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the switch.
     */
    public ToggleableView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    public ToggleableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * <p>Returns the boolean state of this Switch.</p>
     *
     * @return true if the switch is on, false if it is off.
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * <p>Changes the boolean state of this Switch.</p>
     *
     * @param on true to turn switch on, false to turn it off.
     */
    public void setOn(boolean on) {
        isOn = on;
    }

    /**
     * Returns the enabled status for this switch. The interpretation of the
     * enabled state varies by subclass.
     *
     * @return True if this switch is enabled, false otherwise.
     */
    @ViewDebug.ExportedProperty
    @Override public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set the enabled state of this switch. The interpretation of the enabled
     * state varies by subclass.
     *
     * @param enabled True if this view is enabled, false otherwise.
     */
    @Override public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Register a callback to be invoked when the boolean state of switch is changed. If this switch is not
     * enabled, there won't be any event.
     *
     * @param onToggledListener The callback that will run
     *
     * @see #setEnabled(boolean)
     */
    public void setOnToggledListener(OnToggledListener onToggledListener) {
        this.onToggledListener = onToggledListener;
    }
}
