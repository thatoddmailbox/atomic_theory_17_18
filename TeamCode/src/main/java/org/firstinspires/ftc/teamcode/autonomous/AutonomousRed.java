package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.misc.Alliance;

@Autonomous(name="Autonomous RED", group="Tests")
public class AutonomousRed extends AutonomousOperation {
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
