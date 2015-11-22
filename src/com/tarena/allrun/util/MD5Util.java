package com.tarena.allrun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	/**
	 * The jce MD5 message digest generator.
	 */
	private static MessageDigest md5;

	public static final String encodeString(String string)
			throws RuntimeException {
		return byteToHex(digestString(string, null));
	}

	/**
	 * Retrieves a hexidecimal character sequence representing the MD5 digest of
	 * the specified character sequence, using the specified encoding to first
	 * convert the character sequence into a byte sequence. If the specified
	 * encoding is null, then ISO-8859-1 is assumed
	 * 
	 * @param string
	 *            the string to encode.
	 * @param encoding
	 *            the encoding used to convert the string into the byte sequence
	 *            to submit for MD5 digest
	 * @return a hexidecimal character sequence representing the MD5 digest of
	 *         the specified string
	 * @throws HsqlUnsupportedOperationException
	 *             if an MD5 digest algorithm is not available through the
	 *             java.security.MessageDigest spi or the requested encoding is
	 *             not available
	 */
	public static final String encodeString(String string, String encoding)
			throws RuntimeException {
		return byteToHex(digestString(string, encoding));
	}

	/**
	 * Retrieves a byte sequence representing the MD5 digest of the specified
	 * character sequence, using the specified encoding to first convert the
	 * character sequence into a byte sequence. If the specified encoding is
	 * null, then ISO-8859-1 is assumed.
	 * 
	 * @param string
	 *            the string to digest.
	 * @param encoding
	 *            the character encoding.
	 * @return the digest as an array of 16 bytes.
	 * @throws HsqlUnsupportedOperationException
	 *             if an MD5 digest algorithm is not available through the
	 *             java.security.MessageDigest spi or the requested encoding is
	 *             not available
	 */
	public static byte[] digestString(String string, String encoding)
			throws RuntimeException {

		byte[] data;

		if (encoding == null) {
			encoding = "ISO-8859-1";
		}

		try {
			data = string.getBytes(encoding);
		} catch (UnsupportedEncodingException x) {
			throw new RuntimeException(x.toString());
		}

		return digestBytes(data);
	}

	/**
	 * Retrieves a byte sequence representing the MD5 digest of the specified
	 * byte sequence.
	 * 
	 * @param data
	 *            the data to digest.
	 * @return the MD5 digest as an array of 16 bytes.
	 * @throws HsqlUnsupportedOperationException
	 *             if an MD5 digest algorithm is not available through the
	 *             java.security.MessageDigest spi
	 */
	public static final byte[] digestBytes(byte[] data) throws RuntimeException {

		synchronized (MD5Util.class) {
			if (md5 == null) {
				try {
					md5 = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					throw new RuntimeException(e.toString());
				}
			}

			return md5.digest(data);
		}
	}

	private static final char HEXCHAR[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String byteToHex(byte b[]) {

		int len = b.length;
		char[] s = new char[len * 2];

		for (int i = 0, j = 0; i < len; i++) {
			int c = ((int) b[i]) & 0xff;

			s[j++] = HEXCHAR[c >> 4 & 0xf];
			s[j++] = HEXCHAR[c & 0xf];
		}

		return new String(s);
	}

	public static String getFileMd5(String filename) throws Exception {
		return getFileMd5(filename, null);
	}

	public static String getFileMd5(String filename, String encoding)
			throws Exception {
		encoding = encoding == null ? "ISO-8859-1" : encoding;
		File f = new File(filename);

		if (!f.exists()) {
			return "";
		}
		InputStream is = new FileInputStream(f);
		byte[] buffer = new byte[1024];
		MessageDigest digest = MessageDigest.getInstance("MD5");

		int count;
		while ((count = is.read(buffer)) > 0) {
			digest.update(buffer, 0, count);
		}
		byte[] md5sum = digest.digest();
		BigInteger bigInt = new BigInteger(1, md5sum);
		String output = bigInt.toString(16);

		is.close();
		return output;
	}

	public static String getFileMd5(File file) throws Exception {
		return getFileMd5(file, null);
	}

	public static String getFileMd5(File file, String encoding)
			throws Exception {
		encoding = encoding == null ? "ISO-8859-1" : encoding;
		InputStream is = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		MessageDigest digest = MessageDigest.getInstance("MD5");

		int count;
		while ((count = is.read(buffer)) > 0) {
			digest.update(buffer, 0, count);
		}
		byte[] md5sum = digest.digest();
		BigInteger bigInt = new BigInteger(1, md5sum);
		String output = bigInt.toString(16);

		is.close();
		return output;
	}

	public static String getStringMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				// System.out.println((int)b);
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

}
