package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;


@Autonomous(name="IMU Test", group="Tests")
public class IMUTest extends OpMode {
    BNO055IMU imu;

    @Override
    public void init() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 100);
    }

    @Override
    public void loop() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("Heading", angles.firstAngle);
        telemetry.addData("Roll", angles.secondAngle);
        telemetry.addData("Pitch", angles.thirdAngle);
        telemetry.addData("Acceleration (x)", imu.getAcceleration().xAccel);
        telemetry.addData("Acceleration (y)", imu.getAcceleration().yAccel);
        telemetry.addData("Acceleration (z)", imu.getAcceleration().zAccel);
        telemetry.addData("Velocity (x)", imu.getVelocity().xVeloc);
        telemetry.addData("Velocity (y)", imu.getVelocity().yVeloc);
        telemetry.addData("Velocity (z)", imu.getVelocity().zVeloc);
        telemetry.addData("Position (x)", imu.getPosition().x);
        telemetry.addData("Position (y)", imu.getPosition().y);
        telemetry.addData("Position (z)", imu.getPosition().z);
        telemetry.update();
    }
}
