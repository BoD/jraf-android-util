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
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import org.jraf.android.util.dialog.ProgressDialogFragment;

/**
 * A {@link SimpleAsyncTaskFragment} that shows a {@link ProgressDialogFragment} while the task is running.<br/>
 * Do not override {@link #onPostExecute(boolean)}, but instead {@link #onPostExecuteOk()} and {@link #onPostExecuteFail()} (or if you do, you should call
 * {@code super(ok)}).
 */
public abstract class ProgressDialogAsyncTaskFragment extends SimpleAsyncTaskFragment {
    private static final int DELAY_SHOW_PROGRESS_DIALOG = 250; // ms

    private volatile boolean mTaskFinished;
    private String mToastOk;
    private String mToastFail;
    private int mToastOkResId;
    private int mToastFailResId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (mToastOkResId != 0) mToastOk = getString(mToastOkResId);
        if (mToastFailResId != 0) mToastFail = getString(mToastFailResId);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onPreExecute() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mTaskFinished) {
                    ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
                    progressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.FRAGMENT_TAG);
                }
            }
        }, DELAY_SHOW_PROGRESS_DIALOG);
    }

    @Override
    protected void onPostExecute(boolean ok) {
        mTaskFinished = true;
        DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag(ProgressDialogFragment.FRAGMENT_TAG);
        if (dialogFragment != null) dialogFragment.dismissAllowingStateLoss();

        if (!ok) {
            onPostExecuteFail();
            return;
        }
        onPostExecuteOk();
    }

    protected void onPostExecuteOk() {
        if (mToastOk != null) Toast.makeText(getActivity(), mToastOk, Toast.LENGTH_LONG).show();
    }

    protected void onPostExecuteFail() {
        if (mToastFail != null) Toast.makeText(getActivity(), mToastFail, Toast.LENGTH_LONG).show();
    }

    public ProgressDialogAsyncTaskFragment toastOk(String toastOk) {
        mToastOk = toastOk;
        return this;
    }

    public ProgressDialogAsyncTaskFragment toastOk(int toastOk) {
        mToastOkResId = toastOk;
        return this;
    }

    public ProgressDialogAsyncTaskFragment toastFail(String toastFail) {
        mToastFail = toastFail;
        return this;
    }

    public ProgressDialogAsyncTaskFragment toastFail(int toastFail) {
        mToastFailResId = toastFail;
        return this;
    }
}
