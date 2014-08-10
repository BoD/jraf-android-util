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
package org.jraf.android.util.object;

import org.jraf.android.util.Constants;

public class ObjectUtil {
    private static final String TAG = Constants.TAG + ObjectUtil.class.getSimpleName();

    /**
     * Returns {@code true} if the arguments are equal to each other
     * and {@code false} otherwise.<br/>
     * Consequently, if both arguments are {@code null}, {@code true} is returned and if exactly one argument is {@code null}, {@code false} is returned.
     * Otherwise, equality is determined by using
     * the {@link Object#equals equals} method of the first
     * argument.
     * 
     * @param a an object
     * @param b an object to be compared with {@code a} for equality
     * @return {@code true} if the arguments are equal to each other
     *         and {@code false} otherwise
     */
    public static boolean equals(Object a, Object b) {
        if (a == b) return true;
        if (a == null) return b == null;
        return a.equals(b);
    }
}