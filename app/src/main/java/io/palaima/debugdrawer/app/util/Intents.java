package io.palaima.debugdrawer.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

public final class Intents {
    /**
     * Attempt to launch the supplied {@link Intent}. Queries on-device packages before launching and
     * will display a simple message if none are available to handle it.
     */
    public static boolean maybeStartActivity(Context context, Intent intent) {
        if (hasHandler(context, intent)) {
            context.startActivity(intent);
            return true;
        } else {
            Toast.makeText(context, "There are no applications available to handle this action",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Queries on-device packages for a handler for the supplied {@link Intent}.
     */
    public static boolean hasHandler(Context context, Intent intent) {
        List<ResolveInfo> handlers = context.getPackageManager().queryIntentActivities(intent, 0);
        return !handlers.isEmpty();
    }

    private Intents() {
    }
}
