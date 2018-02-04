package com.github.angads25.toggle.interfaces;

import com.github.angads25.toggle.LabeledSwitch;

/**
 * <p>
 * Created by Angad Singh on 28/1/18.
 * </p>
 */

public interface OnToggledListener {

    void onSwitched(LabeledSwitch labeledSwitch, boolean isOn);
}
