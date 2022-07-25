package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;

/**
 * メッセージを行出力するためのフィルタークラスです。<p>
 * このクラスを利用すると、report メソッドに渡されたメッセージ文字列に
 * 改行コードを付加して、行出力することができます。<br>
 * 以下のようなプログラムにより、ログファイルをログファイルを作成することが
 * できます。
 * <blockquote>
 * FIle logFile = new File("test.log");<br>
 * ReportFileWriter writer = new ReportFileWriter(logFile);<br>
 * FileReporter writer = new FileReporter(writer);<br>
 * MessageReporter reporter = new LineReportFilter(writer);<br>
 * </blockquote>
 * これで、変数 reporter の report メソッドを呼び出す毎に、
 * ファイル test.log に report メソッド指定したメッセージが
 * １行として出力されます。
 *
 * @see org.intra_mart.common.aid.jdk.util.report.ReportLevel
 */
public class LineReportFilter extends FilterMessageReporter{
	private static final String lineFeed = System.getProperty("line.separator");

	/**
	 * 指定された基礎出力ストリームにメッセージを一行書き込む
	 * フィルターストリームを作成します。
	 * @param out 基礎出力ストリーム
	 */
	public LineReportFilter(MessageReporter out){
		super(out);
	}

	/**
	 * メッセージを出力します。<br>
	 * このメソッドは、基礎出力ストリームに対して、message に
	 * 改行コードを付加してメッセージを１行出力します。
	 * @param message メッセージ
	 */
	public void report(String message){
		this.out.report(message.concat(lineFeed));
	}

	/**
	 * 基礎出力ストリームに対してメッセージを出力します。<br>
	 * このメソッドは、単純に data を基礎出力ストリームに渡します。
	 * サブクラスで、このメソッドをオーバーライドすることにより、
	 * データの加工などのフィルタリング処理を追加することができます。
	 * @param data データ
	 */
	public synchronized void report(Object data){
		this.report(String.valueOf(data));
	}
}
