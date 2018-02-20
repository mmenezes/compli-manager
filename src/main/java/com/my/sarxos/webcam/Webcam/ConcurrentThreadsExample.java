package com.my.sarxos.webcam.Webcam;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import com.github.sarxos.webcam.Webcam;

public class ConcurrentThreadsExample {
	
	private static AtomicInteger counter = new AtomicInteger(0);
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();

	private static final class Capture extends Thread {
		private List<DetectedFace> faces = null;
		private List<BufferedImage> listImage = null;

		private static final AtomicInteger number = new AtomicInteger(0);
		private long NANOSEC_PER_SEC = 1000l*1000*1000;

		

		public Capture() {
			super("capture-" + number.incrementAndGet());
		}
	

		@Override
		public void run() {

			Webcam webcam = Webcam.getDefault();
			webcam.open();
			Iterator<DetectedFace> iterator = null;
			DetectedFace df = null;
			listImage = new ArrayList<BufferedImage>();
			long startTime = System.nanoTime();
			while ((System.nanoTime()-startTime)< 1*30*NANOSEC_PER_SEC) {
				if (!webcam.isOpen()) {
					break;
				}

				int n = counter.incrementAndGet();

				BufferedImage bf = webcam.getImage();
				faces = detector.detectFaces(ImageUtilities.createFImage(bf));
				iterator = faces.iterator();
	
				while (iterator.hasNext()){
					df = iterator.next();
					if (df.getConfidence()>15.0)
						listImage.add(bf);
				}	

				
				if (n != 0 && n % 100 == 0) {
					System.out.println(Thread.currentThread().getName() + ": Frames captured: " + n);
				}
			}
			
						
			Iterator<BufferedImage> imgIterator = listImage.iterator();
			int ctr=0;
			System.out.println("File Writting in D: capture output folder");
			String folderpath = "D:\\capture_output\\capture_" +Instant.now().toEpochMilli();
			File file = new File(folderpath);
	        if (!file.exists()) {
	            if (file.mkdir()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }			
			while (imgIterator.hasNext()) {
				try {
					// save image to PNG file
		
			        
					ImageIO.write(imgIterator.next(), "PNG", new File(folderpath + "\\ImageCapture_0" + ctr + ".png"));
					ctr++;
				} catch (IOException iox){
					System.out.println(iox);
				}
			}
			
		}
	}

	public static void main(String[] args) throws Throwable {

		System.out.println("Thread: Started..." );
		Capture cap = new Capture();
		cap.start();
		Thread.sleep(1 * 60 * 1000); // 1 minute

		//System.exit(1);
	}
}
