package com.hfq.house.manager.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OcrUtil {
	
	public static String recognizeWord(InputStream in) throws IOException {
		BufferedImage textImage = ImageIO.read(in);
		ITesseract instance = new Tesseract();
		instance.setDatapath("/Users/jjs/Desktop");// 设置训练库
		instance.setLanguage("chi_sim");// 中文识别
		String result = null;
		try {
			result = instance.doOCR(textImage);
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return result;
	}

}
