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
package org.jraf.android.util.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtil {
    /**
     * Converts an array of {@code int} into a {@link List} of {@link Integer}.
     * 
     * @param intArray The array to convert.
     * @return The newly created {@link List}.
     */
    public static List<Integer> asList(int[] intArray) {
        List<Integer> res = new ArrayList<Integer>(intArray.length);
        for (int i : intArray) {
            res.add(i);
        }
        return res;
    }

    /**
     * Returns a new {@link Map} where the values are sorted according to the given comparator (or using the natural order if the comparator is {@code null}).
     * 
     * @param map The original map of which to get a sorted version.
     * @param comparator The comparator to use, or {@code null} to use the natural order.
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> getMapSortedByValue(Map<K, V> map, final Comparator<V> comparator) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (comparator == null) {
                    return o1.getValue().compareTo(o2.getValue());
                }
                return comparator.compare(o1.getValue(), o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
