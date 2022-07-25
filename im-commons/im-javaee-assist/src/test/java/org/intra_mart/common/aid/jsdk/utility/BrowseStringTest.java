package org.intra_mart.common.aid.jsdk.utility;

import junit.framework.TestCase;

public class BrowseStringTest extends TestCase {
	
    public void testConvert_nullを指定した場合() throws Exception {
    	
    	try{
    		BrowseString.convert(null);    		
    	}
    	catch(Exception e){
    		assertTrue(e instanceof NullPointerException);
    	}

    }

    public void testConvert_ASCII文字列を指定した場合() throws Exception {
		String testString = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
		assertEquals("&nbsp;!&quot;#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~",
				     BrowseString.convert(testString));
    }
    
    public void testConvert_改行コードがCRLFの場合() throws Exception {
    	String testString = "\r\n";
    	assertEquals("<BR>", BrowseString.convert(testString));
    }

    public void testConvert_改行コードがLFの場合() throws Exception {
	    String testString = "\n";
		assertEquals("<BR>", BrowseString.convert(testString));
    }
    
    public void testConvert_改行コードがCRの場合() throws Exception {
		String testString = "\r";
		assertEquals("<BR>", BrowseString.convert(testString));
    }

    public void testConvert_改行コードがLFCR通常はありえないの場合() throws Exception {
		String testString = "\n\r";
		assertEquals("<BR><BR>", BrowseString.convert(testString));
    }

    public void testConvert_2バイト文字の場合() throws Exception {
	    String testString = "日本語";
	    assertEquals("日本語", BrowseString.convert(testString));
    }

    public void testConvert_複合パターン() throws Exception {
	    String testString = "日本語" + "\r" + 
							" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" + "\r\n" + 
							" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" + "\n" + 
							" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" + "\r\n";

		assertEquals("日本語" + "<BR>" +
					 "&nbsp;!&quot;#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" + "<BR>" + 
					 "&nbsp;!&quot;#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" + "<BR>" +
					 "&nbsp;!&quot;#$%&amp;'()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~" + "<BR>",
					 BrowseString.convert(testString));
    }


}
