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
package org.jraf.android.util.validation;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import org.jraf.android.util.R;

/**
 * A list of {@link Validator}s plus utility methods.
 */
public class Validators extends ArrayList<Validator> {
    private boolean mValid = true;
    private final ArrayList<OnValidationListener> mOnValidationListenerList = new ArrayList<OnValidationListener>(1);

    private Validators() {
        super(4);
    }

    public static Validators newValidators() {
        return new Validators();
    }

    public void enableWhenValid(final View view) {
        addOnValidationListener(new OnValidationListener() {
            @Override
            public void onValidation(boolean valid) {
                view.setEnabled(valid);
            }
        });
    }

    private void addOnValidationListener(OnValidationListener onValidationListener) {
        mOnValidationListenerList.add(onValidationListener);
    }

    public boolean isValid() {
        return mValid;
    }

    public void validate() {
        for (Validator v : this) {
            boolean valid = v.isValid();
            if (!valid) {
                mValid = false;
                for (OnValidationListener listener : mOnValidationListenerList) {
                    listener.onValidation(false);
                }
                break;
            }
            mValid = true;
            for (OnValidationListener listener : mOnValidationListenerList) {
                listener.onValidation(true);
            }
        }
    }

    public void addValidator(final Validator validator) {
        validator.setValidators(this);
        add(validator);
    }

    public void addEmailValidator(final EditText editText) {
        addValidator(new EditTextValidator(editText, R.string.validation_email) {
            @Override
            public boolean isValid(String s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    return isEmailValidPostFroyo(s);
                }
                return isEmailValidPreFroyo(s);
            }

            @TargetApi(Build.VERSION_CODES.FROYO)
            private boolean isEmailValidPostFroyo(String s) {
                return Patterns.EMAIL_ADDRESS.matcher(s).matches();
            }

            private boolean isEmailValidPreFroyo(String s) {
                return s.contains("@");
            }
        });
    }

    public void addMinLengthValidator(final EditText editText, final int minLength) {
        String invalidMessage = editText.getContext().getString(R.string.validation_email, minLength);
        addValidator(new EditTextValidator(editText, invalidMessage) {
            @Override
            public boolean isValid(String s) {
                return s.length() >= minLength;
            }
        });
    }

    public void addNotEmptyValidator(final EditText editText) {
        addValidator(new EditTextValidator(editText, R.string.validation_notEmpty) {
            @Override
            public boolean isValid(String s) {
                return s.length() > 0;
            }
        });
    }

    public void addUrlValidator(final EditText editText) {
        addValidator(new EditTextValidator(editText, R.string.validation_url) {
            @Override
            public boolean isValid(String s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    return isUrlValidPostFroyo(s);
                }
                return isUrlValidPreFroyo(s);
            }

            @TargetApi(Build.VERSION_CODES.FROYO)
            private boolean isUrlValidPostFroyo(String s) {
                return Patterns.WEB_URL.matcher(s).matches();
            }

            private boolean isUrlValidPreFroyo(String s) {
                return !TextUtils.isEmpty(s);
            }
        });
    }
}