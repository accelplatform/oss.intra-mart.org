package org.intra_mart.framework.extension.seasar.event;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.event.EventListener;
import org.intra_mart.framework.base.event.EventListenerException;
import org.intra_mart.framework.base.event.EventListenerFactory;
import org.intra_mart.framework.base.event.StandardEventListener;
import org.intra_mart.framework.extension.seasar.util.ComponentNameCreator;
import org.intra_mart.framework.extension.seasar.util.ComponentUtil;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * S2EventListenerFactoryはEventListener生成時にS2Containerから取得します。<br>
 * EventListenerがS2Containerに登録されていない場合EventListenerを自動的に登録し、
 * 他のコンポーネントとバインドされたEventListenerを返します。<br>
 * 
 * このファクトリで指定できる初期パラメータは以下のとおりです。<BR>
 * <TABLE border="1">
 *     <TR>
 *         <TH>パラメータ名</TH>
 *         <TH>値</TH>
 *     </TR>
 *     <TR>
 *         <TD>listener</TD>
 *         <TD>生成するイベントリスナ</TD>
 *     </TR>
 * </TABLE>
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class S2EventListenerFactory implements EventListenerFactory {

    /**
     * イベントリスナのパラメータ名
     */
    private static final String PARAM_LISTENER = "listener";

    /**
     * 生成するStandardEventListenerのクラス名
     */
    String listenerName;

    private S2Container container;

    /**
     * S2EventListenerFactoryを新規に生成します。
     */
    public S2EventListenerFactory() {
        this.container = SingletonS2ContainerFactory.getContainer();
    }

    /**
     * イベントリスナを生成します。
     * 初期パラメータで指定されたクラスをイベントリスナとして生成します。
     * 初期パラメータで指定されたクラスは{@link StandardEventListener}を継承している必要があります。
     *
     * @param event イベント
     * @return イベントリスナ
     * @throws EventListenerException イベントリスナの生成に失敗
     */
    public EventListener create(Event event) throws EventListenerException {
        Object listenerObject = null;
        StandardEventListener listener = null;
        Class clazz = null;

        try {
        	clazz = Class.forName(this.listenerName);
            listenerObject = clazz.newInstance();
        } catch (Exception e) {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.FailedToCreateEventListener");
            } catch (MissingResourceException ex) {
            }
            if (event == null) {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", event is null", e);
            } else {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", application = " + event.getApplication() + ", key = " + event.getKey(), e);
            }
        }
        if (listenerObject instanceof StandardEventListener) {
        	String componentKey = ComponentNameCreator.createEventListenerName(event.getApplication(), event.getKey());
        	ComponentUtil.register(this.container, clazz, componentKey, InstanceDefFactory.PROTOTYPE);
        	listener = (StandardEventListener)container.getComponent(componentKey);
        } else {
            String message = null;
            try {
                message = ResourceBundle.getBundle("org.intra_mart.framework.base.event.i18n").getString("Common.EventListenerExtended");
            } catch (MissingResourceException e) {
            }
            if (event == null) {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", factory = " + this.getClass().getName() + ", event is null");
            } else {
                throw new EventListenerException(message + " : class = " + this.listenerName + ", factory = " + this.getClass().getName() + ", application = " + event.getApplication() + ", key = " + event.getKey());
            }
        }
        return listener;
    }

    /**
     * 初期パラメータを設定します。
     * 複数のパラメータが設定される場合、その順番は保証されません。
     *
     * @param name パラメータ名
     * @param value パラメータの値
     * @throws EventListenerException パラメータの設定時に例外が発生
     */
    public void initParam(String name, String value) throws EventListenerException {

        if (name != null) {
            if (name.equals(PARAM_LISTENER)) {
                // パラメータで指定されたイベントリスナの生成
                this.listenerName = value;
            }
        }
    }
}
