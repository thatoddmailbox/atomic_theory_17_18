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
            robot.jewelArmLower.setPosition(0.325);
            sleep(1000);

            Alliance rightBallColor = (robot.jewelColor.blue() > robot.jewelColor.red() ? Alliance.BLUE : Alliance.RED);
            boolean shouldHitRight = (rightBallColor == Alliance.RED && getAlliance() == Alliance.RED) || (rightBallColor == Alliance.BLUE && getAlliance() == Alliance.BLUE);

            if (shouldHitRight) {
                telemetry.addData("hitting", "right");
                telemetry.update();
                robot.straightDrive(-0.4, 0.4);
            } else {
                telemetry.addData("hitting", "left");
                telemetry.update();
                robot.straightDrive(0.4, -0.4);
            }
            sleep(1000);

            robot.jewelArmLower.setPosition(0.7);
            sleep(3000);


            robot.turnToHeading(0, 0.4);

            idle();

            break;
        }
    }
}
