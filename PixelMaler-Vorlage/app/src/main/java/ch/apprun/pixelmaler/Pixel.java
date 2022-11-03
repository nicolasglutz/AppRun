package ch.apprun.pixelmaler;

import java.util.Objects;

public class Pixel {
    private int xNr;
    private int yNr;

    public Pixel(int xNr, int yNr) {
        this.xNr = xNr;
        this.yNr = yNr;
    }

    public int getxNr() {
        return xNr;
    }

    public int getyNr() {
        return yNr;
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
