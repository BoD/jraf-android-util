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
package org.jraf.android.util.dialog;

import java.io.Serializable;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class AlertDialogFragment extends DialogFragment {
    private static final String PREFIX = AlertDialogFragment.class.getName() + ".";
    public static final String FRAGMENT_TAG = PREFIX + "FRAGMENT_TAG";

    private int mTag;
    private String mTitle;
    private int mTitleId;
    private String mMessage;
    private int mMessageId;
    private int mIconId;
    private String mPositiveButton;
    private int mPositiveButtonId;
    private String mNegativeButton;
    private int mNegativeButtonId;
    private Object mPayload;

    public static AlertDialogFragment newInstance(int tag, int titleId, int messageId, int iconId, int positiveButtonId, int negativeButtonId,
            Serializable payload) {
        final AlertDialogFragment res = new AlertDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("tag", tag);
        arguments.putInt("titleId", titleId);
        arguments.putInt("messageId", messageId);
        arguments.putInt("iconId", iconId);
        arguments.putInt("positiveButtonId", positiveButtonId);
        arguments.putInt("negativeButtonId", negativeButtonId);
        arguments.putSerializable("payload", payload);
        res.setArguments(arguments);
        return res;
    }

    public static AlertDialogFragment newInstance(int tag, String title, String message, int iconId, String positiveButton, String negativeButton,
            Serializable payload) {
        final AlertDialogFragment res = new AlertDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("tag", tag);
        arguments.putString("title", title);
        arguments.putString("message", message);
        arguments.putInt("iconId", iconId);
        arguments.putString("positiveButton", positiveButton);
        arguments.putString("negativeButton", negativeButton);
        arguments.putSerializable("payload", payload);
        res.setArguments(arguments);
        return res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getArguments().getInt("tag");
        mTitle = getArguments().getString("title");
        mTitleId = getArguments().getInt("titleId");
        mMessage = getArguments().getString("message");
        mMessageId = getArguments().getInt("messageId");
        mIconId = getArguments().getInt("iconId");
        mPositiveButton = getArguments().getString("positiveButton");
        mPositiveButtonId = getArguments().getInt("positiveButtonId");
        mNegativeButton = getArguments().getString("negativeButton");
        mNegativeButtonId = getArguments().getInt("negativeButtonId");
        mIconId = getArguments().getInt("iconId");
        mPayload = getArguments().getSerializable("payload");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mTitle != null) {
            builder.setTitle(mTitle);
        } else if (mTitleId != 0) {
            builder.setTitle(mTitleId);
        }
        if (mMessage != null) {
            builder.setTitle(mMessage);
        } else if (mMessageId != 0) {
            builder.setMessage(mMessageId);
        }
        OnClickListener positiveOnClickListener = null;
        OnClickListener negativeOnClickListener = null;
        if (getActivity() instanceof AlertDialogListener) {
            positiveOnClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((AlertDialogListener) getActivity()).onClickPositive(mTag, mPayload);
                }
            };

            negativeOnClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((AlertDialogListener) getActivity()).onClickNegative(mTag, mPayload);
                }
            };
        }
        if (mPositiveButton != null) {
            builder.setPositiveButton(mPositiveButton, positiveOnClickListener);
        } else if (mPositiveButtonId != 0) {
            builder.setPositiveButton(mPositiveButtonId, positiveOnClickListener);
        }
        if (mNegativeButton != null) {
            builder.setNegativeButton(mNegativeButton, negativeOnClickListener);
        } else if (mNegativeButtonId != 0) {
            builder.setNegativeButton(mNegativeButtonId, negativeOnClickListener);
        }
        if (mIconId != 0) builder.setIcon(mIconId);
        return builder.create();
    }

    public void show(FragmentManager manager) {
        show(manager, FRAGMENT_TAG);
    }
}
