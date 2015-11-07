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
import io.palaima.debugdrawer.module.DrawerModule;

public class ActionsModule implements DrawerModule {
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
        View view = inflater.inflate(R.layout.debug_drawer_module_actions, parent, false);

        LinearLayout linearLayout = (LinearLayout) view;

        if (mActions.isEmpty()) {
            TextView noActionsLabel = new TextView(parent.getContext());
            noActionsLabel.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.white));
            noActionsLabel.setText("No actions added");
            linearLayout.addView(noActionsLabel);
        } else {
            for (Action action : mActions) {
                linearLayout.addView(action.getView(linearLayout));
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
