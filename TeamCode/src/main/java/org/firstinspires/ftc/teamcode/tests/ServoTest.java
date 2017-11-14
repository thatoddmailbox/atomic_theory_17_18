package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Servo Test", group="Tests")
public class ServoTest extends OpMode {
    Servo servo;

    Servo jewelArmLower;
    Servo jewelArmTwist;

    boolean lastA = false;
    boolean lastB = false;
    boolean lastY = false;

    @Override
    public void init() {
        jewelArmLower = hardwareMap.servo.get("jewel arm lower");
        jewelArmTwist = hardwareMap.servo.get("jewel arm twist");

        servo = jewelArmLower;

        if (servo == jewelArmLower) {
            servo.setPosition(0.5);
        } else {
            servo.setPosition(0.0);
        }
    }

    @Override
    public void loop() {
        telemetry.addData("Position", servo.getPosition());
        telemetry.addData("Servo", (servo == jewelArmLower ? "lower" : "twist"));
        telemetry.update();

        if (gamepad1.a && !lastA) {
            servo.setPosition(servo.getPosition() + 0.05);
        } else if (gamepad1.b && !lastB) {
            servo.setPosition(servo.getPosition() - 0.05);
        }

        if (gamepad1.y && !lastY) {
            if (servo == jewelArmLower) {
                servo = jewelArmTwist;
            } else {
                servo = jewelArmLower;
            }
        }

        lastA = gamepad1.a;
        lastB = gamepad1.b;
        lastY = gamepad1.y;
    }
}
