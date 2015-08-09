package io.palaima.debugdrawer.log.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;
import android.widget.Toast;
import io.palaima.debugdrawer.log.data.LumberYard;
import io.palaima.debugdrawer.log.model.LogEntry;
import io.palaima.debugdrawer.log.util.Intents;

import java.io.File;

public class LogDialog extends AlertDialog {

    private final LogAdapter mAdapter;

    public LogDialog(Context context) {
        super(context);

        mAdapter = new LogAdapter();

        ListView listView = new ListView(context);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setAdapter(mAdapter);

        setTitle("Logs");
        setView(listView);
        setButton(BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* no-op */
            }
        });
        setButton(BUTTON_POSITIVE, "Share", new DialogInterface.OnClickListener() {
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

        mAdapter.setLogs(lumberYard.bufferedLogs());

        lumberYard.setOnLogListener(new LumberYard.OnLogListener() {
            @Override
            public void onLog(LogEntry logEntry) {
                mAdapter.addLog(logEntry);
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
