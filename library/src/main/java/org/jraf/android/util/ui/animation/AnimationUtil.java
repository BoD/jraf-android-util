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

package org.jraf.android.util.ui.animation;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;

public class AnimationUtil {
    public static void animateVisible(View v) {
        if (v.getVisibility() == View.VISIBLE) return;
        v.setVisibility(View.VISIBLE);
        ViewCompat.setAlpha(v, 0F);
        ViewCompat.animate(v).alpha(1F);
    }

    public static void animateInvisible(View v) {
        animate(v, View.INVISIBLE);
    }

    public static void animateGone(View v) {
        animate(v, View.GONE);
    }


    private static void animate(View v, final int visibility) {
        if (v.getVisibility() == visibility) return;
        final ViewPropertyAnimatorCompat animator = ViewCompat.animate(v);
        animator.alpha(0F).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {}

            @Override
            public void onAnimationCancel(View view) {}

            @Override
            public void onAnimationEnd(View view) {
                view.setVisibility(visibility);

                // Remove this listener
                animator.setListener(null);

                // Restore normal alpha
                ViewCompat.setAlpha(view, 1F);
            }
        });
    }
}
