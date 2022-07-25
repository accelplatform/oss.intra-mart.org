package org.intra_mart.jssp.script.api;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import org.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import org.intra_mart.jssp.util.RuntimeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

/**
 * セッション情報を操作するオブジェクト。<br/>
 * <br/>
 * 現在のスレッドに関連付けられているリクエストに関して、<br/>
 * そのリクエストに関連付けられたセッションを操作するオブジェクトです。<br/>
 * <br/>
 * 現在のスレッドに関連付けられているリクエストがセッションを持たない場合、
 * 新しいセッションを生成し、該当操作を行います。<br/>
 * <br/>
 * このSessionオブジェクトを利用するためには、<br/>
 * org.intra_mart.common.aid.jsdk.javax.servlet.filter.HTTPContextHandlingFilter を
 * web.xmlにフィルタとして設定する必要があります。<br/>
 * 
 * @scope public
 * @name Session
 */
public class SessionObject extends ScriptableObject implements Serializable {
		
    /**
     * JSSP実行環境への登録用デフォルトコンストラクタ。
     */
    public SessionObject() {
        super();
    }

    /**
     * JSSP実行環境下でのオブジェクト名称を取得します。
     *
     * @return String JSSP実行環境下でのオブジェクト名称
     */
    public String getClassName() {
        return "Session";
    }

    /**
	 * 現在のスレッドに関連付けられているリクエストに関して、
	 * そのリクエストに関連付けられた現在のセッションを返します。
	 * リクエストがセッションを持たない場合は新しいセッションを生成します。
	 * 
	 * @return このリクエストに関連づけられているHttpSession
	 */
	private static HttpSession getSession(){
		HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
		return httpContext.getSession();
	}


	/**
     * 現在のセッションを無効化し、
     * バインドされていたすべてのオブジェクトをアンバインドします。<br/>
     *
     * @scope public
     */
    public static void jsStaticFunction_invalidate()  {
    	HttpSession session = getSession();
		session.invalidate();
    }

    /**
     * 指定された名前で現在のセッションにバインドされたオブジェクトを返します。<br/>
     * その名前でバインドされたオブジェクトがない場合は、null を返します。
     *
     * @scope public
     * @param name
     *            String オブジェクトの名前を指定する文字列
     * @return Object 
     * 				指定された名前のオブジェクト
     */
    public static Object jsStaticFunction_getAttribute(String name) {
    	HttpSession session = getSession();
		return session.getAttribute(name);
    }

    /**
     * 現在のセッションに割り当てられた一意の識別子が格納された文字列を返します。<br/>
     * この識別子はサーブレットコンテナによって割り当てられ、実装に依存します。
     *
     * @scope public
     * @return String 
     * 				現在のセッションに割り当てられた識別子を指定する文字列
     */
    public static String jsStaticFunction_getId() {
    	HttpSession session = getSession();
        return session.getId();
    }

    /**
     * 現在のセッションにバインドされたすべてのオブジェクトの名前を取得します。
     *
     * @scope public
     * @return Array 
     * 				現在のセッションにバインドされたすべてのオブジェクトの名前一覧
     */
    public static Scriptable jsStaticFunction_getAttributeNames() {

    	Set<String> keySet = new HashSet<String>();
    	
    	HttpSession session = getSession();
		Enumeration cursor = session.getAttributeNames();
		
		while(cursor.hasMoreElements()){
			keySet.add((String) cursor.nextElement());
		}
    	
        return RuntimeObject.newArrayInstance(keySet.toArray());
    }

    /**
     * クライアントのアクセスとアクセスの間に、
     * サーブレットコンテナが現在のセッションを保ち続ける最大の時間間隔を秒数で返します。<br/>
     * 
     * この間隔が経過した後に、サーブレットコンテナはセッションを無効化します。<br/>
     * 最大の時間間隔は、setMaxInactiveInterval メソッドで設定できます。<br/>
     * 負の値は、セッションがタイムアウトにならないことを示します。
     *
     * @scope public
     * @return Number 
     * 				現在のセッションが、クライアントの要求と要求との間で保持され続ける秒数を指定する整数
     */
    public static double jsStaticFunction_getMaxInactiveInterval() {
    	HttpSession session = getSession();
    	return session.getMaxInactiveInterval();
    }

    /**
     * 指定された名前でバインドされたオブジェクトを現在のセッションから削除します。<br/>
     * セッションに指定された名前でバインドされたオブジェクトがない場合、このメソッドは何も実行しません。
     *
     * @scope public
     * @param name
     *            String 現在のセッションから削除されるオブジェクトの名前
     */
    public static void jsStaticFunction_removeAttribute(String name) {
    	HttpSession session = getSession();
        session.removeAttribute(name);
    }


    /**
     * 指定された名前を使用して、オブジェクトを現在のセッションにバインドします。 <br/>
     * <br/>
     * 同じ名前のオブジェクトがすでにセッションにバインドされている場合は、オブジェクトが置き換えられます。<br/>
     * 渡された値が null または undefined の場合の結果は、removeAttribute() を呼び出した場合と同じです。<br/>
     * <br/>
     * 保存可能なデータは、プログラム中で自作したオブジェクト（Object,String,Arrayなどのインスタンス）です。<br/>
     * なお、Requestオブジェクト や Module.XXXXオブジェクト 等の、
     * JSSP標準の組み込みオブジェクトは保存できません。
     *
     * @scope public
     * @param name
     *            String オブジェクトがバインドされる名前。null であってはならない
     * @param value
     *            Object バインドされるオブジェクト
     */
    public static void jsStaticFunction_setAttribute(String name, Object value) {
    	HttpSession session = getSession();
    	
		if( (value != null) && !(value instanceof Undefined) ){
	        session.setAttribute(name, value);
		}
		else{			
	        session.removeAttribute(name);
		}
    }    

    /**
     * 現在のセッションが作成された時刻を、GMT 1970年 1 月 1 日 0 時からのミリ秒単位で返します。
     * 
     * @scope public
     * @return Number 
     * 				GMT 1970年 1 月 1 日 からのミリ秒単位で表した、現在のセッションが作成された時刻を示す数値
     */
    public static double jsStaticFunction_getCreationTime() {
    	HttpSession session = getSession();
    	return session.getCreationTime();
    }    

    
    /**
     * 現在のセッションに関連付けられた要求をクライアントが最後に送信した時刻を
     * GMT 1970 年 1 月 1 日 0 時からのミリ秒数として返します。<br/>
     * 返される時刻は実際にはコンテナが要求を受信した時刻を指します。<br/>
     * <br/>
     * セッションに関連付けられた値の取得や設定といった操作をアプリケーションが実行しても、
     * このアクセス時刻は更新されません。<br/>
     * 
     * @scope public
     * @return Number 
     * 				現在のセッションに関連付けられた要求をクライアントが最後に送信した時刻を
     * 				GMT 1970 年 1 月 1 日からのミリ秒数で表現した数値
     */
    public static double jsStaticFunction_getLastAccessedTime() {
    	HttpSession session = getSession();
    	return session.getLastAccessedTime();
    }

    
    /**
     * サーブレットコンテナが現在のセッションを無効化するまでの、
     * クライアントの要求と要求の間の時間を秒数で指定します。<br/>
     * 負の値を指定すると、セッションがタイムアウトになることはありません。
     * 
     * @scope public
     * @param interval Number 秒数を指定する整数
     */
    public static void jsStaticFunction_setMaxInactiveInterval(int interval) {
    	HttpSession session = getSession();
    	session.setMaxInactiveInterval(interval);
    }
}