package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

@Autonomous(name="PixyCam Test 3", group="Tests")
public class Pixy3Test extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        I2cDeviceSynch pixy = hardwareMap.i2cDeviceSynch.get("pixycam front");
        pixy.setI2cAddress(I2cAddr.create7bit(0x54));
        waitForStart();
        while (opModeIsActive()) {
            pixy.engage();
            byte[] pixyData = pixy.read(0, 100);

            telemetry.update();
            sleep(500);
        }
    }
}
