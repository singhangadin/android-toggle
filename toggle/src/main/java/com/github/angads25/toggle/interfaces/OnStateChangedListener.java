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

package com.github.angads25.toggle.interfaces;

import android.view.View;

/**
 * <p>
 * Created by Angad Singh on 25/2/18.
 * </p>
 *
 * Interface definition for a callback to be invoked when state of switch is changed.
 *
 * <p>This is a <a href="package-summary.html">event listener</a>
 * whose event method is {@link #onStateChanged(View, int)}.
 *
 * @since 1.1.0
 */
public interface OnStateChangedListener {

    /**
     * Called when a view changes it's state.
     *
     * @param view The view whose state was changed.
     * @param state The state of the view.
     */
    void onStateChanged(View view, int state);
}
