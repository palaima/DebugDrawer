package io.palaima.debugdrawer.log.model;

import android.util.Log;

public class LogEntry {
    public final int mLevel;
    public final String mTag;
    public final String mMessage;

    public LogEntry(int level, String tag, String message) {
        this.mLevel = level;
        this.mTag = tag;
        this.mMessage = message;
    }

    public int getLevel() {
        return mLevel;
    }

    public String getTag() {
        return mTag;
    }

    public String getMessage() {
        return mMessage;
    }

    public String prettyPrint() {
        return String.format("%22s %s %s", mTag, displayLevel(), mMessage);
    }

    public String displayLevel() {
        switch (mLevel) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
            default:
                return "?";
        }
    }

}
