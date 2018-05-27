package com.hfq.house.manager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 判断是否含有字符
	 * 
	 * @param cardNum
	 * @return
	 */
	public static boolean isContainsStr(String str) {
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(str);
		return m.matches();
	}

	// 判断是否包含特殊字符
	public static boolean isContainsSpecial(String str) {
		// 只允许字母和数字和英文逗号 // String regEx = "[^a-zA-Z0-9]";
		String regex = "[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】《》‘；：”“’。，、？]";
		Matcher m = Pattern.compile(regex).matcher(str);
		return m.find();
	}

}
