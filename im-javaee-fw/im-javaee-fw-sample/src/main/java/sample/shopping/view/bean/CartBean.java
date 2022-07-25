package sample.shopping.view.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.intra_mart.framework.base.event.EventException;
import org.intra_mart.framework.base.web.bean.HelperBean;
import org.intra_mart.framework.base.web.bean.HelperBeanException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.event.GetItemEvent;
import sample.shopping.model.event.GetItemEventResult;
import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.util.CurrencyView;
import sample.shopping.view.object.SampleCartViewObject;

/**
 * カート画面表示時のBeanです。
 *
 * @author NTTDATA intra-mart
 * @version 1.0
 */
public class CartBean extends HelperBean {
    private Collection carts;
    private Collection strutsCarts;
    private long globalSum;

    /**
     * CartBeanを初期化します。
     *
     * @throws HelperBeanException 初期化時に例外が発生
     */
    public CartBean() throws HelperBeanException {
    }

    /**
     * CartBeanの初期化関数。必要な情報を取得します。
     *
     * @throws HelperBeanException 初期化時に例外が発生
     */
    public void init() throws HelperBeanException {
        Vector carts = new Vector();
        Vector strutsCarts = new Vector();
        HashMap datas = null;
        long price = 0;
        HttpServletRequest request = getRequest();

        // キー情報の取り出し
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new HelperBeanException();
        }
        datas = (HashMap)session.getAttribute("IFW_CART_ITEMS");

        if (datas == null) {
            datas = new HashMap();
        }

        // 商品一覧を作成
        Iterator cds = datas.keySet().iterator();
        GetItemEvent event = (GetItemEvent)createEvent("sample.shopping.conf.shopping", "get_item");
        GetItemEventResult eventResult;
        SampleCartViewObject cart;
        Integer amount;
        SampleItemModelObject tempItem;
        SampleItemModelObject strutsItem;
        SampleCartViewObject strutsCart;
        String item_cd;

    	try {
	        while (cds.hasNext()) {
	        	item_cd = (String)cds.next();
	        	
	        	// 合計を取得
        		amount = (Integer)datas.get(item_cd);

        		// 商品情報を取得
        		event.setCode(item_cd);
				eventResult = (GetItemEventResult)dispatchEvent(event);
	        	tempItem = eventResult.getItem();
	        	
	        	// オブジェクト初期化
        		cart = new SampleCartViewObject();
                strutsCart = new SampleCartViewObject();
        		
	        	if (tempItem != null) {
		        	cart.setItem(tempItem);
	                cart.setAmount(amount.intValue());
	                cart.setIsValid(true);
	                strutsItem = tempItem;
	                strutsCart.setItem(strutsItem);
	                strutsCart.setAmount(amount.intValue());
	                strutsCart.setIsValid(true);
	                
	                // 総合計の設定
	                price += (new Long(cart.getSumPrice())).longValue();
	        	} else {
	        		// 空の商品情報を生成
	        		tempItem = new SampleItemModelObject();
	        		tempItem.setCode(item_cd);
	        		tempItem.setName("\"" + item_cd + "\"がDB上に見つかりません");
	        		tempItem.setImagePath("j2ee/sample/image/item/now_printing.gif");
	
		        	cart.setItem(tempItem);
	                cart.setAmount(amount.intValue());
	                cart.setIsValid(false);
	                strutsItem = tempItem;
	                strutsCart.setItem(strutsItem);
	                strutsCart.setAmount(amount.intValue());
	                strutsCart.setIsValid(false);
	        	}

                strutsItem.setImagePath(request.getContextPath() + "/" + strutsItem.getImagePath());
                carts.add(cart);
                strutsCarts.add(strutsCart);
	        }
		} catch (EventException e) {
            throw new HelperBeanException(e.getMessage(), e);
		} catch (SystemException e) {
            throw new HelperBeanException(e.getMessage(), e);
		} catch (ApplicationException e) {
            throw new HelperBeanException(e.getMessage(), e);
		}

        this.carts = (Collection)carts;
        this.strutsCarts = (Collection)strutsCarts;
        this.globalSum = price;
    }

    /**
     * カート情報を取得します。
     *
     * @return カートに入っている表示用商品情報のCollection
     */
    public Collection getCarts() {
        return this.carts;
    }

    /**
     * カート情報を取得します。(Struts)
     *
     * @return カートに入っている表示用商品情報のCollection
     */
    public Collection getStrutsCarts() {
        return this.strutsCarts;
    }

    /**
     * カートに入っている商品の合計金額を返却します。
     *
     * @return カートに入っている商品の合計金額
     */
    public String getGlobalSum() {
        return Long.toString(this.globalSum);
    }

    public String getGlobalSumView() {
        return CurrencyView.getString(getGlobalSum());
    }
}
