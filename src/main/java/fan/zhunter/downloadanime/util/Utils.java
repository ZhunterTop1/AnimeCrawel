package fan.zhunter.downloadanime.util;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;

/**
 * 项目使用的工具类
 * 
 * @author   zhunter
 * @Date	 2020年2月6日
 */
public class Utils {

	/**
	 * 判断是否为空，支持字符串，map,集合，数组
	 * 
	 * @param obj				判断对象
	 * @return 					为空返回TRUE，不为空返回FASLE
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			return "".equals(String.valueOf(obj).trim());
		} else if (obj instanceof Map<?, ?>) {
			return ((Map<?, ?>) obj).isEmpty();
		} else if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).isEmpty();
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		return false;
	}
	
	/**
	 * 判断是否为空，支持字符串，map,集合，数组
	 * 
	 * @param obj				判断对象
	 * @return 					为空返回FASLE，不为空返回TRUE
	 */
	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}

	/**
	 * 返回字符串的MD5
	 *
	 * @param str				字符串
	 * @return 					字符串的MD5值
	 */
	public static String getMD5(String str){
		try{
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] bs = digest.digest(str.getBytes());
			String hexString = "";
			for (byte b : bs) {
				int temp = b & 255;
				if (temp < 16 && temp >= 0) {
					hexString = hexString + "0" + Integer.toHexString(temp);
				} else {
					hexString = hexString + Integer.toHexString(temp);
				}
			}

			return hexString;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
