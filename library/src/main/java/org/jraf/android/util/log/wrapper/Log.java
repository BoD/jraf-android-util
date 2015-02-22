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

    /**
     * This can be called prior to using the other methods of this class, to specify a prefix to prepend to the tag.
     * Typically this should be called in {@link Application#onCreate()}.<br/>
     */
    public static void init(String tagPrefix) {
        sTagPrefix = tagPrefix;
        if (!sTagPrefix.endsWith("/")) sTagPrefix += "/";
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
    }

    public static void d(String msg) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.d(callerInfo.tag, callerInfo.method + " " + msg);
    }

    public static void w(String msg) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.w(callerInfo.tag, callerInfo.method + " " + msg);
    }

    public static void w(String msg, Throwable t) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.w(callerInfo.tag, callerInfo.method + " " + msg, t);
    }

    public static void e(String msg, Throwable t) {
        if (!sEnabled) return;
        CallerInfo callerInfo = getCallerInfo();
        android.util.Log.e(callerInfo.tag, callerInfo.method + " " + msg, t);
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
