package io.palaima.debugdrawer.actions;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAction<T> implements Action {

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T value);
    }

    private final static int SPINNER_DEFAULT_POSITION = 0;

    private final List<String> titles;
    private final List<T> values;
    private final OnItemSelectedListener<T> listener;

    private int selectedPosition = SPINNER_DEFAULT_POSITION;

    public SpinnerAction(List<T> values, OnItemSelectedListener<T> listener) {
        this(getTitles(values), values, listener, SPINNER_DEFAULT_POSITION);
    }

    public SpinnerAction(List<T> values, OnItemSelectedListener<T> listener, int initialSelectedPosition) {
        this(getTitles(values), values, listener, initialSelectedPosition);
    }

    public SpinnerAction(List<String> titles, List<T> values, OnItemSelectedListener<T> listener, int initialSelectedPosition) {
        this.values = values;
        this.titles = titles;
        this.listener = listener;
        if (initialSelectedPosition >= 0 && initialSelectedPosition < values.size()) {
            this.selectedPosition = initialSelectedPosition;
        } else {
            throw new IllegalStateException("initial selected position is out of bounds");
        }
    }

    @Override
    public View getView(@NonNull final LayoutInflater inflater, @NonNull final LinearLayout parent) {
        final Context context = parent.getContext();
        final Spinner spinner = (Spinner) inflater.inflate(R.layout.dd_debug_drawer_module_actions_spinner, parent, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null && position != selectedPosition) {
                    listener.onItemSelected(values.get(position));
                }
                selectedPosition = position;
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

        return spinner;
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
}
