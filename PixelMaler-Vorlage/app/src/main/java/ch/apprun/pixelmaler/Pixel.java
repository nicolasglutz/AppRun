package ch.apprun.pixelmaler;

import android.graphics.Paint;

import java.util.Objects;

public class Pixel {
    private int xNr;
    private int yNr;
    private Paint drawPaint = new Paint();


    public Pixel(int xNr, int yNr, Paint paint) {
        this.xNr = xNr;
        this.yNr = yNr;
        this.drawPaint = paint;
    }

    public int getxNr() {
        return xNr;
    }

    public int getyNr() {
        return yNr;
    }

    public Paint getDrawPaint() {
        return drawPaint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return xNr == pixel.xNr && yNr == pixel.yNr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xNr, yNr);
    }
}
