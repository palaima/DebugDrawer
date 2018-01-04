package io.palaima.debugdrawer.network.quality;

import android.content.Context;
import android.content.SharedPreferences;

final class NetworkQualityConfig {

    private final SharedPreferences prefs;

    NetworkQualityConfig(Context context) {
        this.prefs = context.getSharedPreferences("debug_drawer_okhttp_network_quality", Context.MODE_PRIVATE);
    }

    boolean networkEnabled() {
        return prefs.getBoolean("networkEnabled", true);
    }

    int delayMs() {
        return prefs.getInt("delayMs", 0);
    }

    int errorPercentage() {
        return prefs.getInt("errorPercentage", 0);
    }

    void networkEnabled(boolean networkEnabled) {
        prefs.edit().putBoolean("networkEnabled", networkEnabled).apply();
    }

    void delayMs(int delayMs) {
        prefs.edit().putInt("delayMs", delayMs).apply();
    }

    void errorPercentage(int errorPercentage) {
        prefs.edit().putInt("errorPercentage", errorPercentage).apply();
    }
}
