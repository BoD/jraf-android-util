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
package org.jraf.android.util.math;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Helper to calculate the moving average of an array.
 */
public class MovingAverage {
    private Queue<Float> mWindow = new LinkedList<Float>();
    private int mPeriod;
    private Float mSum = 0f;

    public MovingAverage(int period) {
        mPeriod = period;
    }

    public void add(Float value) {
        mSum = mSum + value;
        mWindow.add(value);
        if (mWindow.size() > mPeriod) {
            mSum = mSum - mWindow.remove();
        }
    }

    public Float getAverage() {
        if (mWindow.isEmpty()) {
            // Technically the average is undefined
            return 0f;
        }
        return mSum / mWindow.size();
    }
}
