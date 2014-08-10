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
package org.jraf.android.util.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;

public class UiUtil {
    /**
     * Returns the location of the view on the screen. The screen includes the 'notification area' (aka 'status bar').
     */
    public static Rect getLocationInScreen(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        int width = v.getWidth();
        int height = v.getHeight();
        Rect rectPick = new Rect(x, y, x + width, y + height);
        return rectPick;
    }

    /**
     * Returns the location of the view on its window. The window does not include the 'notification area' (aka 'status bar').
     */
    public static Rect getLocationInWindow(View v) {
        // Height of status bar
        Rect rect = new Rect();
        ((Activity) v.getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        Rect res = getLocationInScreen(v);

        res.offset(0, -statusBarHeight);
        return res;
    }
}
