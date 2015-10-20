# Android Debug Drawer

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Debug%20Drawer-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1892)

Faster development with Debug Drawer

![](./images/all.png)

## Features

Currently exists 9 modules:

`DeviceModule` - common information about your device

![](./images/device.png)

`BuildModule` - app build information

![](./images/build.png)

`SettingsModule` - open Developer, Battery, Default settings, open app info and possibility to uninstall app directly from itself

![](./images/settings.png)

`NetworkModule` - enable/disable Wifi, Mobile or Bluetooth

![](./images/network.png)

`OkHttpModule` - common information about http client (requires extra dependency)

![](./images/okhttp.png)

`PicassoModule` - image downloading and caching statistics (requires extra dependency)

![](./images/picasso.png)

`ScalpelModule` - tool to uncover the layers under your app (requires extra dependency)
Thanks [ebabel](https://github.com/ebabel) for contributing.

![](./images/scalpel.png)

`LocationModule` - common location information (requires extra dependency)

![](./images/location.png)

`LogModule` - log viewer with sharing feature
Thanks [Vilian](https://github.com/Vilian) for contributing.

![](./images/log.png)

## TODO
- Network delay/error adapters
- Take screenshot feature

You are always welcome to suggest modules!

## Getting Started

Add Gradle dependency:

```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer:0.4.0'
}
```

If you are using popular [OkHttp](https://github.com/square/okhttp) library. Probably you will be interesting in network statistics
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-okhttp:0.4.0'
}
```

Or if you are using [Picasso](https://github.com/square/picasso) library, also from Square Inc.
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-picasso:0.4.0'
}
```

`ScalpelModule`
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-scalpel:0.4.0'
}
```

`LocationModule`
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-location:0.4.0'
}
```

`LogModule`
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-log:0.4.0'
}
```

* Or
[DebugDrawer Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer/0.4.0/debugdrawer-0.4.0.aar)

* Or
[DebugDrawer-Picasso Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-picasso/0.4.0/debugdrawer-picasso-0.4.0.aar)

* Or
[DebugDrawer-OkHttp Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-okhttp/0.4.0/debugdrawer-okhttp-0.4.0.aar)

* Or
[DebugDrawer-Scalpel Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-scalpel/0.4.0/debugdrawer-scalpel-0.4.0.aar)

* Or
[DebugDrawer-Location Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-location/0.4.0/debugdrawer-location-0.4.0.aar)

* Or
[DebugDrawer-Log Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-log/0.4.0/debugdrawer-log-0.4.0.aar)

You can try the SNAPSHOT version:

```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer:0.5.0-SNAPSHOT'
}
```
Make sure to add the snapshot repository:

```gradle
repositories {
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }
}
```

## Putting All Together

### 1. Initialization in Activity

```java
private DebugDrawer mDebugDrawer;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (BuildConfig.DEBUG) {
        mDebugDrawer = new DebugDrawer.Builder(this).modules(
                new LocationModule(this),
                new ScalpelModule(this),
                new LogModule(),
                new OkHttpModule(mOkHttpClient),
                new PicassoModule(mPicasso),
                new DeviceModule(this),
                new BuildModule(this),
                new NetworkModule(this),
                new SettingsModule(this)
        ).build();
    }
}
```

### 2. onStart/onStop
If you use `NetworkModule`/`LocationModule` or your own which is hooked with BroadcastReceivers you must call onStart/onStop in your activity

```java
@Override
protected void onStart() {
    super.onStart();
    if (mDebugDrawer != null) {
        mDebugDrawer.onStart();
    }
}
```

```java
@Override
protected void onStop() {
    super.onStop();
    if (mDebugDrawer != null) {
        mDebugDrawer.onStop();
    }
}
```

### 3. Timber
If you want to use `LogModule` you need to use [Timber](https://github.com/JakeWharton/timber) for logging. Don't forget
to plant needed log trees in Application class. Tree that is used by `LogModule` stored in `LumberYard` class.

Application class example:

```java
public class DebugDrawerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LumberYard lumberYard = LumberYard.getInstance(this);
        lumberYard.cleanUp();

        Timber.plant(lumberYard.tree());
        Timber.plant(new Timber.DebugTree());
    }
}
```

## Creating Your Own Module
Module must implement `DrawerModule` interface


```java
public interface DrawerModule {

    /**
     * Creates module view
     */
    View onCreateView(LayoutInflater inflater, ViewGroup parent);

    /**
     * Override this method if you need to refresh
     * some information  when drawer is opened
     */
    void onOpened();

    /**
     * Override this method if you need to stop
     * some actions  when drawer is closed
     */
    void onClosed();

    /**
     * Override this method if you need to start
     * some processes that would be killed when
     * onStop() is called
     * E.g. register receivers
     */
    void onStart();

    /**
     * Override this method if you need to do
     * some clean up when activity goes to foreground.
     * E.g. unregister receivers
     */
    void onStop();
}
```

## Sample

You can clone the project and compile it yourself (it includes a sample).
[MainActivity](https://github.com/palaima/DebugDrawer/blob/master/app/src/main/java/io/palaima/debugdrawer/app/MainActivity.java)

## Contributing
Want to contribute? You are welcome!

### Pull Requests
* Fork the repo and create your branch from `dev`.
* If you've changed APIs, update the documentation.
* Make sure your code lints.
* Change README.md if necessary

### Coding Style
* Use the `m` member variable prefix for private fields
* Opening braces to appear on the same line as code
* All variables must be `CamelCase`

Developed By
------------

* Mantas Palaima - <palaima.mantas@gmail.com>

Credits
------------

* Jake Wharton - [U2020](https://github.com/JakeWharton/u2020)

* Mike Penz - [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer)

* LemonLabs - [SlidingDebugMenu](https://github.com/lemonlabs/slidingdebugmenu)

License
--------

    Copyright 2015 Mantas Palaima.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.