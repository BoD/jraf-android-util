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

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class Task<T extends FragmentActivity> {
    private Fragment mFragment;
    private String mToastOk;
    private String mToastFail;
    private int mToastOkResId;
    private int mToastFailResId;

    protected void onPreExecute() {}

    protected abstract void doInBackground() throws Throwable;

    protected void onPostExecuteOk() {
        if (mToastOkResId != 0) mToastOk = getActivity().getString(mToastOkResId);
        if (mToastOk != null) Toast.makeText(getActivity(), mToastOk, Toast.LENGTH_LONG).show();
    }

    protected void onPostExecuteFail() {
        if (mToastFailResId != 0) mToastFail = getActivity().getString(mToastFailResId);
        if (mToastFail != null) Toast.makeText(getActivity(), mToastFail, Toast.LENGTH_LONG).show();
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    @SuppressWarnings("unchecked")
    public T getActivity() {
        return (T) mFragment.getActivity();
    }

    public Task<?> toastOk(String toastOk) {
        mToastOk = toastOk;
        return this;
    }

    public Task<?> toastOk(int toastOk) {
        mToastOkResId = toastOk;
        return this;
    }

    public Task<?> toastFail(String toastFail) {
        mToastFail = toastFail;
        return this;
    }

    public Task<?> toastFail(int toastFail) {
        mToastFailResId = toastFail;
        return this;
    }
}