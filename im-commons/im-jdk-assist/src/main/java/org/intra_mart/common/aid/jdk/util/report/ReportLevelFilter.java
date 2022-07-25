package org.intra_mart.common.aid.jdk.util.report;

import java.io.IOException;

/**
 * メッセージの出力制御を行うためのフィルタークラスです。<p>
 * このクラスを利用することにより、メッセージをメッセージレベルにより、
 * 出力制御を行うことができます。
 *
 * @see org.intra_mart.common.aid.jdk.util.report.ReportLevel
 */
public class ReportLevelFilter extends FilterMessageReporter implements ReportLevel{
	/**
	 * このリポーターがフィルタリングする基準レベル値
	 */
	protected int datumpoint = 0;

	/**
	 * 指定された基礎出力リポーターにメッセージを書き込む
	 * Reporter オブジェクトを作成します。
	 * @param out 基礎出力リポーター
	 */
	public ReportLevelFilter(MessageReporter out, int level){
		super(out);
		this.datumpoint = level;
	}

	/**
	 * メッセージを出力します。<br>
	 * このメソッドは、以下の呼出と同じです。
	 * <p>report(message, ReportLevel.MAX_VALUE)<p>
	 * つまり、メッセージ message は必ず基礎出力リポーターに出力されます。
	 * @param message メッセージ
	 */
	public void report(String message){
		report(message, ReportLevelFilter.MAX_VALUE);
	}

	/**
	 * 指定のメッセージレベルが基準値以上のメッセージを出力します。<br>
	 * メッセージレベルは以下の式で判定されます。
	 * <p>this.datumpoint <= level<p>
	 * 判定式が true の場合、基礎出力リポーターに対して
	 * メッセージが出力されます。逆に判定結果が false の場合、
	 * このメソッドは何も行いません。<br>
	 * @param message メッセージ
	 * @param level メッセージレベル
	 */
	public synchronized void report(String message, int level){
		if(this.datumpoint <= level){
			this.out.report(message);
		}
	}

	/**
	 * data をメッセージとして出力します。<br>
	 * このメソッドは、以下の呼出と同じです。
	 * <p>report(data, ReportLevel.MAX_VALUE)<p>
	 * つまり、メッセージデータ data は必ず基礎出力リポーターに出力されます。
	 * @param data データ
	 */
	public void report(Object data){
		this.report(data, ReportLevelFilter.MAX_VALUE);
	}

	/**
	 * 指定のメッセージレベルが基準値以上のメッセージを出力します。<br>
	 * メッセージレベルは以下の式で判定されます。
	 * <p>this.datumpoint <= level<p>
	 * 判定式が true の場合、基礎出力リポーターに対して
	 * メッセージが出力されます。逆に判定結果が false の場合、
	 * このメソッドは何も行いません。<br>
	 * @param data データ
	 * @param level メッセージレベル
	 */
	public synchronized void report(Object data, int level){
		if(this.datumpoint <= level){
			this.out.report(data);
		}
	}
}
