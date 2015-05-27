# Android Debug Drawer

Faster development with Debug Drawer

![](https://github.com/palaima/DebugDrawer/raw/master/images/all.png)

## Features

Currently exists 6 modules:

`DeviceModule` - common information about your device

![](https://github.com/palaima/DebugDrawer/raw/master/images/device.png)

`BuildModule` - app build information

![](https://github.com/palaima/DebugDrawer/raw/master/images/build.png)

`SettingsModule` - open Developer, Battery, Default settings, open app info and possibility to uninstall app directly from itself

![](https://github.com/palaima/DebugDrawer/raw/master/images/settings.png)

`NetworkModule` - enable/disable Wifi, Mobile or Bluetooth

![](https://github.com/palaima/DebugDrawer/raw/master/images/network.png)

`OkHttpModule` - common information about http client (requires extra dependency)

![](https://github.com/palaima/DebugDrawer/raw/master/images/okhttp.png)

`PicassoModule` - image downloading and caching statistics (requires extra dependency)

![](https://github.com/palaima/DebugDrawer/raw/master/images/picasso.png)

## TODO Features

`LocationModule`, `UserInterfaceModule`, `LogsModule`

## Getting Started

Add Gradle dependency:

```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer:0.1.1'
}
```

If you are using popular [OkHttp](https://github.com/square/okhttp) library. Probably you will be interesting in network statistics
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-okhttp:0.1.1'
}
```

Or if you are using [Picasso](https://github.com/square/picasso) library, also from Square Inc.
```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer-picasso:0.1.1'
}
```


* Or
[DebugDrawer Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer/0.1.1/debugdrawer-0.1.1.aar)

* Or
[DebugDrawer-Picasso Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-picasso/0.1.1/debugdrawer-picasso-0.1.1.aar)

* Or
[DebugDrawer-OkHttp Download from Maven](https://oss.sonatype.org/content/repositories/releases/io/palaima/debugdrawer/debugdrawer-okhttp/0.1.1/debugdrawer-okhttp-0.1.1.aar)

You can try the SNAPSHOT version:

```gradle
dependencies {
   compile 'io.palaima.debugdrawer:debugdrawer:0.2.0-SNAPSHOT'
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
If you use NetworkModule or your own which is hooked with BroadcastReceivers you must call onStart/onStop in your activity

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

## Creating You Own Module
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
    void onRefreshView();

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
Note that all pull request should go to `dev` branch.

Developed By
------------

* Mantas Palaima - <palaima.mantas@gmail.com>

Credits
------------

Jake Wharton - [U2020](https://github.com/JakeWharton/u2020)

Mike Penz - [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer)

LemonLabs - [SlidingDebugMenu](https://github.com/lemonlabs/slidingdebugmenu)

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
