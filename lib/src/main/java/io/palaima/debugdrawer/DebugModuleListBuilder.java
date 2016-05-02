package io.palaima.debugdrawer;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.palaima.debugdrawer.modules.BuildModule;
import io.palaima.debugdrawer.modules.DeviceModule;
import io.palaima.debugdrawer.modules.FpsModule;
import io.palaima.debugdrawer.modules.LogcatModule;
import io.palaima.debugdrawer.modules.NetworkModule;
import io.palaima.debugdrawer.modules.OkHttp3Module;
import io.palaima.debugdrawer.modules.ScalpelModule;
import io.palaima.debugdrawer.modules.SettingsModule;
import jp.wasabeef.takt.Takt;
import okhttp3.OkHttpClient;

/**
 * @author Kale
 * @date 2016/5/2
 */
public class DebugModuleListBuilder {

    private List<DebugModule> moduleList;

    private Context context;

    public DebugModuleListBuilder(Context context) {
        moduleList = new ArrayList<>();
        this.context = context;
    }

    public DebugModuleListBuilder addDefaultModules(Activity activity) {
        List<DebugModule> list = Arrays.asList(
                new NetworkModule(context),
                new FpsModule(Takt.stock(activity.getApplication())),
                new BuildModule(context),
                new ScalpelModule(activity),
                new LogcatModule(activity),
                new DeviceModule(context),
                new SettingsModule(activity));
        
        moduleList = new ArrayList<>(list);
        return this;
    }

    public DebugModuleListBuilder addDefaultModules(Activity activity, OkHttpClient okHttpClient) {
        DebugModuleListBuilder builder = addDefaultModules(activity);
        builder.moduleList.add(0, new OkHttp3Module(okHttpClient));
        return this;
    }

    public DebugModuleListBuilder addOkHttp3Module(OkHttpClient okHttpClient) {
        moduleList.add(new OkHttp3Module(okHttpClient));
        return this;
    }

    public DebugModuleListBuilder addNetworkModule() {
        moduleList.add(new NetworkModule(context));
        return this;
    }

    public DebugModuleListBuilder addFpsModule(Activity activity) {
        moduleList.add(new FpsModule(Takt.stock(activity.getApplication())));
        return this;
    }

    public DebugModuleListBuilder addBuildModule() {
        moduleList.add(new BuildModule(context));
        return this;
    }

    public DebugModuleListBuilder addScalpelModule(Activity activity) {
        moduleList.add(new ScalpelModule(activity));
        return this;
    }

    public DebugModuleListBuilder addLogcatModule(Activity activity) {
        moduleList.add(new LogcatModule(activity));
        return this;
    }

    public DebugModuleListBuilder addDeviceModule() {
        moduleList.add(new DeviceModule(context));
        return this;
    }

    public DebugModuleListBuilder addSettingsModule(Activity activity) {
        moduleList.add(new SettingsModule(activity));
        return this;
    }

    public List<DebugModule> build() {
        return moduleList;
    }

}
