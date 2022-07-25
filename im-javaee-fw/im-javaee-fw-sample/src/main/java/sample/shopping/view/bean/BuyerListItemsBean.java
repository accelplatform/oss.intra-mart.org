package sample.shopping.view.bean;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.intra_mart.framework.base.event.Event;
import org.intra_mart.framework.base.web.bean.HelperBean;
import org.intra_mart.framework.base.web.bean.HelperBeanException;
import org.intra_mart.framework.system.exception.ApplicationException;
import org.intra_mart.framework.system.exception.SystemException;
import sample.shopping.model.event.GetAllItemsEventResult;
import sample.shopping.model.object.SampleItemModelObject;
import sample.shopping.view.object.SampleItemViewObject;

/**
 * 商品一覧画面用の bean です。
 *
 *
 * @author NTTDATA intra-mart TY.
 * @version 1.0
 */
public class BuyerListItemsBean extends HelperBean {
    private Collection items = null;
    private Collection strutsItems = null;

    /**
     * BuyerListItemsBeanを新規に作成します。
     *
     * @throws HelperBeanException HelperBean生成時に例外が発生
     */
    public BuyerListItemsBean() throws HelperBeanException {

    }

    /**
     * 商品情報を返却
     *
     * @throws ApplicationException
     * @throws SystemException
     */
    public Collection getItems() throws ApplicationException, SystemException {
        if (this.items == null) {
            Event event = createEvent("sample.shopping.conf.shopping", "get_all_items");

            GetAllItemsEventResult event_result =
                (GetAllItemsEventResult)dispatchEvent(event);

            Collection modelItems = event_result.getItems();
            Vector viewItems = new Vector();

            Iterator iter = modelItems.iterator();

            while (iter.hasNext()) {
                SampleItemViewObject item =
                    new SampleItemViewObject(
                        (SampleItemModelObject)iter.next());

                viewItems.add(item);
            }

            this.items = (Collection)viewItems;
        }

        return this.items;
    }

    /**
     * 商品情報を返却(Struts)
     *
     * @throws ApplicationException
     * @throws SystemException
     */
    public Collection getStrutsItems()
        throws ApplicationException, SystemException {

        if (this.strutsItems == null) {
            Event event = createEvent("sample.shopping.conf.shopping", "get_all_items");

            GetAllItemsEventResult event_result =
                (GetAllItemsEventResult)dispatchEvent(event);

            Collection modelItems = event_result.getItems();
            Vector viewItems = new Vector();

            Iterator iter = modelItems.iterator();

            while (iter.hasNext()) {
                SampleItemViewObject item =
                    new SampleItemViewObject(
                        (SampleItemModelObject)iter.next());
                item.setImagePath(
                    getRequest().getContextPath() + "/" + item.getImagePath());
                viewItems.add(item);
            }

            this.strutsItems = (Collection)viewItems;
        }

        return this.strutsItems;
    }
}
