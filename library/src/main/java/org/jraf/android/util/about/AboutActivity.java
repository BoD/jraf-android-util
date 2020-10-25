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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jraf.android.util.R;
import org.jraf.android.util.log.Log;

public class AboutActivity extends AppCompatActivity {
    public static final String EXTRA_PARAMS = "org.jraf.android.util.about.AboutActivity.EXTRA_PARAMS";

    private AboutActivityParams mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.util_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mParams = getIntent().getParcelableExtra(EXTRA_PARAMS);

        // Background
        ((ImageView) findViewById(R.id.imgBackground)).setImageResource(mParams.backgroundResId);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ignored) {
            // Cannot happen
        }
        // Title
        String html = getString(R.string.about_txtTitle, mParams.appName, packageInfo.versionName, packageInfo.versionCode, mParams.buildDate, mParams.gitSha1);
        ((TextView) findViewById(R.id.txtTitle)).setText(Html.fromHtml(html));

        // Author, copyright
        ((TextView) findViewById(R.id.txtAuthorCopyright)).setText(mParams.authorCopyright);

        // License
        ((TextView) findViewById(R.id.txtLicense)).setText(mParams.license);

        // Action buttons
        Button btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(mShareOnClickListener);

        Button btnRate = (Button) findViewById(R.id.btnRate);
        btnRate.setOnClickListener(mRateOnClickListener);

        Button btnOtherApps = (Button) findViewById(R.id.btnOtherApps);
        btnOtherApps.setOnClickListener(mOtherAppsOnClickListener);

        if (mParams.isLightIcons) {
            btnShare.setCompoundDrawablesWithIntrinsicBounds(R.drawable.util_ic_share_light, 0, 0, 0);
            btnRate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.util_ic_rate_light, 0, 0, 0);
            btnOtherApps.setCompoundDrawablesWithIntrinsicBounds(R.drawable.util_ic_other_apps_light, 0, 0, 0);
        }

        // Links
        ViewGroup conLinks = (ViewGroup) findViewById(R.id.conLinks);
        for (AboutActivityParams.Link link : mParams.linkList) {
            createLinkView(conLinks, link);
        }

        // Open sources licences
        if (mParams.showOpenSourceLicencesLink) {
            TextView res = (TextView) getLayoutInflater().inflate(R.layout.util_about_link, conLinks, false);
            conLinks.addView(res);
            res.setMovementMethod(LinkMovementMethod.getInstance());
            Spanned spanned = Html.fromHtml(String.format("→ <a href=\"x\">%s</a>", getString(R.string.about_openSourceLicenses)));
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
            URLSpan[] urlSpans = spannableStringBuilder.getSpans(0, spanned.length(), URLSpan.class);
            for (URLSpan urlSpan : urlSpans) {
                int start = spannableStringBuilder.getSpanStart(urlSpan);
                int end = spannableStringBuilder.getSpanEnd(urlSpan);
                int flags = spannableStringBuilder.getSpanFlags(urlSpan);
                ClickableSpan clickable = new ClickableSpan() {
                    public void onClick(View view) {
                        showOpenSourceLicences();
                    }
                };
                spannableStringBuilder.setSpan(clickable, start, end, flags);
                spannableStringBuilder.removeSpan(urlSpan);
            }
            res.setText(spannableStringBuilder);
        }
    }

    private void showOpenSourceLicences() {
        try {
            Class<?> ossLicensesMenuActivityClass = Class.forName("com.google.android.gms.oss.licenses.OssLicensesMenuActivity");
            startActivity(new Intent(this, ossLicensesMenuActivityClass));
        } catch (ClassNotFoundException e) {
            Log.e(e, "Could not find OssLicensesMenuActivity");
        }
    }

    private View createLinkView(ViewGroup conLinks, AboutActivityParams.Link link) {
        TextView res = (TextView) getLayoutInflater().inflate(R.layout.util_about_link, conLinks, false);
        conLinks.addView(res);
        res.setMovementMethod(LinkMovementMethod.getInstance());
        res.setText(Html.fromHtml(String.format("→ <a href=\"%s\">%s</a>", link.uri, link.text)));
        return res;
    }

    @Override
    @SuppressLint("MissingSuperCall")
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // Do not call super - we don't want to save the state because the view hierarchy differs in
        // portrait and landscape.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.util_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
            return true;
        } else if (i == R.id.menu_sendLogs) {
            Toast.makeText(this, R.string.about_sendingLogsToast, Toast.LENGTH_SHORT).show();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    Log.sendAppLogsByMail(AboutActivity.this, mParams.sendLogsEmailAddress);
                    return null;
                }
            }.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final OnClickListener mShareOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mParams.shareTextSubject);
            String shareTextBody = String.format(mParams.shareTextBody, getPackageName());
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareTextBody);
            shareIntent.putExtra("sms_body", shareTextBody);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.about_shareWith)));
        }
    };

    private final OnClickListener mRateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(Intent.createChooser(intent, null));
        }
    };

    private final OnClickListener mOtherAppsOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://search?q=pub:" + mParams.authorPlayStoreName));
            startActivity(Intent.createChooser(intent, null));
        }
    };

    private final OnClickListener mDonateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getString(R.string.about_donatePaypalLink, getPackageName())));
            startActivity(Intent.createChooser(intent, null));
        }
    };
}
