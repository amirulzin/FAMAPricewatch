package com.amirulzin.famapricewatch.data.parser;

import com.amirulzin.famapricewatch.data.StateData;
import com.google.common.base.Stopwatch;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class FAMAPriceDataParserTest {

    private final Logger logger = Logger.getLogger(FAMAPriceDataParserTest.class.getName());

    @Test
    public void parseFarm() throws Exception {
        parse(FAMAEndpoint.PriceKey.FARM);
    }

    @Test
    public void parseWholesale() throws Exception {
        parse(FAMAEndpoint.PriceKey.WHOLESALE);
    }

    @Test
    public void parseRetail() throws Exception {
        parse(FAMAEndpoint.PriceKey.RETAIL);
    }

    private void parse(FAMAEndpoint.PriceKey priceKey) throws Exception {

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder().get().url(FAMAEndpoint.getPriceGenerationUrl(priceKey));
        Call call = client.newCall(builder.build());

        final Stopwatch stopwatch = Stopwatch.createStarted();
        Response response = call.execute();
        String htmlBody = response.body().string();
        logger.info("ResponseTime: " + stopwatch.toString() + " htmlLength: " + String.valueOf(htmlBody.length()));

        stopwatch.reset();
        stopwatch.start();

        ArrayList<StateData> result = FAMAPriceDataParser.parse(htmlBody);
        logger.info("ParseTime: " + stopwatch.toString());
        stopwatch.stop();

        for (StateData stateData : result) {
            logger.info(stateData.toString());
        }

        Assert.assertNotEquals("Result is empty", result.size(), 0);
        Assert.assertNotEquals("Row key is null", result.get(result.size() / 2).getCommodities().get(0).getName(), null);
        Assert.assertNotEquals("Commodity size is empty", result.get(result.size() / 2).getCommodities().size(), 0);
    }

}