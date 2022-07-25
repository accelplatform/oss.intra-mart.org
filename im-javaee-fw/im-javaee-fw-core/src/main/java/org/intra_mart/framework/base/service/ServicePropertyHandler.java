/*
 * ServicePropertyHandler.java
 *
 * Created on 2001/12/17, 14:23
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;

import org.intra_mart.framework.system.property.PropertyHandler;

/**
 * サービスの設定情報に接続するクラスです。
 *
 * @author INTRAMART
 * @version 1.0
 */
public interface ServicePropertyHandler extends PropertyHandler {

    /**
     * アプリケーションIDのデフォルトのパラメータ名
     * @deprecated この実装は廃止されました。
     */
    public static final String DEFAULT_APPLICATION_PARAMETER = "application";

    /**
     * サービスIDのデフォルトのパラメータ名
     * @deprecated この実装は廃止されました。
     */
    public static final String DEFAULT_SERVICE_PARAMETER = "service";

    /**
     * 例外属性のデフォルトの属性名
     */
    public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";

    /**
     * ログインユーザのエンコーディングのデフォルトの属性名
     */
    public static final String DEFAULT_ENCODING_ATTRIBUTE =
        "org.intra_mart.framework.base.service.encoding";

    /**
     * ログインユーザのロケールのデフォルトの属性名
     */
    public static final String DEFAULT_LOCALE_ATTRIBUTE =
        "org.intra_mart.framework.base.service.locale";

    /**
     * プロパティの動的読み込みが可能かどうか調べます。
     *
     * @return true：プロパティの動的読み込みが可能、false：プロパティの動的読み込み不可
     * @throws ServicePropertyException チェック時に例外が発生
     * @since 3.2
     */
    public boolean isDynamic() throws ServicePropertyException;

    /**
     * クライアントのエンコードを取得します。
     *
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
     */
    public String getClientEncoding() throws ServicePropertyException;

    /**
     * ログインユーザが使用するエンコードを保存しておくときの属性名を取得します。
     * 設定されていない場合、{@link #DEFAULT_ENCODING_ATTRIBUTE}で定義されている値を返します。
     *
     * @return エンコードの属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getEncodingAttributeName() throws ServicePropertyException;

    /**
     * クライアントのロケールを取得します。
     *
     * @return クライアントのロケール（設定されていない場合はnull）
     * @throws ServicePropertyException クライアントのロケールの取得時に例外が発生
     * @since 4.2
     */
    public Locale getClientLocale() throws ServicePropertyException;

    /**
     * ログインユーザが使用するロケールを保存しておくときの属性名を取得します。
     * 設定されていない場合、{@link #DEFAULT_LOCALE_ATTRIBUTE}で定義されている値を返します。
     *
     * @return ロケールの属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     * @since 4.2
     */
    public String getLocaleAttributeName() throws ServicePropertyException;

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まる場合にこのメソッドを使用します。
     * このメソッドは{@link #getNextPagePath(String, String, Locale) getNextPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getNextPagePath(String, String, Locale)}を使用してください。
     */
    public String getNextPagePath(String application, String service)
        throws ServicePropertyException;

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まる場合にこのメソッドを使用します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getNextPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException;

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。<CODE>key<CODE>が<CODE>null<CODE>の場合、{@link #getNextPagePath(String, String)}と同じ動作になります。
     * このメソッドは{@link #getNextPagePath(String, String, String, Locale) getNextPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getNextPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getNextPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException;

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。<CODE>key<CODE>が<CODE>null<CODE>の場合、{@link #getNextPagePath(String, String)}と同じ動作になります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getNextPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String, String)}で取得されるページを返します。
     * このメソッドは{@link #getInputErrorPagePath(String, String, String, Locale) getInputErrorPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String)}で取得されるページを返します。
     * このメソッドは{@link #getInputErrorPagePath(String, String, Locale) getInputErrorPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(String, String, Locale)}を使用してください。
     */
    public String getInputErrorPagePath(String application, String service)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath()}で取得されるページを返します。
     * このメソッドは{@link #getInputErrorPagePath(String, Locale) getInputErrorPagePath(application, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(String, Locale)}を使用してください。
     */
    public String getInputErrorPagePath(String application)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath()}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(String application, Locale locale)
        throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     * このメソッドは{@link #getInputErrorPagePath(Locale) getInputErrorPagePath((java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getInputErrorPagePath(Locale)}を使用してください。
     */
    public String getInputErrorPagePath() throws ServicePropertyException;

    /**
     * 入力例外時のページのパスを取得します。
     *
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getInputErrorPagePath(Locale locale)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String, String)}で取得されるページを返します。
     * このメソッドは{@link #getServiceErrorPagePath(String, String, String, Locale) getServiceErrorPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String)}で取得されるページを返します。
     * このメソッドは{@link #getServiceErrorPagePath(String, String, Locale) getServiceErrorPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(String, String, Locale)}を使用してください。
     */
    public String getServiceErrorPagePath(String application, String service)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath()}で取得されるページを返します。
     * このメソッドは{@link #getServiceErrorPagePath(String, Locale) getServiceErrorPagePath(application, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(String, Locale)}を使用してください。
     */
    public String getServiceErrorPagePath(String application)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath()}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(String application, Locale locale)
        throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     * このメソッドは{@link #getServiceErrorPagePath(Locale) getServiceErrorPagePath((java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceErrorPagePath(Locale)}を使用してください。
     */
    public String getServiceErrorPagePath() throws ServicePropertyException;

    /**
     * 処理例外時のページのパスを取得します。
     *
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getServiceErrorPagePath(Locale locale)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。
     * このメソッドは{@link #getSystemErrorPagePath(String, String, String, Locale) getSystemErrorPagePath(application, service, key, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(String, String, String, Locale)}を使用してください。
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String)}で取得されるページを返します。
     * このメソッドは{@link #getSystemErrorPagePath(String, String, Locale) getSystemErrorPagePath(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(String, String, Locale)}を使用してください。
     */
    public String getSystemErrorPagePath(String application, String service)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String)}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath()}で取得されるページを返します。
     * このメソッドは{@link #getSystemErrorPagePath(String, Locale) getSystemErrorPagePath(application, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(String, Locale)}を使用してください。
     */
    public String getSystemErrorPagePath(String application)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath()}で取得されるページを返します。
     *
     * @param application アプリケーションID
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(String application, Locale locale)
        throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     * このメソッドは{@link #getSystemErrorPagePath(Locale) getSystemErrorPagePath((java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getSystemErrorPagePath(Locale)}を使用してください。
     */
    public String getSystemErrorPagePath() throws ServicePropertyException;

    /**
     * システム例外時のページのパスを取得します。
     *
     * @param locale ロケール
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     * @since 4.2
     */
    public String getSystemErrorPagePath(Locale locale)
        throws ServicePropertyException;

    /**
     * サービスコントローラのクラス名を取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     * このメソッドは{@link #getServiceControllerName(String, String, Locale) getServiceControllerName(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getServiceControllerName(String, String, Locale)}を使用してください。
     */
    public String getServiceControllerName(String application, String service)
        throws ServicePropertyException;

    /**
     * サービスコントローラのクラス名を取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
     * @since 4.2
     */
    public String getServiceControllerName(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException;

    /**
     * トランジションのクラス名を取得します。
     * 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。
     * このメソッドは{@link #getTransitionName(String, String, Locale) getTransitionName(application, service, (java.util.Locale)null)}を呼んだときと同じ結果になるように実装される必要があります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
     * @deprecated このメソッドではなく{@link #getTransitionName(String, String, Locale)}を使用してください。
     */
    public String getTransitionName(String application, String service)
        throws ServicePropertyException;

    /**
     * トランジションのクラス名を取得します。
     * 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。
     *
     * @param application アプリケーションID
     * @param service サービスID
     * @param locale ロケール
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
     * @since 4.2
     */
    public String getTransitionName(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException;

    /**
     * サービスサーブレットのパスを取得します。
     *
     * @return サービスサーブレットのパス
     * @throws ServicePropertyException サービスサーブレットのパスの取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getServiceServletPath() throws ServicePropertyException;

    /**
     * コンテキストパスを取得します。
     *
     * @return コンテキストパス(設定されていない場合はnull)
     * @throws ServicePropertyException コンテキストパスの取得時に例外が発生
     * @deprecated このメソッドではなく、javax.servlet.http.HttpRequestのgetContextPathを使用するようにしてください。
     */
    public String getContextPath() throws ServicePropertyException;

    /**
     * アプリケーションIDに該当するパラメータ名を取得します。
     * 設定されていない場合、{@link #DEFAULT_APPLICATION_PARAMETER}で定義されている値を返します。
     *
     * @return アプリケーションIDに該当するパラメータ名
     * @throws ServicePropertyException アプリケーションIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getApplicationParamName() throws ServicePropertyException;

    /**
     * サービスIDに該当するパラメータ名を取得します。
     * 設定されていない場合、{@link #DEFAULT_SERVICE_PARAMETER}で定義されている値を返します。
     *
     * @return サービスIDに該当するパラメータ名
     * @throws ServicePropertyException サービスIDに該当するパラメータ名の取得時に例外が発生
     * @deprecated この実装は廃止されました。
     */
    public String getServiceParamName() throws ServicePropertyException;

    /**
     * 例外ページに遷移するときにjavax.servlet.http.HttpServletRequestに例外情報を属性として追加する場合の属性名を取得します。
     *
     * @return 例外の属性名
     * @throws ServicePropertyException 属性名の取得時に例外が発生
     */
    public String getExceptionAttributeName() throws ServicePropertyException;
}
