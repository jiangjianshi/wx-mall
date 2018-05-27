package com.hfq;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.hfq.house.manager.util.HttpUtil;

public class ReadAccessLog {

	public static void main(String[] args) throws IOException {

		List<String> list = FileUtils.readLines(new File("/Users/jjs/Desktop/access_log.txt"), "UTF-8");
		int total = 1;
		int valid = 1;
		for (String line : list) {
			if (line.contains("text%3A")) {
				
				//解析关键字
				int indexText = line.indexOf("text%3A");
				int otherIndex = line.indexOf("%29", indexText);
				String text = line.substring(indexText + 7, otherIndex);
				String decodeTest = URLDecoder.decode(text);
				System.out.print(decodeTest+"：");
				
				//查询结果
				String domain = "http://admin:dQ7anb0DgyRc@m.huizhaofang.com/";
				int getIndex = line.indexOf("GET /");
				int endIndex = line.indexOf("&wt=javabin");
				String url = line.substring(getIndex + 5, endIndex);

				String newUrl = domain + url;
				System.out.println(newUrl);

				String foundNum = "", matchNum = "";
				try {
					String result = HttpUtil.get(newUrl);
					if (result.contains("numFound=")) {

						int numindex = result.indexOf("numFound=");
						String num = result.substring(numindex + 10, numindex + 15);
						int yin = num.indexOf("\"");
						foundNum = num.substring(0, yin);
					}

					if (result.contains("matches")) {

						int numindex = result.indexOf("matches");
						String num = result.substring(numindex + 7, numindex + 15);
						int gre = num.indexOf(">");
						int les = num.indexOf("<");

						matchNum = num.substring(gre + 1, les);
					}

					if ((StringUtils.isNotEmpty(foundNum) && !"0".equals(foundNum)) || (StringUtils.isNotEmpty(matchNum) && !"0".equals(matchNum))) {
							
						valid++;
						System.out.println(foundNum + " " + matchNum);
					}else{
						System.out.println(0 + " " + 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				total++;
			}
		}
		System.out.println(">>>>>>" + total + ",有效：" + valid);
	}
}
