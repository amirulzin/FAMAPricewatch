package com.mynation.famapricewatch.presenter;

import android.content.Context;
import android.widget.Toast;

import com.mynation.famapricewatch.data.StateData;
import com.mynation.famapricewatch.data.parser.FAMAEndpoint;
import com.mynation.famapricewatch.data.parser.FAMAPriceDataParser;
import com.mynation.famapricewatch.presenter.local.LocalCacheState;
import com.ncornette.cache.OkCacheControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import common.android.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Data processing logic
 */

public class CommodityDataPresenter implements FAMAPriceDataParser.FailureListener, OkCacheControl.NetworkMonitor {

    private static final int CACHE_STALE_IN_DAYS = 14;
    private final OkHttpClient client = OkCacheControl.on(new OkHttpClient().newBuilder()).overrideServerCachePolicy(CACHE_STALE_IN_DAYS, TimeUnit.DAYS).forceCacheWhenOffline(CommodityDataPresenter.this).apply().build();
    private final Context appContext;
    private final LocalCacheState localCacheState;
    private HashMap<String, StateData> stateDataHashMap = new HashMap<>(20, .95f);
    private ArrayList<String> stateIds = new ArrayList<>(20);
    private Set<Listener> mListeners = new HashSet<>(6, .95f);

    public CommodityDataPresenter(final Context applicationContext) {
        this.appContext = applicationContext;
        this.localCacheState = new LocalCacheState(applicationContext);
    }

    public HashMap<String, StateData> getStateDatas() {
        return stateDataHashMap;
    }

    public StateData getStateData(String stateId) {
        return stateDataHashMap.get(stateId);
    }

    public ArrayList<String> getStateIds() {
        return stateIds;
    }

    public void requestData(FAMAEndpoint.PriceKey priceKey) {
        executeRequest(FAMAEndpoint.getPriceGenerationUrl(priceKey));
    }

    private void executeRequest(String url) {

        //When no cache data during first load
        if (!localCacheState.isCacheReady() && !NetworkUtils.isConnected(appContext)) {
            Toast.makeText(appContext, "Not connected to internet", Toast.LENGTH_SHORT).show();
            return;
        }

        for (final Listener listener : mListeners) {
            listener.onDataPreload();
        }

        Request.Builder builder = new Request.Builder().get().url(url);
        Call call = client.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mListeners.size() > 0) {
                    for (final Listener listener : mListeners) {
                        listener.onDataError();
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                localCacheState.setCacheReady();

                String htmlBody = response.body().string();
                ArrayList<StateData> datas = FAMAPriceDataParser.parse(htmlBody, CommodityDataPresenter.this);
                stateIds.clear();
                stateDataHashMap.clear();
                for (final StateData stateData : datas) {
                    stateIds.add(stateData.getCentreName());
                    stateDataHashMap.put(stateData.getCentreName(), stateData);
                }

                if (mListeners.size() > 0) {
                    for (final Listener listener : mListeners) {
                        listener.onDataUpdated();
                    }
                }
            }
        });
    }

    public void addListener(Listener... listeners) {
        Collections.addAll(mListeners, listeners);
    }

    public void removeListener(Listener... listeners) {
        for (final Listener listener : listeners) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void onCurrentTableUnavailable(String previousSuccessfulDataUrl) {
        if (previousSuccessfulDataUrl != null)
            executeRequest(previousSuccessfulDataUrl);
        else
            Toast.makeText(appContext, "No report currently served by FAMA", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isOnline() {
        return NetworkUtils.isConnected(appContext);
    }

    public interface Listener {

        /**
         * Called on UI thread before actual loading begins
         */
        void onDataPreload();

        void onDataUpdated();

        void onDataError();
    }
}
