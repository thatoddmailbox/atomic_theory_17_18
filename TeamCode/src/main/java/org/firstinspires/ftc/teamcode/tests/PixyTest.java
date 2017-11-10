package org.firstinspires.ftc.teamcode.tests;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.pixy.PixyCam;
import org.firstinspires.ftc.teamcode.pixy.PixyCamLego;
import org.firstinspires.ftc.teamcode.pixy.PixyObjectBlock;

import java.util.List;

@Autonomous(name="PixyCam Test", group="Tests")
public class PixyTest extends LinearOpMode {
    PixyCam pixy;

    @Override
    public void runOpMode() throws InterruptedException {
        pixy = new PixyCam(hardwareMap.i2cDeviceSynch.get("pixycam front"), I2cAddr.create7bit(0x54));
        waitForStart();
        while (opModeIsActive()) {
            boolean result = pixy.fetchFrame();
            List<PixyObjectBlock> blocks1 = pixy.getFrame();
            List<PixyObjectBlock> blocks2 = pixy.getFrame();
            List<PixyObjectBlock> blocks3 = pixy.getFrame();
            List<PixyObjectBlock> blocks4 = pixy.getFrame();
            List<PixyObjectBlock> blocks5 = pixy.getFrame();
            telemetry.addData("blocks", blocks1.size());
            telemetry.update();
            sleep(200);
        }
    }
}
