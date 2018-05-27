package com.hfq;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@SpringBootConfiguration
@WebAppConfiguration
public class TestOcr {

	@Test
	public void testOcr() throws TesseractException {
		String path = "/Users/jjs/Desktop/contract.jpg";
//		File imageFile = new File(path);
//		if (!imageFile.exists()) {
//			
//		}
//		Tesseract instance = Tesseract.getInstance();
//		instance.setDatapath("/Users/jjs/Downloads/chi_sim.traineddata");// 设置训练库的位置
//		instance.setLanguage("chi_sim");// 中文识别
//		String result = instance.doOCR(imageFile);
//		System.out.println("fdfd"+result);
		
		File imageFile = new File(path);  
        ITesseract instance = new Tesseract();  // JNA Interface Mapping  
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping  
  
        try {  
            String result = instance.doOCR(imageFile);  
            System.out.println("result:"+result);  
        } catch (TesseractException e) {  
            System.err.println(e.getMessage());  
        }  
	}
}
