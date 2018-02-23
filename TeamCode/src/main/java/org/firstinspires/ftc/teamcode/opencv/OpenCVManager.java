package org.firstinspires.ftc.teamcode.opencv;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.R;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.concurrent.CountDownLatch;

public class OpenCVManager {
    private static boolean inited = false;
    private static int initStatus;

    public static String getMessageForLoaderCallbackInterface(int status) {
        switch(status) {
            case LoaderCallbackInterface.SUCCESS:
                return "OpenCV Manager Connected";
            case LoaderCallbackInterface.INIT_FAILED:
                return "Init Failed";
            case LoaderCallbackInterface.INSTALL_CANCELED:
                return "Install Cancelled";
            case LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION:
                return "Incompatible Manager Version";
            case LoaderCallbackInterface.MARKET_ERROR:
                return "Market Error";
            default:
                return "OpenCV Manager Install";
        }
    }

    public static int getInitStatus() {
        return initStatus;
    }

    public static int initOpenCV(HardwareMap hardwareMap) throws InterruptedException {
        if (inited) {
            return LoaderCallbackInterface.SUCCESS;
        }

        final CountDownLatch latch = new CountDownLatch(1);

        BaseLoaderCallback loaderCallback = new BaseLoaderCallback(hardwareMap.appContext) {
            @Override
            public void onManagerConnected(int status) {
                initStatus = status;

                if (status == LoaderCallbackInterface.SUCCESS) {
                    inited = true;
                } else {
                    inited = false;
                    if (status != LoaderCallbackInterface.INIT_FAILED && status != LoaderCallbackInterface.INSTALL_CANCELED && status != LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION && status != LoaderCallbackInterface.MARKET_ERROR) {
                        super.onManagerConnected(status);
                    }
                }

                latch.countDown();
            }
        };

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, hardwareMap.appContext, loaderCallback);
        latch.await();

        return initStatus;
    }

    public static void attachCameraView(final HardwareMap hardwareMap, final CameraBridgeViewBase.CvCameraViewListener2 listener, final View.OnTouchListener touchListener) {
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final ViewGroup relativeLayout = (ViewGroup)((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                LayoutInflater vi = ((Activity) hardwareMap.appContext).getLayoutInflater();
                View v = vi.inflate(R.layout.opencv_view, null);
                relativeLayout.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                OpenCVView openCVView = (OpenCVView) v.findViewById(R.id.opencv_view);
                openCVView.setVisibility(View.VISIBLE);
                openCVView.enableView();
                openCVView.setCvCameraViewListener(listener);
                openCVView.setOnTouchListener(touchListener);
            }
        });
    }

    public static OpenCVView getCameraView(final HardwareMap hardwareMap) {
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final ViewGroup relativeLayout = (ViewGroup)((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
        return (OpenCVView)relativeLayout.findViewById(R.id.opencv_view_container).findViewById(R.id.opencv_view);
    }
}
