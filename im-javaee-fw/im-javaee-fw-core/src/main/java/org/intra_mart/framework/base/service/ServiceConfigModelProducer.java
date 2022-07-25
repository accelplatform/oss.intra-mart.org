/*
 * ServiceConfigModelProducer.java
 *
 * Created on 2004/01/12, 14:15
 */
package org.intra_mart.framework.base.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.intra_mart.framework.util.XMLDocumentProducer;
import org.intra_mart.framework.util.XMLNode;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * サービスコンフィグモデルを作成するクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class ServiceConfigModelProducer {

    /**
     * ServiceConfigModelを取得します。
     * 
     * @param fileName XMLリソース名
     * @return ServiceConfigModel
     * @throws IllegalArgumentException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    ServiceConfigModel createServiceConfigModel(String fileName)
            throws IllegalArgumentException, ParserConfigurationException,
            SAXException, IOException {
        ServiceConfigModel model = null;
        XMLDocumentProducer producer = new XMLDocumentProducer();
        Document doc = producer.getDocument(fileName);
        Node node = producer.getRoot(doc);
        XMLNode root = new XMLNode(node);
        model = getServiceConfigModel(root);
        return model;
    }

    /**
     * ServiceConfigModelを取得します。
     * 
     * @param rootNode
     * @return ServiceConfigModel
     */
    private ServiceConfigModel getServiceConfigModel(XMLNode rootNode) {
        ServiceConfigModel config = new ServiceConfigModel();
        HashMap map = getServiceModels(rootNode);
        config.setServices(map);

        XMLNode inputErrorPageNode = rootNode
                .lookup(ErrorPageModel.P_ID_INPUT_ERROR);
        config.setInputErrorPage(getInputErrorPage(inputErrorPageNode));

        XMLNode serviceErrorPageNode = rootNode
                .lookup(ErrorPageModel.P_ID_SERVICE_ERROR);
        config.setServiceErrorPage(getServiceErrorPage(serviceErrorPageNode));

        XMLNode systemErrorPageNode = rootNode
                .lookup(ErrorPageModel.P_ID_SYSTEM_ERROR);
        config.setSystemErrorPage(getSystemErrorPage(systemErrorPageNode));

        return config;

    }

    /**
     * サービスモデル一覧を取得
     * 
     * @param rootNode
     * @return
     */
    private HashMap getServiceModels(XMLNode rootNode) {
        HashMap services = new HashMap();
        XMLNode[] serviceNodes = rootNode.select(ServiceModel.ID);
        for (int i = 0; i < serviceNodes.length; i++) {
            ServiceModel service = getServiceModel(serviceNodes[i]);
            services.put(service.getServiceId(), service);
        }
        return services;
    }

    /**
     * 
     * @param rootNode
     * @return
     */
    private ServiceModel getServiceModel(XMLNode serviceNode) {
        ServiceModel service = new ServiceModel();

        service.setServiceId(getServiceId(serviceNode));
        service.setServiceControllerName(getServiceControllerName(serviceNode));
        service.setTransitionName(getTransitionName(serviceNode));

        // デフォルトネクストページの設定
//        XMLNode defaultNextPageNode = serviceNode
//                .lookup(NextPageModel.P_ID_DEFAULT_NEXT_PAGE);
        service.setDefaultNextPage(getDefaultNextPage(serviceNode));

        // ネクストページの設定
        ArrayList nextPages = getNextPages(serviceNode);
        for (int i = 0; i < nextPages.size(); i++) {
            NextPageModel nextPage = (NextPageModel) nextPages.get(i);
            service.setNextPage(nextPage.getPageKey(), nextPage);
        }

        // 入力エラーページの設定
        ArrayList inputErrorPages = getInputErrorPages(serviceNode);
        for (int i = 0; i < inputErrorPages.size(); i++) {
            ErrorPageModel errorPage = (ErrorPageModel) inputErrorPages.get(i);
            if (errorPage.getPageKey() == null) {
                service.setDefaultInputErrorPage(errorPage);
            } else {
                service.setInputErrorPage(errorPage.getPageKey(), errorPage);
            }
        }

        // サービスエラーページの設定
        ArrayList serviceErrorPages = getServiceErrorPages(serviceNode);
        for (int i = 0; i < serviceErrorPages.size(); i++) {
            ErrorPageModel errorPage = (ErrorPageModel) serviceErrorPages
                    .get(i);
            if (errorPage.getPageKey() == null) {
                service.setDefaultServiceErrorPage(errorPage);
            } else {
                service.setServiceErrorPage(errorPage.getPageKey(), errorPage);
            }
        }

        // システムエラーページの設定
        ArrayList systemErrorPages = getSystemErrorPages(serviceNode);
        for (int i = 0; i < systemErrorPages.size(); i++) {
            ErrorPageModel errorPage = (ErrorPageModel) systemErrorPages.get(i);
            if (errorPage.getPageKey() == null) {
                service.setDefaultSystemErrorPage(errorPage);
            } else {
                service.setSystemErrorPage(errorPage.getPageKey(), errorPage);
            }
        }

        return service;

    }

    /**
     * サービスＩＤの取得
     * 
     * @param serviceNode
     * @return
     */
    private String getServiceId(XMLNode serviceNode) {
        // TODO id がない時は例外を出力するべき？
        String id = serviceNode.getString(ServiceModel.P_ID_SERVICE_ID);
        return id;
    }

    /**
     * サービスコントローラ取得
     * 
     * @param serviceNode
     * @return
     */
    private String getServiceControllerName(XMLNode serviceNode) {

        return serviceNode.getString(ServiceModel.P_ID_SERVICE_CONTROLL);
    }

    /**
     * トランジッションクラスを取得
     * 
     * @param serviceNode
     * @return
     */
    private String getTransitionName(XMLNode serviceNode) {

        return serviceNode.getString(ServiceModel.P_ID_SERVICE_TRANSITION);
    }

    /**
     * 入力エラーページ一覧を取得
     * 
     * @param serviceNode
     * @return
     */
    private ArrayList getInputErrorPages(XMLNode serviceNode) {
        ArrayList errorPages = new ArrayList();
        XMLNode[] errorPageNodes = serviceNode
                .select(ErrorPageModel.P_ID_INPUT_ERROR);

        for (int i = 0; i < errorPageNodes.length; i++) {
            ErrorPageModel inputError = getErrorPage(errorPageNodes[i]);
            errorPages.add(inputError);
        }
        return errorPages;
    }

    /**
     * サービスエラーページ一覧を取得
     * 
     * @param serviceNode
     * @return
     */
    private ArrayList getServiceErrorPages(XMLNode serviceNode) {
        ArrayList errorPages = new ArrayList();
        XMLNode[] errorPageNodes = serviceNode
                .select(ErrorPageModel.P_ID_SERVICE_ERROR);

        for (int i = 0; i < errorPageNodes.length; i++) {
            ErrorPageModel inputError = getErrorPage(errorPageNodes[i]);
            errorPages.add(inputError);
        }
        return errorPages;
    }

    /**
     * システムエラーページ一覧を取得
     * 
     * @param serviceNode
     * @return
     */
    private ArrayList getSystemErrorPages(XMLNode serviceNode) {
        ArrayList errorPages = new ArrayList();
        XMLNode[] errorPageNodes = serviceNode
                .select(ErrorPageModel.P_ID_SYSTEM_ERROR);

        for (int i = 0; i < errorPageNodes.length; i++) {
            ErrorPageModel inputError = getErrorPage(errorPageNodes[i]);
            errorPages.add(inputError);
        }
        return errorPages;
    }

    /**
     * 入力エラーページを取得
     * 
     * @param node
     * @return
     */
    private ErrorPageModel getInputErrorPage(XMLNode node) {
        return getErrorPage(node);
    }

    /**
     * サービスエラーページを取得
     * 
     * @param node
     * @return
     */
    private ErrorPageModel getServiceErrorPage(XMLNode node) {
        return getErrorPage(node);
    }

    /**
     * システムエラーページを取得
     * 
     * @param node
     * @return
     */
    private ErrorPageModel getSystemErrorPage(XMLNode node) {
        return getErrorPage(node);
    }

    /**
     * エラーページのモデルを取得
     * 
     * @param node
     * @return
     */
    private ErrorPageModel getErrorPage(XMLNode node) {
        ErrorPageModel errorPage = new ErrorPageModel();
        String key = null;
        String page = null;

        // nodeがNullの時もありえる
        if (node != null) {
            key = node.getString(ErrorPageModel.P_ID_PAGE_KEY);
            page = node.getString(ErrorPageModel.P_ID_ERROR_PAGE);
        }
        errorPage.setPageKey(key);
        errorPage.setErrorPage(page);

        return errorPage;
    }

    /**
     * デフォルトネクストページの取得
     * 
     * @param serviceNode
     * @return
     */
    private NextPageModel getDefaultNextPage(XMLNode serviceNode) {
        NextPageModel nextPage = null;
        String page = null;

        XMLNode[] nodeList = serviceNode.select(NextPageModel.P_ID_NEXT_PAGE);
        for(int i = 0; i < nodeList.length; i ++) {
        	if(nodeList[i].getString(NextPageModel.P_ID_PAGE_KEY) == null) {
        		nextPage = new NextPageModel();
        		nextPage.setPagePath(nodeList[i].getString(NextPageModel.P_ID_PAGE_PATH));
        	}
        }

        return nextPage;
    }

    /**
     * ネクストページ一覧を取得
     * 
     * @param serviceNode
     * @return
     */
    private ArrayList getNextPages(XMLNode serviceNode) {
        ArrayList nextPages = new ArrayList();
        XMLNode[] nextPageNodes = serviceNode
                .select(NextPageModel.P_ID_NEXT_PAGE);

        for (int i = 0; i < nextPageNodes.length; i++) {
            NextPageModel nextPage = getNextPage(nextPageNodes[i]);
            nextPages.add(nextPage);
        }
        return nextPages;
    }

    /**
     * ネストページの取得
     * 
     * @param nextPageNode
     * @return
     */
    private NextPageModel getNextPage(XMLNode nextPageNode) {
        NextPageModel nextPage = new NextPageModel();

        String key = nextPageNode.getString(NextPageModel.P_ID_PAGE_KEY);
        String page = nextPageNode.getString(NextPageModel.P_ID_PAGE_PATH);

        nextPage.setPageKey(key);
        nextPage.setPagePath(page);

        return nextPage;
    }

}