/*
 * FileResourceBundle.java
 *
 * Created on 2003/09/08, 18:00
 */

package org.intra_mart.framework.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * java.util.ResourceBundle のファイル版です。
 * クラスパスではなく実際のファイルパスに依存した方法でプロパティを取得します。
 *
 * @author INTRAMART
 * @since 4.2
 */
public class FileResourceBundle extends ResourceBundle {

    /**
     * パス
     */
    private String path = null;

    /**
     * プロパティ
     */
    private Hashtable myProperties = new Hashtable();

    /**
     * ロケール
     */
    private Locale locale = null;

    /**
     * プロパティリソースを作成します。
     * @param path ファイルパス
     * @throws IOException
     */
    public FileResourceBundle(String path)
        throws MissingResourceException, IOException {
        this.locale = Locale.getDefault();
        this.path = path;
        this.myProperties = loadProperties(this.path, this.locale);
    }

    /**
     * プロパティリソースを作成します。
     * @param path ファイルパス
     * @param locale ロケール
     * @throws IOException
     */
    public FileResourceBundle(String path, Locale locale)
        throws MissingResourceException, IOException {
        this.locale = locale;
        this.path = path;
        this.myProperties = loadProperties(this.path, this.locale);
    }

    /**
     * ファイルの存在をチェックします。
     * @param path ファイルパス
     * @return ファイルが存在するtrue
     */
    private static boolean exist(String path) {
        File file = new File(path);
        return file.isFile();
    }

    /**
     * ファイルからプロパティをロードします。
     * 以下の順でファイルをロードし、プロパティを上書きしていきます。
     * <ol>
     * <li><i><code>path</code></i>.properties
     * <li><i><code>path</code></i>_<i>言語</i>.properties（デフォルトロケール）
     * <li><i><code>path</code></i>_<i>言語</i>_<i>国</i>.properties（デフォルトロケール）
     * <li><i><code>path</code></i>_<i>言語</i>_<i>国</i>_<i>バリアント</i>.properties（デフォルトロケール）
     * <li><i><code>path</code></i>_<i>言語</i>.properties（目的のロケール）
     * <li><i><code>path</code></i>_<i>言語</i>_<i>国</i>.properties（目的のロケール）
     * <li><i><code>path</code></i>_<i>言語</i>_<i>国</i>_<i>バリアント</i>.properties（目的のロケール）
     * </ol>
     *
     * @param path パス（ロケール、サフィックスなし）
     * @param locale ロケール
     * @return プロパティのHashtable
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static Hashtable loadProperties(String path, Locale locale)
        throws FileNotFoundException, IOException {

        Locale defaultLocale = Locale.getDefault();
        String language = null;
        String country = null;
        String variant = null;
        PropertyResourceBundle temp = null;
        Hashtable result = new Hashtable();
        String localeString = path;
        String filename = null;
        final String suffix = ".properties";

        // 基底
        filename = localeString + suffix;
        putHashtable(result, filename);

        // 言語のみ（目的のロケール）
        language = locale.getLanguage();
        if (language != null && !language.equals("")) {
            localeString += "_" + language;
            filename = localeString + suffix;
            putHashtable(result, filename);
        }

        // 言語＋国（目的のロケール）
        country = locale.getCountry();
        if (country != null && !country.equals("")) {
            localeString += "_" + country;
            filename = localeString + suffix;
            putHashtable(result, filename);
        }

        // 言語＋国＋バリアント（目的のロケール）
        variant = locale.getVariant();
        if (variant != null && !variant.equals("")) {
            localeString += "_" + variant;
            filename = localeString + suffix;
            putHashtable(result, filename);
        }

        return result;
    }

    /**
     * ファイルからプロパティをロードします。
     *
     * @param hashtable 出力先のハッシュテーブル
     * @param filename プロパティをロードするファイル
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static void putHashtable(Hashtable hashtable, String filename)
        throws IOException, FileNotFoundException {

        PropertyResourceBundle temp;
        if (exist(filename)) {
            temp =
                new PropertyResourceBundle(
                    new BufferedInputStream(new FileInputStream(filename)));
            Enumeration keys = temp.getKeys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                hashtable.put(key, temp.getString(key));
            }
        }
    }

    /**
     * キーの列挙を返します。
     *
     * @return キーの列挙
     */
    public Enumeration getKeys() {
        return this.myProperties.keys();
    }

    /**
     * ResourceBundle の Locale を返します。
     *
     * @return ロケール
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * @param key
     * @return
     * @throws java.util.MissingResourceException
     */
    protected Object handleGetObject(String key)
        throws MissingResourceException {

        String result = (String)this.myProperties.get(key);

        if (result == null) {
            String message = "";
            try {
                message =
                    ResourceBundle.getBundle(
                        "org.intra_mart.framework.util.i18n").getString(
                        "FileResourceBundle.MissingResource");
            } catch (Exception e) {
            }
            message += " : key = " + key;

            throw new MissingResourceException(message, this.path, key);
        }
        return (String)this.myProperties.get(key);
    }

}
