package org.intra_mart.common.aid.jdk.java.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import junit.framework.TestCase;


public class ArchiverTest extends TestCase {
	
	private static final String NON_CONVERT_CHARSET = "ISO-8859-1";
	private static String tempDir = System.getProperty("java.io.tmpdir");
	private static String fileSep = System.getProperty("file.separator");

	private static boolean firstFlg = true;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if(firstFlg){
			firstFlg = false;

			// Mavenでは空のディレクトリが target/test-classes に作成されない。。。
			URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/");
			File empty_dir = new File(getDecodePath(url), "empty_dir");
			empty_dir.mkdirs();
			
			File foo_empty_dir = new File(getDecodePath(url), "foo/empty_dir");
			foo_empty_dir.mkdirs();
	
			File foo_foo2_empty_dir = new File(getDecodePath(url), "foo/foo2/empty_dir");
			foo_foo2_empty_dir.mkdirs();
			

			// Mavenでは空のディレクトリが target/test-classes に作成されない。。。その２
			url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/003_for_FileArray/");
			foo_empty_dir = new File(getDecodePath(url), "foo/empty_dir");
			foo_empty_dir.mkdirs();

			foo_foo2_empty_dir = new File(getDecodePath(url), "foo/foo2/empty_dir");
			foo_foo2_empty_dir.mkdirs();
			
		}
		
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void test_zipStringStringFile_srcがnull() {
		byte[] binary = null;
		String fileName = null;
		File dest = null;
		
		try{
			Archiver.zip(binary, fileName, dest);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
		}
	}


	/**
	 * 
	 */
	public void test_zipStringStringFile_srcがString_空文字() throws Exception {
		byte[] binary = "".getBytes(NON_CONVERT_CHARSET);
		String fileName = null;
		File dest = null;

		try{
			Archiver.zip(binary, fileName, dest);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
		}
	}

	/**
	 * @throws Exception
	 */
	public void test_zipStringStringFile_srcがString_54Bytes() throws Exception {
		File temp = new File(tempDir, "test_zipStringStringFile_srcがString_54Bytes/");
		String src = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");

		try{
			byte[] binary = src.getBytes(NON_CONVERT_CHARSET);
			String fileName = "001_plain_text.txt";
			File dest = new File(temp, "001_plain_text.zip");
	
			Archiver.zip(binary, fileName, dest);

			
			// 確認
			File unzipSrc = dest;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			String target;
			String result;
			String origin;
			
			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = src;
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		}
		finally{
			deleteDir(temp);
		}
	}

	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_zipStringStringFile_srcがString_10MB() throws Exception {
//		fail("未実装");
//	}

	/**
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcが存在しないファイル() throws Exception {
		File src = new File(tempDir + fileSep + "sonzai_shinai" + fileSep + "file_deth.txt");
		File dest = new File(tempDir);
		FileFilter filter = null;
		
		try{
			Archiver.zip(src, dest, filter);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause() instanceof FileNotFoundException);
		}
	}

	/**
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcが0KBのファイル() throws Exception {
		File temp = new File(tempDir, "test_zipFileFileFileFilter_srcがFileAccessObject_0KBのファイル");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/000_this_file_is_0KB.txt");

		try{
	
			File src = new File(getDecodePath(url));
			File dest = new File(temp, "000_this_file_is_0KB.zip");
			FileFilter filter = null;
	
			Archiver.zip(src, dest, filter);
			
			
			// 確認
			File unzipSrc = dest;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(src);
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

		}
		finally{
			deleteDir(temp);
		}

	}

	/**
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcが54Bytesのファイル() throws Exception {
		File temp = new File(tempDir, "test_zipFileFileFileFilter_srcがFileAccessObject_54Bytesのファイル");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");

		try{
	
			File src = new File(getDecodePath(url));
			File dest = new File(temp, "001_plain_text.zip");
			FileFilter filter = null;
	
			Archiver.zip(src, dest, filter);
			
			
			// 確認
			File unzipSrc = dest;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			String target;
			String result;
			String origin;
			
			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(src);
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

		}
		finally{
			deleteDir(temp);
		}		
	}
	
	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_zipFileFileFileFilter_srcが10MBのファイル() throws Exception {
//		fail("未実装");
//	}
	
	/**
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcが存在しないディレクトリ() throws Exception {
		File src = new File(tempDir + fileSep + "sonzai_shinai" + fileSep + "dir_deth" + fileSep);
		File dest = new File(tempDir);;
		FileFilter filter = null;
		
		try{
			Archiver.zip(src, dest, filter);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause() instanceof FileNotFoundException);
		}
		
	}
	
	
	/**
	 * Antのメッセージ
	 * [zip] Warning: skipping zip archive /foo/bar/target/000_empty_dir.zip because no files were included.
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcが空のディレクトリ() throws Exception {
		File temp = new File(tempDir, "test_zipFileFileFileFilter_srcが空のディレクトリ");
		temp.mkdirs();
		
		File emptyDir = new File(tempDir, "test_zip_srcがFileAccessObject_空のディレクトリ");
		emptyDir.mkdirs();
		
		try{
			File src = emptyDir;
			File dest = File.createTempFile("サンプル", ".txt", temp);
			save(dest, "abcde");
			
			FileFilter filter = null;
			
			Archiver.zip(src, dest, filter);
			
			// 確認			
			assertEquals("空のディレクトリを圧縮した場合、destのファイルは変更されない事", "abcde", load(dest));

		}
		finally{
			deleteDir(temp);
			deleteDir(emptyDir);
		}				
	}

	/**
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcがディレクトリ_1階層() throws Exception {

		File temp = new File(tempDir, "test_zipFileFileFileFilter_srcがディレクトリ_1階層");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/");

		try{
	
			File src = new File(getDecodePath(url));
			File dest = new File(temp, "001_1_hierarchy.zip");
			FileFilter filter = null;
	
			Archiver.zip(src, dest, filter);
			
			
			// 確認
			File unzipSrc = dest;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			String target;
			String result;
			String origin;
			
			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		}
		finally{
			deleteDir(temp);
		}		
	}

	/**
	 * @throws Exception
	 */
	public void test_zipFileFileFileFilter_srcがディレクトリ_3階層() throws Exception {
		File temp = new File(tempDir, "test_zipFileFileFileFilter_srcがディレクトリ_3階層");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/");

		try{
	
			File src = new File(getDecodePath(url));
			File dest = new File(temp, "002_3_hierarchy.zip");
			FileFilter filter = null;
	
			Archiver.zip(src, dest, filter);
			
			
			// 確認
			File unzipSrc = dest;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			// ============ bar ============ 
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			
			// ============ empty_dir ============ 
			target = "empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(unzipDest, target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src, target).list().length);
			
			// ============ bar ============ 
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			
			// ============ foo ============ 
			target = "foo/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		
			// ============ foo/empty_dir ============ 
			target = "foo/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(unzipDest, target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src, target).list().length);

			// ============ foo/foo2 ============ 
			target = "foo/foo2/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);


			// ============ foo/foo2/empty_dir ============ 
			target = "foo/foo2/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(unzipDest, target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src, target).list().length);
		}
		finally{
			deleteDir(temp);
		}

	}
	
	/**
	 * @throws Exception
	 */
	public void test_zip_filterの動作() throws Exception {
		File temp = new File(tempDir, "test_zipFileFileFileFilter_srcがディレクトリ_3階層");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/");

		try{
	
			File src = new File(getDecodePath(url));
			File dest = new File(temp, "002_3_hierarchy.zip");;
			FileFilter filter = new ArchiverFileFilter4OnlyTextFile();
	
			Archiver.zip(src, dest, filter);

			// 確認
			File unzipSrc = dest;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());

			target = "oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());
			
			target = "empty_dir";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());

			
			// ============= bar =============
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());

			target = "bar/oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());
			
			
			// ============= foo =============
			target = "foo/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());

			target = "foo/oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());

			target = "foo/empty_dir";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());

			
			// ============= foo/foo2 =============
			target = "foo/foo2/000_this_file_is_0KB.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());

			target = "foo/foo2/oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src, target).exists());

			target = "foo/foo2/empty_dir";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).exists());

		}
		finally{
			deleteDir(temp);
		}		
		
	}
	
	/**
	 * @throws Exception
	 */
	public void test_zipFile配列FileFileFilter() throws Exception {
		File temp = new File(tempDir, "test_zipFile配列FileFileFilter");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/003_for_FileArray/");
		System.out.println("getDecodePath(url): " + getDecodePath(url));

		try{
			File src_0 = new File(getDecodePath(url), "oim_banner01.gif");             // ファイル指定
			File src_1 = new File(getDecodePath(url), "bar/001_plain_text.txt");       // ファイル指定（サブディレクトリ）
			File src_2 = new File(getDecodePath(url), "bar/000_this_file_is_0KB.txt"); // ファイル指定（filterで弾かれるファイル）
			File src_3 = new File(getDecodePath(url), "foo/");                         // ディレクトリ指定
			
			File[] sources = {src_0, src_1, src_2, src_3};
			File destination = new File(temp, "test_zipFile配列FileFileFilter.zip");
			System.out.println("destination: " + destination);
			
			FileFilter filter = new FileFilter(){
				public boolean accept(File target) {
					// ファイル名に「000_」が含まれるファイルは圧縮しない
					if(target.getName().indexOf("000_") == -1){
						return true;
					}
					else{
						return false;
					}
				}
			};
	
			Archiver.zip(sources, destination, filter);

			// 確認
			File unzipSrc = destination;
			File unzipDest = new File(temp, "/target");
			Archiver.unzip(unzipSrc, unzipDest); // unzipが正しく動作していることが前提
			
			File targetDir;
			String target;
			String result;
			String origin;
			
			targetDir = unzipDest;
			assertEquals("「ルート/」のファイル数が正しいこと", 7, targetDir.listFiles().length); 

			target = "oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(src_0);
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(src_1);
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
			// ↑サブディレクトリ「bar」の下に作られないことに注意

			target = "copy_of_001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src_3, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
			
			target = "copy_of_im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src_3, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
			
			target = "copy_of_oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src_3, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin); 
			
			// ============ empty_dir ============ ← 「foo/empty_dir/」ではなく「empty_dir/」として圧縮されます。
			target = "empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(unzipDest, target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src_3, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src_3, target).list().length);
			
			
			// ============= foo2 =============　← 「foo/foo2/」ではなく「foo2/」として圧縮されます。
			targetDir = new File(unzipDest, "foo2");
			assertEquals("「foo2/」のファイル数が正しいこと", 4, targetDir.listFiles().length); 
			
			target = "foo2/001_plain_text.txt";
			result = load(new File(unzipDest, target));
			origin = load(new File(src_3, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo2/im-jssp-api-list-0.1.2.zip";
			result = load(new File(unzipDest, target));
			origin = load(new File(src_3, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo2/oim_banner01.gif";
			result = load(new File(unzipDest, target));
			origin = load(new File(src_3, target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			// ============ foo2/empty_dir ============ ← 「foo/foo2/empty_dir/」ではなく「foo2/empty_dir/」として圧縮されます。
			target = "foo2/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(unzipDest, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(unzipDest, target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src_3, target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src_3, target).list().length);

		}
		finally{
			deleteDir(temp);
		}		
	}
	
	public void test_zipFile配列FileFileFilter_ZipException_duplicate_entry発生() throws Exception {
		File temp = new File(tempDir, "test_zipFile配列FileFileFilter_ZipException_duplicate_entry発生");
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/");

		try{
			File src_0 = new File(getDecodePath(url), "oim_banner01.gif"); // ファイル指定
			File src_1 = new File(getDecodePath(url), "foo/");             // ディレクトリ指定（この直下に上記と同名ファイルがある）
			
			File[] sources = {src_0, src_1};
			File destination = new File(temp, "test_zipFile配列FileFileFilter_ZipException_duplicate_entry発生.zip");
			
			try{
				Archiver.zip(sources, destination, null);
				fail("例外が発生する必要があります");
			}
			catch(Exception e){
				assertTrue("ZipExceptionが発生する事", e instanceof ZipException);
			}
		}
		finally{
			deleteDir(temp);
		}		
	}

	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcがnull() {
		File src  = null;
		File dest = null;
		
		try{
			Archiver.unzip(src, dest);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
		}
	}
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_destがnull() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.zip");
		
		File src  = new File(getDecodePath(url));
		File dest = null;
		
		try{
			Archiver.unzip(src, dest);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
		}
	}	

	
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcが存在しないファイル() throws Exception {
		File src  = new File(tempDir + fileSep + "sonzai_shinai" + fileSep + "file_deth");
		File dest = new File(tempDir);;
		
		try{
			Archiver.unzip(src, dest);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("IllegalArgumentExceptionが発生する事", e instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause() instanceof FileNotFoundException);
		}
	}
	
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcがZip形式ではない() throws Exception {
		File dest = null;
		try{
			URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");
	
			File src  = new File(getDecodePath(url));
			dest = new File(tempDir, "test_unzipFileFile_srcがZip形式ではない");
			assertFalse("前提：出力先ディレクトリが存在しない事", dest.exists());
			
			try{
				Archiver.unzip(src, dest);
				fail("例外が発生する必要があります");
			}
			catch(Exception e){
				assertTrue("ZipExceptionが発生する事", e instanceof ZipException);
			}
	
			assertTrue("出力先ディレクトリが存在する事", dest.exists());
			assertTrue("出力先がディレクトリである事", dest.isDirectory());
			assertEquals("出力先ディレクトリが空である事", 0, dest.list().length);
		}
		finally{
			if(dest != null){
				dest.delete();
			}
		}
	}
	
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcが0KBのZIPファイル() throws Exception {
		File dest = null;
		try{
			URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/002_this_zipfile_is_0KB.zip");
	
			File src  = new File(getDecodePath(url));
			dest = new File("target/test/test_unzipFileFile_srcが0KBのZIPファイル/");
			assertFalse("前提：出力先ディレクトリが存在しない事", dest.exists());
			
			try{
				Archiver.unzip(src, dest);
				fail("例外が発生する必要があります");
			}
			catch(Exception e){
				assertTrue("ZipExceptionが発生する事", e instanceof ZipException);
			}
	
			assertTrue("出力先ディレクトリが存在する事", dest.exists());
			assertTrue("出力先がディレクトリである事", dest.isDirectory());
			assertEquals("出力先ディレクトリが空である事", 0, dest.list().length);
		}
		finally{
			if(dest != null){
				dest.delete();
			}
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcが001_plain_text_zip() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.zip");

		File src  = new File(getDecodePath(url));
		File dest = new File("target/test/test_unzipFileFile_srcが001_plain_text_zip/");
		
		Archiver.unzip(src, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");
		String actual = load(new File(dest, "001_plain_text.txt"));
		assertEquals("ZIP解凍後の内容が正しい事", expected, actual);
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzipInputStreamFile_srcが001_plain_text_zip() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.zip");

		File src  = new File(getDecodePath(url));
		InputStream is = new FileInputStream(src);
		File dest = new File("target/test/test_unzipFileFile_srcが001_plain_text_zip/");
		
		Archiver.unzip(is, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");
		String actual = load(new File(dest, "001_plain_text.txt"));
		assertEquals("ZIP解凍後の内容が正しい事", expected, actual);
	}	

	/**
	 * @throws Exception
	 */
	public void test_unzipInputStreamFile_srcが001_plain_text_zip_ZipInputStreamを利用した場合() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.zip");

		File src  = new File(getDecodePath(url));
		InputStream is = new FileInputStream(src);
		ZipInputStream zis = new ZipInputStream(is);
		File dest = new File("target/test/test_unzipFileFile_srcが001_plain_text_zip/");
		
		Archiver.unzip(zis, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");
		String actual = load(new File(dest, "001_plain_text.txt"));
		assertEquals("ZIP解凍後の内容が正しい事", expected, actual);
	}	


	/**
	 * @throws Exception
	 */
	public void test_unzipByteFile_srcが001_plain_text_zip() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.zip");

		File src  = new File(getDecodePath(url));
		File dest = new File("target/test/test_unzipFileFile_srcが001_plain_text_zip/");
		
		int n;
		byte[] buf = new byte[1024];
		InputStream in = new FileInputStream(src);
		OutputStream out = new ByteArrayOutputStream();
		try{
			while((n = in.read(buf, 0, buf.length)) != -1){
				out.write(buf, 0, n);
			}
		} finally{
			in.close();
			out.close();
		}

		ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
		Archiver.unzip(baos.toByteArray(), dest);
		
		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/001_plain_text.txt");
		String actual = load(new File(dest, "001_plain_text.txt"));
		assertEquals("ZIP解凍後の内容が正しい事", expected, actual);
	}	

	
	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_unzipFileFile_srcが10MBのファイル() throws Exception {
//		fail("未実装");
//	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcが1階層ディレクトリのZIP_destが既存のディレクトリ() throws Exception {

		String destBaseDir = "target/test/test_unzipFileFile_srcが1階層ディレクトリのZIP_destが既存のディレクトリ";

		// 準備（開始）
		URL url4pre = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy");
		copyDir(new File(getDecodePath(url4pre)), new File(destBaseDir));
		// 準備（終了）
		
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy.zip");

		File src  = new File(getDecodePath(url));
		File dest = new File(destBaseDir + "/foo");
		
		Archiver.unzip(src, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String target;
		String expected;
		String actual;
		
		target = "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
	}
	
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcがsrcが3階層ディレクトリのZIP_destが既存のファイル() throws Exception {
		// ファイル削除後、新しいディレクトリを作成する。
		// （拡張子が付いていたりしてファイルっぽいFileオブジェクトでも強制的にディレクトリとして扱いますよ）
		
		String destBaseDir = "target/test/test_unzipFileFile_srcがsrcが3階層ディレクトリのZIP_destが既存のファイル";
		
		// 準備（開始）
		if(new File(destBaseDir).exists()){
			deleteDir(new File(destBaseDir));
		}
		URL url4pre = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy");
		copyDir(new File(getDecodePath(url4pre)), new File(destBaseDir));
		// 準備（終了）

		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy.zip");
		File src  = new File(getDecodePath(url));
		File dest = new File(destBaseDir + "/foo/001_plain_text.txt");
		
		Archiver.unzip(src, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String target;
		String expected;
		String actual;
		
		target =  "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		//================= bar =================
		String dirName4bar = "bar/";
		target =  dirName4bar;
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());

		target =  dirName4bar + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  dirName4bar + "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  dirName4bar + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  dirName4bar + "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		
		//================= empty_dir =================
		target =  "empty_dir";
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", 0, new File(dest, target).list().length);
	
		
		//================= foo =================
		String dirName4foo = "foo/";
		target =  dirName4foo;
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());

		target =  dirName4foo + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  dirName4foo + "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  dirName4foo + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  dirName4foo + "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		
		//================= foo/empty_dir =================
		target =  "foo/empty_dir";
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", 0, new File(dest, target).list().length);

		
		//================= foo/foo2 =================
		String dirName4foo2 = "foo/foo2/";
		target =  dirName4foo2;
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		
		target =  dirName4foo2 + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  dirName4foo2 + "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  dirName4foo2 + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  dirName4foo2 + "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

	
		//================= foo/foo2/empty_dir =================
		target =  "foo/foo2/empty_dir";
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", 0, new File(dest, target).list().length);
	}	

	
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcが3階層ディレクトリのZIP_destが存在しないディレクトリ_親ディレクトリ無し() throws Exception {
		
		String destBaseDir = "target/test/test_unzipFileFile_srcがsrcが3階層ディレクトリのZIP_destが存在しないディレクトリ_親ディレクトリ無し";

		// 準備（開始）
		if(new File(destBaseDir).exists()){
			deleteDir(new File(destBaseDir));
		}
		// 準備（終了）

		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy.zip");
		File src  = new File(getDecodePath(url));
		File dest = new File(destBaseDir + "/foo/bar");
		
		Archiver.unzip(src, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String target;
		String expected;
		String actual;
		
		target =  "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		//================= bar =================
		String dirName4bar = "bar/";
		target =  dirName4bar;
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());

		target =  dirName4bar + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  dirName4bar + "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  dirName4bar + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  dirName4bar + "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		
		//================= empty_dir =================
		target =  "empty_dir";
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", 0, new File(dest, target).list().length);
	
		
		//================= foo =================
		String dirName4foo = "foo/";
		target =  dirName4foo;
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());

		target =  dirName4foo + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  dirName4foo + "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  dirName4foo + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  dirName4foo + "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		
		//================= foo/empty_dir =================
		target =  "foo/empty_dir";
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", 0, new File(dest, target).list().length);

		
		//================= foo/foo2 =================
		String dirName4foo2 = "foo/foo2/";
		target =  dirName4foo2;
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		
		target =  dirName4foo2 + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  dirName4foo2 + "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  dirName4foo2 + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  dirName4foo2 + "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/002_3_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

	
		//================= foo/foo2/empty_dir =================
		target =  "foo/foo2/empty_dir";
		assertTrue("ZIP解凍後の内容が正しい事(" + target + ")", new File(dest, target).isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", 0, new File(dest, target).list().length);
		
	}
	
	
	/**
	 * @throws Exception
	 */
	public void test_unzipFileFile_srcが1階層ディレクトリのZIP_destが存在しないディレクトリ_親ディレクトリ有り() throws Exception {
		String destBaseDir = "target/test/test_unzipFileFile_srcが1階層ディレクトリのZIP_destが存在しないディレクトリ_親ディレクトリ有り";

		// 準備（開始）
		if(new File(destBaseDir).exists() == false){
			new File(destBaseDir).mkdirs();
		}
		// 準備（終了）

		
		URL url = this.getClass().getResource("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy.zip");

		File src  = new File(getDecodePath(url));
		File dest = new File(destBaseDir + "/foo/bar");
		
		Archiver.unzip(src, dest);

		assertTrue("出力先ディレクトリが存在する事", dest.exists());
		assertTrue("出力先がディレクトリである事", dest.isDirectory());

		String target;
		String expected;
		String actual;
		
		target = "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);

		target =  "001_plain_text.txt";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
		
		target =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	
		target =  "oim_banner01.gif";
		expected = load("/org/intra_mart/common/aid/jdk/java/util/ArchiverTest/dir_test/001_1_hierarchy/" + target);
		actual = load(new File(dest, target));
		assertEquals("ZIP解凍後の内容が正しい事(" + target + ")", expected, actual);
	}
	
	/**
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	private String load(String resourceName) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(resourceName);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		int i;
		while ((i = is.read()) != -1) {
			os.write(i);
		}

		is.close();
		os.close();

		String src = new String(os.toByteArray(), "8859_1");
		return src;
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static String load(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		int iFSize = (int) file.length();
		byte[] aBuf = new byte[iFSize];
		fis.read(aBuf);
		fis.close();
		return new String(aBuf, "ISO-8859-1");
	}
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static void save(File file, String strm) throws IOException {
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(strm.getBytes("ISO-8859-1"));
		fos.flush();
		fos.close();
		return;
	}

	
	/**
	 * @param srcDir
	 * @param destDir
	 * @throws IOException
	 */
	public static void copyDir(File srcDir, File destDir) throws IOException {
		
		if(srcDir.exists() == false || srcDir.isDirectory() == false){
			throw new IllegalArgumentException();
		}
		
		if(destDir.exists()){
			deleteDir(destDir);
		}
		
		destDir.mkdirs();
		
		for(String name : srcDir.list()){
			File child4src = new File(srcDir, name);
			File child4dest = new File(destDir, name);
			
			if(child4src.isDirectory()){
				if(child4src.getName().equals(".svn")){
					continue;
				}
				
				child4dest.mkdirs();
				copyDir(child4src, child4dest); // 再帰！
			}
			else{
				copyFile(child4src, child4dest);
			}
		}
		
	}

	/**
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		
		java.nio.channels.FileChannel sourceChannel      = new java.io.FileInputStream (srcFile) .getChannel();
		java.nio.channels.FileChannel destinationChannel = new java.io.FileOutputStream(destFile).getChannel();
		try{
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		}
		finally{
			sourceChannel.close();
			destinationChannel.close();		
		}
	}
	
	/**
	 * @param targetDir
	 */
	public static void deleteDir(File targetDir) throws IOException {
		if(targetDir.list() != null){
			for(String name : targetDir.list()){
				File child = new File(targetDir, name);
				
				if(child.isDirectory()){
					deleteDir(child); // 再帰！
				}
				else{
					boolean result = child.delete();
					if(!result){
						throw new IllegalStateException("Deletion failure: " + child.getAbsolutePath());
					}
				}
			}
		}
		
		targetDir.delete();
	}

	/**
	 * URLデコードされたpathを取得します.
	 * 
	 * @param url url
	 * @return URLデコードされたpath.
	 * @throws IOException
	 */
	private String getDecodePath(URL url) throws IOException {
		return URLDecoder.decode(url.getPath(), "UTF-8");
	}

}
