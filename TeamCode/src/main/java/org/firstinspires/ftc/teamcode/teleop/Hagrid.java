package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Hagrid️", group="Iterative Opmode")
public class Hagrid extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    // drivetrain
    private DcMotor FrontLeftDrive = null;
    private DcMotor FrontRightDrive = null;
    private DcMotor BackLeftDrive = null;
    private DcMotor BackRightDrive = null;

    private DcMotor LiftMotor = null;

    private Servo LeftArmServo = null;
    private Servo RightArmServo = null;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        FrontLeftDrive = hardwareMap.get(DcMotor.class, "FrontLeft");
        FrontRightDrive = hardwareMap.get(DcMotor.class, "FrontRight");
        BackLeftDrive = hardwareMap.get(DcMotor.class, "BackLeft");
        BackRightDrive = hardwareMap.get(DcMotor.class, "BackRight");

        LiftMotor = hardwareMap.get(DcMotor.class, "Lift");

        LeftArmServo = hardwareMap.get(Servo.class, "LeftArm");
        RightArmServo = hardwareMap.get(Servo.class, "RightArm");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FrontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        FrontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        BackLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        BackRightDrive.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor.setDirection(DcMotor.Direction.FORWARD);

        LeftArmServo.setDirection(Servo.Direction.FORWARD);
        RightArmServo.setDirection(Servo.Direction.REVERSE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        if (gamepad1.right_stick_x != 0) {
            FrontLeftDrive.setPower(gamepad1.right_stick_x);
            BackLeftDrive.setPower(-gamepad1.right_stick_x);
            FrontRightDrive.setPower(-gamepad1.right_stick_x);
            BackRightDrive.setPower(gamepad1.right_stick_x);
        } else if (gamepad1.right_stick_y != 0) {
            FrontLeftDrive.setPower(gamepad1.right_stick_x);
            BackLeftDrive.setPower(gamepad1.right_stick_x);
            FrontRightDrive.setPower(gamepad1.right_stick_x);
            BackRightDrive.setPower(gamepad1.right_stick_x);
        } else {
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.left_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            FrontLeftDrive.setPower(leftPower);
            FrontRightDrive.setPower(rightPower);
            BackLeftDrive.setPower(leftPower);
            BackRightDrive.setPower(rightPower);
        }

        if (gamepad1.right_trigger > 0) {
            double LeftPosition = LeftArmServo.getPosition();
            double RightPosition = RightArmServo.getPosition();
            LeftArmServo.setPosition(LeftPosition + 0.05);
            RightArmServo.setPosition(RightPosition + 0.05);
        }

        if (gamepad1.left_trigger > 0) {
            double LeftPosition = LeftArmServo.getPosition();
            double RightPosition = RightArmServo.getPosition();
            LeftArmServo.setPosition(LeftPosition - 0.05);
            RightArmServo.setPosition(RightPosition - 0.05);
        }

        if (gamepad1.a) {
            LiftMotor.setPower(0.5);
        } else if (gamepad1.b) {
            LiftMotor.setPower(-0.5);
        } else {
            LiftMotor.setPower(0.0);
        }

        telemetry.addData("Status", "Run Time: " + runtime.toString());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Status", "\uD83D\uDED1");
    }

}
