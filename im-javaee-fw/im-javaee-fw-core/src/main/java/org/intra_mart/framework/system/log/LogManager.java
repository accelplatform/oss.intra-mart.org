/*
 * LogManager.java
 *
 * Created on 2003/07/10, 18:00
 */

package org.intra_mart.framework.system.log;

import java.util.ResourceBundle;

import org.intra_mart.framework.system.property.PropertyManager;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyManagerException;

/**
 * ログマネージャです。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class LogManager {

    /**
     * ログフレームワークのログのプレフィックス
     */
    static String LOG_HEAD = "[J2EE][Log]";

    /**
     * ログプロパティハンドラのキー
     */
    public static final String LOG_PROPERTY_HANDLER_KEY = "log";

    /**
     * ログマネージャ取得フラグ
     */
    private static Boolean managerFlag = new Boolean(false);

    /**
     * ログマネージャ
     */
    private static LogManager manager = null;

    /**
     * ログプロパティハンドラ
     */
    private LogPropertyHandler logPropertyHandler = null;

    /**
     * ログエージェント
     */
    private LogAgent agent = null;

    /**
     * ログマネージャを取得します。
     *
     * @return ログマネージャ
     */
    public static LogManager getLogManager() {

        if (!managerFlag.booleanValue()) {
            synchronized (managerFlag) {
                if (!managerFlag.booleanValue()) {
                    manager = new LogManager();
                    managerFlag = new Boolean(true);
                    String message = null;
                }

                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.system.log.i18n")
                            .getString("LogManager.SuccessedToCreateManager");
                } catch (MissingResourceException ex) {
                }
                manager.getLogAgent().sendMessage(
                    LogManager.class.getName(),
                    LogConstant.LEVEL_INFO,
                    LOG_HEAD + message);
            }
        }

        return manager;
    }

    /**
     * LogManagerを生成するコンストラクタです。
     * このコンストラクタは明示的に呼び出すことはできません。
     */
    private LogManager() {
        PropertyManager propertyManager = null;
        Object agentObject = null;
        String className = null;
        LogAgent tempAgent = null;

        // プロパティマネージャの取得
        try {
            propertyManager = PropertyManager.getPropertyManager();
        } catch (PropertyManagerException e) {
            // プロパティマネージャの取得に失敗した場合、
            // デフォルトのログエージェントを生成する。
            this.agent = new DefaultLogAgent();
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.system.log.i18n")
                        .getString("LogManager.FailedToGetPropertyManager");
            } catch (MissingResourceException ex) {
            }
            this.agent.sendMessage(
                LogManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                message + " : " + LOG_PROPERTY_HANDLER_KEY,
                e);
            return;
        }

        // ログプロパティハンドラの取得
        try {
            this.logPropertyHandler =
                (LogPropertyHandler)propertyManager.getPropertyHandler(
                    LOG_PROPERTY_HANDLER_KEY);
        } catch (PropertyHandlerException e) {
            // ログプロパティハンドラの取得に失敗した場合、
            // デフォルトのログエージェントを生成する。
            this.agent = new DefaultLogAgent();
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.system.log.i18n")
                        .getString("LogManager.FailedToGetLogPropertyHandler");
            } catch (MissingResourceException ex) {
            }
            this.agent.sendMessage(
                LogManager.class.getName(),
                LogConstant.LEVEL_WARNNING,
                message + " : " + LOG_PROPERTY_HANDLER_KEY);
            return;
        }

        // ログエージェントのクラス名の取得
        try {
            className = this.logPropertyHandler.getLogAgentName();
        } catch (LogPropertyException e) {
            // ログエージェントのクラス名取得に失敗した場合、
            // デフォルトのログエージェントを生成する。
            this.agent = new DefaultLogAgent();
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.system.log.i18n")
                        .getString("LogManager.FailedToGetAgentName");
            } catch (MissingResourceException ex) {
            }
            this.agent.sendMessage(
                LogManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                message + " : " + LOG_PROPERTY_HANDLER_KEY,
                e);
            return;
        }

        // ログエージェントの生成
        if (className != null) {
            try {
                agentObject = Class.forName(className).newInstance();
            } catch (Exception e) {
                // ログエージェントの生成に失敗した場合、
                // デフォルトのログエージェントを生成する。
                this.agent = new DefaultLogAgent();
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.system.log.i18n")
                            .getString("LogManager.FailedToCreateAgent");
                } catch (MissingResourceException ex) {
                }
                this.agent.sendMessage(
                    LogManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    message + " : " + LOG_PROPERTY_HANDLER_KEY,
                    e);
                return;
            }
            if (agentObject instanceof LogAgent) {
                tempAgent = (LogAgent)agentObject;
            } else {
                // ログエージェントの生成に失敗した場合、
                // デフォルトのログエージェントを生成する。
                this.agent = new DefaultLogAgent();
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.system.log.i18n")
                            .getString("LogManager.AgentImplemented");
                } catch (MissingResourceException ex) {
                }
                this.agent.sendMessage(
                    LogManager.class.getName(),
                    LogConstant.LEVEL_ERROR,
                    message
                        + " ("
                        + agentObject.getClass().getName()
                        + ") : "
                        + LOG_PROPERTY_HANDLER_KEY);
                return;
            }
        }

        // ログエージェントの初期化
        LogAgentParam[] params = null;
        try {
            params = this.logPropertyHandler.getLogAgentParams();
        } catch (LogPropertyException e) {
            // 初期化パラメータの取得に失敗した場合、
            // パラメータはなし
            this.agent = tempAgent;
            String message = null;
            try {
                message =
                    ResourceBundle
                        .getBundle("org.intra_mart.framework.system.log.i18n")
                        .getString("LogManager.FailedToGetAgentParameters");
            } catch (MissingResourceException ex) {
            }
            this.agent.sendMessage(
                LogManager.class.getName(),
                LogConstant.LEVEL_ERROR,
                message + " : " + LOG_PROPERTY_HANDLER_KEY,
                e);
            return;
        }
        tempAgent.init(params);
        this.agent = tempAgent;
    }

    /**
     * ログプロパティハンドラを取得します。
     *
     * @return ログプロパティハンドラ
     */
    public LogPropertyHandler getLogPropertyHandler() {
        return this.logPropertyHandler;
    }

    /**
     * ログエージェントを取得します。
     *
     * @return ログエージェント
     */
    public LogAgent getLogAgent() {
        return this.agent;
    }
}
