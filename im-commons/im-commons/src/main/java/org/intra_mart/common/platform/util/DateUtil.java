package org.intra_mart.common.platform.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日付操作ユーティリティクラスです。<BR>
 * <BR>
 * 日付に関する操作をするためのユーティリティクラスです。<BR>
 * このクラスのメソッドはすべてstaticメソッドとなっています。<BR>
 * このクラスのインスタンスを生成することはできません。
 *
 */

public class DateUtil {

    private static SimpleDateFormat imDateFormat = new SimpleDateFormat("yyyy/MM/dd|HH:mm:ss");

    /** システムで許可されている最小日時 */
    private static final Date MIN_DATE;

    /** システムで許可されている最大日時 */
    private static final Date MAX_DATE;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1582, 9, 15, 0, 0, 0);
        MIN_DATE = calendar.getTime();
        calendar.set(9999, 11, 31, 23, 59, 59);
        MAX_DATE = calendar.getTime();
    }

    /**
     * コンストラクタです。<BR>
     * <BR>
     * 生成はできません。
     */
    private DateUtil() {
        super();
    }

    /**
     * データベース登録用文字列を取得します。
     *
     * @param date 変換元のDateオブジェクト
     * @return データベース登録用文字列(YYYY/MM/DD|hh:mm:ss)
     */
    public static synchronized String dateToString(Date date) {
        return DateUtil.imDateFormat.format(date);
    }

    /**
     * データベース登録用文字列からjava.util.Dateを取得します。
     *
     * @param dateString 変換元のデータベース登録用文字列(YYYY/MM/DD|hh:mm:ss)<br>
     * 			年月日のみ(YYYY/MM/DD形式)で指定した場合、YYYY/MM/DD|00:00:00として変換します。
     * @return Dateオブジェクト
     */
    public static synchronized Date stringToDate(String dateString) {
    	
    	if(dateString != null && dateString.length() == 10){
	    	dateString = dateString.concat("|00:00:00");
    	}
    	else if(dateString != null && dateString.length() == 11){
	    	dateString = dateString.concat("00:00:00");
    	}
    	else if(dateString != null && dateString.length() == 13){
	    	dateString = dateString.concat(":00:00");
    	}
    	else if(dateString != null && dateString.length() == 14){
	    	dateString = dateString.concat("00:00");
    	}
    	else if(dateString != null && dateString.length() == 16){
	    	dateString = dateString.concat(":00");
    	}
    	else if(dateString != null && dateString.length() == 17){
	    	dateString = dateString.concat("00");
    	}
    	
       	try {
            return DateUtil.imDateFormat.parse(dateString,new ParsePosition(0));
        } catch (Exception e) {
	        return null;
        }
    }
    /**
     * 日付のみのjava.util.Dateに変換します。<BR>
     * <BR>
     * 時刻は、00:00:00.000に設定されます。
     * @param date 1970 年 1 月 1 日 00:00:00 GMT からのミリ秒数
     * @return 日付のみのjava.util.Date
     */
    public static Date convertOnlyDate(long date) {
        return convertOnlyDate(new Date(date));
    }

    /**
     * 日付のみのjava.util.Dateに変換します。<BR>
     * <BR>
     * 時刻は、00:00:00.000に設定されます。
     * @param date 日付
     * @return 日付のみのjava.util.Date
     */
    public static Date convertOnlyDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTime();
    }

    /**
     * 日付と時刻のみのjava.util.Dateに変換します。<BR>
     * <BR>
     * ミリ秒の単位は 0 に設定されます。
     * @param date 1970 年 1 月 1 日 00:00:00 GMT からのミリ秒数
     * @return 日付と時刻のみのjava.util.Date
     */
    public static Date convertOnlyDateTime(long date) {
        return convertOnlyDateTime(new Date(date));
    }

    /**
     * 日付と時刻のみのjava.util.Dateに変換します。<BR>
     * <BR>
     * ミリ秒の単位は 0 に設定されます。
     * @param date 日付
     * @return 日付と時刻のみのjava.util.Date
     */
    public static Date convertOnlyDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTime();
    }

    /**
     * システムで許可されている最小日時を取得します。
     *
     * @return システムで許可されている最小日時
     */
    public static Date getMinDate() {
        return (Date) MIN_DATE.clone();
    }

    /**
     * システムで許可されている最大日時を取得します。
     *
     * @return システムで許可されている最大日時
     */
    public static Date getMaxDate() {
        return (Date) MAX_DATE.clone();
    }
}