package org.intra_mart.jssp.util;

import java.util.Calendar;
import java.util.Date;

public class JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty {
	
	private char valuePrimitiveChar = 'ん';
	private double valuePrimitiveDouble = 12.34;
	private float valuePrimitiveFloat = 56.78F;
	private long valuePrimitiveLong = 91011L;
	private int valuePrimitiveInt = 121314;
	private short valuePrimitiveShort = 456;
	private byte valuePrimitiveByte = 123;
	private boolean valuePrimitiveBoolean = true;

	private char[] valuePrimitiveArrayChar = {'わ', 'を', 'ん'};
	private double[] valuePrimitiveArrayDouble = {12.34, 56.78, 910.1112};
	private float[] valuePrimitiveArrayFloat = {56.78F, 910.1112F, 1314.1516F};
	private long[] valuePrimitiveArrayLong = {91011L, 121314L, 151617L};
	private int[] valuePrimitiveArrayInt = {121314, 1516, 1718};
	private short[] valuePrimitiveArrayShort = {456, 789, 1011};
	private byte[] valuePrimitiveArrayByte = {123, 124, 125};
	private boolean[] valuePrimitiveArrayBoolean = {true, false, false};

	private String valueWrapperString = "藍上尾";
	private Character valueWrapperCharacter = new Character('や');
	private Double valueWrapperDouble = new Double(141312);
	private Double valueWrapperDouble_MAX = Double.MAX_VALUE;
	private Double valueWrapperDouble_MIN = Double.MIN_VALUE;
	private Double valueWrapperDouble_NaN = Double.NaN;
	private Double valueWrapperDouble_NEGA_INF = Double.NEGATIVE_INFINITY;
	private Double valueWrapperDouble_POSI_INF = Double.POSITIVE_INFINITY;
	private Float valueWrapperFloat = new Float(11.109F);
	private Long valueWrapperLong = new Long(8765L);
	private Long valueWrapperLong_MAX = Long.MAX_VALUE;
	private Long valueWrapperLong_MIN = Long.MIN_VALUE;
	private Integer valueWrapperInteger = new Integer(4321);
	private Short valueWrapperShort = new Short("34");
	private Byte valueWrapperByte = new Byte("12");
	private Boolean valueWrapperBoolean = Boolean.TRUE;
	
	private String[] valueWrapperArrayString = {"藍上尾", "書きくけ子", "さしすせそ"};
	private Character[] valueWrapperArrayCharacter = {new Character('や'), new Character('ゆ'), new Character('よ')};
	private Double[] valueWrapperArrayDouble = {
				new Double(141312),
				new Double(Double.MAX_VALUE),
				new Double(Double.MIN_VALUE),
				new Double(Double.NaN),
				new Double(Double.NEGATIVE_INFINITY),
				new Double(Double.POSITIVE_INFINITY) 
			};

	private Float[] valueWrapperArrayFloat = {
				new Float(11.109F),
				new Float(Float.MAX_VALUE),
				new Float(Float.MIN_VALUE), 
				new Float(Float.NaN), 
				new Float(Float.NEGATIVE_INFINITY), 
				new Float(Float.POSITIVE_INFINITY)
			};

	private Long[] valueWrapperArrayLong = {
				new Long(8765L),
				new Long(Long.MAX_VALUE),
				new Long(Long.MIN_VALUE)
			};

	private Integer[] valueWrapperArrayInteger = {
				new Integer(4321), 
				new Integer(Integer.MAX_VALUE),
				new Integer(Integer.MIN_VALUE)
			};
	private Short[] valueWrapperArrayShort = {
				new Short("34"),
				new Short(Short.MAX_VALUE),
				new Short(Short.MIN_VALUE)
			};
	
	private Byte[] valueWrapperArrayByte = {
				new Byte("12"),
				new Byte(Byte.MAX_VALUE),
				new Byte(Byte.MIN_VALUE)
			};

	private Boolean[] valueWrapperArrayBoolean = {
				Boolean.FALSE,
				Boolean.FALSE,
				Boolean.TRUE
		};

	private Number valueWrapperNumber = new Double(9999.999);

	private Number[] valueWrapperArrayNumber = {
				new Double(9999.999),
				new Double(8888.888),
				new Double(7777.777),
		};
	
	private Date valueWrapperDate = new Date(1208230496000L); // Tue Apr 15 2008 12:34:56 GMT+0900 (JST)

	private Date[] valueWrapperArrayDate = {
				new Date(1208230496000L), // Tue Apr 15 2008 12:34:56 GMT+0900 (JST)
				new Date(1239766496000L), // Wed Apr 15 2009 12:34:56 GMT+0900 (JST)
				new Date(1271302496000L)  // Thu Apr 15 2010 12:34:56 GMT+0900 (JST)
		};
	
	private Calendar valueWrapperCalendar;

	private Calendar[] valueWrapperArrayCalendar;
	
	public JavaScriptUtilityTestModel4PrimitiveAndWrapperProperty(){
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
	
	public char getValuePrimitiveChar() {
		return valuePrimitiveChar;
	}

	public void setValuePrimitiveChar(char valuePrimitiveChar) {
		this.valuePrimitiveChar = valuePrimitiveChar;
	}

	public double getValuePrimitiveDouble() {
		return valuePrimitiveDouble;
	}

	public void setValuePrimitiveDouble(double valuePrimitiveDouble) {
		this.valuePrimitiveDouble = valuePrimitiveDouble;
	}

	public float getValuePrimitiveFloat() {
		return valuePrimitiveFloat;
	}

	public void setValuePrimitiveFloat(float valuePrimitiveFloat) {
		this.valuePrimitiveFloat = valuePrimitiveFloat;
	}

	public long getValuePrimitiveLong() {
		return valuePrimitiveLong;
	}

	public void setValuePrimitiveLong(long valuePrimitiveLong) {
		this.valuePrimitiveLong = valuePrimitiveLong;
	}

	public int getValuePrimitiveInt() {
		return valuePrimitiveInt;
	}

	public void setValuePrimitiveInt(int valuePrimitiveInt) {
		this.valuePrimitiveInt = valuePrimitiveInt;
	}

	public short getValuePrimitiveShort() {
		return valuePrimitiveShort;
	}

	public void setValuePrimitiveShort(short valuePrimitiveShort) {
		this.valuePrimitiveShort = valuePrimitiveShort;
	}

	public byte getValuePrimitiveByte() {
		return valuePrimitiveByte;
	}

	public void setValuePrimitiveByte(byte valuePrimitiveByte) {
		this.valuePrimitiveByte = valuePrimitiveByte;
	}

	public boolean isValuePrimitiveBoolean() {
		return valuePrimitiveBoolean;
	}

	public void setValuePrimitiveBoolean(boolean valuePrimitiveBoolean) {
		this.valuePrimitiveBoolean = valuePrimitiveBoolean;
	}

	public char[] getValuePrimitiveArrayChar() {
		return valuePrimitiveArrayChar;
	}

	public void setValuePrimitiveArrayChar(char[] valuePrimitiveArrayChar) {
		this.valuePrimitiveArrayChar = valuePrimitiveArrayChar;
	}

	public double[] getValuePrimitiveArrayDouble() {
		return valuePrimitiveArrayDouble;
	}

	public void setValuePrimitiveArrayDouble(double[] valuePrimitiveArrayDouble) {
		this.valuePrimitiveArrayDouble = valuePrimitiveArrayDouble;
	}

	public float[] getValuePrimitiveArrayFloat() {
		return valuePrimitiveArrayFloat;
	}

	public void setValuePrimitiveArrayFloat(float[] valuePrimitiveArrayFloat) {
		this.valuePrimitiveArrayFloat = valuePrimitiveArrayFloat;
	}

	public long[] getValuePrimitiveArrayLong() {
		return valuePrimitiveArrayLong;
	}

	public void setValuePrimitiveArrayLong(long[] valuePrimitiveArrayLong) {
		this.valuePrimitiveArrayLong = valuePrimitiveArrayLong;
	}

	public int[] getValuePrimitiveArrayInt() {
		return valuePrimitiveArrayInt;
	}

	public void setValuePrimitiveArrayInt(int[] valuePrimitiveArrayInt) {
		this.valuePrimitiveArrayInt = valuePrimitiveArrayInt;
	}

	public short[] getValuePrimitiveArrayShort() {
		return valuePrimitiveArrayShort;
	}

	public void setValuePrimitiveArrayShort(short[] valuePrimitiveArrayShort) {
		this.valuePrimitiveArrayShort = valuePrimitiveArrayShort;
	}

	public byte[] getValuePrimitiveArrayByte() {
		return valuePrimitiveArrayByte;
	}

	public void setValuePrimitiveArrayByte(byte[] valuePrimitiveArrayByte) {
		this.valuePrimitiveArrayByte = valuePrimitiveArrayByte;
	}

	public boolean[] getValuePrimitiveArrayBoolean() {
		return valuePrimitiveArrayBoolean;
	}

	public void setValuePrimitiveArrayBoolean(boolean[] valuePrimitiveArrayBoolean) {
		this.valuePrimitiveArrayBoolean = valuePrimitiveArrayBoolean;
	}

	public String getValueWrapperString() {
		return valueWrapperString;
	}

	public void setValueWrapperString(String valueWrapperString) {
		this.valueWrapperString = valueWrapperString;
	}

	public Character getValueWrapperCharacter() {
		return valueWrapperCharacter;
	}

	public void setValueWrapperCharacter(Character valueWrapperCharacter) {
		this.valueWrapperCharacter = valueWrapperCharacter;
	}

	public Double getValueWrapperDouble() {
		return valueWrapperDouble;
	}

	public void setValueWrapperDouble(Double valueWrapperDouble) {
		this.valueWrapperDouble = valueWrapperDouble;
	}

	public Double getValueWrapperDouble_MAX() {
		return valueWrapperDouble_MAX;
	}

	public void setValueWrapperDouble_MAX(Double valueWrapperDouble_MAX) {
		this.valueWrapperDouble_MAX = valueWrapperDouble_MAX;
	}

	public Double getValueWrapperDouble_MIN() {
		return valueWrapperDouble_MIN;
	}

	public void setValueWrapperDouble_MIN(Double valueWrapperDouble_MIN) {
		this.valueWrapperDouble_MIN = valueWrapperDouble_MIN;
	}

	public Double getValueWrapperDouble_NaN() {
		return valueWrapperDouble_NaN;
	}

	public void setValueWrapperDouble_NaN(Double valueWrapperDouble_NaN) {
		this.valueWrapperDouble_NaN = valueWrapperDouble_NaN;
	}

	public Double getValueWrapperDouble_NEGA_INF() {
		return valueWrapperDouble_NEGA_INF;
	}

	public void setValueWrapperDouble_NEGA_INF(Double valueWrapperDouble_NEGA_INF) {
		this.valueWrapperDouble_NEGA_INF = valueWrapperDouble_NEGA_INF;
	}

	public Double getValueWrapperDouble_POSI_INF() {
		return valueWrapperDouble_POSI_INF;
	}

	public void setValueWrapperDouble_POSI_INF(Double valueWrapperDouble_POSI_INF) {
		this.valueWrapperDouble_POSI_INF = valueWrapperDouble_POSI_INF;
	}

	public Float getValueWrapperFloat() {
		return valueWrapperFloat;
	}

	public void setValueWrapperFloat(Float valueWrapperFloat) {
		this.valueWrapperFloat = valueWrapperFloat;
	}

	public Long getValueWrapperLong() {
		return valueWrapperLong;
	}

	public void setValueWrapperLong(Long valueWrapperLong) {
		this.valueWrapperLong = valueWrapperLong;
	}

	public Long getValueWrapperLong_MAX() {
		return valueWrapperLong_MAX;
	}

	public void setValueWrapperLong_MAX(Long valueWrapperLong_MAX) {
		this.valueWrapperLong_MAX = valueWrapperLong_MAX;
	}

	public Long getValueWrapperLong_MIN() {
		return valueWrapperLong_MIN;
	}

	public void setValueWrapperLong_MIN(Long valueWrapperLong_MIN) {
		this.valueWrapperLong_MIN = valueWrapperLong_MIN;
	}

	public Integer getValueWrapperInteger() {
		return valueWrapperInteger;
	}

	public void setValueWrapperInteger(Integer valueWrapperInteger) {
		this.valueWrapperInteger = valueWrapperInteger;
	}

	public Short getValueWrapperShort() {
		return valueWrapperShort;
	}

	public void setValueWrapperShort(Short valueWrapperShort) {
		this.valueWrapperShort = valueWrapperShort;
	}

	public Byte getValueWrapperByte() {
		return valueWrapperByte;
	}

	public void setValueWrapperByte(Byte valueWrapperByte) {
		this.valueWrapperByte = valueWrapperByte;
	}

	public Boolean getValueWrapperBoolean() {
		return valueWrapperBoolean;
	}

	public void setValueWrapperBoolean(Boolean valueWrapperBoolean) {
		this.valueWrapperBoolean = valueWrapperBoolean;
	}

	public String[] getValueWrapperArrayString() {
		return valueWrapperArrayString;
	}

	public void setValueWrapperArrayString(String[] valueWrapperArrayString) {
		this.valueWrapperArrayString = valueWrapperArrayString;
	}

	public Character[] getValueWrapperArrayCharacter() {
		return valueWrapperArrayCharacter;
	}

	public void setValueWrapperArrayCharacter(Character[] valueWrapperArrayCharacter) {
		this.valueWrapperArrayCharacter = valueWrapperArrayCharacter;
	}

	public Double[] getValueWrapperArrayDouble() {
		return valueWrapperArrayDouble;
	}

	public void setValueWrapperArrayDouble(Double[] valueWrapperArrayDouble) {
		this.valueWrapperArrayDouble = valueWrapperArrayDouble;
	}

	public Float[] getValueWrapperArrayFloat() {
		return valueWrapperArrayFloat;
	}

	public void setValueWrapperArrayFloat(Float[] valueWrapperArrayFloat) {
		this.valueWrapperArrayFloat = valueWrapperArrayFloat;
	}

	public Long[] getValueWrapperArrayLong() {
		return valueWrapperArrayLong;
	}

	public void setValueWrapperArrayLong(Long[] valueWrapperArrayLong) {
		this.valueWrapperArrayLong = valueWrapperArrayLong;
	}

	public Integer[] getValueWrapperArrayInteger() {
		return valueWrapperArrayInteger;
	}

	public void setValueWrapperArrayInteger(Integer[] valueWrapperArrayInteger) {
		this.valueWrapperArrayInteger = valueWrapperArrayInteger;
	}

	public Short[] getValueWrapperArrayShort() {
		return valueWrapperArrayShort;
	}

	public void setValueWrapperArrayShort(Short[] valueWrapperArrayShort) {
		this.valueWrapperArrayShort = valueWrapperArrayShort;
	}

	public Byte[] getValueWrapperArrayByte() {
		return valueWrapperArrayByte;
	}

	public void setValueWrapperArrayByte(Byte[] valueWrapperArrayByte) {
		this.valueWrapperArrayByte = valueWrapperArrayByte;
	}

	public Boolean[] getValueWrapperArrayBoolean() {
		return valueWrapperArrayBoolean;
	}

	public void setValueWrapperArrayBoolean(Boolean[] valueWrapperArrayBoolean) {
		this.valueWrapperArrayBoolean = valueWrapperArrayBoolean;
	}

	public Number getValueWrapperNumber() {
		return valueWrapperNumber;
	}

	public void setValueWrapperNumber(Number valueWrapperNumber) {
		this.valueWrapperNumber = valueWrapperNumber;
	}

	public Number[] getValueWrapperArrayNumber() {
		return valueWrapperArrayNumber;
	}

	public void setValueWrapperArrayNumber(Number[] valueWrapperArrayNumber) {
		this.valueWrapperArrayNumber = valueWrapperArrayNumber;
	}

	public Date getValueWrapperDate() {
		return valueWrapperDate;
	}

	public void setValueWrapperDate(Date valueWrapperDate) {
		this.valueWrapperDate = valueWrapperDate;
	}

	public Date[] getValueWrapperArrayDate() {
		return valueWrapperArrayDate;
	}

	public void setValueWrapperArrayDate(Date[] valueWrapperArrayDate) {
		this.valueWrapperArrayDate = valueWrapperArrayDate;
	}

	public Calendar getValueWrapperCalendar() {
		return valueWrapperCalendar;
	}

	public void setValueWrapperCalendar(Calendar valueWrapperCalendar) {
		this.valueWrapperCalendar = valueWrapperCalendar;
	}

	public Calendar[] getValueWrapperArrayCalendar() {
		return valueWrapperArrayCalendar;
	}

	public void setValueWrapperArrayCalendar(Calendar[] valueWrapperArrayCalendar) {
		this.valueWrapperArrayCalendar = valueWrapperArrayCalendar;
	}
	}
