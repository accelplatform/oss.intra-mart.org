package org.intra_mart.common.aid.jdk.java.util;

/**
 * java.util.ResourceBundle のファイル版です。 <BR>
 * <BR>
 * クラスパスではなく実際のファイルパスに依存した方法でプロパティを取得します。
 * 
 */

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

public class FileResourceBundle extends ResourceBundle {

    /**
     * 拡張子の定数
     */
    private String suffix = "properties";

    /**
     * ロケール
     */
    private Locale locale = null;

    /**
     * プロパティ
     */
    private Hashtable myProperties = new Hashtable();

    /**
     * パス
     */
    private String path = null;

    /**
     * プロパティリソースを作成します。
     * 
     * @param path
     *            ファイルパス
     * @throws MissingResourceException
     * @throws IOException
     */

    public FileResourceBundle(String path) throws MissingResourceException,
            IOException {
        this.locale = Locale.getDefault();
        this.path = path;
        loadProperties(this.path, this.locale);
    }

    /**
     * プロパティリソースを作成します。<BR>
     * <BR>
     * @param path
     *            ファイルパス
     * @param locale
     *            ロケール
     * @throws MissingResourceException
     * @throws IOException
     */
    public FileResourceBundle(String path, Locale locale)
            throws MissingResourceException, IOException {
        this.locale = locale;
        this.path = path;
        loadProperties(this.path, this.locale);
    }

    /**
     * プロパティリソースを作成します。
     * 
     * @param path
     *            ファイルパス
     * @throws MissingResourceException
     * @throws IOException
     */

    public FileResourceBundle(String path,String ext) throws MissingResourceException,
            IOException {
        this.locale = Locale.getDefault();
        this.path = path;
        this.suffix = ext;
        loadProperties(this.path, this.locale);
    }

    /**
     * プロパティリソースを作成します。<BR>
     * <BR>
     * @param path
     *            ファイルパス
     * @param locale
     *            ロケール
     * @throws MissingResourceException
     * @throws IOException
     */
    public FileResourceBundle(String path, String ext,Locale locale)
            throws MissingResourceException, IOException {
        this.locale = locale;
        this.path = path;
        this.suffix = ext;
        loadProperties(this.path, this.locale);
    }

    /**
     * ファイルの存在をチェックします。<BR>
     * <BR>
     * @param path
     *            ファイルパス
     * @return ファイルが存在する場合はtrue
     */
    private boolean exist(String path) {
        File file = new File(path);
        return file.isFile();
    }

    /**
     * キーの列挙を返します。 <BR>
     * <BR>
     * 
     * @return @see java.util.ResourceBundle#getKeys()
     */
    public Enumeration getKeys() {
        return this.myProperties.keys();
    }

    /**
     * ロケールを返します。 <BR>
     * 
     * @return ロケール
     * @see java.util.ResourceBundle#getLocale()
     */
    public Locale getLocale() {
        return this.locale;
    }

    /**
     * キーに対応したオブジェクトを返します。 <BR>
     * <BR>
     * 
     * @param key
     *            キー
     * @return 取得したオブジェクト
     * @throws MissingResourceException
     * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
     */
    protected Object handleGetObject(String key)
            throws MissingResourceException {

        String result = (String) this.myProperties.get(key);

        if (result == null) {
            String message = "";
            try {
                message = ResourceBundle.getBundle(this.getClass().getPackage().getName() +".i18n")
                        .getString(
                        "FileResourceBundle.MissingResource");
            } catch (Exception e) {
            }
            message += " : key = " + key;

            throw new MissingResourceException(message, this.path, key);
        }
        return this.myProperties.get(key);
    }

    /**
     * ファイルからプロパティをロードします。<BR>
     * <BR>
     * 以下の順でファイルをロードし、プロパティを上書きしていきます。
     * <ol>
     * <li><i><code>path</code> </i>.properties
     * <li><i><code>path</code> </i>_ <i>言語 </i>.properties（デフォルトロケール）
     * <li><i><code>path</code> </i>_ <i>言語 </i>_ <i>国
     * </i>.properties（デフォルトロケール）
     * <li><i><code>path</code> </i>_ <i>言語 </i>_ <i>国 </i>_ <i>バリアント
     * </i>.properties（デフォルトロケール）
     * <li><i><code>path</code> </i>_ <i>言語 </i>.properties（目的のロケール）
     * <li><i><code>path</code> </i>_ <i>言語 </i>_ <i>国
     * </i>.properties（目的のロケール）
     * <li><i><code>path</code> </i>_ <i>言語 </i>_ <i>国 </i>_ <i>バリアント
     * </i>.properties（目的のロケール）
     * </ol>
     * 
     * @param path
     *            パス（ロケール、サフィックスなし）
     * @param locale
     *            ロケール
     * @return プロパティのHashtable
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void loadProperties(String path, Locale locale)
            throws FileNotFoundException, IOException {

        String language = null;
        String country = null;
        String variant = null;
        String localeString = path;
        String filename = null;
 
        // 基底
        filename = localeString + "." + suffix;
        putHashtable(filename);

        // 言語のみ（目的のロケール）
        language = locale.getLanguage();
        if (language != null && !language.equals("")) {
            localeString += "_" + language;
            filename = localeString + "." + suffix;
            putHashtable(filename);
        }

        // 言語＋国（目的のロケール）
        country = locale.getCountry();
        if (country != null && !country.equals("")) {
            localeString += "_" + country;
            filename = localeString + "." + suffix;
            putHashtable(filename);
        }

        // 言語＋国＋バリアント（目的のロケール）
        variant = locale.getVariant();
        if (variant != null && !variant.equals("")) {
            localeString += "_" + variant;
            filename = localeString + "." + suffix;
            putHashtable(filename);
        }
    }

    /**
     * ファイルからプロパティをロードします。 <BR>
     * <BR>
     * 
     * @param filename
     *            プロパティをロードするファイル
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void putHashtable(String filename)
            throws IOException, FileNotFoundException {

        PropertyResourceBundle temp;
        
        if (exist(filename)) {
            temp = new PropertyResourceBundle(new BufferedInputStream(
                    new FileInputStream(filename)));
            Enumeration keys = temp.getKeys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                this.myProperties.put(key, temp.getString(key));
            }
        }
    }

}