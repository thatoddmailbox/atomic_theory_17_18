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
            //robot.moveDistance(-500, 0.5);

            robot.strafeDistance(200, 0.3);

//            robot.straightDrive(-0.6, -0.6);
//            sleep(2000);
//            robot.straightDrive(0, 0);

            sleep(5000);

            break;
        }
    }
}
