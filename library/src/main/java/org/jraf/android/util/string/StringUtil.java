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
package org.jraf.android.util.string;

import android.content.Intent;
import android.os.Bundle;

public class StringUtil {
    /**
     * Returns a String representation of the bundle, or {@code "null"}.
     */
    public static String toString(Bundle bundle) {
        if (bundle == null) return "null";
        bundle.size(); // This call unparcels the data
        return bundle.toString();
    }

    /**
     * Returns a String representation of the intent, or {@code "null"}.
     */
    public static String toString(Intent intent) {
        if (intent == null) return "null";
        return intent.toString() + ", extras=" + toString(intent.getExtras());
    }
}
