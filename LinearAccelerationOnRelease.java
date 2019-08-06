//package io.github.fs314.KsParser;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinearAccelerationOnRelease {

    @SerializedName("x")
    @Expose
    private List<Double> x = null;
    @SerializedName("y")
    @Expose
    private List<Double> y = null;
    @SerializedName("z")
    @Expose
    private List<Double> z = null;

    public List<Double> getX() {
        return x;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public List<Double> getY() {
        return y;
    }

    public void setY(List<Double> y) {
        this.y = y;
    }

    public List<Double> getZ() {
        return z;
    }

    public void setZ(List<Double> z) {
        this.z = z;
    }

}
