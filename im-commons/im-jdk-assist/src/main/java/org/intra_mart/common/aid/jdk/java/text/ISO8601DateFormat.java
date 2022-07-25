package org.intra_mart.common.aid.jdk.java.text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.intra_mart.common.platform.log.Logger;

/**
 * <a href="http://ja.wikipedia.org/wiki/ISO_8601">ISO 8601</a> 形式の日時に関する
 * フォーマット(日付 -> テキスト) および 解析(テキスト -> 日付) を行うユーティリティクラスです。<br/>
 * <br/>
 * <a href="http://ja.wikipedia.org/wiki/ISO_8601">ISO 8601</a> 形式の例 → 
 * <code>「2008-12-31T12:34:56Z」</code>
 */
public class ISO8601DateFormat {

	private static final Logger logger = Logger.getLogger(); 

	/**
	 * @param dateString
	 * @return
	 */
	/**
	 * <a href="http://ja.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
	 * 形式の文字列を解析し、Dateを生成します。<br/>
	 * 解析に失敗した場合は、nullを返却します。<br/>
	 * <br/>
	 * 解析対象となる文字列の形式例を以下に示します。
	 * <ol>
	 * <li>yyyy</li>
	 * <li>yyyy-MM</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy-MM-ddThh</li>
	 * <li>yyyy-MM-ddTHH:mm</li>
	 * <li>yyyy-MM-ddTHH:mm:ss</li>
	 * <li>yyyy-MM-ddTHH:mm:ssZ</li>
	 * <li>yyyy-MM-ddTHH:mm:ss+HH:mm</li>
	 * <li>yyyy-MM-ddTHH:mm:ss-HH:mm</li>
	 * 
	 * <li>yyyy-MM-ddTHH:mm:ss+HHmm</li>
	 * <li>yyyy-MM-ddTHH:mm:ss+HH</li>
	 * <li>yyyy-MM-ddTHH:mm:ss+H</li>
	 * <li>yyyy-MM-ddTHH:mm:ss+H:mm</li>
	 * <li>yyyy-MM-ddTHH:mm:ss+Hmm</li>
	 * 
	 * </ol>
	 * ※ タイムゾーンを省略した場合は、VMデフォルトのタイムゾーンが指定されたとみなします。<br/>
	 * <br/>
	 * 解析の対象外となる条件は以下の通りです。
	 * <ul>
	 * <li>
	 * 	小数値を使用した時刻表現（ピリオド(.) と カンマ(,)を使用した時刻表現は対象外） <br/>
	 * 	&nbsp;&nbsp;&nbsp;<i>→ 「<b>2008-02-28T12:34</b>.5」 は 「<b>2008-02-28T12:34</b>」と判定されます。</i> 
	 * </li>
	 * <li>
	 * 	年と年内の日の番号<br/>
	 * 	&nbsp;&nbsp;&nbsp;<i>→ 「<b>2004-09</b>2」 は 「<b>2004-09</b>」と判定されます。</i> 
	 * </li>
	 * <li>
	 * 	年と週と曜日<br/>
	 * 	&nbsp;&nbsp;&nbsp;<i>→ 「<b>2004</b>-W14-4」 は 「<b>2004</b>」と判定されます。</i> 
	 * </li>
	 * <li>
	 * 	期間<br/>
	 * 	&nbsp;&nbsp;&nbsp;<i>→ 「<b>2004-04-01T12:34:56+09:00</b>/2007-08-31T16:54:32+09:00」 は
	 *  「<b>2004-04-01T12:34:56+09:00</b>」と判定されます。</i> 
	 * </li>
	 * </ul>
	 * 
	 * @param dateString　ISO 8601形式の文字列。
	 * @return 文字列から解析された Date。エラーの場合は null。
	 */
	public static Date parse(String dateString) {
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(dateString);
		
		String[] parts;
		if(matcher.find()){
			int max = matcher.groupCount();
			parts = new String[max];
			
			for(int idx = 0; idx < max; idx++){
				parts[idx] = matcher.group(idx);
			}
		}
		else{
			return null;
		}
		
		logger.trace("[0]{}\n[1]{}\n[2]{}\n[3]{}\n[4]{}\n" +
					 "[5]{}\n[6]{}\n[7]{}\n[8]{}\n[9]{}\n" + 
					 "[10]{}\n[11]{}\n[12]{}\n[13]{}\n[14]{}\n[15]{}\n", parts);

		Calendar cal = Calendar.getInstance();
		cal.clear();

		// タイムゾーン
		if(parts[12] != null){
			// UTC
			if(parts[12].equals("Z")){
				TimeZone timezone = TimeZone.getTimeZone("UTC");
				cal.setTimeZone(timezone);
			}
			else {
				TimeZone timezone = TimeZone.getTimeZone("GMT" + parts[12]);
				cal.setTimeZone(timezone);
			}
		}

		int radix = 10;
		
		// 年
		int idx4Year = 1;
		if(parts[idx4Year] != null){
			cal.set(Calendar.YEAR, Integer.parseInt(parts[idx4Year], radix));
		}
		// 月
		int idx4Month = 3;
		if(parts[idx4Month] != null){
			cal.set(Calendar.MONTH, Integer.parseInt(parts[idx4Month], radix) - 1);
		}
		// 日
		int idx4Date = 5;
		if(parts[idx4Date] != null){
			cal.set(Calendar.DATE, Integer.parseInt(parts[idx4Date], radix));
		}
		// 時
		int idx4HourOfDay = 7;
		if(parts[idx4HourOfDay] != null){
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[idx4HourOfDay], radix));
		}
		// 分
		int idx4Minute = 9;
		if(parts[idx4Minute] != null){
			cal.set(Calendar.MINUTE, Integer.parseInt(parts[idx4Minute], radix));
		}
		// 秒
		int idx4Second = 11;
		if(parts[idx4Second] != null){
			cal.set(Calendar.SECOND, Integer.parseInt(parts[idx4Second], radix));
		}
		return cal.getTime();
	}

	/**
	 * 指定された Date を
	 * <a href="http://ja.wikipedia.org/wiki/ISO_8601">ISO 8601</a>
	 * 形式の文字列にフォーマットします。<br/>
	 * 返却された文字列のタイムゾーンは、常に「UTC」（=時刻の後ろに「Z」が添えられた形式）となります。
	 * 
	 * 
	 * @param date フォーマットする日付/時刻値
	 * @return <a href="http://ja.wikipedia.org/wiki/ISO_8601">ISO 8601</a> 形式の文字列
	 */
	public static String format(Date date){
		if (ISO_8601_FORMAT == null){
			ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			ISO_8601_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		
		String dateString = ISO_8601_FORMAT.format(date);
		return dateString;
	}
	
	private static SimpleDateFormat ISO_8601_FORMAT = null;
		
	/**
	 * <pre>
	[0]2008-02-28T12:34:56+09:00
	[1]2008
	[2]-02-28T12:34:56+09:00
	[3]02
	[4]-28T12:34:56+09:00
	[5]28
	[6]T12:34:56+09:00
	[7]12
	[8]:34:56
	[9]34
	[10]:56
	[11]56
	[12]+09:00
	[13]+09:00
	[14]+
	[15]09

	<hr/>

	[0]2008-02-28T12:34:56Z
	[1]2008
	[2]-02-28T12:34:56Z
	[3]02
	[4]-28T12:34:56Z
	[5]28
	[6]T12:34:56Z
	[7]12
	[8]:34:56
	[9]34
	[10]:56
	[11]56
	[12]Z
	[13]null
	[14]null
	[15]null

	<hr/>
	
	[0]2008-02-28T12:34:56+900
	[1]2008
	[2]-02-28T12:34:56+900
	[3]02
	[4]-28T12:34:56+900
	[5]28
	[6]T12:34:56+900
	[7]12
	[8]:34:56
	[9]34
	[10]:56
	[11]56
	[12]+900
	[13]+900
	[14]+
	[15]9
	 * </pre>
	 */
	private static final String regexp = "([0-9]{4})(-([0-9]{2})(-([0-9]{2})" +
										 "(T([0-9]{2})(:([0-9]{2})(:([0-9]{2}))?)?" +
										 "(Z|(([-+])([0-9]{1,2}):?([0-9]{2})))?)?)?)?";
	

}
