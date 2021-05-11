# Toggle
Android Library for Custom Switches.

### Added feature
 1. Added different toggle and text color
   - app:colorText="#ffffff"
   - app:colorToggle="#ffffff"

 2. Added text modification option
   - app:textUpperCase
   - app:textLowerCase

### Developed by
[Angad Singh](https://www.github.com/angads25) ([@angads25](https://www.twitter.com/angads25))

### Benchmark:
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14) [![Build Status](https://travis-ci.org/Angads25/android-toggle.svg?branch=release)](https://travis-ci.org/Angads25/android-toggle)

### Mentions:
[![](https://jitpack.io/v/jozsefmezei/android-toggle.svg)](https://jitpack.io/#jozsefmezei/android-toggle)

### Read all about internal classes and functions in the [wiki](https://github.com/Angads25/android-toggle/wiki).

### Installation

Add it in your root build.gradle at the end of repositories:
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

* Put this in your app dependencies:
```gradle
    implementation 'com.github.jozsefmezei:android-toggle:1.1.1'
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

![Labeled Switch](https://raw.githubusercontent.com/jozsefmezei/android-toggle/release/screenshots/version%201/LabeledSwitch.png)

* Day Night Switch | Designed by - [Ramakrishna V](https://dribbble.com/RamakrishnaUX)

![Day Night Switch](https://raw.githubusercontent.com/Angads25/android-toggle/release/screenshots/version%202/DayNightSwitch.gif)

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