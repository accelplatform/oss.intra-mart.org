package org.intra_mart.jssp.script.listener;

import org.intra_mart.jssp.util.config.JSSPConfigHandler;
import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class DefineLanguageVersionContextFactoryListener  implements ContextFactory.Listener{

	public void contextCreated(Context cx) {
		JSSPConfigHandler config = JSSPConfigHandlerManager.getConfigHandler();
		int version = config.getLanguageVersion();
		cx.setLanguageVersion(version);
	}

	public void contextReleased(Context cx) {
		// 特にやることなし
	}
}
