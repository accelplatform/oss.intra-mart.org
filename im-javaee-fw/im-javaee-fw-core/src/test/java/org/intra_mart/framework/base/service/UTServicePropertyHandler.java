/*
 * UTServicePropertyHandler.java
 *
 * Created on 2003/08/20, 14:31:43
 */

package org.intra_mart.framework.base.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.intra_mart.framework.system.property.PropertyHandlerException;
import org.intra_mart.framework.system.property.PropertyParam;

/**
 * 
 *
 * @author intra-mart
 * @since 4.2
 */
public class UTServicePropertyHandler implements ServicePropertyHandler {

    private String createLocaleString(Locale locale) {
        return createLocaleStringLanguageCountryVariant(locale);
    }

    private String createLocaleStringLanguageCountryVariant(Locale locale) {
        String localeString = null;
        if (locale != null) {
            localeString =
                "_"
                    + locale.getLanguage()
                    + "_"
                    + locale.getCountry()
                    + "_"
                    + locale.getVariant();
        } else {
            localeString = "___";
        }

        return localeString;
    }

    private String createLocaleStringLanguageCountry(Locale locale) {
        String localeString = null;
        if (locale != null) {
            localeString =
                "_" + locale.getLanguage() + "_" + locale.getCountry() + "_";
        } else {
            localeString = "___";
        }

        return localeString;
    }

    private String createLocaleStringLanguage(Locale locale) {
        String localeString = null;
        if (locale != null) {
            localeString = "_" + locale.getLanguage() + "__";
        } else {
            localeString = "___";
        }

        return localeString;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public boolean isDynamic() throws ServicePropertyException {
        return true;
    }

    private String clientEncoding = null;

    public void setClientEncoding(String clientEncoding) {
        this.clientEncoding = clientEncoding;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getClientEncoding() throws ServicePropertyException {
        return this.clientEncoding;
    }

    private Locale clientLocale = null;

    public void setClientLocale(Locale clientLocale) {
        this.clientLocale = clientLocale;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public Locale getClientLocale() throws ServicePropertyException {
        return this.clientLocale;
    }

    private Map nextPagePaths = new HashMap();

    public void setNextPagePath(
        String application,
        String service,
        String path,
        Locale locale) {
        this.nextPagePaths.put(
            application + "." + service + createLocaleString(locale),
            path);
    }

    public void setNextPagePath(
        String application,
        String service,
        String path) {
        setNextPagePath(application, service, path, (Locale) null);
    }

    public void setNextPagePath(
        String application,
        String service,
        String key,
        String path,
        Locale locale) {
        this.nextPagePaths.put(
            application
                + "."
                + service
                + "."
                + key
                + createLocaleString(locale),
            path);
    }

    public void setNextPagePath(
        String application,
        String service,
        String key,
        String path) {
        setNextPagePath(application, service, key, path, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getNextPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.nextPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.nextPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.nextPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.nextPagePaths.get(
                    application + "." + service + "___");
            if (path == null) {
                throw new ServicePropertyException();
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getNextPagePath(String application, String service)
        throws ServicePropertyException {
        return getNextPagePath(application, service, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param key
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getNextPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.nextPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.nextPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.nextPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.nextPagePaths.get(
                    application + "." + service + "." + key + "___");
            if (path == null) {
                path = getNextPagePath(application, service, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @param key
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getNextPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        return getNextPagePath(application, service, key, (Locale) null);
    }

    private Map inputErrorPagePaths = new HashMap();

    public void setInputErrorPagePath(String path, Locale locale) {
        this.inputErrorPagePaths.put(createLocaleString(locale), path);
    }

    public void setInputErrorPagePath(String path) {
        setInputErrorPagePath(path, (Locale) null);
    }

    public void setInputErrorPagePath(
        String application,
        String path,
        Locale locale) {
        this.inputErrorPagePaths.put(
            application + createLocaleString(locale),
            path);
    }

    public void setInputErrorPagePath(String application, String path) {
        setInputErrorPagePath(application, path, (Locale) null);
    }

    public void setInputErrorPagePath(
        String application,
        String service,
        String path,
        Locale locale) {
        this.inputErrorPagePaths.put(
            application + "." + service + createLocaleString(locale),
            path);
    }

    public void setInputErrorPagePath(
        String application,
        String service,
        String path) {
        setInputErrorPagePath(application, service, path, (Locale) null);
    }

    public void setInputErrorPagePath(
        String application,
        String service,
        String key,
        String path,
        Locale locale) {
        this.inputErrorPagePaths.put(
            application
                + "."
                + service
                + "."
                + key
                + createLocaleString(locale),
            path);
    }

    public void setInputErrorPagePath(
        String application,
        String service,
        String key,
        String path) {
        setInputErrorPagePath(application, service, key, path, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param key
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.inputErrorPagePaths.get(
                    application + "." + service + "." + key + "___");
            if (path == null) {
                path = getInputErrorPagePath(application, service, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @param key
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        return getInputErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getInputErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.inputErrorPagePaths.get(
                    application + "." + service + "___");
            if (path == null) {
                path = getInputErrorPagePath(application, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getInputErrorPagePath(String application, String service)
        throws ServicePropertyException {
        return getInputErrorPagePath(application, service, (Locale) null);
    }

    /**
     * @param application
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getInputErrorPagePath(String application, Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        application + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path = (String)this.inputErrorPagePaths.get(application + "___");
            if (path == null) {
                path = getInputErrorPagePath(locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getInputErrorPagePath(String application)
        throws ServicePropertyException {
        return getInputErrorPagePath(application, (Locale) null);
    }

    /**
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getInputErrorPagePath(Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.inputErrorPagePaths.get(
                        createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path = (String)this.inputErrorPagePaths.get("___");
            if (path == null) {
                throw new ServicePropertyException();
            }
        }

        return path;
    }

    /**
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getInputErrorPagePath() throws ServicePropertyException {
        return getInputErrorPagePath((Locale) null);
    }

    private Map serviceErrorPaths = new HashMap();

    public void setServiceErrorPagePath(String path, Locale locale) {
        this.serviceErrorPaths.put(createLocaleString(locale), path);
    }

    public void setServiceErrorPagePath(String path) {
        setServiceErrorPagePath(path, (Locale) null);
    }

    public void setServiceErrorPagePath(
        String application,
        String path,
        Locale locale) {
        this.serviceErrorPaths.put(
            application + createLocaleString(locale),
            path);
    }

    public void setServiceErrorPagePath(String application, String path) {
        setServiceErrorPagePath(application, path, (Locale) null);
    }

    public void setServiceErrorPagePath(
        String application,
        String service,
        String path,
        Locale locale) {
        this.serviceErrorPaths.put(
            application + "." + service + createLocaleString(locale),
            path);
    }

    public void setServiceErrorPagePath(
        String application,
        String service,
        String path) {
        setServiceErrorPagePath(application, service, path, (Locale) null);
    }

    public void setServiceErrorPagePath(
        String application,
        String service,
        String key,
        String path,
        Locale locale) {
        this.serviceErrorPaths.put(
            application
                + "."
                + service
                + "."
                + key
                + createLocaleString(locale),
            path);
    }

    public void setServiceErrorPagePath(
        String application,
        String service,
        String key,
        String path) {
        setServiceErrorPagePath(application, service, key, path, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param key
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.serviceErrorPaths.get(
                    application + "." + service + "." + key + "___");
            if (path == null) {
                path = getServiceErrorPagePath(application, service, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @param key
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        return getServiceErrorPagePath(
            application,
            service,
            key,
            (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.serviceErrorPaths.get(
                    application + "." + service + "___");
            if (path == null) {
                path = getServiceErrorPagePath(application, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getServiceErrorPagePath(String application, String service)
        throws ServicePropertyException {
        return getServiceErrorPagePath(application, service, (Locale) null);
    }

    /**
     * @param application
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceErrorPagePath(String application, Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        application + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path = (String)this.serviceErrorPaths.get(application + "___");
            if (path == null) {
                path = getServiceErrorPagePath(locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getServiceErrorPagePath(String application)
        throws ServicePropertyException {
        return getServiceErrorPagePath(application, (Locale) null);
    }

    /**
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceErrorPagePath(Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.serviceErrorPaths.get(
                        createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path = (String)this.serviceErrorPaths.get("___");
            if (path == null) {
                throw new ServicePropertyException();
            }
        }

        return path;
    }

    /**
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getServiceErrorPagePath() throws ServicePropertyException {
        return getServiceErrorPagePath((Locale) null);
    }

    private Map systemErrorPagePaths = new HashMap();

    public void setSystemErrorPagePath(String path, Locale locale) {
        this.systemErrorPagePaths.put(createLocaleString(locale), path);
    }

    public void setSystemErrorPagePath(String path) {
        setSystemErrorPagePath(path, (Locale) null);
    }

    public void setSystemErrorPagePath(
        String application,
        String path,
        Locale locale) {
        this.systemErrorPagePaths.put(
            application + createLocaleString(locale),
            path);
    }

    public void setSystemErrorPagePath(String application, String path) {
        setSystemErrorPagePath(application, path, (Locale) null);
    }

    public void setSystemErrorPagePath(
        String application,
        String service,
        String path,
        Locale locale) {
        this.systemErrorPagePaths.put(
            application + "." + service + createLocaleString(locale),
            path);
    }

    public void setSystemErrorPagePath(
        String application,
        String service,
        String path) {
        setSystemErrorPagePath(application, service, path, (Locale) null);
    }

    public void setSystemErrorPagePath(
        String application,
        String service,
        String key,
        String path,
        Locale locale) {
        this.systemErrorPagePaths.put(
            application
                + "."
                + service
                + "."
                + key
                + createLocaleString(locale),
            path);
    }

    public void setSystemErrorPagePath(
        String application,
        String service,
        String key,
        String path) {
        setSystemErrorPagePath(application, service, key, path, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param key
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        String key,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + "."
                            + key
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.systemErrorPagePaths.get(
                    application + "." + service + "." + key + "___");
            if (path == null) {
                path = getSystemErrorPagePath(application, service, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @param key
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        String key)
        throws ServicePropertyException {
        return getSystemErrorPagePath(application, service, key, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getSystemErrorPagePath(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path =
                (String)this.systemErrorPagePaths.get(
                    application + "." + service + "___");
            if (path == null) {
                path = getSystemErrorPagePath(application, locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @param service
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getSystemErrorPagePath(String application, String service)
        throws ServicePropertyException {
        return getSystemErrorPagePath(application, service, (Locale) null);
    }

    /**
     * @param application
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getSystemErrorPagePath(String application, Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application
                            + createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        application + createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path = (String)this.systemErrorPagePaths.get(application + "___");
            if (path == null) {
                path = getSystemErrorPagePath(locale);
            }
        }

        return path;
    }

    /**
     * @param application
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getSystemErrorPagePath(String application)
        throws ServicePropertyException {
        return getSystemErrorPagePath(application, (Locale) null);
    }

    /**
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getSystemErrorPagePath(Locale locale)
        throws ServicePropertyException {
        String path = null;
        if (locale != null) {
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        createLocaleStringLanguageCountryVariant(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        createLocaleStringLanguageCountry(locale));
            }
            if (path == null) {
                path =
                    (String)this.systemErrorPagePaths.get(
                        createLocaleStringLanguage(locale));
            }
        }
        if (path == null) {
            path = (String)this.systemErrorPagePaths.get("___");
            if (path == null) {
                throw new ServicePropertyException();
            }
        }

        return path;
    }

    /**
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getSystemErrorPagePath() throws ServicePropertyException {
        return getSystemErrorPagePath((Locale) null);
    }

    private Map serviceControllerNames = new HashMap();

    public void setServiceControllerName(
        String application,
        String service,
        String controller,
        Locale locale) {
        this.serviceControllerNames.put(
            application + "." + service + createLocaleString(locale),
            controller);
    }

    public void setServiceControllerName(
        String application,
        String service,
        String controller) {
        setServiceControllerName(
            application,
            service,
            controller,
            (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceControllerName(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        String controller = null;
        if (locale != null) {
            if (controller == null) {
                controller =
                    (String)this.serviceControllerNames.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (controller == null) {
                controller =
                    (String)this.serviceControllerNames.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountry(locale));
            }
            if (controller == null) {
                controller =
                    (String)this.serviceControllerNames.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguage(locale));
            }
        }
        if (controller == null) {
            controller =
                (String)this.serviceControllerNames.get(
                    application + "." + service + "___");
        }

        return controller;
    }

    /**
     * @param application
     * @param service
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getServiceControllerName(String application, String service)
        throws ServicePropertyException {
        return getServiceControllerName(application, service, (Locale) null);
    }

    private Map transitionNames = new HashMap();

    public void setTransitionName(
        String application,
        String service,
        String transition,
        Locale locale) {
        this.transitionNames.put(
            application + "." + service + createLocaleString(locale),
            transition);
    }

    public void setTransitionName(
        String application,
        String service,
        String transition) {
        setTransitionName(application, service, transition, (Locale) null);
    }

    /**
     * @param application
     * @param service
     * @param locale
     * @return
     * @throws ServicePropertyException
     */
    public String getTransitionName(
        String application,
        String service,
        Locale locale)
        throws ServicePropertyException {
        String transition = null;
        if (locale != null) {
            if (transition == null) {
                transition =
                    (String)this.transitionNames.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountryVariant(locale));
            }
            if (transition == null) {
                transition =
                    (String)this.transitionNames.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguageCountry(locale));
            }
            if (transition == null) {
                transition =
                    (String)this.transitionNames.get(
                        application
                            + "."
                            + service
                            + createLocaleStringLanguage(locale));
            }
        }
        if (transition == null) {
            transition =
                (String)this.transitionNames.get(
                    application + "." + service + "___");
        }

        return transition;
    }

    /**
     * @param application
     * @param service
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getTransitionName(String application, String service)
        throws ServicePropertyException {
        return getTransitionName(application, service, (Locale) null);
    }

    private String serviceServletPath = null;

    public void setServiceServletPath(String serviceServletPath) {
        this.serviceServletPath = serviceServletPath;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceServletPath() throws ServicePropertyException {
        return this.serviceServletPath;
    }

    private String contextPath = null;

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * @return
     * @throws ServicePropertyException
     * @deprecated
     */
    public String getContextPath() throws ServicePropertyException {
        return this.contextPath;
    }

    private String applicationParamName = null;

    public void setApplicationParamName(String applicationParamName) {
        this.applicationParamName = applicationParamName;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getApplicationParamName() throws ServicePropertyException {
        if (this.applicationParamName == null) {
            return ServicePropertyHandler.DEFAULT_APPLICATION_PARAMETER;
        } else {
            return this.applicationParamName;
        }
    }

    private String serviceParamName = null;

    public void setServiceParamName(String serviceParamName) {
        this.serviceParamName = serviceParamName;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getServiceParamName() throws ServicePropertyException {
        if (this.serviceParamName == null) {
            return ServicePropertyHandler.DEFAULT_SERVICE_PARAMETER;
        } else {
            return this.serviceParamName;
        }
    }

    private String exceptionAttributeName = null;

    public void setExceptionAttributeName(String exceptionAttributeName) {
        this.exceptionAttributeName = exceptionAttributeName;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getExceptionAttributeName() throws ServicePropertyException {
        return this.exceptionAttributeName;
    }

    private String encodingAttributeName = null;

    public void setEncodingAttributeName(String encodingAttributeName) {
        this.encodingAttributeName = encodingAttributeName;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getEncodingAttributeName() throws ServicePropertyException {
        return this.encodingAttributeName;
    }

    private String localeAttributeName = null;

    public void setLocaleAttributeName(String localeAttributeName) {
        this.localeAttributeName = localeAttributeName;
    }

    /**
     * @return
     * @throws ServicePropertyException
     */
    public String getLocaleAttributeName() throws ServicePropertyException {
        return this.localeAttributeName;
    }

    /**
     * @param params
     * @throws PropertyHandlerException
     */
    public void init(PropertyParam[] params) throws PropertyHandlerException {
    }

    public void clear() {
        this.applicationParamName = null;
        this.clientEncoding = null;
        this.contextPath = null;
        this.encodingAttributeName = null;
        this.exceptionAttributeName = null;
        this.inputErrorPagePaths = new HashMap();
        this.localeAttributeName = null;
        this.nextPagePaths = new HashMap();
        this.serviceControllerNames = new HashMap();
        this.serviceErrorPaths = new HashMap();
        this.serviceParamName = null;
        this.serviceServletPath = null;
        this.systemErrorPagePaths = new HashMap();
        this.transitionNames = new HashMap();
    }
}
