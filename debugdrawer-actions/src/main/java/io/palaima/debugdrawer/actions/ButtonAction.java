package io.palaima.debugdrawer.actions;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonAction implements Action {

    private final String name;
    private final Listener listener;

    public ButtonAction(String name, Listener listener) {
        this.name = name;
        this.listener = listener;
    }

    @Override
    public View getView(@NonNull final LayoutInflater inflater, @NonNull final LinearLayout parent) {
        final Button button = (Button) inflater.inflate(R.layout.dd_debug_drawer_module_actions_button, parent, false);
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });

        return button;
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
        /* no-op */
    }

    @Override
    public void onStop() {
        /* no-op */
    }

    public interface Listener {
        void onClick();
    }
}
