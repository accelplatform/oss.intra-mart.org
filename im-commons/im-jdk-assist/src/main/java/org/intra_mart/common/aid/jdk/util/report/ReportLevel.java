package org.intra_mart.common.aid.jdk.util.report;

/**
 * メッセージレベルのインターフェースです。<p>
 * このインターフェースは、定数値化したメッセージレベルを示します。
 * すべてのメッセージに関して、必ずしもこのインターフェースで定義されている
 * レベル値を使う必要はありませんが、このインターフェースを利用することにより
 * プログラム全体でレベル値を統一することができます。
 *
 */
public interface ReportLevel{
	/**
	 * レベル値として使用可能な最小値
	 * この値は、java.lang.Integer.MIN_VALUE と同じです。
	 */
	public static final int MIN_VALUE = Integer.MIN_VALUE;

	/**
	 * レベル値として使用可能な最大値
	 * この値は、java.lang.Integer.MAX_VALUE と同じです。
	 */
	public static final int MAX_VALUE = Integer.MAX_VALUE;

	/**
	 * デバッグレベル。
	 * デバッグ情報を出力する場合に利用します。
	 */
	public static final int TYPE_DEBUG = 0;

	/**
	 * 通常の情報レベル。<br>
	 * 常に以下の関係が成り立ちます。<p>
	 * TYPE_DEBUG < TYPE_REPORT
	 */
	public static final int TYPE_REPORT = 1;

	/**
	 * 警告レベル。
	 * 警告メッセージ出力時に利用します。<br>
	 * 常に以下の関係が成り立ちます。<p>
	 * TYPE_REPORT < TYPE_WARNING
	 */
	public static final int TYPE_WARNING = 2;

	/**
	 * エラーレベル。
	 * エラーメッセージ出力時に利用します。<br>
	 * 常に以下の関係が成り立ちます。<p>
	 * TYPE_WARNING < TYPE_ERROR
	 */
	public static final int TYPE_ERROR = 3;
}
