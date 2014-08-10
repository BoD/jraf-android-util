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
package org.jraf.android.util.http;

import java.io.IOException;

import android.os.Build;

import org.jraf.android.util.Constants;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Thin wrapper around the <a href="https://github.com/kevinsawicki/http-request"><em>http-request</em></a> library.
 */
public class HttpUtil {
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 10000;

    private static final String HTTP_USER_AGENT = System.getProperties().getProperty("http.agent") + " " + Constants.TAG + Constants.VERSION;

    static {
        // See http://developer.android.com/reference/java/net/HttpURLConnection.html
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            HttpRequest.keepAlive(false);
        }
    }

    public static void setDefaultOptions(HttpRequest req) throws IOException {
        req.userAgent(HTTP_USER_AGENT);
        req.connectTimeout(CONNECT_TIMEOUT);
        req.readTimeout(READ_TIMEOUT);
        req.acceptGzipEncoding();
        req.uncompress(true);
        req.trustAllCerts();
        req.trustAllHosts();
    }

    public static HttpRequest get(String url) throws IOException {
        HttpRequest res = HttpRequest.get(url);
        setDefaultOptions(res);
        return res;
    }

    public static HttpRequest post(String url) throws IOException {
        HttpRequest res = HttpRequest.get(url);
        setDefaultOptions(res);
        return res;
    }
}
