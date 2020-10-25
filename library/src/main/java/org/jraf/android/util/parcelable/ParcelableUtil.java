/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2015 Benoit 'BoD' Lubek (BoD@JRAF.org)
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
package org.jraf.android.util.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ParcelableUtil {
    /**
     * Parcel a parcelable to a byte array.
     *
     * @param parcelable The object to parcel.
     * @return A byte array representing the given object, or {@code null} if {@code parcelable} was {@code null}.
     */
    @Nullable
    public static byte[] parcel(@Nullable Parcelable parcelable) {
        if (parcelable == null) return null;
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    /**
     * Reconstruct an object from a parcelled byte array.
     *
     * @param bytes The parcelled byte array.
     * @param creator The Creator of the class of the object to construct.
     * @return The resulting reconstructed object, or {@code null} if {@code bytes} was {@code null}.
     */
    @Nullable
    public static <T> T unparcel(@Nullable byte[] bytes, @NonNull Parcelable.Creator<T> creator) {
        if (bytes == null) return null;
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return creator.createFromParcel(parcel);
    }
}
