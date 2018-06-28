<p align="center"><img src="http://i66.tinypic.com/5zgqwn.png"></p>

CircularFillableLoaders
=================

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CircularFillableLoaders-lightgrey.svg?style=flat)](https://android-arsenal.com/details/1/2897)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![Twitter](https://img.shields.io/badge/Twitter-@LopezMikhael-blue.svg?style=flat)](http://twitter.com/lopezmikhael)

This is an Android project allowing to realize a beautiful circular fillable loaders to be used for splashscreen for example.

<img src="/preview/preview.gif" alt="sample" title="sample" width="300" height="447" align="right" vspace="52" />

USAGE
-----

To make a circular fillable loaders add CircularFillableLoaders in your layout XML and add CircularFillableLoaders library in your project or you can also grab it via Gradle:

```groovy
implementation 'com.mikhaellopez:circularfillableloaders:1.3.2'
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

You must use the following properties in your XML to change your CircularImageView.


##### Properties:

* `app:cfl_progress`        (integer)   -> default 0
* `app:cfl_border`          (boolean)   -> default true
* `app:cfl_border_width`    (dimension) -> default 4dp
* `app:cfl_wave_color`      (color)     -> default BLACK
* `app:cfl_wave_amplitude`  (float)     -> default 0.05f (between 0.00f and 0.10f)

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


LICENCE
-----

CircularImageView by [Lopez Mikhael](http://mikhaellopez.com/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
Based on a work at https://github.com/gelitenight/WaveView.
