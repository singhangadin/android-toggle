# Toggle
Android Library for Custom Switches.

### Developed by
[Angad Singh](https://www.github.com/angads25) ([@angads25](https://www.twitter.com/angads25))

### Benchmark:
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14) [![Build Status](https://travis-ci.org/Angads25/android-toggle.svg?branch=release)](https://travis-ci.org/Angads25/android-toggle)

### Mentions:
[![Download](https://api.bintray.com/packages/angads25/maven/Toggle/images/download.svg)](https://bintray.com/angads25/maven/Toggle/_latestVersion) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.angads25/toggle/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.angads25/toggle)

### Read all about internal classes and functions in the [wiki](https://github.com/Angads25/android-toggle/wiki).

### Installation

* Library is also Available in MavenCentral, So just put this in your app dependencies to use it:
```gradle
    implementation 'com.github.angads25:toggle:1.1.0'
```

### Usage

1. Start by adding a Switch (eg. `LabeledSwitch`) in your xml layout as:

    ```xml
        <com.github.angads25.toggle.widget.LabeledSwitch
            android:id="@+id/switch"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="16dp"
            android:textSize="14sp"
            app:on="false"
            app:colorBorder="@color/colorAccent"/>
    ```

2. To the reference of Switch in your Activity/Fragment class set a Toggle Event Handler to it as below:

    ```java
        LabeledSwitch labeledSwitch = findViewById(R.id.switch);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                // Implement your switching logic here
            }
        });
    ```
    That's It. All your switching callbacks would be handled in `onSwitched` method, parameter `isOn` will provide the current state of the switch.
    
### Switches Available

* Labeled Switch | Designed by - [Shweta Gupta](https://dribbble.com/shwetagupta)

![Labeled Switch](https://raw.githubusercontent.com/Angads25/android-toggle/release/screenshots/version%201/LabeledSwitch.png)

* Day Night Switch | Designed by - [Ramakrishna V](https://dribbble.com/RamakrishnaUX)

![Day Night Switch](https://raw.githubusercontent.com/Angads25/android-toggle/release/screenshots/version%202/DayNightSwitch.gif)

### Buy me a Coffee

If this project helped you reduce the development time, please consider donating :D

[![Buy me a Coffee](https://raw.githubusercontent.com/Angads25/android-toggle/release/screenshots/bmc-button.webp)](https://www.buymeacoffee.com/singhangad.in)


### License
    Copyright (C) 2018 Angad Singh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.