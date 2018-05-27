package com.hfq.house.manager.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ImgHmashUtil {

	public static BufferedImage getImageIo(String url) throws Exception {
		// 调置http请求连接保持时间，防止大图片下载不成功。
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000).build();

		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

		try {
			RequestBuilder get = RequestBuilder.get();
			get.setUri(url);
			CloseableHttpResponse response = httpclient.execute(get.build());
			InputStream instream = null;
			try {
				HttpEntity entity = response.getEntity();
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {
					String result = EntityUtils.toString(entity);
					System.out.println(result);
				}
				instream = entity.getContent();
				BufferedImage imageIo = ImageIO.read(instream);
				greyScaleImage(imageIo);//转为灰度图片
				return imageIo;
			} finally {
				if (instream != null) {
					instream.close();
				}
				response.close();
			}
		} catch (Exception e) {
		} finally {
			httpclient.close();
		}
		return null;
	}

	public static void greyScaleImage(BufferedImage img) { // this is bad i'll
		// come fix it later
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int p = img.getRGB(x, y); // this is very slow

				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;

				// calculate average
				int avg = (r + g + b) / 3;

				// replace RGB value with avg
				p = (a << 24) | (avg << 16) | (avg << 8) | avg;

				img.setRGB(x, y, p);
			}
		}

	}

	/**
	 * dhash
	 * 
	 * @param img
	 * @return
	 */
	public static String dhash(BufferedImage img) {
		StringBuilder res = new StringBuilder();

		for (int y = 0; y < img.getHeight(); y++) {

			StringBuilder bit = new StringBuilder();

			for (int x = 0; x < img.getWidth() - 1; x++) {

				if ((img.getRGB(x, y) - img.getRGB(x + 1, y)) < 0) {
					bit.append("1");
					// System.out.print(1 + " ");
				} else {
					bit.append("0");
					// System.out.print(0 + " ");
				}
				if (bit.length() == 4) {
					int decimal = Integer.parseInt(bit.toString(), 2);
					String hexStr = Integer.toString(decimal, 16);
					bit = new StringBuilder();
					res.append(hexStr);
				}
			}
			// System.out.println();
		}
		// System.out.println();
		return res.toString();
	}

	public static void main(String[] args) throws Exception {
		String url = "http://hzf-image.oss-cn-beijing.aliyuncs.com/hr_image/HF201706150323szJ/1498446097504nuu1nh5v_(900,506).jpg";
		String suffix = "?x-oss-process=image/resize,m_fixed,h_8,w_9";
		BufferedImage img = getImageIo(url + suffix);
		String hash = dhash(img);
		System.out.println(hash);
	}
}
