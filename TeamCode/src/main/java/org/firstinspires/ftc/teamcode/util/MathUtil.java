package org.firstinspires.ftc.teamcode.util;

public class MathUtil {
    public float convertTo360Heading(float h) {
        if (h < 0) {
            return h + 180;
        } else {
            return h;
        }
    }
}
