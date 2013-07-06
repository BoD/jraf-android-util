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
package org.jraf.android.util.datetime;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.content.Context;

public class DateTimeUtil {
    private static Map<String, DateFormat> sTimeZoneTimeFormats = new HashMap<String, DateFormat>();

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
}
