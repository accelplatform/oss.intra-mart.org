package org.intra_mart.framework.extension.seasar.util;

/**
 * 
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class ComponentNameCreator {
	
	public static String CONTROLLER_SUFFIX = "controller";

	public static String TRANSITION_SUFFIX = "transition";

	public static String EVENT_OBJECT_SUFFIX = "event_object";

	public static String EVENT_LISTENER_SUFFIX = "event_listener";

	public static String DAO_SUFFIX = "dao";

	public ComponentNameCreator(){}
	
	/**
	 * サービスコントローラのコンポーネント名を返します。
	 * 
	 * @param application アプリケーションID
	 * @param service サービスID
	 * @return コンポーネント名
	 */
	public static String createControllerName(String application, String service) {
		if (application == null || service == null) {
			return null;
		}
		return application + "-" + service + "-" + CONTROLLER_SUFFIX;
	}
	
	/**
	 * トランジションのコンポーネント名を返します。
	 * 
	 * @param application アプリケーションID
	 * @param service サービスID
	 * @return コンポーネント名
	 */
	public static String createTransitionName(String application, String service) {
		if (application == null || service == null) {
			return null;
		}
		return application + "-" + service + "-" + TRANSITION_SUFFIX;
	}
	
	/**
	 * イベントオブジェクトのコンポーネント名を返します。
	 * 
	 * @param application アプリケーションID
	 * @param eventKey　イベントキー
	 * @return　コンポーネント名
	 */
	public static String createEventName(String application, String eventKey) {
		if (application == null || eventKey == null) {
			return null;
		}
		return application + "-" + eventKey + "-" + EVENT_OBJECT_SUFFIX;
	}

	/**
	 * イベントリスナのコンポーネント名を返します。
	 * 
	 * @param application アプリケーションID
	 * @param eventKey　イベントキー
	 * @return　コンポーネント名
	 */
	public static String createEventListenerName(String application, String eventKey) {
		if (application == null || eventKey == null) {
			return null;
		}
		return application + "-" + eventKey + "-" + EVENT_LISTENER_SUFFIX;
	}

	/**
	 * daoのコンポーネント名を返します。
	 * 
	 * @param application アプリケーションID
	 * @param daoKey daoキー
	 * @return
	 */
	public static String createDaoName(String application, String daoKey) {
		if (application == null || daoKey == null) {
			return null;
		}
		return application + "-" + daoKey + "-" + DAO_SUFFIX;
	}	
}
