package org.intra_mart.common.aid.jdk.java.io;

import java.io.File;
import java.util.GregorianCalendar;

import org.intra_mart.common.aid.jdk.java.io.FileUtil;

import junit.framework.TestCase;

public class FileUtilTest extends TestCase {

	private static String _tmpDirPath = System.getProperty("java.io.tmpdir"); 	
	private static String _timeStampFileName = "time_stamp_file.txt";

	public static void main(String[] args) {
        junit.textui.TestRunner.run(FileUtilTest.class);
    }

	protected void setUp() throws Exception {
    	(new File(_tmpDirPath, _timeStampFileName)).delete();
	}

	protected void tearDown() throws Exception {
    	(new File(_tmpDirPath, _timeStampFileName)).delete();
	}

	/**
	 * @TestCaseTitle 			指定ディレクトリ直下のファイル群の中で一番古い更新日付を取得<BR>
	 * @TestCasePreCondition	存在しないディレクトリを指定<BR>
	 * @TestCasePostCondition	現在日付が返却されること<BR>
	 * @TestCaseType 			正常系<BR>
	 * @TestCaseMemo 			誤差、100ms<BR>
	 */
    public void testGetOldestLastModifiedInDir_001() throws Exception {
    	long now = System.currentTimeMillis();
    	long toleranceMaxTime = now + 100;
    	long targetTime = FileUtil.getOldestLastModifiedInDir(new File(""));
    	
    	assertTrue(now <= targetTime);
    	assertTrue(targetTime <= toleranceMaxTime);
    }

	/**
	 * @TestCaseTitle 			指定ディレクトリ直下のファイル群の中で一番古い更新日付を取得<BR>
	 * @TestCasePreCondition	ファイルを指定<BR>
	 * @TestCasePostCondition	現在日付が返却されること<BR>
	 * @TestCaseType 			正常系<BR>
	 * @TestCaseMemo 			誤差、100ms<BR>
	 */
    public void testGetOldestLastModifiedInDir_002() throws Exception{
		long DUMMY = (new GregorianCalendar(1971, 0, 1, 01, 23, 45)).getTimeInMillis();

    	File file = new File(_tmpDirPath, _timeStampFileName); 
    	file.createNewFile();
    	file.setLastModified(DUMMY);

    	assertTrue(file.exists());

    	long now = System.currentTimeMillis();
    	long toleranceMaxTime = now + 100;
    	long targetTime = FileUtil.getOldestLastModifiedInDir(file); //←ファイルを指定
    	
    	assertTrue(now <= targetTime);
    	assertTrue(targetTime <= toleranceMaxTime);
    }

    /**
	 * @TestCaseTitle 			指定ディレクトリ直下のファイル群の中で一番古い更新日付を取得<BR>
	 * @TestCasePreCondition	ディレクトリを指定<BR>
	 * @TestCasePostCondition	指定ディレクトリ直下のファイル群の中で一番古い更新日付が返却されること<BR>
	 * @TestCaseType 			正常系<BR>
	 * @TestCaseMemo 			なし<BR>
	 */
    public void testGetOldestLastModifiedInDir_003() throws Exception {

		long EXPECTED = (new GregorianCalendar(1971, 0, 1, 01, 23, 45)).getTimeInMillis();

    	File file = new File(_tmpDirPath, _timeStampFileName);
    	file.createNewFile();
    	file.setLastModified(EXPECTED);
    	
    	assertEquals(EXPECTED, FileUtil.getOldestLastModifiedInDir(new File( _tmpDirPath )));
    }
}
