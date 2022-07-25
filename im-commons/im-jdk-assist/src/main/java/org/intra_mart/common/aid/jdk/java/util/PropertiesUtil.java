package org.intra_mart.common.aid.jdk.java.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * java.util.Properties を、より便利に利用するためのインタフェースを提供します。<P>
 *
 */
public class PropertiesUtil{
	/**
	 * Properties テーブル内のプロパティリスト (キーと要素のペア) を、load メソッドを使って Properties テーブルにロードするのに適切なフォーマットで出力ストリームに書き込みます。<p>
	 * このメソッドは、
	 * {@link java.util.Properties#store(OutputStream, String)} を
	 * 拡張したもので、出力内容は行単位で文字列ソートされます。
	 * @param properties 出力元となる情報
	 * @param out 出力ストリーム
	 * @param header プロパティリストの記述
	 * @throws IOException このプロパティリストを指定した出力ストリームに書き込んで、IOException がスローされた場合
	 * @throws ClassCastException この Properties オブジェクトに、String ではないキーまたは値が格納されている場合
	 * @throws NullPointerException out が null の場合
	 */
	public static void store(Properties properties, OutputStream out, String header) throws IOException{
		// 出力情報の取得(通常はファイル出力するところをメモリ内で構築)
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		properties.store(baos, header);

		// 順序をソート
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);
		Set set = new TreeSet(new PropertiesLineComparator());
		while(true){
			String line = br.readLine();
			if(line != null){ set.add(line); } else{ break; }
		}
		br.close();

		// 出力
		OutputStreamWriter osw = new OutputStreamWriter(out);
		BufferedWriter bw = new BufferedWriter(osw);
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			bw.write((String) iterator.next());
			bw.newLine();
		}
		bw.flush();
	}

	/**
	 * 唯一のコンストラクタ。
	 * 他者からのインスタンス化を防止するために隠蔽化。
	 */
	private PropertiesUtil(){
		super();
	}

	private static class PropertiesLineComparator implements Comparator{
		protected PropertiesLineComparator(){
			super();
		}

		/**
		 * 与えられた二つのオブジェクトの順序を比較します。
		 * ２つの引数 o1 および o2 は、ともに String 型として扱います。
		 */
		public int compare(Object o1, Object o2){
			String s1 = (String) o1;
			String s2 = (String) o2;

			if(s1.startsWith("#")){
				if(s2.startsWith("#")){
					return s1.compareTo(s2);
				}
				else{
					return -1;
				}
			}
			else{
				if(s2.startsWith("#")){
					return 1;
				}
				else{
					return s1.compareTo(s2);
				}
			}
		}
	}
}

