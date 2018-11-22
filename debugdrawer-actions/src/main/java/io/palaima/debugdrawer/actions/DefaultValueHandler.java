package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.content.SharedPreferences;

public class DefaultValueHandler implements SwitchAction.ValueHandler, SpinnerAction.ValueHandler
{
    private final SharedPreferences sharedPreferences;

    public DefaultValueHandler(Context context) {
        sharedPreferences = context.getSharedPreferences("debugdrawer", Context.MODE_PRIVATE);
    }

    public DefaultValueHandler(Context context, String preferenceName) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    @Override
    public boolean getSwitchValue(String preferenceName)
    {
        return sharedPreferences.getBoolean(preferenceName, false);
    }

    @Override
    public void setSwitchValue(String preferenceName, boolean enabled)
    {
        sharedPreferences.edit().putBoolean(preferenceName, enabled).apply();
    }

    @Override
    public int getSpinnerValue(String preferenceName)
    {
        return sharedPreferences.getInt(preferenceName, 0);
    }

    @Override
    public void setSpinnerValue(String preferenceName, int selectedPosition)
    {
        sharedPreferences.edit().putInt(preferenceName, selectedPosition).apply();
    }
}
