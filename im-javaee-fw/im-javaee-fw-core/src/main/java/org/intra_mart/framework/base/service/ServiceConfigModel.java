/*
 * ServiceConfigModel.java
 * 
 */
package org.intra_mart.framework.base.service;

import java.util.HashMap;

/**
 * サービスコンフィグモデルクラスです。
 * 
 * @author INTRAMART
 * @since 5.0
 */
class ServiceConfigModel {

    static final String ID = "service-config";

    private HashMap services;

    private ErrorPageModel inputErrorPage;

    private ErrorPageModel systemErrorPage;

    private ErrorPageModel serviceErrorPage;

    // 多言語対応
    private ServiceConfigModel parent;

    /**
     * コンストラクタです。
     */
    ServiceConfigModel() {
        setServices(new HashMap());
    }

    /**
     * Serviceを設定します。
     * 
     * @param services
     */
    void setServices(HashMap services) {
        this.services = services;
    }

    /**
     * Serviceを取得します。
     * 
     * @return
     */
    HashMap getServices() {
        return services;
    }

    /**
     * Serviceを取得します。
     * 
     * @param key
     * @return ServiceModel Service
     */
    ServiceModel getService(String key) {
        return (ServiceModel) services.get(key);
    }

    /**
     * InputErrorPageを設定します。
     * 
     * @param inputErrorPage
     */
    void setInputErrorPage(ErrorPageModel inputErrorPage) {
        this.inputErrorPage = inputErrorPage;
    }

    /**
     * InputErrorPageを取得します。
     * 
     * @return ErrorPageModel InputErrorPage
     */
    ErrorPageModel getInputErrorPage() {
        return inputErrorPage;
    }

    /**
     * SystemErrorPageを設定します。
     * 
     * @param systemErrorPage
     */
    void setSystemErrorPage(ErrorPageModel systemErrorPage) {
        this.systemErrorPage = systemErrorPage;
    }

    /**
     * SystemErrorPageを取得します。
     * 
     * @return ErrorPageModel SystemErrorPage
     */
    ErrorPageModel getSystemErrorPage() {
        return systemErrorPage;
    }

    /**
     * ServiceErrorPageを設定します。
     * 
     * @param serviceErrorPage
     */
    void setServiceErrorPage(ErrorPageModel serviceErrorPage) {
        this.serviceErrorPage = serviceErrorPage;
    }

    /**
     * ServiceErrorPageを取得します。
     * 
     * @return ErrorPageModel ServiceErrorPage
     */
    ErrorPageModel getServiceErrorPage() {
        return serviceErrorPage;
    }

    /**
     * InputErrorPagePathを取得します。
     * 
     * @param service
     * @param key
     * @return String InputErrorPagePath
     */
    String getInputErrorPagePath(String service, String key) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getInputErrorPagePath(key);
        }
        if (result == null && getParent() != null) {
            result = getParent().getInputErrorPagePath(service, key);
        }
        return result;
    }

    /**
     * InputErrorPagePathを取得します。
     * 
     * @param service
     * @return String InputErrorPagePath
     */
    String getInputErrorPagePath(String service) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getInputErrorPagePath();
        }
        if (result == null && getParent() != null) {
            result = getParent().getInputErrorPagePath(service);
        }
        return result;
    }

    /**
     * InputErrorPagePathを取得します。
     * 
     * @return String InputErrorPagePath
     */
    String getInputErrorPagePath() {
        String result = null;
        if (inputErrorPage != null) {
            result = inputErrorPage.getErrorPage();
        }
        if (result == null && getParent() != null) {
            result = getParent().getInputErrorPagePath();
        }
        return result;
    }

    /**
     * ServiceErrorPagePathを取得します。
     * 
     * @param service
     * @param key
     * @return String ServiceErrorPagePath
     */
    String getServiceErrorPagePath(String service, String key) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getServiceErrorPagePath(key);
        }
        if (result == null && getParent() != null) {
            result = getParent().getServiceErrorPagePath(service, key);
        }
        return result;
    }

    /**
     * ServiceErrorPagePathを取得します。
     * 
     * @param service
     * @return String ServiceErrorPagePath
     */
    String getServiceErrorPagePath(String service) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getServiceErrorPagePath();
        }
        if (result == null && getParent() != null) {
            result = getParent().getServiceErrorPagePath(service);
        }
        return result;
    }

    /**
     * ServiceErrorPagePathを取得します、
     * 
     * @return String ServiceErrorPagePath
     */
    String getServiceErrorPagePath() {
        String result = null;
        if (serviceErrorPage != null) {
            result = serviceErrorPage.getErrorPage();
        }
        if (result == null && getParent() != null) {
            result = getParent().getServiceErrorPagePath();
        }

        return result;
    }

    /**
     * SystemErrorPagePathを取得します。
     * 
     * @param service
     * @param key
     * @return String SystemErrorPagePath
     */
    String getSystemErrorPagePath(String service, String key) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getSystemErrorPagePath(key);
        }
        if (result == null && getParent() != null) {
            result = getParent().getSystemErrorPagePath(service, key);
        }
        return result;
    }

    /**
     * SystemErrorPagePathを取得します。
     * 
     * @param service
     * @return String SystemErrorPagePath
     */
    String getSystemErrorPagePath(String service) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getSystemErrorPagePath();
        }
        if (result == null && getParent() != null) {
            result = getParent().getSystemErrorPagePath(service);
        }
        return result;
    }

    /**
     * SystemErrorPagePathを取得します。
     * 
     * @return String getSystemErrorPagePath
     */
    String getSystemErrorPagePath() {
        String result = null;
        if (systemErrorPage != null) {
            result = systemErrorPage.getErrorPage();
        }
        if (result == null && getParent() != null) {
            result = getParent().getSystemErrorPagePath();
        }
        return result;
    }

    /**
     * NextPagePathを取得します。
     * 
     * @param service
     * @param key
     * @return String NextPagePath
     */
    String getNextPagePath(String service, String key) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getNextPagePath(key);
        }

        if (result == null && getParent() != null) {
            result = getParent().getNextPagePath(service, key);
        }

        return result;
    }

    /**
     * NextPagePathを取得します。
     * 
     * @param service
     * @return String NextPagePath
     */
    String getNextPagePath(String service) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getNextPagePath();
        }
        if (result == null && getParent() != null) {
            result = getParent().getNextPagePath(service);
        }

        return result;
    }

    /**
     * ServiceControllerClassNameを取得します。
     * 
     * @param service
     * @return String ServiceControllerClassName
     */
    String getServiceControllerClassName(String service) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getServiceControllerName();
        }

        if (result == null && getParent() != null) {
            result = getParent().getServiceControllerClassName(service);
        }
        return result;
    }

    /**
     * TransitionNameを取得します。
     * 
     * @param service
     * @return String TransitionName
     */
    String getTransitionName(String service) {
        String result = null;

        ServiceModel serviceModel = getService(service);
        if (serviceModel != null) {
            result = serviceModel.getTransitionName();
        }
        if (result == null && getParent() != null) {
            result = getParent().getTransitionName(service);
        }

        return result;
    }

    /**
     * 設定します。
     * 
     * @param parent
     */
    void setParent(ServiceConfigModel parent) {
        this.parent = parent;
    }

    /**
     * 取得します。
     * 
     * @return
     */
    ServiceConfigModel getParent() {
        return parent;
    }

}