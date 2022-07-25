package org.intra_mart.common.aid.jsdk.utility;

/**
 * HTML表示用文字列変換ユーティリティクラス。<BR>
 * <BR>
 * HTML表示用に文字列の変換を行うクラスです。<BR>
 * 
 */

public class BrowseString {

    /**
     * コンストラクタ。<BR>
     * <BR>
     * 
     */
    private BrowseString() {
        super();
    }
    
    /**
     * HTML表示可能な文字列に変換します。<br>
     * <br> 
     * 「&gt;」、「&lt;」、「&quot;」、「&amp;」、「半角スペース」、「改行コード」をHTML表示可能な文字に変換します。<br>
     * 改行コードは「&lt;BR&gt;」に変換します。
     * <br>
     * @param string 変換元文字列
     * @return 変換された文字列
     */
    public static String convert(String string) {
        return 	string.replaceAll("&", "&amp;").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("\\r\\n", "<BR>").replaceAll("\\r|\\n", "<BR>");
    }

}
