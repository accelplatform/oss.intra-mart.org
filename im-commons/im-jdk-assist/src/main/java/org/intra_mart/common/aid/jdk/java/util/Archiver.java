package org.intra_mart.common.aid.jdk.java.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ファイルを圧縮／解凍するためのクラスです。<br/>
 */
public class Archiver {

	private static java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger(Archiver.class.getName());
	
	/**
	 * バイト配列をZIP形式で圧縮します。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param binary 圧縮対象のバイト配列
	 * @param fileName 圧縮時のファイル名
	 * @param dest ZIPファイルの出力先
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static void zip(final byte[] binary, final String fileName, final File dest) throws IOException {
		String [] argName = {"binary", "fileName", "dest"};
		
		if(binary == null || fileName == null || dest == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "', '" + argName[1] + "' and '" + argName[2] + "' cannot specify null.");
		}
		
		if(binary.length == 0){
			throw new IllegalArgumentException("Argument '" + argName[0] + "' cannot specify empty string.");
		}

		ByteArrayInputStream bai = new ByteArrayInputStream(binary);
		InputStream is = new BufferedInputStream(bai);
		
		zip(is, fileName, dest);
	}
	
	/**
	 * バイト配列をZIP形式で圧縮します。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param src 圧縮対象のバイト入力ストリーム
	 * @param fileName 圧縮時のファイル名
	 * @param dest ZIPファイルの出力先
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static void zip(final InputStream src, final String fileName, final File dest) throws IOException {
		String [] argName = {"src", "fileName", "dest"};
		
		if(src == null || fileName == null || dest == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "', '" + argName[1] + "' and '" + argName[2] + "' cannot specify null.");
		}
				
		ZipOutputStream zos = null;
		InputStream is = null;
		
		try {
			if(dest.exists()){
				deleteFileRecursive(dest);
			}
			dest.getParentFile().mkdirs();
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
			zos = new ZipOutputStream(bos);
			
			writeEntry(zos, src, fileName);
		}
		finally{
			if(is != null){
				try {
					is.close();
				}
				catch (IOException e) { /* Do Nothing */ }
			}
			
			if(zos != null){
				try {
					zos.close();
				}
				catch (IOException e) { /* Do Nothing */ }
			}
		}
	}
	
	 
	/**
	 * 指定されたファイルやディレクトリをZIP形式で圧縮します。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param src 圧縮するファイル、または、ディレクトリ
	 * @param dest ZIPファイルの出力先
	 * @param filter 
	 * 			このメソッドは、ディレクトリの圧縮処理を行う際、そのディレクトリ配下に含まれるファイルを走査します。<br/>
	 * 			この引数に設定されたフィルタの実行結果により、走査したファイルを圧縮対象とするか否かを判定します。<br/>
	 * 			フィルタが true  を返却した場合、該当ファイルは圧縮対象となります。<br/>
	 * 			フィルタが false を返却した場合、該当ファイルは圧縮対象から<b>外れます</b>。<br/>
	 * 			フィルタが未指定の場合、すべてのファイルが圧縮対象となります。<br/>
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static void zip(final File src, final File dest, final FileFilter filter) throws IOException {
		String [] argName = {"src", "dest", "filter"};

		if(src == null || dest == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "' and '" + argName[1] + "' cannot specify null.");
		}
		
		if(src.exists() == false){
			FileNotFoundException fnfe = new FileNotFoundException("'" + src.getAbsolutePath() + "' is not found.");
			throw new IllegalArgumentException(fnfe);
		}

		if(src != null && src.isDirectory() && src.list().length == 0){
			jdkLogger.warning("skipping zip archive because no files were included: " + src);
			return;
		}
		
		File[] sources = { src };
		zip(sources, dest, filter);
	}

	/**
	 * 指定されたファイルやディレクトリをZIP形式で圧縮します。<br/>
	 * 圧縮するファイルやディレクトリは、複数指定することが可能です。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param sources 圧縮するファイル、または、ディレクトリの配列
	 * @param destination ZIPファイルの出力先
	 * @param filter 
	 * 			このメソッドは、ディレクトリの圧縮処理を行う際、そのディレクトリ配下に含まれるファイルを走査します。<br/>
	 * 			この引数に設定されたフィルタの実行結果により、走査したファイルを圧縮対象とするか否かを判定します。<br/>
	 * 			フィルタが true  を返却した場合、該当ファイルは圧縮対象となります。<br/>
	 * 			フィルタが false を返却した場合、該当ファイルは圧縮対象から<b>外れます</b>。<br/>
	 * 			フィルタが未指定の場合、すべてのファイルが圧縮対象となります。<br/>
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static void zip(final File[] sources, final File destination, final FileFilter filter) throws IOException {
		String [] argName = {"sources", "destination", "filter"};

		if(sources == null || destination == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "' and '" + argName[1] + "' cannot specify null.");
		}
		
		ZipOutputStream zos = null;
		InputStream is = null;
		
		try {
			if(destination.exists()){
				deleteFileRecursive(destination);
			}
			destination.getParentFile().mkdirs();

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destination));
			zos = new ZipOutputStream(bos);

			int srcLength = sources.length;
			for(int idx = 0; idx < srcLength; idx++){
				File src = sources[idx];
				
				if(!src.exists()){
					jdkLogger.warning("skipping zip archive because file/dir is not found: " + src);
					continue;
				}
				
				if(src.isDirectory()){
					if(src.list().length == 0){
						jdkLogger.warning("skipping zip archive because no files were included: " + src);
						continue;
					}
					
					boolean result = doFileFilter(filter, src);
					if(result == true){
						writeDirEntry(zos, src, null, filter);
					}
				}
				else{
					boolean result = doFileFilter(filter, src);
					if(result == true){
						writeEntry(zos, new FileInputStream(src), src.getName());
					}
				}
			}
		}
		finally{
			if(is != null){
				try {
					is.close();
				}
				catch (IOException e) { /* Do Nothing */ }
			}
			
			if(zos != null){
				try {
					zos.close();
				}
				catch (IOException e) { /* Do Nothing */ }
			}
		}
	}
	
	
	/**
	 * ZIPファイルを解凍します。<br/>
	 * 解凍先のパスにファイルやディレクトリが既に存在する場合、それらを削除してから解凍を行います。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param src ZIPファイル
	 * @param dest 解凍先ディレクトリ
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws ZipException ZIP 形式エラーが発生した場合
	 */
	public static void unzip(final File src, final File dest) throws IOException, ZipException {
		String [] argName = {"src", "dest"};

		if(src == null || dest == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "' and '" + argName[1] + "' cannot specify null.");
		}
		
		if(src.exists() == false){
			FileNotFoundException fnfe = new FileNotFoundException("'" + src.getAbsolutePath() + "' is not found.");
			throw new IllegalArgumentException(fnfe);
		}
		
		FileInputStream fis = new FileInputStream(src);
		BufferedInputStream bis = new BufferedInputStream(fis);

		unzip(bis, dest);
	}

	/**
	 * ZIPファイルを解凍します。<br/>
	 * 解凍先のパスにファイルやディレクトリが既に存在する場合、それらを削除してから解凍を行います。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param binary ZIP形式のバイト配列
	 * @param dest 解凍先ディレクトリ
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws ZipException ZIP 形式エラーが発生した場合
	 */
	public static void unzip(final byte[] binary, final File dest) throws IOException, ZipException {
		String [] argName = {"binary", "dest"};

		if(binary == null || dest == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "' and '" + argName[1] + "' cannot specify null.");
		}

		ByteArrayInputStream bai = new ByteArrayInputStream(binary);
		InputStream is = new BufferedInputStream(bai);
		
		unzip(is, dest);
	}

	/**
	 * ZIPファイルを解凍します。<br/>
	 * 解凍先のパスにファイルやディレクトリが既に存在する場合、それらを削除してから解凍を行います。<br/>
	 * <br/>
	 * <b>【注意】</b><br/>
	 * 圧縮ファイル内のファイル名は「UTF-8」で扱われます。<br/>
	 * （そのため、ファイル名が「UTF-8」に対応していないOSやアーカイバを利用した場合、文字化けが発生します）<br/>
	 * 
	 * @param src ZIP形式のバイト入力ストリーム
	 * @param dest 解凍先ディレクトリ
	 * @throws IOException 入出力エラーが発生した場合
	 * @throws ZipException ZIP 形式エラーが発生した場合
	 */
	public static void unzip(final InputStream src, final File dest) throws IOException, ZipException {
		String [] argName = {"src", "dest"};

		if(src == null || dest == null){
			throw new IllegalArgumentException("Argument '" + argName[0] + "' and '" + argName[1] + "' cannot specify null.");
		}
		
		if(dest.exists()){
			deleteFileRecursive(dest);
		}
		dest.mkdirs();

		ZipInputStream zis = null;
		CheckedOutputStream cos = null;
		
		try{
			if(src instanceof ZipInputStream) {
				zis = (ZipInputStream) src;
			}
			else {
				zis = new ZipInputStream(src);
			}
			
			boolean isExistZipEntry = false;
			ZipEntry entry = null;
			while ((entry = zis.getNextEntry()) != null) {
				isExistZipEntry = true;
				
				File child = new File(dest, entry.getName());
				
				if (entry.isDirectory()) {
					child.mkdirs();
				}
				else {
					child.getParentFile().mkdirs();
					
					FileOutputStream fos = new FileOutputStream(child);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					cos = new CheckedOutputStream(bos, new CRC32());
					
					byte[] buf = new byte[1024];

					int len = 0;
					int totalSize = 0;
					while ((len = zis.read(buf)) != -1) {
						totalSize += len;
						cos.write(buf, 0, len);
					}
					cos.close();

					if( jdkLogger.isLoggable(java.util.logging.Level.WARNING) ){
						if (entry.getSize() != totalSize) {
							jdkLogger.log(java.util.logging.Level.WARNING,
										  "The uncompressed size is different. " + 
										  "expected: " + entry.getSize() + ", but actual: " + totalSize + ".");
						}
						if (entry.getCrc() != cos.getChecksum().getValue()) {
							jdkLogger.log(java.util.logging.Level.WARNING,
										  "The checksum is different. " + 
										  "expected: " + entry.getCrc() + ", but actual: " + cos.getChecksum().getValue() + ".");
						}
					}
				}
				zis.closeEntry();
			}
			
			if(isExistZipEntry == false){
				throw new ZipException("ZIP format error has occurred");
			}
		}
		finally{
			if(zis != null){
				try {
					zis.close();
				}
				catch (IOException e) { /* Do Nothing */ }
			}
			
			if(cos != null){
				try {
					cos.close();
				}
				catch (IOException e) { /* Do Nothing */ }
			}
		}
	}
	
	/**
	 * @param zos
	 * @param dir
	 * @param filter
	 * @throws IOException
	 */
	private static void writeDirEntry(final ZipOutputStream zos, 
										final File dir, 
										final String baseDirName, 
										final FileFilter filter) throws IOException {
		
		if( jdkLogger.isLoggable(java.util.logging.Level.FINER) ){
			jdkLogger.log(java.util.logging.Level.FINER, "Enter writeDirEntry[" + baseDirName + "]: " + dir);
		}
		
		boolean result4Dir = doFileFilter(filter, dir);
		if(result4Dir == false){
			return;
		}
		
		if(dir.isFile()){
			writeEntry(zos, new FileInputStream(dir), dir.getName());
		}
		else{
			// 空のディレクトリ
			if(dir.list().length == 0){
				writeEntry(zos, null, baseDirName + "/");
			}
			else{
				for(String name : dir.list()){
					File child = new File(dir, name);
					String baseDirNameInner = (baseDirName == null) ? child.getName() : baseDirName + "/" + child.getName();
					
					boolean result4child = doFileFilter(filter, child);
					if(result4child == false){
						continue;
					}
					
					if(child.isDirectory()){
						writeDirEntry(zos, child, baseDirNameInner , filter); // 再帰！
					}
					else{
						writeEntry(zos, new FileInputStream(child), baseDirNameInner);
					}
				}
			}
		}
	}

	
	/**
	 * @param zos
	 * @param is
	 * @param entryName
	 * @throws IOException
	 */
	private static void writeEntry(final ZipOutputStream zos, 
									 final InputStream is, 
									 final String entryName) throws IOException {
		
		ZipEntry ze = new ZipEntry(entryName);
		zos.putNextEntry(ze);

		int totalSize = 0;

		if(is != null){
			
			CheckedInputStream cin = null;
			byte[] buf = new byte[1024];
			
			try{
				cin = new CheckedInputStream(new BufferedInputStream(is), new CRC32());
				int len = 0;
				while ((len = cin.read(buf)) != -1) {
					totalSize += len;
					zos.write(buf, 0, len);
				}
			}
			finally{
				cin.close();
				ze.setCrc(cin.getChecksum().getValue());
			}
			
		}
		
	    ze.setSize(totalSize);

		if( jdkLogger.isLoggable(java.util.logging.Level.FINE) ){
			jdkLogger.log(java.util.logging.Level.FINE, "Add zip entry: " + entryName);
		}
		zos.closeEntry();
	}
	
	/**
	 * @param filter
	 * @param target
	 * @return
	 */
	private static boolean doFileFilter(final FileFilter filter, final File target){
		if( filter == null || filter.accept(target) == true ){
			if( jdkLogger.isLoggable(java.util.logging.Level.FINER) ){
				jdkLogger.log(java.util.logging.Level.FINER, "Filter returns true: " + target);
			}
			return true;
		}
		else{
			if( jdkLogger.isLoggable(java.util.logging.Level.FINER) ){
				jdkLogger.log(java.util.logging.Level.FINER, "Filter returns false: " + target);
			}
			return false;
		}
	}
	
	/**
	 * 指定されたファイル、または、ディレクトリを削除します。<br/>
	 * ディレクトリが指定された場合、そのディレクトリに含まれているファイル、および、ディレクトリも再帰的に削除します。
	 * 
	 * @param target 削除対象ファイル、または、ディレクトリ。
	 */
	protected static void deleteFileRecursive(final File target) throws IOException {
		if( jdkLogger.isLoggable(java.util.logging.Level.FINER) ){
			jdkLogger.log(java.util.logging.Level.FINER, "Delete Dir Enter: " + target.getAbsolutePath());
		}

		if(!target.exists()){
			return;
		}
		
		if(target.isFile()){
			target.delete();
			return;
		}

		for(String name : target.list()){
			File child = new File(target, name);
			
			if(child.isDirectory()){
				deleteFileRecursive(child); // 再帰！
			}
			else{
				boolean result = child.delete();
				if(!result){
					throw new IllegalStateException("Deletion failure: " + child.getAbsolutePath());
				}

				if( jdkLogger.isLoggable(java.util.logging.Level.FINE) ){
					jdkLogger.log(java.util.logging.Level.FINE, 
								  "Delete File: " + child.getAbsolutePath());
				}
			}
		}
	}
}