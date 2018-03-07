package com.emobile.jets.mayapada.util;

import java.security.Security;
import java.util.Arrays;
import java.util.BitSet;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CipherUtil.class);
	
	private static final int MAX_LENGTH_3DES_KEY	= 24;
	
	// http://www.random.org/bytes/
	private static final String KEY_CACHE_VO = 
			"39375213f3a3a8b8af90cb9a25bf2f52d1104d9f009a864f";
	
	private static final String KEY_STAGING_ACC_PIN = 
			"em0b!le1nd0n3s!@";
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/*
	 * http://www.exampledepot.com/egs/java.util/Bits2Array.html Returns a
	 * bitset containing the values in bytes. The byte-ordering of bytes must be
	 * big-endian which means the most significant bit is in element 0
	 */
	public static BitSet fromByteArray(byte[] bytes) {
		BitSet bits = new BitSet();
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
				bits.set(i);
			}
		}
		return bits;
	}

	/*
	 * http://www.exampledepot.com/egs/java.util/Bits2Array.html Returns a byte
	 * array of at least length 1. The most significant bit in the result is
	 * guaranteed not to be a 1 (since BitSet does not support sign extension).
	 * The byte-ordering of the result is big-endian which means the most
	 * significant bit is in element 0. The bit at index 0 of the bit set is
	 * assumed to be the least significant bit.
	 */
	public static byte[] toByteArray(BitSet bits) {
		// byte[] bytes = new byte[bits.length()/8+1];
		// WARNING !!! set fixed to 8 byte, only to be used for ISOMsg
		byte[] bytes = new byte[8];
		for (int i = 0; i < bits.length(); i++) {
			if (bits.get(i)) {
				bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
			}
		}
		return bytes;
	}
	
	public static String toHexString(byte[] data) {
		return new String(Hex.encode(data)).toUpperCase();
	}
	public static byte[] toHexByte(String input) {
		return Hex.decode(input);
	}
	
	public static String toHexString(BitSet bits) {
		return toHexString(toByteArray(bits));
	}
	public static BitSet toBitSet(String hexStr) {
		return fromByteArray(toHexByte(hexStr));
	}
	
	public static String passwordDigest(String userId, String password) {
		String upperUserId = userId.toUpperCase();
		String temp2 = (new StringBuilder(upperUserId)).reverse().toString() + password;
		
		RIPEMD256Digest digester = new RIPEMD256Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] input = temp2.getBytes();
		digester.update(input, 0, input.length);
		digester.doFinal(resBuf, 0);

		String result = toHexString(resBuf);

		return result.toUpperCase();
	}
	
	private static byte[] hashSHA256(String input) {
		SHA256Digest digester = new SHA256Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] resPass = input.getBytes();
		digester.update(resPass, 0, resPass.length);
		digester.doFinal(resBuf, 0);
		return resBuf;
	}
	
	private static byte[] hashRIPEMD256(String input) {
		RIPEMD256Digest digester = new RIPEMD256Digest();
		byte[] resBuf = new byte[digester.getDigestSize()];
		byte[] resPass = input.getBytes();
		digester.update(resPass, 0, resPass.length);
		digester.doFinal(resBuf, 0);
		return resBuf;
	}
	
	public static String encryptDESede(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return toHexString(encryptDESede(input.getBytes(), hashPasswordx) );
	}
	
	private static byte[] encryptDESede(byte[] input, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/ZeroBytePadding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		}
		catch (Exception e) {
			LOGGER.error("Failed to encrypt DES EDE. Input: {}, Key: {}. {}",
					new String[] { new String(input), Arrays.toString(key), e.getMessage() });
			return null;
		}
	}
	
	public static String decryptDESede(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		
		return new String(decryptDESede(toHexByte(input), hashPasswordx));
	}
	
	private static byte[] decryptDESede(byte[] input, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/ZeroBytePadding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "DESEDE");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		}
		catch (Exception e) {
			LOGGER.error("Failed to decrypt DES EDE. Input: {}, Key: {}. {}",
					new String[] { new String(input), Arrays.toString(key), e.getMessage() });
			return null;
		}
	}
	
	public static byte[] decryptDESedeWithPassword(String input, String password) {
		return decryptDESedeWithPassword(toHexByte(input), password);
	}

	public static byte[] decryptDESedeWithPassword(byte[] input, String password) {
		byte[] hashPassword = hashRIPEMD256(password);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return decryptDESede(input, hashPasswordx);
	}
	
	public static byte[] encryptDESedeWithPassword(String input, String password) {
		return encryptDESedeWithPassword(input.getBytes(), password);
	}

	public static byte[] encryptDESedeWithPassword(byte[] input, String password) {
		byte[] hashPassword = hashRIPEMD256(password);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return encryptDESede(input, hashPasswordx);
	}
	
	public static String encryptCacheVO(String cacheVO) {
		byte[] key = toHexByte(KEY_CACHE_VO);
		byte[] input = cacheVO.getBytes();
		return toHexString(encryptDESede(input, key) );
	}
	public static String decryptCacheVO(String cacheVO) {
		byte[] key = toHexByte(KEY_CACHE_VO);
		byte[] input = toHexByte(cacheVO);
		return new String(decryptDESede(input, key));
	}
	
	// encrypt staging account pin
	public static String encryptStagingAccPinUsingDESede(String input) {
		byte[] hashPassword = hashSHA256(KEY_STAGING_ACC_PIN);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return toHexString(encryptDESede(input.getBytes(), hashPasswordx) );
	}
	// decrypt staging account pin
	public static String decryptStagingAccPinUsingDESede(String input) {
		byte[] hashPassword = hashSHA256(KEY_STAGING_ACC_PIN);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		
		return new String(decryptDESede(toHexByte(input), hashPasswordx));
	}
	
	public static String decryptDESedePKCS7(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		
		return new String(decryptDESedePKCS7(toHexByte(input), hashPasswordx));
	}
	
	public static byte[] decryptDESedePKCS7(byte[] input, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "DESEDE");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		} catch (Exception e) {
			LOGGER.error("Error decryptDESedePKCS7. Input: " + Arrays.toString(input) + 
					". Password: " + Arrays.toString(key), e);
			return null;
		}
	}
	
	public static String encryptDESedePKCS7(String input, String key) {
		byte[] hashPassword = hashSHA256(key);
		byte[] hashPasswordx = new byte[MAX_LENGTH_3DES_KEY];
		System.arraycopy(hashPassword, 0, hashPasswordx, 0, MAX_LENGTH_3DES_KEY);
		return toHexString(encryptDESedePKCS7(input.getBytes(), hashPasswordx) );
	}
	
	public static byte[] encryptDESedePKCS7(byte[] input, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC");
			SecretKeySpec secretKey = new SecretKeySpec(key, "DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(input);
		} catch (Exception e) {
			LOGGER.error("Error encryptDESedePKCS7. Input: " + Arrays.toString(input) + 
					". Password: " + Arrays.toString(key), e);
			return null;
		}
	}
}
