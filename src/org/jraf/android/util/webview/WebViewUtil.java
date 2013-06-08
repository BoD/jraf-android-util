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
package org.jraf.android.util.webview;

import java.io.IOException;

import android.content.res.Resources.NotFoundException;
import android.webkit.WebView;

import org.jraf.android.util.io.IoUtil;

public class WebViewUtil {
    /**
     * Load the contents of a raw resource into the given {@link WebView}.
     * 
     * @param webView The {@link WebView} to load data into.
     * @param pageResId The id of a resource in the {@code raw} folder.
     * @throws NotFoundException If the given ID does not exist.
     */
    public static void loadFromRaw(WebView webView, int pageResId) {
        String html;
        try {
            html = IoUtil.readFully(webView.getContext().getResources().openRawResource(pageResId));
        } catch (final IOException e) {
            // Should never happen
            throw new AssertionError("Could not read raw resource");
        }
        html = reworkForWebView(html);
        webView.loadData(html, "text/html", "utf-8");
    }

    private static String reworkForWebView(String s) {
        return s.replace("\n", " ");
    }
}
