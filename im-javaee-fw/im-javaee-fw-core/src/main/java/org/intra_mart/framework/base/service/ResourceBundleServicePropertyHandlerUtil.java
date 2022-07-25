/*
 * ResourceBundleServicePropertyHandlerUtil.java
 *
 * Created on 2002/08/12, 19:51
 */

package org.intra_mart.framework.base.service;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * 指定されたリソースバンドルからサービスプロパティを取得するユーティリティです。
 *
 * @author INTRAMART
 * @since 3.2
 */
class ResourceBundleServicePropertyHandlerUtil {

    /**
     * クライアントのエンコードを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return クライアントのエンコーディング
     * @throws ServicePropertyException クライアントのエンコードの取得時に例外が発生
     */
    public static String getClientEncoding(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String encoding;

        try {
            encoding = commonBundle.getString("client.encoding");
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetClientEncoding");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(message, e);
        }

        return encoding;
    }

    /**
     * クライアントのロケールを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return クライアントのロケール
     * @throws ServicePropertyException クライアントのロケールの取得時に例外が発生
     * @since 4.2
     */
    public static Locale getClientLocale(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String localeString = null;
        Locale locale = null;

        try {
            localeString = commonBundle.getString("client.locale");
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetClientLocale");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(message, e);
        }
        if (localeString != null) {
            locale = getRealLocale(localeString);
        }

        return locale;
    }

    /**
     * ロケール文字列から実際のロケールを取得します。
     * ロケール文字列は以下の書式に従います。<BR>
     * <I>言語</I>_<I>国</I>[_<I>バリアント</I>]<BR>
     * ロケール文字列のアンダーバー(_)はハイフン(-)であってもかまいません。
     *
     * @param localeString ロケール文字列
     * @return ロケール
     */
    private static Locale getRealLocale(String localeString) {
        StringTokenizer tokenizer = new StringTokenizer(localeString, "-_");
        if (tokenizer.countTokens() == 2) {
            String language = tokenizer.nextToken();
            String country = tokenizer.nextToken();
            return new Locale(language, country);
        } else if (tokenizer.countTokens() == 3) {
            String language = tokenizer.nextToken();
            String country = tokenizer.nextToken();
            String variant = tokenizer.nextToken();
            return new Locale(language, country, variant);
        } else {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.web.tag.i18n")
                        .getString("MessageTag.LocaleStringIncorrect");
            } catch (MissingResourceException e) {
            }
            throw new IllegalArgumentException(
                message + " : \"" + localeString + "\"");
        }
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getInputErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        String page = null;

        try {
            page =
                applicationBundle.getString(
                    "input.error.page.path." + service + "." + key);
        } catch (MissingResourceException e) {
            try {
                page =
                    getInputErrorPagePath(
                        commonBundle,
                        applicationBundle,
                        application,
                        service);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message
                        + " : application = "
                        + application
                        + ", service = "
                        + service
                        + ", key = "
                        + key,
                    e);
            }
        }

        return page;
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath(String)}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getInputErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application,
        String service)
        throws ServicePropertyException {
        String page = null;

        try {
            page =
                applicationBundle.getString("input.error.page.path." + service);
        } catch (MissingResourceException e) {
            try {
                page =
                    getInputErrorPagePath(
                        commonBundle,
                        applicationBundle,
                        application);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message
                        + " : application = "
                        + application
                        + ", service = "
                        + service,
                    e);
            }
        }

        return page;
    }

    /**
     * 入力例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getInputErrorPagePath()}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getInputErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application)
        throws ServicePropertyException {
        String page = null;

        try {
            page = applicationBundle.getString("input.error.page.path");
        } catch (MissingResourceException e) {
            try {
                page = getInputErrorPagePath(commonBundle);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message + " : application = " + application,
                    e);
            }
        }

        return page;
    }

    /**
     * 入力例外時のページのパスを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getInputErrorPagePath(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String page = null;

        try {
            page = commonBundle.getString("input.error.page.path");
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetInputErrorPagePath");
            } catch (MissingResourceException exc) {
            }
            throw new ServicePropertyException(message, e);
        }

        return page;
    }

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まる場合にこのメソッドを使用します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getNextPagePath(
        ResourceBundle applicationBundle,
        String application,
        String service)
        throws ServicePropertyException {
        String page;

        try {
            page = applicationBundle.getString("nextpage.path." + service);
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetNextPagePath");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(
                message
                    + " : application = "
                    + application
                    + ", service = "
                    + service,
                e);
        }

        return page;
    }

    /**
     * 遷移先のページのパスを取得します。
     * サービスから遷移先が一意に決まらない場合にこのメソッドを使用します。<CODE>key<CODE>が<CODE>null<CODE>の場合、{@link #getNextPagePath(String, String)}と同じ動作になります。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getNextPagePath(
        ResourceBundle applicationBundle,
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        String page;

        try {
            page =
                applicationBundle.getString(
                    "nextpage.path." + service + "." + key);
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetNextPagePath");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(
                message
                    + " : application = "
                    + application
                    + ", service = "
                    + service
                    + ", key = "
                    + key,
                e);
        }

        return page;
    }

    /**
     * サービスコントローラのクラス名を取得します。
     * 該当するサービスコントローラが存在しない場合、nullを返します。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @return サービスコントローラのクラス名、存在しない場合はnull
     * @throws ServicePropertyException サービスコントローラのクラス名の取得時に例外が発生
     */
    public static String getServiceControllerName(
        ResourceBundle applicationBundle,
        String application,
        String service)
        throws ServicePropertyException {
        String controller;

        try {
            controller =
                applicationBundle.getString("controller.class." + service);
        } catch (MissingResourceException e) {
            controller = null;
        }

        return controller;
    }

    /**
     * 処理例外時のページのパスを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getServiceErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        String page = null;

        try {
            page =
                applicationBundle.getString(
                    "service.error.page.path." + service + "." + key);
        } catch (MissingResourceException e) {
            try {
                page =
                    getServiceErrorPagePath(
                        commonBundle,
                        applicationBundle,
                        application,
                        service);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message
                        + " : application = "
                        + application
                        + ", service = "
                        + service
                        + ", key = "
                        + key,
                    e);
            }
        }

        return page;
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath(String)}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getServiceErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application,
        String service)
        throws ServicePropertyException {
        String page;

        try {
            page =
                applicationBundle.getString(
                    "service.error.page.path." + service);
        } catch (MissingResourceException e) {
            try {
                page =
                    getServiceErrorPagePath(
                        commonBundle,
                        applicationBundle,
                        application);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message
                        + " : application = "
                        + application
                        + ", service = "
                        + service,
                    e);
            }
        }

        return page;
    }

    /**
     * 処理例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getServiceErrorPagePath()}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getServiceErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application)
        throws ServicePropertyException {
        String page = null;

        try {
            page = applicationBundle.getString("service.error.page.path");
        } catch (MissingResourceException e) {
            try {
                page = getServiceErrorPagePath(commonBundle);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message + " : application = " + application,
                    e);
            }
        }

        return page;
    }

    /**
     * 処理例外時のページのパスを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getServiceErrorPagePath(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String page = null;

        try {
            page = commonBundle.getString("service.error.page.path");
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceErrorPagePath");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(message, e);
        }

        return page;
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String, String)}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @param key 遷移先のキー
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getSystemErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        String page = null;

        try {
            page =
                applicationBundle.getString(
                    "system.error.page.path." + service + "." + key);
        } catch (MissingResourceException e) {
            try {
                page =
                    getSystemErrorPagePath(
                        commonBundle,
                        applicationBundle,
                        application,
                        service);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetSystemErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message
                        + " : application = "
                        + application
                        + ", service = "
                        + service
                        + ", key = "
                        + key,
                    e);
            }
        }

        return page;
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath(String)}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getSystemErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application,
        String service)
        throws ServicePropertyException {
        String page = null;

        try {
            page =
                applicationBundle.getString(
                    "system.error.page.path." + service);
        } catch (MissingResourceException e) {
            try {
                page =
                    getSystemErrorPagePath(
                        commonBundle,
                        applicationBundle,
                        application);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetSystemErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message
                        + " : application = "
                        + application
                        + ", service = "
                        + service,
                    e);
            }
        }

        return page;
    }

    /**
     * システム例外時のページのパスを取得します。
     * 該当するページのパスが取得できない場合、{@link #getSystemErrorPagePath()}で取得されるページを返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getSystemErrorPagePath(
        ResourceBundle commonBundle,
        ResourceBundle applicationBundle,
        String application)
        throws ServicePropertyException {
        String page = null;

        try {
            page = applicationBundle.getString("system.error.page.path");
        } catch (MissingResourceException e) {
            try {
                page = getSystemErrorPagePath(commonBundle);
            } catch (ServicePropertyException ex) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.service.i18n")
                            .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetSystemErrorPagePath");
                } catch (MissingResourceException exc) {
                }
                throw new ServicePropertyException(
                    message + " : application = " + application,
                    e);
            }
        }

        return page;
    }

    /**
     * システム例外時のページのパスを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return 遷移先のページのパス
     * @throws ServicePropertyException 遷移先のページのパスの取得時に例外が発生
     */
    public static String getSystemErrorPagePath(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String page = null;

        try {
            page = commonBundle.getString("system.error.page.path");
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetSystemErrorPagePath");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(message, e);
        }

        return page;
    }

    /**
     * トランジションのクラス名を取得します。
     * 指定されたアプリケーションIDとサービスIDに対応するトランジション名を取得します。
     * 特に設定されていない場合、nullが返ります。
     *
     * @param applicationBundle アプリケーションのリソースバンドル
     * @param application アプリケーションID
     * @param service サービスID
     * @return トランジションのクラス名、設定されていない場合はnull
     * @throws ServicePropertyException トランジションのクラス名の取得時に例外が発生
     */
    public static String getTransitionName(
        ResourceBundle applicationBundle,
        String application,
        String service)
        throws ServicePropertyException {
        String controller;

        try {
            controller =
                applicationBundle.getString("transition.class." + service);
        } catch (MissingResourceException e) {
            controller = null;
        }

        return controller;
    }

    /**
     * サービスサーブレットのパスを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return サービスサーブレットのパス
     * @throws ServicePropertyException サービスサーブレットのパスの取得時に例外が発生
     */
    public static String getServiceServletPath(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String path;

        try {
            path = commonBundle.getString("servlet.path");
        } catch (MissingResourceException e) {
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.base.service.i18n")
                        .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetServiceServletPath");
            } catch (MissingResourceException ex) {
            }
            throw new ServicePropertyException(message, e);
        }

        return path;
    }

    /**
     * コンテキストパスを取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return コンテキストパス
     * @throws ServicePropertyException コンテキストパスの取得時に例外が発生
     * @deprecated このメソッドではなく、javax.servlet.http.HttpRequestのgetContextPathを使用するようにしてください。
     */
    public static String getContextPath(ResourceBundle commonBundle)
        throws ServicePropertyException {
        String path;

        try {
            path = commonBundle.getString("context.path");
            if (path != null && path.trim().equals("")) {
                path = null;
            }
        } catch (MissingResourceException e) {
            path = null;
        }

        return path;
    }

    /**
     * アプリケーションIDに該当するパラメータ名を取得します。
     * 設定されていない場合、{@link ServicePropertyHandler#DEFAULT_APPLICATION_PARAMETER}で定義されている値を返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return アプリケーションIDに該当するパラメータ名
     * @throws MissingResourceException アプリケーションIDに該当するパラメータ名の取得時に例外が発生
     */
    public static String getApplicationParamName(ResourceBundle commonBundle) {
        try {
            return commonBundle.getString("application.param");
        } catch (MissingResourceException e) {
            return ServicePropertyHandler.DEFAULT_APPLICATION_PARAMETER;
        }
    }

    /**
     * サービスIDに該当するパラメータ名を取得します。
     * 設定されていない場合、{@link ServicePropertyHandler#DEFAULT_SERVICE_PARAMETER}で定義されている値を返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return サービスIDに該当するパラメータ名
     * @throws MissingResourceException サービスIDに該当するパラメータ名の取得時に例外が発生
     */
    public static String getServiceParamName(ResourceBundle commonBundle) {
        try {
            return commonBundle.getString("service.param");
        } catch (MissingResourceException e) {
            return ServicePropertyHandler.DEFAULT_SERVICE_PARAMETER;
        }
    }

    /**
     * 例外ページに遷移するときにjavax.servlet.http.HttpServletRequestに例外情報を属性として追加する場合の属性名を取得します。
     * 設定されていない場合、{@link ServicePropertyHandler#DEFAULT_EXCEPTION_ATTRIBUTE}で定義されている値を返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return 例外の属性名
     * @throws MissingResourceException 属性名の取得時に例外が発生
     */
    public static String getExceptionAttributeName(ResourceBundle commonBundle)
        throws MissingResourceException {
        return commonBundle.getString("exception.attirbute");
    }

    /**
     * キャッシュ情報をすべて取得します。
     * {@link CacheRuleInfo}を定義された順番でソートしたCollectionとして取得します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return キャッシュ情報の一覧
     * @throws ServicePropertyException キャッシュ情報の取得時に例外が発生
     * @see CacheRule
     * @see Cache
     * @see CacheCondition
     * @since 4.2
     */
//    public static Collection getCacheRuleInfos(ResourceBundle commonBundle)
//        throws ServicePropertyException {
//        Enumeration enum = null;
//        CacheRuleInfo info = null;
//        TreeMap infos = new TreeMap();
//        ResourceBundle targetBundle = null;
//        String conditionPrefix = "cache.rule.condition.class.";
//        String conditionParamPrefix = "cache.rule.condition.param.";
//        HashMap conditionParamMap = new HashMap();
//        String cachePrefix = "cache.rule.cache.class.";
//        String cacheParamPrefix = "cache.rule.cache.param.";
//        HashMap cacheParamMap = new HashMap();
//        String conditionKey = null;
//        String cacheKey = null;
//        String conditionName = null;
//        String cacheName = null;
//        String option = null;
//        int num = 0;
//
//        // リソースからすべてのキーを取得する
//        enum = commonBundle.getKeys();
//
//        // キャッシュに該当するプロパティすべてに対して繰り返し処理をする
//        while (enum.hasMoreElements()) {
//            conditionKey = (String)enum.nextElement();
//            if (conditionKey.startsWith(conditionPrefix)) {
//                info = new CacheRuleInfo();
//
//                // ソート番号を設定する
//                num =
//                    Integer.parseInt(
//                        conditionKey.substring(conditionPrefix.length()));
//
//                // キャッシュ条件のクラス名を設定する
//                try {
//                    conditionName = commonBundle.getString(conditionKey);
//                } catch (MissingResourceException e) {
//                    String message = null;
//                    try {
//                        message =
//                            ResourceBundle
//                                .getBundle("org.intra_mart.framework.base.service.i18n")
//                                .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetCacheCondition");
//                    } catch (MissingResourceException ex) {
//                    }
//                    throw new ServicePropertyException(
//                        message + " : key = " + conditionKey,
//                        e);
//                }
//                if (conditionName == null || conditionName.equals("")) {
//                    // キャッシュ条件のクラス名を取得できない場合例外をthrow
//                    String message = null;
//                    try {
//                        message =
//                            ResourceBundle
//                                .getBundle("org.intra_mart.framework.base.service.i18n")
//                                .getString("ResourceBundleServicePropertyHandlerUtil.CacheConditionNotDeclared");
//                    } catch (MissingResourceException ex) {
//                    }
//                    throw new ServicePropertyException(
//                        message + " : key = " + conditionKey);
//                }
//                info.setCondition(conditionName);
//
//                // キャッシュのクラス名を設定する
//                cacheKey = cachePrefix + num;
//                try {
//                    cacheName = commonBundle.getString(cacheKey);
//                } catch (MissingResourceException e) {
//                    String message = null;
//                    try {
//                        message =
//                            ResourceBundle
//                                .getBundle("org.intra_mart.framework.base.service.i18n")
//                                .getString("ResourceBundleServicePropertyHandlerUtil.FailedToGetCache");
//                    } catch (MissingResourceException ex) {
//                    }
//                    throw new ServicePropertyException(
//                        message + " : key = " + cacheKey,
//                        e);
//                }
//                if (cacheName == null || cacheName.equals("")) {
//                    // キャッシュのクラス名を取得できない場合例外をthrow
//                    String message = null;
//                    try {
//                        message =
//                            ResourceBundle
//                                .getBundle("org.intra_mart.framework.base.service.i18n")
//                                .getString("ResourceBundleServicePropertyHandlerUtil.CacheNotDeclared");
//                    } catch (MissingResourceException ex) {
//                    }
//                    throw new ServicePropertyException(
//                        message + " : key = " + cacheKey);
//                }
//                info.setCache(cacheName);
//
//                // キャッシュ情報を追加する
//                infos.put(new Integer(num), info);
//            } else if (conditionKey.startsWith(conditionParamPrefix)) {
//                int sortKeyIndex =
//                    conditionKey.indexOf(".", conditionParamPrefix.length());
//                String number =
//                    conditionKey.substring(
//                        conditionParamPrefix.length(),
//                        sortKeyIndex);
//                String conditionParamName =
//                    conditionKey.substring(sortKeyIndex);
//                PropertyParam conditionParam = new PropertyParam();
//                conditionParam.setName(conditionParamName);
//                conditionParam.setValue(commonBundle.getString(conditionKey));
//                Vector innerConditionParams =
//                    (Vector)conditionParamMap.get(new Integer(number));
//                if (innerConditionParams == null) {
//                    innerConditionParams = new Vector();
//                    conditionParamMap.put(
//                        new Integer(number),
//                        innerConditionParams);
//                }
//                innerConditionParams.add(conditionParam);
//            } else if (conditionKey.startsWith(cacheParamPrefix)) {
//                int sortKeyIndex =
//                    conditionKey.indexOf(".", cacheParamPrefix.length());
//                String number =
//                    conditionKey.substring(
//                        cacheParamPrefix.length(),
//                        sortKeyIndex);
//                String conditionParamName =
//                    conditionKey.substring(sortKeyIndex);
//                PropertyParam cacheParam = new PropertyParam();
//                cacheParam.setName(conditionParamName);
//                cacheParam.setValue(commonBundle.getString(conditionKey));
//                Vector innerCacheParams =
//                    (Vector)cacheParamMap.get(new Integer(number));
//                if (innerCacheParams == null) {
//                    innerCacheParams = new Vector();
//                    cacheParamMap.put(new Integer(number), innerCacheParams);
//                }
//                innerCacheParams.add(cacheParam);
//            }
//        }
//
//        Iterator iter = infos.keySet().iterator();
//        while (iter.hasNext()) {
//            Integer ruleInfoKey = (Integer)iter.next();
//            CacheRuleInfo ruleInfo = (CacheRuleInfo)infos.get(ruleInfoKey);
//
//            // キャッシュ条件パラメータの設定
//            PropertyParam[] conditionParams = null;
//            Vector innerConditionParams =
//                (Vector)conditionParamMap.get(ruleInfoKey);
//            if (innerConditionParams != null) {
//                conditionParams =
//                    new PropertyParam[innerConditionParams.size()];
//                for (int i = 0; i < innerConditionParams.size(); i++) {
//                    conditionParams[i] =
//                        (PropertyParam)innerConditionParams.elementAt(i);
//                }
//            }
//            ruleInfo.setConditionParameters(conditionParams);
//
//            // キャッシュパラメータの設定
//            PropertyParam[] cacheParams = null;
//            Vector innerCacheParams = (Vector)cacheParamMap.get(ruleInfoKey);
//            if (innerCacheParams != null) {
//                cacheParams = new PropertyParam[innerCacheParams.size()];
//                for (int i = 0; i < innerCacheParams.size(); i++) {
//                    cacheParams[i] =
//                        (PropertyParam)innerCacheParams.elementAt(i);
//                }
//            }
//            ruleInfo.setCacheParameters(conditionParams);
//        }
//
//        return infos.values();
//    }

    /**
     * ログインユーザが使用するエンコードを保存しておくときの属性名を取得します。
     * 設定されていない場合、{@link ServicePropertyHandler#DEFAULT_ENCODING_ATTRIBUTE}で定義されている値を返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return エンコードの属性名
     */
    public static String getEncodingAttributeName(ResourceBundle commonBundle) {
        try {
            return commonBundle.getString("encoding.attribute");
        } catch (MissingResourceException e) {
            return ServicePropertyHandler.DEFAULT_ENCODING_ATTRIBUTE;
        }
    }

    /**
     * ログインユーザが使用するロケールを保存しておくときの属性名を取得します。
     * 設定されていない場合、{@link ServicePropertyHandler#DEFAULT_LOCALE_ATTRIBUTE}で定義されている値を返します。
     *
     * @param commonBundle 共通のリソースバンドル
     * @return ロケールの属性名
     */
    public static String getLocaleAttributeName(ResourceBundle commonBundle) {
        try {
            return commonBundle.getString("locale.attribute");
        } catch (MissingResourceException e) {
            return ServicePropertyHandler.DEFAULT_LOCALE_ATTRIBUTE;
        }
    }
}
