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
 * Copyright (C) 2013 Carmen Alvarez (c@rmen.ca)
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
package org.jraf.android.util.bitmap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;

import org.jraf.android.util.Constants;

public class BitmapUtil {
    private static final String TAG = Constants.TAG + BitmapUtil.class.getSimpleName();

    /**
     * Call {@link BitmapFactory#decodeFile(String, android.graphics.BitmapFactory.Options)}, retrying up to 4 times with an increased
     * {@link android.graphics.BitmapFactory.Options#inSampleSize} if an {@link OutOfMemoryError} occurs.<br/>
     * If after trying 4 times the file still could not be decoded, {@code null} is returned.
     * 
     * @param imageFile The file to be decoded.
     * @param options The Options object passed to {@link BitmapFactory#decodeFile(String, android.graphics.BitmapFactory.Options)} (can be {@code null}).
     * @return The decoded bitmap, or {@code null} if it could not be decoded.
     */
    public static Bitmap tryDecodeFile(File imageFile, BitmapFactory.Options options) {
        Log.d(TAG, "tryDecodeFile imageFile=" + imageFile);
        int trials = 0;
        while (trials < 4) {
            try {
                Bitmap res = BitmapFactory.decodeFile(imageFile.getPath(), options);
                if (res == null) {
                    Log.d(TAG, "tryDecodeFile res=null");
                } else {
                    Log.d(TAG, "tryDecodeFile res width=" + res.getWidth() + " height=" + res.getHeight());
                }
                return res;
            } catch (OutOfMemoryError e) {
                if (options == null) {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                }
                Log.w(TAG, "tryDecodeFile Could not decode file with inSampleSize=" + options.inSampleSize + ", try with inSampleSize="
                        + (options.inSampleSize + 1), e);
                options.inSampleSize++;
                trials++;
            }
        }
        Log.w(TAG, "tryDecodeFile Could not decode the file after " + trials + " trials, returning null");
        return null;
    }

    /**
     * Returns an mutable version of the given bitmap.<br/>
     * The given bitmap is recycled. A temporary file is used (using {@link File#createTempFile(String, String)}) to avoid allocating twice the needed memory.
     */
    public static Bitmap asMutable(Bitmap bitmap) throws IOException {
        // This is the file going to use temporally to dump the bitmap bytes
        File tmpFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), null);
        Log.d(TAG, "getImmutable tmpFile=" + tmpFile);

        // Open it as an RandomAccessFile
        RandomAccessFile randomAccessFile = new RandomAccessFile(tmpFile, "rw");

        // Get the width and height of the source bitmap
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Dump the bytes to the file.
        // This assumes the source bitmap is loaded using options.inPreferredConfig = Config.ARGB_8888 (hence the value of 4 bytes per pixel)
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, width * height * 4);
        bitmap.copyPixelsToBuffer(buffer);

        // Recycle the source bitmap, this will be no longer used
        bitmap.recycle();

        // Create a new mutable bitmap to load the bitmap from the file
        bitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());

        // Load it back from the temporary buffer
        buffer.position(0);
        bitmap.copyPixelsFromBuffer(buffer);

        // Cleanup
        channel.close();
        randomAccessFile.close();
        tmpFile.delete();

        return bitmap;
    }

    /**
     * List of EXIF tags used by {@link #copyExifTags(File, File)}.
     */
    //@formatter:off
    @SuppressLint("InlinedApi")
    private static final String[] EXIF_TAGS = new String[] { 
        ExifInterface.TAG_APERTURE,
        ExifInterface.TAG_DATETIME, 
        ExifInterface.TAG_EXPOSURE_TIME,
        ExifInterface.TAG_FLASH, 
        ExifInterface.TAG_FOCAL_LENGTH, 
        ExifInterface.TAG_GPS_ALTITUDE, 
        ExifInterface.TAG_GPS_ALTITUDE_REF,
        ExifInterface.TAG_GPS_DATESTAMP, 
        ExifInterface.TAG_GPS_LATITUDE, 
        ExifInterface.TAG_GPS_LATITUDE_REF, 
        ExifInterface.TAG_GPS_LONGITUDE,
        ExifInterface.TAG_GPS_LONGITUDE_REF, 
        ExifInterface.TAG_GPS_PROCESSING_METHOD, 
        ExifInterface.TAG_GPS_TIMESTAMP, 
        ExifInterface.TAG_ISO, 
        ExifInterface.TAG_MAKE, 
        ExifInterface.TAG_MODEL, 
        ExifInterface.TAG_WHITE_BALANCE,
    };
    //@formatter:on

    /**
     * Copy the EXIF tags from the source image file to the destination image file.
     * 
     * @param sourceFile The existing source JPEG file.
     * @param destFile The existing destination JPEG file.
     * @throws IOException If EXIF information could not be read or written.
     */
    public static void copyExifTags(File sourceFile, File destFile) throws IOException {
        Log.d(TAG, "copyExifTags sourceFile=" + sourceFile + " destFile=" + destFile);
        ExifInterface sourceExifInterface = new ExifInterface(sourceFile.getPath());
        ExifInterface destExifInterface = new ExifInterface(destFile.getPath());
        boolean atLeastOne = false;
        for (String exifTag : EXIF_TAGS) {
            String value = sourceExifInterface.getAttribute(exifTag);
            if (value != null) {
                atLeastOne = true;
                destExifInterface.setAttribute(exifTag, value);
            }
        }
        if (atLeastOne) destExifInterface.saveAttributes();
    }

    /**
     * Retrieves the dimensions of the bitmap in the given file.
     * 
     * @param bitmapFile The file containing the bitmap to measure.
     * @return A {@code Point} containing the width in {@code x} and the height in {@code y}.
     */
    public static Point getDimensions(File bitmapFile) {
        Log.d(TAG, "getDimensions bitmapFile=" + bitmapFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapFile.getPath(), options);
        int width = options.outWidth;
        int height = options.outHeight;
        Point res = new Point(width, height);
        Log.d(TAG, "getDimensions res=" + res);
        return res;
    }

    /**
     * Retrieves the rotation in the EXIF tags of the given file.
     * 
     * @param bitmapFile The file from which to retrieve the info.
     * @return The rotation in degrees, or {@code 0} if there was no EXIF tags in the given file, or it could not be read.
     */
    public static int getExifRotation(File bitmapFile) {
        Log.d(TAG, "getExifRotation bitmapFile=" + bitmapFile);
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(bitmapFile.getPath());
        } catch (IOException e) {
            Log.w(TAG, "getExifRotation Could not read exif info: returning 0", e);
            return 0;
        }
        int exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Log.d(TAG, "getExifRotation orientation=" + exifOrientation);
        int res = 0;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                res = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                res = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                res = 270;
                break;
        }
        Log.d(TAG, "getExifRotation res=" + res);
        return res;
    }

    /**
     * Creates a small version of the bitmap inside the given file, using the given max dimensions.<br/>
     * The resulting bitmap's dimensions will always be smaller than the given max dimensions.<br/>
     * The rotation EXIF tag of the given file, if present, is used to return a thumbnail that won't be rotated.
     * 
     * @param bitmapFile The file containing the bitmap to create a thumbnail from.
     * @param maxWidth The wanted maximum width of the resulting thumbnail.
     * @param maxHeight The wanted maximum height of the resulting thumbnail.
     * @return A small version of the bitmap, or (@code null} if the given bitmap could not be decoded.
     */
    public static Bitmap createThumbnail(File bitmapFile, int maxWidth, int maxHeight) {
        Log.d(TAG, "createThumbnail imageFile=" + bitmapFile + " maxWidth=" + maxWidth + " maxHeight=" + maxHeight);
        // Get exif rotation
        int rotation = getExifRotation(bitmapFile);

        // Determine optimal inSampleSize
        Point originalDimensions = getDimensions(bitmapFile);
        int width = originalDimensions.x;
        int height = originalDimensions.y;
        int inSampleSize = 1;
        if (rotation == 90 || rotation == 270) {
            // In these 2 cases we invert the measured dimensions because the bitmap is rotated
            width = originalDimensions.y;
            height = originalDimensions.x;
        }
        int widthRatio = width / maxWidth;
        int heightRatio = height / maxHeight;

        // Take the max, because we don't care if one of the returned thumbnail's side is smaller
        // than the specified maxWidth/maxHeight.
        inSampleSize = Math.max(widthRatio, heightRatio);
        Log.d(TAG, "createThumbnail using inSampleSize=" + inSampleSize);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap res = tryDecodeFile(bitmapFile, options);
        if (res == null) {
            Log.w(TAG, "createThumbnail Could not decode file, returning null");
            return null;
        }

        // Rotate if necessary
        if (rotation != 0) {
            Log.d(TAG, "createThumbnail rotating thumbnail");
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedBitmap = null;
            try {
                rotatedBitmap = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, false);
                res.recycle();
                res = rotatedBitmap;
            } catch (OutOfMemoryError exception) {
                Log.w(TAG, "createThumbnail Could not rotate bitmap, keeping original orientation", exception);
            }
        }
        Log.d(TAG, "createThumbnail res width=" + res.getWidth() + " height=" + res.getHeight());

        return res;
    }
}