/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 * 
 * Copyright (C) 2013 Benoit 'BoD' Lubek (BoD@JRAF.org)
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
package org.jraf.android.util.log.wrapper;

import android.app.Application;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewParent;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jraf.android.util.handler.HandlerUtil;


/**
 * A wrapper around the standard Android Logcat facility that automatically uses the calling class name for the tag, and prefixes the messages with the calling
 * method name.
 */
public class Log {
    private static class CallerInfo {
        public String tag;
        public String method;
    }

    private static String sTagPrefix = "";
    private static boolean sEnabled = true;
    @Nullable
    private static TextView sLogTextView;

    /**
     * This can be called prior to using the other methods of this class, to specify a prefix to prepend to the tag.
     * Typically this should be called in {@link Application#onCreate()}.<br/>
     */
    public static void init(String tagPrefix) {
        sTagPrefix = tagPrefix;
        if (!sTagPrefix.endsWith("/")) sTagPrefix += "/";
    }

    /**
     * Call this to also log to a TextView in addition to normal Android logging.
     * This can be useful during early stages of a project.
     * Pass {@code null} to stop logging to a TextView.
     *
     * @param textView The TextView to log to, or {@code null}.
     */
    public static void setLogTextView(@Nullable TextView textView) {
        sLogTextView = textView;
    }

    /**
     * Enable or disable logging.
     */
    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
    }

    public static void d() {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.d(callerInfo.tag, callerInfo.method);
        if (sLogTextView != null) logToTextView("D " + formatMessage(callerInfo.method, null, null) + "\n");
    }

    public static void d(String msg) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.d(callerInfo.tag, callerInfo.method + " " + msg);
        if (sLogTextView != null) logToTextView("D " + formatMessage(callerInfo.method, msg, null) + "\n");
    }

    public static void w(String msg) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.w(callerInfo.tag, callerInfo.method + " " + msg);
        if (sLogTextView != null) logToTextView("W " + formatMessage(callerInfo.method, msg, null) + "\n");
    }

    public static void w(String msg, Throwable t) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.w(callerInfo.tag, callerInfo.method + " " + msg, t);
        if (sLogTextView != null) logToTextView("W " + formatMessage(callerInfo.method, msg, t) + "\n");
    }

    public static void e(String msg, Throwable t) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.e(callerInfo.tag, callerInfo.method + " " + msg, t);
        if (sLogTextView != null) logToTextView("E " + formatMessage(callerInfo.method, msg, t) + "\n");
    }

    private static void logToTextView(final String msg) {
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sLogTextView.append(msg);
                ViewParent parent = sLogTextView.getParent();
                if (parent instanceof ScrollView) {
                    ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }

    private static String formatMessage(String method, @Nullable String msg, @Nullable Throwable t) {
        StringBuilder sb = new StringBuilder(method);
        if (msg != null) {
            sb.append(" ");
            sb.append(msg);
        }
        if (t != null) {
            sb.append("\n");
            sb.append(android.util.Log.getStackTraceString(t));
        }
        return sb.toString();
    }

    private static CallerInfo getCallerInfo() {
        CallerInfo res = new CallerInfo();
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        res.tag = element.getClassName();
        res.tag = res.tag.substring(res.tag.lastIndexOf('.') + 1);
        res.tag = sTagPrefix + res.tag;
        res.method = element.getMethodName();
        return res;
    }
}
