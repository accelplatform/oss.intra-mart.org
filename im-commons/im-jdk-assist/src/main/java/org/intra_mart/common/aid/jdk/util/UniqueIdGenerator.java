package org.intra_mart.common.aid.jdk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * ユニークＩＤを作成するユーティリティ・クラスです。
 * 
 */
public class UniqueIdGenerator {

	private static final int max = Integer.parseInt("zz", 36);
	private static volatile int seq = (new Random()).nextInt(max);
	private static SimpleDateFormat datePattern = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	/**
	 * ユニークなＩＤを作成します。<br/>
	 * 現在のプロセスに対して一意性を保証するＩＤを生成します。<br/>
	 * <br/>
	 * ＩＤは時間情報およびこのクラスが持つシーケンス番号から構成されています。
	 * これによりメソッド呼び出しの度に異なる文字列を生成しＩＤとして返します。<br/>
	 * 生成される文字列の長さは 13 です。<br/>
	 * 本メソッドは、IDの一意性を保障する為に synchronized メソッドとなっています。<br/>
	 * 
	 * @return ユニークＩＤ
	 */
	public synchronized static String getUniqueId(){
		Date now = new Date();
		String dateFormat = Long.toString(Long.parseLong(datePattern.format(now)), 36);
		String sequence = sequencer();
		
		return dateFormat.concat(sequence);
	}

	/**
	 * シーケンスを取得して返却。
	 * シーケンスは、このメソッドがコールされるたびにインクリメントされる値。
	 * 
	 * @return　返却値は、３６進数２桁の文字列。
	 */
	private static String sequencer(){
		
		if(seq == max){
			seq = 0;
		}
		else{
			seq++;
		}
		
		// 桁チェック後３６進数変換して返却
		if(seq < 36){
			return ("0").concat(Integer.toString(seq, 36));
		}
		else{
			return Integer.toString(seq, 36);
		}
	}
}
