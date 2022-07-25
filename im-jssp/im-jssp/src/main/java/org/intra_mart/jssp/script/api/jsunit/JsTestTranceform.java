package org.intra_mart.jssp.script.api.jsunit;

import org.w3c.dom.Element;

/**
 * 
 */
public interface JsTestTranceform {

	public static final String ROOT_NODE_NAME = "js-unit";

	public static final String SUIT_NODE_NAME = "js-test-suite";

	public static final String TEST_NODE_NAME = "js-test";

	public static final String FUNCTION_NODE_NAME = "test-function";

	public static final String ASSERTION_NODE_NAME = "assertion";

	public static final String ERROR_ASSERTION_NODE_NAME = "error-assertion";

	public static final String COMMENT_ATTR_NAME = "comment";

	public static final String TOTAL_ATTR_NAME = "total";

	public static final String FAILURE_ATTR_NAME = "failure";

	public static final String ERROR_ATTR_NAME = "error";

	public static final String PATH_ATTR_NAME = "path";

	public static final String NAME_ATTR_NAME = "name";

	public static final String LINE_ATTR_NAME = "line";

	public static final String TIME_ATTR_NAME = "time";

	/**
	 * DOMにノードを書き込みます。
	 * 
	 * @param parent
	 *            親ノード
	 * @return 作成されたノード
	 */
	public Element tranceform(Element parent);

}
