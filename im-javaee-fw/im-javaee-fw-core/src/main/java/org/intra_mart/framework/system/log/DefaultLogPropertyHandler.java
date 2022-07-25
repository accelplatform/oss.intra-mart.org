/*
 * DefaultLogPropertyHandler.java
 *
 * Created on 2003/07/10, 19:20
 */

package org.intra_mart.framework.system.log;

import java.util.Enumeration;
import java.util.Vector;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyParam;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;

/**
 * ログに関連するデフォルトのプロパティハンドラです。
 * プロパティの設定内容は以下のとおりです。
 * <TABLE border="1">
 *  <TR>
 *      <TH>キー</TH>
 *      <TH>内容</TH>
 *  </TR>
 *  <TR>
 *      <TD>agent.class</TD>
 *      <TD>{@link LogAgent}のクラス名</TD>
 *  </TR>
 *  <TR>
 *      <TD>agent.param.<I>パラメータ名</I></TD>
 *      <TD>ログエージェントに渡すパラメータ</TD>
 *  </TR>
 * </TABLE>
 *
 * @author INTRAMART
 * @since 4.2
 */
public class DefaultLogPropertyHandler implements LogPropertyHandler {

    /**
     * デフォルトのリソースバンドル名
     */
    public static final String DEFAULT_BUNDLE_NAME = "LogConfig";

    /**
     * リソースバンドル名のパラメータ名
     */
    public static final String DEFAULT_BUNDLE_NAME_PARAM = "bundle";

    /**
     * ログリソース情報が設定されているリソースバンドル
     */
    private ResourceBundle bundle;

    /**
     * DefaultLogPropertyHandlerを新規に生成します。
     */
    public DefaultLogPropertyHandler() {
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
                    bundleName = params[i].getValue();
                }
            }
        }
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }

        this.bundle = ResourceBundle.getBundle(bundleName);
    }

    /**
     * ログエージェントのクラス名を取得します。
     * 何も設定されていない場合nullが返ります。
     *
     * @return ログエージェントのクラス名（設定されていない場合null）
     * @throws LogPropertyException ログエージェントのクラス名の取得時に例外が発生
     */
    public String getLogAgentName() throws LogPropertyException {
        String name;

        try {
            name = this.bundle.getString("agent.class");
        } catch (MissingResourceException e) {
            name = null;
        } catch (Throwable e) {
            String message = null;
            try {
                message =ResourceBundle.getBundle("org.intra_mart.framework.system.log.i18n").getString("DefaultLogPropertyHandler.FailedToGetAgent");
            } catch (MissingResourceException ex) {
            }
            throw new LogPropertyException(message, e);
        }

        return name;
    }

    /**
     * ログエージェントのパラメータを取得します。
     * 何も設定されていない場合大きさが0の配列が返ります。
     *
     * @return ログエージェントのパラメータの配列
     * @throws LogPropertyException ログエージェントのパラメータの取得時に例外が発生
     */
    public LogAgentParam[] getLogAgentParams() throws LogPropertyException {
        Enumeration keys = null;
        String key = null;
        String value = null;
        LogAgentParam param = null;
        LogAgentParam[] params = null;
        Vector temp = new Vector();

        keys = this.bundle.getKeys();
        while (keys.hasMoreElements()) {
            key = (String)keys.nextElement();
            if (key.startsWith("agent.param")) {
                value = this.bundle.getString(key);
                param = new LogAgentParam();
                param.setName(key);
                param.setValue(value);
                temp.add(param);
            }
        }
        params = new LogAgentParam[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            params[i] = (LogAgentParam)temp.elementAt(i);
        }

        return params;
    }
}
