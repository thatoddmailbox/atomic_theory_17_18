package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

@Autonomous(name="Autonomous RED (far)")
public class AutonomousRedFar extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.RED;
    } //set alliance to red

    public BalancingStonePosition getBalancingStonePosition() {
        return BalancingStonePosition.FAR;
    } //set balancing stone position to far
}
