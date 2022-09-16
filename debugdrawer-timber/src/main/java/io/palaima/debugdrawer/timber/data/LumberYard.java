package io.palaima.debugdrawer.timber.data;

import android.content.Context;
import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

import io.palaima.debugdrawer.timber.model.LogEntry;
import timber.log.Timber;

public class LumberYard {

    private static final boolean HAS_TIMBER;

    static {
        boolean hasDependency;

        try {
            Class.forName("timber.log.Timber");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_TIMBER = hasDependency;
    }

    private static final int BUFFER_SIZE = 200;

    private static final DateFormat FILENAME_DATE = new SimpleDateFormat("yyyy-MM-dd HHmm a", Locale.US);
    private static final DateFormat LOG_DATE_PATTERN = new SimpleDateFormat("MM-dd HH:mm:ss.S", Locale.US);

    private static final String LOG_FILE_END = ".log";

    private static LumberYard sInstance;

    private final Context context;

    private final Deque<LogEntry> entries = new ArrayDeque<>(BUFFER_SIZE + 1);

    private OnLogListener onLogListener;

    public LumberYard(@NonNull Context context) {
        if (!HAS_TIMBER) {
            throw new RuntimeException("Timber dependency is not found");
        }
        this.context = context.getApplicationContext();
    }

    public static LumberYard getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LumberYard(context);
        }

        return sInstance;
    }

    public Timber.Tree tree() {
        return new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                addEntry(new LogEntry(priority, tag, message, LOG_DATE_PATTERN.format(Calendar.getInstance().getTime())));
            }
        };
    }

    public void setOnLogListener(OnLogListener onLogListener) {
        this.onLogListener = onLogListener;
    }

    private synchronized void addEntry(LogEntry entry) {
        entries.addLast(entry);

        if (entries.size() > BUFFER_SIZE) {
            entries.removeFirst();
        }

        onLog(entry);
    }

    public List<LogEntry> bufferedLogs() {
        return new ArrayList<>(entries);
    }

    /**
     * Save the current logs to disk.
     */
    public void save(OnSaveLogListener listener) {
        File dir = getLogDir();

        if (dir == null) {
            listener.onError("Can't save logs. External storage is not mounted. " +
                "Check android.permission.WRITE_EXTERNAL_STORAGE permission");
            return;
        }

        FileWriter fileWriter = null;

        try {
            File output = new File(dir, getLogFileName());
            fileWriter = new FileWriter(output, true);

            List<LogEntry> entries = bufferedLogs();
            for (LogEntry entry : entries) {
                fileWriter.write(entry.prettyPrint() + "\n");
            }

            listener.onSave(output);

        } catch (IOException e) {
            listener.onError(e.getMessage());
            e.printStackTrace();

        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    listener.onError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void cleanUp() {
        File dir = getLogDir();
        if (dir != null) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(LOG_FILE_END)) {
                        file.delete();
                    }
                }
            }
        }
    }

    private File getLogDir() {
        return context.getExternalFilesDir(null);
    }

    private void onLog(LogEntry entry) {
        if (onLogListener != null) {
            onLogListener.onLog(entry);
        }
    }

    private String getLogFileName() {
        String pattern = "%s%s";
        String currentDate = FILENAME_DATE.format(Calendar.getInstance().getTime());

        return String.format(pattern, currentDate, LOG_FILE_END);
    }

    public interface OnSaveLogListener {
        void onSave(File file);

        void onError(String message);
    }

    public interface OnLogListener {
        void onLog(LogEntry logEntry);
    }
}
