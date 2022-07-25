package org.intra_mart.common.aid.jdk.util.charset;

import org.intra_mart.common.aid.jdk.util.charset.CharacterSetManager;

import junit.framework.TestCase;

public class CharacterSetManagerTest extends TestCase {

    public static void main(String[] args) {
    }
    
    
    /* (非 Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
		super.setUp();
    }

    /* (非 Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        // TODO 自動生成されたメソッド・スタブ
        super.tearDown();
    }
	
    public void testGetEncodingName() {

    	String encoding_Name = "";
	String result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("",result);

	encoding_Name = "MS932";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("MS932",result);

	encoding_Name = "MS932";
	result = CharacterSetManager.toIANAName(encoding_Name);
	assertEquals("Windows-31J",result);

	encoding_Name = "Windows-31J";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("MS932",result);

	encoding_Name = "Windows-31J";
	result = CharacterSetManager.toIANAName(encoding_Name);
	assertEquals("Windows-31J",result);

	encoding_Name = "csWindows31J";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("MS932",result);

	encoding_Name = "csWindows31J";
	result = CharacterSetManager.toIANAName(encoding_Name);
	assertEquals("csWindows31J",result);


	encoding_Name = "ISO2022JP";
	result = CharacterSetManager.toIANAName(encoding_Name);
	assertEquals("ISO-2022-JP",result);

	encoding_Name = "ISO-2022-JP";
	result = CharacterSetManager.toIANAName(encoding_Name);
	assertEquals("ISO-2022-JP",result);



	encoding_Name = "ISO-2022-JP";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("ISO2022JP",result);
	
	encoding_Name = "ISO2022JP";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("ISO2022JP",result);
	
	encoding_Name = "EUC-JP";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("EUC_JP",result);

	encoding_Name = "Extended_UNIX_Code_Packed_Format_for_Japanese";
	result = CharacterSetManager.toJDKName(encoding_Name);
	assertEquals("EUC_JP",result);
    }
    
}
