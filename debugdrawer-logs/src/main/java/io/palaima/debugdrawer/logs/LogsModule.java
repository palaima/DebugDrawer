/*
 * Copyright (C) 2015 Mantas Palaima
 * Copyright (C) 2016 Oleg Godovykh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.palaima.debugdrawer.logs;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pedrovgs.lynx.LynxActivity;
import com.github.pedrovgs.lynx.LynxConfig;
import com.readystatesoftware.chuck.Chuck;
import com.readystatesoftware.chuck.ChuckInterceptor;

import io.palaima.debugdrawer.base.DebugModuleAdapter;
import okhttp3.Interceptor;

public class LogsModule extends DebugModuleAdapter {

    private static final boolean HAS_LYNX;
    private static final boolean HAS_CHUCK;

    static {
        boolean hasDependencyLynx;
        boolean hasDependencyChuck;

        try {
            Class.forName("com.github.pedrovgs.lynx.LynxActivity");
            hasDependencyLynx = true;
        } catch (ClassNotFoundException e) {
            hasDependencyLynx = false;
        }

        try {
            Class.forName("com.readystatesoftware.chuck.Chuck");
            hasDependencyChuck = true;
        } catch (ClassNotFoundException e) {
            hasDependencyChuck = false;
        }

        HAS_LYNX = hasDependencyLynx;
        HAS_CHUCK = hasDependencyChuck;
    }

    private final LynxConfig lynxConfig;

    public LogsModule() {
        this(null);
    }

    public LogsModule(@Nullable LynxConfig lynxConfig) {
        this.lynxConfig = lynxConfig;
    }

    public static Interceptor chuckInterceptor(Context context) {
        if (!HAS_CHUCK) {
            throw new RuntimeException("Chuck dependency is not found");
        }
        return new ChuckInterceptor(context).showNotification(false);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_module_logs, parent, false);
        view.findViewById(R.id.dd_module_logs_logcat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HAS_LYNX) {
                    throw new RuntimeException("Lynx dependency is not found");
                } else {
                    if (lynxConfig != null) {
                        view.getContext().startActivity(LynxActivity.getIntent(view.getContext(), lynxConfig));
                    } else {
                        view.getContext().startActivity(LynxActivity.getIntent(view.getContext()));
                    }
                }
            }
        });

        view.findViewById(R.id.dd_module_logs_chuck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HAS_CHUCK) {
                    throw new RuntimeException("Chuck dependency is not found");
                } else {
                    view.getContext().startActivity(Chuck.getLaunchIntent(view.getContext()));
                }
            }
        });

        return view;
    }
}
