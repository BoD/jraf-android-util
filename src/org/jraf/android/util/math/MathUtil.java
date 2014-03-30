/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2013-2014 Benoit 'BoD' Lubek (BoD@JRAF.org)
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
package org.jraf.android.util.math;

public class MathUtil {
    /**
     * Get the min and the max values of the given array.
     * 
     * @param values The array in which to look for the min and max values.
     * @return an array containing the min at index 0 and the max at index 1.
     */
    public static float[] getMinMax(float[] values) {
        if (values.length == 0) return new float[] { Float.MIN_VALUE, Float.MAX_VALUE };
        float min = values[0];
        float max = values[0];
        int len = values.length;
        for (int i = 1; i < len; i++) {
            float curVal = values[i];
            if (curVal < min) {
                min = curVal;
            }
            if (curVal > max) {
                max = curVal;
            }
        }
        return new float[] { min, max };
    }

    /**
     * Get the average of the given array.
     * 
     * @param values The array in which to calculate the average.
     * @return the average.
     */
    public static float getAverage(float[] values) {
        float sum = 0;
        for (float val : values) {
            sum += val;
        }
        return sum / values.length;
    }
}
