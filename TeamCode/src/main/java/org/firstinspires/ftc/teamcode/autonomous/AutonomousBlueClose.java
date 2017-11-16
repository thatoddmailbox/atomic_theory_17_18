package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

@Autonomous(name="Autonomous BLUE (close)", group="Tests")
public class AutonomousBlueClose extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }

    public BalancingStonePosition getBalancingStonePosition() {
        return BalancingStonePosition.CLOSE;
    }
}
