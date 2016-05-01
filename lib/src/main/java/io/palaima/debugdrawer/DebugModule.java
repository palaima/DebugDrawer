package io.palaima.debugdrawer;

import android.support.annotation.NonNull;

/**
 * @author Kale
 * @date 2016/4/30
 */
public interface DebugModule {

    @NonNull String getName();
    
    DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder);
}
