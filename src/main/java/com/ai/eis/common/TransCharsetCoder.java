package com.ai.eis.common;

import java.lang.Character.UnicodeBlock;

public class TransCharsetCoder {
	/**
	 * @Title:bytes2HexString @Description:字节数组转16进制字符串 @param b 字节数组 @return
	 * 16进制字符串 @throws
	 */
	public static String bytes2HexString(byte[] b) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			result.append(String.format("%02X", b[i]));
		}
		return result.toString();
	}

	/**
	 * @Title:hexString2Bytes @Description:16进制字符串转字节数组 @param src 16进制字符串 @return
	 * 字节数组 @throws
	 */
	public static byte[] hexString2Bytes(String src) {
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
		}
		return ret;
	}

	/**
	 * @Title:string2HexUTF8 @Description:字符UTF8串转16进制字符串 @param strPart 字符串 @return
	 * 16进制字符串 @throws
	 */
	public static String string2HexUTF8(String strPart) {

		return string2HexString(strPart, "UTF-8");
	}

	/**
	 * @Title:string2HexUnicode @Description:字符Unicode串转16进制字符串 @param strPart
	 * 字符串 @return 16进制字符串 @throws
	 */
	public static String string2HexUnicode(String strPart) {

		return string2HexString(strPart, "Unicode");
	}

	/**
	 * @Title:string2HexGBK @Description:字符GBK串转16进制字符串 @param strPart 字符串 @return
	 * 16进制字符串 @throws
	 */
	public static String string2HexGBK(String strPart) {

		return string2HexString(strPart, "GBK");
	}

	/**
	 * @Title:string2HexString @Description:字符串转16进制字符串 @param strPart 字符串 @param
	 * tochartype hex目标编码 @return 16进制字符串 @throws
	 */
	public static String string2HexString(String strPart, String tochartype) {
		try {
			return bytes2HexString(strPart.getBytes(tochartype));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @Title:hexUTF82String @Description:16进制UTF-8字符串转字符串 @param src
	 * 16进制字符串 @return 字节数组 @throws
	 */
	public static String hexUTF82String(String src) {

		return hexString2String(src, "UTF-8", "UTF-8");
	}

	/**
	 * @Title:hexGBK2String 
	 * @Description:16进制GBK字符串转字符串
	 *  @param src 16进制字符串 @return
	 * 字节数组 @throws
	 */
	public static String hexGBK2String(String src) {

		return hexString2String(src, "GBK", "UTF-8");
	}

	/**
	 * @Title:hexUnicode2String @Description:16进制Unicode字符串转字符串 @param src
	 * 16进制字符串 @return 字节数组 @throws
	 */
	public static String hexUnicode2String(String src) {
		return hexString2String(src, "Unicode", "UTF-8");
	}

	/**
	 * @Title:hexString2String @Description:16进制字符串转字符串 @param src 16进制字符串 @return
	 * 字节数组 @throws
	 */
	public static String hexString2String(String src, String oldchartype, String chartype) {
		byte[] bts = hexString2Bytes(src);
		try {
			if (oldchartype.equals(chartype))
				return new String(bts, oldchartype);
			else
				return new String(new String(bts, oldchartype).getBytes(), chartype);
		} catch (Exception e) {

			return "";
		}
	}
	
	   /**
     * gbk转unicode
     * @param str
     * @return
     */
    public static String gbkToUnicode(String str) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr1 = (char) str.charAt(i);
            if ((chr1 & (0x00FF)) == chr1) {
                result.append(chr1);
                continue;
            }
            result.append("\\u" + Integer.toHexString((int) chr1));
        }
        return result.toString();
    }
    
    
    /**
     * unicode转gbk
     * @param dataStr
     * @return
     */
    public static String unicodeToGbk(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();
        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1
                    || !"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));
                index++;
                continue;
            }
            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);
            char letter = (char) Integer.parseInt(charStr, 16);
            buffer.append(letter);
            index += 6;
        }
        return buffer.toString();
    }
    
    /**
     * utf-8转unicode
     * @param inStr
     * @return
     */
    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode转utf-8
     * @param theString
     * @return
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
    
}
