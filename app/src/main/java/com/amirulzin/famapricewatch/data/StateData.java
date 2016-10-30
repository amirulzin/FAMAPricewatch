package com.amirulzin.famapricewatch.data;

import java.util.ArrayList;

/**
 *
 */

public class StateData {
    String centreName;
    ArrayList<Commodity> commodities;

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public ArrayList<Commodity> getCommodities() {
        return commodities;
    }

    public void setCommodities(ArrayList<Commodity> commodities) {
        this.commodities = commodities;
    }

    @Override
    public String toString() {
        return "StateData{" +
                "centreName='" + centreName + '\'' +
                ", commodities=" + commodities +
                '}';
    }
}
