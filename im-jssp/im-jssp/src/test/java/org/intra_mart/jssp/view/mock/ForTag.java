package org.intra_mart.jssp.view.mock;

import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.view.tag.InnerTextObject;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;

public class ForTag extends AbstractImartTagType {

	public String doTag(Scriptable attr, Scriptable inner) {

		double start = ScriptRuntime.toNumber(attr.get("start", null));
		double end = ScriptRuntime.toNumber(attr.get("end", null));
		double step = attr.get("step", null) instanceof UniqueTag ? 1 : ScriptRuntime.toNumber(attr.get("step", null));

		StringBuffer result = new StringBuffer();

		Object item = attr.get("item", null);

		for (; start < end; start += step) {
			if (item != null) {
				ScriptScope.current().defineProperty(item.toString(), start, ScriptableObject.EMPTY);
			}

			result.append(((InnerTextObject) inner).execute());
		}

		return result.toString();
	}
}
