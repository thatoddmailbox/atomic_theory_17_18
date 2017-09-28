package org.firstinspires.ftc.teamcode.pixy;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.misc.Utils;
import org.firstinspires.ftc.teamcode.sensors.Sensor;

public class PixyCam extends Sensor {
    public I2cDeviceSynch _device;
    public I2cAddr _address;

    public PixyCam(I2cDeviceSynch device, I2cAddr address) {
        _device = device;
        _address = address;

        if (address != null) {
            _device.setI2cAddress(address);
        }

        _device.engage();
    }

    @Override
    public boolean ping() {
        return true;
    }

    @Override
    public String name() {
        return "PixyCam";
    }

    @Override
    public String uniqueName() {
        return name() + " @ " + Utils.intToHexString(_address.get8Bit());
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }
}
