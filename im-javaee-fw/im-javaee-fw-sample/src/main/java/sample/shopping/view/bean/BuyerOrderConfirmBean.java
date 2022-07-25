package sample.shopping.view.bean;

import java.util.HashMap;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.intra_mart.framework.base.service.ServiceManager;
import org.intra_mart.framework.base.service.ServiceManagerException;
import org.intra_mart.framework.base.service.ServicePropertyException;
import org.intra_mart.framework.base.message.MessageManager;
import org.intra_mart.framework.base.message.MessageManagerException;
import org.intra_mart.framework.base.web.bean.HelperBean;
import org.intra_mart.framework.base.web.bean.HelperBeanException;

/**
 * 注文情報を画面用に取得するBeanです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class BuyerOrderConfirmBean extends HelperBean {
    private HashMap items;
    private HashMap infos;

    public BuyerOrderConfirmBean() throws HelperBeanException {
        this.items = null;
        this.infos = null;
    }

    /**
     * 初期化します。
     * ここではリクエストの内容から表示用の情報を生成します。
     *
     * @throws HelperBeanException 初期化時に例外が発生
     */
    public void init() throws HelperBeanException {
        getInfos();
    }

    /**
     * 注文情報を取得します。
     *
     * @return 注文情報
     * @throws HelperBeanException 注文情報取得時に例外が発生
     */
    public HashMap getInfos() throws HelperBeanException {
        if (this.infos == null) {
            try {
                this.infos = new HashMap();
                HttpServletRequest request = getRequest();
                Locale locale = ServiceManager.getServiceManager().getLocale(getRequest(), getResponse());

                // 姓のセット
                this.infos.put("famiry_name", request.getParameter("famiry_name"));

                // 名のセット
                this.infos.put("first_name", request.getParameter("first_name"));

                // 郵便番号のセット
                String post =
                    request.getParameter("post_first")
                        + "-"
                        + request.getParameter("post_second");

                this.infos.put("post", post);

                // 都道府県のセット
                String departmentCd = request.getParameter("department");

                if (departmentCd != null) {
                    String[] departmentList = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "list.department", null, locale).split(",");
                    this.infos.put("department", departmentList[Integer.parseInt(departmentCd)]);
                }

                // 住所1,住所2のセット
                this.infos.put("address1", request.getParameter("address1"));
                this.infos.put("address2", request.getParameter("address2"));

                // 電話番号のセット
                this.infos.put("tel_no", request.getParameter("tel_no"));

                // 生年月日のセット
                String birth;

                birth = request.getParameter("birth_year") + "/" + request.getParameter("birth_month") + "/" + request.getParameter("birth_date");
                this.infos.put("birth", birth);

                // 支払方法のセット
                String payMethod = "";
                if (request.getParameter("pay_method").equals("single")) {
                    payMethod = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "order.pay_single", null, locale);
                } else if (request.getParameter("pay_method").equals("installment")) {
                    payMethod = MessageManager.getMessageManager().getMessage("sample.shopping.conf.shopping", "order.pay_installment", null, locale);
                }
                this.infos.put("pay_method", payMethod);
            } catch(MessageManagerException e) {
                throw new HelperBeanException(e.getMessage(), e);
            } catch(ServiceManagerException e) {
                throw new HelperBeanException(e.getMessage(), e);
            } catch(ServicePropertyException e) {
                throw new HelperBeanException(e.getMessage(), e);
            }
        }

        return this.infos;
    }

    /**
     * 姓を取得します。
     *
     * @return 姓
     */
    public String getFamiry_name() {
        return (String)this.infos.get("famiry_name");
    }

    /**
     * 名を取得します。
     *
     * @return 名
     */
    public String getFirst_name() {
        return (String)this.infos.get("first_name");
    }

    /**
     * 郵便番号を取得します。
     *
     * @return 郵便番号
     */
    public String getPost() {
        return (String)this.infos.get("post");
    }

    /**
     * 地域を取得します。
     *
     * @return 地域
     */
    public String getDepartment() {
        return (String)this.infos.get("department");
    }

    /**
     * 住所1を取得します。
     *
     * @return 住所1
     */
    public String getAddress1() {
        return (String)this.infos.get("address1");
    }

    /**
     * 住所2を取得します。
     *
     * @return 住所2
     */
    public String getAddress2() {
        return (String)this.infos.get("address2");
    }

    /**
     * 電話番号を取得します。
     *
     * @return 電話番号
     */
    public String getTel_no() {
        return (String)this.infos.get("tel_no");
    }

    /**
     * 生年月日を取得します。
     *
     * @return 生年月日
     */
    public String getBirth() {
        return (String)this.infos.get("birth");
    }

    /**
     * 支払方法を取得します。
     *
     * @return 支払方法
     */
    public String getPay_method() {
        return (String)this.infos.get("pay_method");
    }
}
