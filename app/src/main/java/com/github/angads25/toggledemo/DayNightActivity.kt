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

package com.github.angads25.toggledemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView
import com.github.angads25.toggle.widget.DayNightSwitch

/**
 * Created by Angad Singh on 14/07/18.
 */

class DayNightActivity : AppCompatActivity(), OnToggledListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_night)

        val dayNightSwitch = findViewById<DayNightSwitch>(R.id.switch1)
        dayNightSwitch.setOnToggledListener(this)

    }

    override fun onSwitched(toggleableView: ToggleableView, isOn: Boolean) {}
}
