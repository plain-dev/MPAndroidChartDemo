package demo.android.mpchart.entity;

public class ChartData {

    private String YVal1;

    private String YVal2;

    private String XVal;

    public ChartData(String YVal1, String XVal) {
        this.YVal1 = YVal1;
        this.XVal = XVal;
    }

    public ChartData(String YVal1, String YVal2, String XVal) {
        this.YVal1 = YVal1;
        this.YVal2 = YVal2;
        this.XVal = XVal;
    }

    public String getYVal1() {
        return YVal1;
    }

    public void setYVal1(String YVal1) {
        this.YVal1 = YVal1;
    }

    public String getYVal2() {
        return YVal2;
    }

    public void setYVal2(String YVal2) {
        this.YVal2 = YVal2;
    }

    public String getXVal() {
        return XVal;
    }

    public void setXVal(String XVal) {
        this.XVal = XVal;
    }
}
