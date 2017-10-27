package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.misc.Alliance;

@Autonomous(name="Autonomous BLUE", group="Tests")
public class AutonomousBlue extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
}
