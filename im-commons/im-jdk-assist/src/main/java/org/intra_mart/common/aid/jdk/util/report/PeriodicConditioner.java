package org.intra_mart.common.aid.jdk.util.report;


/**
 * このクラスは、タイマーによりリフレッシュイベントを
 * 発生させるためのヘルパークラスです。<br>
 * このクラスは、FileReporter と組み合わせて利用します。
 * このクラスのオブジェクトを FileReporter オブジェクトにセットすることで、
 * 一定期間の経過に合わせてに、
 * メッセージファイルのローテイト機能イベントを発生させることができます。
 *
 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
 */
public class PeriodicConditioner implements ReportFileConditioner{
	private long START_TIME = System.currentTimeMillis();	// 出力開始時間
	private long EXPIRE_TIME;								// 満期
	private long LIFE_COUNTER;								// 有効時間

	/**
	 * タイマーによりリフレッシュイベントを発生させるための
	 * ヘルパーオブジェクトを構築します。<br>
	 * このオブジェクトは、指定の時間が経過した時に
	 * リフレッシュイベント発生を通知します。
	 * @throws IllegalArgumentException millis がゼロ以下の場合
	 */
	public PeriodicConditioner(long millis) throws IllegalArgumentException{
		if(millis > 0){
			this.LIFE_COUNTER = millis;						// 設定の保存
			this.EXPIRE_TIME = this.START_TIME + millis;	// 満期の設定
		}
		else{
			throw new IllegalArgumentException("Time specification is unsuitable");
		}
	}

	/**
	 * メッセージファイルを再作成する必要があるかどうかを判定します。<br>
	 * 引数 outputSize の値が、このオブジェクトの作成時に指定された
	 * 値以上の場合 true を返します。
	 *
	 * @param writer 出力ストリーム
	 * @return メッセージファイルの再作成が必要と判断された場合は true。
	 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
	 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
	 */
	public boolean expiration(ReportFileWriter writer){
		if(this.EXPIRE_TIME <= System.currentTimeMillis()){
			// 時間だ！
			this.START_TIME = System.currentTimeMillis();
			this.EXPIRE_TIME = this.START_TIME + this.LIFE_COUNTER;
			return true;
		}

		return false;
	}
}

