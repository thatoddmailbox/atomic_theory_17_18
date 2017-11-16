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

    public void straightDrive(double leftPower, double rightPower) {
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);

        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
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
        double currentHeading = Math.round(getHeading());
        while (currentHeading != targetHeading) {
            currentHeading = Math.round(getHeading());

            if (targetHeading > currentHeading) {
                straightDrive(power * -1, power * 1);
            } else {
                straightDrive(power * 1, power * -1);
            }

            telemetry.addData("heading", currentHeading);
            telemetry.update();

            idle();
        }
        straightDrive(0, 0);
    }
}
