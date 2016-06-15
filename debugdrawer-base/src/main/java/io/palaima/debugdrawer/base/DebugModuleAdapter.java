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

package io.palaima.debugdrawer.base;

public abstract class DebugModuleAdapter implements DebugModule {

    @Override
    public void onOpened() {
        // do nothing
    }

    @Override
    public void onClosed() {
        // do nothing
    }

    @Override
    public void onResume() {
        // do nothing
    }

    @Override
    public void onPause() {
        // do nothing
    }

    @Override
    public void onStart() {
        // do nothing
    }

    @Override
    public void onStop() {
        // do nothing
    }
}
