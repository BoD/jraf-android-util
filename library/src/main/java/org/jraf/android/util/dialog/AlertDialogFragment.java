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
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ArrayRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;

/**
 * A simple implementation of an {@link AlertDialog}.<br/>
 * <br/>
 * If the parent {@link Fragment} or {@link Activity} implements
 * {@link AlertDialogListener}, it will be notified of button clicks.<br/>
 * If {@link #mCancelIsNegative} is {@code true} (the default value),
 * canceling is equivalent to clicking the negative button.
 *
 * @see AlertDialogListener
 */
public class AlertDialogFragment extends AppCompatDialogFragment {
    private static final String PREFIX = AlertDialogFragment.class.getName() + ".";
    public static final String FRAGMENT_TAG = PREFIX + "FRAGMENT_TAG";

    private int mTag;
    private CharSequence mTitle;
    private int mTitleId;
    private CharSequence mMessage;
    private int mMessageId;
    private ArrayList<CharSequence> mItems;
    private int mItemsId;
    private CharSequence mPositiveButton;
    private int mPositiveButtonId;
    private CharSequence mNegativeButton;
    private int mNegativeButtonId;
    private boolean mCancelIsNegative;
    private int mViewId;
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
        res.cancelIsNegative(true);
        return res;
    }


    /**
     * @param resId The resource id to be used for the title text, or {@code 0} for no title.
     */
    public AlertDialogFragment title(@StringRes int resId) {
        getArgs().putInt("titleId", resId);
        return this;
    }

    /**
     * @param title The title text, or {@code null} for no title.
     */
    public AlertDialogFragment title(@Nullable CharSequence title) {
        getArgs().putCharSequence("title", title);
        return this;
    }


    /**
     * @param resId The resource id to be used for the message text, or {@code 0} for no message.
     */
    public AlertDialogFragment message(@StringRes int resId) {
        getArgs().putInt("messageId", resId);
        return this;
    }

    /**
     * @param message The message text, or {@code null} for no message.
     */
    public AlertDialogFragment message(@Nullable CharSequence message) {
        getArgs().putCharSequence("message", message);
        return this;
    }


    /**
     * @param resId The resource id to be used for the list items, or {@code 0} for no list.
     */
    public AlertDialogFragment items(@ArrayRes int resId) {
        getArgs().putInt("itemsId", resId);
        return this;
    }

    /**
     * @param items The list items, or {@code null} for no list.
     */
    public AlertDialogFragment items(@Nullable ArrayList<CharSequence> items) {
        getArgs().putCharSequenceArrayList("items", items);
        return this;
    }


    /**
     * @param resId The resource id to be used for the view layout, or {@code 0} for no view.
     */
    public AlertDialogFragment view(@LayoutRes int resId) {
        getArgs().putInt("view", resId);
        return this;
    }


    /**
     * @param resId The resource id to be used for the negative button text, or {@code 0} for no negative button.
     */
    public AlertDialogFragment negativeButton(@StringRes int resId) {
        getArgs().putInt("negativeButtonId", resId);
        return this;
    }

    /**
     * @param negativeButton The negative button text, or {@code null} for no negative button.
     */
    public AlertDialogFragment negativeButton(@Nullable CharSequence negativeButton) {
        getArgs().putCharSequence("negativeButton", negativeButton);
        return this;
    }


    /**
     * @param resId The resource id to be used for the positive button text, or {@code 0} for no positive button.
     */
    public AlertDialogFragment positiveButton(@StringRes int resId) {
        getArgs().putInt("positiveButtonId", resId);
        return this;
    }

    /**
     * @param positiveButton The positive button text, or {@code null} for no positive button.
     */
    public AlertDialogFragment positiveButton(@Nullable CharSequence positiveButton) {
        getArgs().putCharSequence("positiveButton", positiveButton);
        return this;
    }


    /**
     * @param payload A payload that will be passed back to click events in the calling {@link Activity}.
     */
    public AlertDialogFragment payload(Serializable payload) {
        getArgs().putSerializable("payload", payload);
        return this;
    }

    /**
     * @param payload A payload that will be passed back to click events in the calling {@link Activity}.
     */
    public AlertDialogFragment payload(Parcelable payload) {
        getArgs().putParcelable("payload", payload);
        return this;
    }


    /**
     * @param cancelIsNegative If {@code true} (the default value), canceling is considered equivalent
     * to clicking the negative button.
     */
    public AlertDialogFragment cancelIsNegative(boolean cancelIsNegative) {
        getArgs().putBoolean("cancelIsNegative", cancelIsNegative);
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTag = getArguments().getInt("tag");
        mTitle = getArguments().getCharSequence("title");
        mTitleId = getArguments().getInt("titleId");
        mMessage = getArguments().getCharSequence("message");
        mMessageId = getArguments().getInt("messageId");
        mItems = getArguments().getCharSequenceArrayList("items");
        mItemsId = getArguments().getInt("itemsId");
        mViewId = getArguments().getInt("view");
        mPositiveButton = getArguments().getCharSequence("positiveButton");
        mPositiveButtonId = getArguments().getInt("positiveButtonId");
        mNegativeButton = getArguments().getCharSequence("negativeButton");
        mNegativeButtonId = getArguments().getInt("negativeButtonId");
        mCancelIsNegative = getArguments().getBoolean("cancelIsNegative");
        mPayload = getArguments().get("payload");
    }

    @Nullable
    private AlertDialogListener getAlertDialogListener() {
        AlertDialogListener listener = null;
        // Try the parent fragment first
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof AlertDialogListener) {
            listener = (AlertDialogListener) parentFragment;
        } else if (getActivity() instanceof AlertDialogListener) {
            // Then try the activity
            listener = (AlertDialogListener) getActivity();
        }
        return listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Title
        if (mTitle != null) {
            builder.setTitle(mTitle);
        } else if (mTitleId != 0) {
            builder.setTitle(mTitleId);
        }

        // Message
        if (mMessage != null) {
            builder.setMessage(mMessage);
        } else if (mMessageId != 0) {
            builder.setMessage(mMessageId);
        }

        final AlertDialogListener listener = getAlertDialogListener();

        // Items
        if (mItems != null) {
            builder.setItems(mItems.toArray(new CharSequence[mItems.size()]), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) listener.onDialogClickListItem(mTag, which, mPayload);
                }
            });
        } else if (mItemsId != 0) {
            builder.setItems(mItemsId, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) listener.onDialogClickListItem(mTag, which, mPayload);
                }
            });
        }

        // View
        if (mViewId != 0) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            builder.setView(layoutInflater.inflate(mViewId, null, false));
        }

        // Buttons
        OnClickListener positiveOnClickListener = null;
        OnClickListener negativeOnClickListener = null;
        if (listener != null) {
            positiveOnClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onDialogClickPositive(mTag, mPayload);
                }
            };

            negativeOnClickListener = new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onDialogClickNegative(mTag, mPayload);
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

        AlertDialogListener listener = getAlertDialogListener();
        if (mCancelIsNegative && listener != null) {
            listener.onDialogClickNegative(mTag, mPayload);
        }
    }

    private void show(FragmentManager manager) {
        try {
            show(manager, FRAGMENT_TAG);
        } catch (Throwable ignored) {
            // There are times where this could be called after onSaveInstanceState, which throws an IllegalStateException.
            // When that happens, it generally means the activity has been finished so we probably don't want to show the dialog anyway.
            // Just catch it.
        }
    }

    /**
     * Show this {@link AlertDialogFragment}.
     */
    public void show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager());
    }

    /**
     * Show this {@link AlertDialogFragment}.
     */
    public void show(Fragment fragment) {
        show(fragment.getChildFragmentManager());
    }
}
