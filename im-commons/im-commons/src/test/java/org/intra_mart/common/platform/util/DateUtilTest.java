package org.intra_mart.common.platform.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.intra_mart.common.platform.util.DateUtil;

import junit.framework.TestCase;

/**
 * タイトル。<BR>
 * <BR>
 * 説明<BR>
 */

public class DateUtilTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DateUtilTest.class);
    }

    public void testDateToString() {
        
        GregorianCalendar calendar = new GregorianCalendar(2004,0,12,13,30,45);
        
        String dateString = DateUtil.dateToString(calendar.getTime());
        
        assertEquals("2004/01/12|13:30:45",dateString);
        
    }

    public void testStringToDate() {
        
        Date date = DateUtil.stringToDate("2004/01/12|13:30:45");
        
        
        GregorianCalendar calendar = new GregorianCalendar(2004,0,12,13,30,45);
        
        assertEquals(calendar.getTime(),date);

    }

    /*
     * Date convertOnlyDate のテスト中のクラス(long)
     */
    public void testConvertOnlyDatelong() {
        GregorianCalendar calendar = new GregorianCalendar(2004,0,12,13,30,45);
        calendar.set(Calendar.MILLISECOND,50);
        Date date = calendar.getTime();
        
        calendar = new GregorianCalendar(2004,0,12,0,0,0);
        
        assertEquals(calendar.getTime(),DateUtil.convertOnlyDate(date.getTime()));

        assertEquals(date.getTime()-(13*3600000)-(30*60000)-(45*1000)-50 ,DateUtil.convertOnlyDate(date.getTime()).getTime());
    }

    /*
     * Date convertOnlyDate のテスト中のクラス(Date)
     */
    public void testConvertOnlyDateDate() {
        GregorianCalendar calendar = new GregorianCalendar(2004,0,12,13,30,45);
        calendar.set(Calendar.MILLISECOND,50);
        Date date = calendar.getTime();
        
        calendar = new GregorianCalendar(2004,0,12,0,0,0);
        
        assertEquals(calendar.getTime(),DateUtil.convertOnlyDate(date));

        assertEquals(date.getTime()-(13*3600000)-(30*60000)-(45*1000)-50 ,DateUtil.convertOnlyDate(date).getTime());
    }

    /*
     * Date convertOnlyDateTime のテスト中のクラス(long)
     */
    public void testConvertOnlyDateTimelong() {
        GregorianCalendar calendar = new GregorianCalendar(2004,0,12,13,30,45);
        calendar.set(Calendar.MILLISECOND,50);
        Date date = calendar.getTime();
        
        calendar = new GregorianCalendar(2004,0,12,13,30,45);
        
        assertEquals(calendar.getTime(),DateUtil.convertOnlyDateTime(date.getTime()));

        assertEquals(date.getTime() - 50,DateUtil.convertOnlyDateTime(date.getTime()).getTime());
    }

    /*
     * Date convertOnlyDateTime のテスト中のクラス(Date)
     */
    public void testConvertOnlyDateTimeDate() {
        GregorianCalendar calendar = new GregorianCalendar(2004,0,12,13,30,45);
        calendar.set(Calendar.MILLISECOND,50);
        Date date = calendar.getTime();
        
        calendar = new GregorianCalendar(2004,0,12,13,30,45);
        
        assertEquals(calendar.getTime(),DateUtil.convertOnlyDateTime(date));

        assertEquals(date.getTime() - 50,DateUtil.convertOnlyDateTime(date).getTime());
    }

}
