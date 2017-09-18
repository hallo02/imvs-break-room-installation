package ch.fhnw.imvs.automation.java;

import java.awt.Dimension;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

public class UNUSED_CamRecognition implements WebcamMotionListener{
	
	int i = 0;
	
	public void getWebcam(){
		Webcam webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}
	}
	public UNUSED_CamRecognition() {
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(176, 144));
		WebcamMotionDetector detector = new WebcamMotionDetector(webcam);
		detector.setInterval(1000); // one check per 500 ms
		detector.setAreaThreshold(95);
		detector.addMotionListener(this);
		detector.start();
		System.out.println(detector.getWebcam().getViewSize());
	}

	@Override
	public void motionDetected(WebcamMotionEvent wme) {
		i++;
		//getWebcam();
		System.out.println("Detected motion I " + i);
		
	}
	
	

}
