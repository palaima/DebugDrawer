package io.palaima.debugdrawer.actions.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import io.palaima.debugdrawer.actions.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SwitchAction implements Action {
    private final Context mContext;
    private final String mName;
    private final Listener mListener;
    private final CompoundButton.OnCheckedChangeListener mSwitchListener;

    private Switch mSwitch;

    public SwitchAction(Context context, String name, Listener listener) {
        mContext = context.getApplicationContext();
        mName = name;
        mListener = listener;
        mSwitchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCheckedChanged(isChecked);
                writeValue(isChecked);
            }
        };
    }

    @Override
    public View getView(LinearLayout linearLayout) {

        Context context = linearLayout.getContext();
        Resources resources = context.getResources();

        LinearLayout.LayoutParams viewGroupLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        viewGroupLayoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.padding_small);

        LinearLayout viewGroup = new LinearLayout(context);
        viewGroup.setLayoutParams(viewGroupLayoutParams);
        viewGroup.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textViewLayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.spacing_big);

        TextView textView = new TextView(context);
        textView.setLayoutParams(textViewLayoutParams);
        textView.setText(mName);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_normal));
        textView.setGravity(Gravity.CENTER_VERTICAL);

        mSwitch = new Switch(context);
        mSwitch.setOnCheckedChangeListener(mSwitchListener);

        viewGroup.addView(textView);
        viewGroup.addView(mSwitch);

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
    public void onStart() {
        mSwitch.setOnCheckedChangeListener(null);
        mSwitch.setChecked(readValue());
        mSwitch.setOnCheckedChangeListener(mSwitchListener);
    }

    @Override
    public void onStop() {
        /* no-op */
    }

    public boolean isChecked() {
        return readValue();
    }

    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
    }

    private boolean readValue() {
        return getPreferences().getBoolean(mName, false);
    }

    private void writeValue(boolean b) {
        getPreferences().edit().putBoolean(mName, b).apply();
    }

    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(mName, Context.MODE_PRIVATE);
    }

    public interface Listener {
        void onCheckedChanged(boolean value);
    }
}
