package com.hfq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReadText {
	
	public static void main(String[] args) throws IOException {
		String path = "/Users/jjs/Desktop/hsId";
		// BufferedReader是可以按行读取文件
		FileInputStream inputStream = new FileInputStream(new File(path));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		String str = "";
		
		//重复图片
		StringBuffer sb = new StringBuffer();
		sb.append("update t_city a set a.has_subway = 1 where a.city_id in{");
		while((str=bufferedReader.readLine())!=null)
		{
			sb.append("\'" + str + "\',");
		}
		System.out.println(sb.toString());
		
		//公司城市
//		while((str=bufferedReader.readLine())!=null)
//		{
//			StringBuffer sb = new StringBuffer();
//			sb.append("update  t_house_detail a set a.f_approve_status = 10  where a.f_house_sell_id =");
//			sb.append("\'"+ str+"\';");
//			System.out.println(sb.toString());
//		}
		
		//公司城市
//		Map<String, String> map = new HashMap<>();
//		while ((str = bufferedReader.readLine()) != null) {
//			String s = str.split("：")[1];
//			String tmp = s.substring(0, s.length()-1);
//			map.put(tmp, tmp);
//		}
//		for(String key: map.keySet()){
//			System.out.println(map.get(key));
//		}
		// close
		inputStream.close();bufferedReader.close();
	}
	
	
}
