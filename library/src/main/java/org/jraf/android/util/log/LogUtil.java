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
package org.jraf.android.util.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LogUtil {
    private static final String UNKNOWN = "(unknown value %s)";

    /**
     * Use reflection to return the name of the constant in the class {@code clazz} that corresponds to the given value.
     * 
     * @param clazz The class in which to search for the constant.
     * @param value The value for which to return the constant name
     * @param prefix Optional prefix to use when searching for the constant. Pass {@code null} for no prefix.
     * @return The name of the matching constant, or the string {@code "(unknown value)"} if it was not found.
     */
    public static String getConstantName(Class<?> clazz, int value, String prefix) {
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            // Ignore non final fields
            if (!Modifier.isFinal(field.getModifiers())) continue;

            // Ignore fields not starting with prefix
            if (prefix != null && !field.getName().startsWith(prefix)) continue;

            // Ignore non int fields
            if (!field.getType().equals(Integer.TYPE)) continue;

            try {
                if (field.get(null).equals(value)) return field.getName();
            } catch (Exception e) {
                return String.format(UNKNOWN, value);
            }
        }
        return String.format(UNKNOWN, value);
    }

    /**
     * Use reflection to return the name of the constant in the class {@code clazz} that corresponds to the given value.
     * 
     * @param clazz The class in which to search for the constant.
     * @param value The value for which to return the constant name
     * @return The name of the matching constant, or the string {@code "(unknown value)"} if it was not found.
     */
    public static String getConstantName(Class<?> clazz, int value) {
        return getConstantName(clazz, value, null);
    }
}
