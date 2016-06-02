package io.palaima.debugdrawer;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kale
 * @date 2016/4/30
 */
public class DebugWidgets {

    private List<DebugWidgetsBuilder.DebugWidget> widgets;

    public DebugWidgets(List<DebugWidgetsBuilder.DebugWidget> widgets) {
        this.widgets = widgets;
    }

    public List<DebugWidgetsBuilder.DebugWidget> getWidgets() {
        return widgets;
    }

    public static class DebugWidgetsBuilder {

        private Context context;

        private List<DebugWidget> widgets1;

        DebugWidgetsBuilder(Context context) {
            this.context = context;
            widgets1 = new ArrayList<>();
        }

        public DebugWidgetsBuilder addText(String title, int number) {
            return addText(title, String.valueOf(number));
        }

        public DebugWidgetsBuilder addText(String title, boolean b) {
            return addText(title, String.valueOf(b));
        }

        public DebugWidgetsBuilder addText(String title, String summary) {
            TextView textView = new TextView(new ContextThemeWrapper(context, R.style.Widget_DebugDrawer_Base_RowValue));
            textView.setText(" " + summary);
            return returnBuilder(title, textView);
        }

        public DebugWidgetsBuilder addButton(String text, View.OnClickListener listener) {
            Button button = new Button(new ContextThemeWrapper(context, R.style.Widget_DebugDrawer_Base_RowWidget));
            button.setText(text);
            button.setOnClickListener(listener);
            return returnBuilder(null, button);
        }

        public DebugWidgetsBuilder addIconButton(String text, @DrawableRes int drawableId, View.OnClickListener listener) {
            View view = LayoutInflater.from(context).inflate(R.layout.dd_icon_button, null);
            ((ImageView) view.findViewById(R.id.icon_iv)).setImageResource(drawableId);
            Button button = (Button) view.findViewById(R.id.summary_btn);
            button.setText(text);
            button.setOnClickListener(listener);
            return returnBuilder(null, view);
        }

        public DebugWidgetsBuilder addSwitch(String title, boolean checked,
                CompoundButton.OnCheckedChangeListener listener) {
            Switch sw = new Switch(new ContextThemeWrapper(context, R.style.Widget_DebugDrawer_Base_RowWidget));
            sw.setChecked(checked);
            sw.setOnCheckedChangeListener(listener);
            return returnBuilder(title, sw);
        }

        @NonNull
        private DebugWidgetsBuilder returnBuilder(String title, View view) {
            widgets1.add(new DebugWidget(title, view));
            return this;
        }

        public DebugWidgets build() {
            return new DebugWidgets(widgets1);
        }

        static class DebugWidget {

            String title;

            View view;

            DebugWidget(String title, View view) {
                this.title = title;
                this.view = view;
            }
        }
    }
}
