package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

@Autonomous(name="Autonomous Test", group="Tests")
public class AutonomousTest extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.RED;
    }

    public BalancingStonePosition getBalancingStonePosition() {
        return BalancingStonePosition.FAR;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot();
        robot.init(this);

        telemetry.addLine("Ready to go!");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            robot.strafeLeft(0.8);
            sleep(1400);
            robot.straightDrive(0, 0);
            sleep(2000);

            robot.turnToHeading(-90, 0.65);
            sleep(2000);

            robot.strafeRight(0.8);
            sleep(700);
            robot.strafeRight(0.4);
            sleep(400);

            robot.turnToHeading(-90, 0.65);
            sleep(2000);

            break;
        }
    }
}
