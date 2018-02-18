package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot;

@TeleOp(name="Babybot", group="Iterative Opmode")
public class Babybot extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    private DcMotor relicMotor = null;

    private DcMotor liftMotor = null;

    private Servo rightArmServo = null;
    private Servo leftArmServo = null;
    private Servo jewelArmServo = null;

    private Servo relicClawOver = null;
    private Servo relicClawClose = null;

    private double driveSpeedMultiplier = 0.4;
    private final double strafeSpeedMultiplier = 0.9;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // initialize the motors and servos
        frontLeftMotor = hardwareMap.get(DcMotor.class, "FrontLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FrontRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BackLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "BackRight");

        relicMotor = hardwareMap.get(DcMotor.class, "RelicMotor");

        liftMotor = hardwareMap.get(DcMotor.class, "Lift");

        rightArmServo = hardwareMap.get(Servo.class, "LeftArm");
        leftArmServo = hardwareMap.get(Servo.class, "RightArm");
        jewelArmServo = hardwareMap.get(Servo.class, "jewel arm lower");

        relicClawOver = hardwareMap.get(Servo.class, "relicClawOver");
        relicClawClose = hardwareMap.get(Servo.class, "relicClawClose");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);

        relicMotor.setDirection(DcMotor.Direction.REVERSE);

        rightArmServo.setDirection(Servo.Direction.FORWARD);
        leftArmServo.setDirection(Servo.Direction.REVERSE);
        jewelArmServo.setDirection(Servo.Direction.FORWARD);
        jewelArmServo.setPosition(Robot.JEWEL_ARM_UP);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        relicMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        relicClawOver.setDirection(Servo.Direction.FORWARD);
        relicClawClose.setDirection(Servo.Direction.FORWARD);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // If the controls are in strafing position
        if (gamepad1.right_stick_x < -0.2 || gamepad1.right_stick_x > 0.2) {
            telemetry.addData("Drive mode", "strafe");

            // get the right game pad stick's direction
            double direction = gamepad1.right_stick_x;

            /*
             * strafing is weird...
             * a good manual on how to turn the wheels is here: https://goo.gl/UW863x
             * BUT THAT MANUAL IS FOR FRC! PLEASE DISREGARD ALL CODE ON THAT SITE!
             * JUST LOOK AT THE TOP, ONCE YOU GET TO CODE, STOP READING.
             */

            // set the motors
            frontLeftMotor.setPower(direction* strafeSpeedMultiplier);
            backLeftMotor.setPower(-direction* strafeSpeedMultiplier);
            frontRightMotor.setPower(-direction* strafeSpeedMultiplier);
            backRightMotor.setPower(direction* strafeSpeedMultiplier);
        } else {
            telemetry.addData("Drive mode", "normal");

            double leftPower;
            double rightPower;

            double drive = 0;
            if (gamepad1.dpad_up) {
                drive = 1;
            } else if (gamepad1.dpad_down) {
                drive = -1;
            }

            double turn = 0;
            if (gamepad1.dpad_right) {
                turn = 1;
            } else if (gamepad1.dpad_left) {
                turn = -1;
            }

            // restrict the left and right power to be
            // between -1 and 1
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            frontLeftMotor.setPower(leftPower* driveSpeedMultiplier);
            frontRightMotor.setPower(rightPower* driveSpeedMultiplier);
            backLeftMotor.setPower(leftPower* driveSpeedMultiplier);
            backRightMotor.setPower(rightPower* driveSpeedMultiplier);
        }

        /*
         * LIFT CODE:
         */

        if (gamepad2.dpad_up) {
            liftMotor.setPower(-0.5);
        } else if (gamepad2.dpad_down) {
            liftMotor.setPower(0.5);
        } else {
            liftMotor.setPower(0.0);
        }

        /*
         * ARM CODE:
         */

        if (gamepad2.a) { //closed all the way
            rightArmServo.setPosition(0.425);
            leftArmServo.setPosition(0.3);
        }  else if (gamepad2.y) { // both open
            rightArmServo.setPosition(0.55);
            leftArmServo.setPosition(0.6);
        } else if (gamepad2.b) { // both open
            rightArmServo.setPosition(0.73);
            leftArmServo.setPosition(0.65); //lower = closer
        }



        /*
         * RELIC CODE:
         */

        if(gamepad2.dpad_left) {
            relicMotor.setPower(0.75);
        } else if(gamepad2.dpad_right) {
            relicMotor.setPower(-0.75);
        }
        else {
            relicMotor.setPower(0.0);
        }

        /*
         * JEWEL ARM CODE:
         */

        if (gamepad2.right_bumper) {
            double position = jewelArmServo.getPosition();
            position += 0.1;
            position = Math.max(position, 1.0);
            jewelArmServo.setPosition(position);
        } else if (gamepad2.left_bumper) {
            double position = jewelArmServo.getPosition();
            position -= 0.1;
            position = Math.min(position, 0.0);
            jewelArmServo.setPosition(position);
        }

        /*
         * RELIC ARM CODE: // servo names are opposite!!! DEAL
         */

        //carisa worry:
        if (gamepad1.x) { //open grabber
            relicClawOver.setPosition(1.0);

        } else if (gamepad1.b) { // closed grabber
            relicClawOver.setPosition(0.0);
        }

        //stop worrying
        if (gamepad1.right_bumper) { //close
            relicClawClose.setPosition(1.0);
        } else if (gamepad1.left_bumper) { // all the way open
            relicClawClose.setPosition(0.0);
        }

        /*
         * SPEED MULTIPLIER CODE:
         */
        if (gamepad1.y) {
            driveSpeedMultiplier = 0.8;
        } else if (gamepad1.a) {
            driveSpeedMultiplier = 0.4;
        }

        //worry
        if (gamepad1.right_trigger > 0.5) { //releasing relic
            relicClawOver.setPosition(0.75);
            relicClawClose.setPosition(0.0);
        }
        //stop worry

        /*
         * MORE ARM CODE:
         */
        if (gamepad2.right_trigger > 0.5) {
            rightArmServo.setPosition(0.73);
            leftArmServo.setPosition(0.75);
        }
        if (gamepad2.left_trigger > 0.5) {
            rightArmServo.setPosition(0.65);
            leftArmServo.setPosition(0.5);
        }

        telemetry.addData("Speed", driveSpeedMultiplier);
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status", "\uD83D\uDED1");
    }

}
