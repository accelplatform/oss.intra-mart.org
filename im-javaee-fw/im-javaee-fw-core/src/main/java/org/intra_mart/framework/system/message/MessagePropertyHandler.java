/*
 * MessagePropertyHandler.java
 *
 * Created on 2002/02/21, 16:55
 */

package org.intra_mart.framework.system.message;

import org.intra_mart.framework.system.property.PropertyHandler;

/**
 * メッセージに関連するプロパティを取得します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface MessagePropertyHandler extends PropertyHandler {

    /**
     * キーに該当するメッセージを取得します。
     * 該当するキーが存在しない場合、空文字列を返します。
     *
     * @param key メッセージのキー
     * @param loginGroup ログイングループ
     * @return メッセージ
     * @deprecated intra-mart v5.0からのメッセージマスタにはログイングループの概念がありません。<BR>
     * {@link #getMessage(String)}を使用してください。
     */
    public String getMessage(String key, String loginGroup);

    /**
     * キーに該当するメッセージを取得します。
     * 該当するキーが存在しない場合、空文字列を返します。
     *
     * @param key メッセージのキー
     * @param loginGroup ログイングループ
     * @return メッセージ
     * @since 5.0
     */
    public String getMessage(String key);
}
