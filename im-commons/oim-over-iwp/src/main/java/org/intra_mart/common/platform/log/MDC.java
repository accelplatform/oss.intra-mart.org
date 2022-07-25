package org.intra_mart.common.platform.log;

/**
 * OPEN INTRA-MART モジュールを intra-mart WebPlatform 上で動作させるためのラッパークラスです。
 * 
 * @see jp.co.intra_mart.common.platform.log.MDC
 */
public class MDC {

	private MDC() {
	}

	public static void put(String key, String val) throws IllegalArgumentException {
		jp.co.intra_mart.common.platform.log.MDC.put(key, val);
	}

	public static String get(String key) throws IllegalArgumentException {
		return jp.co.intra_mart.common.platform.log.MDC.get(key);
	}

	public static void remove(String key) throws IllegalArgumentException {
		jp.co.intra_mart.common.platform.log.MDC.remove(key);
	}

	public static void clear() {
		jp.co.intra_mart.common.platform.log.MDC.clear();
	}
}
