package io.palaima.debugdrawer.util;

import android.support.annotation.CheckResult;

/**
 * @author Kale
 * @date 2016/5/2
 */
/*package*/public class LibUtil {

    public static
    @CheckResult
    boolean hasDependency(String packageName) {
        try {
            Class.forName(packageName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
