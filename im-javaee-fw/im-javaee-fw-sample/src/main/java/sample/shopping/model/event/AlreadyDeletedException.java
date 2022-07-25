/*
 * AlreadyDeletedException.java
 *
 * Created on 2002/03/14, 13:52
 */

package sample.shopping.model.event;

import org.intra_mart.framework.system.exception.ApplicationException;

/**
 * データが既に削除されていた時の例外です。
 *
 * @author  NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class AlreadyDeletedException extends ApplicationException {

    /**
     * 商品コード
     */
    private String code;

    /**
     * 戻り先のページのキー
     */
    private String key;

    /**
     * 詳細メッセージを指定しないで <code>AlreadyDeletedException</code> を構築します。
     */
    public AlreadyDeletedException() {
        super();
        setCode(null);
    }

    /**
     * 指定された詳細メッセージを持つ <code>AlreadyDeletedException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public AlreadyDeletedException(String msg) {
        super(msg);
        setCode(null);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>AlreadyDeletedException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public AlreadyDeletedException(String msg, Throwable throwable) {
        super(msg, throwable);
        setCode(null);
    }

    /**
     * 既に削除されていた商品コードを格納します。
     *
     * @param code 商品コード
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 既に削除されていた商品コードを返却します。
     *
     * @return 商品コード
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 戻り先のページのキーを設定します。
     *
     * @param key 戻り先のページのキー
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 戻り先のページのキーを取得します。
     *
     * @return 戻り先のページのキー
     */
    public String getKey() {
        return this.key;
    }
}
