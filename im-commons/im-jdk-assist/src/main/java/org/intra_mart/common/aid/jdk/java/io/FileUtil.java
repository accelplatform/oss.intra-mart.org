package org.intra_mart.common.aid.jdk.java.io;

import java.io.File;

/**
 * Fileユーティリティクラス。 <BR>
 * <BR>
 * Fileに関する実用的な関数および定数値を提供するクラス。 <BR>
 * 
 */
public class FileUtil {

    /**
     * コンストラクタ。<BR>
     * <BR>
     * 隠蔽します。
     */
    private FileUtil() {
        super();
    }

	/**
	 * 指定ディレクトリ直下のファイル群の中で一番古い更新日付を返却します。
	 * @param dir　指定ディレクトリ
	 * @return 指定ディレクトリ直下のファイル群の中で一番古い更新日付を返却します。<BR>
	 * 引数 <B>dir</B> がディレクトリを示さない場合は、現在日付を返却します。
	 */
	public static long getOldestLastModifiedInDir(File dir) {
		
		long timeStamp = System.currentTimeMillis();
		
		String [] fileLists = dir.list();

		if(fileLists != null){
			for(int idx = 0; idx < fileLists.length; idx++ ){
				timeStamp = Math.min(timeStamp, (new File(dir, fileLists[idx])).lastModified());
			}
		}
		
		return timeStamp;
	}

}