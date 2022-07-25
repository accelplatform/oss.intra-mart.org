package org.intra_mart.common.aid.jdk.util.report;


/**
 * このクラスは、ReportFileWriter の初期化チェックのインターフェースです。<p>
 * このクラスは FileReporter クラスにより ReportFileWriter へのメッセージ
 * 書き出し前に呼び出されます。<br>
 * このクラスは、FileReporter および ReportFileWriter オブジェクトと
 * 組み合わせて使います。
 * これらのクラスと連携することにより、メッセージファイルの
 * ローテイト機能などを実装することができます。
 *
 */
public interface ReportFileConditioner{
	/**
	 * メッセージファイルを再作成する必要があるかどうかを判定します。<br>
	 * このメソッドは、FileReporter クラスにより呼び出されます。
	 * 真値(true)を返すと、ReportFileWriter は現在のログファイルに対して
	 * 初期化処理を行います。
	 * メッセージファイルの初期化処理に関しては、
	 * ReportFileWriter クラス(または、そのサブクラス)により行われます。
	 *
	 * @param writer メッセージファイルへの出力ストリーム
	 * @return メッセージファイルの再作成が必要と判断された場合は true。
	 */
	public boolean expiration(ReportFileWriter writer);
}

