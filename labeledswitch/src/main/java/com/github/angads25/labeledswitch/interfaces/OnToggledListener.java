package com.github.angads25.labeledswitch.interfaces;

import com.github.angads25.labeledswitch.LabeledSwitch;

/**
 * <p>
 * Created by Angad Singh on 28/1/18.
 * </p>
 */

public interface OnToggledListener {

    void onSwitched(LabeledSwitch labeledSwitch, boolean isOn);
}
