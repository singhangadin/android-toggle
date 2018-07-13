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

import com.github.angads25.toggle.interfaces.OnStateChangedListener;

/**
 * <p>
 * Created by Angad Singh on 25/2/18.
 * </p>
 */

public class StateView extends View {
    protected int width;
    protected int height;

    protected int state;

    protected int[] states;

    protected boolean enabled;

    protected OnStateChangedListener onStateChangedListener;

    public StateView(Context context) {
        super(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int[] getStates() {
        return states;
    }

    public void setStates(int[] states) {
        this.states = states;
    }

    @Override public boolean isEnabled() {
        return enabled;
    }

    @Override public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }
}
