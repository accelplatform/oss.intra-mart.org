/*
 * ItemCodeDuplicationException.java
 *
 * Created on 2002/02/28, 13:45
 */

package sample.shopping.model.data;

import org.intra_mart.framework.base.data.DataAccessException;

/**
 * データが重複していた時の例外です。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class ItemCodeDuplicationException extends DataAccessException {

    /**
     * 商品コード
     */
    private String code;

    /**
     * 詳細メッセージを指定しないで <code>ItemCodeDuplicationException</code> を構築します。
     */
    public ItemCodeDuplicationException() {
        super();
        setCode(null);
    }

    /**
     * 指定された詳細メッセージを持つ <code>ItemCodeDuplicationException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public ItemCodeDuplicationException(String msg) {
        super(msg);
        setCode(null);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>ItemCodeDuplicationException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public ItemCodeDuplicationException(String msg, Throwable throwable) {
        super(msg, throwable);
        setCode(null);
    }

    /**
     * 重複していた商品コードを格納します。
     *
     * @param code 商品コード
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 重複していた商品コードを返却します。
     *
     * @return 商品コード
     */
    public String getCode() {
        return this.code;
    }
}
