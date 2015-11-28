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
package org.jraf.android.util.log;


import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.WorkerThread;

import org.jraf.android.util.log.timber.FileTree;
import org.jraf.android.util.log.timber.LogcatTree;

import timber.log.Timber;

public class Log {
    private static final int MAX_LOG_SIZE_BYTES = 2 * 1024 * 1024; // 2 Megs
    private static FileTree sFileTree;

    public static void init(Context context, String applicationTag) {
        // Log to Android logcat
        Timber.plant(new LogcatTree(applicationTag));

        // Log to a file
        sFileTree = new FileTree(context, applicationTag, MAX_LOG_SIZE_BYTES);
        Timber.plant(sFileTree);
    }

    @WorkerThread
    public static File getLogFile() {
        sFileTree.prepareLogFile();
        return sFileTree.getFile();
    }

    /**
     * Log a verbose message with optional format args.
     */
    public static void v(String message, Object... args) {
        Timber.v(message, args);
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    public static void v(Throwable t, String message, Object... args) {
        Timber.v(t, message, args);
    }

    /**
     * Log a debug message with optional format args.
     */
    public static void d(String message, Object... args) {
        Timber.d(message, args);
    }

    public static void d() {
        Timber.d(" ");
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    public static void d(Throwable t, String message, Object... args) {
        Timber.d(t, message, args);
    }

    /**
     * Log an info message with optional format args.
     */
    public static void i(String message, Object... args) {
        Timber.i(message, args);
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    public static void i(Throwable t, String message, Object... args) {
        Timber.i(t, message, args);
    }

    /**
     * Log a warning message with optional format args.
     */
    public static void w(String message, Object... args) {
        Timber.w(message, args);
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    public static void w(Throwable t, String message, Object... args) {
        Timber.w(t, message, args);
    }

    /**
     * Log an error message with optional format args.
     */
    public static void e(String message, Object... args) {
        Timber.e(message, args);
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    public static void e(Throwable t, String message, Object... args) {
        Timber.e(t, message, args);
    }

    /**
     * Log an assert message with optional format args.
     */
    public static void wtf(String message, Object... args) {
        Timber.wtf(message, args);
    }

    /**
     * Log an assert exception and a message with optional format args.
     */
    public static void wtf(Throwable t, String message, Object... args) {
        Timber.wtf(t, message, args);
    }

    /**
     * Set a one-time tag for use on the next logging call.
     */
    public static void tag(String tag) {
        Timber.tag(tag);
    }

    @WorkerThread
    public static void sendAppLogsByMail(Context context, String emailAddress) {
        File logFile;
        try {
            logFile = getLogFile();
        } catch (Exception e) {
            Log.e(e, "sendLogcatByMail");
            return;
        }

        sendByEmail(context, logFile, emailAddress);
    }

    private static void sendByEmail(Context context, File logFile, String emailAddress) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Logs");
        intent.putExtra(Intent.EXTRA_TEXT, "See logs in attachment.");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(logFile));
        intent.setType("message/rfc882");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
