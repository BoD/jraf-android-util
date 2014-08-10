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
 * Copyright (C) 2009 The Android Open Source Project
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
package org.jraf.android.util.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;

import org.jraf.android.util.R;

public class DateTimeUtil {
    private static Map<String, DateFormat> sTimeZoneTimeFormats = new HashMap<String, DateFormat>();
    private static SimpleDateFormat DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    private static SimpleDateFormat DATE_FORMAT_ISO8601_UTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    static {
        DATE_FORMAT_ISO8601_UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Formats the given time according to the user's preference, and using the given time zone.
     * 
     * @param date The time to format.
     * @param timeZone The time zone to use.
     * @return The formatted time.
     */
    public static String formatTime(Context context, Date date, TimeZone timeZone) {
        DateFormat timeZoneTimeFormat = sTimeZoneTimeFormats.get(timeZone.getID());
        if (timeZoneTimeFormat == null) {
            timeZoneTimeFormat = android.text.format.DateFormat.getTimeFormat(context);
            timeZoneTimeFormat.setTimeZone(timeZone);
            sTimeZoneTimeFormats.put(timeZone.getID(), timeZoneTimeFormat);
        }
        return timeZoneTimeFormat.format(date);
    }

    /**
     * Formats the current time according to the user's preference, and using the given time zone.
     * 
     * @param timezone The time zone to use.
     * @return The formatted time.
     */
    public static String getCurrentTimeForTimezone(Context context, String timezone) {
        return formatTime(context, new Date(), TimeZone.getTimeZone(timezone));
    }

    /**
     * Formats a date / time according to the ISO 8601 format, optionally forcing to UTC.
     * 
     * @param date The date / time to format.
     * @param utc If {@code true}, the resulting string will be in UTC timezone (that is, ending with "Z").
     * @return The formatted date / time.
     */
    public static synchronized String toIso8601(long date, boolean utc) {
        if (utc) {
            return DATE_FORMAT_ISO8601_UTC.format(date);
        }
        return DATE_FORMAT_ISO8601.format(date);
    }

    /**
     * Parses a date / time formatted according to the ISO 8601 format.
     * 
     * @param date The date / time to parse.
     * @return The parsed date / time.
     */
    public static synchronized Date fromIso8601(String date) throws ParseException {
        return DATE_FORMAT_ISO8601.parse(date);
    }

    /**
     * Return given duration in a human-friendly format. For example, "4
     * minutes" or "1 second".
     */
    public static CharSequence formatDuration(Context context, long millis) {
        final Resources res = context.getResources();
        if (millis >= DateUtils.HOUR_IN_MILLIS) {
            final int hours = (int) (millis / DateUtils.HOUR_IN_MILLIS);
            final int minutes = (int) (millis % DateUtils.HOUR_IN_MILLIS / DateUtils.MINUTE_IN_MILLIS);
            if (minutes == 0) {
                return res.getQuantityString(R.plurals.duration_hours, hours, hours);
            }
            return res.getQuantityString(R.plurals.duration_hours, hours, hours) + " " + res.getQuantityString(R.plurals.duration_minutes, minutes, minutes);
        } else if (millis >= DateUtils.MINUTE_IN_MILLIS) {
            final int minutes = (int) ((millis + 30000) / DateUtils.MINUTE_IN_MILLIS);
            return res.getQuantityString(R.plurals.duration_minutes, minutes, minutes);
        } else {
            final int seconds = (int) ((millis + 500) / DateUtils.SECOND_IN_MILLIS);
            return res.getQuantityString(R.plurals.duration_seconds, seconds, seconds);
        }
    }

    /**
     * Return given duration in a human-friendly 'short' format. Seconds won't be shown.
     */
    public static CharSequence formatDurationShort(Context context, long millis) {
        final Resources res = context.getResources();
        if (millis >= DateUtils.HOUR_IN_MILLIS) {
            final int hours = (int) (millis / DateUtils.HOUR_IN_MILLIS);
            final int minutes = (int) (millis % DateUtils.HOUR_IN_MILLIS / DateUtils.MINUTE_IN_MILLIS);
            if (minutes == 0) {
                return res.getString(R.string.duration_short_hours, hours);
            }
            return res.getString(R.string.duration_short_hours, hours) + " " + res.getString(R.string.duration_short_minutes, minutes);
        } else if (millis >= DateUtils.MINUTE_IN_MILLIS) {
            final int minutes = (int) ((millis + 30000) / DateUtils.MINUTE_IN_MILLIS);
            return res.getString(R.string.duration_short_minutes, minutes);
        } else {
            return res.getString(R.string.duration_short_lessThanOneMinute);
        }
    }
}
