package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public static final double JEWEL_ARM_UP = 0.9;
    public static final double JEWEL_ARM_DOWN = 0.29;
    public static final double JEWEL_ARM_DOWN_MAX = 0.225;

    public static final double GLYPH_ARM_LEFT_CLOSE = 0.2;
    public static final double GLYPH_ARM_RIGHT_CLOSE = 0.3;
    public static final double GLYPH_ARM_LEFT_OPEN = 0.5;
    public static final double GLYPH_ARM_RIGHT_OPEN = 0.6;

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    public DcMotor liftMotor;

    public Servo jewelArmLower;
    public Servo leftArmServo;
    public Servo rightArmServo;

    public Servo relicClawOver;
    public Servo relicClawClose;

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

        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        jewelArmLower = hardwareMap.servo.get("jewel arm lower");
        leftArmServo = hardwareMap.servo.get("LeftArm");
        rightArmServo = hardwareMap.servo.get("RightArm");

        relicClawOver = hardwareMap.servo.get("relicClawOver");
        relicClawClose = hardwareMap.servo.get("relicClawClose");

        relicClawClose.setPosition(0.0);

        leftArmServo.setDirection(Servo.Direction.FORWARD);
        rightArmServo.setDirection(Servo.Direction.REVERSE);

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
        //parameters.accelerationIntegrationAlgorithm = new NaiveAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
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

    public void moveDistance(int distance, double power) throws InterruptedException {
        int target = frontRight.getCurrentPosition() - distance;

        int distanceToTarget = frontRight.getCurrentPosition() - target;
        while (opModeIsActive() && Math.abs(distanceToTarget) > 1) {
            distanceToTarget = frontRight.getCurrentPosition() - target;
            int negativeFactor = (distanceToTarget > 0 ? 1 : -1);

            double factor = 1.0f;
            if (Math.abs(distanceToTarget) > (distance / 2)) {
                factor = 0.75f;
            }

            frontRight.setPower(factor*negativeFactor*power);
            frontLeft.setPower(factor*negativeFactor*power);
            backLeft.setPower(factor*negativeFactor*power);
            backRight.setPower(factor*negativeFactor*power);

            idle();

            telemetry.addData("Current position", frontRight.getCurrentPosition());
            telemetry.addData("Target position", target);
            telemetry.update();
        }

        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void strafeLeft(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void strafeRight(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    /**
     * Strafe for a distance
     * Shocking I know
     * @param distance The distance to strafe for
     * @param power Motor power, positive strafes right.
     */
    public void strafeDistance(int distance, double power) {
        int startPosition = frontRight.getCurrentPosition();

        boolean negativeTarget = false;
        if (power < 0) {
            negativeTarget = true;
        }
        int targetPosition = startPosition + (distance * (negativeTarget ? -1 : 1));
        int distanceToTarget = targetPosition - frontRight.getCurrentPosition();

        while (opModeIsActive() && Math.abs(distanceToTarget) > 10) {
            int negativeFactor = 1;

            if (distanceToTarget < 0) {
                negativeFactor = -1;
            }

            frontLeft.setPower(negativeFactor * power);
            frontRight.setPower(negativeFactor * -power);
            backLeft.setPower(negativeFactor * -power);
            backRight.setPower(negativeFactor * power);

            distanceToTarget = targetPosition - frontRight.getCurrentPosition();

            idle();

            telemetry.addData("Current position", frontRight.getCurrentPosition());
            telemetry.addData("Target position", targetPosition);
            telemetry.addData("Distance to position", distanceToTarget);
            telemetry.update();
        }

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    /**
     * Strafe in a straight manner, using the IMU for maintaining the heading
     * @param power Motor power, positive strafes right.
     * @param milliseconds Milliseconds to strafe for.
     */
    public void strafeStraight(double power, int milliseconds) {
        float startHeading = getHeading();
        long startMillis = System.currentTimeMillis();
        while (opModeIsActive() && (System.currentTimeMillis() - startMillis) < milliseconds) {
            double leftPower = power;
            double rightPower = power;

            float distanceToTarget = Math.abs(getHeading() - startHeading);
            double multiplyFactor = 1;
            if (distanceToTarget > 2) {
                multiplyFactor = 0.9;
                if (distanceToTarget > 5) {
                    multiplyFactor = 0.8;
                } else if (distanceToTarget > 10) {
                    multiplyFactor = 0.7;
                } else if (distanceToTarget > 15) {
                    multiplyFactor = 0.5;
                }

                if (getHeading() > startHeading) {
                    // we are too much to the left
                    rightPower *= multiplyFactor;
                } else {
                    // we are too much to the right
                    leftPower *= multiplyFactor;
                }
            }

            frontLeft.setPower(leftPower);
            frontRight.setPower(-rightPower);
            backLeft.setPower(-leftPower);
            backRight.setPower(rightPower);

            telemetry.addData("target", startHeading);
            telemetry.addData("current", getHeading());
            telemetry.addData("multiplyFactor", multiplyFactor);
            telemetry.update();
        }
    }

    public void glyphArms(double left, double right) {
        leftArmServo.setPosition(left);
        rightArmServo.setPosition(right);
    }

    public void lift(double power) {
        liftMotor.setPower(power);
    }

    public float getHeading() {
        return imu.getAngularOrientation().firstAngle;
    }

    public void turnToHeading(int targetHeading, double power) {
        float currentHeading = getHeading();
        boolean turnLeft = (targetHeading - currentHeading > 0 ? true : false);
        while (opModeIsActive()) {
            telemetry.addData("hdg", currentHeading);
            telemetry.update();

            double currentSpeed = power;
            float distanceTo = Math.abs(targetHeading - currentHeading);
            double minimumSpeed = 0.3f;
            double minimumLeftSpeed = (turnLeft ? minimumSpeed : -minimumSpeed);
            double minimumRightSpeed = (turnLeft ? -minimumSpeed : minimumSpeed);

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

            leftMotors(Math.max(minimumLeftSpeed, (turnLeft ? currentSpeed : -currentSpeed)));
            rightMotors(Math.max(minimumRightSpeed, (turnLeft ? -currentSpeed : currentSpeed)));

            currentHeading = getHeading();

            turnLeft = (targetHeading - currentHeading > 0 ? true : false);
            if (distanceTo < 1.5) {
                break;
            }

            idle();
        }
        leftMotors(0.0);
        rightMotors(0.0);
    }
}
