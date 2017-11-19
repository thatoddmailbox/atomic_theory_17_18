package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public static final double PIXYCAM_THRESHOLD = (3.3/2);

    public static final double JEWEL_ARM_UP = 0.7;
    public static final double JEWEL_ARM_DOWN = 0.35;
    public static final double JEWEL_ARM_DOWN_MAX = 0.275;

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    public DcMotor liftMotor;

    public Servo jewelArmLower;
    public Servo leftArmServo;
    public Servo rightArmServo;

    public AnalogInput pixycamAnalog;

    public BNO055IMU imu;

    public ColorSensor jewelColor;

    private LinearOpMode opMode;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public void init(LinearOpMode o) {
        opMode = o;
        hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;

        frontRight = hardwareMap.dcMotor.get("FrontRight");
        frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        backRight = hardwareMap.dcMotor.get("BackRight");
        backLeft = hardwareMap.dcMotor.get("BackLeft");
        liftMotor = hardwareMap.dcMotor.get("Lift");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);

        jewelArmLower = hardwareMap.servo.get("jewel arm lower");
        leftArmServo = hardwareMap.servo.get("LeftArm");
        rightArmServo = hardwareMap.servo.get("RightArm");

        leftArmServo.setDirection(Servo.Direction.FORWARD);
        rightArmServo.setDirection(Servo.Direction.REVERSE);

        pixycamAnalog = hardwareMap.analogInput.get("pixycam analog");

        jewelColor = hardwareMap.colorSensor.get("jewel color");

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu 1");
        imu.initialize(parameters);
    }

    public void idle() {
        opMode.idle();
    }

    public boolean opModeIsActive() {
        return opMode.opModeIsActive();
    }

    public void leftMotors(double power) {
        frontLeft.setPower(power);
        backLeft.setPower(power);
    }

    public void rightMotors(double power) {
        frontRight.setPower(power);
        backRight.setPower(power);
    }

    public void straightDrive(double leftPower, double rightPower) {
        leftMotors(leftPower);
        rightMotors(rightPower);
    }

    public void strafeLeft(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public void strafeRight(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public double getHeading() {
        return imu.getAngularOrientation().firstAngle;
    }

    public void turnToHeading(int targetHeading, double power) {
        double currentHeading = getHeading();
        boolean turnLeft = (targetHeading - currentHeading > 0 ? true : false);
        while (opModeIsActive()) {
            telemetry.addData("hdg", currentHeading);
            telemetry.update();

            double currentSpeed = power;
            double distanceTo = Math.abs(targetHeading - currentHeading);
            double minimumSpeed = 0.2f;
            double minimumLeftSpeed = (turnLeft ? -minimumSpeed : minimumSpeed);
            double minimumRightSpeed = (turnLeft ? minimumSpeed : -minimumSpeed);

            if (distanceTo < 10) {
                currentSpeed *= 0.20;
                currentSpeed = Math.min(currentSpeed, 0.7f);
            } else if (distanceTo < 20) {
                currentSpeed *= 0.30;
                currentSpeed = Math.min(currentSpeed, 0.75f);
            } else if (distanceTo < 30) {
                currentSpeed *= 0.40;
                currentSpeed = Math.min(currentSpeed, 0.8f);
            }

            leftMotors(Math.max(minimumLeftSpeed, (turnLeft ? -currentSpeed : currentSpeed)));
            rightMotors(Math.max(minimumRightSpeed, (turnLeft ? currentSpeed : -currentSpeed)));

            currentHeading = getHeading();

            turnLeft = (targetHeading - currentHeading > 0 ? true : false);
            if (targetHeading == currentHeading) {
                break;
            }

            idle();
        }
        leftMotors(0.0);
        rightMotors(0.0);
    }
}
