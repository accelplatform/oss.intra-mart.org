package org.intra_mart.jssp.script.listener;

import java.util.ArrayList;
import java.util.List;

import org.intra_mart.jssp.util.config.JSSPConfigHandlerManager;
import org.mozilla.javascript.ContextFactory;

public class ContextFactoryListenerManager {

	public static synchronized ContextFactory.Listener[] getListeners() 
						throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		List<ContextFactory.Listener> listenersList = new ArrayList<ContextFactory.Listener>();
		
		String[] listenerClassNames = JSSPConfigHandlerManager.getConfigHandler().getContextFactoryListeners();		

		for(String listener : listenerClassNames){
			listenersList.add((ContextFactory.Listener) Class.forName(listener).newInstance());
		}
		
		return listenersList.toArray(new ContextFactory.Listener[listenersList.size()]);		
	}
}
