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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple implementation of an {@link AlertDialog}.<br/>
 * If the calling {@link Activity} implements {@link AlertDialogListener}, it will be notified of button clicks.<br/>
 * If {@link #mCancelIsNegative} is {@code true} (the default value), canceling is considered equivalent
 * to clicking the negative button.
 * 
 * @see AlertDialogListener
 */
public class AlertDialogFragment extends DialogFragment {
    private static final String PREFIX = AlertDialogFragment.class.getName() + ".";
    public static final String FRAGMENT_TAG = PREFIX + "FRAGMENT_TAG";

    private int mTag;
    private String mTitle;
    private int mTitleId;
    private String mMessage;
    private int mMessageId;
    private ArrayList<CharSequence> mItems;
    private int mItemsId;
    private String mPositiveButton;
    private int mPositiveButtonId;
    private String mNegativeButton;
    private int mNegativeButtonId;
    private boolean mCancelIsNegative;
    private Object mPayload;

    private Bundle getArgs() {
        Bundle res = getArguments();
        if (res == null) {
            res = new Bundle();
            setArguments(res);
        }
        return res;
    }

    /**
     * Create a new {@link AlertDialogFragment}.
     * 
     * @param tag A tag to use to identify the origin of click events in the calling {@link Activity}.
     * @return The newly built {@link AlertDialogFragment}.
     */
    public static AlertDialogFragment newInstance(int tag) {
        AlertDialogFragment res = new AlertDialogFragment();
        res.getArgs().putInt("tag", tag);
        res.setCancelIsNegative(true);
        return res;
    }


    /**
     * @param resId The resource id to be used for the title text, or {@code 0} for no title.
     */
    public void setTitle(int resId) {
        getArgs().putInt("titleId", resId);
    }

    /**
     * @param title The title text, or {@code null} for no title.
     */
    public void setTitle(String title) {
        getArgs().putString("title", title);
    }


    /**
     * @param resId The resource id to be used for the message text, or {@code 0} for no message.
     */
    public void setMessage(int resId) {
        getArgs().putInt("messageId", resId);
    }

    /**
     * @param message The message text, or {@code null} for no message.
     */
    public void setMessage(String message) {
        getArgs().putString("message", message);
    }


    /**
     * @param resId The resource id to be used for the list items, or {@code 0} for no list.
     */
    public void setItems(int resId) {
        getArgs().putInt("itemsId", resId);
    }

    /**
     * @param items The list items, or {@code null} for no list.
     */
    public void setItems(ArrayList<CharSequence> items) {
        getArgs().putCharSequenceArrayList("items", items);
    }


    /**
     * @param resId The resource id to be used for the negative button text, or {@code 0} for no negative button.
     */
    public void setNegativeButton(int resId) {
        getArgs().putInt("negativeButtonId", resId);
    }

    /**
     * @param negativeButton The negative button text, or {@code null} for no negative button.
     */
    public void setNegativeButton(String negativeButton) {
        getArgs().putString("negativeButton", negativeButton);
    }


    /**
     * @param resId The resource id to be used for the positive button text, or {@code 0} for no positive button.
     */
    public void setPositiveButton(int resId) {
        getArgs().putInt("positiveButtonId", resId);
    }

    /**
     * @param positiveButton The positive button text, or {@code null} for no positive button.
     */
    public void setPositiveButton(String positiveButton) {
        getArgs().putString("positiveButton", positiveButton);
    }


    /**
     * @param payload A payload that will be passed back to click events in the calling {@link Activity}.
     */
    public void setPayload(Serializable payload) {
        getArgs().putSerializable("payload", payload);
    }

    /**
     * @param payload A payload that will be passed back to click events in the calling {@link Activity}.
     */
    public void setPayload(Parcelable payload) {
        getArgs().putParcelable("payload", payload);
    }


    /**
     * @param cancelIsNegative If {@code true} (the default value), canceling is considered equivalent
     *            to clicking the negative button.
     */
    public void setCancelIsNegative(boolean cancelIsNegative) {
        getArgs().putBoolean("cancelIsNegative", cancelIsNegative);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getArguments().getInt("tag");
        mTitle = getArguments().getString("title");
        mTitleId = getArguments().getInt("titleId");
        mMessage = getArguments().getString("message");
        mMessageId = getArguments().getInt("messageId");
        mItems = getArguments().getCharSequenceArrayList("items");
        mItemsId = getArguments().getInt("itemsId");
        mPositiveButton = getArguments().getString("positiveButton");
        mPositiveButtonId = getArguments().getInt("positiveButtonId");
        mNegativeButton = getArguments().getString("negativeButton");
        mNegativeButtonId = getArguments().getInt("negativeButtonId");
        mCancelIsNegative = getArguments().getBoolean("cancelIsNegative");
        mPayload = getArguments().get("payload");
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
            builder.setMessage(mMessage);
        } else if (mMessageId != 0) {
            builder.setMessage(mMessageId);
        }

        if (mItems != null) {
            builder.setItems(mItems.toArray(new CharSequence[mItems.size()]), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((AlertDialogListener) getActivity()).onClickListItem(mTag, which, mPayload);
                }
            });
        } else if (mItemsId != 0) {
            builder.setItems(mItemsId, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((AlertDialogListener) getActivity()).onClickListItem(mTag, which, mPayload);
                }
            });
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

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (mCancelIsNegative) {
            if (getActivity() instanceof AlertDialogListener) {
                ((AlertDialogListener) getActivity()).onClickNegative(mTag, mPayload);
            }
        }
    }

    /**
     * Show this {@link AlertDialogFragment}.
     */
    public void show(FragmentManager manager) {
        show(manager, FRAGMENT_TAG);
    }
}
