package com.cq.cardutil.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @ClassName：StringUtil
 * @Description：字符串处理类
 * @author：GeYazhong @date：2015-12-23 下午04:30:38
 * @version V1.0
 */
public class StringUtil {
	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return 空返回true否则返回false
	 */
	public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

	/**
	 * 判断对象是否为空
	 * 
	 * @param s
	 * @return 空返回true否则返回false
	 */
	@SuppressWarnings("unused")
	public static boolean isEmpty(Object s) {
		if (s instanceof List) {
			if (s == null)
				return true;
			if ("[]".equals(s.toString()))
				return true;
		}

        return s == null || "".equals(s);
    }

	/**
	 * 判断字符串是否不为空
	 * 
	 * @param s
	 * @return 空返回false否则返回true
	 */
	public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s);
    }

	/**
	 * 判断对象是否不为空
	 * 
	 * @param s
	 * @return 空返回false否则返回true
	 */
	public static boolean isNotEmpty(Object s) {
        return s != null && !"".equals(s);
    }

	/**
	 * 拼接字符串
	 * 
	 * @param arr
	 *            字符串数组
	 * @param split
	 *            分隔符
	 * @param suffix
	 *            字符串外包装字符
	 * @return
	 */
	public static String join(String[] arr, String split, String suffix) {
		StringBuffer buffer = new StringBuffer();
		for (String s : arr) {
			buffer.append(suffix);
			buffer.append(s);
			buffer.append(suffix);
			buffer.append(split);
		}
		if (buffer.length() > 0) {
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}

	/**
	 * 拼接字符串
	 * 
	 * @param lst
	 *            字符串集合
	 * @param split
	 *            分隔符
	 * @param suffix
	 *            字符串外包装字符
	 * @return
	 */
	public static String join(List<String> lst, String split, String suffix) {
		StringBuffer buffer = new StringBuffer();
		for (String s : lst) {
			buffer.append(suffix);
			buffer.append(s);
			buffer.append(suffix);
			buffer.append(split);
		}
		if (buffer.length() > 0) {
			buffer.deleteCharAt(buffer.length() - 1);
		}
		return buffer.toString();
	}

	/**
	 * 拼接字符串
	 * 
	 * @param lst
	 *            字符串集合
	 * @param split
	 *            分隔符
	 * @return
	 */
	public static String join(List<String> lst, String split) {
		return join(lst, split, "");
	}

	/**
	 * 拼接字符串
	 * 
	 * @param arr
	 *            字符串数组
	 * @param split
	 *            分隔符
	 * @return
	 */
	public static String join(String[] arr, String split) {
		return join(arr, split, "");
	}

	/**
	 * 拼接字符串
	 * 
	 * @param arrStr
	 *            字符串数组字符串
	 * @param split
	 *            分隔符
	 * @param suffix
	 *            字符串外包装字符
	 * @return
	 */
	public static String join(String arrStr, String split, String suffix) {
		if (arrStr != null) {
			return join(arrStr.split(","), split, suffix);
		} else
			return "";
	}

	/**
	 * 判断字符串是否数值
	 * 
	 * @param str
	 * @return true:是数值 ；false：不是数值
	 */
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");
		Matcher match = pattern.matcher(str);
		return match.matches();
	}

	/**
	 * 根据长度截取字符串
	 * 
	 * @param data
	 * @param len
	 * @param prefix
	 * @return
	 */
	public static String subStringEx(String data, int len, String prefix) {
		if (data == null || "".equals(data))
			return null;
		String afterfix = "...";
		try {
			byte[] bytes = data.getBytes("GBK");
			StringBuilder sb = new StringBuilder();
			if (prefix != null && !"".equals(prefix)) {
				len -= prefix.getBytes("GBK").length;
				sb.append(prefix);
			}
			if (bytes.length <= len) {
				sb.append(data);
			} else {
				len -= afterfix.getBytes("GBK").length;
				if (bytes[len] < 0) {
					len--;
				}
				String s = new String(bytes, 0, len, "GBK");
				sb.append(s);
				sb.append(afterfix);
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * in条件查询时拼接问号和参数
	 * 
	 * @param strings
	 * @return
	 */
	public static String inParam2QuestionMarkAndParam(String parameters, List<Object> paramList) {
		StringBuffer sb = new StringBuffer();
		for (String parameter : parameters.split(",")) {
			sb.append("?,");
			paramList.add(parameter);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/** 获取问加你分隔符 */
	public static String getSystemFileSeparator() {
		return System.getProperty("file.separator");
	}

	/**
	 * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	 * 
	 * @param sourceDate
	 * @param formatLength
	 * @return 重组后的数据
	 */
	public static String get2LenStr(int sourceData) {
		if (sourceData >= 0 && sourceData <= 9) {
			return frontCompWithZore(sourceData, 2);
		} else if (sourceData < 0) {
			return "00";
		} else if (sourceData > 99) {
			return ("" + sourceData).substring(0, 2);
		} else {
			return "" + sourceData;
		}
	}

	/**
	 * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	 * 
	 * @param sourceDate
	 * @param formatLength
	 * @return 重组后的数据
	 */
	public static String frontCompWithZore(int sourceData, int formatLength) {
		/*
		 * 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
		 */
		String newString = String.format("%0" + formatLength + "d", sourceData);
		return newString;
	}

	// 将时间戳后面加上两位整数的随机数
	public static String generateRandomStr() {
		long l = System.currentTimeMillis();
		int r = Math.abs((int) (Math.random() * 900) + 100);
		return "" + l + r;
	}

	/**
	 * parm String 判断邮箱是否合格 return boolean
	 * 
	 */
	public static boolean email(String str) {
		boolean flag = false;
		String mail = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		if (str.matches(mail)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * parm String 判断邮手机号码是否合格 return boolean
	 * 
	 */
	public static boolean phone(String str) {
		boolean flag = false;
		String phone = "^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$";
		if (str.matches(phone)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 验证QQ号码是否符合要求
	 * 
	 * @param str
	 * @return
	 */
	public static boolean qq(String str) {
		boolean flag = false;
		String qq = "[1-9][0-9]{4,9}";
		if (str.matches(qq)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * parm String 判断密码是否合格可含有特殊字符及长度只能在6-16之间，不可含有特殊字符 return boolean
	 * 
	 */
	public static boolean passWord(String str) {
		boolean flag = false;
		String pwd = "^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._!@#$%^&*<>,+;':/]){5,15}$";
		if (!str.matches(pwd)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断字符串是否相等
	 * 
	 * @param 字符串a
	 * @param 字符串b
	 * @return 相等返回true否则返回false
	 */
	public static boolean equals(String a, String b) {
		if (a == null && b == null)
			return true;
        return a != null && a.equals(b);
    }

	public static int bytes2Int(byte[] bytes) {
		int result = 0;
		int length = bytes.length;
		// 将每个byte依次搬运到int相应的位置
		for (int i = length - 1; i >= 0; i--) {
			if (i == length - 1) {
				result = bytes[i] & 0xff;
			} else {
				result = result << 8 | bytes[i] & 0xff;
			}
		}
		return result;
	}

	/**
	 * 单个byte转换成无符号int
	 * 
	 * @param b
	 * @return
	 */
	public static int singleBytes2Int(byte b) {
		// 将每个byte依次搬运到int相应的位置
		return b & 0xff;
	}

	/**
	 *
	 * @param n
	 * @return
	 */
	public static byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 *
	 * @param n
	 * @return
	 */
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 正序 高位在前低位在后
	 * 
	 * @param n
	 * @param seat
	 * @return
	 */

	public static byte[] toHH(int n, int seat) {
		if (seat <= 0) {
			return new byte[0];
		}
		byte[] b = new byte[seat];
		for (int i = b.length - 1; i >= 0; i--) {
			b[i] = (byte) (n >> ((b.length - 1 - i) * 8) & 0xff);
		}
		return b;
	}

	/**
	 * 反序排列 低位在前高位在后
	 * 
	 * @param n
	 * @param seat
	 * @return
	 */
	public static byte[] toLH(int n, int seat) {
		if (seat <= 0) {
			return new byte[0];
		}
		byte[] b = new byte[seat];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (n >> (i * 8) & 0xff);
		}
		return b;
	}

	public static boolean isBytesEmpty(byte[] bytes) {
		boolean res = true;
		for (byte b : bytes) {
			if (b != 0)
				return false;
		}
		return res;
	}

	public static boolean isBytesEmpty(byte[] bytes, int start, int end) {
		boolean res = true;
		for (int i = start; i <= end; i++) {
			if (bytes[i] != 0)
				return false;
		}
		return res;
	}

	/**
	 *判断是否是ip
	 * @param text ip地址
	 * @return true ：false
	 */
	public static boolean ipCheck(String text) {
		if (text != null && !text.isEmpty()) {
			// 定义正则表达式
			String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+
			"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
			"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
			"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
			// 判断ip地址是否与正则表达式匹配
			if (text.matches(regex)) {
				// 返回判断信息
				return true;
			} else {
				// 返回判断信息
				return false;
			}
		}
		return false;
	}

}