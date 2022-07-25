/*
 * DefaultMessagePropertyHandler.java
 *
 * Created on 2002/02/21, 20:24
 */

package org.intra_mart.framework.system.message;

import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyParam;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * デフォルトのメッセージプロパティハンドラです。
 * プロパティの設定内容は以下のとおりです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class DefaultMessagePropertyHandler implements MessagePropertyHandler {

    /**
     * デフォルトのリソースバンドル名
     */
    public static final String DEFAULT_BUNDLE_NAME = "MessageConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * メッセージプロパティ情報が設定されているリソースバンドル
     */
    private ResourceBundle bundle;

    /**
     * DefaultMessagePropertyHandlerを新規に生成します。
     */
    public DefaultMessagePropertyHandler() {
        this.bundle = null;
    }

    /**
     * プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getName().equals(DEFAULT_BUNDLE_NAME_PARAM)) {
                    bundleName = params[i].getName();
                }
            }
        }
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }

        try {
            this.bundle = ResourceBundle.getBundle(bundleName);
        } catch (MissingResourceException e) {
            throw new PropertyHandlerException(e.getMessage(), e);
        }
    }

    /**
     * キーに該当するメッセージを取得します。
     * 該当するキーが存在しない場合、空文字列を返します。
	 * プロパティの設定内容は以下のとおりです。
	 * <TABLE border="1">
	 *   <TR>
	 *       <TH>キー</TH>
	 *       <TH>内容</TH>
	 *   </TR>
	 *   <TR>
	 *       <TD><I>ログイングループID</I>.<I>キー</I></TD>
	 *       <TD><I>ログイングループID</I>と<I>キー</I>に対応するメッセージ</TD>
	 *   </TR>
	 * </TABLE>
	 * 
     * @param key メッセージのキー
     * @param loginGroup ログイングループ
     * @return メッセージ
     * @deprecated intra-mart v5.0からのメッセージマスタにはログイングループの概念がありません。<BR>
     * {@link #getMessage(String)}を使用してください。
     */
    public String getMessage(String key, String loginGroup) {
        String message = "";
        String messageKey;

        if (loginGroup != null && !loginGroup.equals("")) {
            messageKey = loginGroup + "." + key;
            try {
                message = this.bundle.getString(messageKey);
            } catch (MissingResourceException e) {
                message = "";
            }
        } else {
            message = "";
        }

        return message;
    }

    /**
     * キーに該当するメッセージを取得します。
     * 該当するキーが存在しない場合、空文字列を返します。
     * プロパティの設定内容は以下のとおりです。
	 * <TABLE border="1">
	 *   <TR>
	 *       <TH>キー</TH>
	 *       <TH>内容</TH>
	 *   </TR>
	 *   <TR>
	 *       <TD><I>キー</I></TD>
	 *       <TD><I>キー</I>に対応するメッセージ</TD>
	 *   </TR>
	 * </TABLE>
     *
     * @param key メッセージのキー
     * @return メッセージ
     * @since 5.0
     */
    public String getMessage(String key) {
        String message = "";
        if (key != null) {
            try {
                message = this.bundle.getString(key);
            } catch (Exception e) {
                message = "";
            }
        }
        return message;
    }
}
