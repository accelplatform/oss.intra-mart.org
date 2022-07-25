package org.intra_mart.framework.extension.seasar.util;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;

/**
 * イントラマート、Seasar2連携のユーティリティクラスです。
 * 
 * @author INTRAMART
 * @since 6.0
 */
public class ComponentUtil {

	private ComponentUtil(){}
	
	/**
	 * 指定したキーでS2Containerにコンポーネントを登録します。
	 * 既にキーが登録されている場合は登録しません。
	 * 
	 * @param container S2Container
	 * @param component　S2Containerに登録するクラス
	 * @param componentKey コンポーネントキー
	 * @param instanceType インスタンス属性
	 */
    public static void register(S2Container container, Class component, String componentKey, InstanceDef instanceType) {
        ComponentDef componentDef = new ComponentDefImpl(component, componentKey);
        componentDef.setInstanceDef(instanceType);
        if (false == container.hasComponentDef(componentKey)) {
            container.register(componentDef);
        }
    }
}
