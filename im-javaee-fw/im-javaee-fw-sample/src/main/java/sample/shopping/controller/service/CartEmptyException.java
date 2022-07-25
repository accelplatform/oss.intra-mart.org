/*
 * CartEmptyException.java
 *
 * Created on 2002/03/19, 18:22
 */

package sample.shopping.controller.service;

import org.intra_mart.framework.base.service.RequestException;

/**
 * カート内に商品が入っていない時の例外です。
 *
 * @author  NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class CartEmptyException extends RequestException {

    /**
     * 詳細メッセージを指定しないで <code>CartEmptyException</code> を構築します。
     */
    public CartEmptyException() {
        super();
    }

    /**
     * 指定された詳細メッセージを持つ <code>CartEmptyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     */
    public CartEmptyException(String msg) {
        super(msg);
    }

    /**
     * 指定された詳細メッセージとこの例外の発生原因となる例外を持つ <code>CartEmptyException</code> を構築します。
     *
     * @param msg 詳細メッセージ
     * @param throwable この例外の発生原因となる例外
     */
    public CartEmptyException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
