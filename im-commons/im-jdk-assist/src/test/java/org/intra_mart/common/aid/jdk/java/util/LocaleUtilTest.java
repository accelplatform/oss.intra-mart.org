package org.intra_mart.common.aid.jdk.java.util;

import java.util.Locale;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * LocaleUtilのテストクラス。<BR>
 * <BR>
 * LocaleUtilの各種メソッドのテストを行います。<BR>
 */
public class LocaleUtilTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(LocaleUtilTest.class);
    }

    public void testToLocale() {
 
        Assert.assertEquals(LocaleUtil.toLocale(null),null);

        Assert.assertEquals(LocaleUtil.toLocale(""),null);

        Assert.assertEquals(LocaleUtil.toLocale("ja"),new Locale("ja"));
        
        Assert.assertEquals(LocaleUtil.toLocale("ja_JP"),new Locale("ja","JP"));

        Assert.assertEquals(LocaleUtil.toLocale("ja_JP_kansai"),new Locale("ja","JP","kansai"));

        Assert.assertEquals(LocaleUtil.toLocale("ja_JP_kansai_ohsaka"),new Locale("ja","JP","kansai_ohsaka"));

        Assert.assertEquals(LocaleUtil.toLocale("en"),new Locale("en"));

        Assert.assertEquals(LocaleUtil.toLocale("en_US"),new Locale("en","US"));

    }

}
