package org.intra_mart.common.aid.jdk.util;

/**
 * 例外をイベントとして扱うためのスケルトン実装を提供します。<P>
 * このインターフェースを実装したクラスのインスタンスは例外の発生に伴い
 * その例外をイベントとして受け取ることができます。
 * このインターフェースを実装しているクラスから生成されたインスタンスの
 * オーナーオブジェクトは、
 * 例外発生をイベントとしてインスタンスに対して通知する義務があります。<BR>
 *
 */
public interface ErrorEventListener{

	/**
	 * 発生した例外を通知します。
	 * @param err 発生したエラーイベント
	 */
	public void handleError(ErrorEvent err);
}
