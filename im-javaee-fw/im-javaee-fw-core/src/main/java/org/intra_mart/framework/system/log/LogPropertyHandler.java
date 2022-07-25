/*
 * LogPropertyHandler.java
 *
 * Created on 2003/08/01, 18:00
 */

package org.intra_mart.framework.system.log;

import org.intra_mart.framework.system.property.PropertyHandler;

/**
 * ログフレームワークで使用するプロパティハンドラです。
 *
 * @author INTRAMART
 * @since 4.2
 */
public interface LogPropertyHandler extends PropertyHandler {

    /**
     * ログエージェントのクラス名を取得します。
     * 何も設定されていない場合nullを返すように実装する必要があります。
     *
     * @return ログエージェントのクラス名（設定されていない場合null）
     * @throws LogPropertyException ログエージェントのクラス名の取得時に例外が発生
     */
    public String getLogAgentName() throws LogPropertyException;

    /**
     * ログエージェントのパラメータを取得します。
     * 何も設定されていない場合大きさが0の配列を返すように実装する必要があります。
     *
     * @return ログエージェントのパラメータの配列
     * @throws LogPropertyException ログエージェントのパラメータの取得時に例外が発生
     */
    public LogAgentParam[] getLogAgentParams() throws LogPropertyException;
}
