package org.intra_mart.common.aid.jdk.java.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 文字列ユーティリティクラス。<BR>
 * <BR>
 * 文字列に関する実用的な関数および定数値を提供するクラス。<BR>
 * 
 */
public class StringUtil {

    /**
     * 空文字列の定数値。<BR>
     */
    public static final String EMPTY_STRING = new String();
    
    /**
     * コンストラクタ。<BR>
     * <BR>
     * 隠蔽します。
     */
    private StringUtil() {
    }
    /**
     * コレクションの連結。<BR>
     * <BR>
     * 引数で指定した区切り文字で、コレクション内の各オブジェクトの文字列表現を連結した文字列を返す。
     * @param collection 連結を行うコレクション
     * @param delm 区切り文字
     * @return 連結した文字列
     */
    public static String join(Collection collection , String delm){
        StringBuffer joinString = new StringBuffer();
        
        for (Iterator i = collection.iterator(); i.hasNext();) {
            // 文字列に変換してバッファに追加。
            joinString.append(i.next().toString());
            // 次のエレメントが存在するなら、区切り文字を追加。
            if(i.hasNext()) {
                joinString.append(delm);
            }
        }
        
        return joinString.toString();
    }

    /**
     * オブジェクト配列の連結。<BR>
     * <BR>
     * 引数で指定した区切り文字で、オブジェクト配列の各オブジェクトの文字列表現を連結した文字列を返す。
     * @param collection 連結を行うオブジェクト配列
     * @param delm 区切り文字
     * @return 連結した文字列
     */
    public static String join(Object[] objects , String delm){
        return join(Arrays.asList(objects),delm);
    }

}
