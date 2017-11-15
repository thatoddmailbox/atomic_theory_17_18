package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.misc.Alliance;

public abstract class AutonomousOperation extends LinearOpMode {
    public abstract Alliance getAlliance();

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot();
        robot.init(this);

        telemetry.addLine("Ready to go!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            robot.jewelArmLower.setPosition(0.3);
            robot.LiftMotor.setPower(0.5);
            sleep(300);
            robot.LiftMotor.setPower(0);
            robot.LeftArmServo.setPosition(0.6);
            robot.RightArmServo.setPosition(0.6);
            sleep(700);

            Alliance rightBallColor = (robot.jewelColor.blue() > robot.jewelColor.red() ? Alliance.BLUE : Alliance.RED);
            boolean shouldHitRight = (rightBallColor == Alliance.RED && getAlliance() == Alliance.RED) || (rightBallColor == Alliance.BLUE && getAlliance() == Alliance.BLUE);

            if (shouldHitRight) {
                telemetry.addData("hitting", "right");
                telemetry.update();
                robot.straightDrive(-0.4, 0.4);
                sleep(300);
                robot.jewelArmLower.setPosition(0.7);
                robot.straightDrive(0.3, -0.3);
            } else {
                telemetry.addData("hitting", "left");
                telemetry.update();
                robot.straightDrive(0.4, -0.4);
                sleep(300);
                robot.jewelArmLower.setPosition(0.7);
                robot.straightDrive(-0.3, 0.3);

            }
            sleep(300);
            idle();



            robot.strafeLeft(0.7);
            sleep(2800);
            robot.straightDrive(-1, 1);
            sleep(1000);
            robot.straightDrive(0.8, 0.8);
            sleep(300);

            idle();
            break;
        }
    }
}
