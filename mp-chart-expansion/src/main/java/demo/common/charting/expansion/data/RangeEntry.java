package demo.common.charting.expansion.data;

import com.github.mikephil.charting.data.BarEntry;

public class RangeEntry extends BarEntry {

    private float y2;

    public RangeEntry(float x, float y) {
        super(x, y);
    }

    public RangeEntry(float x, float y, float y2) {
        super(x, y);
        this.y2 = y2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }
}
