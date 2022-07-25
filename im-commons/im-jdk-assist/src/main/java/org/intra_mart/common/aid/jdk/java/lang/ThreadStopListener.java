package org.intra_mart.common.aid.jdk.java.lang;

public interface ThreadStopListener {
	/**
	 * 監視していたスレッドが終了したときに実行されるメソッドです。
	 * このメソッドをサブクラスが実行することで、特定のスレッド終了時に
	 * プログラムを実行することができます。
	 */
	public void handleThreadStop();
}
