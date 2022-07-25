/*
 * EventTriggerList.java
 *
 * Created on 2001/12/04, 16:08
 */

package org.intra_mart.framework.base.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import org.intra_mart.framework.base.data.DataAccessController;

import java.util.MissingResourceException;

import org.intra_mart.framework.system.exception.SystemException;
import org.intra_mart.framework.system.exception.ApplicationException;

/**
 * イベントトリガの集合を扱います。
 * イベントトリガの生成し、順番に実行します。
 *
 * @author INTRAMART
 * @version 1.0
 */
public class EventTriggerList {

    /** イベントトリガの集合 */
    private Collection triggers;

    /** データアクセスコントローラ */
    private DataAccessController dataAccessController;

    /**
     * EventTriggerListを新規に生成します。
     * このコンストラクタではイベントの前に処理するEventTriggerListを生成します。
     * このコンストラクタは{@link #EventTriggerList(String, String, boolean) Event(application, key, true)}
     * と同じ結果となります。
     *
     * @param application アプリケーション名
     * @param key キー
     * @throws EventManagerException EventManager生成時に例外が発生した
     * @throws EventPropertyException イベントプロパティの取得時に例外が発生した
     * @throws EventTriggerException イベントトリガ生成時に例外が発生した
     */
    public EventTriggerList(String application, String key)
        throws EventManagerException, EventPropertyException, EventTriggerException {

        this(application, key, true);
    }

    /**
     * EventTriggerListを新規に生成します。
     * <code>pre</code>によってイベントの前に処理されるイベントトリガであるか
     * イベントの後に処理されるイベントトリガであるかを指定します。
     *
     * @param application アプリケーション名
     * @param key キー
     * @param pre true:イベントの前に起動するイベントトリガ、false:イベントの後に起動するイベントトリガ
     * @throws EventManagerException EventManager生成時に例外が発生した
     * @throws EventPropertyException イベントプロパティの取得時に例外が発生した
     * @throws EventTriggerException イベントトリガ生成時に例外が発生した
     * @since 4.3
     */
    public EventTriggerList(String application, String key, boolean pre)
        throws EventManagerException, EventPropertyException, EventTriggerException {

        Iterator triggerInfos = null;
        Object triggerObject = null;
        EventTrigger trigger = null;
        EventTriggerInfo currentInfo = null;

        // イベントトリガ情報の取得
        if (pre) {
            triggerInfos =
                EventManager
                    .getEventManager()
                    .getEventPropertyHandler()
                    .getEventTriggerInfos(application, key)
                    .iterator();
        } else {
            triggerInfos =
                EventManager
                    .getEventManager()
                    .getEventPropertyHandler()
                    .getPostEventTriggerInfos(application, key)
                    .iterator();
        }

        // イベントトリガの集合の初期化
        this.triggers = new Vector();
        while (triggerInfos.hasNext()) {
            currentInfo = (EventTriggerInfo)triggerInfos.next();

            // イベントトリガを新規に生成する
            try {
                triggerObject =
                    Class.forName(currentInfo.getName()).newInstance();
            } catch (Exception e) {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.event.i18n")
                            .getString("EventTriggerList.FailedToCreateTrigger");
                } catch (MissingResourceException ex) {
                }
                throw new EventTriggerException(
                    message
                        + " : trigger class = "
                        + currentInfo.getName()
                        + ", application = "
                        + application
                        + ", key = "
                        + key,
                    e);
            }
            if (triggerObject instanceof EventTrigger) {
                trigger = (EventTrigger)triggerObject;
            } else {
                String message = null;
                try {
                    message =
                        ResourceBundle
                            .getBundle("org.intra_mart.framework.base.event.i18n")
                            .getString("EventTriggerList.TriggerImplemented");
                } catch (MissingResourceException e) {
                }
                throw new EventTriggerException(
                    message
                        + " : trigger class = "
                        + currentInfo.getName()
                        + ", application = "
                        + application
                        + ", key = "
                        + key);
            }

            // 生成したイベントトリガを集合の最後尾に追加する
            this.triggers.add(trigger);
        }
    }

    /**
     * すべてのトリガに対してイベントの処理を行います。
     *
     * @param event イベント
     * @param controller データアクセスコントローラ
     * @throws SystemException システム例外が発生した
     * @throws ApplicationException アプリケーション例外が発生した
     */
    public void fireAll(Event event, DataAccessController controller)
        throws SystemException, ApplicationException {

        Iterator triggerIterator;
        EventTrigger currentTrigger;

        // イベントトリガが設定されていない場合、何もしない
        if (this.triggers == null) {
            return;
        }

        // イベントトリガが設定されている場合
        triggerIterator = this.triggers.iterator();
        while (triggerIterator.hasNext()) {
            currentTrigger = (EventTrigger)triggerIterator.next();
            currentTrigger.fire(event, controller);
        }
    }
}
