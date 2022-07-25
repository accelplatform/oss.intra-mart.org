/*
 * AlreadyExistException.java
 *
 * Created on 2002/03/14, 14:06
 */

package sample.shopping.model.event;

import org.intra_mart.framework.system.exception.ApplicationException;

/**
 * データが既に存在していた時の例外です。
 *
 * @author  NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class AlreadyExistException extends ApplicationException {

    /**
     * 商品コード
     */
    private String code;

    /**
     * 詳細メッセージを指定しないで <code>AlreadyExistException</code> を構築します。
     */
    public AlreadyExistException() {
        super();
        setCode(null);
    }

    /**
     * 指定された詳細メッセージを持つ <code>AlreadyExistException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public AlreadyExistException(String msg) {
        super(msg);
        setCode(null);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>AlreadyDeletedException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public AlreadyExistException(String msg, Throwable throwable) {
        super(msg, throwable);
        setCode(null);
    }

    /**
     * データが既に存在していた時の商品コードを格納します。
     *
     * @param code 商品コード
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * データが既に存在していた時の商品コードを返却します。
     *
     * @return 商品コード
     */
    public String getCode() {
        return this.code;
    }
}
