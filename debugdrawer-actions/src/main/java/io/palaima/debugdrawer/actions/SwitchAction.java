package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class SwitchAction implements Action {

    private final String name;
    private final Listener listener;
    private final boolean shouldEmitFirstValue;

    private WeakReference<Context> contextRef;

    private Switch switchButton;

    public SwitchAction(String name, @Nullable Listener listener) {
        this.name = name;
        this.listener = listener;
        this.shouldEmitFirstValue = false;
    }

    public SwitchAction(String name, @Nullable Listener listener, boolean shouldEmitFirstValue) {
        this.name = name;
        this.listener = listener;
        this.shouldEmitFirstValue = shouldEmitFirstValue;
    }

    @Override
    public View getView(@NonNull final LayoutInflater inflater, @NonNull final LinearLayout parent) {
        final Context context = parent.getContext();

        if (contextRef == null) {
            contextRef = new WeakReference<>(context);
        }

        View viewGroup = inflater.inflate(R.layout.dd_debug_drawer_module_actions_switch, parent, false);

        final TextView textView = viewGroup.findViewById(R.id.action_switch_name);
        textView.setText(name);

        switchButton = viewGroup.findViewById(R.id.action_switch_switch);
        switchButton.setOnCheckedChangeListener(switchListener);

        return viewGroup;
    }

    @Override
    public void onOpened() {
        /* no-op */
    }

    @Override
    public void onClosed() {
        /* no-op */
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {
        final boolean isChecked = readValue();

        switchButton.setOnCheckedChangeListener(null);
        switchButton.setChecked(isChecked);
        switchButton.setOnCheckedChangeListener(switchListener);

        if (shouldEmitFirstValue && listener != null) {
            listener.onCheckedChanged(isChecked);
        }
    }

    @Override
    public void onStop() {
        /* no-op */
    }

    private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (listener != null) {
                listener.onCheckedChanged(isChecked);
            }
            writeValue(isChecked);
        }
    };

    public boolean isChecked() {
        return readValue();
    }

    public void setChecked(boolean checked) {
        switchButton.setChecked(checked);
    }

    private boolean readValue() {
        return getPreferences().getBoolean(name, false);
    }

    private void writeValue(boolean b) {
        getPreferences().edit().putBoolean(name, b).apply();
    }

    private SharedPreferences getPreferences() {
        return contextRef.get().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public interface Listener {
        void onCheckedChanged(boolean value);
    }
}
