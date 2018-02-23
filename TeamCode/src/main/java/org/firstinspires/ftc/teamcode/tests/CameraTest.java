package org.firstinspires.ftc.teamcode.tests;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.opencv.OpenCVManager;
import org.firstinspires.ftc.teamcode.opencv.OpenCVView;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Autonomous(name="Camera Test", group="Tests")
public class CameraTest extends OpMode implements CameraBridgeViewBase.CvCameraViewListener2, View.OnTouchListener {

    private BaseLoaderCallback mLoaderCallback;
    private Mat mProc;

    @Override
    public void init() {
        try {
            telemetry.addData("Status", "Starting OpenCV...");
            telemetry.update();
            OpenCVManager.initOpenCV(hardwareMap);
            if (OpenCVManager.getInitStatus() != LoaderCallbackInterface.SUCCESS) {
                telemetry.addData("Status", "OpenCV failed!");
                telemetry.addData("Error", OpenCVManager.getMessageForLoaderCallbackInterface(OpenCVManager.getInitStatus()));
                telemetry.update();
                return;
            }
        } catch (InterruptedException e) {
            telemetry.addData("Status", "OpenCV failed!");
            telemetry.addData("Error", "InterruptedException");
            telemetry.update();
            e.printStackTrace();
        }

        telemetry.addData("Status", "Ready to go!");
        telemetry.update();

        OpenCVManager.attachCameraView(hardwareMap, this, this);
    }

    @Override
    public void loop() {

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mProc = new Mat();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mRgba = inputFrame.rgba();
        Imgproc.GaussianBlur(mRgba, mProc, new Size(15,15), 50);
        return mProc;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        OpenCVView openCVView = OpenCVManager.getCameraView(hardwareMap);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        String timestamp = sdf.format(new Date());
        String path = Environment.getExternalStorageDirectory().getPath() + "/sample_" + timestamp + ".jpg";
        openCVView.takePicture(path);

        Log.i("tag", "Saved image to " + path);
        Toast.makeText(hardwareMap.appContext, "Saved image to " + path, Toast.LENGTH_SHORT).show();

        return true;
    }
}
