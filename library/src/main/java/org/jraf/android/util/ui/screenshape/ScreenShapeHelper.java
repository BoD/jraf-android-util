/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2017 Benoit 'BoD' Lubek (BoD@JRAF.org)
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
package org.jraf.android.util.ui.screenshape;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ScreenShapeHelper {
    private static ScreenShapeHelper INSTANCE = new ScreenShapeHelper();
    private boolean isInitialized;

    /**
     * Width of the display in pixels.
     */
    public int width;

    /**
     * Height of the display in pixels. This <strong>doesn't</strong> include the chin, if any.
     */
    public int height;

    /**
     * Height of the chin, if any.
     */
    public int chinHeight;

    /**
     * Whether the display is round.
     */
    public boolean isRound;

    /**
     * A margin in pixels that allows content to be safely displayed on round displays.
     */
    public float safeMargin;

    private ScreenShapeHelper() {}

    public static ScreenShapeHelper get(Context context) {
        if (!INSTANCE.isInitialized) INSTANCE.init(context);
        return INSTANCE;
    }

    public void init(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        isRound = resources.getConfiguration().isScreenRound();
        if (isRound) {
            // Assume width=height for round screens (I guess that means oval is not supported!)
            chinHeight = width - height;

            // Pythagorean Theorem
            double edge = width / Math.sqrt(2);
            safeMargin = (float) ((width - edge) / 2.0);
        }
        isInitialized = true;
    }

    /**
     * Calculate the margin in pixels that allows a rectangular content to be safely displayed on round displays.<br/>
     * The ratio parameter is the width to height ratio of the rectangle to fit (must be between 0 and 1 - the height is 1).
     * For any questions about this formula, please ask c@rmen.ca :)
     */
    public float getSafeMargin(float ratio) {
        return (float) ((width / 2) * (1.0 - (1.0 / Math.sqrt(1 + ratio * ratio))));
    }
}
