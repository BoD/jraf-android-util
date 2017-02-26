/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2017 Benoit 'BoD' Lubek (BoD@JRAF.org)
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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jraf.android.util.R;
import org.jraf.android.util.databinding.UtilAboutWearBinding;

public class WearAboutActivity extends Activity {
    public static final String EXTRA_PARAMS = "org.jraf.android.util.about.AboutActivity.EXTRA_PARAMS";

    private AboutActivityParams mParams;
    private UtilAboutWearBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.util_about_wear);
        mParams = getIntent().getParcelableExtra(EXTRA_PARAMS);
        mBinding.setParams(mParams);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ignored) {
            // Cannot happen
        }

        // App icon
        Drawable appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
        mBinding.imgAppIcon.setImageDrawable(appIcon);

        // Title
        String html =
                getString(R.string.about_txtTitle, mParams.appName, packageInfo.versionName, packageInfo.versionCode, mParams.buildDate,
                        mParams.gitSha1);
        mBinding.txtTitle.setText(Html.fromHtml(html));

        // Links
        ViewGroup conLinks = (ViewGroup) findViewById(R.id.conLinks);
        for (AboutActivityParams.Link link : mParams.linkList) {
            createLinkView(conLinks, link);
        }
    }

    private View createLinkView(ViewGroup conLinks, final AboutActivityParams.Link link) {
        TextView res = (TextView) getLayoutInflater().inflate(R.layout.util_about_link_wear, conLinks, false);
        conLinks.addView(res);
        res.setText(Html.fromHtml(String.format("→ <u>%s</u>", link.text)));
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUri(link.uri);
            }
        });
        return res;
    }

    private void openUri(String uri) {

    }

    public void onShareClick(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mParams.shareTextSubject);
        String shareTextBody = String.format(mParams.shareTextBody, getPackageName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTextBody);
        shareIntent.putExtra("sms_body", shareTextBody);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.about_shareWith)));
    }

    public void onRateClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(Intent.createChooser(intent, null));
    }

    public void onOtherAppsClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=pub:" + mParams.authorPlayStoreName));
        startActivity(Intent.createChooser(intent, null));
    }
}
