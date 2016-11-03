package com.mynation.famapricewatch.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mynation.famapricewatch.data.Commodity;

import java.util.List;

/**
 * Pure visual logic
 */

public class CommodityUIPresenter {

    private boolean measureInHundreds = false;

    private String sanitizeMeasureUnits(final String s) {
        if (s == null || s.isEmpty()) return null;

        if (s.equalsIgnoreCase("KILOGRAM")) {
            return "1 KG";
        }

        if (s.equalsIgnoreCase("100 KILOGRAM")) {
            return measureInHundreds ? "100 KG" : "1 KG";
        }

        if (s.equalsIgnoreCase("BIJI")) {
            return "1 BIJI";
        }

        if (s.equalsIgnoreCase("100 BIJI")) {
            return measureInHundreds ? s : "1 BIJI";
        }

        return s;
    }

    public <T extends TextView> void bind(final Commodity commodity, final T title, final T unit, final T grade, final T low, final T avg, final T high) {

        title.setText(commodity.getName());

        unit.setText(sanitizeMeasureUnits(commodity.getMeasureUnit()));
        filterGrade(grade, commodity.getGrade());

        low.setText(commodity.getPriceLow());
        avg.setText(commodity.getPriceAvg());
        high.setText(commodity.getPriceHigh());

    }

    private <T extends TextView> void filterGrade(T textView, String commodityString) {
        if (commodityString == null || commodityString.equalsIgnoreCase("F.A.Q"))
            textView.setVisibility(View.GONE);
        else {
            applyVisibilityIndiscriminately(textView, commodityString);
        }
    }

    private <T extends TextView> void applyVisibilityIndiscriminately(T textView, String commodityString) {
        if (textView.getVisibility() == View.GONE)
            textView.setVisibility(View.VISIBLE);
        textView.setText(commodityString);
    }

    public <RV extends RecyclerView.Adapter> void toggleUnitPerHundred(boolean enable, RV adapter) {
        measureInHundreds = !enable;
        adapter.notifyDataSetChanged();
    }

    public String getInitialStateId(List<String> newStateIds) {
        return newStateIds.get(newStateIds.size() / 2);
    }

}
