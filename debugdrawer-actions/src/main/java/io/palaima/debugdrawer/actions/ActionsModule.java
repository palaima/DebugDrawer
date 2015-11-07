package io.palaima.debugdrawer.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import io.palaima.debugdrawer.actions.models.Action;
import io.palaima.debugdrawer.actions.models.SwitchAction;
import io.palaima.debugdrawer.module.DrawerModule;

import java.util.ArrayList;
import java.util.List;

public class ActionsModule implements DrawerModule {
    private final List<Action> mActions = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_actions, parent, false);

        LinearLayout linearLayout = (LinearLayout) view;

        for (Action action : mActions) {
            linearLayout.addView(action.getView(linearLayout));
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

    public void addAction(Action action) {
        mActions.add(action);
    }
}
