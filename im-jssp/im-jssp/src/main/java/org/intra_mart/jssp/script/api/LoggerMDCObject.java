package org.intra_mart.jssp.script.api;

import org.intra_mart.common.platform.log.MDC;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * ログ出力用のMDCオブジェクト。<br/>
 * 
 * このオブジェクトは、ロギングシステムの
 * MDC(=Mapped Diagnostic Context(マップ化された診断コンテキスト))の隠蔽、および、代行を行います。
 * <p>
 * 設定ファイル（例：im_logger.xml）のレイアウト設定で「X{key}」または「mdc{key}」を記述することにより、
 * 独自に定義したkeyで保存した情報をログに出力することが可能となります。
 * (設定方法は、ロギングAPIの実装ライブラリに依存します。)
 * </p>
 * 
 * <br/>
 * ●サンプルコード
 * <pre>
 *    1: // MDCに情報を保存
 *    2: LoggerMDC.put(<font color="red"><b>"user_application_key"</b></font>, "MDCに値を設定しました。");
 *    3: 
 *    4: // ログの出力
 *    5: Logger.info("処理を終了しました。");
 * </pre>
 * 
 * <br/>
 * ●設定ファイル「im_logger_XXXX.xml」
 * <pre>
 *    1: &lt;appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender"&gt;
 *    2:     &lt;layout class="ch.qos.logback.classic.PatternLayout"&gt;
 *    3:         &lt;pattern&gt;[%level] %logger{10} - <font color="red"><b>%X{user_application_key}</b></font> %msg%n&lt;/pattern&gt;
 *    4:     &lt;/layout&gt;
 *    5: &lt;/appender&gt;
 * </pre>
 * 
 * <h2>MDC利用時の注意事項</h2>
 * <ul>
 * 	<li>MDCは、スレッド単位で情報を保存します。</li>
 * 	<li>
 * 		MDCに保存した内容は、明示的に初期化が行われない限り、値が初期化されることはありません。
 * 		そのため、必要に応じて初期化処理（MDC.remove(key)）を行う必要があります。
 *	</li>
 * 	<li>
 * 		intra-mart WebPlatform/AppFrameworkでは、製品の標準ログ出力機能にてMDCを利用しています。
 * 		そのため、同一のkeyを使用した場合、正常にログが出力されなくなります。
 * 	</li>
 * </ul>
 * 
 * MDC に関しては、intra-mart WebPlatform/AppFramework の ログ設定ガイド、
 * および、LogBackマニュアルの
 * <a href="http://logback.qos.ch/manual/mdc.html">chapter on MDC</a> を参照してください。
 * 
 * @scope public
 * @name LoggerMDC
 */
public class LoggerMDCObject extends ScriptableObject {

	/* (非 Javadoc)
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName(){
		return "LoggerMDC";
	}

	
	/**
	 * 指定された値 と 指定されたキー を現在のスレッドのコンテキストマップに設定します。
	 * <p>
	 * 引数 <code>key</code> に null を指定することはできません。<br/>
	 * 引数 <code>val</code> は、null を指定することが可能です。(ロギングシステムの実装がサポートしている場合に限る)<br/>
	 * </p>
	 * <p>
	 * このメソッドは、すべての処理を、ロギングシステムの MDC に委譲します。
	 * </p>
	 * 
	 * @scope public
	 * @param key String 指定される値が関連付けられるキー
	 * @param val String 指定されるキーに関連付けられる値
	 */
	public static void jsStaticFunction_put(Object key, Object val) {
		if(key instanceof Undefined || val instanceof Undefined){
    		throw new IllegalArgumentException("parameter cannot be undefined");
		}
		
		MDC.put((String)key, (String)val);
	}
	
	
	/**
	 * 引数 <code>key</code> に関連付けられた値を取得します。
	 * <p>
	 * 引数 <code>key</code> に null を指定することはできません。
	 * </p>
	 * <p>
	 * このメソッドは、すべての処理を、ロギングシステムの MDC に委譲します。
	 * </p>
	 * 
	 * @scope public
	 * @param key　String キー
	 * @return String　
	 * 				引数　<code>key</code>　に関連付けられた値
	 */
	public static Object jsStaticFunction_get(Object key) {
		if(key instanceof Undefined){
    		throw new IllegalArgumentException("parameter cannot be undefined");
		}
		
		return (String) MDC.get((String)key);
	}

	/**
	 * 引数 <code>key</code> に関連付けられた値を削除します。
	 * <p>
	 * 引数 <code>key</code> に null を指定することはできません。<br/>
	 * 引数 <code>key</code> に関連する値が存在しない場合、このメソッドは何も行いません。<br/>
	 * </p>
	 * <p>
	 * このメソッドは、すべての処理を、ロギングシステムの MDC に委譲します。
	 * </p>
	 * @scope public
	 * @param key String　キー
	 */
	public static void jsStaticFunction_remove(Object key) {
		if(key instanceof Undefined){
    		throw new IllegalArgumentException("parameter cannot be undefined");
		}
		
		MDC.remove((String)key);
	}

	/**
	 * MDCに設定されているすべてのエントリーを削除します。
	 * <p>
	 * このメソッドは、すべての処理を、ロギングシステムの MDC に委譲します。
	 * </p>
	 * @scope public
	 */
	public static void jsStaticFunction_clear() {
		MDC.clear();
	}
}