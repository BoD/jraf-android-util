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
 * Copyright (C) 2008 The Android Open Source Project
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
package org.jraf.android.util.mediascanner;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import org.jraf.android.util.Constants;

/**
 * Convenience utility to add an image to the media library.<br/>
 * Note: most of the code here was copied from the Android source, in order to achieve the same functionality in API level 7.
 */
public class MediaScannerUtil {
    private static final String TAG = Constants.TAG + MediaScannerUtil.class.getSimpleName();

    /**
     * Interface for notifying clients of the result of scanning a
     * requested media file.
     */
    public interface OnScanCompletedListener {
        /**
         * Called to notify the client when the media scanner has finished
         * scanning a file.
         * 
         * @param path the path to the file that has been scanned.
         * @param uri the Uri for the file if the scanning operation succeeded
         *            and the file was added to the media database, or null if scanning failed.
         */
        public void onScanCompleted(String path, Uri uri);
    }

    /**
     * Convenience for constructing a {@link MediaScannerConnection}, calling {@link #connect} on it, and calling {@link #scanFile} with the given
     * <var>path</var> and <var>mimeType</var> when the connection is
     * established.
     * 
     * @param context The caller's Context, required for establishing a connection to
     *            the media scanner service.
     *            Success or failure of the scanning operation cannot be determined until {@link MediaScannerConnectionClient#onScanCompleted(String, Uri)} is
     *            called.
     * @param paths Array of paths to be scanned.
     * @param mimeTypes Optional array of MIME types for each path.
     *            If mimeType is null, then the mimeType will be inferred from the file extension.
     * @param callback Optional callback through which you can receive the
     *            scanned URI and MIME type; If null, the file will be scanned but
     *            you will not get a result back.
     */
    public static void scanFile(Context context, String[] paths, String[] mimeTypes, OnScanCompletedListener callback) {
        ClientProxy client = new ClientProxy(paths, mimeTypes, callback);
        MediaScannerConnection connection = new MediaScannerConnection(context, client);
        client.mConnection = connection;
        connection.connect();
    }

    private static class ClientProxy implements MediaScannerConnectionClient {
        final String[] mPaths;
        final String[] mMimeTypes;
        final OnScanCompletedListener mClient;
        MediaScannerConnection mConnection;
        int mNextPath;

        ClientProxy(String[] paths, String[] mimeTypes, OnScanCompletedListener client) {
            mPaths = paths;
            mMimeTypes = mimeTypes;
            mClient = client;
        }

        @Override
        public void onMediaScannerConnected() {
            scanNextPath();
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (mClient != null) {
                mClient.onScanCompleted(path, uri);
            }
            scanNextPath();
        }

        void scanNextPath() {
            if (mNextPath >= mPaths.length) {
                mConnection.disconnect();
                return;
            }
            String mimeType = mMimeTypes != null ? mMimeTypes[mNextPath] : null;
            mConnection.scanFile(mPaths[mNextPath], mimeType);
            mNextPath++;
        }
    }

    /**
     * Synchronously scan a file. This will <strong>block</strong> until the underlying MediaScanner has finished processing the file.
     * 
     * @return the Uri of the scanned file.
     * @throws InterruptedException if the scan has not been completed after 5000 ms.
     */
    public static Uri scanFileNow(Context context, File imageFile) throws InterruptedException {
        // Scan it
        final AtomicReference<Uri> scannedImageUri = new AtomicReference<Uri>();
        MediaScannerUtil.scanFile(context, new String[] { imageFile.getPath() }, null, new OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String p, Uri uri) {
                Log.d(TAG, "onScanCompleted path=" + p + " uri=" + uri);
                scannedImageUri.set(uri);
            }
        });

        // Wait until the media scanner has found our file
        long start = System.currentTimeMillis();
        while (scannedImageUri.get() == null) {
            Log.d(TAG, "scanFileNow Waiting 250ms for media scanner...");
            SystemClock.sleep(250);
            if (System.currentTimeMillis() - start > 6000) {
                throw new InterruptedException("MediaScanner did not scan the file " + imageFile + " after 6000ms");
            }
        }

        return scannedImageUri.get();
    }
}