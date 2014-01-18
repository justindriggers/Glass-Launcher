Glass Launcher
===========

This is a fork of the fantastic Glass Launcher by [justindriggers](https://github.com/justindriggers/Glass-Launcher). This version allows the user to pick an app just by saying its name, in addition to the existing scroll functionality.

To install, download the .apk file and type in: ```adb install GlassLauncher.apk```
Then, to run, type in: ```adb shell am start com.jtxdriggers.android.glass.glasslauncher/.MainActivity```

Glass Launcher is a simple application launcher for Google Glass.

The goal of Glass Launcher is to make Google Glass a more practical device for the average user.

![Flow Model for Glass Launcher](http://i.imgur.com/8tJRCRj.png)

Why should I use this over other application launchers?
There are currently two other solutions for launching applications. One of them involves installing the AOSP Launcher2 APK, which is designed for typical Android devices, making it difficult to use on Google Glass. The other intercepts an event and starts its own activity in place of the normal Settings activity.
Recently, Google allowed developers access to the Glass Development Kit (GDK), allowing deeper access into the platform's APIs. Glass Launcher is built with the GDK and gives the user a truly native feel when launching an application.

Use of this source code assumes that you know how to build and install applications to your Google Glass device. To install, simply build and run as you would any other application. After starting the first time, the service will start on each boot, creating a Glass Launcher card to the left of the Time card.

Building this application requires the Android SDK, with both the Android 4.0.3 SDK Platform (API 15) and the Glass Development Kit Sneap Peek installed.

Copyright 2013 Justin Driggers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
