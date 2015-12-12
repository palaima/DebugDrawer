package io.palaima.debugdrawer.actions;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SpinnerAction<T> implements Action {

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T value);
    }

    private final List<String>              titles;
    private final List<T>                   values;
    private final OnItemSelectedListener<T> listener;

    private int selectedPosition = 0;

    public SpinnerAction(List<T> values, OnItemSelectedListener<T> listener) {
        this(getTitles(values), values, listener);
    }

    public SpinnerAction(List<String> titles, List<T> values, OnItemSelectedListener<T> listener) {
        this.values = values;
        this.titles = titles;
        this.listener = listener;
    }

    @Override
    public View getView(LinearLayout view) {
        Context context = view.getContext();
        Resources resources = context.getResources();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.padding_small);

        Spinner spinner = new Spinner(context);
        spinner.setLayoutParams(layoutParams);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null && position != selectedPosition) {
                    listener.onItemSelected(values.get(position));
                }
                selectedPosition = position;
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.debug_drawer_module_actions_spinner_item, titles);
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
        List<String> titles = new ArrayList<>(values.size());
        for (T value : values) {
            titles.add(value.toString());
        }
        return titles;
    }
}