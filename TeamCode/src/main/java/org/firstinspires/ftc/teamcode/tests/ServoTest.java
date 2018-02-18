package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Servo Test", group="Tests")
public class ServoTest extends OpMode {
    Servo leftArm;
    Servo rightArm;

    boolean last1A = false;
    boolean last1B = false;
    boolean last2A = false;
    boolean last2B = false;

    @Override
    public void init() {
        leftArm = hardwareMap.servo.get("LeftArm");
        rightArm = hardwareMap.servo.get("RightArm");

        leftArm.setPosition(0.5);
        rightArm.setPosition(0.5);
    }

    @Override
    public void loop() {
        telemetry.addData("1 Position", leftArm.getPosition());
        telemetry.addData("2 Position", rightArm.getPosition());
        telemetry.update();

        if (gamepad1.a && !last1A) {
            leftArm.setPosition(leftArm.getPosition() + 0.05);
        } else if (gamepad1.b && !last1B) {
            leftArm.setPosition(leftArm.getPosition() - 0.05);
        }

        if (gamepad2.a && !last2A) {
            rightArm.setPosition(rightArm.getPosition() + 0.05);
        } else if (gamepad2.b && !last2B) {
            rightArm.setPosition(rightArm.getPosition() - 0.05);
        }

        last1A = gamepad1.a;
        last1B = gamepad1.b;
        last2A = gamepad2.a;
        last2B = gamepad2.b;
    }
}
