# Toggle
Android Library for Custom Switches.

### Developed by
[Angad Singh](https://www.github.com/angads25) ([@angads25](https://www.twitter.com/angads25))

### Benchmark:
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

### Where to Find:
[![Download](https://api.bintray.com/packages/angads25/maven/Toggle/images/download.svg)](https://bintray.com/angads25/maven/Toggle/_latestVersion) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.angads25/toggle/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.angads25/toggle)


### Installation

* Library is also Available in MavenCentral, So just put this in your app dependencies to use it:
```gradle
    compile 'com.github.angads25:toggle:1.0.0'
```

### Usage

1. Start by adding a Switch (eg. LabeledSwitch) in your xml layout as:

    ```xml

        <com.github.angads25.toggle.LabeledSwitch
            android:id="@+id/switch"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="16dp"
            android:textSize="14sp"
            app:on="false"
            app:colorBorder="@color/colorAccent"/>
    ```

2. To the reference of Switch in your Activity/Fragment class and set a ToggleListener to it as below:

    ```java
        LabeledSwitch labeledSwitch = findViewById(R.id.switch);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                // Implement your switching logic here
            }
        });
    ```
    That's It. All your switching callbacks would be handled in onSwitched method, isOn will provide the current state of the switch.
    
### Switches Available

* Labeled Switch | Designed by - [Shweta Gupta](https://dribbble.com/shwetagupta)

![Labeled Switch](https://raw.githubusercontent.com/Angads25/android-toggle/develop/screenshots/version%201/device-2018-02-21-233623.png)