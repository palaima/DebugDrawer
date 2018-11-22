package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAction<T> implements Action {

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T value);
    }

    private final String name;
    private final List<String> titles;
    private final List<T> values;
    private final OnItemSelectedListener<T> listener;

    private int selectedPosition;

    public SpinnerAction(String name, List<T> values, OnItemSelectedListener<T> listener) {
        this(name, getTitles(values), values, listener);
    }

    public SpinnerAction(String name, List<String> titles, List<T> values, OnItemSelectedListener<T> listener) {
        this.name = name;
        this.values = values;
        this.titles = titles;
        this.listener = listener;
        this.selectedPosition = ActionSetup.getInstance().getSpinnerActionHandler().getSpinnerValue(name);
        if (selectedPosition < 0 || selectedPosition >= values.size()) {
            throw new IllegalStateException("initial selected position is out of bounds");
        }
    }

    @Override
    public View getView(@NonNull final LayoutInflater inflater, @NonNull final LinearLayout parent) {
        final Context context = parent.getContext();
        final View viewGroup = inflater.inflate(R.layout.dd_debug_drawer_module_actions_spinner, parent, false);

        final TextView textView = viewGroup.findViewById(R.id.action_spinner_name);
        final Spinner spinner = viewGroup.findViewById(R.id.action_spinner);

        textView.setText(name);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null && position != selectedPosition) {
                    listener.onItemSelected(values.get(position));
                }
                if (position != selectedPosition) {
                    selectedPosition = position;
                    ActionSetup.getInstance().getSpinnerActionHandler().setSpinnerValue(name, selectedPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dd_debug_drawer_module_actions_spinner_item, titles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (!values.isEmpty()) {
            spinner.setSelection(selectedPosition);
        }

        return viewGroup;
    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    private static <T> List<String> getTitles(List<T> values) {
        final List<String> titles = new ArrayList<>(values.size());
        for (T value : values) {
            titles.add(value.toString());
        }
        return titles;
    }

    public interface ValueHandler {
        int getSpinnerValue(String preferenceName );
        void setSpinnerValue(String preferenceName, int selectedPosition);
    }
}
