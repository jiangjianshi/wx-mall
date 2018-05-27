package com.hfq;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

public class Test {

	// 设置APPID/AK/SK
	// public static final String APP_ID = "10227433";
	// public static final String API_KEY = "Nrm8zPYTY9jbzG3gYTBq6Ok2";
	// public static final String SECRET_KEY =
	// "AyIW2DNKGNdNzsLhph97SE0sztx3wiaj";
	public static final String APP_ID = "10230809";
	public static final String API_KEY = "Sb8HEwAqkuDCFOMi3Qh4x7Bu";
	public static final String SECRET_KEY = "BH1MWcZEYkTg7Z4rKOuS4rotZPWGsMBx";

	public static void main(String[] args) throws JSONException {

		// 初始化一个AipImageClassifyClient
		// AipImageClassify client = new AipImageClassify(APP_ID, API_KEY,
		// SECRET_KEY);
		//
		// HashMap<String, String> options = new HashMap<String, String>();
		// // options.put("with_face", "1");
		// options.put("top_num", "3");
		//
		// // 参数为本地图片路径
		// String image = "/Users/jjs/Downloads/房间2.jpg";
		// JSONObject res = client.objectDetect(image, options);
		// System.out.println(res.toString(2));
		//
		// // 参数为本地图片二进制数组
		// byte[] file = getImageBinary(image);
		// res = client.animalDetect(file, options);
		// System.out.println(res.toString(2));

//		 AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
//		
//		 // 可选：设置网络连接参数
//		 client.setConnectionTimeoutInMillis(2000);
//		 client.setSocketTimeoutInMillis(60000);
//		
//		
//		 // 调用接口
//		 String path =
//		 "/Users/jjs/Downloads/黑白合同.jpg";
//		 JSONObject res = client.detect(path, new HashMap<String, String>());
//		 System.out.println(res.toString(2));

		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
//		String imagePath = "/Users/jjs/Downloads/genimage.png";
//		JSONObject response1 = client.basicGeneral(imagePath, new HashMap<String, String>());
//		System.out.println(response1.toString());
		
		String url = "http://hzf-image.oss-cn-beijing.aliyuncs.com/hr_image/HF020313852462/1505889999180QJ9robwO_(1280,960).jpg";
	    JSONObject response3 = client.basicGeneralUrl(url, new HashMap<String, String>());
	    System.out.println(response3.toString());
		
	}

	public static byte[] getImageBinary(String filePath) {
		File f = new File(filePath);
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] bytes = baos.toByteArray();

			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean checkColor(String path) throws IOException {
		BufferedImage src = ImageIO.read(new File(path));
		int height = src.getHeight();
		int width = src.getWidth();
		System.out.println(height*width);
		int[] rgb = new int[4];
		int o = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pixel = src.getRGB(i, j);
				rgb[1] = (pixel & 0xff0000) >> 16;
				rgb[2] = (pixel & 0xff00) >> 8;
				rgb[3] = (pixel & 0xff);
				// 如果像素点不相等的数量超过50个 就判断为彩色图片
				if (rgb[1] != rgb[2] && rgb[2] != rgb[3] && rgb[3] != rgb[1]) {
					o += 1;
//					if (o >= 50) {
//						System.out.println("true");
//						return true;
//					}
				}
			}
		}
		System.out.println(o);
		System.out.println("false");
		return false;
	}
}
