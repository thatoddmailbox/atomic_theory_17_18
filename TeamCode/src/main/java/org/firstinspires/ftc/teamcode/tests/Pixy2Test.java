package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

@Autonomous(name="PixyCam Test 2", group="Tests")
public class Pixy2Test extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        I2cDeviceSynch pixy = hardwareMap.i2cDeviceSynch.get("pixycam");
        waitForStart();
        while (opModeIsActive()) {
            pixy.engage();
            byte[] pixyData = pixy.read(0x51, 5);

            telemetry.addData("0", pixyData[0]);
            telemetry.addData("1", pixyData[1]);
            telemetry.addData("2", pixyData[2]);
            telemetry.addData("3", pixyData[3]);
            telemetry.addData("4", pixyData[4]);
            telemetry.update();
            sleep(500);
        }
    }
}
