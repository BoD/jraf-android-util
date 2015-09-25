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
package org.jraf.android.util.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializableUtil {
    /**
     * Serialize an object into an OutputStream.<br/>
     * Note: the given OutputStream will not be closed by this method.
     *
     * @throws RuntimeException In case of a problem while serializing or writing.
     */
    public static void serialize(Serializable obj, OutputStream outputStream) {
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialize an object into a byte array.
     *
     * @throws RuntimeException In case of a problem while serializing.
     */
    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        serialize(obj, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * Deserialize an object by reading an InputStream.<br/>
     * Note: the given InputStream will not be closed by this method.
     *
     * @return The resulting object.
     * @throws RuntimeException In case of a problem while deserializing or reading.
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(InputStream inputStream) {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserialize an object from a byte array.
     *
     * @throws RuntimeException In case of a problem while deserializing.
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] objectData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectData);
        return (T) deserialize(byteArrayInputStream);
    }
}
