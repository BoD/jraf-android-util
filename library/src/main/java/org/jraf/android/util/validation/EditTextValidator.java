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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * Abstract class for validators dealing with an {@code EditText}.
 */
abstract class EditTextValidator extends Validator {
    private final EditText mEditText;

    public EditTextValidator(EditText editText, int invalidMessage) {
        this(editText, editText.getContext().getText(invalidMessage));
    }

    public EditTextValidator(final EditText editText, final CharSequence invalidMessage) {
        mEditText = editText;
        final OnFocusChangeListener originalOnFocusChangeListener = editText.getOnFocusChangeListener();
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!isValid()) {
                        editText.setError(invalidMessage);
                    }
                }
                if (originalOnFocusChangeListener != null) originalOnFocusChangeListener.onFocusChange(v, hasFocus);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                getValidators().validate();
                // Remove any error while typing.
                // This is supposed to be done automatically by the framework, but sometimes it doesn't work (Android bug?)
                editText.setError(null);
            }
        });
    }

    @Override
    public boolean isValid() {
        return isValid(mEditText.getText().toString().trim());
    }

    public abstract boolean isValid(String trim);
}