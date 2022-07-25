package org.intra_mart.common.aid.jdk.util;

/**
 * このクラスは、イベントリスナーのインターフェースです。<p>
 * イベントコントローラと組み合わせて使うことにより、
 * 様々なイベント機能を実装することができます。
 *
 * @see org.intra_mart.common.aid.jdk.util.EventQueueController
 */
public interface EventListener{
	/**
	 * イベントが発生した時にコントローラから呼び出されます。
	 */
	public void handleEvent();
}
