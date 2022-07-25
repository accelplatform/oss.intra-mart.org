package org.intra_mart.jssp.view.mock;

import org.intra_mart.jssp.view.tag.ImartTagType;

public abstract class AbstractImartTagType implements ImartTagType {

	public String getTagName() {
		return getClass().getSimpleName().substring(0, getClass().getSimpleName().length() - "Tag".length()).toLowerCase();
	}
}
