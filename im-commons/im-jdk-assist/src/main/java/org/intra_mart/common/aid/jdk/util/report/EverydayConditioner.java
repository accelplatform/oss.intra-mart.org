package org.intra_mart.common.aid.jdk.util.report;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * このクラスは、時間によりリフレッシュイベントを
 * 発生させるためのヘルパークラスです。<br>
 * このクラスは、FileReporter と組み合わせて利用します。
 * このクラスのオブジェクトを FileReporter オブジェクトにセットすることで、
 * 指定時間になった(指定時間を経過した)ときに、
 * メッセージファイルのローテイト機能イベントを発生させることができます。
 * 時間のチェックは時および分によるものなので、FileReporter の中で
 * リフレッシュイベントは毎日発生することになります。
 *
 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
 */
public class EverydayConditioner implements ReportFileConditioner{
	private static final long ONE_DAY = 1000 * 60 * 60 * 24;	// １日のミリ秒

	private long EXPIRE_TIME;								// チェック基準時刻
	
	private TimeZone timeZone;

	/**
	 * 毎日定刻にリフレッシュイベントを発生させるための
	 * ヘルパーオブジェクトを構築します。<br>
	 * このオブジェクトは、毎日指定された時刻に
	 * なった場合にリフレッシュイベント発生を通知します。<br>
	 * 
	 * 時刻の計算は、指定されたタイムゾーンに基づきます。
	 * 
	 * @param hours 時間(0 ～ 23)
	 * @param minutes 分(0 ～ 59)
	 * @param timeZone タイムゾーン
	 * @throws IllegalArgumentException 引数が有効範囲外の場合
	 */
	public EverydayConditioner(int hours, int minutes, TimeZone timeZone) throws IllegalArgumentException{

		if(timeZone == null){
			throw new IllegalArgumentException("TimeZone are indispensable arguments.");	
		}
			
		if(hours >= 0 && minutes >= 0 && minutes <= 59){
			
			this.timeZone = timeZone;

			// 現在時刻
			Calendar cal = Calendar.getInstance(this.timeZone);		
			long now = cal.getTimeInMillis();			

			// 今日の始まり
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long today = cal.getTimeInMillis();			
			
			// 日付変更点との時間差分
			long gap_min = (hours * 60) + minutes;		
			long gap_millis = gap_min * (60 * 1000);	// ミリ秒へ変換

			// 満期点
			this.EXPIRE_TIME = today + gap_millis;		
			if(this.EXPIRE_TIME < now){
				this.EXPIRE_TIME += ONE_DAY;			// 満期点の修正
			}
		}
		else{
			throw new IllegalArgumentException("Time specification is unsuitable");
		}
	}

	/**
	 * 毎日定刻にリフレッシュイベントを発生させるための
	 * ヘルパーオブジェクトを構築します。<br>
	 * このオブジェクトは、毎日指定された時刻に
	 * なった場合にリフレッシュイベント発生を通知します。<br>
	 * 
	 * 時刻の計算は、デフォルトタイムゾーンに基づきます。
	 * 
	 * @param hours 時間(0 ～ 23)
	 * @param minutes 分(0 ～ 59)
	 * @throws IllegalArgumentException 引数が有効範囲外の場合
	 */
	public EverydayConditioner(int hours, int minutes) throws IllegalArgumentException{
		this(hours, minutes, TimeZone.getDefault());
	}

	/**
	 * メッセージファイルを再作成する必要があるかどうかを判定します。<br>
	 * このオブジェクトの作成時に指定された時刻を経過して最初の呼び出し時
	 * の場合 true を返します。
	 *
	 * @param writer 出力ストリーム
	 * @return メッセージファイルの再作成が必要と判断された場合は true。
	 * @see org.intra_mart.common.aid.jdk.util.report.FileReporter
	 * @see org.intra_mart.common.aid.jdk.util.report.ReportFileWriter
	 */
	public boolean expiration(ReportFileWriter writer){
		
		// 基準時刻を経過しているかな？
		if(this.EXPIRE_TIME <= Calendar.getInstance(this.timeZone).getTimeInMillis()){
			this.EXPIRE_TIME += ONE_DAY;		// チェック用基準値の更新
			return true;
		}
		return false;
	}
}

