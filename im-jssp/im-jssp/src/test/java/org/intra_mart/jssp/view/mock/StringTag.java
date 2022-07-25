package org.intra_mart.jssp.view.mock;

import org.mozilla.javascript.Scriptable;

public class StringTag extends AbstractImartTagType {

	public String doTag(Scriptable attr, Scriptable inner) {
		return attr.get("value", attr).toString();
	}
}
