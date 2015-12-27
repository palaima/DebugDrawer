package io.palaima.debugdrawer.timber.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import io.palaima.debugdrawer.timber.R;
import io.palaima.debugdrawer.timber.model.LogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogAdapter extends BaseAdapter {
    private List<LogEntry> mLogEntries = Collections.emptyList();

    @Override
    public int getCount() {
        return mLogEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return mLogEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dd_item_log_entry, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LogEntry logEntry = mLogEntries.get(position);
        viewHolder.fillData(logEntry);

        return convertView;
    }

    public void setLogs(List<LogEntry> logs) {
        mLogEntries = new ArrayList<>(logs);
    }

    public void addLog(LogEntry logEntry) {
        mLogEntries.add(logEntry);

        notifyDataSetChanged();
    }

    static class ViewHolder {

        View mRootView;
        TextView mLogLevelTextView;
        TextView mLogTagTextView;
        TextView mLogMessageTextView;

        ViewHolder(View view) {
            mRootView = view;
            mLogLevelTextView = (TextView) view.findViewById(R.id.dd_text_log_level);
            mLogTagTextView = (TextView) view.findViewById(R.id.dd_text_log_tag);
            mLogMessageTextView = (TextView) view.findViewById(R.id.dd_text_log_message);
        }

        void fillData(LogEntry entry) {
            mRootView.setBackgroundResource(backgroundForLevel(entry.getLevel()));
            mLogLevelTextView.setText(entry.displayLevel());
            mLogTagTextView.setText(String.format("%s %s", entry.getTimeStamp(), entry.getTag()));
            mLogMessageTextView.setText(entry.getMessage());
        }
    }

    public static int backgroundForLevel(int level) {
        switch (level) {
            case Log.VERBOSE:
            case Log.DEBUG:
                return R.color.dd_debug_log_accent_debug;
            case Log.INFO:
                return R.color.dd_debug_log_accent_info;
            case Log.WARN:
                return R.color.dd_debug_log_accent_warn;
            case Log.ERROR:
            case Log.ASSERT:
                return R.color.dd_debug_log_accent_error;
            default:
                return R.color.dd_debug_log_accent_unknown;
        }
    }
}
