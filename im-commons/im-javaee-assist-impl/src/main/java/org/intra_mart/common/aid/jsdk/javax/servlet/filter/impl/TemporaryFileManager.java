package org.intra_mart.common.aid.jsdk.javax.servlet.filter.impl;

import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * 一時ファイル名を作成するためのクラス
 * シーケンス番号を用いて、ユニークな一時ファイルを作成。
 * このオブジェクトが返す File オブジェクトが、どのオブジェクトからも
 * 使われなくなると、そのパスは自動的に再利用されます。
 * シーケンス番号は、必ず 0 から開始されます。
 */
public class TemporaryFileManager{
	/**
	 * ファイル名の一意性を保証するために使用するカウンタ値
	 */
	private volatile long fileIndex = 0;
	/**
	 * 再利用可能なファイルパスを保存するためのスタック
	 */
	private ReferenceQueue referenceQueue = new ReferenceQueue();
	/**
	 * 一時ファイル保存ディレクトリ
	 */
	private File parentDirectory;
	/**
	 * ファイル名のフォーマッタ
	 */
	private MessageFormat fileNamePattern;
	/**
	 * パス名に対して、現在使用中の弱参照を保存します。
	 */
	private Map referenceMap = new WeakHashMap();

	/**
	 * @param dir 親ディレクトリ
	 * @param name ファイル名を決定するためのパターン
	 */
	public TemporaryFileManager(File dir, MessageFormat name){
		super();
		this.parentDirectory = dir;
		this.fileNamePattern = name;
	}

	/**
	 * 一時ファイルパスを返します。
	 * 現在使われていないパスを返します。
	 * @return 一時ファイルパス
	 */
	public synchronized File getPath(){
		File file = this.findFile();
		FileWeakReference fileWeakReference = new FileWeakReference(file, this.referenceQueue);
		referenceMap.put(file, fileWeakReference);
		return file;
	}

	/**
	 * ファイルパスを返します。<p>
	 * このメソッドは、再利用可能なパスがあれば、再利用可能なパスの中から
	 * １つを選択して返します。<br>
	 * 再利用可能なパスがない場合は、新しいパスを作成して返します。
	 * @return ファイルパス
	 */
	private File findFile(){
		FileWeakReference reference = (FileWeakReference) referenceQueue.poll();
		if(reference != null){
			return new File(reference.getAbsolutePath());
		}
		else{
			// 新規ファイル名を作成
			Object[] arguments = { new Long(this.fileIndex) };
			try{
				return new File(this.parentDirectory, this.fileNamePattern.format(arguments));
			}
			finally{
				this.fileIndex++;		// カウンタのインクリメント
			}
		}
	}

	/**
	 * リクエストへの弱参照オブジェクト。
	 * リクエストへの参照がなくなった時点で、一時ファイルのパスを
	 * 再利用できるようにする事が目的。
	 */
	private static class FileWeakReference extends WeakReference{
		private String filePath;

		/**
		 * ビルダの弱参照を作成します。
		 * @param file クエリを保存しているファイルのパスを表す File オブジェクト
		 * @param builder 監視するビルダオブジェクト
		 * @param q リファレンスキュー
		 */
		protected FileWeakReference(File file, ReferenceQueue q){
			super(file, q);
			this.filePath = file.getAbsolutePath();
		}

		/**
		 * このリファレンスが弱参照している File オブジェクトの絶対パスを返します。
		 * @return 絶対パス
		 */
		public String getAbsolutePath(){
			return this.filePath;
		}
	}
}

