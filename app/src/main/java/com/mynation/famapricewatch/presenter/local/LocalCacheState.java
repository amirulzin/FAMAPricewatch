package com.mynation.famapricewatch.presenter.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 */

public class LocalCacheState {
    private final Context context;
    private final String sharedPref = "shared_pref";
    private final String cacheStateKey = "Cache-Ready";
    private boolean init, cacheReady;

    public LocalCacheState(Context context) {
        this.context = context.getApplicationContext();
        this.init = false;
    }

    public boolean isCacheReady() {
        if (!init) {
            init = true;
            cacheReady = getSharedPreferences().getBoolean(cacheStateKey, false);
        }
        return cacheReady;
    }


    public void setCacheReady() {
        if (!cacheReady) {
            cacheReady = true;
            getSharedPreferences().edit().putBoolean(cacheStateKey, true).apply();
        }
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
    }
}
