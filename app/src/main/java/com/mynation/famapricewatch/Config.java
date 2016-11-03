package com.mynation.famapricewatch;

import com.mynation.famapricewatch.data.parser.FAMAEndpoint;

/**
 *
 */

public class Config {
    public static FAMAEndpoint.PriceKey getDefaultPriceKey() {
        return FAMAEndpoint.PriceKey.RETAIL;
    }

}
