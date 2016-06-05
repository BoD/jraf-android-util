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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jraf.android.util.about;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

public class AboutActivityIntentBuilder {
    private String mAppName;
    private String mBuildDate;
    private String mGitSha1;
    private String mAuthorCopyright;
    private String mLicense;
    private List<AboutActivityParams.Link> mLinkList = new ArrayList<>();
    private String mShareTextSubject;
    private String mShareTextBody;
    private int mBackgroundResId;
    private boolean mIsLightIcons;
    private String mAuthorPlayStoreName = "BoD";

    public AboutActivityIntentBuilder setAppName(String appName) {
        mAppName = appName;
        return this;
    }

    public AboutActivityIntentBuilder setBuildDate(String buildDate) {
        mBuildDate = buildDate;
        return this;
    }

    public AboutActivityIntentBuilder setGitSha1(String gitSha1) {
        mGitSha1 = gitSha1;
        return this;
    }

    public AboutActivityIntentBuilder setAuthorCopyright(String authorCopyright) {
        mAuthorCopyright = authorCopyright;
        return this;
    }

    public AboutActivityIntentBuilder setLicense(String license) {
        mLicense = license;
        return this;
    }

    public AboutActivityIntentBuilder addLink(String uri, String text) {
        mLinkList.add(new AboutActivityParams.Link(uri, text));
        return this;
    }

    public AboutActivityIntentBuilder setShareTextSubject(String shareTextSubject) {
        mShareTextSubject = shareTextSubject;
        return this;
    }

    public AboutActivityIntentBuilder setShareTextBody(String shareTextBody) {
        mShareTextBody = shareTextBody;
        return this;
    }

    public AboutActivityIntentBuilder setBackgroundResId(int backgroundResId) {
        mBackgroundResId = backgroundResId;
        return this;
    }

    public AboutActivityIntentBuilder setIsLightIcons(boolean isLightIcons) {
        mIsLightIcons = isLightIcons;
        return this;
    }

    public AboutActivityIntentBuilder setAuthorPlayStoreNamee(String authorPlayStoreName) {
        mAuthorPlayStoreName = authorPlayStoreName;
        return this;
    }

    public Intent build(Context context) {
        AboutActivityParams params = new AboutActivityParams(mAppName, mBuildDate, mGitSha1, mAuthorCopyright, mLicense, mLinkList, mShareTextSubject,
                mShareTextBody, mBackgroundResId, mIsLightIcons, mAuthorPlayStoreName);
        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra(AboutActivity.EXTRA_PARAMS, params);
        return intent;
    }
}