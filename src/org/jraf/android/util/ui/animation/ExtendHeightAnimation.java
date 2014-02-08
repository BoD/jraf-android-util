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
package org.jraf.android.util.ui.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation to extend/reduce the height of a view.
 */
public class ExtendHeightAnimation extends Animation {
    private final View mView;
    private final int mTargetHeight;
    private final boolean mExtend;

    public ExtendHeightAnimation(View view, int targetHeight, boolean extend) {
        mView = view;
        mTargetHeight = targetHeight;
        mExtend = extend;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (mExtend) {
            newHeight = (int) (mTargetHeight * interpolatedTime);
        } else {
            newHeight = (int) (mTargetHeight * (1 - interpolatedTime));
        }
        mView.getLayoutParams().height = newHeight;
        mView.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
