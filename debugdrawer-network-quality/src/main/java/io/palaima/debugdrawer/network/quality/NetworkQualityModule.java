package io.palaima.debugdrawer.network.quality;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import io.palaima.debugdrawer.base.DebugModuleAdapter;
import okhttp3.Interceptor;

public class NetworkQualityModule extends DebugModuleAdapter {

    private static final boolean HAS_OKHTTP3;

    static {
        boolean hasDependency;

        try {
            Class.forName("okhttp3.OkHttpClient");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_OKHTTP3 = hasDependency;
    }

    public static Interceptor interceptor(Context context) {
        if (!HAS_OKHTTP3) {
            throw new RuntimeException("OkHttp3 dependency is not found");
        }
        return new NetworkQualityInterceptor(new NetworkQualityConfig(context));
    }

    private static final List<Integer> DELAY_VALUES = Arrays.asList(0, 500, 1000, 2000, 3000, 5000, 10000);
    private static final List<Integer> ERROR_VALUES = Arrays.asList(0, 3, 10, 25, 50, 100);

    private final NetworkQualityConfig networkQualityConfig;

    private Switch enabledSwitch;
    private SeekBar delaySeekBar;
    private SeekBar errorSeekBar;

    public NetworkQualityModule(@NonNull Context context) {
        if (!HAS_OKHTTP3) {
            throw new RuntimeException("OkHttp3 dependency is not found");
        }
        this.networkQualityConfig = new NetworkQualityConfig(context);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_module_network, parent, false);
        enabledSwitch = view.findViewById(R.id.dd_module_networkquality_enabled);
        enabledSwitch.setSaveEnabled(false);
        enabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                networkQualityConfig.networkEnabled(isChecked);
                delaySeekBar.setEnabled(isChecked);
                errorSeekBar.setEnabled(isChecked);
            }
        });

        final TextView delayIndicator = view.findViewById(R.id.debug_network_delay_indicator);
        delaySeekBar = view.findViewById(R.id.debug_network_delay);
        delaySeekBar.setSaveEnabled(false);
        delaySeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                delaySeekBar.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        delaySeekBar.setMax(DELAY_VALUES.size() - 1);
        delaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int delay = DELAY_VALUES.get(progress);
                networkQualityConfig.delayMs(delay);
                delayIndicator.setText(delay(delay));
                setThemeColor(delayIndicator, progress == 0 ? android.R.attr.textColorSecondary : R.attr.colorAccent);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView errorIndicator = view.findViewById(R.id.debug_network_error_indicator);
        errorSeekBar = view.findViewById(R.id.debug_network_error);
        errorSeekBar.setSaveEnabled(false);
        errorSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                errorSeekBar.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        errorSeekBar.setMax(ERROR_VALUES.size() - 1);
        errorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int errorPercentage = ERROR_VALUES.get(progress);
                networkQualityConfig.errorPercentage(errorPercentage);
                errorIndicator.setText(errorPercentage + "%");

                setThemeColor(errorIndicator, progress == 0 ? android.R.attr.textColorSecondary : R.attr.colorAccent);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enabledSwitch.setChecked(networkQualityConfig.networkEnabled());
        delaySeekBar.setProgress(DELAY_VALUES.indexOf(networkQualityConfig.delayMs()));
        errorSeekBar.setProgress(ERROR_VALUES.indexOf(networkQualityConfig.errorPercentage()));
    }

    private String delay(int delay) {
        final String text;
        if (delay == 0) {
            text = "0s";
        } else if (delay % 1000 == 0) {
            text = delay / 1000.0 + "s";
        } else {
            text = ((float) delay) / 1000.0 + "s";
        }
        return text;
    }

    private void setThemeColor(TextView textView, @AttrRes int colorAttr) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = textView.getContext().obtainStyledAttributes(typedValue.data, new int[]{colorAttr});
        int color = a.getColor(0, 0);

        a.recycle();

        textView.setTextColor(color);
    }
}
