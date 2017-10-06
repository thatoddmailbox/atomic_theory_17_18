package org.firstinspires.ftc.teamcode.tests;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.pixy.PixyCam;
import org.firstinspires.ftc.teamcode.pixy.PixyCamLego;
import org.firstinspires.ftc.teamcode.pixy.PixyObjectBlock;

import java.util.List;

@Autonomous(name="PixyCam Test", group="Tests")
public class PixyTest extends OpMode {
    PixyCamLego pixy;

    @Override
    public void init() {
        pixy = new PixyCamLego(hardwareMap.i2cDeviceSynch.get("pixycam"));
    }

    @Override
    public void loop() {
        byte[] data = pixy._device.read(0x51, 5);
        telemetry.addData("0", data[0]);
        telemetry.addData("1", data[1]);
        telemetry.addData("2", data[2]);
        telemetry.addData("3", data[3]);
        telemetry.addData("4", data[4]);
        telemetry.update();
    }
}
