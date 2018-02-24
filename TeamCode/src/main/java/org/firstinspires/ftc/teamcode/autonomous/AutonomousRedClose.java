package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

@Autonomous(name="Autonomous RED (close)")
public class AutonomousRedClose extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.RED;
    } //Set alliance to red

    public BalancingStonePosition getBalancingStonePosition() {
        return BalancingStonePosition.CLOSE;
    } //set balancing stone position to close
}
