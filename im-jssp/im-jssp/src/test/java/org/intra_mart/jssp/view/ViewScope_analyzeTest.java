package org.intra_mart.jssp.view;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;

import org.intra_mart.jssp.view.tag.ImartTagAnalyzer;

/**
 * ViewScope + ImartTagAnalyzerのテストケース.
 * 
 */
public class ViewScope_analyzeTest extends TestCase {

	/** ImartTagAnalyzer.TYPE.DYNAMIC */
	private static Object DYNAMIC;

	/** ImartTagAnalyzer.TYPE.STATIC */
	private static Object STATIC;

	/** ImartTagAnalyzer.TYPE.SINGLE */
	private static Object SINGLE;

	// private enum TYPEを引っこ抜きます.
	static {

		try {
			Class<?>[] declaredClasses = ImartTagAnalyzer.class.getDeclaredClasses();

			Class typeClass = null;
			for (Class<?> clazz : declaredClasses) {
				if (clazz.getName().equals(ImartTagAnalyzer.class.getName() + "$TYPE")) {
					typeClass = clazz;
				}
			}

			for (Field field : typeClass.getDeclaredFields()) {
				field.setAccessible(true);
				if ("DYNAMIC".equals(field.getName())) {
					DYNAMIC = field.get(typeClass);
				} else if ("STATIC".equals(field.getName())) {
					STATIC = field.get(typeClass);
				} else if ("SINGLE".equals(field.getName())) {
					SINGLE = field.get(typeClass);
				}
			}
		} catch (SecurityException e) {
			throw new InternalError(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			throw new InternalError(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			throw new InternalError(e.getLocalizedMessage());
		}
	}

	/** 文字 */
	public void testViewScope01() {

		String source = "文字列のみの場合";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenのみであること", 1, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenの確認", source, string(element.get(0)));
	}

	/** 静的属性 */
	public void testViewScope02() {

		String source = "<IMART type=\"string\" />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
	}

	/** 動的属性 */
	public void testViewScope03() {

		String source = "<IMART value=source />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
	}

	/** 単一属性 */
	public void testViewScope04() {

		String source = "<IMART readonly />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** 静的属性 + 動的属性 + 単一属性 */
	public void testViewScope05() {

		String source = "<IMART type=\"string\" value=source readonly />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** 属性なしのタグ */
	public void testViewScope06() {

		String source = "<IMART />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		// Q: 良いのか不明?
		// TODO <IMART></IMART>の場合は文字列となっていたが... どうするべきか? そもそもこんな形で使わないので無問題?
		assertEquals("IMARTタグのみの場合はtokenとして扱われない(<IMART></IMART>の場合はtokenとして扱われる", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		// 属性は空である事
		assertTagEquals(element.get(1), "type", null, null);
	}

	/** 属性なし + ちょっと空白のタグ */
	public void testViewScope07() {

		String source = "<IMART  />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		// Q: 良いのか不明?
		assertEquals("文字tokenとタグであること", 2, element.size());
		assertTrue("1番目は文字tokenである事", element.get(0) instanceof char[]);
		assertTrue("2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));

		assertTrue("attributeは空であること", getAttribute((ImartTagAnalyzer) element.get(1)).isEmpty());
		assertTrue("valueは空であること", getValue((ImartTagAnalyzer) element.get(1)).isEmpty());
	}

	/** 静的属性 + 動的属性 + 単一属性 + 閉じタグの中に文字列がある -> ない */
	public void testViewScope08() {

		String source = "<IMART type=\"string\" value=source readonly />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** 動的属性 プロパティを参照してる */
	public void testViewScope09() {

		String source = "<IMART value=source.property.value />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "value", values("source", "property", "value"), DYNAMIC);
	}

	/** 文字token + タグ */
	public void testViewScope10() {

		String source = "文字じゃい<IMART type=\"string\" value=source readonly />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは\"文字じゃい\"であること", "文字じゃい", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** タグ + 文字token */
	public void testViewScope11() {

		String source = "<IMART type=\"string\" value=source readonly />文字じゃい";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事と、文字tokenであること", 3, element.size());
		assertTrue("1番目は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは\"\"であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);

		assertTrue("3番名は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("文字tokenは\"文字じゃい\"であること", "文字じゃい", string(element.get(2)));
	}

	/** 文字token + タグ + 文字token */
	public void testViewScope12() {

		String source = "<html><head><xIMART><IMART type=\"string\" value=source readonly />文字じゃい";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事と、文字tokenであること", 3, element.size());
		assertTrue("1番目は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは\"<html><head><xIMART>\"であること", "<html><head><xIMART>", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);

		assertTrue("3番名は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("文字tokenは\"文字じゃい\"であること", "文字じゃい", string(element.get(2)));
	}

	/** 閉じタグだけの場合は無視される事. */
	public void testViewScope13() {

		String source = "</IMART type=\"sakananoko\">";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("閉じタグだけなので何も存在しない事", 0, element.size());
	}

	/** 閉じタグだけのように見えて>が足りない場合. */
	public void testViewScope14() {

		String source = "</IMART type=\"sakananoko\"";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字の終わりに達している為無いものとして扱われる事", 0, element.size());
	}

	/** IMARTタグが開きっぱなしで閉じていない場合 */
	public void testViewScope15() {

		String source = "<IMART type=\"sakananoko\"";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());
		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertTrue("2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "sakananoko", STATIC);
	}

	/** タグ + タグ */
	public void testViewScope16() {

		String source = "<IMART type=\"string\" value=value /><IMART type=\"date\" value=date />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ + 文字token + タグ", 4, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("value"), DYNAMIC);

		assertTrue("3番は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("3番は空文字であること", "", string(element.get(2)));

		assertTrue("4番はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "value", values("date"), DYNAMIC);
	}

	/** タグ + タグ + タグ */
	public void testViewScope17() {

		String source = "<IMART type=\"string\" value=value /><IMART type=\"date\" value=date /><IMART type=\"input\" style=\"text\" value=foo.bar.baz.qux.quux readonly />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ + 文字token + タグ + 文字token + タグ", 6, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("value"), DYNAMIC);

		assertTrue("3番は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("3番は空文字であること", "", string(element.get(2)));

		assertTrue("4番はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "value", values("date"), DYNAMIC);

		assertTrue("5番は文字tokenである事", element.get(4) instanceof char[]);
		assertEquals("5番は空文字であること", "", string(element.get(4)));

		assertTrue("6番はタグである事", element.get(5) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(5), "type", "input", STATIC);
		assertTagEquals(element.get(5), "style", "text", STATIC);
		assertTagEquals(element.get(5), "value", values("foo", "bar", "baz", "qux", "quux"), DYNAMIC);
		assertTagEquals(element.get(5), "readonly", "readonly", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(5)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(5)).get("dummy"));
	}

	/** 文字 + タグ + 文字 + タグ + 文字 + タグ */
	public void testViewScope18() {

		String source = "ぽーにょ<IMART type=\"string\" value=value />ぽにょぽにょ<IMART type=\"date\" value=date />魚の子!<IMART type=\"input\" style=\"text\" value=foo.bar.baz.qux.quux readonly />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ + 文字token + タグ + 文字token + タグ", 6, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭はぽーにょであること", "ぽーにょ", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("value"), DYNAMIC);

		assertTrue("3番は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("3番はぽにょぽにょであること", "ぽにょぽにょ", string(element.get(2)));

		assertTrue("4番はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "value", values("date"), DYNAMIC);

		assertTrue("5番は文字tokenである事", element.get(4) instanceof char[]);
		assertEquals("5番は魚の子!であること", "魚の子!", string(element.get(4)));

		assertTrue("6番はタグである事", element.get(5) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(5), "type", "input", STATIC);
		assertTagEquals(element.get(5), "style", "text", STATIC);
		assertTagEquals(element.get(5), "value", values("foo", "bar", "baz", "qux", "quux"), DYNAMIC);
		assertTagEquals(element.get(5), "readonly", "readonly", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(5)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(5)).get("dummy"));
	}

	/** <タグ>文字</タグ> */
	public void testViewScope19() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?>avavavavava-</IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字tokenであること", 1, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"avavavavava-\"であること", "avavavavava-", string(element.get(0)));
	}

	/** <タグ /> */
	public void testViewScope20() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello? />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供はtokenが無い事", ViewScope.EMPTY, inner);
	}

	/** <タグ>\n</タグ> */
	public void testViewScope21() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?>\n</IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字tokenであること", 1, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"\\n\"であること", "\n", string(element.get(0)));
	}

	/** <タグ>\t</タグ> */
	public void testViewScope22() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?>\t</IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字tokenであること", 1, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"\\t\"であること", "\t", string(element.get(0)));
	}

	/** <タグ><子タグ /></タグ> */
	public void testViewScope23() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?><IMART type=\"string\" value=foo.bar.baz /></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字token + タグであること", 2, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"\"であること", "", string(element.get(0)));

		assertTrue("子供の2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("foo", "bar", "baz"), DYNAMIC);
	}

	/** <タグ>文字<子タグ></子タグ>文字</タグ> */
	public void testViewScope24() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?><div id=\"foo\"></div><IMART type=\"string\" value=foo.bar.baz /><pre></pre></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字token + タグ + 文字token であること", 3, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"<div id=\"foo\"></div>\"であること", "<div id=\"foo\"></div>", string(element.get(0)));

		assertTrue("子供の2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("foo", "bar", "baz"), DYNAMIC);

		assertTrue("子供の3番目は文字である事", element.get(2) instanceof char[]);
		assertEquals("子供の3番目は\"<pre></pre>\"であること", "<pre></pre>", string(element.get(2)));
	}

	/** <タグ><子タグ></子タグ><子タグ></子タグ></タグ> */
	public void testViewScope25() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?><IMART type=\"string\" value=foo.bar.baz /><IMART type=\"date\" format=\"yyyy/MM/dd\" value=item /></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字token + タグ + 文字token + タグ であること", 4, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"\"であること", "", string(element.get(0)));

		assertTrue("子供の2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("foo", "bar", "baz"), DYNAMIC);

		assertTrue("子供の3番目は文字である事", element.get(2) instanceof char[]);
		assertEquals("子供の3番目は\"\"であること", "", string(element.get(2)));

		assertTrue("子供の4番目はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "format", "yyyy/MM/dd", STATIC);
		assertTagEquals(element.get(3), "value", values("item"), DYNAMIC);

	}

	/** <タグ 属性 == 値></タグ> */
	public void testViewScope26() {

		String source = "<IMART type == \"hendesuzo-\" />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		// = = で分解される + "\""が存在しない為動的 + 値なしとみなされる
		assertTagEquals(element.get(1), "type", values(""), DYNAMIC);

		// 残った = は無視される為空属性名, 空動的値は存在しない
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get(""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get(""));

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供は存在しない事", ViewScope.EMPTY, inner);
	}

	/** <タグ 属性 ===== 値></タグ> */
	public void testViewScope27() {

		String source = "<IMART type ===== \"hendesuzo-\" />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		// = ===== で分解される + "\""が存在しない為動的 + 値なしとみなされる
		assertTagEquals(element.get(1), "type", values(""), DYNAMIC);

		// 残った = は無視される為空属性名, 空動的値は存在しない
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get(""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get(""));

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供は存在しない事", ViewScope.EMPTY, inner);
	}

	/** <タグ 属性 =&= 値></タグ> */
	public void testViewScope28() {

		String source = "<IMART type =&= \"hendesuzo-\" />";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);
		assertTagEquals(element.get(1), "type", values("&"), DYNAMIC);

		// 残った = は無視される為空属性名, 空動的値は存在しない
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get(""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get(""));

		// \"hendesuzo-\"は無視される
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("hendesuzo-"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("hendesuzo-"));
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("\"hendesuzo-\""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("\"hendesuzo-\""));

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供は存在しない事", ViewScope.EMPTY, inner);
	}

	/** <タグ /タグ> */
	public void testViewScope29() {

		String source = "<IMART /IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		// /IMARTが単独値とみなされる
		// FIXME ここがこれまででは /を含めて認識されていたが、singletag対応によって"/"が除去された状態で認識される
		assertTagEquals(element.get(1), "/IMART", "/IMART", SINGLE);

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は存在しない事", 0, element.size());
	}

	// ここまで <IMART xxx="xxx" />
	// ここから <IMART xxx="xxx"/>
	// 閉じタグの部分に空白がない場合

	/** 静的属性 */
	public void testViewScope30() {

		String source = "<IMART type=\"string\"/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
	}

	/** 動的属性 */
	public void testViewScope31() {

		String source = "<IMART value=source/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
	}

	/** 単一属性 */
	public void testViewScope32() {

		String source = "<IMART readonly/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** 静的属性 + 動的属性 + 単一属性 */
	public void testViewScope33() {

		String source = "<IMART type=\"string\" value=source readonly/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** 属性なしのタグ */
	public void testViewScope34() {

		String source = "<IMART/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		// Q: 良いのか不明...<IMART></IMART>の場合と同様に文字列tokenとして扱われる
		assertEquals("IMARTタグのみの場合はtokenとして扱われる", 1, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
	}

	/** 静的属性 + 動的属性 + 単一属性 + 閉じタグの中に文字列がある -> ない */
	public void testViewScope35() {

		String source = "<IMART type=\"string\" value=source readonly/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** 動的属性 プロパティを参照してる */
	public void testViewScope36() {

		String source = "<IMART value=source.property.value/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは空であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "value", values("source", "property", "value"), DYNAMIC);
	}

	/** 文字token + タグ */
	public void testViewScope37() {

		String source = "文字じゃい<IMART type=\"string\" value=source readonly/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事", 2, element.size());
		assertTrue("文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは\"文字じゃい\"であること", "文字じゃい", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);
	}

	/** タグ + 文字token */
	public void testViewScope38() {

		String source = "<IMART type=\"string\" value=source readonly/>文字じゃい";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事と、文字tokenであること", 3, element.size());
		assertTrue("1番目は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは\"\"であること", "", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);

		assertTrue("3番名は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("文字tokenは\"文字じゃい\"であること", "文字じゃい", string(element.get(2)));
	}

	/** 文字token + タグ + 文字token */
	public void testViewScope39() {

		String source = "<html><head><xIMART><IMART type=\"string\" value=source readonly/>文字じゃい";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字tokenと、IMARTタグである事と、文字tokenであること", 3, element.size());
		assertTrue("1番目は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("文字tokenは\"<html><head><xIMART>\"であること", "<html><head><xIMART>", string(element.get(0)));
		assertTrue("2番目のtokenはimartタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("source"), DYNAMIC);
		assertTagEquals(element.get(1), "readonly", "readonly", SINGLE);

		assertTrue("3番名は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("文字tokenは\"文字じゃい\"であること", "文字じゃい", string(element.get(2)));
	}

	/** タグ + タグ */
	public void testViewScope40() {

		String source = "<IMART type=\"string\" value=value/><IMART type=\"date\" value=date/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ + 文字token + タグ", 4, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("value"), DYNAMIC);

		assertTrue("3番は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("3番は空文字であること", "", string(element.get(2)));

		assertTrue("4番はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "value", values("date"), DYNAMIC);
	}

	/** タグ + タグ + タグ */
	public void testViewScope41() {

		String source = "<IMART type=\"string\" value=value/><IMART type=\"date\" value=date/><IMART type=\"input\" style=\"text\" value=foo.bar.baz.qux.quux readonly/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ + 文字token + タグ + 文字token + タグ", 6, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("value"), DYNAMIC);

		assertTrue("3番は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("3番は空文字であること", "", string(element.get(2)));

		assertTrue("4番はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "value", values("date"), DYNAMIC);

		assertTrue("5番は文字tokenである事", element.get(4) instanceof char[]);
		assertEquals("5番は空文字であること", "", string(element.get(4)));

		assertTrue("6番はタグである事", element.get(5) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(5), "type", "input", STATIC);
		assertTagEquals(element.get(5), "style", "text", STATIC);
		assertTagEquals(element.get(5), "value", values("foo", "bar", "baz", "qux", "quux"), DYNAMIC);
		assertTagEquals(element.get(5), "readonly", "readonly", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(5)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(5)).get("dummy"));
	}

	/** 文字 + タグ + 文字 + タグ + 文字 + タグ */
	public void testViewScope42() {

		String source = "ぽーにょ<IMART type=\"string\" value=value/>ぽにょぽにょ<IMART type=\"date\" value=date/>魚の子!<IMART type=\"input\" style=\"text\" value=foo.bar.baz.qux.quux readonly/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ + 文字token + タグ + 文字token + タグ", 6, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭はぽーにょであること", "ぽーにょ", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("value"), DYNAMIC);

		assertTrue("3番は文字tokenである事", element.get(2) instanceof char[]);
		assertEquals("3番はぽにょぽにょであること", "ぽにょぽにょ", string(element.get(2)));

		assertTrue("4番はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "value", values("date"), DYNAMIC);

		assertTrue("5番は文字tokenである事", element.get(4) instanceof char[]);
		assertEquals("5番は魚の子!であること", "魚の子!", string(element.get(4)));

		assertTrue("6番はタグである事", element.get(5) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(5), "type", "input", STATIC);
		assertTagEquals(element.get(5), "style", "text", STATIC);
		assertTagEquals(element.get(5), "value", values("foo", "bar", "baz", "qux", "quux"), DYNAMIC);
		assertTagEquals(element.get(5), "readonly", "readonly", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(5)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(5)).get("dummy"));
	}

	/** <タグ /> */
	public void testViewScope43() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供はtokenが無い事", ViewScope.EMPTY, inner);
	}

	/** <タグ><子タグ /></タグ> */
	public void testViewScope44() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?><IMART type=\"string\" value=foo.bar.baz/></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字token + タグであること", 2, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"\"であること", "", string(element.get(0)));

		assertTrue("子供の2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("foo", "bar", "baz"), DYNAMIC);
	}

	/** <タグ>文字<子タグ></子タグ>文字</タグ> */
	public void testViewScope45() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?><div id=\"foo\"></div><IMART type=\"string\" value=foo.bar.baz/><pre></pre></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字token + タグ + 文字token であること", 3, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"<div id=\"foo\"></div>\"であること", "<div id=\"foo\"></div>", string(element.get(0)));

		assertTrue("子供の2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("foo", "bar", "baz"), DYNAMIC);

		assertTrue("子供の3番目は文字である事", element.get(2) instanceof char[]);
		assertEquals("子供の3番目は\"<pre></pre>\"であること", "<pre></pre>", string(element.get(2)));
	}

	/** <タグ><子タグ></子タグ><子タグ></子タグ></タグ> */
	public void testViewScope46() {

		String source = "<IMART type=\"repeat\" list=myList item=\"item\" hello?><IMART type=\"string\" value=foo.bar.baz/><IMART type=\"date\" format=\"yyyy/MM/dd\" value=item/></IMART>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "repeat", STATIC);
		assertTagEquals(element.get(1), "list", values("myList"), DYNAMIC);
		assertTagEquals(element.get(1), "item", "item", STATIC);
		assertTagEquals(element.get(1), "hello?", "hello?", SINGLE);

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		element = getElement(getInner(element.get(1)));

		assertEquals("子供は文字token + タグ + 文字token + タグ であること", 4, element.size());

		assertTrue("子供の1番目は文字である事", element.get(0) instanceof char[]);
		assertEquals("子供の1番目は\"\"であること", "", string(element.get(0)));

		assertTrue("子供の2番目はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(1), "type", "string", STATIC);
		assertTagEquals(element.get(1), "value", values("foo", "bar", "baz"), DYNAMIC);

		assertTrue("子供の3番目は文字である事", element.get(2) instanceof char[]);
		assertEquals("子供の3番目は\"\"であること", "", string(element.get(2)));

		assertTrue("子供の4番目はタグである事", element.get(3) instanceof ImartTagAnalyzer);

		assertTagEquals(element.get(3), "type", "date", STATIC);
		assertTagEquals(element.get(3), "format", "yyyy/MM/dd", STATIC);
		assertTagEquals(element.get(3), "value", values("item"), DYNAMIC);

	}

	/** <タグ 属性 == 値></タグ> */
	public void testViewScope47() {

		String source = "<IMART type == \"hendesuzo-\"/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		// = = で分解される + "\""が存在しない為動的 + 値なしとみなされる
		assertTagEquals(element.get(1), "type", values(""), DYNAMIC);

		// 残った = は無視される為空属性名, 空動的値は存在しない
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get(""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get(""));

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供は存在しない事", ViewScope.EMPTY, inner);
	}

	/** <タグ 属性 ===== 値></タグ> */
	public void testViewScope48() {

		String source = "<IMART type ===== \"hendesuzo-\"/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);

		// = ===== で分解される + "\""が存在しない為動的 + 値なしとみなされる
		assertTagEquals(element.get(1), "type", values(""), DYNAMIC);

		// 残った = は無視される為空属性名, 空動的値は存在しない
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get(""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get(""));

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供は存在しない事", ViewScope.EMPTY, inner);
	}

	/** <タグ 属性 =&= 値></タグ> */
	public void testViewScope49() {

		String source = "<IMART type =&= \"hendesuzo-\"/>";

		ViewScope viewScope = new ViewScope(new StringReader(source));

		List<Object> element = getElement(viewScope);

		assertEquals("文字token + タグ", 2, element.size());

		assertTrue("先頭は文字tokenである事", element.get(0) instanceof char[]);
		assertEquals("先頭は空文字であること", "", string(element.get(0)));

		assertTrue("2番はタグである事", element.get(1) instanceof ImartTagAnalyzer);
		assertTagEquals(element.get(1), "type", values("&"), DYNAMIC);

		// 残った = は無視される為空属性名, 空動的値は存在しない
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get(""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get(""));

		// \"hendesuzo-\"は無視される
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("hendesuzo-"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("hendesuzo-"));
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("\"hendesuzo-\""));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("\"hendesuzo-\""));

		// 余計なのが無い事
		assertNull(getAttribute((ImartTagAnalyzer) element.get(1)).get("dummy"));
		assertNull(getValue((ImartTagAnalyzer) element.get(1)).get("dummy"));

		ViewScope inner = getInner(element.get(1));

		assertEquals("子供は存在しない事", ViewScope.EMPTY, inner);
	}

	// ここからユーティリティメソッド群
	/** 文字列変換 */
	private String string(Object value) {
		if (value instanceof char[]) {
			return new String((char[]) value);
		}

		return value + "";
	}

	/** collection */
	private Collection<String> values(String... values) {
		return new Vector<String>(Arrays.asList(values));
	}

	/**
	 * タグ属性確認
	 * 
	 * @param tag
	 *            タグObject.
	 * @param attributeName
	 *            属性名
	 * @param expectedValue
	 *            属性値
	 * @param expectedType
	 *            属性種別 (DYNAMIC, STATIC, SINGLE)
	 */
	private void assertTagEquals(Object tag, String attributeName, Object expectedValue, Object expectedType) {

		ImartTagAnalyzer _tag = (ImartTagAnalyzer) tag;

		Map<String, Object> attribute = getAttribute(_tag);
		assertEquals(attributeName + "@type: " + expectedType, expectedType, attribute.get(attributeName));

		Map<String, Object> value = getValue(_tag);
		assertEquals(attributeName + "@value: " + expectedValue, expectedValue, value.get(attributeName));

	}

	/** 内包するViewScopeを取得 */
	private ViewScope getInner(Object tag) {
		return getFieldValue(tag, "inner");
	}

	/** 内包するelementを取得 */
	private List<Object> getElement(ViewScope viewScope) {
		return getFieldValue(viewScope, "element");
	}

	/** 内包するattributeを取得 */
	private Map<String, Object> getAttribute(ImartTagAnalyzer imartTagAnalyzer) {
		return getFieldValue(imartTagAnalyzer, "attribute");
	}

	/** 内包するvalueを取得 */
	private Map<String, Object> getValue(ImartTagAnalyzer imartTagAnalyzer) {
		return getFieldValue(imartTagAnalyzer, "value");
	}

	/** リフレクションショートカット */
	@SuppressWarnings("unchecked")
	private <T> T getFieldValue(Object target, String fieldName) {

		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(target);
		} catch (Throwable t) {
			fail("フィールド値の取得に失敗");
		}

		return null;
	}
}
