package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

public abstract class AutonomousOperation extends LinearOpMode {
    public abstract Alliance getAlliance();
    public abstract BalancingStonePosition getBalancingStonePosition();

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot();
        robot.init(this);

        telemetry.addLine("Ready to go!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            robot.jewelArmLower.setPosition(Robot.JEWEL_ARM_DOWN);
            sleep(700);

            telemetry.addData("r", robot.jewelColor.red());
            telemetry.addData("g", robot.jewelColor.green());
            telemetry.addData("b", robot.jewelColor.blue());

            if (robot.jewelColor.red() == robot.jewelColor.blue()) {
                telemetry.addData("hitting", "neither");
                telemetry.update();
            } else {
                Alliance seenBallColor = (robot.jewelColor.blue() > robot.jewelColor.red() ? Alliance.BLUE : Alliance.RED);

                // if we're on red, we're looking at the right side
                // if we're on blue, we're looking at the left side

                boolean shouldHitRight = (seenBallColor == Alliance.RED && getAlliance() == Alliance.RED) || (seenBallColor == Alliance.BLUE && getAlliance() == Alliance.BLUE);

                if (shouldHitRight) {
                    telemetry.addData("hitting", "right");
                    telemetry.update();

                    sleep(300);

                    robot.leftMotors(0.4);
                    robot.rightMotors(-0.4);
                    sleep(300);
                } else {
                    telemetry.addData("hitting", "left");
                    telemetry.update();

                    sleep(300);

                    robot.leftMotors(-0.4);
                    robot.rightMotors(0.4);
                    sleep(300);
                }
            }

            robot.jewelArmLower.setPosition(Robot.JEWEL_ARM_UP);
            sleep(700);

            if (getAlliance() == Alliance.RED) {
                robot.strafeLeft(0.7);
                sleep(500);
            } else {
                robot.strafeRight(0.7);
                sleep(500);
            }

            idle();
            break;
        }
    }
}
