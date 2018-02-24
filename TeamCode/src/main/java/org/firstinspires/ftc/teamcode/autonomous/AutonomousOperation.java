package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.misc.Alliance;
import org.firstinspires.ftc.teamcode.misc.BalancingStonePosition;

public abstract class AutonomousOperation extends LinearOpMode {
    public abstract Alliance getAlliance();
    public abstract BalancingStonePosition getBalancingStonePosition();

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        int blueNegativeFactor = (getAlliance() == Alliance.RED ? 1 : -1);

        robot = new Robot();
        robot.init(this);

        telemetry.addLine("Starting Vuforia...");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AR+iLgj/////AAAAmS9bkSnmY0kFlliVvKlHO6srskUOSAet/+7CxNX1r58PDBPcdJ1Oez2dp8+Sou9eNRa1xwyAe8axEgE9MkAaD9JcOBlOJMBW3ThvB1/2ycv7OQga4kuIgOtQ3w5It14k9P7hVU9aVpXAZFrkoykwDNumaT08hmFk+cJtFznF0CprLnGNyQ8wuLB7hBqx5xWzt6JPEdF5Pn3eNgEyCR76MhegCvD+V5i+D+YojEUgrt7aUH7SiEQJDcr6RFilzIGjpxN9r7j7xfp7yki3D1HzidnXSavjEEzn13dHKmdu9wfdyY9+SvCTUEPDwklWiRC9Bj1rVMRR4MBbPS3m2ClknaDqKHNxIjBfPaH4MP0Tdcdg ";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);

        relicTrackables.activate();

        telemetry.addLine("Ready to go!");
        telemetry.update();

        waitForStart();

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        int vuMarkAttempt = 1;
        while(vuMark == RelicRecoveryVuMark.UNKNOWN && vuMarkAttempt <= 20) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            telemetry.addData("Cipher", "Not visible, waiting " + (((20-vuMarkAttempt))/10) + " more seconds.");
            telemetry.addData("VuMark Detection", vuMark);
            telemetry.update();
            sleep(100);
            vuMarkAttempt++;
        }
        telemetry.addData("VuMark", "%s visible", vuMark);
        telemetry.update();

        while (opModeIsActive()) {

            // grab the block
            robot.glyphArms(Robot.GLYPH_ARM_LEFT_CLOSE, Robot.GLYPH_ARM_RIGHT_CLOSE);
            sleep(500);
            robot.lift(-0.5); //lift lift
            sleep(400);
            robot.lift(0.0);

            // lower the arm
            double armPosition = Robot.JEWEL_ARM_DOWN;
            robot.jewelArmLower.setPosition(armPosition);
            sleep(1250);

            while (robot.jewelColor.red() == robot.jewelColor.blue() && armPosition > Robot.JEWEL_ARM_DOWN_MAX) {
                // try lowering the arm more
                armPosition -= 0.05;
                robot.jewelArmLower.setPosition(armPosition);
                sleep(200);
            }

            sleep(500);

            telemetry.addData("r", robot.jewelColor.red());
            telemetry.addData("g", robot.jewelColor.green());
            telemetry.addData("b", robot.jewelColor.blue());

            if (robot.jewelColor.red() == robot.jewelColor.blue()) {
                telemetry.addData("hitting", "neither");
                telemetry.update();

                robot.jewelArmLower.setPosition(Robot.JEWEL_ARM_UP);
                sleep(700);
            } else {
                Alliance seenBallColor = (robot.jewelColor.blue() > robot.jewelColor.red() ? Alliance.BLUE : Alliance.RED);

                // if we're on red, we're looking at the right side
                // if we're on blue, we're looking at the left side

                boolean shouldHitRight = (seenBallColor == Alliance.RED && getAlliance() == Alliance.RED) || (seenBallColor == Alliance.BLUE && getAlliance() == Alliance.BLUE);

                if (shouldHitRight) {
                    telemetry.addData("hitting", "right");
                    telemetry.update();

                    sleep(300);

                    robot.leftMotors(-0.3);
                    robot.rightMotors(0.3);
                    sleep(150);

                    robot.leftMotors(0);
                    robot.rightMotors(0);

                    robot.jewelArmLower.setPosition(Robot.JEWEL_ARM_UP);
                    sleep(700);

                    robot.leftMotors(0.3);
                    robot.rightMotors(-0.3);
                    sleep(150);
                } else {
                    telemetry.addData("hitting", "left");
                    telemetry.update();

                    sleep(300);

                    robot.leftMotors(0.3);
                    robot.rightMotors(-0.3);
                    sleep(150);

                    robot.leftMotors(0);
                    robot.rightMotors(0);

                    robot.jewelArmLower.setPosition(Robot.JEWEL_ARM_UP);
                    sleep(700);

                    robot.leftMotors(-0.3);
                    robot.rightMotors(0.3);
                    sleep(150);
                }

                robot.leftMotors(0);
                robot.rightMotors(0);
            }

            if (getBalancingStonePosition() == BalancingStonePosition.CLOSE) {
                robot.moveDistance(-900, 0.5);
                sleep(1000);

                robot.turnToHeading(blueNegativeFactor * -90, 0.65);
                sleep(1000);

                robot.moveDistance(-1800, 0.5);
                sleep(1000);

                robot.turnToHeading(179, 0.65);
                sleep(1000);

                robot.moveDistance(-900, 0.5);
                sleep(1000);

                if (vuMark == RelicRecoveryVuMark.LEFT) {
                    robot.turnToHeading(blueNegativeFactor * -117, 0.65);
                    sleep(1000);
                } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                    robot.turnToHeading(blueNegativeFactor * -63, 0.65);
                    sleep(1000);
                }
            } else {
                robot.moveDistance(-900, 0.5);
                sleep(1000);

                robot.strafeRight(0.65);
                sleep(1100);
                robot.straightDrive(0, 0);
                sleep(1000);

                robot.turnToHeading(blueNegativeFactor * -90, 0.65);
                sleep(1000);

                robot.moveDistance(-450, 0.5);
                sleep(1000);

                if (vuMark == RelicRecoveryVuMark.CENTER) {
                    robot.strafeRight(0.65);
                    sleep(700);
                    robot.straightDrive(0, 0);
                } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                    robot.strafeRight(0.65);
                    sleep(700);
                    robot.straightDrive(0, 0);

                    robot.turnToHeading(blueNegativeFactor * -117, 0.65);
                    sleep(1000);
                }
            }

            robot.straightDrive(-0.3, -0.3);
            sleep(1400);
            robot.straightDrive(0, 0);

            robot.glyphArms(Robot.GLYPH_ARM_LEFT_OPEN, Robot.GLYPH_ARM_RIGHT_OPEN);
            sleep(200);

            robot.straightDrive(-0.3, -0.3);
            sleep(1000);
            robot.straightDrive(0.3, 0.3);
            sleep(500);
            robot.straightDrive(-0.3, -0.3);
            sleep(1000);
            robot.straightDrive(0, 0);

            robot.straightDrive(0.3, 0.3);
            sleep(200);
            robot.straightDrive(0, 0);

            idle();
            break;
        }
    }
}
