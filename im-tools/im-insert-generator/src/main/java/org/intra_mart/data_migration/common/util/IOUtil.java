/*
 * IOUtil.java
 *
 * Created on 2005/05/17,  17:37:49
 */
package org.intra_mart.data_migration.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO関連のユーティリティです。
 *
 * @author intra-mart
 * 
 */
public class IOUtil {
    /**
     * コンストラクタ
     */
    private IOUtil() {
    }
    
    /**
     * InputStreamからOutputStreamへ出力します。
     * 
     * @param inputStream
     * @param outputStream
     */
    public static void write(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = inputStream.read(buf)) >= 0) {
            outputStream.write(buf, 0, len);
        }
    }

    /**
     * InputStreamからファイルへ出力します。
     * 
     * @param inputStream
     * @param file
     */
    public static void write(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            write(inputStream, outputStream);
        } finally {
            close(outputStream);
        }
    }
    /**
     * InputStreamをcloseします。<br>
     * inputStreamがnullの場合はなにもしません。
     * 
     * @param inputStream
     */
    public static void close(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        inputStream.close();
    }

    /**
     * OutputStreamをcloseします。<br>
     * outputStreamがnullの場合はなにもしません。
     * 
     * @param outputStream
     */
    public static void close(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            return;
        }
        outputStream.close();
    }
}
