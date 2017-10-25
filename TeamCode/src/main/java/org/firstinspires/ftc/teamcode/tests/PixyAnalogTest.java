package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

@Autonomous(name="PixyCam Analog Test", group="Tests")
public class PixyAnalogTest extends OpMode {
    AnalogInput pixy;

    @Override
    public void init() {
        pixy = hardwareMap.analogInput.get("pixycam analog");
    }

    @Override
    public void loop() {
        telemetry.addData("Voltage", pixy.getVoltage());
        telemetry.update();
    }
}
