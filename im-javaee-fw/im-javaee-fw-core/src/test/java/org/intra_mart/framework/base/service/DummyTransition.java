/*
 * DummyTransition.java
 *
 * Created on 2002/01/29, 15:55
 */

package org.intra_mart.framework.base.service;

import org.intra_mart.framework.system.exception.SystemException;

/**
 *
 * @author  intra-mart
 * @version 
 */
public class DummyTransition extends Transition {

    /** Creates new DummyTransition */
    public DummyTransition() {
        super();
    }

    /**
     * 入力エラーのページを取得します。
     *
     * @param e 入力時の例外
     * @return 入力エラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getInputErrorPage(RequestException e)
        throws ServicePropertyException, TransitionException {
        return "DummyErrorPage: exception = "
            + e.getClass().getName()
            + ", "
            + e.getMessage();
    }

    /**
     * 次に遷移するページを取得します。
     *
     * @return 次に遷移するページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getNextPage()
        throws ServicePropertyException, TransitionException {
        return "NextPage: request "
            + (getRequest() == null ? "is empty" : " exists")
            + ", response "
            + (getResponse() == null ? "is empty" : " exists")
            + ", result "
            + (getResult() == null ? "is empty" : " exists");
    }

    /**
     * 処理エラーのページを取得します。
     *
     * @param e 処理時の例外
     * @return 処理エラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getServiceErrorPage(Exception e)
        throws ServicePropertyException, TransitionException {
        return "ServiceErrorPage: exception = "
            + e.getClass().getName()
            + ", "
            + e.getMessage();
    }

    /**
     * システムエラーのページを取得します。
     *
     * @param e 処理時の例外
     * @return システムエラーページ
     * @throws ServicePropertyException 遷移ページ取得時にサービスプロパティ例外が発生
     * @throws TransitionException 遷移ページ取得時に例外が発生
     */
    public String getSystemErrorPage(Exception e)
        throws ServicePropertyException, TransitionException {
        return "SystemErrorPage: exception = "
            + e.getClass().getName()
            + ", "
            + e.getMessage();
    }

    /**
     * 次の遷移先に渡す情報を設定します。
     *
     * @throws TransitionException 情報設定時に例外が発生
     */
    public void setInformation() throws TransitionException {
        System.out.println(
            "["
                + this.getClass().getName()
                + "] Information: request "
                + (getRequest() == null ? "is empty" : " exists")
                + ", response "
                + (getResponse() == null ? "is empty" : " exists")
                + ", result "
                + (getResult() == null ? "is empty" : " exists"));
    }

    /** 次のページに遷移します。
     * 遷移する方法は次のページで要求される形式に依存します。
     *
     * @throws SystemException システム例外が発生
     * @since 3.2
     */
    public void transfer() throws SystemException {
        System.out.println(
            "["
                + this.getClass().getName()
                + "] transfer: request "
                + (getRequest() == null ? "is empty" : " exists")
                + ", response "
                + (getResponse() == null ? "is empty" : " exists")
                + ", result "
                + (getResult() == null ? "is empty" : " exists"));
    }
}
