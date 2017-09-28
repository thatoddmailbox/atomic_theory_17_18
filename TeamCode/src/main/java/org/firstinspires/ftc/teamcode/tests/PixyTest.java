package org.firstinspires.ftc.teamcode.tests;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.pixy.PixyCam;
import org.firstinspires.ftc.teamcode.pixy.PixyObjectBlock;

import java.util.List;

@Autonomous(name="PixyCam Test", group="Tests")
public class PixyTest extends OpMode {
    PixyCam pixy;

    @Override
    public void init() {
        pixy = new PixyCam(hardwareMap.i2cDeviceSynch.get("pixycam"), I2cAddr.create7bit(0x54));
    }

    @Override
    public void loop() {
        pixy.fetchFrame();
        List<PixyObjectBlock> frame = pixy.getFrame();
        telemetry.addLine("hi");
        telemetry.update();
    }
}
