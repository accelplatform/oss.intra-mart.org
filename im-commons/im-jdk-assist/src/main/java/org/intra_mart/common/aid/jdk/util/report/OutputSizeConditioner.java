package org.intra_mart.common.aid.jdk.util.report;


/**
 * このクラスは、メッセージファイルのサイズによりリフレッシュイベントを
 * 発生させるためのヘルパークラスです。<br>
 * このクラスは、FileReporter と組み合わせて利用します。
 * このクラスのオブジェクトを FileReporter オブジェクトにセットすることで、
 * メッセージファイルが一定サイズ以上になったときに、
 * メッセージファイルのローテイト機能イベントを発生させることができます。
 *
 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
 */
public class OutputSizeConditioner implements ReportFileConditioner{
	private long MAX_OUTPUT_SIZE;		// チェック基準となるデータ出力量

	/**
	 * メッセージファイルへのデータ出力量によりリフレッシュイベントを
	 * 発生させるためのヘルパーオブジェクトを構築します。<br>
	 * このオブジェクトは、ログファイルへ書き出したメッセージの量が
	 * 一定(max)以上の大きさに
	 * なった場合にリフレッシュイベント発生を通知します。
	 * @throws IllegalArgumentException max がゼロ以下の場合
	 */
	public OutputSizeConditioner(long max) throws IllegalArgumentException{
		if(max > 0){
			this.MAX_OUTPUT_SIZE = max;
		}
		else{
			throw new IllegalArgumentException("Size specification is unsuitable");
		}
	}

	/**
	 * メッセージファイルを再作成する必要があるかどうかを判定します。<br>
	 * ファイルへの出力文字数が、このオブジェクトの作成時に指定された
	 * 値以上の場合 true を返します。
	 *
	 * @param writer 出力ストリーム
	 * @return メッセージファイルの再作成が必要と判断された場合は true。
	 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
	 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
	 */
	public boolean expiration(ReportFileWriter writer){
		return writer.length() > this.MAX_OUTPUT_SIZE;
	}
}

