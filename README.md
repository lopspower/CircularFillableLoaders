<p align="center"><img src="/preview/header.png"></p>

CircularFillableLoaders
=================

<img src="/preview/preview.gif" alt="sample" title="sample" width="300" height="447" align="right" vspace="52" />

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Maven Central](https://img.shields.io/maven-central/v/com.mikhaellopez/circularfillableloaders.svg?label=Maven%20Central)](https://search.maven.org/artifact/com.mikhaellopez/circularfillableloaders)
<br>
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CircularFillableLoaders-lightgrey.svg?style=flat)](https://android-arsenal.com/details/1/2897)
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

This is an Android project allowing to realize a beautiful circular fillable loaders to be used for splashscreen for example.

<a href="https://play.google.com/store/apps/details?id=com.mikhaellopez.lopspower">
  <img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

USAGE
-----

To make a circular fillable loaders add CircularFillableLoaders in your layout XML and add CircularFillableLoaders library in your project or you can also grab it via Gradle:

```groovy
implementation 'com.mikhaellopez:circularfillableloaders:1.4.0'
```

XML
-----

```xml
<com.mikhaellopez.circularfillableloaders.CircularFillableLoaders
    android:id="@+id/circularFillableLoaders"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/your_logo"
    app:cfl_border="true"
    app:cfl_border_width="12dp"
    app:cfl_progress="80"
    app:cfl_wave_amplitude="0.06"
    app:cfl_wave_color="#3f51b5" />
```

You must use the following properties in your XML to change your CircularFillableLoaders.

| Properties              | Type      | Default                         |
| ----------------------- | ----------| ------------------------------- |
| `app:cfl_progress`      | integer   | 0                               |
| `app:cfl_border`        | boolean   | true                            |
| `app:cfl_border_width`  | dimension | 4dp                             |
| `app:cfl_wave_color`    | color     | BLACK                           |
| `app:cfl_wave_amplitude`| float     | 0.05f (between 0.00f and 0.10f) |

JAVA
-----

```java
CircularFillableLoaders circularFillableLoaders = (CircularFillableLoaders)findViewById(R.id.yourCircularFillableLoaders);
// Set Progress
circularFillableLoaders.setProgress(60);
// Set Wave and Border Color
circularFillableLoaders.setColor(Color.RED);
// Set Border Width
circularImageView.setBorderWidth(10 * getResources().getDisplayMetrics().density);
// Set Wave Amplitude (between 0.00f and 0.10f)
circularFillableLoaders.setAmplitudeRatio(0.08);
```

SUPPORT ❤️
-----

Find this library useful? Support it by joining [**stargazers**](https://github.com/lopspower/CircularFillableLoaders/stargazers) for this repository ⭐️
<br/>
And [**follow me**](https://github.com/lopspower?tab=followers) for my next creations 👍

LICENCE
-----

CircularFillableLoaders by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
Based on a work at https://github.com/gelitenight/WaveView.
