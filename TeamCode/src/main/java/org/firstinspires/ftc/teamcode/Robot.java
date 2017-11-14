package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {
    public static final double PIXYCAM_THRESHOLD = (3.3/2);

    public Servo jewelArmLower;
    public Servo jewelArmTwist;

    public AnalogInput pixycamAnalog;

    public ColorSensor jewelColor;

    public void init(HardwareMap hardwareMap) {
        jewelArmLower = hardwareMap.servo.get("jewel arm lower");
        jewelArmTwist = hardwareMap.servo.get("jewel arm twist");

        pixycamAnalog = hardwareMap.analogInput.get("pixycam analog");

        jewelColor = hardwareMap.colorSensor.get("jewel color");
    }
}
