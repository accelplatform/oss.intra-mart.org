package org.intra_mart.jssp.util;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.intra_mart.jssp.exception.IllegalConversionException;
import org.intra_mart.jssp.script.FoundationScriptScope;
import org.intra_mart.jssp.script.ScriptScope;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;


public class JavaScriptUtilityTest extends TestCase {

	private static final String jsSourceCharset = "UTF-8";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Context.enter();
		JavaScriptUtility.initializeSampleData(null, null, null, null, -1);
	}
	

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		Context.exit();
	}


	public void testExecuteVoidFunction() {
		assertTrue("サーバサイドで実行する必要があるテストです", true);
	}

	public void testExecuteFunction() {
		assertTrue("サーバサイドで実行する必要があるテストです", true);
	}


	public void testConvertJavaBeans2JsObject_null() throws Exception {
		Object actual = JavaScriptUtility.javaBeanToJS(null);

		assertEquals(null, actual);
	}

	public void testConvertJavaBeans2JsObject_プリミティブ型_char() throws Exception {
		char value = 'あ';
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(char.class != actual.getClass());
		assertEquals(String.class, actual.getClass());
		assertEquals((new Character(value)).toString(), actual);
	}

	public void testConvertJavaBeans2JsObject_プリミティブ型_double() throws Exception {
		double value = 123.456;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(double.class != actual.getClass());
		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_プリミティブ型_float() throws Exception {
		float value = 123.456F;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(float.class != actual.getClass());
		assertEquals(Double.class, actual.getClass());
		assertEquals(value, ((Double)actual).floatValue());
	}
	
	public void testConvertJavaBeans2JsObject_プリミティブ型_long() throws Exception {
		long value = 123L;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(long.class != actual.getClass());
		assertEquals(Double.class, actual.getClass());
		assertEquals(value, ((Double)actual).longValue());
	}
	
	public void testConvertJavaBeans2JsObject_プリミティブ型_int() throws Exception {
		int value = 123;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(int.class != actual.getClass());
		assertEquals(Double.class, actual.getClass());
		assertEquals(value, ((Double)actual).intValue());
	}
	
	public void testConvertJavaBeans2JsObject_プリミティブ型_short() throws Exception {
		short value = 123;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(short.class != actual.getClass());
		assertEquals(Double.class, actual.getClass());
		assertEquals(value, ((Double)actual).shortValue());
	}
	
	public void testConvertJavaBeans2JsObject_プリミティブ型_byte() throws Exception {
		byte value = 123;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(byte.class != actual.getClass());
		assertEquals(Double.class, actual.getClass());
		assertEquals(value, ((Double)actual).byteValue());
	}
	
	public void testConvertJavaBeans2JsObject_プリミティブ型_boolean() throws Exception {
		boolean value = true;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertTrue(boolean.class != actual.getClass());
		assertEquals(Boolean.class, actual.getClass());
		assertEquals(value, actual);
	}


	public void testConvertJavaBeans2JsObject_ラッパークラス_String() throws Exception {
		String value = "藍上尾";
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(String.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Character() throws Exception {
		Character value = new Character('あ');
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(String.class, actual.getClass());
		assertEquals((new Character(value)).toString(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Double() throws Exception {
		Double value = new Double(123.456);
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
	}
	
	public void testConvertJavaBeans2JsObject_ラッパークラス_Double_MAX_VALUE() throws Exception {
		Double value = Double.MAX_VALUE;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Double_MIN_VALUE() throws Exception {
		Double value = Double.MIN_VALUE;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Double_NaN() throws Exception {
		Double value = Double.NaN;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
		assertEquals(ScriptRuntime.NaNobj, actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Double_NEGATIVE_INFINITY() throws Exception {
		Double value = Double.NEGATIVE_INFINITY;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Double_POSITIVE_INFINITY() throws Exception {
		Double value = Double.POSITIVE_INFINITY;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Float() throws Exception {
		Float value = new Float(123.456F);
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		// Doubleになります
		assertEquals(Double.class, actual.getClass()); 
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Long() throws Exception {
		Long value = new Long(123L);
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Long_MAX_VALUE() throws Exception {
		Long value = Long.MAX_VALUE;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Long_MIN_VALUE() throws Exception {
		Long value = Long.MIN_VALUE;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Integer() throws Exception {
		Integer value = new Integer(123);
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Short() throws Exception {
		Short value = new Short("123");
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Byte() throws Exception {
		Byte value = new Byte("123");
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Double.class, actual.getClass());
		assertEquals(value.doubleValue(), actual);
	}

	public void testConvertJavaBeans2JsObject_ラッパークラス_Boolean() throws Exception {
		Boolean value = Boolean.TRUE;
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Boolean.class, actual.getClass());
		assertEquals(value, actual);
	}

	public void testConvertJavaBeans2JsObject_javaLangObject() throws Exception {
		Object value = new Object();
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(Object.class, actual.getClass());
		assertEquals("そのままが返却されること", value, actual);
	}

	public void testConvertJavaBeans2JsObject_BeanのプロパティがjavaLangObject() throws Exception {
		Object expected = new Object();
		
		JavaScriptUtilityTestModelA value = new JavaScriptUtilityTestModelA();
		value.setPlainObject(expected);
		
		Object actual = JavaScriptUtility.javaBeanToJS(value);

		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObject = (ValueObject)actual;
		
		assertEquals(Object.class, jsObject.get("plainObject", jsObject).getClass());
		assertEquals("そのままが返却されること", expected, jsObject.get("plainObject", jsObject));
	}
	
	public void testConvertJavaBeans2JsObject_DateCalendar含むラッパークラスとプリミティブ型が定義されたJavaBeans() throws Exception {
		JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty value = new JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty();
		
		Object actual = JavaScriptUtility.javaBeanToJS(value);
		
		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObject = (ValueObject)actual;
		
		assertEquals(String.class, jsObject.get("valuePrimitiveChar", jsObject).getClass());
		assertEquals("ん", jsObject.get("valuePrimitiveChar", jsObject));
		assertEquals((new Double(12.34)).doubleValue(), jsObject.get("valuePrimitiveDouble", jsObject));
		assertEquals((new Float(56.78F)).doubleValue(), jsObject.get("valuePrimitiveFloat", jsObject));
		assertEquals((new Long(91011L)).doubleValue(), jsObject.get("valuePrimitiveLong", jsObject));
		assertEquals((new Integer(121314)).doubleValue(), jsObject.get("valuePrimitiveInt", jsObject));
		assertEquals((new Short("456")).doubleValue(), jsObject.get("valuePrimitiveShort", jsObject));
		assertEquals((new Byte("123")).doubleValue(), jsObject.get("valuePrimitiveByte", jsObject));
		assertEquals(true, jsObject.get("valuePrimitiveBoolean", jsObject));

		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayChar", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayChar", jsObject)).getLength());
		assertEquals("わ", ((NativeArray)jsObject.get("valuePrimitiveArrayChar", jsObject)).get(0, jsObject));
		assertEquals("を", ((NativeArray)jsObject.get("valuePrimitiveArrayChar", jsObject)).get(1, jsObject));
		assertEquals("ん", ((NativeArray)jsObject.get("valuePrimitiveArrayChar", jsObject)).get(2, jsObject));

		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayDouble", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayDouble", jsObject)).getLength());
		assertEquals((new Double(12.34)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayDouble", jsObject)).get(0, jsObject));
		assertEquals((new Double(56.78)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayDouble", jsObject)).get(1, jsObject));
		assertEquals((new Double(910.1112)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayDouble", jsObject)).get(2, jsObject));
		
		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayFloat", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayFloat", jsObject)).getLength());
		assertEquals((new Float(56.78F)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayFloat", jsObject)).get(0, jsObject));
		assertEquals((new Float(910.1112F)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayFloat", jsObject)).get(1, jsObject));
		assertEquals((new Float(1314.1516F)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayFloat", jsObject)).get(2, jsObject));

		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayLong", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayLong", jsObject)).getLength());
		assertEquals((new Long(91011L)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayLong", jsObject)).get(0, jsObject));
		assertEquals((new Long(121314L)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayLong", jsObject)).get(1, jsObject));
		assertEquals((new Long(151617L)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayLong", jsObject)).get(2, jsObject));
	
		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayInt", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayInt", jsObject)).getLength());
		assertEquals((new Integer(121314)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayInt", jsObject)).get(0, jsObject));
		assertEquals((new Integer(1516)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayInt", jsObject)).get(1, jsObject));
		assertEquals((new Integer(1718)).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayInt", jsObject)).get(2, jsObject));

		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayShort", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayShort", jsObject)).getLength());
		assertEquals((new Short("456")).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayShort", jsObject)).get(0, jsObject));
		assertEquals((new Short("789")).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayShort", jsObject)).get(1, jsObject));
		assertEquals((new Short("1011")).doubleValue(), ((NativeArray)jsObject.get("valuePrimitiveArrayShort", jsObject)).get(2, jsObject));

		// byte[]はStringに変換されます。
		byte[] bytes = new byte[3];
		bytes[0] = 123;
		bytes[1] = 124;
		bytes[2] = 125;
		assertEquals(String.class, jsObject.get("valuePrimitiveArrayByte", jsObject).getClass());
		assertEquals(new String(bytes, JavaScriptUtility.NON_CONVERT_CHARSET), jsObject.get("valuePrimitiveArrayByte", jsObject));
		
		assertEquals(NativeArray.class, jsObject.get("valuePrimitiveArrayBoolean", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valuePrimitiveArrayBoolean", jsObject)).getLength());
		assertEquals(true, ((NativeArray)jsObject.get("valuePrimitiveArrayBoolean", jsObject)).get(0, jsObject));
		assertEquals(false, ((NativeArray)jsObject.get("valuePrimitiveArrayBoolean", jsObject)).get(1, jsObject));
		assertEquals(false, ((NativeArray)jsObject.get("valuePrimitiveArrayBoolean", jsObject)).get(2, jsObject));

		
		assertEquals("藍上尾", jsObject.get("valueWrapperString", jsObject));
		assertEquals(String.class, jsObject.get("valueWrapperCharacter", jsObject).getClass());
		assertEquals("や", jsObject.get("valueWrapperCharacter", jsObject));
		assertEquals((new Double(141312)).doubleValue(), jsObject.get("valueWrapperDouble", jsObject));
		assertEquals(Double.MAX_VALUE, jsObject.get("valueWrapperDouble_MAX", jsObject));
		assertEquals(Double.MIN_VALUE, jsObject.get("valueWrapperDouble_MIN", jsObject));
		assertEquals(Double.NaN, jsObject.get("valueWrapperDouble_NaN", jsObject));
		assertEquals(Double.NEGATIVE_INFINITY, jsObject.get("valueWrapperDouble_NEGA_INF", jsObject));
		assertEquals(Double.POSITIVE_INFINITY, jsObject.get("valueWrapperDouble_POSI_INF", jsObject));
		assertEquals((new Float(11.109F)).doubleValue(), jsObject.get("valueWrapperFloat", jsObject));
		assertEquals((new Long(8765L)).doubleValue(), jsObject.get("valueWrapperLong", jsObject));
		assertEquals("Doubleに変換されることに注意", new Double(Long.MAX_VALUE).doubleValue(), jsObject.get("valueWrapperLong_MAX", jsObject));
		assertEquals("Doubleに変換されることに注意", new Double(Long.MIN_VALUE).doubleValue(), jsObject.get("valueWrapperLong_MIN", jsObject));
		assertEquals((new Integer(4321)).doubleValue(), jsObject.get("valueWrapperInteger", jsObject));
		assertEquals((new Short("34")).doubleValue(), jsObject.get("valueWrapperShort", jsObject));
		assertEquals((new Byte("12")).doubleValue(), jsObject.get("valueWrapperByte", jsObject));
		assertEquals(true, jsObject.get("valueWrapperBoolean", jsObject));

		System.out.println("JSのLong_MAXプロパティ: " + jsObject.get("valueWrapperLong_MAX", jsObject));
		System.out.println("Long.MAX_VALUE: " + Long.MAX_VALUE);

		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayString", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayString", jsObject)).getLength());
		assertEquals("藍上尾", ((NativeArray)jsObject.get("valueWrapperArrayString", jsObject)).get(0, jsObject));
		assertEquals("書きくけ子", ((NativeArray)jsObject.get("valueWrapperArrayString", jsObject)).get(1, jsObject));
		assertEquals("さしすせそ", ((NativeArray)jsObject.get("valueWrapperArrayString", jsObject)).get(2, jsObject));
		
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayCharacter", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).getLength());
		assertEquals(String.class, ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).get(0, jsObject).getClass());
		assertEquals("や", ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).get(0, jsObject));
		assertEquals(String.class, ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).get(1, jsObject).getClass());
		assertEquals("ゆ", ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).get(1, jsObject));
		assertEquals(String.class, ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).get(2, jsObject).getClass());
		assertEquals("よ", ((NativeArray)jsObject.get("valueWrapperArrayCharacter", jsObject)).get(2, jsObject));

		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayDouble", jsObject).getClass());
		assertEquals(6, ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).getLength());
		assertEquals((new Double(141312)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).get(0, jsObject));
		assertEquals(Double.MAX_VALUE, ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).get(1, jsObject));
		assertEquals(Double.MIN_VALUE, ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).get(2, jsObject));
		assertEquals(Double.NaN, ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).get(3, jsObject));
		assertEquals(Double.NEGATIVE_INFINITY, ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).get(4, jsObject));
		assertEquals(Double.POSITIVE_INFINITY, ((NativeArray)jsObject.get("valueWrapperArrayDouble", jsObject)).get(5, jsObject));

		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayFloat", jsObject).getClass());
		assertEquals(6, ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).getLength());
		assertEquals((new Float(11.109F)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).get(0, jsObject));
		assertEquals(new Double(Float.MAX_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).get(1, jsObject));
		assertEquals(new Double(Float.MIN_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).get(2, jsObject));
		assertEquals(new Double(Float.NaN).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).get(3, jsObject));
		assertEquals(new Double(Float.NEGATIVE_INFINITY).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).get(4, jsObject));
		assertEquals(new Double(Float.POSITIVE_INFINITY).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayFloat", jsObject)).get(5, jsObject));
		
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayLong", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayLong", jsObject)).getLength());
		assertEquals((new Long(8765L)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayLong", jsObject)).get(0, jsObject));
		assertEquals(new Double(Long.MAX_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayLong", jsObject)).get(1, jsObject));
		assertEquals(new Double(Long.MIN_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayLong", jsObject)).get(2, jsObject));

		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayInteger", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayInteger", jsObject)).getLength());
		assertEquals((new Integer(4321)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayInteger", jsObject)).get(0, jsObject));
		assertEquals(new Double(Integer.MAX_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayInteger", jsObject)).get(1, jsObject));
		assertEquals(new Double(Integer.MIN_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayInteger", jsObject)).get(2, jsObject));
	
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayShort", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayShort", jsObject)).getLength());
		assertEquals((new Short("34")).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayShort", jsObject)).get(0, jsObject));
		assertEquals(new Double(Short.MAX_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayShort", jsObject)).get(1, jsObject));
		assertEquals(new Double(Short.MIN_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayShort", jsObject)).get(2, jsObject));
	
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayByte", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayByte", jsObject)).getLength());
		assertEquals((new Byte("12")).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayByte", jsObject)).get(0, jsObject));
		assertEquals(new Double(Byte.MAX_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayByte", jsObject)).get(1, jsObject));
		assertEquals(new Double(Byte.MIN_VALUE).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayByte", jsObject)).get(2, jsObject));
		
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayBoolean", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayBoolean", jsObject)).getLength());
		assertEquals(false, ((NativeArray)jsObject.get("valueWrapperArrayBoolean", jsObject)).get(0, jsObject));
		assertEquals(false, ((NativeArray)jsObject.get("valueWrapperArrayBoolean", jsObject)).get(1, jsObject));
		assertEquals(true, ((NativeArray)jsObject.get("valueWrapperArrayBoolean", jsObject)).get(2, jsObject));

		assertEquals((new Double(9999.999)).doubleValue(), jsObject.get("valueWrapperNumber", jsObject));
		
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayNumber", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayNumber", jsObject)).getLength());
		assertEquals((new Double(9999.999)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayNumber", jsObject)).get(0, jsObject));
		assertEquals((new Double(8888.888)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayNumber", jsObject)).get(1, jsObject));
		assertEquals((new Double(7777.777)).doubleValue(), ((NativeArray)jsObject.get("valueWrapperArrayNumber", jsObject)).get(2, jsObject));
	
		// Tue Apr 15 2008 12:34:56 GMT+0900 (JST)
		Date actualDate = (Date) Context.jsToJava(jsObject.get("valueWrapperDate", jsObject), Date.class);
		assertEquals(new Date(1208230496000L), actualDate);
		
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayDate", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayDate", jsObject)).getLength());
		
		// Tue Apr 15 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(((NativeArray)jsObject.get("valueWrapperArrayDate", jsObject)).get(0, jsObject), Date.class);
		assertEquals(new Date(1208230496000L), actualDate);

		// Wed Apr 15 2009 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(((NativeArray)jsObject.get("valueWrapperArrayDate", jsObject)).get(1, jsObject), Date.class);
		assertEquals(new Date(1239766496000L), actualDate);

		// Thu Apr 15 2010 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(((NativeArray)jsObject.get("valueWrapperArrayDate", jsObject)).get(2, jsObject), Date.class);
		assertEquals(new Date(1271302496000L), actualDate);


		Calendar expectedCal = Calendar.getInstance();
		Calendar actualCal = Calendar.getInstance();

		expectedCal.setTimeInMillis(1227584096000L); // Tue Nov 25 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(jsObject.get("valueWrapperCalendar", jsObject), Date.class);
		actualCal.setTime(actualDate);
		assertEquals(expectedCal, actualCal);

		
		assertEquals(NativeArray.class, jsObject.get("valueWrapperArrayCalendar", jsObject).getClass());
		assertEquals(3, ((NativeArray)jsObject.get("valueWrapperArrayCalendar", jsObject)).getLength());
		
		expectedCal.setTimeInMillis(1227584096000L); // Tue Nov 25 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(((NativeArray)jsObject.get("valueWrapperArrayCalendar", jsObject)).get(0, jsObject), Date.class);
		actualCal.setTime(actualDate);
		assertEquals(expectedCal, actualCal);
		
		expectedCal.setTimeInMillis(1259120096000L); // Wed Nov 25 2009 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(((NativeArray)jsObject.get("valueWrapperArrayCalendar", jsObject)).get(1, jsObject), Date.class);
		actualCal.setTime(actualDate);
		assertEquals(expectedCal, actualCal);
		
		expectedCal.setTimeInMillis(1290656096000L); // Thu Nov 25 2010 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(((NativeArray)jsObject.get("valueWrapperArrayCalendar", jsObject)).get(2, jsObject), Date.class);
		actualCal.setTime(actualDate);
		assertEquals(expectedCal, actualCal);
	}
	
	public void testConvertJavaBeans2JsObject_変換対象外なクラス_List() throws Exception {
		Object actual;
		List value = new ArrayList();
		
		try {
			actual = JavaScriptUtility.javaBeanToJS(value);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}
		
		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObj = (ValueObject) actual;
		assertEquals(0, jsObj.getIds().length);
	}
	
	public void testConvertJavaBeans2JsObject_変換対象外なクラス_Map() throws Exception {
		Object actual;
		Map value = new HashMap(); 
		
		try {
			actual = JavaScriptUtility.javaBeanToJS(value);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}

		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObj = (ValueObject) actual;
		assertEquals(0, jsObj.getIds().length);
	}

	public void testConvertJavaBeans2JsObject_変換対象外なクラス_Set() throws Exception {
		Object actual;
		Set value = new HashSet();
		
		try {
			actual = JavaScriptUtility.javaBeanToJS(value);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}

		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObj = (ValueObject) actual;
		assertEquals(0, jsObj.getIds().length);
	}

	/**
	 * <pre>
	 * function createTestData(){
	 *     load('jssp/script/api/soap_client_helper');
	 * 
	 *     var beanType = "org.intra_mart.jssp.util.JavaScriptUtilityTestModelA";
	 *     var bean = Packages.org.intra_mart.jssp.util.JavaScriptUtility.newInstanceFilledProperty(beanType, "hoge");
	 *     var obj = Packages.org.intra_mart.jssp.util.JavaScriptUtility.convertJavaBeans2JsObject(bean);
	 *         obj = normalize(obj);	
	 *     Debug.console(obj);
	 * }
	 * </pre>
	 * 
	 * <pre>
	 *   0: ========== 1 ==========
	 *   1: /&#42; Object &lt;JavaScriptUtilityTestModelA&gt; &#42;/
	 *   2: {
	 *   3:     /&#42; Null &lt;JavaScriptUtilityTestModelA&gt; &#42;/
	 *   4:     "modelA" : null, 
	 *   5: 
	 *   6:     /&#42; Object &lt;JavaScriptUtilityTestModelB&gt; &#42;/
	 *   7:     "modelB" : {
	 *   8:         /&#42; Null &lt;JavaScriptUtilityTestModelA&gt; &#42;/
	 *   9:         "modelA" : null, 
	 *  10: 
	 *  11:         /&#42; Null &lt;JavaScriptUtilityTestModelB&gt; &#42;/
	 *  12:         "modelB" : null, 
	 *  13: 
	 *  14:         /&#42; Object &lt;JavaScriptUtilityTestModelC&gt; &#42;/
	 *  15:         "modelC" : {
	 *  16:             /&#42; Null &lt;JavaScriptUtilityTestModelA&gt; &#42;/
	 *  17:             "modelA" : null, 
	 *  18: 
	 *  19:             /&#42; Number &#42;/
	 *  20:             "valueWrapperNumberModelC" : 123, 
	 *  21: 
	 *  22:             /&#42; Null &lt;JavaScriptUtilityTestModelB&gt; &#42;/
	 *  23:             "modelB" : null, 
	 *  24: 
	 *  25:             /&#42; Null &lt;JavaScriptUtilityTestModelC&gt; &#42;/
	 *  26:             "modelC" : null, 
	 *  27: 
	 *  28:             /&#42; Array &lt;JavaScriptUtilityTestModelA[]&gt; &#42;/
	 *  29:             "modelArrayA" : [
	 *  30:                 
	 *  31:             ], 
	 *  32: 
	 *  33:             /&#42; Array &lt;JavaScriptUtilityTestModelB[]&gt; &#42;/
	 *  34:             "modelArrayB" : [
	 *  35:                 
	 *  36:             ], 
	 *  37: 
	 *  38:             /&#42; Boolean &#42;/
	 *  39:             "valueWrapperBooleanModelC" : true, 
	 *  40: 
	 *  41:             /&#42; Array &lt;JavaScriptUtilityTestModelC[]&gt; &#42;/
	 *  42:             "modelArrayC" : [
	 *  43:                 
	 *  44:             ], 
	 *  45: 
	 *  46:             /&#42; String &#42;/
	 *  47:             "valueWrapperStringModelC" : "prop_valueWrapperStringModelC", 
	 *  48: 
	 *  49:             /&#42; Date  // Thu Jun 19 2008 12:34:56 GMT+0900 (JST) &#42;/
	 *  50:             "valueWrapperDateModelC" : new Date(1213846496000)
	 *  51:         }, 
	 *  52: 
	 *  53:         /&#42; Array &lt;JavaScriptUtilityTestModelA[]&gt; &#42;/
	 *  54:         "modelArrayA" : [
	 *  55:             
	 *  56:         ], 
	 *  57: 
	 *  58:         /&#42; Array &lt;JavaScriptUtilityTestModelB[]&gt; &#42;/
	 *  59:         "modelArrayB" : [
	 *  60:             
	 *  61:         ], 
	 *  62: 
	 *  63:         /&#42; Boolean &#42;/
	 *  64:         "valueWrapperBooleanModelB" : true, 
	 *  65: 
	 *  66:         /&#42; Array &lt;JavaScriptUtilityTestModelC[]&gt; &#42;/
	 *  67:         "modelArrayC" : [
	 *  68:             
	 *  69:         ], 
	 *  70: 
	 *  71:         /&#42; String &#42;/
	 *  72:         "valueWrapperStringModelB" : "prop_valueWrapperStringModelB", 
	 *  73: 
	 *  74:         /&#42; Date  // Thu Jun 19 2008 12:34:56 GMT+0900 (JST) &#42;/
	 *  75:         "valueWrapperDateModelB" : new Date(1213846496000), 
	 *  76: 
	 *  77:         /&#42; Number &#42;/
	 *  78:         "valueWrapperNumberModelB" : 123
	 *  79:     }, 
	 *  80: 
	 *  81:     /&#42; Boolean &#42;/
	 *  82:     "valueWrapperBooleanModelA" : true, 
	 *  83: 
	 *  84:     /&#42; Array &lt;JavaScriptUtilityTestModelA[]&gt; &#42;/
	 *  85:     "modelArrayA" : [
	 *  86:         
	 *  87:     ], 
	 *  88: 
	 *  89:     /&#42; Array &lt;JavaScriptUtilityTestModelB[]&gt; &#42;/
	 *  90:     "modelArrayB" : [
	 *  91:         
	 *  92:     ], 
	 *  93: 
	 *  94:     /&#42; String &#42;/
	 *  95:     "valueWrapperStringModelA" : "prop_valueWrapperStringModelA", 
	 *  96: 
	 *  97:     /&#42; Date  // Thu Jun 19 2008 12:34:56 GMT+0900 (JST) &#42;/
	 *  98:     "valueWrapperDateModelA" : new Date(1213846496000), 
	 *  99: 
	 * 100:     /&#42; Number &#42;/
	 * 101:     "valueWrapperNumberModelA" : 123
	 * 102: }
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public void testConvertJavaBeans2JsObject_JavaBeans() throws Exception {
		// 可逆性あり！
		JavaScriptUtilityTestModelA testData = 
			(JavaScriptUtilityTestModelA)JavaScriptUtility.newInstanceFilledSampleData(JavaScriptUtilityTestModelA.class, null);

		Object actual = JavaScriptUtility.javaBeanToJS(testData);

		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObj = (ValueObject)actual;

		String propName;
		String expected;
		String actualString;
		Object actualValue;
		

		// 1行目
		propName = "";
		expected = JavaScriptUtilityTestModelA.class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
		
		// 3行目
		propName = "modelA";
		expected = JavaScriptUtilityTestModelA.class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 4行目
		assertEquals(null, jsObj.get(propName, jsObj));
		
		// 6行目
		propName = "modelB";
		expected = JavaScriptUtilityTestModelB.class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 7行目
		Object modelB = jsObj.get("modelB", jsObj);
		assertNotNull(modelB);
		assertEquals(ValueObject.class, modelB.getClass());
		ValueObject jsModelB = (ValueObject)modelB;
		
		// 8行目
		propName = "modelA";
		expected = JavaScriptUtilityTestModelA.class.getSimpleName();
		actualString = (String) jsModelB.get("__javaClassName_" + propName + "__", jsModelB);
		assertEquals(expected, actualString);
		
		// 9行目
		assertEquals(null, jsModelB.get(propName, jsObj));
			
		// 11行目
		propName = "modelB";
		expected = JavaScriptUtilityTestModelB.class.getSimpleName();
		actualString = (String) jsModelB.get("__javaClassName_" + propName + "__", jsModelB);
		assertEquals(expected, actualString);

		// 12行目
		assertEquals(null, jsModelB.get(propName, jsObj));
		
		// 14行目
		propName = "modelC";
		expected = JavaScriptUtilityTestModelC.class.getSimpleName();
		actualString = (String) jsModelB.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 15行目
		Object modelC = jsModelB.get("modelC", jsModelB);
		assertNotNull(modelC);
		assertEquals(ValueObject.class, modelC.getClass());
		ValueObject jsModelC = (ValueObject)modelC;
		
		// 16行目
		propName = "modelA";
		expected = JavaScriptUtilityTestModelA.class.getSimpleName();
		actualString = (String) jsModelC.get("__javaClassName_" + propName + "__", jsModelC);
		assertEquals(expected, actualString);
		
		// 17行目
		assertEquals(null, jsModelC.get(propName, jsObj));
			
		// 20行目
		actualValue = jsModelC.get("valueWrapperNumberModelC", jsModelC);
		assertEquals(Double.class, actualValue.getClass());
		assertEquals(new Double(123), actualValue);
		
		// 22行目
		propName = "modelB";
		expected = JavaScriptUtilityTestModelB.class.getSimpleName();
		actualString = (String) jsModelC.get("__javaClassName_" + propName + "__", jsModelC);
		assertEquals(expected, actualString);

		// 23行目
		assertEquals(null, jsModelC.get(propName, jsObj));

		// 25行目
		propName = "modelC";
		expected = JavaScriptUtilityTestModelC.class.getSimpleName();
		actualString = (String) jsModelC.get("__javaClassName_" + propName + "__", jsModelC);
		assertEquals(expected, actualString);

		// 26行目
		assertEquals(null, jsModelC.get(propName, jsObj));
		
		// 28行目
		propName = "modelArrayA";
		expected = JavaScriptUtilityTestModelA[].class.getSimpleName();
		actualString = (String) jsModelC.get("__javaClassName_" + propName + "__", jsModelC);
		assertEquals(expected, actualString);
		
		// 29行目
		Object modelArrayA = jsModelC.get("modelArrayA", jsModelC);
		assertNotNull(modelArrayA);
		assertEquals(NativeArray.class, modelArrayA.getClass());
		NativeArray jsModelArrayA = (NativeArray)modelArrayA;
		assertEquals(0, jsModelArrayA.getLength());

		// 33行目
		propName = "modelArrayB";
		expected = JavaScriptUtilityTestModelB[].class.getSimpleName();
		actualString = (String) jsModelC.get("__javaClassName_" + propName + "__", jsModelC);
		assertEquals(expected, actualString);
		
		// 34行目
		Object modelArrayB = jsModelC.get("modelArrayB", jsModelC);
		assertNotNull(modelArrayB);
		assertEquals(NativeArray.class, modelArrayB.getClass());
		NativeArray jsModelArrayB = (NativeArray)modelArrayB;
		assertEquals(0, jsModelArrayB.getLength());

		// 39行目
		Object valueWrapperBooleanModelC = jsModelC.get("valueWrapperBooleanModelC", jsModelC);
		assertNotNull(valueWrapperBooleanModelC);
		assertEquals(true, valueWrapperBooleanModelC);

		// 41行目
		propName = "modelArrayC";
		expected = JavaScriptUtilityTestModelC[].class.getSimpleName();
		actualString = (String) jsModelC.get("__javaClassName_" + propName + "__", jsModelC);
		assertEquals(expected, actualString);
		
		// 42行目
		Object modelArrayC = jsModelC.get("modelArrayC", jsModelC);
		assertNotNull(modelArrayC);
		assertEquals(NativeArray.class, modelArrayC.getClass());
		NativeArray jsModelArrayC = (NativeArray)modelArrayC;
		assertEquals(0, jsModelArrayC.getLength());
		
		// 47行目
		Object valueWrapperStringModelC = jsModelC.get("valueWrapperStringModelC", jsModelC);
		assertNotNull(valueWrapperStringModelC);
		assertEquals("prop_valueWrapperStringModelC", valueWrapperStringModelC);

		// 50行目
		Object valueWrapperDateModelC = jsModelC.get("valueWrapperDateModelC", jsModelC);
		assertNotNull(valueWrapperDateModelC);
		Date date = (Date)Context.jsToJava(valueWrapperDateModelC, Date.class);
		assertEquals(new Date(1213846496000L), date); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST) */
		
		// 53行目
		propName = "modelArrayA";
		expected = JavaScriptUtilityTestModelA[].class.getSimpleName();
		actualString = (String) jsModelB.get("__javaClassName_" + propName + "__", jsModelB);
		assertEquals(expected, actualString);

		// 54行目
		modelArrayA = jsModelB.get("modelArrayA", jsModelB);
		assertNotNull(modelArrayA);
		assertEquals(NativeArray.class, modelArrayA.getClass());
		jsModelArrayA = (NativeArray)modelArrayA;
		assertEquals(0, jsModelArrayA.getLength());

		// 58行目
		propName = "modelArrayB";
		expected = JavaScriptUtilityTestModelB[].class.getSimpleName();
		actualString = (String) jsModelB.get("__javaClassName_" + propName + "__", jsModelB);
		assertEquals(expected, actualString);

		// 59行目
		modelArrayB = jsModelB.get("modelArrayB", jsModelB);
		assertNotNull(modelArrayB);
		assertEquals(NativeArray.class, modelArrayB.getClass());
		jsModelArrayB = (NativeArray)modelArrayB;
		assertEquals(0, jsModelArrayB.getLength());

		// 63行目
		Object valueWrapperBooleanModelB = jsModelB.get("valueWrapperBooleanModelB", jsModelB);
		assertNotNull(valueWrapperBooleanModelB);
		assertEquals(true, valueWrapperBooleanModelB);
		
		// 66行目
		propName = "modelArrayC";
		expected = JavaScriptUtilityTestModelC[].class.getSimpleName();
		actualString = (String) jsModelB.get("__javaClassName_" + propName + "__", jsModelB);
		assertEquals(expected, actualString);

		// 67行目
		modelArrayC = jsModelB.get("modelArrayC", jsModelB);
		assertNotNull(modelArrayC);
		assertEquals(NativeArray.class, modelArrayC.getClass());
		jsModelArrayC = (NativeArray)modelArrayC;
		assertEquals(0, jsModelArrayC.getLength());
		
		// 72行目
		Object valueWrapperStringModelB = jsModelB.get("valueWrapperStringModelB", jsModelB);
		assertNotNull(valueWrapperStringModelB);
		assertEquals("prop_valueWrapperStringModelB", valueWrapperStringModelB);

		// 75行目
		Object valueWrapperDateModelB = jsModelB.get("valueWrapperDateModelB", jsModelB);
		assertNotNull(valueWrapperDateModelB);
		date = (Date)Context.jsToJava(valueWrapperDateModelB, Date.class);
		assertEquals(new Date(1213846496000L), date); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST) */

		// 78行目
		Object valueWrapperNumberModelB = jsModelB.get("valueWrapperNumberModelB", jsModelB);
		assertNotNull(valueWrapperNumberModelB);
		assertEquals(Double.class, valueWrapperNumberModelB.getClass());
		assertEquals(new Double(123), valueWrapperNumberModelB);
		
		// 82行目
		Object valueWrapperBooleanModelA = jsObj.get("valueWrapperBooleanModelA", jsObj);
		assertNotNull(valueWrapperBooleanModelA);
		assertEquals(true, valueWrapperBooleanModelA);
		
		// 84行目
		propName = "modelArrayA";
		expected = JavaScriptUtilityTestModelA[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 85行目
		modelArrayA = jsObj.get("modelArrayA", jsObj);
		assertNotNull(modelArrayA);
		assertEquals(NativeArray.class, modelArrayA.getClass());
		jsModelArrayA = (NativeArray)modelArrayA;
		assertEquals(0, jsModelArrayA.getLength());

	
		// 89行目
		propName = "modelArrayB";
		expected = JavaScriptUtilityTestModelB[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 90行目
		modelArrayB = jsObj.get("modelArrayB", jsObj);
		assertNotNull(modelArrayB);
		assertEquals(NativeArray.class, modelArrayB.getClass());
		jsModelArrayB = (NativeArray)modelArrayB;
		assertEquals(0, jsModelArrayB.getLength());

		// 95行目
		Object valueWrapperStringModelA = jsObj.get("valueWrapperStringModelA", jsObj);
		assertNotNull(valueWrapperStringModelA);
		assertEquals("prop_valueWrapperStringModelA", valueWrapperStringModelA);

		// 98行目
		Object valueWrapperDateModelA = jsObj.get("valueWrapperDateModelA", jsObj);
		assertNotNull(valueWrapperDateModelA);
		Date dateValueWrapperDateModelA = (Date)Context.jsToJava(valueWrapperDateModelA, Date.class);
		assertEquals(new Date(1213846496000L), dateValueWrapperDateModelA); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST) */

		// 101行目
		Object valueWrapperNumberModelA = jsObj.get("valueWrapperNumberModelA", jsObj);
		assertNotNull(valueWrapperNumberModelA);
		assertEquals(Double.class, valueWrapperNumberModelA.getClass());
		assertEquals(new Double(123), valueWrapperNumberModelA);
	}

	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_char() throws Exception {
		String type = char.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean("１２３４５", type);
		
		assertEquals(Character.class, actual.getClass());
		assertEquals(new Character('１'), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_char_オブジェクトがNumber() throws Exception {
		String type = char.class.getName();

		Object actual = JavaScriptUtility.jsToJavaBean(new Double(54321), type);
		assertEquals(Character.class, actual.getClass());
		assertEquals(new Character('5'), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_char_オブジェクトがBoolean() throws Exception {
		String type = char.class.getName();

		Object actual = JavaScriptUtility.jsToJavaBean(true, type);
		assertEquals(Character.class, actual.getClass());
		assertEquals(new Character('t'), actual);
	}
	
	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_char_オブジェクトがnull() throws Exception {
		String type = char.class.getName();

		Object actual = JavaScriptUtility.jsToJavaBean(null, type);
		assertNull(actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_double() throws Exception {
		String type = double.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean(12345, type);
		
		assertEquals(Double.class, actual.getClass());
		assertEquals(new Double(12345), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_異常系_プリミティブ型_double() throws Exception {
		String type = double.class.getName();

		try{
			JavaScriptUtility.jsToJavaBean("文字列です", type);
		}
		catch(NumberFormatException e){
			assertTrue("NumberFormatExceptionが起こること", true);
			return;
		}

		fail("NumberFormatExceptionが起こっていません");
	}

	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_float() throws Exception {
		String type = float.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean(12345, type);
		
		assertEquals(Float.class, actual.getClass());
		assertEquals(new Float(12345), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_異常系_プリミティブ型_float() throws Exception {
		String type = float.class.getName();

		try{
			JavaScriptUtility.jsToJavaBean("文字列です", type);
		}
		catch(NumberFormatException e){
			assertTrue("NumberFormatExceptionが起こること", true);
			return;
		}

		fail("NumberFormatExceptionが起こっていません");
	}
	
	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_long() throws Exception {
		String type = long.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean(12345, type);
		
		assertEquals(Long.class, actual.getClass());
		assertEquals(new Long(12345), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_異常系_プリミティブ型_long() throws Exception {
		String type = long.class.getName();

		try{
			JavaScriptUtility.jsToJavaBean("文字列です", type);
		}
		catch(NumberFormatException e){
			assertTrue("NumberFormatExceptionが起こること", true);
			return;
		}

		fail("NumberFormatExceptionが起こっていません");
	}

	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_int() throws Exception {
		String type = int.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean(12345, type);
		
		assertEquals(Integer.class, actual.getClass());
		assertEquals(new Integer(12345), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_異常系_プリミティブ型_int() throws Exception {
		String type = int.class.getName();

		try{
			JavaScriptUtility.jsToJavaBean("文字列です", type);
		}
		catch(NumberFormatException e){
			assertTrue("NumberFormatExceptionが起こること", true);
			return;
		}

		fail("NumberFormatExceptionが起こっていません");
	}
	
	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_short() throws Exception {
		String type = short.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean(12345, type);
		
		assertEquals(Short.class, actual.getClass());
		assertEquals(new Short("12345"), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_異常系_プリミティブ型_short() throws Exception {
		String type = short.class.getName();

		try{
			JavaScriptUtility.jsToJavaBean("文字列です", type);
		}
		catch(NumberFormatException e){
			assertTrue("NumberFormatExceptionが起こること", true);
			return;
		}

		fail("NumberFormatExceptionが起こっていません");
	}
	
	public void testConvertJsObject2JavaBeansObjectString_正常系_プリミティブ型_byte() throws Exception {
		String type = byte.class.getName();
		Object actual = JavaScriptUtility.jsToJavaBean(123, type);
		
		assertEquals(Byte.class, actual.getClass());
		assertEquals(new Byte("123"), actual);
	}

	public void testConvertJsObject2JavaBeansObjectString_異常系_プリミティブ型_byte() throws Exception {
		String type = byte.class.getName();

		try{
			JavaScriptUtility.jsToJavaBean("文字列です", type);
		}
		catch(NumberFormatException e){
			assertTrue("NumberFormatExceptionが起こること", true);
			return;
		}

		fail("NumberFormatExceptionが起こっていません");
	}
	
	
	public void testConvertJsObject2JavaBeansObjectStringClassLoader() throws Exception {
		assertTrue("JavaScriptUtility.convertJsObject2JavaBeans(Object, String)系のテストが通っていればOK", true);
	}

	public void testConvertJsObject2JavaBeansObjectClassOfQ_異常系_型変換_StringをNumberで指定() throws Exception {
		
		String jsSoucePath = "/org/intra_mart/jssp/util/JavaScriptUtilityTestModelA.js";
		
		InputStream is = this.getClass().getResourceAsStream(jsSoucePath);
		assert(is != null);
		Reader in = new InputStreamReader(is, jsSourceCharset);
		
		Object sampelData;
		Context cx = Context.enter();
		try{
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);
			
			ScriptScope scope = new ScriptScope(jsSoucePath);
			Script script = cx.compileReader(in, jsSoucePath, 1, null);
			script.exec(cx, scope);
			
			sampelData = scope.call(cx, "getTestData4String", new Object[0]);

		}
		finally{
			Context.exit();
		}

		assert(sampelData != null);

		// 変換
		try{
			JavaScriptUtility.jsToJavaBean(sampelData, JavaScriptUtilityTestModelA.class);
		}
		catch (IllegalConversionException e) {
			assertTrue("IllegalConversionException", true);
			return;
		}

		fail("IllegalConversionExceptionが起こっていません。");

	}

	public void testConvertJsObject2JavaBeansObjectClassOfQ_異常系_型変換_NumberをStringで指定() throws Exception {
		
		String jsSoucePath = "/org/intra_mart/jssp/util/JavaScriptUtilityTestModelA.js";
		
		InputStream is = this.getClass().getResourceAsStream(jsSoucePath);
		assert(is != null);
		Reader in = new InputStreamReader(is, jsSourceCharset);
		
		Object sampelData;
		Context cx = Context.enter();
		try{
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);
			
			ScriptScope scope = new ScriptScope(jsSoucePath);
			Script script = cx.compileReader(in, jsSoucePath, 1, null);
			script.exec(cx, scope);
			
			sampelData = scope.call(cx, "getTestData4Number", new Object[0]);

		}
		finally{
			Context.exit();
		}

		assert(sampelData != null);

		// 変換
		try{
			JavaScriptUtility.jsToJavaBean(sampelData, JavaScriptUtilityTestModelA.class);
		}
		catch (IllegalConversionException e) {
			assertTrue("IllegalConversionException", true);
			return;
		}

		fail("IllegalConversionExceptionが起こっていません。");

	}	
	
	public void testConvertJsObject2JavaBeansObjectClassOfQ_異常系_型変換_DateをStringで指定() throws Exception {
		
		String jsSoucePath = "/org/intra_mart/jssp/util/JavaScriptUtilityTestModelA.js";
		
		InputStream is = this.getClass().getResourceAsStream(jsSoucePath);
		assert(is != null);
		Reader in = new InputStreamReader(is, jsSourceCharset);
		
		Object sampelData;
		Context cx = Context.enter();
		try{
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);
			
			ScriptScope scope = new ScriptScope(jsSoucePath);
			Script script = cx.compileReader(in, jsSoucePath, 1, null);
			script.exec(cx, scope);
			
			sampelData = scope.call(cx, "getTestData4Date", new Object[0]);

		}
		finally{
			Context.exit();
		}

		assert(sampelData != null);

		// 変換
		try{
			JavaScriptUtility.jsToJavaBean(sampelData, JavaScriptUtilityTestModelA.class);
		}
		catch (IllegalConversionException e) {
			System.out.println(e.getMessage());
			assertTrue("IllegalConversionException", true);
			return;
		}

		fail("IllegalConversionExceptionが起こっていません。");

	}	
		
	public void testConvertJsObject2JavaBeansObjectClassOfQ_正常系_型変換_BooleanをStringで指定_False判定() throws Exception {
		
		String jsSoucePath = "/org/intra_mart/jssp/util/JavaScriptUtilityTestModelA.js";
		
		InputStream is = this.getClass().getResourceAsStream(jsSoucePath);
		assert(is != null);
		Reader in = new InputStreamReader(is, jsSourceCharset);
		
		Object sampelData;
		Context cx = Context.enter();
		try{
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);
			
			ScriptScope scope = new ScriptScope(jsSoucePath);
			Script script = cx.compileReader(in, jsSoucePath, 1, null);
			script.exec(cx, scope);
			
			sampelData = scope.call(cx, "getTestData4Boolean", new Object[0]);

		}
		finally{
			Context.exit();
		}

		assert(sampelData != null);

		// 変換
		Object actual = null; 
		try{
			actual = JavaScriptUtility.jsToJavaBean(sampelData, JavaScriptUtilityTestModelA.class);
		}
		catch (IllegalConversionException e) {
			fail("IllegalConversionExceptionが発生してはいけません。");
		}

		assertNotNull(actual);
		assertEquals(JavaScriptUtilityTestModelA.class, actual.getClass());

		JavaScriptUtilityTestModelA actualModel = (JavaScriptUtilityTestModelA) actual;
		assertEquals(Boolean.FALSE, actualModel.getValueWrapperBooleanModelA());

		// 以下は未設定時のの初期値
		assertEquals("モデルA", actualModel.getValueWrapperStringModelA());
		assertEquals(new Double(394.859), actualModel.getValueWrapperNumberModelA());
		assertEquals(new Date(1209612896000L), actualModel.getValueWrapperDateModelA());// Mon May 1 2008 12:34:56 GMT+0900 (JST)
	}
	

	public void testConvertJsObject2JavaBeansObjectClassOfQ_正常系_型変換_BooleanをStringで指定_True判定() throws Exception {
		
		String jsSoucePath = "/org/intra_mart/jssp/util/JavaScriptUtilityTestModelA.js";
		
		InputStream is = this.getClass().getResourceAsStream(jsSoucePath);
		assert(is != null);
		Reader in = new InputStreamReader(is, jsSourceCharset);
		
		Object sampelData;
		Context cx = Context.enter();
		try{
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);
			
			ScriptScope scope = new ScriptScope(jsSoucePath);
			Script script = cx.compileReader(in, jsSoucePath, 1, null);
			script.exec(cx, scope);
			
			sampelData = scope.call(cx, "getTestData4BooleanSuccess", new Object[0]);

		}
		finally{
			Context.exit();
		}

		assert(sampelData != null);

		// 変換
		Object actual = null; 
		try{
			actual = JavaScriptUtility.jsToJavaBean(sampelData, JavaScriptUtilityTestModelA.class);
		}
		catch (IllegalConversionException e) {
			fail("IllegalConversionExceptionが発生してはいけません。");
		}

		assertNotNull(actual);
		assertEquals(JavaScriptUtilityTestModelA.class, actual.getClass());

		JavaScriptUtilityTestModelA actualModel = (JavaScriptUtilityTestModelA) actual;
		assertEquals(Boolean.TRUE, actualModel.getValueWrapperBooleanModelA());

		// 以下は未設定時のの初期値
		assertEquals("モデルA", actualModel.getValueWrapperStringModelA());
		assertEquals(new Double(394.859), actualModel.getValueWrapperNumberModelA());
		assertEquals(new Date(1209612896000L), actualModel.getValueWrapperDateModelA());// Mon May 1 2008 12:34:56 GMT+0900 (JST)
	}
	public void testConvertJsObject2JavaBeansObjectClassOfQ_DateCalendar含むラッパークラスとプリミティブ型が定義されたJavaBeans() throws Exception {
		
		String jsSoucePath = "/org/intra_mart/jssp/util/JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty.js";
		
		InputStream is = this.getClass().getResourceAsStream(jsSoucePath);
		assert(is != null);
		Reader in = new InputStreamReader(is, jsSourceCharset);
		
		Object sampelData;
		Context cx = Context.enter();
		try{
			cx.setGeneratingDebug(true);
			cx.setOptimizationLevel(-1);
			
			ScriptScope scope = new ScriptScope(jsSoucePath);
			Script script = cx.compileReader(in, jsSoucePath, 1, null);
			script.exec(cx, scope);
			
			sampelData = scope.call(cx, "init", new Object[0]);

		}
		finally{
			Context.exit();
		}
		
		assert(sampelData != null);
		
		// 変換
		Object actual = JavaScriptUtility.jsToJavaBean(sampelData, JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty.class);
		
		assertNotNull(actual);
		assertEquals(JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty.class, actual.getClass());
		
		JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty actualModel = (JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty)actual;
		assertEquals('ほ', actualModel.getValuePrimitiveChar());
		assertEquals(new Double(12.345), actualModel.getValuePrimitiveDouble());
		assertEquals((new Double(56.785)).floatValue(), actualModel.getValuePrimitiveFloat());
		assertEquals((new Double(910115)).longValue(), actualModel.getValuePrimitiveLong());
		assertEquals((new Double(1213145)).intValue(), actualModel.getValuePrimitiveInt());
		assertEquals((new Double(455)).shortValue(), actualModel.getValuePrimitiveShort());
		assertEquals((new Double(125)).byteValue(), actualModel.getValuePrimitiveByte());
		assertEquals(true, actualModel.isValuePrimitiveBoolean());
		
		assertNotNull(actualModel.getValuePrimitiveArrayChar());
		assertEquals(3, actualModel.getValuePrimitiveArrayChar().length);
		assertEquals('わ', actualModel.getValuePrimitiveArrayChar()[0]);
		assertEquals('を', actualModel.getValuePrimitiveArrayChar()[1]);
		assertEquals('ん', actualModel.getValuePrimitiveArrayChar()[2]);

		assertNotNull(actualModel.getValuePrimitiveArrayDouble());
		assertEquals(3, actualModel.getValuePrimitiveArrayDouble().length);
		assertEquals(new Double(12.345), actualModel.getValuePrimitiveArrayDouble()[0]);
		assertEquals(new Double(56.785), actualModel.getValuePrimitiveArrayDouble()[1]);
		assertEquals(new Double(910.11125), actualModel.getValuePrimitiveArrayDouble()[2]);
		
		assertNotNull(actualModel.getValuePrimitiveArrayFloat());
		assertEquals(3, actualModel.getValuePrimitiveArrayFloat().length);
		assertEquals((new Double(56.785)).floatValue(), actualModel.getValuePrimitiveArrayFloat()[0]);
		assertEquals((new Double(910.11125)).floatValue(), actualModel.getValuePrimitiveArrayFloat()[1]);
		assertEquals((new Double(1314.15165)).floatValue(), actualModel.getValuePrimitiveArrayFloat()[2]);
		
		assertNotNull(actualModel.getValuePrimitiveArrayLong());
		assertEquals(3, actualModel.getValuePrimitiveArrayLong().length);
		assertEquals((new Double(91011)).longValue(), actualModel.getValuePrimitiveArrayLong()[0]);
		assertEquals((new Double(121314)).longValue(), actualModel.getValuePrimitiveArrayLong()[1]);
		assertEquals((new Double(151617)).longValue(), actualModel.getValuePrimitiveArrayLong()[2]);
		
		assertNotNull(actualModel.getValuePrimitiveArrayInt());
		assertEquals(3, actualModel.getValuePrimitiveArrayInt().length);
		assertEquals((new Double(1213145)).intValue(), actualModel.getValuePrimitiveArrayInt()[0]);
		assertEquals((new Double(15165)).intValue(), actualModel.getValuePrimitiveArrayInt()[1]);
		assertEquals((new Double(17185)).intValue(), actualModel.getValuePrimitiveArrayInt()[2]);
		
		assertNotNull(actualModel.getValuePrimitiveArrayShort());
		assertEquals(3, actualModel.getValuePrimitiveArrayShort().length);
		assertEquals((new Double(4565)).shortValue(), actualModel.getValuePrimitiveArrayShort()[0]);
		assertEquals((new Double(7895)).shortValue(), actualModel.getValuePrimitiveArrayShort()[1]);
		assertEquals((new Double(10115)).shortValue(), actualModel.getValuePrimitiveArrayShort()[2]);

		assertNotNull(actualModel.getValuePrimitiveArrayByte());
		assertEquals(3, actualModel.getValuePrimitiveArrayByte().length);
		assertEquals((new Double(53)).byteValue(), actualModel.getValuePrimitiveArrayByte()[0]);
		assertEquals((new Double(54)).byteValue(), actualModel.getValuePrimitiveArrayByte()[1]);
		assertEquals((new Double(55)).byteValue(), actualModel.getValuePrimitiveArrayByte()[2]);

		assertNotNull(actualModel.getValuePrimitiveArrayBoolean());
		assertEquals(3, actualModel.getValuePrimitiveArrayBoolean().length);
		assertEquals(false, actualModel.getValuePrimitiveArrayBoolean()[0]);
		assertEquals(true, actualModel.getValuePrimitiveArrayBoolean()[1]);
		assertEquals(true, actualModel.getValuePrimitiveArrayBoolean()[2]);
		
		assertEquals("藍上尾げ", actualModel.getValueWrapperString());
		assertEquals(new Character('や'), actualModel.getValueWrapperCharacter());
		assertEquals(new Double(1413125), actualModel.getValueWrapperDouble());
		assertEquals(Double.MAX_VALUE, actualModel.getValueWrapperDouble_MAX());
		assertEquals(Double.MIN_VALUE, actualModel.getValueWrapperDouble_MIN());
		assertEquals(Double.NaN, actualModel.getValueWrapperDouble_NaN());
		assertEquals(Double.NEGATIVE_INFINITY, actualModel.getValueWrapperDouble_NEGA_INF());
		assertEquals(Double.POSITIVE_INFINITY, actualModel.getValueWrapperDouble_POSI_INF());

		assertEquals((new Double(11.1095)).floatValue(), actualModel.getValueWrapperFloat());
		assertEquals(new Long((new Double(87655)).longValue()), actualModel.getValueWrapperLong());

		assertEquals("JSでは、明示的に設定していない値。newInstance()時に設定された値です。", new Long(Long.MAX_VALUE), actualModel.getValueWrapperLong_MAX());
		assertEquals("JSでは、明示的に設定していない値。newInstance()時に設定された値です。", new Long(Long.MIN_VALUE), actualModel.getValueWrapperLong_MIN());

		assertEquals(new Integer((new Double(43215)).intValue()), actualModel.getValueWrapperInteger());
		assertEquals(new Short((new Double(345)).shortValue()), actualModel.getValueWrapperShort());
		assertEquals(new Byte((new Double(125)).byteValue()), actualModel.getValueWrapperByte());
		assertEquals(Boolean.FALSE, actualModel.getValueWrapperBoolean());
		
		assertNotNull(actualModel.getValueWrapperArrayString());
		assertEquals(3, actualModel.getValueWrapperArrayString().length);
		assertEquals("藍上尾げ", actualModel.getValueWrapperArrayString()[0]);
		assertEquals("書きくけ子げ", actualModel.getValueWrapperArrayString()[1]);
		assertEquals("さしすせそげ", actualModel.getValueWrapperArrayString()[2]);

		assertNotNull(actualModel.getValueWrapperArrayCharacter());
		assertEquals(3, actualModel.getValueWrapperArrayCharacter().length);
		assertEquals(new Character('や'), actualModel.getValueWrapperArrayCharacter()[0]);
		assertEquals(new Character('ゆ'), actualModel.getValueWrapperArrayCharacter()[1]);
		assertEquals(new Character('よ'), actualModel.getValueWrapperArrayCharacter()[2]);
	
		assertNotNull(actualModel.getValueWrapperArrayDouble());
		assertEquals(6, actualModel.getValueWrapperArrayDouble().length);
		assertEquals(new Double(1413125), actualModel.getValueWrapperArrayDouble()[0]);
		assertEquals(Double.MAX_VALUE, actualModel.getValueWrapperArrayDouble()[1]);
		assertEquals(Double.MIN_VALUE, actualModel.getValueWrapperArrayDouble()[2]);
		assertEquals(Double.NaN, actualModel.getValueWrapperArrayDouble()[3]);
		assertEquals(Double.NEGATIVE_INFINITY, actualModel.getValueWrapperArrayDouble()[4]);
		assertEquals(Double.POSITIVE_INFINITY, actualModel.getValueWrapperArrayDouble()[5]);

		assertNotNull(actualModel.getValueWrapperArrayFloat());
		assertEquals(3, actualModel.getValueWrapperArrayFloat().length);
		assertEquals((new Double(11.1095)).floatValue(), actualModel.getValueWrapperArrayFloat()[0]);
		assertEquals((new Double(12.1095)).floatValue(), actualModel.getValueWrapperArrayFloat()[1]);
		assertEquals((new Double(13.1095)).floatValue(), actualModel.getValueWrapperArrayFloat()[2]);
	
		assertNotNull(actualModel.getValueWrapperArrayLong());
		assertEquals(3, actualModel.getValueWrapperArrayLong().length);
		assertEquals(new Long((new Double(87655)).longValue()), actualModel.getValueWrapperArrayLong()[0]);
		assertEquals(new Long((new Double(88655)).longValue()), actualModel.getValueWrapperArrayLong()[1]);
		assertEquals(new Long((new Double(89655)).longValue()), actualModel.getValueWrapperArrayLong()[2]);

		assertNotNull(actualModel.getValueWrapperArrayInteger());
		assertEquals(3, actualModel.getValueWrapperArrayInteger().length);
		assertEquals(new Integer((new Double(43215)).intValue()), actualModel.getValueWrapperArrayInteger()[0]);
		assertEquals(new Integer((new Double(53215)).intValue()), actualModel.getValueWrapperArrayInteger()[1]);
		assertEquals(new Integer((new Double(63215)).intValue()), actualModel.getValueWrapperArrayInteger()[2]);
	
		assertNotNull(actualModel.getValueWrapperArrayShort());
		assertEquals(3, actualModel.getValueWrapperArrayShort().length);
		assertEquals(new Short((new Double(345)).shortValue()), actualModel.getValueWrapperArrayShort()[0]);
		assertEquals(new Short((new Double(355)).shortValue()), actualModel.getValueWrapperArrayShort()[1]);
		assertEquals(new Short((new Double(365)).shortValue()), actualModel.getValueWrapperArrayShort()[2]);
	
		assertNotNull(actualModel.getValueWrapperArrayByte());
		assertEquals(3, actualModel.getValueWrapperArrayByte().length);
		assertEquals(new Byte((new Double(15)).byteValue()), actualModel.getValueWrapperArrayByte()[0]);
		assertEquals(new Byte((new Double(25)).byteValue()), actualModel.getValueWrapperArrayByte()[1]);
		assertEquals(new Byte((new Double(35)).byteValue()), actualModel.getValueWrapperArrayByte()[2]);

		assertNotNull(actualModel.getValueWrapperArrayBoolean());
		assertEquals(3, actualModel.getValueWrapperArrayBoolean().length);
		assertEquals(Boolean.TRUE, actualModel.getValueWrapperArrayBoolean()[0]);
		assertEquals(Boolean.TRUE, actualModel.getValueWrapperArrayBoolean()[1]);
		assertEquals(Boolean.FALSE, actualModel.getValueWrapperArrayBoolean()[2]);

		assertEquals(new Double(9999.9995), actualModel.getValueWrapperNumber());

		assertNotNull(actualModel.getValueWrapperArrayNumber());
		assertEquals(3, actualModel.getValueWrapperArrayNumber().length);
		assertEquals(new Double(9999.9995), actualModel.getValueWrapperArrayNumber()[0]);
		assertEquals(new Double(8888.8885), actualModel.getValueWrapperArrayNumber()[1]);
		assertEquals(new Double(7777.7775), actualModel.getValueWrapperArrayNumber()[2]);

		// Tue Apr 15 2008 23:45:16 GMT+0900 (JST)
		assertEquals(new Date(1208270716000L), actualModel.getValueWrapperDate());
		
		assertNotNull(actualModel.getValueWrapperArrayDate());
		assertEquals(3, actualModel.getValueWrapperArrayDate().length);
		// Tue Apr 15 2008 23:45:16 GMT+0900 (JST)
		assertEquals(new Date(1208270716000L), actualModel.getValueWrapperArrayDate()[0]);
		// Wed Apr 15 2009 23:45:16 GMT+0900 (JST)
		assertEquals(new Date(1239806716000L), actualModel.getValueWrapperArrayDate()[1]);
		// Thu Apr 15 2010 23:45:16 GMT+0900 (JST)
		assertEquals(new Date(1271342716000L), actualModel.getValueWrapperArrayDate()[2]);
		

		// Tue Nov 25 2008 23:45:16 GMT+0900 (JST)
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(1227624316000L));
		assertEquals(cal, actualModel.getValueWrapperCalendar());

		assertNotNull(actualModel.getValueWrapperArrayCalendar());
		assertEquals(3, actualModel.getValueWrapperArrayCalendar().length);
		
		Calendar cal0 = Calendar.getInstance();
		cal0.setTime(new Date(1227624316000L)); // Tue Nov 25 2008 23:45:16 GMT+0900 (JST)
		assertEquals(cal0, actualModel.getValueWrapperArrayCalendar()[0]);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date(1259160316000L)); // Wed Nov 25 2009 23:45:16 GMT+0900 (JST)
		assertEquals(cal1, actualModel.getValueWrapperArrayCalendar()[1]);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date(1290696316000L)); // Thu Nov 25 2010 23:45:16 GMT+0900 (JST)
		assertEquals(cal2, actualModel.getValueWrapperArrayCalendar()[2]);

	}
	
	
	public void testConvertJsObject2JavaBeansObjectClassOfQ_3つのクラスによる包含関係() throws Exception {
		// 可逆性あり！
		JavaScriptUtilityTestModelA expected = (JavaScriptUtilityTestModelA)JavaScriptUtility.newInstanceFilledSampleData(JavaScriptUtilityTestModelA.class, null);
		Object jsObject = JavaScriptUtility.javaBeanToJS(expected);
		
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, JavaScriptUtilityTestModelA.class);
		assertEquals(JavaScriptUtilityTestModelA.class, actual.getClass());
		
		JavaScriptUtilityTestModelA actualModelA = (JavaScriptUtilityTestModelA) actual;

		assertEquals(null, actualModelA.getModelA());
		assertEquals(          expected.getModelA(),
		                   actualModelA.getModelA());

		assertEquals(null, actualModelA.getModelB().getModelA());
		assertEquals(          expected.getModelB().getModelA(),
		                   actualModelA.getModelB().getModelA());
		
		assertEquals(null, actualModelA.getModelB().getModelB());
		assertEquals(          expected.getModelB().getModelA(), 
				           actualModelA.getModelB().getModelB());
		
		assertEquals(null, actualModelA.getModelB().getModelC().getModelA());
		assertEquals(          expected.getModelB().getModelC().getModelA(),
				           actualModelA.getModelB().getModelC().getModelA());
		
		assertEquals(new Double(123), actualModelA.getModelB().getModelC().getValueWrapperNumberModelC());
		assertEquals(                     expected.getModelB().getModelC().getValueWrapperNumberModelC(),
				                      actualModelA.getModelB().getModelC().getValueWrapperNumberModelC());

		assertEquals(null, actualModelA.getModelB().getModelC().getModelB());
		assertEquals(          expected.getModelB().getModelC().getModelB(),
				           actualModelA.getModelB().getModelC().getModelB());
		
		assertEquals(null, actualModelA.getModelB().getModelC().getModelC());
		assertEquals(          expected.getModelB().getModelC().getModelC(),
				           actualModelA.getModelB().getModelC().getModelC());
		
		assertEquals(0, actualModelA.getModelB().getModelC().getModelArrayA().length);
		assertEquals(       expected.getModelB().getModelC().getModelArrayA().length,
				        actualModelA.getModelB().getModelC().getModelArrayA().length);
		
		assertEquals(0, actualModelA.getModelB().getModelC().getModelArrayB().length);
		assertEquals(       expected.getModelB().getModelC().getModelArrayB().length,
		                actualModelA.getModelB().getModelC().getModelArrayB().length);

		assertEquals(Boolean.TRUE, actualModelA.getModelB().getModelC().getValueWrapperBooleanModelC());
		assertEquals(                  expected.getModelB().getModelC().getValueWrapperBooleanModelC(),
				                   actualModelA.getModelB().getModelC().getValueWrapperBooleanModelC());

		assertEquals(0, actualModelA.getModelB().getModelC().getModelArrayC().length);
		assertEquals(       expected.getModelB().getModelC().getModelArrayC().length,
				        actualModelA.getModelB().getModelC().getModelArrayC().length);
		
		assertEquals("prop_valueWrapperStringModelC", actualModelA.getModelB().getModelC().getValueWrapperStringModelC());
		assertEquals(                                     expected.getModelB().getModelC().getValueWrapperStringModelC(), 
				                                      actualModelA.getModelB().getModelC().getValueWrapperStringModelC());

		assertEquals(new Date(1213846496000L), actualModelA.getModelB().getModelC().getValueWrapperDateModelC());
		assertEquals(                              expected.getModelB().getModelC().getValueWrapperDateModelC(),
				                               actualModelA.getModelB().getModelC().getValueWrapperDateModelC());
		
		assertEquals(0, actualModelA.getModelB().getModelArrayA().length);
		assertEquals(       expected.getModelB().getModelArrayA().length,
				        actualModelA.getModelB().getModelArrayA().length);
		
		assertEquals(0, actualModelA.getModelB().getModelArrayB().length);
		assertEquals(       expected.getModelB().getModelArrayB().length,
				        actualModelA.getModelB().getModelArrayB().length);

		assertEquals(Boolean.TRUE, actualModelA.getModelB().getValueWrapperBooleanModelB());
		assertEquals(                  expected.getModelB().getValueWrapperBooleanModelB(),
				                   actualModelA.getModelB().getValueWrapperBooleanModelB());
	
		assertEquals(0, actualModelA.getModelB().getModelArrayC().length);
		assertEquals(       expected.getModelB().getModelArrayC().length,
				        actualModelA.getModelB().getModelArrayC().length);
	
		assertEquals("prop_valueWrapperStringModelB", actualModelA.getModelB().getValueWrapperStringModelB());
		assertEquals(                                     expected.getModelB().getValueWrapperStringModelB(),
				                                      actualModelA.getModelB().getValueWrapperStringModelB());
	
		assertEquals(new Date(1213846496000L), actualModelA.getModelB().getValueWrapperDateModelB());
		assertEquals(                              expected.getModelB().getValueWrapperDateModelB(),
				                               actualModelA.getModelB().getValueWrapperDateModelB());
	
		assertEquals(new Double(123), actualModelA.getModelB().getValueWrapperNumberModelB());
		assertEquals(                     expected.getModelB().getValueWrapperNumberModelB(),
				                      actualModelA.getModelB().getValueWrapperNumberModelB());

		assertEquals(Boolean.TRUE, actualModelA.getValueWrapperBooleanModelA());
		assertEquals(                  expected.getValueWrapperBooleanModelA(),
				                   actualModelA.getValueWrapperBooleanModelA());

		assertEquals(0, actualModelA.getModelArrayA().length);
		assertEquals(       expected.getModelArrayA().length,
				        actualModelA.getModelArrayA().length);

		assertEquals(0, actualModelA.getModelArrayB().length);
		assertEquals(       expected.getModelArrayB().length,
				        actualModelA.getModelArrayB().length);

		assertEquals("prop_valueWrapperStringModelA", actualModelA.getValueWrapperStringModelA());
		assertEquals(                                     expected.getValueWrapperStringModelA(),
				                                      actualModelA.getValueWrapperStringModelA());

		assertEquals(new Date(1213846496000L), actualModelA.getValueWrapperDateModelA());
		assertEquals(                              expected.getValueWrapperDateModelA(),
				                               actualModelA.getValueWrapperDateModelA());
		
		assertEquals(new Double(123), actualModelA.getValueWrapperNumberModelA());
		assertEquals(                     expected.getValueWrapperNumberModelA(),
				                      actualModelA.getValueWrapperNumberModelA());
		
	}
	
	public void testConvertJsObject2JavaBeansObjectClassOfQ_Undifinedの場合() throws Exception {
		Object jsObject = Undefined.instance;
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, JavaScriptUtilityTestModelA.class);
		assertNull("Undefinedはnullに変換される", actual);
	}

	public void testConvertJsObject2JavaBeansObjectClassOfQ_NativeJavaObject_正常系_Stringの場合() throws Exception {
		Scriptable scope = FoundationScriptScope.instance();
		NativeJavaObject jsObject = new NativeJavaObject(scope, new String("あいうえお"), String.class);
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, String.class);

		assertEquals("あいうえお", actual);
	}
		
	public void testConvertJsObject2JavaBeansObjectClassOfQ_NativeJavaObject_異常系_Stringの場合() throws Exception {
		Scriptable scope = FoundationScriptScope.instance();
		NativeJavaObject jsObject = new NativeJavaObject(scope, new String("あいうえお"), String.class);
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, Number.class);

		assertNull("型が異なるのでnullが返却される", actual);
	}


	public void testConvertJsObject2JavaBeansObjectClassOfQ_NativeJavaArray_正常系_Stringの場合() throws Exception {
		String[] array = {"123", "あいう", "abc"};
		
		Scriptable scope = FoundationScriptScope.instance();
		NativeJavaArray jsObject = new NativeJavaArray(scope, array);
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, array.getClass());

		assertTrue(actual.getClass().isArray());
		assertEquals(String.class, actual.getClass().getComponentType());
		assertEquals("123", Array.get(actual, 0));
		assertEquals("あいう", Array.get(actual, 1));
		assertEquals("abc", Array.get(actual, 2));
	}
		
	public void testConvertJsObject2JavaBeansObjectClassOfQ_NativeJavaArray_異常系_Stringの場合() throws Exception {
		String[] array = {"123", "あいう", "abc"};
		
		Scriptable scope = FoundationScriptScope.instance();
		NativeJavaArray jsObject = new NativeJavaArray(scope, array);
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, String.class); // 配列ではない！

		assertNull("型が異なるのでnullが返却される", actual);
	}


	public void testConvertJsObject2JavaBeansObjectClassOfQ_NativeJavaArray_JSのプロパティ名が先頭大文字で指定された場合() throws Exception {
		Map<String, Object> propertiesSmallLetter = new HashMap<String, Object>();
		propertiesSmallLetter.put("valueWrapperStringModelA", "文字列です");
		propertiesSmallLetter.put("valueWrapperNumberModelA", new Double(123.456));
		propertiesSmallLetter.put("valueWrapperBooleanModelA", Boolean.FALSE);
		ValueObject jsObjectSmallLetter = new ValueObject(propertiesSmallLetter);
		JavaScriptUtilityTestModelA expected = (JavaScriptUtilityTestModelA) JavaScriptUtility.jsToJavaBean(jsObjectSmallLetter, JavaScriptUtilityTestModelA.class);

		Map<String, Object> propertiesLargeLetter = new HashMap<String, Object>();
		propertiesLargeLetter.put("ValueWrapperStringModelA", "文字列です");
		propertiesLargeLetter.put("ValueWrapperNumberModelA", new Double(123.456));
		propertiesLargeLetter.put("ValueWrapperBooleanModelA", Boolean.FALSE);
		ValueObject jsObjectLargeLetter = new ValueObject(propertiesLargeLetter);
		JavaScriptUtilityTestModelA actual = (JavaScriptUtilityTestModelA) JavaScriptUtility.jsToJavaBean(jsObjectLargeLetter, JavaScriptUtilityTestModelA.class);
		
		assertEquals(expected.getValueWrapperStringModelA(), actual.getValueWrapperStringModelA());
		assertEquals(expected.getValueWrapperNumberModelA(), actual.getValueWrapperNumberModelA());
		assertEquals(expected.getValueWrapperBooleanModelA(), actual.getValueWrapperBooleanModelA());
	}

		
	public void testGetBeansPropertyMap_対象外_List() throws Exception {
		Object actual;
		Class targetType = List.class;

		try {
			actual = JavaScriptUtility.getBeanPropertyMap(targetType);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}
		
		assertNull(actual);
	}
	
	public void testGetBeansPropertyMap_対象外_Map() throws Exception {
		Object actual;
		Class targetType = Map.class;
		
		try {
			actual = JavaScriptUtility.getBeanPropertyMap(targetType);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}

		assertNull(actual);
	}

	public void testGetBeansPropertyMap_対象外_Set() throws Exception {
		Object actual;
		Class targetType = Set.class;

		try {
			actual = JavaScriptUtility.getBeanPropertyMap(targetType);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}

		assertNull(actual);
	}

	public void testGetBeansPropertyMap_対象外_Object() throws Exception {
		Object actual;
		Class targetType = Object.class;
		
		try {
			actual = JavaScriptUtility.getBeanPropertyMap(targetType);
		}
		catch (IllegalArgumentException e) {
			fail("IllegalArgumentExceptionは発生しません");
			return;
		}

		assertNull(actual);
	}

	public void testGetBeansPropertyMap_対象クラス_JavaScriptUtilityTestModelA() throws Exception {
		Class beanType = JavaScriptUtilityTestModelA.class;
		Map<String, PropertyDescriptor> actual = JavaScriptUtility.getBeanPropertyMap(beanType);

		assertEquals(9, actual.size());
		assertNotNull(actual.get("modelA"));
		assertNotNull(actual.get("modelArrayA"));
		assertNotNull(actual.get("modelArrayB"));
		assertNotNull(actual.get("valueWrapperStringModelA"));
		assertNotNull(actual.get("valueWrapperNumberModelA"));
		assertNotNull(actual.get("valueWrapperBooleanModelA"));
		assertNotNull(actual.get("valueWrapperDateModelA"));
		assertNotNull(actual.get("plainObject"));
	}
	
	
	public void testNewInstanceFilledPropertyStringString_プリミティブ型_char_初期値_null() throws Exception {
		String beanClassName = char.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Character.class, actual.getClass());
		assertEquals('_', actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_char_初期値_空文字() throws Exception {
		String beanClassName = char.class.getName();
		String defaultValue = "";
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Character.class, actual.getClass());
		assertEquals('_', actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_char_初期値_任意() throws Exception {
		String beanClassName = char.class.getName();
		String defaultValue = "任意の文字列";
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Character.class, actual.getClass());
		assertEquals('任', actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_char_initializeあり_初期値_null() throws Exception {
		JavaScriptUtility.initializeSampleData("テストです。", null, null, null, -1);
		
		String beanClassName = char.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Character.class, actual.getClass());
		assertEquals('テ', actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_double() throws Exception {
		String beanClassName = double.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Double.class, actual.getClass());
		assertEquals(new Double(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_double_initializeあり() throws Exception {
		Double expected = new Double(567.89);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);
		
		String beanClassName = double.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Double.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_float() throws Exception {
		String beanClassName = float.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Float.class, actual.getClass());
		assertEquals(new Float(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_float_initializeあり() throws Exception {
		Float expected = new Float(111.222);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = float.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Float.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_long() throws Exception {
		String beanClassName = long.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Long.class, actual.getClass());
		assertEquals(new Long(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_long_initializeあり() throws Exception {
		Long expected = new Long(123456789011L);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = long.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Long.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_int() throws Exception {
		String beanClassName = int.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Integer.class, actual.getClass());
		assertEquals(new Integer(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_int_initializeあり() throws Exception {
		Integer expected = new Integer(8786543);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = int.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Integer.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_short() throws Exception {
		String beanClassName = short.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Short.class, actual.getClass());
		assertEquals(new Short("123"), actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_short_initializeあり() throws Exception {
		Short expected = new Short("345");
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = short.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Short.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_byte() throws Exception {
		String beanClassName = byte.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Byte.class, actual.getClass());
		assertEquals(new Byte("123"), actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_byte_initializeあり() throws Exception {
		Byte expected = new Byte("16");
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = byte.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Byte.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_boolean() throws Exception {
		String beanClassName = boolean.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Boolean.class, actual.getClass());
		assertEquals(Boolean.TRUE, actual);
	}

	public void testNewInstanceFilledPropertyStringString_プリミティブ型_initializeあり() throws Exception {
		Boolean expected = Boolean.FALSE;
		JavaScriptUtility.initializeSampleData(null, null, expected, null, -1);

		String beanClassName = boolean.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Boolean.class, actual.getClass());
		assertEquals(expected, actual);
	}
	
	
	public void testNewInstanceFilledPropertyStringString_ラッパークラス_String() throws Exception {
		String beanClassName = String.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(String.class, actual.getClass());
		assertEquals("__default__", actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_String_initializeあり() throws Exception {
		String expected = new String("テストしています。");
		JavaScriptUtility.initializeSampleData(expected, null, null, null, -1);

		String beanClassName = String.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(String.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Character() throws Exception {
		String beanClassName = Character.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Character.class, actual.getClass());
		assertEquals('_', actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Character_initializeあり() throws Exception {
		String expected = new String("はひふへほ");
		JavaScriptUtility.initializeSampleData(expected, null, null, null, -1);

		String beanClassName = Character.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Character.class, actual.getClass());
		assertEquals(expected.charAt(0), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Double() throws Exception {
		String beanClassName = Double.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Double.class, actual.getClass());
		assertEquals(new Double(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Double_initializeあり() throws Exception {
		Double expected = new Double(123);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = Double.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Double.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Float() throws Exception {
		String beanClassName = Float.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Float.class, actual.getClass());
		assertEquals(new Float(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Float_initializeあり() throws Exception {
		Float expected = new Float(123.456);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = Float.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Float.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Long() throws Exception {
		String beanClassName = Long.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Long.class, actual.getClass());
		assertEquals(new Long(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Long_initializeあり() throws Exception {
		Long expected = new Long(9876543210L);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = Long.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Long.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Integer() throws Exception {
		String beanClassName = Integer.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Integer.class, actual.getClass());
		assertEquals(new Integer(123), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Integer_initializeあり() throws Exception {
		Integer expected = new Integer(7684657);
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = Integer.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Integer.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Short() throws Exception {
		String beanClassName = Short.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Short.class, actual.getClass());
		assertEquals(new Short("123"), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Short_initializeあり() throws Exception {
		Short expected = new Short("456");
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = Short.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Short.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Byte() throws Exception {
		String beanClassName = Byte.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Byte.class, actual.getClass());
		assertEquals(new Byte("123"), actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Byte_initializeあり() throws Exception {
		Byte expected = new Byte("23");
		JavaScriptUtility.initializeSampleData(null, expected, null, null, -1);

		String beanClassName = Byte.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Byte.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Boolean() throws Exception {
		String beanClassName = Boolean.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Boolean.class, actual.getClass());
		assertEquals(Boolean.TRUE, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Boolean_initializeあり() throws Exception {
		Boolean expected = Boolean.FALSE;
		JavaScriptUtility.initializeSampleData(null, null, expected, null, -1);

		String beanClassName = Boolean.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Boolean.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Date() throws Exception {
		String beanClassName = Date.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Date.class, actual.getClass());
		assertEquals(new Date(1213846496000L), actual); // "Mon June 19 2008 12:34:56 GMT+0900 (JST)"
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Date_initializeあり() throws Exception {
		Date expected = new Date();
		JavaScriptUtility.initializeSampleData(null, null, null, expected, -1);

		String beanClassName = Date.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals(Date.class, actual.getClass());
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Calendar() throws Exception {
		Calendar expected = Calendar.getInstance();
		expected.setTime(new Date(1213846496000L)); // "Mon June 19 2008 12:34:56 GMT+0900 (JST)"

		String beanClassName = Calendar.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertTrue(actual instanceof Calendar);
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_ラッパークラス_Calendar_initializeあり() throws Exception {
		Date date = new Date();
		JavaScriptUtility.initializeSampleData(null, null, null, date, -1);

		Calendar expected = Calendar.getInstance();
		expected.setTime(date);

		String beanClassName = Calendar.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertTrue(actual instanceof Calendar);
		assertEquals(expected, actual);
	}

	public void testNewInstanceFilledPropertyStringString_javaLangObject() throws Exception {
		String beanClassName = Object.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals("java.lang.Objectの場合は、サンプルデータの型がStringとなります", String.class, actual.getClass());
		assertEquals("__default__", actual);
	}

	public void testNewInstanceFilledPropertyStringString_javaLangObject_Calendar_initializeあり() throws Exception {
		String expected = new String("テストしています。");
		JavaScriptUtility.initializeSampleData(expected, null, null, null, -1);

		String beanClassName = Object.class.getName();
		String defaultValue = null;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(beanClassName, defaultValue);

		assertEquals("java.lang.Objectの場合は、サンプルデータの型がStringとなります", String.class, actual.getClass());
		assertEquals(expected, actual);
	}
	
	
	public void testNewInstanceFilledPropertyStringClassLoaderString() throws Exception {
		assertTrue("JavaScriptUtility.newInstanceFilledProperty(String, String)系のテストが通っていればOK", true);
	}

	/**
	 * <pre>
	 *  1: A ・・・①
	 *  2: ├─A  → (内部走査しない ∵自身)
	 *  3: │
	 *  4: ├─B ・・・②
	 *  5: │  │
	 *  6: │  ├─A  → (内部走査しない ∵既に①で走査済み)
	 *  7: │  │
	 *  8: │  ├─B  → (内部走査しない ∵自身)
	 *  9: │  │
	 * 10: │  ├─C ・・・③
	 * 11: │  │  │
	 * 12: │  │  ├─A    → (内部走査しない ∵既に①で走査済み)
	 * 13: │  │  │
	 * 14: │  │  ├─B    → (内部走査しない ∵既に②で走査済み)
	 * 15: │  │  │
	 * 16: │  │  ├─C    → (内部走査しない ∵自身)
	 * 17: │  │  │
	 * 18: │  │  ├─Wrap → (内部走査終了可能)
	 * 19: │  │  │
	 * 20: │  │  ├─A[]  → (内部走査しない ∵既に①で走査済み)
	 * 21: │  │  │
	 * 22: │  │  ├─B[]  → (内部走査しない ∵既に②で走査済み)
	 * 23: │  │  │
	 * 24: │  │  └─C[]  → (内部走査しない ∵既に③で走査済み)
	 * 25: │  │
	 * 26: │  ├─Wrap → (内部走査終了可能)
	 * 27: │  │
	 * 28: │  ├─A[] → (内部走査しない ∵既に①で走査済み)
	 * 29: │  │
	 * 30: │  ├─B[] → (内部走査しない ∵既に②で走査済み)
	 * 31: │  │
	 * 32: │  └─C[] → (内部走査しない ∵既に③で走査済み)
	 * 33: │
	 * 34: ├─Wrap → (内部走査終了可能)
	 * 35: │
	 * 36: ├─A[] → (内部走査しない ∵既に①で走査済み)
	 * 37: │
	 * 38: └─B[] → (内部走査しない ∵既に②で走査済み)
	 * </pre>
	 * @throws Exception
	 */
	public void testNewInstanceFilledPropertyClassOfQString_3つのクラスによる包含関係() throws Exception {
		Class expectedClass = JavaScriptUtilityTestModelA.class;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(expectedClass, null);
		
		assertEquals(expectedClass, actual.getClass());
		JavaScriptUtilityTestModelA actualObj = (JavaScriptUtilityTestModelA) actual;

		//  2行目
		assertNull(actualObj.getModelA());

		//  4行目
		assertNotNull(actualObj.getModelB());

		//  6行目
		assertNull(actualObj.getModelB().getModelA());
		
		//  8行目 
		assertNull(actualObj.getModelB().getModelB());

		// 10行目 
		assertNotNull(actualObj.getModelB().getModelC());

		// 12行目
		assertNull(actualObj.getModelB().getModelC().getModelA());
		
		// 14行目
		assertNull(actualObj.getModelB().getModelC().getModelB());

		// 16行目
		assertNull(actualObj.getModelB().getModelC().getModelC());

		// 18行目
		assertEquals("prop_valueWrapperStringModelC", actualObj.getModelB().getModelC().getValueWrapperStringModelC());
		assertEquals(new Double(123),            actualObj.getModelB().getModelC().getValueWrapperNumberModelC());
		assertEquals(Boolean.TRUE,               actualObj.getModelB().getModelC().getValueWrapperBooleanModelC());
		assertEquals(new Date(1213846496000L),   actualObj.getModelB().getModelC().getValueWrapperDateModelC()); // "Mon June 19 2008 12:34:56 GMT+0900 (JST)"

		// 20行目
		assertNotNull(  actualObj.getModelB().getModelC().getModelArrayA());
		assertEquals(0, actualObj.getModelB().getModelC().getModelArrayA().length);
		
		// 22行目
		assertNotNull(  actualObj.getModelB().getModelC().getModelArrayB());
		assertEquals(0, actualObj.getModelB().getModelC().getModelArrayB().length);
		
		// 24行目
		assertNotNull(  actualObj.getModelB().getModelC().getModelArrayC());
		assertEquals(0, actualObj.getModelB().getModelC().getModelArrayC().length);

		// 26行目
		assertEquals("prop_valueWrapperStringModelB", actualObj.getModelB().getValueWrapperStringModelB());
		assertEquals(new Double(123),            actualObj.getModelB().getValueWrapperNumberModelB());
		assertEquals(Boolean.TRUE,               actualObj.getModelB().getValueWrapperBooleanModelB());
		assertEquals(new Date(1213846496000L),   actualObj.getModelB().getValueWrapperDateModelB()); // "Mon June 19 2008 12:34:56 GMT+0900 (JST)"

		// 28行目
		assertNotNull(  actualObj.getModelB().getModelArrayA());
		assertEquals(0, actualObj.getModelB().getModelArrayA().length);

		// 30行目
		assertNotNull(  actualObj.getModelB().getModelArrayB());
		assertEquals(0, actualObj.getModelB().getModelArrayB().length);

		// 32行目
		assertNotNull(  actualObj.getModelB().getModelArrayC());
		assertEquals(0, actualObj.getModelB().getModelArrayC().length);
		
		// 34行目
		assertEquals("prop_valueWrapperStringModelA", actualObj.getValueWrapperStringModelA());
		assertEquals(new Double(123),            	  actualObj.getValueWrapperNumberModelA());
		assertEquals(Boolean.TRUE,               	  actualObj.getValueWrapperBooleanModelA());
		assertEquals(new Date(1213846496000L),   	  actualObj.getValueWrapperDateModelA()); // "Mon June 19 2008 12:34:56 GMT+0900 (JST)"


		// 36行目
		assertNotNull(  actualObj.getModelArrayA());
		assertEquals(0, actualObj.getModelArrayA().length);

		// 38行目
		assertNotNull(  actualObj.getModelArrayB());
		assertEquals(0, actualObj.getModelArrayB().length);

	}
	
	
	
	public void testNewInstanceFilledPropertyClassOfQString_プロパティがプリミティブやラッパークラスのみ() throws Exception {
		Class expectedClass = JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty.class;
		Object actual = JavaScriptUtility.newInstanceFilledSampleData(expectedClass, null);

		assertEquals(expectedClass, actual.getClass());

		JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty actualObj = 
				(JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty) actual;
		
		assertEquals('p', actualObj.getValuePrimitiveChar());
		assertEquals((new Double(123)).doubleValue(), actualObj.getValuePrimitiveDouble());
		assertEquals((new Float(123)).floatValue(), actualObj.getValuePrimitiveFloat());
		assertEquals((new Long(123)).longValue(), actualObj.getValuePrimitiveLong());
		assertEquals((new Integer(123)).intValue(), actualObj.getValuePrimitiveInt());
		assertEquals((new Short("123")).shortValue(), actualObj.getValuePrimitiveShort());
		assertEquals((new Byte("123")).byteValue(), actualObj.getValuePrimitiveByte());
		assertEquals(Boolean.TRUE.booleanValue(), actualObj.isValuePrimitiveBoolean());
		
		assertEquals(1, actualObj.getValuePrimitiveArrayChar().length);
		assertEquals('p', actualObj.getValuePrimitiveArrayChar()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayDouble().length);
		assertEquals((new Double(123)).doubleValue(), actualObj.getValuePrimitiveArrayDouble()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayFloat().length);
		assertEquals((new Float(123)).floatValue(), actualObj.getValuePrimitiveArrayFloat()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayLong().length);
		assertEquals((new Long(123)).longValue(), actualObj.getValuePrimitiveArrayLong()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayInt().length);
		assertEquals((new Integer(123)).intValue(), actualObj.getValuePrimitiveArrayInt()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayShort().length);
		assertEquals((new Short("123")).shortValue(), actualObj.getValuePrimitiveArrayShort()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayByte().length);
		assertEquals((new Byte("123")).byteValue(), actualObj.getValuePrimitiveArrayByte()[0]);
		assertEquals(1, actualObj.getValuePrimitiveArrayBoolean().length);
		assertEquals(Boolean.TRUE.booleanValue(), actualObj.getValuePrimitiveArrayBoolean()[0]);

		assertEquals("prop_valueWrapperString", actualObj.getValueWrapperString());
		assertEquals(new Character('p'), actualObj.getValueWrapperCharacter());
		assertEquals(new Double(123), actualObj.getValueWrapperDouble());
		assertEquals(new Double(123), actualObj.getValueWrapperDouble_MAX());
		assertEquals(new Double(123), actualObj.getValueWrapperDouble_MIN());
		assertEquals(new Double(123), actualObj.getValueWrapperDouble_NaN());
		assertEquals(new Double(123), actualObj.getValueWrapperDouble_NEGA_INF());
		assertEquals(new Double(123), actualObj.getValueWrapperDouble_POSI_INF());
		assertEquals(new Float(123), actualObj.getValueWrapperFloat());
		assertEquals(new Long(123), actualObj.getValueWrapperLong());
		assertEquals(new Long(123), actualObj.getValueWrapperLong_MAX());
		assertEquals(new Long(123), actualObj.getValueWrapperLong_MIN());
		assertEquals(new Integer(123), actualObj.getValueWrapperInteger());
		assertEquals(new Short("123"), actualObj.getValueWrapperShort());
		assertEquals(new Byte("123"), actualObj.getValueWrapperByte());
		assertEquals(Boolean.TRUE, actualObj.getValueWrapperBoolean());
		
		
		assertEquals(1, actualObj.getValueWrapperArrayString().length);
		assertEquals("prop_valueWrapperArrayString", actualObj.getValueWrapperArrayString()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayCharacter().length);
		assertEquals(new Character('p'), actualObj.getValueWrapperArrayCharacter()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayDouble().length);
		assertEquals(new Double(123), actualObj.getValueWrapperArrayDouble()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayFloat().length);
		assertEquals(new Float(123), actualObj.getValueWrapperArrayFloat()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayLong().length);
		assertEquals(new Long(123), actualObj.getValueWrapperArrayLong()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayInteger().length);
		assertEquals(new Integer(123), actualObj.getValueWrapperArrayInteger()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayShort().length);
		assertEquals(new Short("123"), actualObj.getValueWrapperArrayShort()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayByte().length);
		assertEquals(new Byte("123"), actualObj.getValueWrapperArrayByte()[0]);
		assertEquals(1, actualObj.getValueWrapperArrayBoolean().length);
		assertEquals(Boolean.TRUE, actualObj.getValueWrapperArrayBoolean()[0]);
	}

	public void testGetPublicFieldMap_Publicフィールド() throws Exception {
		
		Map<String, Field> pubFieldsMap = JavaScriptUtility.getPublicFieldMap(JavaScriptUtilityTestModel4PublicFields.class);
		
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveChar"), pubFieldsMap.get("valuePrimitiveChar"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveDouble"), pubFieldsMap.get("valuePrimitiveDouble"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveFloat"), pubFieldsMap.get("valuePrimitiveFloat"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveLong"), pubFieldsMap.get("valuePrimitiveLong"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveInt"), pubFieldsMap.get("valuePrimitiveInt"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveShort"), pubFieldsMap.get("valuePrimitiveShort"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveByte"), pubFieldsMap.get("valuePrimitiveByte"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveBoolean"), pubFieldsMap.get("valuePrimitiveBoolean"));

		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayChar"), pubFieldsMap.get("valuePrimitiveArrayChar"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayDouble"), pubFieldsMap.get("valuePrimitiveArrayDouble"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayFloat"), pubFieldsMap.get("valuePrimitiveArrayFloat"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayLong"), pubFieldsMap.get("valuePrimitiveArrayLong"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayInt"), pubFieldsMap.get("valuePrimitiveArrayInt"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayShort"), pubFieldsMap.get("valuePrimitiveArrayShort"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayByte"), pubFieldsMap.get("valuePrimitiveArrayByte"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valuePrimitiveArrayBoolean"), pubFieldsMap.get("valuePrimitiveArrayBoolean"));

		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperString"), pubFieldsMap.get("valueWrapperString"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperCharacter"), pubFieldsMap.get("valueWrapperCharacter"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDouble"), pubFieldsMap.get("valueWrapperDouble"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDouble_MAX"), pubFieldsMap.get("valueWrapperDouble_MAX"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDouble_MIN"), pubFieldsMap.get("valueWrapperDouble_MIN"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDouble_NaN"), pubFieldsMap.get("valueWrapperDouble_NaN"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDouble_NEGA_INF"), pubFieldsMap.get("valueWrapperDouble_NEGA_INF"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDouble_POSI_INF"), pubFieldsMap.get("valueWrapperDouble_POSI_INF"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperFloat"), pubFieldsMap.get("valueWrapperFloat"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperLong"), pubFieldsMap.get("valueWrapperLong"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperLong_MAX"), pubFieldsMap.get("valueWrapperLong_MAX"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperLong_MIN"), pubFieldsMap.get("valueWrapperLong_MIN"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperInteger"), pubFieldsMap.get("valueWrapperInteger"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperShort"), pubFieldsMap.get("valueWrapperShort"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperByte"), pubFieldsMap.get("valueWrapperByte"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperBoolean"), pubFieldsMap.get("valueWrapperBoolean"));

		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayString"), pubFieldsMap.get("valueWrapperArrayString"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayCharacter"), pubFieldsMap.get("valueWrapperArrayCharacter"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayDouble"), pubFieldsMap.get("valueWrapperArrayDouble"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayFloat"), pubFieldsMap.get("valueWrapperArrayFloat"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayLong"), pubFieldsMap.get("valueWrapperArrayLong"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayInteger"), pubFieldsMap.get("valueWrapperArrayInteger"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayShort"), pubFieldsMap.get("valueWrapperArrayShort"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayByte"), pubFieldsMap.get("valueWrapperArrayByte"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayBoolean"), pubFieldsMap.get("valueWrapperArrayBoolean"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperNumber"), pubFieldsMap.get("valueWrapperNumber"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayNumber"), pubFieldsMap.get("valueWrapperArrayNumber"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperDate"), pubFieldsMap.get("valueWrapperDate"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayDate"), pubFieldsMap.get("valueWrapperArrayDate"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperCalendar"), pubFieldsMap.get("valueWrapperCalendar"));
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class.getField("valueWrapperArrayCalendar"), pubFieldsMap.get("valueWrapperArrayCalendar"));
	}

	public void testGetPublicFieldMap_Publicフィールド_ModelA() throws Exception {
		Map<String, Field> pubFieldsMap = JavaScriptUtility.getPublicFieldMap(JavaScriptUtilityTestModel4PublicFieldsHasModelA.class);
		assertEquals(JavaScriptUtilityTestModel4PublicFieldsHasModelA.class.getField("modelA"), pubFieldsMap.get("modelA"));
	}
	
	public void testGetPublicFieldMap_Publicフィールド_フィールドなし() throws Exception {
		Map<String, Field> pubFieldsMap = JavaScriptUtility.getPublicFieldMap(JavaScriptUtilityTestModelA.class);
		assertNull(pubFieldsMap);
	}

	/**
	 * <pre>
	 * function createTestData(){
	 *     load('jssp/script/api/soap_client_helper');
	 * 
	 *     var beanType = "org.intra_mart.jssp.util.JavaScriptUtilityTestModel4PublicFields";
	 *     var bean = Packages.org.intra_mart.jssp.util.JavaScriptUtility.newInstanceFilledSampleData(beanType, "hoge");
	 *     var obj = Packages.org.intra_mart.jssp.util.JavaScriptUtility.javaBeanToJS(bean);
	 *         obj = normalize(obj);	
	 *     Debug.console(obj);
	 * }
	 * </pre>
	 *
	 * <pre>
	 *   0: ========== 1 ==========
	 *   1: /&#42; Object &lt;JavaScriptUtilityTestModel4PublicFields&gt; &#42;/
	 *   2: {
	 *   3:     /&#42; Array &lt;Character[]&gt; &#42;/
	 *   4:     "valueWrapperArrayCharacter" : [
	 *   5:         /&#42; String &#42;/
	 *   6:         "p"
	 *   7:     ],
	 *   8: 
	 *   9:     /&#42; Array &lt;Short[]&gt; &#42;/
	 *  10:     "valueWrapperArrayShort" : [
	 *  11:         /&#42; Number &#42;/
	 *  12:         123
	 *  13:     ],
	 *  14: 
	 *  15:     /&#42; String &#42;/
	 *  16:     "valueWrapperCharacter" : "p",
	 *  17: 
	 *  18:     /&#42; Array &lt;byte[]&gt; &#42;/
	 *  19:     "valuePrimitiveArrayByte" : [
	 *  20:         /&#42; Number &#42;/
	 *  21:         123
	 *  22:     ],
	 *  23: 
	 *  24:     /&#42; Number &#42;/
	 *  25:     "valuePrimitiveFloat" : 123,
	 *  26: 
	 *  27:     /&#42; String &#42;/
	 *  28:     "valuePrimitiveChar" : "p",
	 *  29: 
	 *  30:     /&#42; Number &#42;/
	 *  31:     "valueWrapperInteger" : 123,
	 *  32: 
	 *  33:     /&#42; Array &lt;Long[]&gt; &#42;/
	 *  34:     "valueWrapperArrayLong" : [
	 *  35:         /&#42; Number &#42;/
	 *  36:         123
	 *  37:     ],
	 *  38: 
	 *  39:     /&#42; Array &lt;Double[]&gt; &#42;/
	 *  40:     "valueWrapperArrayDouble" : [
	 *  41:         /&#42; Number &#42;/
	 *  42:         123
	 *  43:     ],
	 *  44: 
	 *  45:     /&#42; Number &#42;/
	 *  46:     "valuePrimitiveByte" : 123,
	 *  47: 
	 *  48:     /&#42; Array &lt;Calendar[]&gt; &#42;/
	 *  49:     "valueWrapperArrayCalendar" : [
	 *  50:         /&#42; Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) &#42;/
	 *  51:         new Date(1213846496000)
	 *  52:     ],
	 *  53: 
	 *  54:     /&#42; Array &lt;String[]&gt; &#42;/
	 *  55:     "valueWrapperArrayString" : [
	 *  56:         /&#42; String &#42;/
	 *  57:         "prop_valueWrapperArrayString"
	 *  58:     ],
	 *  59: 
	 *  60:     /&#42; Array &lt;long[]&gt; &#42;/
	 *  61:     "valuePrimitiveArrayLong" : [
	 *  62:         /&#42; Number &#42;/
	 *  63:         123
	 *  64:     ],
	 *  65: 
	 *  66:     /&#42; Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) &#42;/
	 *  67:     "valueWrapperCalendar" : new Date(1213846496000),
	 *  68: 
	 *  69:     /&#42; Array &lt;Byte[]&gt; &#42;/
	 *  70:     "valueWrapperArrayByte" : [
	 *  71:         /&#42; Number &#42;/
	 *  72:         123
	 *  73:     ],
	 *  74: 
	 *  75:     /&#42; Number &#42;/
	 *  76:     "valuePrimitiveLong" : 123,
	 *  77: 
	 *  78:     /&#42; Array &lt;Float[]&gt; &#42;/
	 *  79:     "valueWrapperArrayFloat" : [
	 *  70:         /&#42; Number &#42;/
	 *  81:         123
	 *  82:     ],
	 *  83: 
	 *  84:     /&#42; Boolean &#42;/
	 *  85:     "valuePrimitiveBoolean" : true,
	 *  86: 
	 *  87:     /&#42; String &#42;/
	 *  88:     "valueWrapperString" : "prop_valueWrapperString",
	 *  89: 
	 *  90:     /&#42; Number &#42;/
	 *  91:     "valuePrimitiveShort" : 123,
	 *  92: 
	 *  93:     /&#42; Number &#42;/
	 *  94:     "valueWrapperLong" : 123,
	 *  95: 
	 *  96:     /&#42; Number &#42;/
	 *  97:     "valueWrapperDouble" : 123,
	 *  98: 
	 *  99:     /&#42; Array &lt;Boolean[]&gt; &#42;/
	 * 100:     "valueWrapperArrayBoolean" : [
	 * 101:         /&#42; Boolean &#42;/
	 * 102:         true
	 * 103:     ],
	 * 104: 
	 * 105:     /&#42; Array &lt;Number[]&gt; &#42;/
	 * 106:     "valueWrapperArrayNumber" : [
	 * 107:         /&#42; Number &#42;/
	 * 108:         123
	 * 109:     ],
	 * 110: 
	 * 111:     /&#42; Number &#42;/
	 * 112:     "valueWrapperByte" : 123,
	 * 113: 
	 * 114:     /&#42; Number &#42;/
	 * 115:     "valuePrimitiveDouble" : 123,
	 * 116: 
	 * 117:     /&#42; Array &lt;Date[]&gt; &#42;/
	 * 118:     "valueWrapperArrayDate" : [
	 * 119:         /&#42; Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) &#42;/
	 * 120:         new Date(1213846496000)
	 * 121:     ],
	 * 122: 
	 * 123:     /&#42; Number &#42;/
	 * 124:     "valueWrapperShort" : 123,
	 * 125: 
	 * 126:     /&#42; Array &lt;boolean[]&gt; &#42;/
	 * 127:     "valuePrimitiveArrayBoolean" : [
	 * 128:         /&#42; Boolean &#42;/
	 * 129:         true
	 * 130:     ],
	 * 131: 
	 * 132:     /&#42; Array &lt;float[]&gt; &#42;/
	 * 133:     "valuePrimitiveArrayFloat" : [
	 * 134:         /&#42; Number &#42;/
	 * 135:         123
	 * 136:     ],
	 * 137: 
	 * 138:     /&#42; Number &#42;/
	 * 139:     "valueWrapperDouble_POSI_INF" : 123,
	 * 140: 
	 * 141:     /&#42; Array &lt;Integer[]&gt; &#42;/
	 * 142:     "valueWrapperArrayInteger" : [
	 * 143:         /&#42; Number &#42;/
	 * 144:         123
	 * 145:     ],
	 * 146: 
	 * 147:     /&#42; Number &#42;/
	 * 148:     "valuePrimitiveInt" : 123,
	 * 149: 
	 * 150:     /&#42; Date (Thu Jun 19 2008 12:34:56 GMT+0900 (JST)) &#42;/
	 * 151:     "valueWrapperDate" : new Date(1213846496000),
	 * 152: 
	 * 153:     /&#42; Number &#42;/
	 * 154:     "valueWrapperDouble_NaN" : 123,
	 * 155: 
	 * 156:     /&#42; Number &#42;/
	 * 157:     "valueWrapperDouble_NEGA_INF" : 123,
	 * 158: 
	 * 159:     /&#42; Number &#42;/
	 * 160:     "valueWrapperLong_MAX" : 123,
	 * 161: 
	 * 162:     /&#42; Array &lt;short[]&gt; &#42;/
	 * 163:     "valuePrimitiveArrayShort" : [
	 * 164:         /&#42; Number &#42;/
	 * 165:         123
	 * 166:     ],
	 * 167: 
	 * 168:     /&#42; Array &lt;char[]&gt; &#42;/
	 * 169:     "valuePrimitiveArrayChar" : [
	 * 170:         /&#42; String &#42;/
	 * 171:         "p"
	 * 172:     ],
	 * 173: 
	 * 174:     /&#42; Array &lt;double[]&gt; &#42;/
	 * 175:     "valuePrimitiveArrayDouble" : [
	 * 176:         /&#42; Number &#42;/
	 * 177:         123
	 * 178:     ],
	 * 179: 
	 * 180:     /&#42; Number &#42;/
	 * 181:     "valueWrapperDouble_MIN" : 123,
	 * 182: 
	 * 183:     /&#42; Number &#42;/
	 * 184:     "valueWrapperNumber" : 123,
	 * 185: 
	 * 186:     /&#42; Boolean &#42;/
	 * 187:     "valueWrapperBoolean" : true,
	 * 188: 
	 * 189:     /&#42; Number &#42;/
	 * 190:     "valueWrapperLong_MIN" : 123,
	 * 191: 
	 * 192:     /&#42; Number &#42;/
	 * 193:     "valueWrapperDouble_MAX" : 123,
	 * 194: 
	 * 195:     /&#42; Number &#42;/
	 * 196:     "valueWrapperFloat" : 123,
	 * 197: 
	 * 198:     /&#42; Array &lt;int[]&gt; &#42;/
	 * 199:     "valuePrimitiveArrayInt" : [
	 * 200:         /&#42; Number &#42;/
	 * 201:         123
	 * 202:     ],
	 * 203: 
	 * 204:     /&#42; String &#42;/
	 * 205:     "propertyString" : "prop_propertyString"
	 * 206: }
	 * </pre>
	 */
	public void testJavaBeanToJS_Publicフィールド() throws Exception {
		// 可逆性あり！
		JavaScriptUtilityTestModel4PublicFields testData = 
			(JavaScriptUtilityTestModel4PublicFields)JavaScriptUtility.newInstanceFilledSampleData(JavaScriptUtilityTestModel4PublicFields.class, null);

		Object actual = JavaScriptUtility.javaBeanToJS(testData);

		assertEquals(ValueObject.class, actual.getClass());
		ValueObject jsObj = (ValueObject)actual;

		String propName;
		String expected;
		String actualString;
		Object actualValue;
		Date expectedDate;
		Date actualDate;
		Calendar expectedCal = Calendar.getInstance();
		Calendar actualCal = Calendar.getInstance();
		

		// 1行目
		propName = "";
		expected = JavaScriptUtilityTestModel4PublicFields.class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
		
		// 3行目
		propName = "valueWrapperArrayCharacter";
		expected = Character[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
		
		// 4行目
		Object valueWrapperArrayCharacter = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayCharacter);
		assertEquals(NativeArray.class, valueWrapperArrayCharacter.getClass());
		NativeArray js_valueWrapperArrayCharacter = (NativeArray)valueWrapperArrayCharacter;
		assertEquals(1, js_valueWrapperArrayCharacter.getLength());
		assertEquals("p", js_valueWrapperArrayCharacter.get(0, js_valueWrapperArrayCharacter));
		
		// 9行目
		propName = "valueWrapperArrayShort";
		expected = Short[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 10行目
		Object valueWrapperArrayShort = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayCharacter);
		assertEquals(NativeArray.class, valueWrapperArrayCharacter.getClass());
		NativeArray js_valueWrapperArrayShort = (NativeArray)valueWrapperArrayShort;
		assertEquals(1, js_valueWrapperArrayShort.getLength());
		assertEquals(123.0, js_valueWrapperArrayShort.get(0, js_valueWrapperArrayShort));

		// 15行目
		propName = "valueWrapperCharacter";
		expected = Character.class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);

		// 16行目
		Object valueWrapperCharacter = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperCharacter);
		assertEquals(String.class, valueWrapperCharacter.getClass());
		assertEquals("p", valueWrapperCharacter);
	
		// 18行目
		propName = "valuePrimitiveArrayByte";
		expected = byte[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 19行目
		// byte[]はStringに変換されます。
		byte[] bytes = {123}; // Array の中の byte[]なので、実際には使われないであろう値。
		Object valuePrimitiveArrayByte = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayByte);
		assertEquals(String.class, valuePrimitiveArrayByte.getClass());
		assertEquals(new String(bytes, JavaScriptUtility.NON_CONVERT_CHARSET), valuePrimitiveArrayByte);

		// 25行目
		propName = "valuePrimitiveFloat";
		Object valuePrimitiveFloat = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveFloat);
		assertEquals(Double.class, valuePrimitiveFloat.getClass());
		assertEquals(123.0, valuePrimitiveFloat);
		
		// 27行目
		propName = "valuePrimitiveChar";
		Object valuePrimitiveChar = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveChar);
		assertEquals(String.class, valuePrimitiveChar.getClass());
		assertEquals("p", valuePrimitiveChar);

		// 30行目
		propName = "valueWrapperInteger";
		Object valueWrapperInteger = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperInteger);
		assertEquals(Double.class, valueWrapperInteger.getClass());
		assertEquals(123.0, valueWrapperInteger);
		
		// 33行目
		propName = "valueWrapperArrayLong";
		expected = Long[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 34行目
		Object valueWrapperArrayLong = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayLong);
		assertEquals(NativeArray.class, valueWrapperArrayLong.getClass());
		NativeArray js_valueWrapperArrayLong = (NativeArray)valueWrapperArrayLong;
		assertEquals(1, js_valueWrapperArrayLong.getLength());
		assertEquals(123.0, js_valueWrapperArrayShort.get(0, js_valueWrapperArrayLong));
	
		// 39行目
		propName = "valueWrapperArrayDouble";
		expected = Double[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 40行目
		Object valueWrapperArrayDouble = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayDouble);
		assertEquals(NativeArray.class, valueWrapperArrayDouble.getClass());
		NativeArray js_valueWrapperArrayDouble = (NativeArray)valueWrapperArrayDouble;
		assertEquals(1, js_valueWrapperArrayDouble.getLength());
		assertEquals(123.0, js_valueWrapperArrayShort.get(0, js_valueWrapperArrayDouble));

		// 45行目
		propName = "valuePrimitiveByte";
		Object valuePrimitiveByte = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveByte);
		assertEquals(Double.class, valuePrimitiveByte.getClass());
		assertEquals(123.0, valuePrimitiveByte);

		// 48行目
		propName = "valueWrapperArrayCalendar";
		expected = Calendar[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 49行目
		Object valueWrapperArrayCalendar = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayCalendar);
		assertEquals(NativeArray.class, valueWrapperArrayCalendar.getClass());
		NativeArray js_valueWrapperArrayCalendar = (NativeArray)valueWrapperArrayCalendar;
		assertEquals(1, js_valueWrapperArrayCalendar.getLength());

		expectedCal.setTimeInMillis(1213846496000L); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(js_valueWrapperArrayCalendar.get(0, js_valueWrapperArrayCalendar), Date.class);
		actualCal.setTime(actualDate);
		assertEquals(expectedCal, actualCal);
		
		// 54行目
		propName = "valueWrapperArrayString";
		expected = String[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 55行目
		Object valueWrapperArrayString = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayString);
		assertEquals(NativeArray.class, valueWrapperArrayString.getClass());
		NativeArray js_valueWrapperArrayString = (NativeArray)valueWrapperArrayString;
		assertEquals(1, js_valueWrapperArrayString.getLength());
		assertEquals("prop_valueWrapperArrayString", js_valueWrapperArrayString.get(0, js_valueWrapperArrayString));

		// 60行目
		propName = "valuePrimitiveArrayLong";
		expected = long[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 61行目
		Object valuePrimitiveArrayLong = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayLong);
		assertEquals(NativeArray.class, valuePrimitiveArrayLong.getClass());
		NativeArray js_valuePrimitiveArrayLong = (NativeArray)valuePrimitiveArrayLong;
		assertEquals(1, js_valuePrimitiveArrayLong.getLength());
		assertEquals(123.0, js_valuePrimitiveArrayLong.get(0, js_valuePrimitiveArrayLong));

		// 67行目
		propName = "valueWrapperCalendar";
		Object valueWrapperCalendar = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperCalendar);
		assertEquals("org.mozilla.javascript.NativeDate", valueWrapperCalendar.getClass().getName());
		expectedDate = new Date(1213846496000L); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(valueWrapperCalendar, Date.class);
		assertEquals(expectedDate, actualDate);
		
		// 69行目
		propName = "valueWrapperArrayByte";
		expected = Byte[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 70行目
		Object valueWrapperArrayByte = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayByte);
		assertEquals(NativeArray.class, valueWrapperArrayByte.getClass());
		NativeArray js_valueWrapperArrayByte = (NativeArray)valueWrapperArrayByte;
		assertEquals(1, js_valueWrapperArrayByte.getLength());
		assertEquals(123.0, js_valueWrapperArrayByte.get(0, js_valueWrapperArrayByte));
		
		
		// 76行目
		propName = "valuePrimitiveLong";
		Object valuePrimitiveLong = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveLong);
		assertEquals(Double.class, valuePrimitiveLong.getClass());
		assertEquals(123.0, valuePrimitiveLong);
		

		// 78行目
		propName = "valueWrapperArrayFloat";
		expected = Float[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 79行目
		Object valueWrapperArrayFloat = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayFloat);
		assertEquals(NativeArray.class, valueWrapperArrayFloat.getClass());
		NativeArray js_valueWrapperArrayFloat = (NativeArray)valueWrapperArrayFloat;
		assertEquals(1, js_valueWrapperArrayFloat.getLength());
		assertEquals(123.0, js_valueWrapperArrayFloat.get(0, js_valueWrapperArrayFloat));

		// 85行目
		propName = "valuePrimitiveBoolean";
		Object valuePrimitiveBoolean = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveBoolean);
		assertEquals(Boolean.class, valuePrimitiveBoolean.getClass());
		assertEquals(true, valuePrimitiveBoolean);
	
		// 88行目
		propName = "valueWrapperString";
		Object valueWrapperString = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperString);
		assertEquals(String.class, valueWrapperString.getClass());
		assertEquals("prop_valueWrapperString", valueWrapperString);

		// 90行目
		propName = "valuePrimitiveShort";
		Object valuePrimitiveShort = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveShort);
		assertEquals(Double.class, valuePrimitiveShort.getClass());
		assertEquals(123.0, valuePrimitiveShort);

		// 94行目
		propName = "valueWrapperLong";
		Object valueWrapperLong = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperLong);
		assertEquals(Double.class, valueWrapperLong.getClass());
		assertEquals(123.0, valueWrapperLong);
		
		// 96行目
		propName = "valueWrapperDouble";
		Object valueWrapperDouble = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDouble);
		assertEquals(Double.class, valueWrapperDouble.getClass());
		assertEquals(123.0, valueWrapperLong);
		
		// 99行目
		propName = "valueWrapperArrayBoolean";
		expected = Boolean[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 100行目
		Object valueWrapperArrayBoolean = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayBoolean);
		assertEquals(NativeArray.class, valueWrapperArrayBoolean.getClass());
		NativeArray js_valueWrapperArrayBoolean = (NativeArray)valueWrapperArrayBoolean;
		assertEquals(1, js_valueWrapperArrayBoolean.getLength());
		assertEquals(true, js_valueWrapperArrayBoolean.get(0, js_valueWrapperArrayBoolean));
		
		// 105行目
		propName = "valueWrapperArrayNumber";
		expected = Number[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 106行目
		Object valueWrapperArrayNumber = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayNumber);
		assertEquals(NativeArray.class, valueWrapperArrayNumber.getClass());
		NativeArray js_valueWrapperArrayNumber = (NativeArray)valueWrapperArrayNumber;
		assertEquals(1, js_valueWrapperArrayNumber.getLength());
		assertEquals(123.0, js_valueWrapperArrayNumber.get(0, js_valueWrapperArrayNumber));

		// 112行目
		propName = "valueWrapperByte";
		Object valueWrapperByte = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperByte);
		assertEquals(Double.class, valueWrapperByte.getClass());
		assertEquals(123.0, valueWrapperByte);

		// 115行目
		propName = "valuePrimitiveDouble";
		Object valuePrimitiveDouble = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveDouble);
		assertEquals(Double.class, valuePrimitiveDouble.getClass());
		assertEquals(123.0, valuePrimitiveDouble);

		// 117行目
		propName = "valueWrapperArrayDate";
		expected = Date[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 118行目
		Object valueWrapperArrayDate = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayDate);
		assertEquals(NativeArray.class, valueWrapperArrayDate.getClass());
		NativeArray js_valueWrapperArrayDate = (NativeArray)valueWrapperArrayDate;
		assertEquals(1, js_valueWrapperArrayDate.getLength());
		
		expectedDate = new Date(1213846496000L); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(js_valueWrapperArrayDate.get(0, js_valueWrapperArrayDate), Date.class);
		assertEquals(expectedDate, actualDate);
		
		// 124行目
		propName = "valueWrapperShort";
		Object valueWrapperShort = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperShort);
		assertEquals(Double.class, valueWrapperShort.getClass());
		assertEquals(123.0, valueWrapperShort);

		// 126行目
		propName = "valuePrimitiveArrayBoolean";
		expected = boolean[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 127行目
		Object valuePrimitiveArrayBoolean = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayBoolean);
		assertEquals(NativeArray.class, valuePrimitiveArrayBoolean.getClass());
		NativeArray js_valuePrimitiveArrayBoolean = (NativeArray)valuePrimitiveArrayBoolean;
		assertEquals(1, js_valuePrimitiveArrayBoolean.getLength());
		assertEquals(true, js_valuePrimitiveArrayBoolean.get(0, js_valuePrimitiveArrayBoolean));

		// 132行目
		propName = "valuePrimitiveArrayFloat";
		expected = float[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 133行目
		Object valuePrimitiveArrayFloat = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayFloat);
		assertEquals(NativeArray.class, valuePrimitiveArrayFloat.getClass());
		NativeArray js_valuePrimitiveArrayFloat = (NativeArray)valuePrimitiveArrayFloat;
		assertEquals(1, js_valuePrimitiveArrayFloat.getLength());
		assertEquals(123.0, js_valuePrimitiveArrayFloat.get(0, js_valuePrimitiveArrayFloat));
	
		// 138行目
		propName = "valueWrapperDouble_POSI_INF";
		Object valueWrapperDouble_POSI_INF = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDouble_POSI_INF);
		assertEquals(Double.class, valueWrapperDouble_POSI_INF.getClass());
		assertEquals(123.0, valueWrapperDouble_POSI_INF);
	

		// 141行目
		propName = "valueWrapperArrayInteger";
		expected = Integer[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 142行目
		Object valueWrapperArrayInteger = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperArrayInteger);
		assertEquals(NativeArray.class, valueWrapperArrayInteger.getClass());
		NativeArray js_valueWrapperArrayInteger = (NativeArray)valueWrapperArrayInteger;
		assertEquals(1, js_valueWrapperArrayInteger.getLength());
		assertEquals(123.0, js_valueWrapperArrayInteger.get(0, js_valueWrapperArrayInteger));
	
		// 147行目
		propName = "valuePrimitiveInt";
		Object valuePrimitiveInt = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveInt);
		assertEquals(Double.class, valuePrimitiveInt.getClass());
		assertEquals(123.0, valuePrimitiveInt);
		
		// 151行目
		propName = "valueWrapperDate";
		Object valueWrapperDate = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDate);
		assertEquals("org.mozilla.javascript.NativeDate", valueWrapperDate.getClass().getName());
		expectedDate = new Date(1213846496000L); // Thu Jun 19 2008 12:34:56 GMT+0900 (JST)
		actualDate = (Date) Context.jsToJava(valueWrapperDate, Date.class);
		assertEquals(expectedDate, actualDate);
		
		// 154行目
		propName = "valueWrapperDouble_NaN";
		Object valueWrapperDouble_NaN = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDouble_NaN);
		assertEquals(Double.class, valueWrapperDouble_NaN.getClass());
		assertEquals(123.0, valueWrapperDouble_NaN);
	
		// 157行目
		propName = "valueWrapperDouble_NEGA_INF";
		Object valueWrapperDouble_NEGA_INF = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDouble_NEGA_INF);
		assertEquals(Double.class, valueWrapperDouble_NEGA_INF.getClass());
		assertEquals(123.0, valueWrapperDouble_NEGA_INF);

		// 160行目
		propName = "valueWrapperLong_MAX";
		Object valueWrapperLong_MAX = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperLong_MAX);
		assertEquals(Double.class, valueWrapperLong_MAX.getClass());
		assertEquals(123.0, valueWrapperLong_MAX);
		
		// 162行目
		propName = "valuePrimitiveArrayShort";
		expected = short[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 163行目
		Object valuePrimitiveArrayShort = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayShort);
		assertEquals(NativeArray.class, valuePrimitiveArrayShort.getClass());
		NativeArray js_valuePrimitiveArrayShort = (NativeArray)valuePrimitiveArrayShort;
		assertEquals(1, js_valuePrimitiveArrayShort.getLength());
		assertEquals(123.0, js_valuePrimitiveArrayShort.get(0, js_valuePrimitiveArrayShort));

		// 168行目
		propName = "valuePrimitiveArrayChar";
		expected = char[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 169行目
		Object valuePrimitiveArrayChar = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayChar);
		assertEquals(NativeArray.class, valuePrimitiveArrayChar.getClass());
		NativeArray js_valuePrimitiveArrayChar = (NativeArray)valuePrimitiveArrayChar;
		assertEquals(1, js_valuePrimitiveArrayChar.getLength());
		assertEquals("p", js_valuePrimitiveArrayChar.get(0, js_valuePrimitiveArrayChar));
		
		// 174行目
		propName = "valuePrimitiveArrayDouble";
		expected = double[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 175行目
		Object valuePrimitiveArrayDouble = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayDouble);
		assertEquals(NativeArray.class, valuePrimitiveArrayDouble.getClass());
		NativeArray js_valuePrimitiveArrayDouble = (NativeArray)valuePrimitiveArrayDouble;
		assertEquals(1, js_valuePrimitiveArrayDouble.getLength());
		assertEquals(123.0, js_valuePrimitiveArrayDouble.get(0, js_valuePrimitiveArrayDouble));
		
		// 181行目
		propName = "valueWrapperDouble_MIN";
		Object valueWrapperDouble_MIN = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDouble_MIN);
		assertEquals(Double.class, valueWrapperDouble_MIN.getClass());
		assertEquals(123.0, valueWrapperDouble_MIN);

		// 184行目
		propName = "valueWrapperNumber";
		Object valueWrapperNumber = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperNumber);
		assertEquals(Double.class, valueWrapperNumber.getClass());
		assertEquals(123.0, valueWrapperNumber);

		// 187行目
		propName = "valueWrapperBoolean";
		Object valueWrapperBoolean = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperBoolean);
		assertEquals(Boolean.class, valueWrapperBoolean.getClass());
		assertEquals(true, valueWrapperBoolean);

		// 189行目
		propName = "valueWrapperLong_MIN";
		Object valueWrapperLong_MIN = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperLong_MIN);
		assertEquals(Double.class, valueWrapperLong_MIN.getClass());
		assertEquals(123.0, valueWrapperLong_MIN);

		// 193行目
		propName = "valueWrapperDouble_MAX";
		Object valueWrapperDouble_MAX = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperDouble_MAX);
		assertEquals(Double.class, valueWrapperDouble_MAX.getClass());
		assertEquals(123.0, valueWrapperDouble_MAX);

		// 195行目
		propName = "valueWrapperFloat";
		Object valueWrapperFloat = jsObj.get(propName, jsObj);
		assertNotNull(valueWrapperFloat);
		assertEquals(Double.class, valueWrapperFloat.getClass());
		assertEquals(123.0, valueWrapperFloat);

		// 198行目
		propName = "valuePrimitiveArrayInt";
		expected = int[].class.getSimpleName();
		actualString = (String) jsObj.get("__javaClassName_" + propName + "__", jsObj);
		assertEquals(expected, actualString);
	
		// 199行目
		Object valuePrimitiveArrayInt = jsObj.get(propName, jsObj);
		assertNotNull(valuePrimitiveArrayInt);
		assertEquals(NativeArray.class, valuePrimitiveArrayInt.getClass());
		NativeArray js_valuePrimitiveArrayInt = (NativeArray)valuePrimitiveArrayInt;
		assertEquals(1, js_valuePrimitiveArrayInt.getLength());
		assertEquals(123.0, js_valuePrimitiveArrayInt.get(0, js_valuePrimitiveArrayInt));
		
		// 205行目
		propName = "propertyString";
		Object propertyString = jsObj.get(propName, jsObj);
		assertNotNull(propertyString);
		assertEquals(String.class, propertyString.getClass());
		assertEquals("prop_propertyString", propertyString);
	}

	public void testjsToJavaBean_Publicフィールド() throws Exception {
		// 可逆性あり！
		JavaScriptUtilityTestModel4PublicFields expected = (JavaScriptUtilityTestModel4PublicFields)JavaScriptUtility.newInstanceFilledSampleData(JavaScriptUtilityTestModel4PublicFields.class, null);
		Object jsObject = JavaScriptUtility.javaBeanToJS(expected);
		
		Object actual = JavaScriptUtility.jsToJavaBean(jsObject, JavaScriptUtilityTestModel4PublicFields.class);
		assertEquals(JavaScriptUtilityTestModel4PublicFields.class, actual.getClass());
		
		JavaScriptUtilityTestModel4PublicFields actualModel = (JavaScriptUtilityTestModel4PublicFields) actual;
		
		assertEquals(expected.valuePrimitiveChar, actualModel.valuePrimitiveChar);
		assertEquals(expected.valuePrimitiveDouble, actualModel.valuePrimitiveDouble);
		assertEquals(expected.valuePrimitiveFloat, actualModel.valuePrimitiveFloat);
		assertEquals(expected.valuePrimitiveLong, actualModel.valuePrimitiveLong);
		assertEquals(expected.valuePrimitiveInt, actualModel.valuePrimitiveInt);
		assertEquals(expected.valuePrimitiveShort, actualModel.valuePrimitiveShort);
		assertEquals(expected.valuePrimitiveByte, actualModel.valuePrimitiveByte);
		assertEquals(expected.valuePrimitiveBoolean, actualModel.valuePrimitiveBoolean);
		
		assertEquals(expected.valuePrimitiveArrayChar[0], actualModel.valuePrimitiveArrayChar[0]);
		assertEquals(expected.valuePrimitiveArrayDouble[0], actualModel.valuePrimitiveArrayDouble[0]);
		assertEquals(expected.valuePrimitiveArrayFloat[0], actualModel.valuePrimitiveArrayFloat[0]);
		assertEquals(expected.valuePrimitiveArrayLong[0], actualModel.valuePrimitiveArrayLong[0]);
		assertEquals(expected.valuePrimitiveArrayInt[0], actualModel.valuePrimitiveArrayInt[0]);
		assertEquals(expected.valuePrimitiveArrayShort[0], actualModel.valuePrimitiveArrayShort[0]);
		assertEquals(expected.valuePrimitiveArrayByte[0], actualModel.valuePrimitiveArrayByte[0]);
		assertEquals(expected.valuePrimitiveArrayBoolean[0], actualModel.valuePrimitiveArrayBoolean[0]);

		assertEquals(expected.valueWrapperString, actualModel.valueWrapperString);
		assertEquals(expected.valueWrapperCharacter, actualModel.valueWrapperCharacter);
		assertEquals(expected.valueWrapperDouble, actualModel.valueWrapperDouble);
		assertEquals(expected.valueWrapperDouble_MAX, actualModel.valueWrapperDouble_MAX);
		assertEquals(expected.valueWrapperDouble_MIN, actualModel.valueWrapperDouble_MIN);
		assertEquals(expected.valueWrapperDouble_NaN, actualModel.valueWrapperDouble_NaN);
		assertEquals(expected.valueWrapperDouble_NEGA_INF, actualModel.valueWrapperDouble_NEGA_INF);
		assertEquals(expected.valueWrapperDouble_POSI_INF, actualModel.valueWrapperDouble_POSI_INF);
		assertEquals(expected.valueWrapperFloat, actualModel.valueWrapperFloat);
		assertEquals(expected.valueWrapperLong, actualModel.valueWrapperLong);
		assertEquals(expected.valueWrapperLong_MAX, actualModel.valueWrapperLong_MAX);
		assertEquals(expected.valueWrapperLong_MIN, actualModel.valueWrapperLong_MIN);
		assertEquals(expected.valueWrapperInteger, actualModel.valueWrapperInteger);
		assertEquals(expected.valueWrapperShort, actualModel.valueWrapperShort);
		assertEquals(expected.valueWrapperByte, actualModel.valueWrapperByte);
		assertEquals(expected.valueWrapperBoolean, actualModel.valueWrapperBoolean);
		
		assertEquals(expected.valueWrapperArrayString[0], actualModel.valueWrapperArrayString[0]);
		assertEquals(expected.valueWrapperArrayCharacter[0], actualModel.valueWrapperArrayCharacter[0]);
		assertEquals(expected.valueWrapperArrayDouble[0], actualModel.valueWrapperArrayDouble[0]);
		assertEquals(expected.valueWrapperArrayFloat[0], actualModel.valueWrapperArrayFloat[0]);
		assertEquals(expected.valueWrapperArrayLong[0], actualModel.valueWrapperArrayLong[0]);
		assertEquals(expected.valueWrapperArrayLong[0], actualModel.valueWrapperArrayLong[0]);
		assertEquals(expected.valueWrapperArrayInteger[0], actualModel.valueWrapperArrayInteger[0]);
		assertEquals(expected.valueWrapperArrayShort[0], actualModel.valueWrapperArrayShort[0]);
		assertEquals(expected.valueWrapperArrayByte[0], actualModel.valueWrapperArrayByte[0]);
		assertEquals(expected.valueWrapperArrayBoolean[0], actualModel.valueWrapperArrayBoolean[0]);
		assertEquals(expected.valueWrapperNumber, actualModel.valueWrapperNumber);
		assertEquals(expected.valueWrapperArrayNumber[0], actualModel.valueWrapperArrayNumber[0]);
		assertEquals(expected.valueWrapperDate, actualModel.valueWrapperDate);
		assertEquals(expected.valueWrapperArrayDate[0], actualModel.valueWrapperArrayDate[0]);
		assertEquals(expected.valueWrapperCalendar, actualModel.valueWrapperCalendar);
		assertEquals(expected.valueWrapperArrayCalendar[0], actualModel.valueWrapperArrayCalendar[0]);
	}

}
