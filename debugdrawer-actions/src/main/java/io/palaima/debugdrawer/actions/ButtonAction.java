package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ButtonAction implements Action {

    private final String   name;
    private final Listener listener;

    public ButtonAction(String name, Listener listener) {
        this.name = name;
        this.listener = listener;
    }

    @Override
    public View getView(LinearLayout linearLayout) {
        Context context = linearLayout.getContext();
        Resources resources = context.getResources();

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        buttonLayoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.padding_small);

        Button button = new Button(context);
        button.setLayoutParams(buttonLayoutParams);
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
