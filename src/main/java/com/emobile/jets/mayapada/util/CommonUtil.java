package com.emobile.jets.mayapada.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.emobile.jets.mayapada.smi.data.JetsConstant;

public class CommonUtil {
	
	private static SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //("dd-MMM-yyyy HH:mm:ss");
	private static NumberFormat nfNoDecimal = new DecimalFormat("#,##0");
	
	private static Random r = new Random();

	public static String convertCamelCaseToUnderScore(String camelCase) {
		if (camelCase == null) return null;
		StringBuilder sb = new StringBuilder();
		String[] tokens = camelCase.split("[A-Z]");
		int count = 0;
		for (String token: tokens) {
			sb.append(token);
			if (++count < tokens.length) {
				//get first upper case string
				int idx = sb.length() - (count - 1);
				String s = camelCase.substring(idx, idx+1);
				sb.append("_").append(s.toLowerCase());
			}
		}
		if (sb.length() > 0 && sb.charAt(0) == '_')
			sb.deleteCharAt(0);
		return sb.toString();
	}
	
	public static void parseQueryString(String query, Properties params) {
		StringTokenizer st = new StringTokenizer(query, "?&=", true);
		String previous = null;
		while (st.hasMoreTokens()) {
			String current = st.nextToken();
			if ("?".equals(current) || "&".equals(current)) {
				// ignore
			} else if ("=".equals(current)) {
				String value = "";
				if (st.hasMoreTokens())
					value = st.nextToken();
				if ("&".equals(value))
					value = ""; // ignore &
				params.setProperty(previous, value);
			} else {
				previous = current;
			}
		}
	}

	public static String repeat(String str, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static String padRight(String str, int length) {
		if (str == null) str = "";
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() < length)
			sb.append(' ');
		return sb.toString();
	}
	
	public static String padLeft(String str, char c, int length) {
		if (str == null) str = "";
		str = str.trim();
		StringBuilder sb = new StringBuilder();
		if (str.length() >= length) {
			return str;
		}
		int fill = length - str.length();
		while (fill-- > 0)
			sb.append(c);
		sb.append(str);
		return sb.toString();
	}
	
	public static String zeroPadLeft(String str, int length) {
		return padLeft(str, '0', length);
	}
	
	/**
	 * normalize 081xxx -> +6281xxx standard iso format 
	 * @param phoneNo
	 * @return
	 */
	public static String normalizePhoneNo(String countryCode, String phoneNo) {
		if (StringUtils.isEmpty(phoneNo)) return phoneNo;
		phoneNo = phoneNo.trim();
		if (phoneNo.startsWith(countryCode) && phoneNo.length() > 5)
			phoneNo = "+" + phoneNo;
		else if (phoneNo.startsWith("0") && phoneNo.length() > 4)
			phoneNo = "+" + countryCode + phoneNo.substring(1);
		return phoneNo;
	}
	
	public static String formatPhone(String phoneNo) {
		if (StringUtils.isEmpty(phoneNo)) return phoneNo;
		if (phoneNo.startsWith("+62") && phoneNo.length() > 3)
			phoneNo = "0" + phoneNo.substring(3);
		else if (phoneNo.startsWith("+"))  // just in case +555
			phoneNo = phoneNo.substring(1);
		else if (phoneNo.startsWith("62") && phoneNo.length() > 5)
			phoneNo = "0" + phoneNo.substring(2);
		return phoneNo;
	}
	
	public static String replaceCR(String str) {
		return str.replaceAll(JetsConstant.NEWLINE, "\n");
	}
	
	public static String displayDateTime(Date dateTime) {
		return sdfDateTime.format(dateTime);
	}
	public static String displayNumberNoDecimal(double number) {
		return nfNoDecimal.format(number);
	}
	
	/*
	 * refer to: http://www.javapractices.com/topic/TopicAction.do?Id=62
	 */
	public static int random(int range) {
		return random(0, range);
	}

	public static int random(int start, int end) {
		long range = (long) end - (long) start + 1;
		long fraction = (long) (range * r.nextDouble());
		int randomNumber = (int) (fraction + start);
		return randomNumber;
	}

}
