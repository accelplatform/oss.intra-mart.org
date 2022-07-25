package org.intra_mart.common.aid.jdk.java.lang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * ユーティリティ的な実装を提供するクラスローダです。
 * 通常のクラスローダとしても利用可能です。
 */
public class SearchableClassLoader extends ExtendedClassLoader {
/*
	public static void main(String[] args){
		try{
			// Ver.5インストールディレクトリ
			File parentDir = new File("E:/work/bm51/srv");

			// 親クラスローダ
			// 捨てても良いように、新規作成。
			// 現在の実行環境に設定されているクラスパス以外を有効にするため
			ExtendedClassLoader ecl = new ExtendedClassLoader();
			// -cp に設定されていないが、
			// これから検索するクラスに必要なクラス群
			ecl.addClassArchiveLibrary(new File(parentDir, "doc/imart/WEB-INF/lib"));
			ecl.addClassArchiveLibrary(new File("Z:/BaseModule/V50/materials/library/axis"));
			ecl.addClassArchive(new File("Z:/BaseModule/V50/materials/library/struts/struts.jar"));
			ecl.addClassArchive(new File("Z:/BaseModule/V50/materials/library/j2ee/j2ee.jar"));

			// クラスローダの作成
			AdvancedClassLoader acl = new AdvancedClassLoader(ecl);
			// 検索対象の設定
			acl.addClassArchiveLibrary(new File(parentDir, "bin"));

			// インタフェースまたはクラスの指定。
			// 指定したインタフェースを実装またはクラスを継承しているクラスを検索。
			// 間接的な関係でもＯＫ。例えばスーパークラスが実装しているインタフェースを指定しても、
			// ちゃんと検索してくれます。
			// 例：java.util.Map を指定すると、java.util.AbstractMap だけではなく、
			//     java.util.HashTable も検索結果として返される。
//			Class interfaceClass = acl.loadClass("java.util.Map");
			Class interfaceClass = acl.loadClass("javax.servlet.Filter");
			long startTime1 = System.currentTimeMillis();
				Class[] subClasses1 = acl.getImplementedClasses(interfaceClass);
			long endTime1 = System.currentTimeMillis();
			long startTime2 = System.currentTimeMillis();
				Class[] subClasses2 = acl.getImplementedClasses(interfaceClass);
			long endTime2 = System.currentTimeMillis();

			// 検索結果の表示
			System.out.println(Arrays.asList(subClasses1));
			System.out.println("処理時間[１回目]：" + (endTime1 - startTime1) + "[ms]");
			System.out.println("処理時間[２回目]：" + (endTime2 - startTime2) + "[ms]");
			// クラスローダがクラス情報をメモリ中に保持するので、
			// ２回目はベラボーに速い。
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
*/

	/**
	 * 新しいクラスローダを生成します。
	 * このコンストラクタは、スーパークラスのデフォルトコンストラクタを呼び出します。
	 * @throws SecurityException セキュリティマネージャが存在し、その checkCreateClassLoader メソッドが新しいクラスローダの作成を許可しない場合
	 */
	public SearchableClassLoader() {
		super();
	}

	/**
	 * 指定された親クラスローダを使って、委譲のために新しいクラスローダを作成します。
	 * @param parent 親のクラスローダ
	 * @throws SecurityException セキュリティマネージャが存在し、その checkCreateClassLoader メソッドが新しいクラスローダの作成を許可しない場合
	 */
	public SearchableClassLoader(ClassLoader parent) {
		super(parent);
	}

	/**
	 * 指定されたパスがファイルを表している場合、jar または zip を指定されたと判断して、
	 * {@link ResourceLoader#addClassArchive(File)}を呼び出します。
	 * 指定されたパスがディレクトリを表している場合、
	 * {@link ResourceLoader#addClassPath(File)}および
	 * {@link ResourceLoader#addClassArchiveLibrary(File)}を呼び出します。
	 * @param file クラスを検索するパス
	 */
	public void addSearchPath(File file){
		if(file.isFile()){
			this.addClassArchive(file);
		}
		if(file.isDirectory()){
			this.addClassPath(file);
			this.addClassArchiveLibrary(file);
		}
	}

	/**
	 * 指定されたクラスと代入互換のあるインスタンスを生成できるクラスを検索します。
	 * 「代入互換がある」とは、以下のいずれかの条件をみたすクラスです。
	 * <ul>
	 * <li>同一のクラスである
	 * <li>継承している親クラスが指定したクラスと同一
	 * <li>実装しているインタフェースが指定した（インタフェースを表す）クラスと同一
	 * </ul>
	 * @param clazz
	 * @return
	 * @throws ZipException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Class[] getImplementedClasses(Class clazz) throws ZipException, IOException, ClassNotFoundException{
		Collection collection = new HashSet();
		String[] names = this.getClassNames();

		for(int idx = 0; idx < names.length; idx++){
			Class loadedClass = this.loadClass(names[idx]);
			if(this.isFamiry(clazz, loadedClass)){
				collection.add(loadedClass);
			}
		}

		return (Class[]) collection.toArray(new Class[collection.size()]);
	}

	private boolean isFamiry(Class ancestor, Class subClass){
		if(ancestor.equals(subClass)){
			return true;
		}
		else{
			Class superClass = subClass.getSuperclass();
			if(superClass != null){
				if(this.isFamiry(ancestor, superClass)){
					return true;
				}
			}

			Class[] interfaces = subClass.getInterfaces();
			for(int idx = 0; idx < interfaces.length; idx++){
				if(this.isFamiry(ancestor, interfaces[idx])){
					return true;
				}
			}
		}
		return false;
	}

	private String[] getClassNames() throws ZipException, IOException{
		Collection collection = new ArrayList();

		File[] paths = this.getClassPaths();
		for(int idx = 0; idx < paths.length; idx++){
			collection.addAll(this.getClassNames(paths[idx], ""));
		}

		File[] files = this.getClassArchives();
		for(int idx = 0; idx < files.length; idx++){
			ZipFile zipFile = new ZipFile(files[idx]);
			Enumeration enumeration = zipFile.entries();
			while(enumeration.hasMoreElements()){
				ZipEntry entry = (ZipEntry) enumeration.nextElement();
				String name = entry.getName();
				if(name.endsWith(".class")){
					collection.add(name.substring(0, name.length() - 6).replace('/', '.'));
				}
			}
		}

		return (String[]) collection.toArray(new String[collection.size()]);
	}

	private Collection getClassNames(File directory, String plefix){
		Collection collection = new ArrayList();
		File[] files = directory.listFiles();
		if(files != null){
			for(int idx = 0; idx < files.length; idx++){
				if(files[idx].isFile()){
					String name = files[idx].getName();
					if(name.endsWith(".class")){
						collection.add(plefix + name.substring(0, name.length() - 6));
					}
				}
				if(files[idx].isDirectory()){
					collection.addAll(this.getClassNames(files[idx], plefix + files[idx].getName() + "."));
				}
			}
		}
		return collection;
	}
}
