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

        robot.jewelArmLower.setPosition(1.0);

        waitForStart();

        while (opModeIsActive()) {
            boolean redJewelOnRight = (robot.pixycamAnalog.getVoltage() > Robot.PIXYCAM_THRESHOLD);

            robot.jewelArmLower.setPosition(0.0);

            if (redJewelOnRight) {
                robot.jewelArmTwist.setPosition(0.0);
            } else {
                robot.jewelArmTwist.setPosition(1.0);
            }

            telemetry.addData("Red jewel on", (redJewelOnRight ? "right" : "left"));
            telemetry.update();

            idle();

            break;
        }
    }
}
