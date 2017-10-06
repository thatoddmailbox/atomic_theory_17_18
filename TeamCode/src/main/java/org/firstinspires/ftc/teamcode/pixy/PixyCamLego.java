package org.firstinspires.ftc.teamcode.pixy;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;

import org.firstinspires.ftc.teamcode.misc.Utils;
import org.firstinspires.ftc.teamcode.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

public class PixyCamLego extends Sensor {
    // i2c stuff
    public I2cDeviceSynch _device;

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public String name() {
        return "PixyCam (LEGO I2C)";
    }

    @Override
    public String uniqueName() {
        return name();
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    public PixyCamLego(I2cDeviceSynch device) {
        _device = device;

        //_device.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));

        _device.engage();
    }
}
