/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2015 Benoit 'BoD' Lubek (BoD@JRAF.org)
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

package org.jraf.android.util.log.timber;

public class LogcatTree extends TagAndMethodNameTree {
    private static final int MAX_LOG_LENGTH = 4000;

    public LogcatTree(String applicationTag) {
        super(applicationTag);
    }

    @Override
    protected void doLog(int priority, String tag, String methodName, String message, Throwable t) {
        message = methodName + " " + message;
        if (message.length() < MAX_LOG_LENGTH) {
            if (priority == android.util.Log.ASSERT) {
                android.util.Log.wtf(tag, message);
            } else {
                android.util.Log.println(priority, tag, message);
            }
            return;
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + MAX_LOG_LENGTH);
                String part = message.substring(i, end);
                if (priority == android.util.Log.ASSERT) {
                    android.util.Log.wtf(tag, part);
                } else {
                    android.util.Log.println(priority, tag, part);
                }
                i = end;
            } while (i < newline);
        }
    }
}
