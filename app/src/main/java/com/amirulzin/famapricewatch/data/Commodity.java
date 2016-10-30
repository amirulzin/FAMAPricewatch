package com.amirulzin.famapricewatch.data;

/**
 * Base data model
 */
//TODO: Utilize autovalue

public class Commodity {
    String name;
    String grade;
    String measureUnit;
    int centsHigh;
    int centsAvg;
    int centsLow;

    public Commodity(String name, String grade, String measureUnit, int centsHigh, int centsAvg, int centsLow) {
        this.name = name;
        this.grade = grade;
        this.measureUnit = measureUnit;
        this.centsHigh = centsHigh;
        this.centsAvg = centsAvg;
        this.centsLow = centsLow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Commodity commodity = (Commodity) o;

        if (getCentsHigh() != commodity.getCentsHigh()) return false;
        if (getCentsAvg() != commodity.getCentsAvg()) return false;
        if (getCentsLow() != commodity.getCentsLow()) return false;
        if (getName() != null ? !getName().equals(commodity.getName()) : commodity.getName() != null)
            return false;
        if (getGrade() != null ? !getGrade().equals(commodity.getGrade()) : commodity.getGrade() != null)
            return false;
        return getMeasureUnit() != null ? getMeasureUnit().equals(commodity.getMeasureUnit()) : commodity.getMeasureUnit() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getGrade() != null ? getGrade().hashCode() : 0);
        result = 31 * result + (getMeasureUnit() != null ? getMeasureUnit().hashCode() : 0);
        result = 31 * result + getCentsHigh();
        result = 31 * result + getCentsAvg();
        result = 31 * result + getCentsLow();
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

    public int getCentsHigh() {
        return centsHigh;
    }

    public int getCentsAvg() {
        return centsAvg;
    }

    public int getCentsLow() {
        return centsLow;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", measureUnit='" + measureUnit + '\'' +
                ", centsHigh=" + centsHigh +
                ", centsAvg=" + centsAvg +
                ", centsLow=" + centsLow +
                '}';
    }
}
