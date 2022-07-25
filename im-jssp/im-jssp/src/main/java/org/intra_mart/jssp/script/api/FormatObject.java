package org.intra_mart.jssp.script.api;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import org.intra_mart.common.aid.jdk.java.util.LocaleUtil;
import org.intra_mart.jssp.util.JsUtil;
import org.intra_mart.jssp.util.RuntimeObject;
import org.intra_mart.jssp.util.locale.LocaleHandler;
import org.intra_mart.jssp.util.locale.LocaleHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 文字列変換オブジェクト。 <BR>
 * <BR>
 * 文字列変換を行うＡＰＩを持つオブジェクトです。 <BR>
 *
 * @scope public
 * @name Format
 */
public class FormatObject extends ScriptableObject implements Cloneable, java.io.Serializable {

    /**
     * JSSP実行環境への登録用デフォルトコンストラクタ
     */
	public FormatObject(){
		super();
	}

    /**
     * JSSP実行環境下でのオブジェクト名称を取得します
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
    public String getClassName() {
        return "Format";
    }

    /**
     * フォーマット指定に沿った形に日付を文字列に変換します。 <br>
     * <br>
     * <br/>
     * 書式設定時に適用するロケールは、org.intra_mart.jssp.util.locale.LocaleHandlerが返却する値です。<br/>
	 * <br/>
	 * org.intra_mart.jssp.util.locale.LocaleHandler は、<br/>
	 * 「<i>/org/intra_mart/jssp/util/jssp-runtime-classes.xml</i>」の
	 * 「<i>/jssp-runtime-classes/locale-handler</i>」タグで設定します。<br/>
	 * 上記 LocaleHandler の設定を省略した場合、
	 * Java-VMのデフォルトロケールを返却する LocaleHandler が設定されます。<br/>
     * <br/>
     * フォーマット指定文字 <br>
     * <table border='1'>
     * <tr>
     * <th>記号</th>
     * <th>意味</th>
     * </tr>
     * <tr>
     * <td>y</td>
     * <td>年</td>
     * </tr>
     * <tr>
     * <td>M</td>
     * <td>月</td>
     * </tr>
     * <tr>
     * <td>d</td>
     * <td>日</td>
     * </tr>
     * <tr>
     * <td>h</td>
     * <td>午前/午後の時 (1 - 12)</td>
     * </tr>
     * <tr>
     * <td>H</td>
     * <td>一日における時 (0 - 23)</td>
     * </tr>
     * <tr>
     * <td>k</td>
     * <td>一日における時 (1 - 24)</td>
     * </tr>
     * <tr>
     * <td>m</td>
     * <td>分</td>
     * </tr>
     * <tr>
     * <td>s</td>
     * <td>秒</td>
     * </tr>
     * <tr>
     * <td>S</td>
     * <td>ミリ秒</td>
     * </tr>
     * <tr>
     * <td>a</td>
     * <td>午前/午後</td>
     * </tr>
     * <tr>
     * <td>E</td>
     * <td>曜日</td>
     * </tr>
     * <tr>
     * <td>'</td>
     * <td>テキスト用エスケープ</td>
     * </tr>
     * <tr>
     * <td>''</td>
     * <td>単一引用符</td>
     * </tr>
     * </table>
     *
     * @scope public
     * @param format
     *            String フォーマット指定文字列
     * @param date
     *            Date 日付データ
     * @return String 変換後の文字列
     */
    public static String jsStaticFunction_fromDate(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

    	String str = new String();
    	
    	// ロケール未指定
        if ( args.length == 2
        	 &&
        	 args[0] instanceof String
        	 &&
        	 JsUtil.isDate(args[1]) ) {

            try {
            	String format = (String) args[0];
            	Locale locale = LocaleHandlerManager.getLocaleHandler().getLocale();            	
                Date date = JsUtil.toDate(args[1]);
                
                SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
                str = sdf.format(date);
            }
            catch (IllegalArgumentException iae) {
                return str;
            }
        }
        // ロケール指定の場合
        else if(args.length == 3
	        	 &&
	        	 args[0] instanceof String
	        	 &&
	        	 args[1] instanceof String
	        	 &&
	        	 JsUtil.isDate(args[2]) ) {
        	
            try {
            	Locale locale = LocaleUtil.toLocale((String) args[0]);            	
            	String format = (String) args[1];
                Date date = JsUtil.toDate(args[2]);
                
                SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
                str = sdf.format(date);
            }
            catch (IllegalArgumentException iae) {
                return str;
            }
        	
        }
        return str;
    }

    /**
     * フォーマット指定に沿った形に日付を文字列に変換します。 <br/>
	 * 引数 locale で指定された値に則り、国際化されたフォーマット後の文字列を返します。<br/>
	 * フォーマット指定文字の説明は、<a href="#fromDateStringDate">Format.fromDate()</a>を参照してください。
	 * 
	 * @scope public
     * @param locale
     * 				String ロケール
     * @param format
     * 				String フォーマット指定文字列
     * @param date
     * 				Date 日付データ
     * @return String 変換後の文字列
     */
    public static String fromDate(String locale, String format, Date date){
    	// APIリストダミー用
    	return null;
    }
    
    /**
     * フォーマット指定に沿った日付文字列データをDate型オブジェクトへ変換します。<br/>
     * 変換に失敗した場合は、nullを返却します。<br/>
	 * フォーマット指定文字の説明は、<a href="#fromDateStringDate">Format.fromDate()</a>を参照してください。
     * 
     * @scope public
     * @param format String フォーマット指定文字列
     * @param src String 日付文字列データ
     * @return Date 変換後のDate型オブジェクト
     */
    public static Scriptable jsStaticFunction_toDate(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
    	
    	String format;
    	String src;
    	
    	// 引数チェック
    	if (args.length == 2 &&
    		args[0] instanceof String &&
    		args[1] instanceof String) {
    		format = (String)args[0];
    		src = (String)args[1];
    	} else {
    		return null;
    	}
    	
    	// 変換
    	try {
    		DateFormat fmt = new SimpleDateFormat(format);
    		Date date = fmt.parse(src);
    		long time = date.getTime();
        	return RuntimeObject.newDate(time);
    	} catch (Exception e) {
    		return null;
    	}
    }
    
    /**
     * フォーマット指定に沿った形に数値を文字列に変換します。 <br>
     * <br>
     * フォーマット指定文字 <br>
     * 0 - 数字 <br># - 数字。ゼロだと表示されない <br>. - 数値桁区切り文字の位置 <br>, - グループ区切り文字位置
     * <br>
     *
     * @scope public
     * @param format
     *            String フォーマット指定文字列
     * @param value
     *            Number 数値データ
     * @return String 変換後の文字列
     */
    public static String jsStaticFunction_fromNumber(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        String str = new String();
    	String pattern = null;
        Number num = null;
        if (args.length == 2
        	&&
        	args[0] instanceof String
        	&&
        	args[1] instanceof Number) {

            pattern = (String) args[0];
            num = (Number) args[1];

        }
        else {
            return str;
        }

        try {
            DecimalFormat df = new DecimalFormat(pattern);
            str = df.format(num);
        }
        catch (IllegalArgumentException iae) {
            return str;
        }

        return str;
    }

    /**
     * ３桁ずつのカンマ区切り数字列を生成します。 <br>
     * <br>
     * 少数部分の値は２桁までの表示となります。 <br>
     *
     * @scope public
     * @param value
     *            Number 数値データ
     * @return String 変換された文字列
     */
    public static String jsStaticFunction_toMoney(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        if (args.length == 1
        	&&
        	args[0] instanceof Number) {

            Number num = (Number) args[0];
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            return nf.format(num);
        }
        else {
            return "";
        }
    }

    /**
     * 指定された書式の文字列と引数を使って、フォーマットされた文字列を返します。<br/>
     * <br/>
     * 引数 format に指定された書式文字列は、固定のテキストと 1 つ以上の埋め込まれた「書式指示子」を含めることができます。<br>
     * <br/>
     * 引数 arg に指定できる型は、String, Number, Date, Boolean です。<br/>
     * 引数 arg にNumber型の値が指定された場合、<br/>
     * その値が 1 で割り切れる場合は整数として扱われ、<br/>
     * その値が 1 で割り切れない場合は浮動小数点として扱われます。<br/>
     * （整数用の書式指示子に対して浮動小数点を割り当てた場合、<br/>
     * 　または、浮動小数点用の書式指示子に対して整数を割り当た場合には、実行時エラーが発生します）<br/>
     * <br/>
     * 書式設定時に適用するロケールは、org.intra_mart.jssp.util.locale.LocaleHandlerが返却する値です。<br/>
	 * <br/>
	 * org.intra_mart.jssp.util.locale.LocaleHandler は、<br/>
	 * 「<i>/org/intra_mart/jssp/util/jssp-runtime-classes.xml</i>」の
	 * 「<i>/jssp-runtime-classes/locale-handler</i>」タグで設定します。<br/>
	 * 上記 LocaleHandler の設定を省略した場合、
	 * Java-VMのデフォルトロケールを返却する LocaleHandler が設定されます。<br/>
     * <br/>
     * 本関数は、Javaのjava.util.Formatterを使用しています。<br/>
     * 書式文字列の構文や、書式指示子の説明は
     * <a href="http://java.sun.com/j2se/1.5.0/ja/docs/ja/api/java/util/Formatter.html#syntax">
     * 	書式設定の概要
     * </a>
     * を参照してください。<br/>
     * <br/>
     * 以下にサンプルを示します。<br/>
     * <pre>
     * 	// String型
     * 	Format.format("逆順に表示されます。%4$2s %3$2s %2$2s %1$2s", "a", "b", "c", "d");
     *
     * 	// Number型
     * 	Format.format("%sは、10進数で「%&lt;d」, 16進数で「%&lt;x」, 8進数で「%&lt;o」です。", 256);
     * 	Format.format("123.456を20桁(小数点第5位)で表示します。→[%20.5f]", 123.456);
     * 	Format.format("通常、「%10d」、左揃え「%&lt;-10d」", 512);
     * 	Format.format("常に符号が含まれます。正数「%+d」、負数「%+d」", 1234, -5678);
     *
     *	// Date型
     *	var now = new Date();
     *	Format.format("『%tH』：24 時間制の時（必要に応じて 0 を先頭に追加し、2 桁で表現 (00 - 23)）", now);
     *	Format.format("『%tI』：12 時間制の時（必要に応じて 0 を先頭に追加し、2 桁で表現 (00 - 12)）", now);
     *	Format.format("『%tk』：24 時間制の時", now);
     *	Format.format("『%tl』：12 時間制の時", now);
     *	Format.format("『%tM』：分（必要に応じて 0 を先頭に追加し、2 桁で表現(00 - 59)）", now);
     *	Format.format("『%tS』：秒（必要に応じて 0 を先頭に追加し、2 桁で表現(00 - 60)）", now);
     *	Format.format("『%tp』：午前または午後を表すロケール固有の小文字のマーカ (例、「am」や「pm」)", now);
     *	Format.format("『%tB』：ロケール固有の月の完全な名前", now);
     *	Format.format("『%tb』：ロケール固有の月の省略名", now);
     *	Format.format("『%tA』：ロケール固有の曜日の完全な名前", now);
     *	Format.format("『%ta』：ロケール固有の曜日の短縮名", now);
     *	Format.format("『%tY』：年。必要に応じて 0 を先頭に追加し、4 桁以上で表現", now);
     *	Format.format("『%ty』：年の下 2 桁。（必要に応じて 0 を先頭に追加 (00 - 99)）", now);
     *	Format.format("『%tm』：月。（必要に応じて 0 を先頭に追加し、2 桁で表現）", now);
     *	Format.format("『%td』：月の何日目かを表す日。（必要に応じて 0 を先頭に追加し、2 桁で表現(01 -31)）", now);
     *	Format.format("『%te』：月の何日目かを表す日。（最大 2 桁で表現します (1 - 31)）", now);
     *	Format.format("『%tT』：「%%tH:%%tM:%%tS」として 24 時間制で書式設定された時刻", now);
     *	Format.format("『%tr』：「%%tI:%%tM:%%tS %%Tp」として 12 時間制で書式設定された時刻", now);
     *	Format.format("『%tD』：「%%tm/%%td/%%ty」として書式設定された日付", now);
     *	Format.format("『%tF』：「%%tY-%%tm-%%td」として書式設定された、ISO 8601 に準拠した日付", now);
     *
     *	// Boolean型
     *	Format.format("Boolean型を表示します。→%B, %b, %S, %s", true, true, true, true);
     * </pre>
     *
     * @scope public
     * @param format
     *            String 書式文字列
     * @param arg
     *            Object 書式文字列の書式指示子により参照される引数。
     * @param ...
     *            ... 複数指定する場合は、カンマの後に続けて指定します。
     *
     * @return String フォーマットされた文字列
     *
     * @see Formatter
     */
    public static String jsStaticFunction_format(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

    	
        if (args.length < 1
        	||
        	!(args[0] instanceof String)){
            return "";
        }

        String format = (String) args[0];

        // 第2引数以降を走査
        int secondArgIndex = 1;
        Object[] convertedArgs = new Object[args.length - secondArgIndex];

        for(int idx = secondArgIndex; idx < args.length; idx++){

    		convertedArgs[idx - secondArgIndex] = args[idx];

        	// 日付型
    		if(JsUtil.isDate(args[idx])){
        		convertedArgs[idx - secondArgIndex] = JsUtil.toDate(args[idx]);
        	}
        	// 数値型
        	else if(args[idx] instanceof Double){
        		Double doubleNum = (Double) args[idx];

        		// 整数の場合
        		if(doubleNum % 1 == 0){
            		convertedArgs[idx - secondArgIndex] = doubleNum.longValue();
        		}
        	}
        }
        
        // 変換
        LocaleHandler localeHandler = LocaleHandlerManager.getLocaleHandler();
        return format(format, localeHandler.getLocale(), convertedArgs);

    }

   /**
    * 指定された書式の文字列と引数を使って、フォーマットされた文字列を返します。<br/>
    * 引数 locale で指定された値に則り、国際化されたフォーマット後の文字列を返します。<br/>
    * <br/>
    * 書式文字列の構文や書式指示子の説明は、{@link Format#format}を参照してください。
    * 
     * <br/>
     * 以下にサンプルを示します。<br/>
     * <pre>
     *	// Date型
     *	var now = new Date();
     *	Format.formatLocale("ja_JP", "『%tp』：午前または午後を表すロケール固有の小文字のマーカ (例、「am」や「pm」)", now);
     *	Format.formatLocale("ja_JP", "『%tB』：ロケール固有の月の完全な名前", now);
     *	Format.formatLocale("ja_JP", "『%tb』：ロケール固有の月の省略名", now);
     *	Format.formatLocale("ja_JP", "『%tA』：ロケール固有の曜日の完全な名前", now);
     *	Format.formatLocale("ja_JP", "『%ta』：ロケール固有の曜日の短縮名", now);
     *	Format.formatLocale("ja_JP", "『%tr』：「%%tI:%%tM:%%tS %%Tp」として 12 時間制で書式設定された時刻", now);
     *
     *	Format.formatLocale("en_US", "『%tp』：Locale-specific morning or afternoon marker in lower case (e.g."am" or "pm")", now);
     *	Format.formatLocale("en_US", "『%tB』：Locale-specific full month name", now);
     *	Format.formatLocale("en_US", "『%tb』：Locale-specific abbreviated month name", now);
     *	Format.formatLocale("en_US", "『%tA』：Locale-specific full name of the day of the week", now);
     *	Format.formatLocale("en_US", "『%ta』：Locale-specific short name of the day of the week", now);
     *	Format.formatLocale("en_US", "『%tr』：Time formatted for the 12-hour clock as "%%tI:%%tM:%%tS %%Tp"", now);
     * </pre>
     * 
    * @scope public
    * @param locale
    *            String ロケール
    * @param format
    *            String 書式文字列
    * @param arg
    *            Object 書式文字列の書式指示子により参照される引数。
    * @param ...
    *            ... 複数指定する場合は、カンマの後に続けて指定します。
    *
    * @return String フォーマットされた文字列
    *
    * @see Formatter
    */
    public static String jsStaticFunction_formatLocale(Context cx, Scriptable thisObj, Object[] args, Function funObj) {

        if (args.length < 2
        	||
        	!(args[0] instanceof String)
        	||
        	!(args[1] instanceof String) ) {
            return "";
        }

        Locale locale = LocaleUtil.toLocale((String) args[0]);
        String format = (String) args[1];
        
        // 第3引数以降を走査
    	int thirdArgIndex = 2;
        Object[] convertedArgs = new Object[args.length - thirdArgIndex];

        for(int idx = thirdArgIndex; idx < args.length; idx++){

    		convertedArgs[idx - thirdArgIndex] = args[idx];

        	// 日付型
    		if(JsUtil.isDate(args[idx])){
        		convertedArgs[idx - thirdArgIndex] = JsUtil.toDate(args[idx]);
        	}
        	// 数値型
        	else if(args[idx] instanceof Double){
        		Double doubleNum = (Double) args[idx];

        		// 整数の場合
        		if(doubleNum % 1 == 0){
            		convertedArgs[idx - thirdArgIndex] = doubleNum.longValue();
        		}
        	}
        }
        
        // 変換
        return format(format, locale, convertedArgs);
    }
    
	/**
	 * @param format
	 * @param locale
	 * @param args
	 * @return
	 */
	private static String format(String format, Locale locale, Object[] args) {
		StringBuffer sb = new StringBuffer();
        Formatter formatter = new Formatter(sb, locale);
        formatter.format(format, args);
        return sb.toString();
	}
    
    
    
}