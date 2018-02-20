package com.my.sarxos.webcam.Webcam;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

public class DetectWebCam {
	public static void main(String[] args) throws Throwable{
		// get default webcam and open it
		Webcam webcam = Webcam.getDefault();
		webcam.open();

				// get image
		BufferedImage image = webcam.getImage();

		// save image to PNG file
		ImageIO.write(image, "PNG", new File("test.png"));
	}

}
