package io.palaima.debugdrawer.actions.models;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import io.palaima.debugdrawer.actions.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ButtonAction implements Action {
    private final String mName;
    private final Listener mListener;

    public ButtonAction(String name, Listener listener) {
        mName = name;
        mListener = listener;
    }

    @Override
    public View getView(LinearLayout linearLayout) {
        Context context = linearLayout.getContext();
        Resources resources = context.getResources();

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        buttonLayoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.padding_small);

        Button button = new Button(context);
        button.setLayoutParams(buttonLayoutParams);
        button.setText(mName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick();
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
