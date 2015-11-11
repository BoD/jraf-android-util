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
package org.jraf.android.util.listeners;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jraf.android.util.handler.HandlerUtil;

/**
 * Utility class facilitating the implementation of the 'observer' design pattern.
 */
public class Listeners<T> implements Iterable<T> {
    private Set<T> mListeners = new HashSet<T>(3);
    private Dispatcher<T> mNewListenerDispatcher;

    public static interface Dispatcher<T> {
        void dispatch(T listener);
    }

    public static <T> Listeners<T> newInstance() {
        return new Listeners<T>();
    }

    public boolean add(T listener) {
        int prevSize = mListeners.size();
        boolean res = mListeners.add(listener);
        if (res) {
            onListenerCountChanged(prevSize, mListeners.size());
            if (mNewListenerDispatcher != null) dispatch(listener, mNewListenerDispatcher);
            onNewListener(listener);
        }
        return res;
    }

    public boolean remove(T listener) {
        int prevSize = mListeners.size();
        boolean res = mListeners.remove(listener);
        if (res) onListenerCountChanged(prevSize, mListeners.size());
        return res;
    }

    @Override
    public Iterator<T> iterator() {
        return mListeners.iterator();
    }

    private void onListenerCountChanged(int prevSize, int newSize) {
        if (prevSize == 0 && newSize == 1) {
            onFirstListener();
        } else if (newSize == 0) {
            onNoMoreListeners();
        }
    }

    protected void onFirstListener() {}

    protected void onNoMoreListeners() {}

    protected void onNewListener(T listener) {}

    public void setNewListenerDispatcher(Dispatcher<T> newListenerDispatcher) {
        mNewListenerDispatcher = newListenerDispatcher;
    }

    /**
     * Dispatch an event to all listeners.<br/>
     * Dispatching will be done in the main/ui thread.
     */
    public void dispatch(final Dispatcher<T> dispatcher) {
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (T listener : mListeners) {
                    dispatcher.dispatch(listener);
                }
            }
        });
    }

    /**
     * Dispatch an event to a specific listener.<br/>
     * Dispatching will be done in the main/ui thread.
     */
    public void dispatch(final T listener, final Dispatcher<T> dispatcher) {
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dispatcher.dispatch(listener);
            }
        });
    }


    /**
     * Returns the current number of listeners.
     */
    public int size() {
        return mListeners.size();
    }
}
