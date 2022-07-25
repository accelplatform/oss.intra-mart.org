package org.intra_mart.common.aid.jdk.util;


/**
 * 例外をイベントとして扱うためのプロトコルです。<P>
 * イベントリスナー(インターフェース ErrorEventListener を実装したクラス)
 * のオーナーオブジェクトは、例外の発生時にこのクラスのインスタンスを
 * 生成して各リスナーに通知する義務があります。<BR>
 * 
 * @see org.intra_mart.common.aid.jdk.util.ErrorEventListener
 */
public class ErrorEvent{
	private Throwable ERROR_OR_EXCEPTION;
	private String MESSAGE;
	private Object PROBLEM_OBJECT;

	/**
	 * エラーを通知するためのイベントを構築します。<P>
	 * obj パラメータはエラーの発生したクラスまたはインスタンスです。
	 *
	 * @param t 発生した例外
	 * @param obj エラーの発生したオブジェクト
	 */
	public ErrorEvent(Throwable t, Object obj){
		this.ERROR_OR_EXCEPTION = t;
		this.PROBLEM_OBJECT = obj;
	}

	/**
	 * エラーを通知するためのイベントを構築します。<P>
	 * obj パラメータはエラーの発生したクラスまたはインスタンスです。
	 *
	 * @param t 発生した例外
	 * @param msg エラーメッセージ
	 * @param obj エラーの発生したオブジェクト
	 */
	public ErrorEvent(Throwable t, String msg, Object obj){
		this(t, obj);
		this.MESSAGE = msg;
	}

	/**
	 * 通知されたエラー発生オブジェクトを取得します。
	 * @return 通知されたエラーオブジェクト
	 */
	public Object getObject(){
		return PROBLEM_OBJECT;
	}

	/**
	 * 通知されたエラーメッセージを取得します。
	 * メッセージが通知されていない場合、通知されたエラーのメッセージを
	 * 返します。
	 * @return 通知されたエラーメッセージ
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		if(MESSAGE != null){
			return MESSAGE;
		}
		else{
			return getException().getMessage();
		}
	}

	/**
	 * 通知されたエラーまたは例外を取得します。
	 * @return 通知されたエラーまたは例外
	 */
	public Throwable getException(){
		return ERROR_OR_EXCEPTION;
	}
}
