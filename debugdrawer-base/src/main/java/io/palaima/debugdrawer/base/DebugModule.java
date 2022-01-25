/*
 * Copyright (C) 2015 Mantas Palaima
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

package io.palaima.debugdrawer.base;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface DebugModule {

    /**
     * Creates module view
     */
    @NonNull
    View onCreateView(@NonNull final LayoutInflater inflater, @NonNull final ViewGroup parent);

    /**
     * Override this method if you need to refresh
     * some information  when drawer is opened
     */
    void onOpened();

    /**
     * Override this method if you need to stop
     * some actions  when drawer is closed
     */
    void onClosed();

    /**
     * Override this method if you need to start
     * some processes
     */
    void onResume();

    /**
     * Override this method if you need to do
     * some clean up
     */
    void onPause();

    /**
     * Override this method if you need to start
     * some processes that would be killed when
     * onStop() is called
     * E.g. register receivers
     */
    void onStart();

    /**
     * Override this method if you need to do
     * some clean up when activity goes to foreground.
     * E.g. unregister receivers
     */
    void onStop();
}
