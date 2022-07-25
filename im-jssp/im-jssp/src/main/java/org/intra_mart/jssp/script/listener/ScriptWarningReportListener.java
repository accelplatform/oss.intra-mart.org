package org.intra_mart.jssp.script.listener;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.ToolErrorReporter;

public class ScriptWarningReportListener implements ContextFactory.Listener{

	/* (non-Javadoc)
	 * @see org.mozilla.javascript.ContextFactory.Listener#contextCreated(org.mozilla.javascript.Context)
	 */
	public void contextCreated(Context cx) {
		cx.setErrorReporter(new ToolErrorReporter(true));
	}

	/* (non-Javadoc)
	 * @see org.mozilla.javascript.ContextFactory.Listener#contextReleased(org.mozilla.javascript.Context)
	 */
	public void contextReleased(Context cx) {
		// 何もしません
	}
	
}