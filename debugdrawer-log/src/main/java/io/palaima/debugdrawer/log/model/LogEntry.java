package io.palaima.debugdrawer.log.model;

import android.util.Log;

public class LogEntry {
    private final int mLevel;
    private final String mTag;
    private final String mMessage;
    private final String mTimeStamp;

    public LogEntry(int level, String tag, String message, String timeStamp) {
        mLevel = level;
        mTag = tag;
        mMessage = message;
        mTimeStamp = timeStamp;
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

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String prettyPrint() {
        return String.format("%18s %18s %s %s", mTimeStamp, mTag, displayLevel(), mMessage);
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
