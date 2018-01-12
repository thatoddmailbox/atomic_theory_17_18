package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

@Autonomous(name="Autonomous BLUE (close)", group="Tests")
public class AutonomousBlueClose extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.BLUE;
    } //Set Alliance to blue

    public BalancingStonePosition getBalancingStonePosition() {
        return BalancingStonePosition.CLOSE; //Set balancing stone position to close.
    }
}
