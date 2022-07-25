package org.intra_mart.jssp.view.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mozilla.javascript.Scriptable;

public class AttributesTag extends AbstractImartTagType {

	public String doTag(Scriptable attr, Scriptable inner) {

		List<String> list = new ArrayList<String>();

		for (Object id : attr.getIds()) {
			if ("type".equals(id)) {
				continue;
			}

			list.add(id.toString());
		}

		Collections.sort(list);

		return list.toString();
	}
}
