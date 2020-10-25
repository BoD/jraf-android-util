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
package org.jraf.android.util.app.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class BaseFrameworkFragment<C> extends Fragment {
    private C mCallbacks;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null) {
            // This Fragment is nested in another Fragment
            mCallbacks = (C) getParentFragment();
        } else {
            // This Fragment is attached to an Activity
            mCallbacks = (C) context;
        }
    }

    @Override
    public void onDetach() {
        mCallbacks = null;
        super.onDetach();
    }

    public C getCallbacks() {
        return mCallbacks;
    }
}