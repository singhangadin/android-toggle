package com.github.angads25.labeledswitchdemo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.github.angads25.labeledswitch.LabeledSwitch;
import com.github.angads25.labeledswitch.interfaces.OnToggledListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabeledSwitch labeledSwitch = findViewById(R.id.switch_demo);
        Typeface openSansBold = ResourcesCompat.getFont(MainActivity.this, R.font.open_sans_bold);
        labeledSwitch.setTypeface(openSansBold);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                ((AppCompatTextView)findViewById(R.id.label_value)).setText(isOn ? "ON" : "OFF");
            }
        });
    }
}
