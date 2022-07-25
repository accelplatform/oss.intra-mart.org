package org.intra_mart.common.aid.jdk.java.text;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

/**
 * {@link ISO8601DateFormat}クラスのテストケースです。<br/>
 * 
 * <br/>
 * パース対象となる文字列形式
 * <ul>
 * <li>yyyy</li>
 * <li>yyyy-MM</li>
 * <li>yyyy-MM-dd</li>
 * <li>yyyy-MM-ddThh</li>
 * <li>yyyy-MM-ddThh:mm</li>
 * <li>yyyy-MM-ddThh:mm:ss</li>
 * <li>yyyy-MM-ddThh:mm:ssZ</li>
 * <li>yyyy-MM-ddThh:mm:ss+09:00</li>
 * <li>yyyy-MM-ddThh:mm:ss-10:00</li>
 * 
 * <li>yyyy-MM-ddThh:mm:ss+0900</li>
 * <li>yyyy-MM-ddThh:mm:ss+09</li>
 * <li>yyyy-MM-ddThh:mm:ss+9</li>
 * <li>yyyy-MM-ddThh:mm:ss+9:00</li>
 * <li>yyyy-MM-ddThh:mm:ss+900</li>
 * 
 * </ul>
 * タイムゾーンの省略した場合は、VMデフォルトのタイムゾーンとします。<br/>
 * <br/>
 * パースの対象外となる条件は以下の通りです。
 * <ul>
 * <li>小数値を使用した時刻表現（ピリオド(.) と カンマ(,)を使用した時刻表現は対象外） </li>
 * <li>年と年内の日の番号</li>
 * <li>年と週と曜日</li>
 * <li>期間</li>
 * </ul>
 * 
 * @see http://ja.wikipedia.org/wiki/ISO_8601
 */
public class ISO8601DateFormatTest extends TestCase {

	private Calendar cal;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cal = Calendar.getInstance();
		cal.clear();
	}

	
	public void testParseISO8601_年だけ() throws Exception {
		cal.set(Calendar.YEAR, 2004);
		
		Date expected = cal.getTime();

		String dateString = "2004";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}

	public void testParseISO8601_年月だけ() throws Exception {
		cal.set(Calendar.YEAR, 2004);
		cal.set(Calendar.MONTH, 0);
		
		Date expected = cal.getTime();

		String dateString = "2004-01";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}
	
	public void testParseISO8601_年月日() throws Exception {
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}


	public void testParseISO8601_年月日_時() throws Exception {
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}	
	
	public void testParseISO8601_年月日_時分() throws Exception {
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}	
	
	public void testParseISO8601_年月日_時分秒() throws Exception {
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}

	public void testParseISO8601_年月日_時分秒_UTC() throws Exception {
		TimeZone utc = TimeZone.getTimeZone("UTC");
		cal.setTimeZone(utc);
		
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56Z";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}

	public void testParseISO8601_年月日_時分秒_プラス9時間() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56+09:00";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}

	public void testParseISO8601_年月日_時分秒_マイナス5時間() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT-05:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56-05:00";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}
	
	public void testParseISO8601_年月日_時分秒_プラス9時間_コロンなし() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56+0900";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}
	
	public void testParseISO8601_年月日_時分秒_プラス9時間_時間のみ_二桁() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56+09";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}
	
	public void testParseISO8601_年月日_時分秒_プラス9時間_時間のみ_ひと桁() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56+9";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}	
	
	public void testParseISO8601_年月日_時分秒_プラス9時間_時間一桁() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56+9:00";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}

	public void testParseISO8601_年月日_時分秒_プラス9時間_コロンなし_時間一桁() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-28T12:34:56+900";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}
	
	
	public void testParseISO8601_年月日_時分秒_プラス9時間_うるう年() throws Exception {
		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 29);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date expected = cal.getTime();

		String dateString = "2008-02-29T12:34:56+09:00";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		assertEquals(expected, actual);
	}
	
	
	
	
	public void testParseISO8601_対象外_年と年内の日の番号() throws Exception {
		String dateString = "2004-092";
		Date actual = ISO8601DateFormat.parse(dateString);

		// 上記は、以下と同一とみなされます。
		String expectedString = "2004-09";
		Date expected = ISO8601DateFormat.parse(expectedString);

		assertEquals(expected, actual);
	}

	public void testParseISO8601_対象外_年と週と曜日() throws Exception {
		String dateString = "2004-W14-4";
		Date actual = ISO8601DateFormat.parse(dateString);
		
		// 上記は、以下と同一とみなされます。
		String expectedString = "2004";
		Date expected = ISO8601DateFormat.parse(expectedString);

		assertEquals(expected, actual);
	}

	public void testParseISO8601_対象外_年月日_時分秒の分がピリオド指定() throws Exception {
		String dateString = "2008-02-28T12.5";
		Date actual = ISO8601DateFormat.parse(dateString);

		// 上記は、以下と同一とみなされます。
		String expectedString = "2008-02-28T12";
		Date expected = ISO8601DateFormat.parse(expectedString);

		assertEquals(expected, actual);
	}
	
	public void testParseISO8601_対象外_年月日_時分秒の秒がピリオド指定() throws Exception {
		String dateString = "2008-02-28T12:34.5";
		Date actual = ISO8601DateFormat.parse(dateString);

		// 上記は、以下と同一とみなされます。
		String expectedString = "2008-02-28T12:34";
		Date expected = ISO8601DateFormat.parse(expectedString);

		assertEquals(expected, actual);
	}
	
	public void testParseISO8601_対象外_期間() throws Exception {
		String dateString = "2004-04-01T12:34:56+09:00/2007-08-31T16:54:32+09:00";
		Date actual = ISO8601DateFormat.parse(dateString);

		// 上記は、以下と同一とみなされます。
		String expectedString = "2004-04-01T12:34:56+09:00";
		Date expected = ISO8601DateFormat.parse(expectedString);

		assertEquals(expected, actual);
	}
	
	
	public void testFormat_年月日_時分秒_UTC() throws Exception {
		String expected = "2008-02-28T12:34:56Z";

		TimeZone utc = TimeZone.getTimeZone("UTC");
		cal.setTimeZone(utc);
		
		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date date = cal.getTime();
		String actual = ISO8601DateFormat.format(date);
		
		assertEquals(expected, actual);
	}

	public void testFormat_年月日_時分秒_プラス9時間() throws Exception {
		String expected = "2008-02-28T03:34:56Z";

		TimeZone timezone = TimeZone.getTimeZone("GMT+09:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date date = cal.getTime();
		String actual = ISO8601DateFormat.format(date);
		
		assertEquals(expected, actual);
	}

	public void testFormat_年月日_時分秒_マイナス5時間() throws Exception {
		String expected = "2008-02-28T17:34:56Z";

		TimeZone timezone = TimeZone.getTimeZone("GMT-05:00");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date data = cal.getTime();
		String actual = ISO8601DateFormat.format(data);
		
		assertEquals(expected, actual);
	}
	
	public void testFormat_年月日_時分秒_JST() throws Exception {
		String expected = "2008-02-28T03:34:56Z";

		TimeZone timezone = TimeZone.getTimeZone("JST");
		cal.setTimeZone(timezone);

		cal.set(Calendar.YEAR, 2008);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 28);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 34);
		cal.set(Calendar.SECOND, 56);
		
		Date date = cal.getTime();
		String actual = ISO8601DateFormat.format(date);
		
		assertEquals(expected, actual);
	}
	
}
