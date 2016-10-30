package com.amirulzin.famapricewatch.data.parser;

/**
 *
 */

public class FAMAEndpoint {
    private static final String priceGenerationUrl = "https://sdvi2.fama.gov.my/price/direct/generate.asp?";

    public static String getPriceGenerationUrl(PriceKey key) {
        return priceGenerationUrl.concat(key.getArgKey());
    }

    enum PriceKey {
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
