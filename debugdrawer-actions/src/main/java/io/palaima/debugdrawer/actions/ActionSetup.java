package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.support.annotation.NonNull;

public class ActionSetup
{
    private static ActionSetup instance = null;

    public static ActionSetup getInstance() {
        if(instance == null) {
            instance = new ActionSetup();
        }
        return instance;
    }

    private DefaultValueHandler defaultValueHandler = null;
    private SwitchAction.ValueHandler switchValueHandler = null;
    private SpinnerAction.ValueHandler spinnerValueHandler = null;

    private ActionSetup() {
    }

    public boolean isInitialised() {
        return defaultValueHandler != null || (switchValueHandler != null && spinnerValueHandler != null);
    }

    public void initDefault(Context context) {
        defaultValueHandler = new DefaultValueHandler(context);
    }

    public void initSwitchValueHandler(SwitchAction.ValueHandler switchValueHandler, SpinnerAction.ValueHandler spinnerValueHandler) {
        this.switchValueHandler = switchValueHandler;
        this.spinnerValueHandler = spinnerValueHandler;
    }

    public final SwitchAction.ValueHandler getSwitchValueHandler() {
        if (switchValueHandler == null && defaultValueHandler == null) {
            throw new RuntimeException("You must initialised the ActionSetup class before you can use the ActionModule!");        }
        return switchValueHandler != null ? switchValueHandler : defaultValueHandler;
    }

    public final SpinnerAction.ValueHandler getSpinnerActionHandler() {
        if (spinnerValueHandler == null && defaultValueHandler == null) {
            throw new RuntimeException("You must initialised the ActionSetup class before you can use the ActionModule!");        }
        return spinnerValueHandler != null ? spinnerValueHandler : defaultValueHandler;
    }
}
