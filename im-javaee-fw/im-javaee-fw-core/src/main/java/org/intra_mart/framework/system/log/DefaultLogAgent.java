/*
 * DefaultLogAgent.java
 *
 * Created on 2003/08/01, 18:00
 */

package org.intra_mart.framework.system.log;

import java.io.PrintStream;

/**
 * デフォルトのログエージェントです。
 * 標準出力にログを出力します。
 * 出力されるメッセージは以下のような形式になります。<BR>
 * <CODE>[<I>カテゴリ</I>][<I>レベル</I>]<I>メッセージ</I><CODE>
 *
 * @author INTRAMART
 * @since 4.2
 */
public class DefaultLogAgent implements LogAgent {

    /**
     * 初期化します。
     * このクラスでは実際には何も行われません。
     *
     * @param params ログエージェントのパラメータ
     */
    public void init(LogAgentParam[] params) {
    }

    /**
     * メッセージを出力します。
     * このクラスでは通常、<code>message</code>を標準出力にログとして出力します。
     * ただし、<code>level</code>が{@link LogConstant#LEVEL_ERROR}である場合は
     * 標準例外出力にログを出力します。
     *
     * @param category ログのカテゴリ
     * @param level メッセージのレベル
     * @param message メッセージ
     */
    public void sendMessage(String category, String level, String message) {
        String logMessage = createMessage(category, level, message);
        if (!level.equals(LogConstant.LEVEL_ERROR)) {
            System.out.println(logMessage);
        } else {
            System.err.println(logMessage);
        }
    }

    /**
     * メッセージを出力します。
     * このクラスでは通常、<code>message</code>はそのまま、
     * <code>detail</code>は{@link Object#toString()}の値を標準出力にログとして出力します。
     * ただし、<code>level</code>が{@link LogConstant#LEVEL_ERROR}である場合は
     * 標準例外出力にログを出力します。<BR><BR>
     * <code>detail</code>が{@link Throwable}のサブクラスである場合、
     * {@link Throwable#printStackTrace(java.io.PrintWriter)}を使用して
     * スタックトレースが出力されます。出力先は<code>detail</code>による出力先に従います。
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
        Object detail) {

        PrintStream out = null;
        String logMessage = createMessage(category, level, message);
        if (!level.equals(LogConstant.LEVEL_ERROR)) {
            out = System.out;
        } else {
            out = System.err;
        }
        out.println(logMessage);
        if (detail != null) {
            if (detail instanceof Throwable) {
                ((Throwable)detail).printStackTrace(out);
            } else {
                out.print(detail.toString());
            }
        }
    }

    /**
     * メッセージを生成します。
     *
     * @param message メッセージ
     * @return 出力するメッセージ
     */
    private String createMessage(
        String category,
        String level,
        String message) {

        return "[" + category + "][" + level + "]" + message;
    }
}
