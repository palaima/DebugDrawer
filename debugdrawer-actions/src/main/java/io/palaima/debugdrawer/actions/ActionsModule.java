package io.palaima.debugdrawer.actions;

import android.support.annotation.NonNull;
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
    private final String title;

    public ActionsModule(String title, Action... actions) {
        this.title = title;
        if (actions != null) {
            for (int i = 0; i < actions.length; i++) {
                this.actions.add(actions[i]);
            }
        }
    }

    public ActionsModule(Action... actions) {
        this("Actions", actions);
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dd_debug_drawer_module_actions, parent, false);
        ((TextView) view.findViewById(R.id.dd_debug_actions_title)).setText(title);

        if (actions.isEmpty()) {
            final TextView noActionsLabel = new TextView(parent.getContext());
            noActionsLabel.setTextColor(parent.getResources().getColor(android.R.color.white));
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
