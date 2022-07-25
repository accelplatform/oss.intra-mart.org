package org.intra_mart.jssp.view;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.intra_mart.jssp.exception.IllegalTagException;
import org.intra_mart.jssp.script.ScriptScope;
import org.intra_mart.jssp.view.mock.AttributesTag;
import org.intra_mart.jssp.view.mock.ForTag;
import org.intra_mart.jssp.view.mock.NumberTag;
import org.intra_mart.jssp.view.mock.StringTag;
import org.intra_mart.jssp.view.tag.ImartTagType;
import org.intra_mart.jssp.view.tag.ImartTagTypeManger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

/**
 * ViewScope + ImartTagAnalyzerのテストケース.
 * 
 */
public class ViewScope_executeTest extends TestCase {

	private Context context;

	private Scriptable scope;

	@SuppressWarnings("deprecation")
	public ViewScope_executeTest() {

		for (Class<ImartTagType> imartTagType : new Class[] { StringTag.class, NumberTag.class, ForTag.class, AttributesTag.class }) {
			try {
				ImartTagTypeManger.getInstance().defineImartTag(imartTagType);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void setUp() throws Exception {
		context = Context.enter();
		scope = context.initStandardObjects();
		ScriptScope.entry(new ScriptScope(scope));
	}

	@Override
	protected void tearDown() throws Exception {
		ScriptScope.exit();
		Context.exit();
	}

	public void testViewScope01() {

		String tag = "<IMART type=\"string\" value=\"Hello world.\"></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("出力値はHello world.", "Hello world.", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope02() {

		String tag = "<IMART type=\"number\" value=\"1234\"></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("出力値は1234", ((double) 1234) + "", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope03() {

		String tag = "<IMART type=\"for\" start=\"0\" end=\"10\" item=\"item\">Hello!</IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("出力値はHello!*10", "Hello!Hello!Hello!Hello!Hello!Hello!Hello!Hello!Hello!Hello!", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope04() {

		String tag = "<IMART type=\"for\" start=\"0\" end=\"10\" item=\"item\"><IMART type=\"number\" value=item format=\"#\"></IMART></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("出力値は0123456789", "0123456789", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope05() {

		String tag = "<IMART type=\"attributes\" foo bar baz qux quux></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("出力値は属性のアルファベット順", "[bar, baz, foo, quux, qux]", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope06() {

		String tag = "<IMART></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			viewScope.execute(context, scope);
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		} catch (IllegalTagException e) {
			assertTrue(true);
		}
	}

	public void testViewScope07() {

		String tag = "<IMART type=\"string\" value=String></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("String関数を呼び出して戻す", "", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope08() {

		String tag = "<IMART type=\"string\" value=myValue></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		scope.put("myValue", scope, "myValueDesu");

		try {
			assertEquals("scopeに格納された値", "myValueDesu", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope09() {

		String tag = "<IMART type=\"string\" value=thisValueIsUndefined></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("scopeに格納された値が無いのでUndefined", Undefined.instance.toString(), viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}

	public void testViewScope10() {

		String tag = "<IMART type=\"string\" value=\"hello\\world\"></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(tag));

		try {
			assertEquals("hello\\world", "hello\\world", viewScope.execute(context, scope));
		} catch (JavaScriptException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		// カバレッジ埋め
		viewScope.print();
	}
}
