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
package org.jraf.android.util.async;

import android.os.AsyncTask;
import android.util.Log;

import org.jraf.android.util.Constants;

/**
 * A simple extension of {@code AsyncTask<Void, Void, Boolean>} that logs the caller's stacktrace, making it easier to debug if an Exception occurs in
 * {@link #doInBackground(Void...)}.<br/>
 * To use this class, do not override {@link #doInBackground(Void...)} but instead override {@link #doInBackground()}.
 */
public abstract class SimpleAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = Constants.TAG + SimpleAsyncTask.class.getSimpleName();
    private final Exception mCallerStackTrace;

    public SimpleAsyncTask() {
        mCallerStackTrace = new Exception();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            doInBackground();
        } catch (Throwable t) {
            Log.w(TAG, "doInBackground", t);
            Log.w(TAG, "Caller stack trace: ", mCallerStackTrace);
            return false;
        }
        return true;
    }

    protected abstract void doInBackground() throws Throwable;
}
