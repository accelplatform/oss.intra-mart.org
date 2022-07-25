package org.intra_mart.jssp.script.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

import junit.framework.TestCase;

import org.intra_mart.common.platform.log.Logger;
import org.intra_mart.jssp.page.JSSPInitializer;
import org.intra_mart.jssp.util.ValueObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.WrappedException;

//TODO ファイルの書き込み権限チェックに引っかかるテストが今のところありません
/**
 * JsUnit利用のテストケースも以下に存在します。
 * http://oss.intra-mart.org/projects/im-jssp/svn/trunk/im-jssp/src/test/js/jssp/script/api/ArchiverTest.js
 */
public class ArchiverObjectTest extends TestCase {
	
	private static Logger logger = Logger.getLogger();

	private static String tempDir = System.getProperty("java.io.tmpdir");
	private static String fileSep = System.getProperty("file.separator");
	
	private static boolean firstFlg = true;

	static {
		JSSPInitializer.init(".", "/org/intra_mart/jssp/script/api/ArchiverObjectTest/jssp-config-ArchiverObjectTest.xml");
	}
	
	private Context cxMadeInSetup;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		cxMadeInSetup = Context.enter();
		
		if(firstFlg){
			firstFlg = false;

			// Mavenでは空のディレクトリが target/test-classes に作成されない。。。
			URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/");
			File empty_dir = new File(getDecodePath(url), "empty_dir");
			empty_dir.mkdirs();
			
			File foo_empty_dir = new File(getDecodePath(url), "foo/empty_dir");
			foo_empty_dir.mkdirs();
	
			File foo_foo2_empty_dir = new File(getDecodePath(url), "foo/foo2/empty_dir");
			foo_foo2_empty_dir.mkdirs();
		}
	}

	
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		Context.exit();
		cxMadeInSetup = null;
	}



	/**
	 * 
	 */
	public void testGetClassName() {
		ArchiverObject archiver = new ArchiverObject();
		assertEquals("Archiver", archiver.getClassName());
	}

	/**
	 * 
	 */
	public void test_zip_srcがnull() {
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, null);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}

	/**
	 * 
	 */
	public void test_zip_srcがUndefined() {
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, Undefined.instance);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}

	/**
	 * 
	 */
	public void test_zip_srcプロパティがない() {
		ValueObject voIn = new ValueObject();
		voIn.put("srcs", voIn, null);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}

	/**
	 * 
	 */
	public void test_zip_srcがString_空文字() {
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, "");
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcがString_54Bytes() throws Exception {
		String src = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.txt");

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("srcFileName", voIn, "001_plain_text.txt");
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がStringであること", actual instanceof String);
		
		// String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");
		// assertTrue("ZIP圧縮の内容が正しい事", expected.equals(actual)); // assertEquals()だとテスト結果表示に時間がかかる（∵ファイルの内容をメモリ上に展開する必要があるためか？）
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcがString_54Bytes");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, (String)actual);
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
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
//	public void test_zip_srcがString_10MB() throws Exception {
//		fail("未実装");
//	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_存在しないファイル() throws Exception {
		Object[] args4src = { tempDir + fileSep + "sonzai_shinai" + fileSep + "file_deth.txt"};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause().getCause() instanceof FileNotFoundException);
		}
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_0KBのファイル() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/000_this_file_is_0KB.txt");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がStringであること", actual instanceof String);
//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/000_this_file_is_0KB.zip");
//		assertTrue("ZIP圧縮の内容が正しい事", expected.equals(actual)); // assertEquals()だとテスト結果表示に時間がかかる（∵ファイルの内容をメモリ上に展開する必要があるためか？）
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcがFileAccessObject_0KBのファイル");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, (String)actual);
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = src.jsFunction_load();
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		}
		finally{
			deleteDir(temp);
		}

	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_54Bytesのファイル() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.txt");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がStringであること", actual instanceof String);

//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");
//		assertTrue("ZIP圧縮の内容が正しい事", expected.equals(actual)); // assertEquals()だとテスト結果表示に時間がかかる（∵ファイルの内容をメモリ上に展開する必要があるためか？）

		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcがFileAccessObject_54Bytesのファイル");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, (String)actual);
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
		
			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = src.jsFunction_load();
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
//	public void test_zip_srcがFileAccessObject_10MBのファイル() throws Exception {
//		fail("未実装");
//	}
	
	/**
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_存在しないディレクトリ() throws Exception {
		Object[] args4src = { tempDir + fileSep + "sonzai_shinai" + fileSep + "dir_deth" + fileSep};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause().getCause() instanceof FileNotFoundException);
		}
	}
	
	
	/**
	 * Antのメッセージ
	 * [zip] Warning: skipping zip archive /foo/bar/target/000_empty_dir.zip because no files were included.
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_空のディレクトリ() throws Exception {
		File emptyDir = new File(tempDir, "test_zip_srcがFileAccessObject_空のディレクトリ");
		emptyDir.mkdirs();
		emptyDir.deleteOnExit();
		
		Object[] args4src = { emptyDir.getAbsolutePath() };
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertEquals("空のディレクトリの場合、返却値がundefinedとなること", Undefined.instance, actual);
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_ディレクトリ_1階層() throws Exception {
		
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
		
		assertTrue("返却値がStringであること", actual instanceof String);

//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy.zip");
//		assertTrue("ZIP圧縮の内容が正しい事", expected.equals(actual)); // assertEquals()だとテスト結果表示に時間がかかる（∵ファイルの内容をメモリ上に展開する必要があるためか？）
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcがFileAccessObject_ディレクトリ_1階層");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, (String)actual);
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

		}
		finally{
			deleteDir(temp);
		}
		
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcがFileAccessObject_ディレクトリ_3階層() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がStringであること", actual instanceof String);

		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcがFileAccessObject_ディレクトリ_3階層");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, (String)actual);
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			// ============ bar ============ 
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			
			// ============ empty_dir ============ 
			target = "empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(destResultCheck.jsFunction_path(), target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src.jsFunction_path(), target).list().length);
			
			// ============ bar ============ 
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			
			// ============ foo ============ 
			target = "foo/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		
			// ============ foo/empty_dir ============ 
			target = "foo/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(destResultCheck.jsFunction_path(), target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src.jsFunction_path(), target).list().length);

			// ============ foo/foo2 ============ 
			target = "foo/foo2/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);


			// ============ foo/foo2/empty_dir ============ 
			target = "foo/foo2/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(destResultCheck.jsFunction_path(), target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src.jsFunction_path(), target).list().length);
		}
		finally{
			deleteDir(temp);
		}		
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_存在しないファイル() throws Exception {

		Object[] args4src = { tempDir + fileSep + "sonzai_shinai" + fileSep + "file_deth.txt"};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/sonzai_shinai_file_deth.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause().getCause() instanceof FileNotFoundException);
		}
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_0KBのファイル() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/000_this_file_is_0KB.txt");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_zip_srcとdestがFileAccessObject_0KBのファイル/000_this_file_is_0KB.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP圧縮後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/000_this_file_is_0KB.zip");
//		assertEquals("ZIP圧縮の内容が正しい事", expected.equals(((FileAccessObject)actual).jsFunction_load())); // assertEquals()だとテスト結果表示に時間がかかる（∵ファイルの内容をメモリ上に展開する必要があるためか？）

		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcとdestがFileAccessObject_0KBのファイル");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, ((FileAccessObject)actual).jsFunction_load());
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path()));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		}
		finally{
			deleteDir(temp);
		}		
		
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_54Bytesのファイル() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.txt");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_zip_srcとdestがFileAccessObject_54Bytesのファイル/001_plain_text.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP圧縮後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);
		
//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");
//		assertEquals("ZIP圧縮の内容が正しい事", expected.equals(((FileAccessObject)actual).jsFunction_load())); // assertEquals()だとテスト結果表示に時間がかかる（∵ファイルの内容をメモリ上に展開する必要があるためか？）
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcとdestがFileAccessObject_54Bytesのファイル");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, ((FileAccessObject)actual).jsFunction_load());
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path()));
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
//	public void test_zip_srcとdestがFileAccessObject_10MBのファイル() throws Exception {
//		fail("未実装");
//	}
	
	/**
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_存在しないディレクトリ() throws Exception {
		Object[] args4src = { tempDir + fileSep + "sonzai_shinai" + fileSep + "dir_deth" + fileSep};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_zip_srcとdestがFileAccessObject_存在しないディレクトリ/sonzai_shinai_dir_deth.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause().getCause() instanceof FileNotFoundException);
		}
	}
	
	
	/**
	 * Antのメッセージ
	 * [zip] Warning: skipping zip archive /foo/bar/target/000_empty_dir.zip because no files were included.
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_空のディレクトリ() throws Exception {
		File emptyDir = new File(tempDir, "test_zip_srcとdestがFileAccessObject_空のディレクトリ");
		emptyDir.mkdirs();
		emptyDir.deleteOnExit();
		
		Object[] args4src = { emptyDir.getAbsolutePath() };
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4destParent = { "target/test/test_zip_srcとdestがFileAccessObject_空のディレクトリ/" };
		FileAccessObject destParent = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destParent, null, true);
		destParent.jsFunction_makeDirectories(); // あらかじめ親ディレクトリを作成しておく

		Object[] args4dest = { args4destParent[0] + "000_empty_dir.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);
		dest.jsFunction_save("abcde"); // あらかじめファイルを作成しておく

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };
		
		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);
		assertEquals("ZIP圧縮後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());

		assertEquals("空のディレクトリを圧縮した場合、destのファイルは変更されない事", "abcde", ((FileAccessObject)actual).jsFunction_load());
		
	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_ディレクトリ_1階層() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4destParent = { "target/test/test_zip_srcとdestがFileAccessObject_ディレクトリ_1階層/" };
		FileAccessObject destParent = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destParent, null, true);
		destParent.jsFunction_makeDirectories(); // あらかじめ親ディレクトリを作成しておく

		Object[] args4dest = { args4destParent[0] + "001_1_hierarchy.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);
		dest.jsFunction_save("abcde"); // あらかじめファイルを作成しておく

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP圧縮後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);
		
//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy.zip");
//		assertEquals("ZIP圧縮の内容が正しい事", expected, ((FileAccessObject)actual).jsFunction_load());
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcとdestがFileAccessObject_ディレクトリ_1階層");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, ((FileAccessObject)actual).jsFunction_load());
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		}
		finally{
			deleteDir(temp);
		}		

	}

	/**
	 * @throws Exception
	 */
	public void test_zip_srcとdestがFileAccessObject_ディレクトリ_3階層() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_zip_srcとdestがFileAccessObject_ディレクトリ_3階層/002_3_hierarchy.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP圧縮後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy.zip");
//		assertEquals("ZIP圧縮の内容が正しい事", expected, ((FileAccessObject)actual).jsFunction_load());
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_srcとdestがFileAccessObject_ディレクトリ_3階層");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, ((FileAccessObject)actual).jsFunction_load());
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			// ============ bar ============ 
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			
			// ============ empty_dir ============ 
			target = "empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(destResultCheck.jsFunction_path(), target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src.jsFunction_path(), target).list().length);
			
			// ============ bar ============ 
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			
			// ============ foo ============ 
			target = "foo/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);
		
			// ============ foo/empty_dir ============ 
			target = "foo/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(destResultCheck.jsFunction_path(), target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src.jsFunction_path(), target).list().length);

			// ============ foo/foo2 ============ 
			target = "foo/foo2/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/oim_banner01.gif";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);


			// ============ foo/foo2/empty_dir ============ 
			target = "foo/foo2/empty_dir";
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(destResultCheck.jsFunction_path(), target).list().length);
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).isDirectory());
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", 0, new File(src.jsFunction_path(), target).list().length);
		}
		finally{
			deleteDir(temp);
		}		
		
	}

	
	/**
	 * @throws Exception
	 */
	public void test_zip_filterの動作() throws Exception {
		String varName4filter = "filter";
		String jsSourceName = "SampleFilterFunction";

		StringBuilder jsSource = new StringBuilder();
		jsSource.append("var " + varName4filter + " = function(file){ ");
		jsSource.append("	if(file.isDirectory() || file.path().indexOf('.txt') != -1){");
		jsSource.append("	    return true;");
		jsSource.append("	}");
		jsSource.append("	else{");
		jsSource.append("	    return false;");
		jsSource.append("	}");
		jsSource.append("}");
		
		logger.debug(jsSource.toString());

		Function filterFunction = null;
		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();

			cx.evaluateString(scope, jsSource.toString(), jsSourceName, 1, null);

			Object x = scope.get(varName4filter, scope);
			if (x instanceof Function) {
				filterFunction = (Function) x;
			}
			else {
				fail("filter用関数生成失敗");
			}
		}
		finally {
			Context.exit();
		}
		
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_zip_filterの動作/001_3_hierarchy_without_txt.zip" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		voIn.put("filter", voIn, filterFunction);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_zip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP圧縮後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);
		
//		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/filter_test/001_3_hierarchy_without_txt.zip");
//		assertEquals("ZIP圧縮の内容が正しい事", expected, ((FileAccessObject)actual).jsFunction_load());
		
		File temp = new File(System.getProperty("java.io.tmpdir"), "test_zip_filterの動作");
		File actualCopy = new File(temp, "actual.zip");
		save(actualCopy, ((FileAccessObject)actual).jsFunction_load());
		
		try{
			Object[] args4destResultCheck = { temp.getAbsolutePath() + "/target" };
			FileAccessObject destResultCheck = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4destResultCheck, null, true);
	
			ValueObject voInResultCheck = new ValueObject();
			voInResultCheck.put("src", voInResultCheck, load(actualCopy));
			voInResultCheck.put("dest", voInResultCheck, destResultCheck);
			Object[] argsResultCheck = { voInResultCheck };
			
			// unzipが正しく動作していることが前提
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, argsResultCheck, null);
			
			String target;
			String result;
			String origin;
			
			target = "000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());

			target = "oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());
			
			// ============= bar =============
			target = "bar/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "bar/im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());

			target = "bar/oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());
			
			
			// ============= foo =============
			target = "foo/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());

			target = "foo/oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());

			// ============= foo/foo2 =============
			target = "foo/foo2/000_this_file_is_0KB.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/001_plain_text.txt";
			result = load(new File(destResultCheck.jsFunction_path(), target));
			origin = load(new File(src.jsFunction_path(), target));
			assertEquals("ZIP圧縮の内容が正しい事(" + target + ")", result, origin);

			target = "foo/foo2/im-jssp-api-list-0.1.2.zip";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());

			target = "foo/foo2/oim_banner01.gif";
			assertFalse("ZIP圧縮の内容が正しい事(" + target + ")", new File(destResultCheck.jsFunction_path(), target).exists());
			assertTrue("ZIP圧縮の内容が正しい事(" + target + ")", new File(src.jsFunction_path(), target).exists());

		}
		finally{
			deleteDir(temp);
		}		
		
	}

	
	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_zip_encoding() throws Exception {
//		fail("未実装");
//	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがnull() {
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, null);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがUndefined() {
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, Undefined.instance);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcプロパティがない() {
		ValueObject voIn = new ValueObject();
		voIn.put("no_src", voIn, null);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_空文字() {
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, "");
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがZip形式ではない() {

		Object[] args4dest = { "target/test/test_unzip_srcがZip形式ではない/" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);
		dest.jsFunction_remove();
		assertFalse("前提：出力先ディレクトリが存在しないこと", dest.jsFunction_exist());
		
		try{
			ValueObject voIn = new ValueObject();
			voIn.put("src", voIn,  "zip_keishiki_deha_arimasen");
			voIn.put("dest", voIn,  dest);
			Object[] args = { voIn };
	
			Object actual = null;
			try{
				actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			}
			catch(Exception e){
				// 内部でZipExceptionが発生しますが、無視します。
				fail("例外が発生してはいけません");
			}
			
			assertEquals("Zip形式ではないモノをunzipした場合は、結果がundefinedで返却されること", Undefined.instance, actual);
			
			// unzipに失敗しても、出力先は生成されます。（制限とする）
			assertTrue("出力先ディレクトリが存在する事", dest.jsFunction_exist());
			assertTrue("出力先がディレクトリである事", dest.jsFunction_isDirectory());
			assertEquals("出力先ディレクトリが空である事", 0, new File(dest.jsFunction_path()).list().length);
			
		}
		finally{
			dest.jsFunction_remove();
		}
	}

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_001_plain_text_zip() throws Exception {
		String src = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");

		Object[] args4dest = { "target/test/test_unzip_srcがString_001_plain_text_zip/" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP解凍後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

		File actualFile = new File(((FileAccessObject)actual).jsFunction_path());
		assertTrue("出力先ディレクトリが存在する事", actualFile.exists());
		assertTrue("出力先がディレクトリである事", actualFile.isDirectory());

		Object[] args4defrost = { ((FileAccessObject)actual).jsFunction_path(), "001_plain_text.txt" };
		FileAccessObject actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);

		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.txt");
		assertEquals("ZIP解凍後の内容が正しい事", expected, actualDefrost.jsFunction_load());
	}	

	
	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_unzip_srcがString_10MB() throws Exception {
//		fail("未実装");
//	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがFileAccessObject_存在しないファイル() throws Exception {
		Object[] args4src = { tempDir + fileSep + "sonzai_shinai" + fileSep + "file_deth"};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { tempDir + fileSep + "test_unzip_srcがFileAccessObject_存在しないファイル"};
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		
		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
			assertTrue("CauseがFileNotFoundExceptionである事", e.getCause().getCause() instanceof FileNotFoundException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがFileAccessObject_0KBのZIPファイル() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/002_this_zipfile_is_0KB.zip");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_unzip_srcがFileAccessObject_0KBのZIPファイル/" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = null;
		try{
			actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
		}
		catch(Exception e){
			// 内部でZipExceptionが発生しますが、無視します。
			fail("例外が発生してはいけません");
		}

		assertEquals("Zip形式ではないモノをunzipした場合は、結果がundefinedで返却されること", Undefined.instance, actual);

		// unzipに失敗しても、出力先は生成されます。（制限とする）
		assertTrue("出力先ディレクトリが存在する事", dest.jsFunction_exist());
		assertTrue("出力先がディレクトリである事", dest.jsFunction_isDirectory());
		assertEquals("出力先ディレクトリが空である事", 0, new File(dest.jsFunction_path()).list().length);
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがFileAccessObject_001_plain_text_zip() throws Exception {
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { "target/test/test_unzip_srcがFileAccessObject_001_plain_text_zip/" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP解凍後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

		File actualFile = new File(((FileAccessObject)actual).jsFunction_path());
		assertTrue("出力先ディレクトリが存在する事", actualFile.exists());
		assertTrue("出力先がディレクトリである事", actualFile.isDirectory());

		Object[] args4defrost = { ((FileAccessObject)actual).jsFunction_path(), "001_plain_text.txt" };
		FileAccessObject actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);

		String expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.txt");
		assertEquals("ZIP解凍後の内容が正しい事", expected, actualDefrost.jsFunction_load());
	}	

	
	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_unzip_srcがFileAccessObject_10MBのファイル() throws Exception {
//		fail("未実装");
//	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destがnull() throws Exception {
		String src = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, null);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destがUndefined() throws Exception {
		String src = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, Undefined.instance);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destプロパティがない() throws Exception {
		String src = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/001_plain_text.zip");

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("no_dest", voIn, null);
		Object[] args = { voIn };

		try{
			ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);
			fail("例外が発生する必要があります");
		}
		catch(Exception e){
			assertTrue("WrappedExceptionが発生する事", e instanceof WrappedException);
			assertTrue("CauseがIllegalArgumentExceptionが発生する事", e.getCause() instanceof IllegalArgumentException);
		}
	}	

	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destが既存のディレクトリ() throws Exception {

		String destBaseDir = "target/test/test_unzip_srcがString_destが既存のディレクトリ";

		// 準備（開始）
		URL url4pre = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy");
		copyDir(new File(getDecodePath(url4pre)), new File(destBaseDir));
		// 準備（終了）

		
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy.zip");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { destBaseDir + "/foo" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP解凍後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

		File actualFile = new File(((FileAccessObject)actual).jsFunction_path());
		assertTrue("出力先ディレクトリが存在する事", actualFile.exists());
		assertTrue("出力先がディレクトリである事", actualFile.isDirectory());

		String expected;
		Object[] args4defrost = new Object[2];
		args4defrost[0] = ((FileAccessObject)actual).jsFunction_path();
		FileAccessObject actualDefrost;

		args4defrost[1] =  "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	}	

	
	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destが既存のファイル() throws Exception {
		// ファイル削除後、新しいディレクトリを作成する。
		// （拡張子が付いていたりしてファイルっぽいFileオブジェクトでも強制的にディレクトリとして扱いますよ）
		
		String destBaseDir = "target/test/test_unzip_srcがString_destが既存のファイル";
		
		// 準備（開始）
		if(new File(destBaseDir).exists()){
			deleteDir(new File(destBaseDir));
		}
		URL url4pre = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy");
		copyDir(new File(getDecodePath(url4pre)), new File(destBaseDir));
		// 準備（終了）

		
		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy.zip");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { destBaseDir + "/foo/001_plain_text.txt" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP解凍後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

		File actualFile = new File(((FileAccessObject)actual).jsFunction_path());
		assertTrue("出力先ディレクトリが存在する事", actualFile.exists());
		assertTrue("出力先がディレクトリである事", actualFile.isDirectory());

		String expected;
		Object[] args4defrost = new Object[2];
		args4defrost[0] = ((FileAccessObject)actual).jsFunction_path();
		FileAccessObject actualDefrost;

		args4defrost[1] =  "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		//================= bar =================
		String dirName4bar = "bar/";
		args4defrost[1] =  dirName4bar;
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());

		args4defrost[1] =  dirName4bar + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  dirName4bar + "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  dirName4bar + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  dirName4bar + "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		
		//================= empty_dir =================
		args4defrost[1] =  "empty_dir";
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", 0, new File(actualDefrost.jsFunction_path()).list().length);
	
		
		//================= foo =================
		String dirName4foo = "foo/";
		args4defrost[1] =  dirName4foo;
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());

		args4defrost[1] =  dirName4foo + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  dirName4foo + "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  dirName4foo + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  dirName4foo + "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		
		//================= foo/empty_dir =================
		args4defrost[1] =  "foo/empty_dir";
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", 0, new File(actualDefrost.jsFunction_path()).list().length);

		
		//================= foo/foo2 =================
		String dirName4foo2 = "foo/foo2/";
		args4defrost[1] =  dirName4foo2;
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());

		args4defrost[1] =  dirName4foo2 + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  dirName4foo2 + "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  dirName4foo2 + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  dirName4foo2 + "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

	
		//================= foo/foo2/empty_dir =================
		args4defrost[1] =  "foo/foo2/empty_dir";
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", 0, new File(actualDefrost.jsFunction_path()).list().length);
	}	

	
	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destが存在しないディレクトリ_親ディレクトリ無し() throws Exception {
		
		String destBaseDir = "target/test/test_unzip_srcがString_destが存在しないディレクトリ_親ディレクトリ無し";

		// 準備（開始）
		if(new File(destBaseDir).exists()){
			deleteDir(new File(destBaseDir));
		}
		// 準備（終了）

		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy.zip");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { destBaseDir + "/foo/bar" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP解凍後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

		File actualFile = new File(((FileAccessObject)actual).jsFunction_path());
		assertTrue("出力先ディレクトリが存在する事", actualFile.exists());
		assertTrue("出力先がディレクトリである事", actualFile.isDirectory());

		String expected;
		Object[] args4defrost = new Object[2];
		args4defrost[0] = ((FileAccessObject)actual).jsFunction_path();
		FileAccessObject actualDefrost;

		args4defrost[1] =  "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		//================= bar =================
		String dirName4bar = "bar/";
		args4defrost[1] =  dirName4bar;
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());

		args4defrost[1] =  dirName4bar + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  dirName4bar + "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  dirName4bar + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  dirName4bar + "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		
		//================= empty_dir =================
		args4defrost[1] =  "empty_dir";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", 0, new File(actualDefrost.jsFunction_path()).list().length);
	
		
		//================= foo =================
		String dirName4foo = "foo/";
		args4defrost[1] =  dirName4foo;
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());

		args4defrost[1] =  dirName4foo + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  dirName4foo + "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  dirName4foo + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  dirName4foo + "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		
		//================= foo/empty_dir =================
		args4defrost[1] =  "foo/empty_dir";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", 0, new File(actualDefrost.jsFunction_path()).list().length);

		
		//================= foo/foo2 =================
		String dirName4foo2 = "foo/foo2/";
		args4defrost[1] =  dirName4foo2;
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());

		args4defrost[1] =  dirName4foo2 + "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  dirName4foo2 + "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  dirName4foo2 + "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  dirName4foo2 + "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

	
		//================= foo/foo2/empty_dir =================
		args4defrost[1] =  "foo/foo2/empty_dir";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/002_3_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertTrue("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", actualDefrost.jsFunction_isDirectory());
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", 0, new File(actualDefrost.jsFunction_path()).list().length);
	}
	
	/**
	 * @throws Exception
	 */
	public void test_unzip_srcがString_destが存在しないディレクトリ_親ディレクトリ有り() throws Exception {
		String destBaseDir = "target/test/test_unzip_srcがString_destが存在しないディレクトリ_親ディレクトリ有り";

		// 準備（開始）
		if(new File(destBaseDir).exists() == false){
			new File(destBaseDir).mkdirs();
		}
		// 準備（終了）

		URL url = this.getClass().getResource("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy.zip");
		Object[] args4src = {getDecodePath(url)};
		FileAccessObject src = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4src, null, true);

		Object[] args4dest = { destBaseDir + "/foo/bar" };
		FileAccessObject dest = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4dest, null, true);

		ValueObject voIn = new ValueObject();
		voIn.put("src", voIn, src);
		voIn.put("dest", voIn, dest);
		Object[] args = { voIn };

		Object actual = ArchiverObject.jsStaticFunction_unzip(cxMadeInSetup, null, args, null);

		assertTrue("返却値がFileAccessObjectであること", actual instanceof FileAccessObject);
		assertEquals("ZIP解凍後のファイルパスが正しい事", dest.jsFunction_path(), ((FileAccessObject)actual).jsFunction_path());
		assertNotSame("返却値が引数オブジェクトのdestと同じインスタンスではない事", dest, actual);

		File actualFile = new File(((FileAccessObject)actual).jsFunction_path());
		assertTrue("出力先ディレクトリが存在する事", actualFile.exists());
		assertTrue("出力先がディレクトリである事", actualFile.isDirectory());

		String expected;
		Object[] args4defrost = new Object[2];
		args4defrost[0] = ((FileAccessObject)actual).jsFunction_path();
		FileAccessObject actualDefrost;

		args4defrost[1] =  "000_this_file_is_0KB.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());

		args4defrost[1] =  "001_plain_text.txt";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
		
		args4defrost[1] =  "im-jssp-api-list-0.1.2.zip";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	
		args4defrost[1] =  "oim_banner01.gif";
		expected = load("/org/intra_mart/jssp/script/api/ArchiverObjectTest/dir_test/001_1_hierarchy/" + args4defrost[1]);
		actualDefrost = (FileAccessObject)FileAccessObject.jsConstructor(cxMadeInSetup, args4defrost, null, true);
		assertEquals("ZIP解凍後の内容が正しい事(" + args4defrost[1] + ")", expected, actualDefrost.jsFunction_load());
	}

	// TODO 未実装
//	/**
//	 * @throws Exception
//	 */
//	public void test_unzip_encoding() throws Exception {
//		fail("未実装");
//	}
	
	
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
	private void copyDir(File srcDir, File destDir) throws IOException {
		logger.trace("Copy Dir Enter: from '{}' to '{}'", srcDir.getAbsolutePath(), destDir.getAbsolutePath());
		
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
	private void copyFile(File srcFile, File destFile) throws IOException {
		
		java.nio.channels.FileChannel sourceChannel      = new java.io.FileInputStream (srcFile) .getChannel();
		java.nio.channels.FileChannel destinationChannel = new java.io.FileOutputStream(destFile).getChannel();
		try{
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
			logger.trace("Copy File: from '{}' to '{}'", srcFile.getAbsolutePath(), destFile.getAbsolutePath());
		}
		finally{
			sourceChannel.close();
			destinationChannel.close();		
		}
	}
	
	/**
	 * @param targetDir
	 */
	private void deleteDir(File targetDir) throws IOException {
		logger.trace("Delete Dir Enter: '{}'", targetDir.getAbsolutePath());

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

				logger.trace("Delete File: '{}'", child.getAbsolutePath());
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
