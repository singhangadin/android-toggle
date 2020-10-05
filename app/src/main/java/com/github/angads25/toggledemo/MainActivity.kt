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

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.switch_labeled).setOnClickListener(this)
        findViewById<View>(R.id.switch_day_night).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.switch_labeled -> startActivity(Intent(this@MainActivity, TestActivity::class.java))
            R.id.switch_day_night -> startActivity(Intent(this@MainActivity, DayNightActivity::class.java))
        }
    }
}
