/*
 * 作成日: 2003/08/07
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package org.intra_mart.framework.base.message;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;
import org.intra_mart.framework.util.FileResourceBundle;

/**
 * 指定されたファイルからプロパティ情報を取得するメッセージプロパティハンドラです。
 * ファイルの書式は{@link DefaultMessagePropertyHandler}で示されるものと同じです。<BR>
 * ファイルが存在するディレクトリ名は{@link #PARAM_FILE_DIR}で示されるパラメータ名で指定します。<BR>
 * {@link #PARAM_DYNAMIC}で示されるパラメータにtrueを指定した場合
 * アプリケーション実行時にもプロパティの変更を動的に反映させることが可能となりますが、
 * パラメータの取得時に毎回ファイル操作を行うためパフォーマンスに悪影響を与える可能性があります。
 * このオプションは、開発時やデバッグ時のようにパラメータを頻繁に変更する必要がある場合のみtrueとし、
 * 通常はfalseに設定しておいてください。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class TextFileMessagePropertyHandler implements MessagePropertyHandler {

    /**
     * プロパティファイルのファイルパスのパラメータ名
     */
    public static final String PARAM_FILE_DIR = "file_dir";

    /**
     * プロパティファイルの再読み込み可能のパラメータ名
     */
    public static final String PARAM_DYNAMIC = "dynamic";

    /**
     * データリソース情報が設定されているリソースバンドルのプレフィックス
     */
    private String bundlePrefix;

    /**
     * メッセージ情報が設定されているリソースバンドル
     */
    private Map bundles;

    /**
     * サービスプロパティのファイルがあるディレクトリ
     */
    private String propertyFileDir;

    /**
     * 再設定可能フラグ
     */
    private boolean dynamic;

    public TextFileMessagePropertyHandler() {
        this.bundles = new HashMap();
        setPropertyFileDir(null);
        setDynamic(false);

    }

    /**
     * アプリケーションIDとロケールで指定されたリソースバンドルを取得します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDとロケールに該当するリソースバンドル
     * @throws MessagePropertyException リソースバンドルの取得時に例外が発生
     */
    private ResourceBundle getResourceBundle(String application, Locale locale)
        throws MessagePropertyException {

        Locale realLocale = locale;

        if (realLocale == null) {
            realLocale = new Locale("", "");
        }

        if (isDynamic()) {
            return createResourceBundle(application, realLocale);
        } else {
            ResourceBundle result = null;
            Map bundle = null;
            synchronized (this.bundles) {
                bundle = (Map)this.bundles.get(realLocale);
                if (bundle == null) {
                    bundle = new HashMap();
                    this.bundles.put(realLocale, bundle);
                }
            }
            synchronized (bundle) {
                result = (ResourceBundle)bundle.get(application);
                if (result == null) {
                    result = createResourceBundle(application, realLocale);
                    bundle.put(application, result);
                }
            }

            return result;
        }
    }

    /**
     * アプリケーションIDで指定されたリソースバンドルを生成します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return アプリケーションIDに該当するリソースバンドル
     * @throws MessagePropertyException リソースバンドルの取得時に例外が発生
     */
    private ResourceBundle createResourceBundle(
        String application,
        Locale locale)
        throws MessagePropertyException {

        try {
            return new FileResourceBundle(
                getPropertyFileDir()
                    + File.separator
                    + getBundlePrefix()
                    + "_"
                    + application,
                locale);
        } catch (Exception e) {
            throw new MessagePropertyException(e.getMessage(), e);
        }
    }

    /**
     * プロパティファイルがあるディレクトリを設定します。
     *
     * @param propertyFileDir  プロパティファイルがあるディレクトリ
     */
    private void setPropertyFileDir(String propertyFileDir) {
        this.propertyFileDir = propertyFileDir;
    }

    /**
     * プロパティファイルがあるディレクトリを取得します。
     *
     * @return プロパティファイルがあるディレクトリ
     */
    private String getPropertyFileDir() {
        return this.propertyFileDir;
    }

    /**
     * 再設定可能／不可能を設定します。
     *
     * @param dynamic true 再設定可能、false 再設定不可
     */
    private void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     *
     * @return true 再設定可能、false 再設定不可
     */
    public boolean isDynamic() throws MessagePropertyException {
        return this.dynamic;
    }

    /**
     * メッセージを取得します。
     * <code>application</code>で指定されるアプリケーション固有のプロパティから
     * <code>key</code>に該当するメッセージを取得します。
     * システムが稼動しているロケールをヒントにメッセージを取得します。
     *
     * @param application アプリケーションID
     * @param key メッセージのキー
     * @return メッセージ
     * @throws MessagePropertyException メッセージ取得時に例外が発生
     */
    public String getMessage(String application, String key)
        throws MessagePropertyException {

        return getMessage(application, key, Locale.getDefault());
    }

    /**
     * メッセージを取得します。
     * <code>application</code>で指定されるアプリケーション固有のプロパティから
     * <code>key</code>に該当するメッセージを取得します。
     * <code>locale</code>で指定されたロケールをヒントにメッセージを取得します。
     *
     * @param application アプリケーションID
     * @param key メッセージのキー
     * @param locale ロケール
     * @return メッセージ
     * @throws MessagePropertyException メッセージ取得時に例外が発生
     */
    public String getMessage(String application, String key, Locale locale)
        throws MessagePropertyException {

        ResourceBundle bundle = getResourceBundle(application, locale);
        return ResourceBundleMessagePropertyHandlerUtil.getMessage(
            bundle,
            application,
            key,
            locale);
    }

    /**
     * プロパティハンドラを初期化します。
     *
     * @param params 初期パラメータ
     * @throws PropertyHandlerException プロパティハンドラの初期化時に例外が発生
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
        String bundleName = null;
        String fileDir = null;
        String dynamic = null;

        // パラメータのパース
        for (int i = 0; i < params.length; i++) {
            if (params[i]
                .getName()
                .equals(
                    DefaultMessagePropertyHandler.DEFAULT_BUNDLE_NAME_PARAM)) {
                // リソースバンドルのファイル名の場合
                bundleName = params[i].getValue();
            } else if (params[i].getName().equals(PARAM_FILE_DIR)) {
                // ファイルディレクトリの場合
                fileDir = params[i].getValue();
            } else if (params[i].getName().equals(PARAM_DYNAMIC)) {
                // 再設定可能フラグの場合
                dynamic = params[i].getValue();
            }
        }

        // リソースバンドルプレフィックスの設定
        if (bundleName == null) {
            bundleName = DefaultMessagePropertyHandler.DEFAULT_BUNDLE_NAME;
        }
        setBundlePrefix(bundleName);

        // 再設定可能フラグの設定
        Boolean dummyDynamic = new Boolean(dynamic);
        setDynamic(dummyDynamic.booleanValue());

        // プロパティファイルディレクトリの必須チェック
        if (fileDir == null) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.message.i18n")
                        .getString("TextFileMessagePropertyHandler.param.FileDirNotFound");
            } catch (MissingResourceException e) {
            }
            throw new PropertyHandlerException(
                message + " : " + PARAM_FILE_DIR);
        }
        this.propertyFileDir = fileDir;
    }

    /**
     * リソースバンドルのプレフィックスを設定します。
     *
     * @param bundlePrefix リソースバンドルのプレフィックス
     */
    private void setBundlePrefix(String bundlePrefix) {
        this.bundlePrefix = bundlePrefix;
    }

    /**
     * リソースバンドルのプレフィックスを取得します。
     *
     * @return リソースバンドルのプレフィックス
     */
    private String getBundlePrefix() {
        return this.bundlePrefix;
    }
}
