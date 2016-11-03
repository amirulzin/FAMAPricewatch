package com.mynation.famapricewatch.data.parser;

/**
 *
 */

public class FAMAEndpoint {
    private static final String priceGenerationUrl = "https://sdvi2.fama.gov.my/price/direct/generate.asp?";
    private static final String redirectChunk = "https://sdvi2.fama.gov.my/price/direct/price/";

    public static String getPriceGenerationUrl(PriceKey key) {
        return priceGenerationUrl.concat(key.getArgKey());
    }

    public static String getPreviousPriceReportUrl(String urlChunk) {
        return redirectChunk + urlChunk;
    }

    public enum PriceKey {
        FARM("levelcd=04"),
        WHOLESALE("levelcd=01"),
        RETAIL("levelcd=03");

        private final String argKey;

        PriceKey(String argKey) {
            this.argKey = argKey;
        }

        public String getArgKey() {
            return argKey;
        }
    }
}
