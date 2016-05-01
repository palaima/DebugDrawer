package io.palaima.debugdrawer.app.model;

import android.util.Log;

public class LogEntry {
    private final int    level;
    private final String tag;
    private final String message;
    private final String timeStamp;

    public LogEntry(int level, String tag, String message, String timeStamp) {
        this.level = level;
        this.tag = tag;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getLevel() {
        return level;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String prettyPrint() {
        return String.format("%18s %18s %s %s", timeStamp, tag, displayLevel(), message);
    }

    public String displayLevel() {
        switch (level) {
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
