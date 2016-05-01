package io.palaima.debugdrawer.app.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import io.palaima.debugdrawer.app.data.LumberYard;
import io.palaima.debugdrawer.app.model.LogEntry;
import io.palaima.debugdrawer.app.util.Intents;

public class LogDialog extends AlertDialog {

    private final LogAdapter adapter;

    private final Handler handler = new Handler(Looper.getMainLooper());

    public LogDialog(Context context) {
        super(context);

        adapter = new LogAdapter();

        ListView listView = new ListView(context);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setAdapter(adapter);

        setTitle("Logs");
        setView(listView);
        setButton(BUTTON_NEGATIVE, "Close", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* no-op */
            }
        });
        setButton(BUTTON_POSITIVE, "Share", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                share();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        LumberYard lumberYard = LumberYard.getInstance(getContext());

        adapter.setLogs(lumberYard.bufferedLogs());

        lumberYard.setOnLogListener(new LumberYard.OnLogListener() {
            @Override
            public void onLog(LogEntry logEntry) {

                addLogEntry(logEntry);
            }
        });
    }

    private void addLogEntry(final LogEntry logEntry) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.addLog(logEntry);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        LumberYard.getInstance(getContext()).setOnLogListener(null);
    }

    private void share() {
        LumberYard.getInstance(getContext())
                .save(new LumberYard.OnSaveLogListener() {
                    @Override
                    public void onSave(File file) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        Intents.maybeStartActivity(getContext(), sendIntent);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
