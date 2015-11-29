package io.palaima.debugdrawer.actions;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.palaima.debugdrawer.actions.models.Action;
import io.palaima.debugdrawer.base.DebugModule;

public class ActionsModule implements DebugModule {
    private final List<Action> mActions = new ArrayList<>();

    public ActionsModule(Action... actions) {
        if (actions != null) {
            for (int i = 0; i < actions.length; i++) {
                mActions.add(actions[i]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.debug_drawer_module_actions, parent, false);

        if (mActions.isEmpty()) {
            TextView noActionsLabel = new TextView(parent.getContext());
            noActionsLabel.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            noActionsLabel.setText("No actions added");
            view.addView(noActionsLabel);
        } else {
            for (Action action : mActions) {
                view.addView(action.getView(view));
            }
        }

        return view;
    }

    @Override
    public void onOpened() {
        for (Action action : mActions) {
            action.onOpened();
        }
    }

    @Override
    public void onClosed() {
        for (Action action : mActions) {
            action.onClosed();
        }
    }

    @Override
    public void onResume() {
        for (Action action : mActions) {
            action.onResume();
        }
    }

    @Override
    public void onPause() {
        for (Action action : mActions) {
            action.onPause();
        }
    }

    @Override
    public void onStart() {
        for (Action action : mActions) {
            action.onStart();
        }
    }

    @Override
    public void onStop() {
        for (Action action : mActions) {
            action.onStop();
        }
    }
}
