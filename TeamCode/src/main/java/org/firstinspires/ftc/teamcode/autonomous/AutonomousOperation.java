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
        robot.init(hardwareMap);

        telemetry.addLine("Ready to go!");
        telemetry.update();

        robot.jewelArmTwist.setPosition(0.5);

        waitForStart();

        while (opModeIsActive()) {
            Alliance rightBallColor = (robot.jewelColor.blue() > robot.jewelColor.red() ? Alliance.BLUE : Alliance.RED);
            boolean shouldHitRight = (rightBallColor == Alliance.RED && getAlliance() == Alliance.RED) || (rightBallColor == Alliance.BLUE && getAlliance() == Alliance.BLUE);

            robot.jewelArmLower.setPosition(0.7);
            sleep(3000);

            if (shouldHitRight) {
                robot.jewelArmTwist.setPosition(0.7);
            } else {
                robot.jewelArmTwist.setPosition(0.3);
            }

            sleep(1000);

            robot.jewelArmLower.setPosition(0.4);
            sleep(1000);

            idle();

            break;
        }
    }
}
