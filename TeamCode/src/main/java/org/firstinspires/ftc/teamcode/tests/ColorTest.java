package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Color Test", group="Tests")
public class ColorTest extends OpMode {
    ColorSensor color;

    @Override
    public void init() {
        color = hardwareMap.colorSensor.get("jewel color");
    }

    @Override
    public void loop() {
        telemetry.addData("R", color.red());
        telemetry.addData("G", color.green());
        telemetry.addData("B", color.blue());
        telemetry.update();
    }
}
