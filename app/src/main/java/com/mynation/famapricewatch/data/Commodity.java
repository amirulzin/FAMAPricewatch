package com.mynation.famapricewatch.data;

/**
 * Base data model
 */
//TODO: Utilize autovalue

public class Commodity {
    String name;
    String grade;
    String measureUnit;
    String priceHigh;
    String priceAvg;
    String priceLow;

    public Commodity(String name, String grade, String measureUnit, String priceHigh, String centsAvg, String priceLow) {
        this.name = name;
        this.grade = grade;
        this.measureUnit = measureUnit;
        this.priceHigh = priceHigh;
        this.priceAvg = centsAvg;
        this.priceLow = priceLow;
    }

    public String getPriceHigh() {
        return priceHigh;
    }

    public String getPriceAvg() {
        return priceAvg;
    }

    public String getPriceLow() {
        return priceLow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Commodity commodity = (Commodity) o;

        if (getName() != null ? !getName().equals(commodity.getName()) : commodity.getName() != null)
            return false;
        if (getGrade() != null ? !getGrade().equals(commodity.getGrade()) : commodity.getGrade() != null)
            return false;
        if (getMeasureUnit() != null ? !getMeasureUnit().equals(commodity.getMeasureUnit()) : commodity.getMeasureUnit() != null)
            return false;
        if (getPriceHigh() != null ? !getPriceHigh().equals(commodity.getPriceHigh()) : commodity.getPriceHigh() != null)
            return false;
        if (getPriceAvg() != null ? !getPriceAvg().equals(commodity.getPriceAvg()) : commodity.getPriceAvg() != null)
            return false;
        return getPriceLow() != null ? getPriceLow().equals(commodity.getPriceLow()) : commodity.getPriceLow() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getGrade() != null ? getGrade().hashCode() : 0);
        result = 31 * result + (getMeasureUnit() != null ? getMeasureUnit().hashCode() : 0);
        result = 31 * result + (getPriceHigh() != null ? getPriceHigh().hashCode() : 0);
        result = 31 * result + (getPriceAvg() != null ? getPriceAvg().hashCode() : 0);
        result = 31 * result + (getPriceLow() != null ? getPriceLow().hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", measureUnit='" + measureUnit + '\'' +
                ", priceHigh='" + priceHigh + '\'' +
                ", priceAvg='" + priceAvg + '\'' +
                ", priceLow='" + priceLow + '\'' +
                '}';
    }
}
