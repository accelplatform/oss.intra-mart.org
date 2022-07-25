/*
 * ShoppingCartCustomerModelObject.java
 *
 * Created on 2002/08/30, 12:00
 */

package sample.shopping.model.object;

import java.io.Serializable;

/**
 * ショッピングカートの顧客情報のモデルオブジェクトです。
 *
 * @author NTT DATA INTRAMART CO.,LTD
 * @version 1.0
 */
public class ShoppingCartCustomerModelObject implements Serializable {

    /**
     * 姓
     */
    private String familyName;

    /**
     * 名
     */
    private String firstName;

    /**
     * 郵便番号
     */
    private String post;

    /**
     * 都道府県
     */
    private String department;

    /**
     * 住所１
     */
    private String address1;

    /**
     * 住所２
     */
    private String address2;

    /**
     * 電話番号
     */
    private String telNo;

    /**
     * 生年月日
     */
    private String birth;

    /**
     * ShoppingCartCustomerModelObjectを新規に生成します。
     */
    public ShoppingCartCustomerModelObject() {
    }

    /**
     * 姓を設定します。
     *
     * @param familyName 姓
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * 姓を取得します。
     *
     * @return 姓
     */
    public String getFamilyName() {
        return this.familyName;
    }

    /**
     * 名を設定します。
     *
     * @param firstName 名
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 名を取得します。
     *
     * @return 名
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * 郵便番号を設定します。
     *
     * @param post 郵便番号
     */
    public void setPost(String post) {
        this.post = post;
    }

    /**
     * 郵便番号を取得します。
     *
     * @return 郵便番号
     */
    public String getPost() {
        return this.post;
    }

    /**
     * 都道府県を設定します。
     *
     * @param department 都道府県
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * 都道府県を取得します。
     *
     * @return 都道府県
     */
    public String getDepartment() {
        return this.department;
    }

    /**
     * 住所１を設定します。
     *
     * @param address1 住所１
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * 住所１を取得します。
     *
     * @return 住所１
     */
    public String getAddress1() {
        return this.address1;
    }

    /**
     * 住所２を設定します。
     *
     * @param address2 住所２
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * 住所２を取得します。
     *
     * @return 住所２
     */
    public String getAddress2() {
        return this.address2;
    }

    /**
     * 電話番号を設定します。
     *
     * @param telNo 電話番号
     */
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    /**
     * 電話番号を取得します。
     *
     * @return 電話番号
     */
    public String getTelNo() {
        return this.telNo;
    }

    /**
     * 生年月日を設定します。
     *
     * @param birth 生年月日
     */
    public void setBirth(String birth) {
        this.birth = birth;
    }

    /**
     * 生年月日を取得します。
     *
     * @return 生年月日
     */
    public String getBirth() {
        return birth;
    }
}
