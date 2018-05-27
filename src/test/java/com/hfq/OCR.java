package com.hfq;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;

public class OCR {

	/**
	 *
	 * @param srImage
	 *            图片路径
	 * @param ZH_CN
	 *            是否使用中文训练库,true-是
	 * @return 识别结果
	 */
	private static String FindOCR(String srImage, boolean ZH_CN) {
		try {
			System.out.println("start");
			double start = System.currentTimeMillis();
			File imageFile = new File(srImage);
			if (!imageFile.exists()) {
				return "图片不存在";
			}
			BufferedImage textImage = ImageIO.read(imageFile);
			ITesseract instance = new Tesseract();
			 instance.setDatapath("/Users/jjs/Desktop");//设置训练库
			if (ZH_CN)
				instance.setLanguage("chi_sim");// 中文识别
			String result = null;
			result = instance.doOCR(imageFile);
			double end = System.currentTimeMillis();
			System.out.println("耗时" + (end - start) / 1000 + " s");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "发生未知错误";
		}
	}

	public static void main(String[] args) throws Exception {
//		 String result=FindOCR("/Users/jjs/Desktop/contract.jpg",true);
		 String result=FindOCR("/Users/jjs/Desktop/house-type.jpg",true);
//		String result = FindOCR("/Users/jjs/Desktop/idcard.jpg", true);
//		String result = FindOCR("/Users/jjs/Desktop/house_inner.jpg", true);
//		String result = FindOCR("/Users/jjs/Desktop/danke.jpg", true);
		System.out.println(result);
		System.out.println(result.length());

//		 File imageFile = new File("/Users/jjs/Desktop/contract.jpg");
//		 ITesseract instance = new Tesseract(); // JNA Interface Mapping
//		 instance.setLanguage("chi_sim");//中文识别
//		 // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//		
//		 try {
//		 String result = instance.doOCR(imageFile);
//		 System.out.println("result:"+result);
//		 } catch (TesseractException e) {
//		 System.err.println(e.getMessage());
//		 }
	}
}
