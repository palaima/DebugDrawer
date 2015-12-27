package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SwitchAction implements Action {

    private final String   name;
    private final Listener listener;

    private Context context;
    private Switch  switchButton;

    public SwitchAction(String name, Listener listener) {
        this.name = name;
        this.listener = listener;
    }

    @Override
    public View getView(LinearLayout linearLayout) {

        context = linearLayout.getContext();
        Resources resources = context.getResources();

        LinearLayout.LayoutParams viewGroupLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        viewGroupLayoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.dd_padding_small);

        LinearLayout viewGroup = new LinearLayout(context);
        viewGroup.setLayoutParams(viewGroupLayoutParams);
        viewGroup.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textViewLayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.dd_spacing_big);

        TextView textView = new TextView(context);
        textView.setLayoutParams(textViewLayoutParams);
        textView.setText(name);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.dd_font_normal));
        textView.setGravity(Gravity.CENTER_VERTICAL);

        switchButton = new Switch(context);
        switchButton.setOnCheckedChangeListener(switchListener);

        viewGroup.addView(textView);
        viewGroup.addView(switchButton);

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
        switchButton.setOnCheckedChangeListener(null);
        switchButton.setChecked(readValue());
        switchButton.setOnCheckedChangeListener(switchListener);
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
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public interface Listener {
        void onCheckedChanged(boolean value);
    }
}
