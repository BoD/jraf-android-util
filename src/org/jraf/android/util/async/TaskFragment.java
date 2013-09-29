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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.jraf.android.util.Constants;
import org.jraf.android.util.dialog.ProgressDialogFragment;

/**
 * A non UI {@link Fragment} that executes a task in the background.<br/>
 * A {@link ProgressDialogFragment} is shown while the task is running for at least a few milliseconds.
 */
@SuppressLint("ValidFragment")
public class TaskFragment extends Fragment {
    private static final String TAG = Constants.TAG + TaskFragment.class.getSimpleName();
    private static final String FRAGMENT_TAG_PREFIX = TaskFragment.class.getName() + ".FRAGMENT_TAG.";

    private static final int DELAY_SHOW_PROGRESS_DIALOG = 250; // ms

    private static int sCounter = 0;

    private Task<?> mTask;
    private boolean mTaskStarted;
    private volatile boolean mTaskFinished;
    private Exception mCallerStackTrace;

    public TaskFragment() {}

    public TaskFragment(Task<?> task) {
        mTask = task;
        mTask.setFragment(this);
        mCallerStackTrace = new Exception();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mTaskStarted && mTask != null) {
            mTaskStarted = true;
            startTask();
        }
    }

    private void startTask() {
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            public void onPreExecute() {
                mTask.onPreExecute();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This will happen after a small delay, so we must check that the task hasn't already finished,
                        // and that the fragment is still added.
                        Log.d(TAG, "onPreExecute mTaskFinished=" + mTaskFinished);
                        if (!mTaskFinished && isResumed()) {
                            ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
                            progressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.FRAGMENT_TAG);
                        }
                    }
                }, DELAY_SHOW_PROGRESS_DIALOG);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    mTask.doInBackground();
                } catch (Throwable t) {
                    Log.w(TAG, "doInBackground", t);
                    Log.w(TAG, "Caller stack trace: ", mCallerStackTrace);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean ok) {
                Log.d(TAG, "onPostExecute ok=" + ok);
                mTaskFinished = true;
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    fragmentManager.executePendingTransactions();
                    DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag(ProgressDialogFragment.FRAGMENT_TAG);
                    if (dialogFragment != null) dialogFragment.dismissAllowingStateLoss();
                }
                if (isAdded()) {
                    if (ok) {
                        mTask.onPostExecuteOk();
                    } else {
                        mTask.onPostExecuteFail();
                    }
                }
                if (fragmentManager != null) fragmentManager.beginTransaction().remove(TaskFragment.this).commitAllowingStateLoss();
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            executeHoneycomb(asyncTask);
        } else {
            asyncTask.execute();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void executeHoneycomb(AsyncTask<Void, Void, Boolean> asyncTask) {
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void execute(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().add(this, getUniqueFragmentTag()).commitAllowingStateLoss();
    }

    private String getUniqueFragmentTag() {
        return FRAGMENT_TAG_PREFIX + System.currentTimeMillis() + "." + sCounter++;
    }
}
