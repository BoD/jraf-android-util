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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * A non UI fragment that starts a {@link SimpleAsyncTask}.
 */
public abstract class SimpleAsyncTaskFragment extends Fragment {
    private static final String PREFIX = SimpleAsyncTaskFragment.class.getName() + ".";
    public static final String FRAGMENT_TAG = PREFIX + "FRAGMENT_TAG";

    private boolean mHasStarted;

    public SimpleAsyncTaskFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mHasStarted) {
            mHasStarted = true;
            startAsyncTask();
        }
    }

    private void startAsyncTask() {
        new SimpleAsyncTask() {
            @Override
            protected void onPreExecute() {
                SimpleAsyncTaskFragment.this.onPreExecute();
            }

            @Override
            protected void doInBackground() throws Throwable {
                SimpleAsyncTaskFragment.this.doInBackground();
            }

            @Override
            protected void onPostExecute(Boolean ok) {
                SimpleAsyncTaskFragment.this.onPostExecute(ok);
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager == null) return;
                fragmentManager.beginTransaction().remove(SimpleAsyncTaskFragment.this).commitAllowingStateLoss();
            }
        }.execute();
    }

    public SimpleAsyncTaskFragment execute(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().add(this, FRAGMENT_TAG).commitAllowingStateLoss();
        return this;
    }

    protected void onPreExecute() {}

    protected abstract void doInBackground() throws Throwable;

    protected void onPostExecute(boolean ok) {}
}
