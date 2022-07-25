package org.intra_mart.jssp.view.mock;

import java.text.DecimalFormat;

import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

public class NumberTag extends AbstractImartTagType {

	public String doTag(Scriptable attr, Scriptable inner) {

		Double number = new Double(ScriptRuntime.toNumber(attr.get("value", null)));
		Object format = attr.get("format", null);

		if (format instanceof String) {
			return new DecimalFormat((String) format).format(number);
		}

		return number.toString();
	}
}
