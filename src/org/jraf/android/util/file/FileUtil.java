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
package org.jraf.android.util.file;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.jraf.android.util.Constants;

public class FileUtil {
    private static final String TAG = Constants.TAG + FileUtil.class.getSimpleName();

    /**
     * Creates an empty temporary file using the given base name and suffix as part of the file name.<br/>
     * If {@code suffix} is {@code null}, {@code ".tmp"} is used.
     * 
     * @param baseName the base name to use (must be at least 3 characters long).
     * @param suffix the suffix to use (can be {@code null}).
     * @return an empty temporary file.
     * @throws RuntimeException if the file could not be created.
     * @throws IllegalArgumentException if the length of {@code baseName} is less than 3.
     */
    public static File newTemporaryFile(Context context, String baseName, String suffix) {
        // API Level 7 Equivalent of context.getExternalCacheDir()
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File cacheDir = new File(externalStorageDirectory, "Android/data/" + context.getPackageName() + "/cache");
        cacheDir.mkdirs();
        File res = new File(cacheDir, baseName + (suffix == null ? ".tmp" : suffix));
        if (res.exists()) res.delete();
        try {
            res.createNewFile();
            // This may very well be useless
            res.deleteOnExit();
            Log.d(TAG, "newTemporaryFile res=" + res);
            return res;
        } catch (IOException e) {
            Log.w(TAG, "Could not create a temporary file at " + res, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an empty temporary file using a unique id as the base name and the given suffix as part of the file name.<br/>
     * If {@code suffix} is {@code null}, {@code ".tmp"} is used.
     * 
     * @param suffix the suffix to use (can be {@code null}).
     * @return an empty temporary file.
     * @throws RuntimeException if the file could not be created.
     */
    public static File newTemporaryFile(Context context, String suffix) {
        return newTemporaryFile(context, UUID.randomUUID().toString(), suffix);
    }
}
