//package io.github.fs314.KsParser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeystrokeData{

    @SerializedName("KeyReleaseDelay")
    @Expose
    private Double keyReleaseDelay;
    @SerializedName("primaryCode")
    @Expose
    private Integer primaryCode;
    @SerializedName("PressureOnPress")
    @Expose
    private Double pressureOnPress;
    @SerializedName("XOnPress")
    @Expose
    private Integer xOnPress;
    @SerializedName("YOnPress")
    @Expose
    private Integer yOnPress;
    @SerializedName("LinearAccelerationOnPress")
    @Expose
    private LinearAccelerationOnPress linearAccelerationOnPress;
    @SerializedName("RotationVectorOnPress")
    @Expose
    private RotationVectorOnPress rotationVectorOnPress;
    @SerializedName("KeyPressDelay")
    @Expose
    private Double keyPressDelay;
    @SerializedName("PressureOnRelease")
    @Expose
    private Double pressureOnRelease;
    @SerializedName("XOnRelease")
    @Expose
    private Integer xOnRelease;
    @SerializedName("YOnRelease")
    @Expose
    private Integer yOnRelease;
    @SerializedName("LinearAccelerationOnRelease")
    @Expose
    private LinearAccelerationOnRelease linearAccelerationOnRelease;
    @SerializedName("RotationVectorOnRelease")
    @Expose
    private RotationVectorOnRelease rotationVectorOnRelease;
    @SerializedName("vectorCoord")
    @Expose
    private String vectorCoord;

    public Double getKeyReleaseDelay() {
        return keyReleaseDelay;
    }

    public void setKeyReleaseDelay(Double keyReleaseDelay) {
        this.keyReleaseDelay = keyReleaseDelay;
    }

    public Integer getPrimaryCode() {
        return primaryCode;
    }

    public void setPrimaryCode(Integer primaryCode) {
        this.primaryCode = primaryCode;
    }

    public Double getPressureOnPress() {
        return pressureOnPress;
    }

    public void setPressureOnPress(Double pressureOnPress) {
        this.pressureOnPress = pressureOnPress;
    }

    public Integer getXOnPress() {
        return xOnPress;
    }

    public void setXOnPress(Integer xOnPress) {
        this.xOnPress = xOnPress;
    }

    public Integer getYOnPress() {
        return yOnPress;
    }

    public void setYOnPress(Integer yOnPress) {
        this.yOnPress = yOnPress;
    }

    public LinearAccelerationOnPress getLinearAccelerationOnPress() {
        return linearAccelerationOnPress;
    }

    public void setLinearAccelerationOnPress(LinearAccelerationOnPress linearAccelerationOnPress) {
        this.linearAccelerationOnPress = linearAccelerationOnPress;
    }

    public RotationVectorOnPress getRotationVectorOnPress() {
        return rotationVectorOnPress;
    }

    public void setRotationVectorOnPress(RotationVectorOnPress rotationVectorOnPress) {
        this.rotationVectorOnPress = rotationVectorOnPress;
    }

    public Double getKeyPressDelay() {
        return keyPressDelay;
    }

    public void setKeyPressDelay(Double keyPressDelay) {
        this.keyPressDelay = keyPressDelay;
    }

    public Double getPressureOnRelease() {
        return pressureOnRelease;
    }

    public void setPressureOnRelease(Double pressureOnRelease) {
        this.pressureOnRelease = pressureOnRelease;
    }

    public Integer getXOnRelease() {
        return xOnRelease;
    }

    public void setXOnRelease(Integer xOnRelease) {
        this.xOnRelease = xOnRelease;
    }

    public Integer getYOnRelease() {
        return yOnRelease;
    }

    public void setYOnRelease(Integer yOnRelease) {
        this.yOnRelease = yOnRelease;
    }

    public LinearAccelerationOnRelease getLinearAccelerationOnRelease() {
        return linearAccelerationOnRelease;
    }

    public void setLinearAccelerationOnRelease(LinearAccelerationOnRelease linearAccelerationOnRelease) {
        this.linearAccelerationOnRelease = linearAccelerationOnRelease;
    }

    public RotationVectorOnRelease getRotationVectorOnRelease() {
        return rotationVectorOnRelease;
    }

    public void setRotationVectorOnRelease(RotationVectorOnRelease rotationVectorOnRelease) {
        this.rotationVectorOnRelease = rotationVectorOnRelease;
    }

    public String getVectorCoord() {
        return vectorCoord;
    }

    public void setVectorCoord(String vectorCoord) {
        this.vectorCoord = vectorCoord;
    }

}
