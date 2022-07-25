package org.intra_mart.jssp.util;

import java.util.Calendar;
import java.util.Date;

public class JavaScriptUtilityTestModel4PublicFields {
	
	public char valuePrimitiveChar = 'ん';
	public double valuePrimitiveDouble = 12.34;
	public float valuePrimitiveFloat = 56.78F;
	public long valuePrimitiveLong = 91011L;
	public int valuePrimitiveInt = 121314;
	public short valuePrimitiveShort = 456;
	public byte valuePrimitiveByte = 123;
	public boolean valuePrimitiveBoolean = true;

	public char[] valuePrimitiveArrayChar = {'わ', 'を', 'ん'};
	public double[] valuePrimitiveArrayDouble = {12.34, 56.78, 910.1112};
	public float[] valuePrimitiveArrayFloat = {56.78F, 910.1112F, 1314.1516F};
	public long[] valuePrimitiveArrayLong = {91011L, 121314L, 151617L};
	public int[] valuePrimitiveArrayInt = {121314, 1516, 1718};
	public short[] valuePrimitiveArrayShort = {456, 789, 1011};
	public byte[] valuePrimitiveArrayByte = {123, 124, 125};
	public boolean[] valuePrimitiveArrayBoolean = {true, false, false};

	public String valueWrapperString = "藍上尾";
	public Character valueWrapperCharacter = new Character('や');
	public Double valueWrapperDouble = new Double(141312);
	public Double valueWrapperDouble_MAX = Double.MAX_VALUE;
	public Double valueWrapperDouble_MIN = Double.MIN_VALUE;
	public Double valueWrapperDouble_NaN = Double.NaN;
	public Double valueWrapperDouble_NEGA_INF = Double.NEGATIVE_INFINITY;
	public Double valueWrapperDouble_POSI_INF = Double.POSITIVE_INFINITY;
	public Float valueWrapperFloat = new Float(11.109F);
	public Long valueWrapperLong = new Long(8765L);
	public Long valueWrapperLong_MAX = Long.MAX_VALUE;
	public Long valueWrapperLong_MIN = Long.MIN_VALUE;
	public Integer valueWrapperInteger = new Integer(4321);
	public Short valueWrapperShort = new Short("34");
	public Byte valueWrapperByte = new Byte("12");
	public Boolean valueWrapperBoolean = Boolean.TRUE;
	
	public String[] valueWrapperArrayString = {"藍上尾", "書きくけ子", "さしすせそ"};
	public Character[] valueWrapperArrayCharacter = {new Character('や'), new Character('ゆ'), new Character('よ')};
	public Double[] valueWrapperArrayDouble = {
				new Double(141312),
				new Double(Double.MAX_VALUE),
				new Double(Double.MIN_VALUE),
				new Double(Double.NaN),
				new Double(Double.NEGATIVE_INFINITY),
				new Double(Double.POSITIVE_INFINITY) 
			};

	public Float[] valueWrapperArrayFloat = {
				new Float(11.109F),
				new Float(Float.MAX_VALUE),
				new Float(Float.MIN_VALUE), 
				new Float(Float.NaN), 
				new Float(Float.NEGATIVE_INFINITY), 
				new Float(Float.POSITIVE_INFINITY)
			};

	public Long[] valueWrapperArrayLong = {
				new Long(8765L),
				new Long(Long.MAX_VALUE),
				new Long(Long.MIN_VALUE)
			};

	public Integer[] valueWrapperArrayInteger = {
				new Integer(4321), 
				new Integer(Integer.MAX_VALUE),
				new Integer(Integer.MIN_VALUE)
			};
	public Short[] valueWrapperArrayShort = {
				new Short("34"),
				new Short(Short.MAX_VALUE),
				new Short(Short.MIN_VALUE)
			};
	
	public Byte[] valueWrapperArrayByte = {
				new Byte("12"),
				new Byte(Byte.MAX_VALUE),
				new Byte(Byte.MIN_VALUE)
			};

	public Boolean[] valueWrapperArrayBoolean = {
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.TRUE
		};

	public Number valueWrapperNumber = new Double(9999.999);

	public Number[] valueWrapperArrayNumber = {
				new Double(9999.999),
				new Double(8888.888),
				new Double(7777.777),
		};
	
	public Date valueWrapperDate = new Date(1208230496000L); // Tue Apr 15 2008 12:34:56 GMT+0900 (JST)

	public Date[] valueWrapperArrayDate = {
				new Date(1208230496000L), // Tue Apr 15 2008 12:34:56 GMT+0900 (JST)
				new Date(1239766496000L), // Wed Apr 15 2009 12:34:56 GMT+0900 (JST)
				new Date(1271302496000L)  // Thu Apr 15 2010 12:34:56 GMT+0900 (JST)
		};
	
	public Calendar valueWrapperCalendar;

	public Calendar[] valueWrapperArrayCalendar;
	
	public JavaScriptUtilityTestModel4PublicFields(){
		valueWrapperCalendar = Calendar.getInstance();
		valueWrapperCalendar.setTimeInMillis(1227584096000L); // Tue Nov 25 2008 12:34:56 GMT+0900 (JST)

		Calendar cal0 = Calendar.getInstance();
		cal0.setTimeInMillis(1227584096000L); // Tue Nov 25 2008 12:34:56 GMT+0900 (JST)
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(1259120096000L); // Wed Nov 25 2009 12:34:56 GMT+0900 (JST)
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(1290656096000L); // Thu Nov 25 2010 12:34:56 GMT+0900 (JST)

		valueWrapperArrayCalendar = new Calendar[]{cal0, cal1, cal2};

	}
	
	
	private String propertyString = "プロパティ"; // 

	public String getPropertyString() {
		return propertyString;
	}


	public void setPropertyString(String propertyString) {
		this.propertyString = propertyString;
	}
	
	
	
	

}