/*
 * LogAgent.java
 *
 * Created on 2003/08/01, 18:00
 */

package org.intra_mart.framework.system.log;

/**
 * ログに出力するエージェントです。
 * im-J2EE Framework から出るログの出力先を変更したい場合、
 * このインタフェースを実装したクラスを設定します。
 *
 * @author INTRAMART
 * @since 4.2
 */
public interface LogAgent {

    /**
     * 初期化します。
     *
     * @param params ログエージェントのパラメータ
     */
    public void init(LogAgentParam[] params);

    /**
     * メッセージを出力します。
     *
     * @param category ログのカテゴリ
     * @param level メッセージのレベル
     * @param message メッセージ
     */
    public void sendMessage(String category, String level, String message);

    /**
     * メッセージを出力します。
     * 詳細な情報が添付されたメッセージを出力します。
     *
     * @param category ログのカテゴリ
     * @param level メッセージのレベル
     * @param message メッセージ
     * @param detail 詳細
     */
    public void sendMessage(
        String category,
        String level,
        String message,
        Object detail);
}
