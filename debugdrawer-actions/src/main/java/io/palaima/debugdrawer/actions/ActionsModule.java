package io.palaima.debugdrawer.actions;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.palaima.debugdrawer.base.DebugModule;

public class ActionsModule implements DebugModule {
    private final List<Action> actions = new ArrayList<>();

    public ActionsModule(Action... actions) {
        if (actions != null) {
            for (int i = 0; i < actions.length; i++) {
                this.actions.add(actions[i]);
            }
        }
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.debug_drawer_module_actions, parent, false);

        if (actions.isEmpty()) {
            TextView noActionsLabel = new TextView(parent.getContext());
            noActionsLabel.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            noActionsLabel.setText("No actions added");
            view.addView(noActionsLabel);
        } else {
            for (Action action : actions) {
                view.addView(action.getView(view));
            }
        }

        return view;
    }

    @Override
    public void onOpened() {
        for (Action action : actions) {
            action.onOpened();
        }
    }

    @Override
    public void onClosed() {
        for (Action action : actions) {
            action.onClosed();
        }
    }

    @Override
    public void onResume() {
        for (Action action : actions) {
            action.onResume();
        }
    }

    @Override
    public void onPause() {
        for (Action action : actions) {
            action.onPause();
        }
    }

    @Override
    public void onStart() {
        for (Action action : actions) {
            action.onStart();
        }
    }

    @Override
    public void onStop() {
        for (Action action : actions) {
            action.onStop();
        }
    }
}
