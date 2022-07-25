/*
 * EventListener.java
 *
 * Created on 2001/11/29, 18:36
 */

package org.intra_mart.framework.base.event;

import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;

/**
 * イベントリスナです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface EventListener {

    /**
     * 処理を実行します。
     * イベントを元に処理を実行します。
     * このインタフェースを実装するすべてのクラスは、
     * すべてのイベントトリガを実行した後に処理を
     * 実行するように設計されている必要があります。
     *
     * @param event イベント
     * @return イベント処理結果
     * @throws ApplicationException イベント処理時にアプリケーション例外が発生
     * @throws SystemException システム処理時にアプリケーション例外が発生
     */
    public EventResult execute(Event event)
        throws SystemException, ApplicationException;

    /**
     * このイベントリスナがトランザクションの中で実行されているかどうかの情報を設定します。
     *
     * @param transaction トランザクションの中で実行されている場合：true、そうでない場合：false
     * @since 4.2
     */
    public void setInTransaction(boolean transaction);
}
