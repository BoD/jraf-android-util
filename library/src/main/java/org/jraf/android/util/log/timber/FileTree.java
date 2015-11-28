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

package org.jraf.android.util.log.timber;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.jraf.android.util.io.IoUtil;

public class FileTree extends TagAndMethodNameTree {
    private static final String FILE = "log_%s.html";

    private static final String FILE_0 = "log0.txt";
    private static final String FILE_1 = "log1.txt";
    private static final String KEY_TAG = "KEY_TAG";
    private static final String KEY_MESSAGE = "KEY_MESSAGE";
    private static final String KEY_DATE = "KEY_DATE";
    private static final String KEY_THREAD_NAME = "KEY_THREAD_NAME";
    private static final String KEY_THROWABLE = "KEY_THROWABLE";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss''SSS");

    private final Context mContext;
    private final int mMaxLogSize;
    private File mFile;
    private final File mFile0;
    private final File mFile1;
    private Handler mHandler;
    private boolean mErrorLogged;
    private File mCurrentFile;
    private BufferedWriter mWriter;

    @SuppressWarnings("HandlerLeak")
    public FileTree(Context context, String applicationTag, int maxLogSize) {
        super(applicationTag);
        mContext = context;
        mMaxLogSize = maxLogSize;

        mFile0 = new File(context.getFilesDir(), FILE_0);
        mFile1 = new File(context.getFilesDir(), FILE_1);

        try {
            initFile();
        } catch (IOException e) {
            logError("Fatal error! Could not open log file.", e);
            return;
        }

        HandlerThread handlerThread = new HandlerThread(FileTree.class.getName(), android.os.Process.THREAD_PRIORITY_LOWEST);
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (mCurrentFile.length() >= mMaxLogSize / 2) {
                    android.util.Log.d("Log", "File is " + mCurrentFile.length() + " bytes: switch");
                    // Switch files
                    mCurrentFile = mCurrentFile == mFile0 ? mFile1 : mFile0;
                    try {
                        IoUtil.closeSilently(mWriter);
                        mWriter = new BufferedWriter(new FileWriter(mCurrentFile, false));
                    } catch (IOException e) {
                        logError("Fatal error! Could not open log file.", e);
                    }
                }

                try {
                    mWriter.write(getCurrentDateTime());
                    mWriter.write('\t');
                    switch (msg.what) {
                        case android.util.Log.VERBOSE:
                            mWriter.write("V");
                            break;
                        case android.util.Log.DEBUG:
                            mWriter.write("D");
                            break;
                        case android.util.Log.INFO:
                            mWriter.write("I");
                            break;
                        case android.util.Log.WARN:
                            mWriter.write("W");
                            break;
                        case android.util.Log.ERROR:
                            mWriter.write("E");
                            break;
                    }
                    mWriter.write('\t');
                    Bundle data = msg.getData();
                    mWriter.write(data.getString(KEY_THREAD_NAME));
                    mWriter.write('\t');
                    mWriter.write(data.getString(KEY_TAG));
                    mWriter.write('\t');
                    mWriter.write(data.getString(KEY_MESSAGE));
                    mWriter.write('\n');
                    Throwable throwable = (Throwable) data.getSerializable(KEY_THROWABLE);
                    if (throwable != null) {
                        throwable.printStackTrace(new PrintWriter(mWriter));
                        mWriter.write('\n');
                    }
                    mWriter.flush();
                } catch (IOException e) {
                    logError("Fatal error! Could not write to log file.", e);
                }
            }
        };

        // Install an exception handler
        final Thread.UncaughtExceptionHandler previousExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                prepareLogFile();
                previousExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }

    private String getCurrentDateTime() {
        return DATE_FORMAT.format(new Date());
    }

    private void initFile() throws IOException {
        if (!mFile0.exists() || !mFile1.exists()) {
            // Use file 0 by default (the first time)
            mCurrentFile = mFile0;
        } else {
            // Keep using the file we used last time (the most recently modified one)
            if (mFile0.lastModified() > mFile1.lastModified()) {
                android.util.Log.d("Log", "Using log0");
                mCurrentFile = mFile0;
            } else {
                android.util.Log.d("Log", "Using log1");
                mCurrentFile = mFile1;
            }
        }
        mWriter = new BufferedWriter(new FileWriter(mCurrentFile, true));
    }

    /**
     * Prepares the log file by retrieving contents from the temporary files.<br/>
     * This must not be called from the UI thread since it accesses the disk.
     *
     * @return true if we were able to prepare the log file, false if some error occurred.
     */
    public boolean prepareLogFile() {
        String fileName = String.format(FILE, new SimpleDateFormat("yyMMddHHmm").format(new Date()));
        mFile = new File(mContext.getExternalFilesDir(null), fileName);

        android.util.Log.d("Log", "Preparing log file...");
        BufferedInputStream in0 = null;
        BufferedInputStream in1 = null;
        BufferedOutputStream out = null;
        try {
            if (mFile0.exists()) in0 = new BufferedInputStream(new FileInputStream(mFile0));
            if (mFile1.exists()) in1 = new BufferedInputStream(new FileInputStream(mFile1));
            out = new BufferedOutputStream(new FileOutputStream(mFile, false));

            out.write(getHeader().getBytes("utf-8"));

            if (mFile0.exists() && mFile1.exists()) {
                if (mFile0.lastModified() < mFile1.lastModified()) {
                    IoUtil.copy(in0, out);
                    IoUtil.copy(in1, out);
                } else {
                    IoUtil.copy(in1, out);
                    IoUtil.copy(in0, out);
                }
            } else if (mFile0.exists()) {
                IoUtil.copy(in0, out);
            } else if (mFile1.exists()) {
                IoUtil.copy(in1, out);
            }
            out.flush();
        } catch (IOException e) {
            android.util.Log.e("Log", "Could not prepare log file.", e);
            return false;
        } finally {
            IoUtil.closeSilently(in0, in1, out);
        }
        android.util.Log.d("Log", "Done.");
        return true;
    }

    private String getHeader() {
        int versionCode;
        try {
            final PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Should never happen
            throw new AssertionError(e);
        }
        String res = "<html><body><pre>\n";
        res += "===================================================================\n";
        res += "Logs collected on: " + getCurrentDateTime() + "\n";
        res += "Version code: " + versionCode + "\n";
        res += "Android API level: " + Build.VERSION.SDK_INT + "\n";
        res += "Device: " + Build.MANUFACTURER + " " + Build.DEVICE + "\n";
        res += "===================================================================\n";
        return res;
    }

    public File getFile() {
        return mFile;
    }


    /*
     * Internal errors.
     */

    private void logError(String message, Throwable throwable) {
        // This will be logged only once
        if (!mErrorLogged) {
            android.util.Log.e("Log", message, throwable);
            mErrorLogged = true;
        }
    }


    /*
     * TagAndMethodNameTree implementation.
     */

    @Override
    protected void doLog(int priority, String tag, String methodName, String message, Throwable t) {
        Message msg = Message.obtain(mHandler, priority, message);
        Bundle data = msg.getData();
        data.putString(KEY_TAG, tag);
        data.putString(KEY_MESSAGE, methodName + " " + message);
        data.putLong(KEY_DATE, System.currentTimeMillis());
        data.putString(KEY_THREAD_NAME, String.valueOf(Thread.currentThread().getName()));
        if (t != null) data.putSerializable(KEY_THROWABLE, t);
        mHandler.sendMessage(msg);
    }
}
