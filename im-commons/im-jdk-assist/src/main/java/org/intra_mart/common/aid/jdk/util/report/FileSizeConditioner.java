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
public class FileSizeConditioner implements ReportFileConditioner{
	private long MAX_FILE_SIZE;		// チェック基準となるファイルサイズ

	/**
	 * メッセージファイルのサイズによりリフレッシュイベントを発生させるための
	 * ヘルパーオブジェクトを構築します。<br>
	 * このオブジェクトは、メッセージファイルが指定の大きさ(max)以上に
	 * なった場合にリフレッシュイベント発生を通知します。
	 * @throws IllegalArgumentException max がゼロ以下の場合
	 */
	public FileSizeConditioner(long max) throws IllegalArgumentException{
		if(max > 0){
			this.MAX_FILE_SIZE = max;
		}
		else{
			throw new IllegalArgumentException("Size specification is unsuitable");
		}
	}

	/**
	 * メッセージファイルを再作成する必要があるかどうかを判定します。<br>
	 * ファイルサイズが、このオブジェクトの作成時に指定された
	 * 値以上の場合 true を返します。
	 *
	 * @param writer 出力ストリーム
	 * @return メッセージファイルの再作成が必要と判断された場合は true。
	 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
	 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
	 */
	public boolean expiration(ReportFileWriter writer){
		return writer.getFile().length() > this.MAX_FILE_SIZE;
	}
}

