package org.intra_mart.common.aid.jdk.java.io.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * このクラスは、ディレクトリを扱う時に便利な機能を提供します。<P>
 *
 * このクラスは java.io.File クラスをベースに、オブジェクトが表す抽象パスが
 * ディレクトリである場合に利用することができます。<br>
 * <P>
 *
 */
public class Directory extends File{
	/**
	 * ファイルパスを抽出するためのフィルター
	 */
	private static FilePathFilter fileFilter = new FilePathFilter();
	/**
	 * ディレクトリパスを抽出するためのフィルター
	 */
	private static DirectoryPathFilter directoryFilter = new DirectoryPathFilter();

	/**
	 * 指定のパスがディレクトリを表すかどうかをチェックします。
	 * @param path ディレクトリの抽象パス
	 * @throws IllegalArgumentException 指定の抽象パスがファイルとして存在している場合
	 */
	private static void isValid(File path) throws IllegalArgumentException{
		if(path.isFile()){
			throw new IllegalArgumentException("A path is a file: " + path.getAbsolutePath());
		}
	}

	/**
	 * 指定されたパス名文字列を抽象パス名に変換して、
	 * 新しい File のインスタンスを生成します。
	 * 指定された文字列が空の文字列の場合、結果は空の抽象パス名になります。
	 * @param pathname パス名文字列
	 * @throws NullPointerException pathname 引数が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがファイルとして存在している場合
	 * @see java.io.File#File(File, String)
	 */
	public Directory(String pathname){
		super(pathname);
		isValid(this);
	}

	/**
	 * 親抽象パス名および子パス名文字列から新しい File のインスタンスを
	 * 生成します。<p>
	 * parent が null の場合、新しい File のインスタンスは、
	 * 指定された child パス名文字列で単一引数の File コンストラクタを
	 * 呼び出したように生成されます。<p>
	 * そうでない場合、parent 抽象パス名はディレクトリを示し、
	 * child パス名文字列はディレクトリまたはファイルを示します。
	 * child パス名文字列が絶対の場合、
	 * それはシステムに依存する方法で相対パス名に変換されます。
	 * parent が空の抽象パス名の場合、新しい File のインスタンスは、
	 * child を抽象パス名に変換し、その結果をシステムに依存する
	 * デフォルトディレクトリを基準に解決することで生成されます。
	 * そうでない場合、各パス名文字列は抽象パス名に変換され、
	 * 子抽象パス名は親を基準に解決されます。
	 * @param parent 親抽象パス名
	 * @param child 子パス名文字列
	 * @throws NullPointerException child が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがファイルとして存在している場合
	 * @see java.io.File#File(File, String)
	 */
	public Directory(File parent, String child){
		super(parent, child);
		isValid(this);
	}

	/**
	 * 親パス名文字列および子パス名文字列から
	 * 新しい File のインスタンスを生成します。<p>
	 * parent が null の場合、新しい File のインスタンスは、
	 * 指定された child パス名文字列で単一引数の File コンストラクタを
	 * 呼び出したように生成されます。<pr>
	 * そうでない場合、parent パス名文字列はディレクトリを示し、
	 * child パス名文字列はディレクトリまたはファイルを示します。
	 * child パス名文字列が絶対の場合、
	 * それはシステムに依存する方法で相対パス名に変換されます。
	 * parent が空の文字列の場合、新しい File のインスタンスは、
	 * child を抽象パス名に変換し、
	 * その結果をシステムに依存するデフォルトディレクトリを基準に
	 * 解決することで生成されます。
	 * そうでない場合、各パス名文字列は抽象パス名に変換され、
	 * 子抽象パス名は親を基準に解決されます。
	 * @param parent 親パス名文字列
	 * @param child 子パス名文字列
	 * @throws NullPointerException child が null の場合
	 * @throws IllegalArgumentException 指定の抽象パスがファイルとして存在している場合
	 * @see java.io.File#File(File, String)
	 */
	public Directory(String parent, String child){
		super(parent, child);
		isValid(this);
	}

	/**
	 * この抽象パス名が示すディレクトリにあるサブディレクトリの抽象パスを
	 * 繰り返し処理する反復子を作成します。
	 * 反復子が返す各要素は、ディレクトリを表す
	 * java.io.File クラスのインスタンスです。
	 * @return ディレクトリパスを繰り返し処理する反復子
	 */
	public Collection childDirectories(){
		File members[] = this.listFiles(directoryFilter);

		if(members != null){
			List list = new ArrayList();
			for(int idx = 0; idx < members.length; idx++){
				list.add(members[idx]);
			}
			return list;
		}
		else{
			return null;
		}
	}

	/**
	 * この抽象パス名が示すディレクトリにあるファイルの抽象パスを
	 * 繰り返し処理する反復子を作成します。
	 * 反復子が返す各要素は、ファイルを表す
	 * java.io.File クラスのインスタンスです。
	 * @return ファイルパスを繰り返し処理する反復子
	 */
	public Collection childFiles(){
		File members[] = this.listFiles(fileFilter);

		if(members != null){
			List list = new ArrayList();
			for(int idx = 0; idx < members.length; idx++){
				list.add(members[idx]);
			}
			return list;
		}
		else{
			return null;
		}
	}

	/**
	 * このディレクトリパスを削除します。
	 * このディレクトリがファイルまたはサブディレクトリを持つ場合、
	 * それらもすべて削除します。<p>
	 * このメソッドは複数のパスを削除しますが、すべてのパスを削除したか、
	 * またはしないことを保証するものではありません。
	 * このディレクトリパスの削除に失敗した場合でも、
	 * このディレクトリに含まれるいくつかのパスが削除される場合があります。
	 * @return このディレクトリパスの削除に成功した場合 true。それ以外の場合 false。
	 * @throws IOException このディレクトリパスに含まれるいずれかのパスの削除に失敗した場合。
	 */
	public boolean deleteAll() throws IOException{
		this.deleteChilds();
		return this.delete();
	}

	/**
	 * このディレクトリが持つファイルまたはサブディレクトリをすべて削除します。
	 * このサブディレクトリに含まれるファイルやサブディレクトリも
	 * 再帰的に検索し、すべて削除します。<p>
	 * このメソッドが false を返した場合でも、
	 * いくつかのパスが削除される場合があります。
	 * @throws IOException このディレクトリパスに含まれるいずれかのパスの削除に失敗した場合。
	 */
	public void deleteChilds() throws IOException{
		this.deleteChilds(this);
	}

	/**
	 * 指定のパスに含まれるすべてのパスを削除します。
	 * @param dir ディレクトリを表すパス
	 * @throws IOException ディレクトリパス dir に含まれるいずれかのパスの削除に失敗した場合。
	 * @throws IllegalArgumentException 引数がファイルを表すパスの場合
	 */
	private void deleteChilds(File dir) throws IOException{
		if(dir.exists()){
			if(dir.isFile()){
				throw new IllegalArgumentException("File has no childs: " + dir.getAbsolutePath());
			}
			else{
				File[] files = dir.listFiles();
				for(int idx = 0; idx < files.length; idx++){
					if(files[idx].isDirectory()){
						this.deleteChilds(files[idx]);
					}

					if(! files[idx].delete()){
						throw new IOException("Deletion failure in file: " + files[idx].getAbsolutePath());
					}
				}
			}
		}
	}

	/**
	 * この抽象パス名が示すディレクトリにあるファイルおよびディレクトリの
	 * 抽象パスを繰り返し処理する反復子を作成します。
	 * 反復子が返す各要素は、ファイルまたはディレクトリを表す
	 * java.io.File クラスのインスタンスです。
	 * @param depth 検索をするサブディレクトリの深さ
	 * @return ファイルパスを繰り返し処理する反復子
	 * @throws IllegalArgumentException depth 引数が 1 よりも小さい場合
	 */
	public Collection paths(int depth){
		if(depth > 0){
			File[] members = this.listFiles();

			if(members != null){
				return pathList(new ArrayList(), members, depth, true);
			}
			else{
				return null;
			}
		}
		else{
			throw new IllegalArgumentException("Illegal directory depth level: " + String.valueOf(depth));
		}
	}

	/**
	 * この抽象パス名が示すディレクトリにあるファイルの抽象パスを
	 * 繰り返し処理する反復子を作成します。
	 * 反復子が返す各要素は、ファイルを表す
	 * java.io.File クラスのインスタンスです。<br>
	 * このメソッドは、この抽象パスが表すディレクトリ内のすべての
	 * ファイルパスを検索します。
	 * @return ファイルパスを繰り返し処理する反復子
	 * @throws IllegalArgumentException depth 引数が 1 よりも小さい場合
	 */
//	public Collection files(){
//		return this.files(Integer.MAX_VALUE);
//	}

	/**
	 * この抽象パス名が示すディレクトリにあるファイルの抽象パスを
	 * 繰り返し処理する反復子を作成します。
	 * 反復子が返す各要素は、ファイルを表す
	 * java.io.File クラスのインスタンスです。
	 * @param depth 検索をするサブディレクトリの深さ
	 * @return ファイルパスを繰り返し処理する反復子
	 * @throws IllegalArgumentException depth 引数が 1 よりも小さい場合
	 */
	public Collection files(int depth){
		if(depth > 0){
			File[] members = this.listFiles();

			if(members != null){
				return pathList(new ArrayList(), members, depth, false);
			}
			else{
				return null;
			}
		}
		else{
			throw new IllegalArgumentException("Illegal directory depth level: " + String.valueOf(depth));
		}
	}

	/**
	 * 指定の抽象パスが表すディレクトリに含まれるすべてのファイルの
	 * 抽象パスのリストを返します。
	 * このメソッドは、指定のディレクトリがサブディレクトリを含む場合、
	 * それらサブディレクトリに含まれるファイルも再帰的に検索して
	 * リストに含めます。
	 * @param list ファイルを表す抽象パスを格納するリスト
	 * @param members ファイルリスト
	 * @param depth 検索をするサブディレクトリの深さ
	 * @return 指定ディレクトリ内に含まれるファイルを表す抽象パスのリスト
	 */
	private List pathList(List list, File[] members, int depth, boolean withDirectory){
		int nextDepth = depth - 1;
		for(int idx = 0; idx < members.length; idx++){
			if(members[idx].isDirectory()){
				// ディレクトリだ！
				if(withDirectory){ list.add(members[idx]); }
				if(nextDepth > 0){
					File[] children = members[idx].listFiles();
					if(children != null){ pathList(list, children, nextDepth, withDirectory); }
				}
			}
			else{
				list.add(members[idx]);				// ファイルだ！
			}
		}
		return list;
	}

	/**
	 * ファイルを表すパスのみを抽出するためのフィルタークラスです。
	 * listFiles メソッドで利用します。
	 * @see java.io.File#listFiles(FilenameFilter)
	 */
	private static class FilePathFilter implements FileFilter{
		/**
		 * ファイルを表すパスのみを抽出するためのフィルターオブジェクトを
		 * 作成します。
		 */
		public FilePathFilter(){
			super();
		}

		/**
		 * 指定された抽象パス名がパス名リストに含まれる必要があるかどうかを
		 * 判定します。
		 * このメソッドは、pathname が表すパスがファイルである場合に
		 * true を返します。
		 * @param pathname テスト対象の抽象パス名
		 * @return pathname が含まれる場合は true
		 * @throws NullPointerException 引数 pathname が null の場合
		 */
		public boolean accept(File pathname){
			return pathname.isFile();
		}
	}

	/**
	 * ディレクトリを表すパスのみを抽出するためのフィルタークラスです。
	 * listFiles メソッドで利用します。
	 * @see java.io.File#listFiles(FilenameFilter)
	 */
	private static class DirectoryPathFilter implements FileFilter{
		/**
		 * ディレクトリを表すパスのみを抽出するためのフィルターオブジェクトを
		 * 作成します。
		 */
		public DirectoryPathFilter(){
			super();
		}

		/**
		 * 指定された抽象パス名がパス名リストに含まれる必要があるかどうかを
		 * 判定します。
		 * このメソッドは、pathname が表すパスがディレクトリである場合に
		 * true を返します。
		 * @param pathname テスト対象の抽象パス名
		 * @return pathname が含まれる場合は true
		 * @throws NullPointerException 引数 pathname が null の場合
		 */
		public boolean accept(File pathname){
			return pathname.isDirectory();
		}
	}
}

