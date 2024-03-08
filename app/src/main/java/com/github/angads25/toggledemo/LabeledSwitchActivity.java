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

package com.github.angads25.toggledemo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.BounceInterpolator;

import com.github.angads25.toggle.interfaces.OnAnimateListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LabeledSwitchActivity extends AppCompatActivity {
    private Timer timers[];
    private volatile boolean stopped = false;

    private int[] switches = {
        R.id.switch1, R.id.switch2,
        R.id.switch4, R.id.switch5,
        R.id.switch7, R.id.switch8,
            R.id.switch9, R.id.switch10, R.id.switch12,
            R.id.switch12, R.id.switch13
    };

    private LabeledSwitch[] labeledSwitches;

    private TimerTask[] timerTasks;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labeled_switch);

        Typeface openSansBold = ResourcesCompat.getFont(LabeledSwitchActivity.this, R.font.open_sans_bold);

        timers = new Timer[switches.length];
        timerTasks = new TimerTask[switches.length];
        labeledSwitches = new LabeledSwitch[switches.length];

        for (int i = 0; i < switches.length; i++) {
            labeledSwitches[i] = findViewById(switches[i]);
            timers[i] = new Timer();

            final int finalI = i;
            timerTasks[i] = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> labeledSwitches[finalI].performClick());
                }
            };

            int delay = (2 + new Random().nextInt(5)) * 1000;
            Handler timerHandler = new Handler();
            timerHandler.postDelayed(() -> {
                if(!stopped) {
                    labeledSwitches[finalI].performClick();
                    timers[finalI].schedule(timerTasks[finalI], 0, 10000);
                }
            }, delay);
        }

        labeledSwitches[2].setTypeface(openSansBold);
        labeledSwitches[3].setTypeface(openSansBold);
        labeledSwitches[10].setDuration(5111);
        labeledSwitches[10].setInterpolator(new BounceInterpolator());
        labeledSwitches[10].setStartAnimationFromTouchPosition(false);
        labeledSwitches[10].setInterruptAnimation(false);
        labeledSwitches[10].setOnAnimateListener(new OnAnimateListener() {
            @Override
            public void onAnimate(ToggleableView toggleableView, float position) {
                Log.d("Actual_position_percent", String.valueOf(position));
            }
        });
    }

    @Override
    protected void onStop() {
        stopped = true;
        for (int i = 0; i < switches.length; i++) {
            if(timers[i] != null) {
                timers[i].cancel();
            }
        }
        super.onStop();
    }
}
