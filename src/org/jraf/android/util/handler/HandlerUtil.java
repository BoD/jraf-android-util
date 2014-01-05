/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2014 Benoit 'BoD' Lubek (BoD@JRAF.org)
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
package org.jraf.android.util.handler;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {
    private static Handler sMainHandler;
    private static Thread sUiThread;

    /**
     * Returns the main handler, which lives in the main thread of the application.
     */
    public static Handler getMainHandler() {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
        return sMainHandler;
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     * 
     * @param action the action to run on the UI thread
     */
    public static void runOnUiThread(Runnable action) {
        if (sUiThread == null) {
            sUiThread = Looper.getMainLooper().getThread();
        }
        if (Thread.currentThread() != sUiThread) {
            getMainHandler().post(action);
        } else {
            action.run();
        }
    }
}
