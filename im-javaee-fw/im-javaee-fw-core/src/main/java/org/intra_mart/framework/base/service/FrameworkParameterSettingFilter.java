package org.intra_mart.framework.base.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * 以前のバージョンのサービスフレームワークが使用していたアプリケーションID及びサービスIDを
 * リクエストのパラメータに設定します。<BR>
 * 
 * このフィルタを使用することにより{@link javax.servlet.ServletRequest#getParameter(String)}から
 * アプリケーションID及びサービスIDを取得することができますが、この方法は正しくありません。<BR>
 * 正しくは以下のメソッドを使用してください。<BR>
 * <BR>
 * {@link org.intra_mart.framework.base.service.ServiceManager#getApplication(HttpServletRequest, HttpServletResponse)}<BR>
 * {@link org.intra_mart.framework.base.service.ServiceManager#getService(HttpServletRequest, HttpServletResponse)}<BR>
 *
 * @author INTRAMART
 * @version 1.0
 * @since 5.0
 * @deprecated このフィルタは下位互換のためのフィルタです。将来廃止される可能性があります。
 */
public class FrameworkParameterSettingFilter implements Filter {

	/**
     * リクエストのパラメーターを設定後、次の処理に遷移します。<BR>
     * パラメータ名は以下のメソッドから取得したものを使用します。<BR>
     * {@link org.intra_mart.framework.base.service.ServicePropertyHandler#getApplicationParamName()}<BR>
     * {@link org.intra_mart.framework.base.service.ServicePropertyHandler#getServiceParamName()}<BR>
     *
     * @param servletRequest リクエスト
     * @param servletResponse レスポンス
     * @param filterChain フィルタのチェーン
     * @throws IOException フィルタ処理実行時にIOExceptionが発生
     * @throws ServletException フィルタ処理実行時にServletExceptionが発生
     */
	public void doFilter(
			ServletRequest servletRequest,
			ServletResponse servletResponse,
			FilterChain filterChain)
	throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String application;
        String service;
        String applicationParamName;
        String serviceParamName;
        
    	try {
    		ServiceManager serviceManager = ServiceManager.getServiceManager();
    		ServicePropertyHandler servicePropertyHandler = serviceManager.getServicePropertyHandler();
			applicationParamName = servicePropertyHandler.getApplicationParamName();
			serviceParamName = servicePropertyHandler.getServiceParamName();
			application = serviceManager.getApplication(request, response);
			service = serviceManager.getService(request, response);
		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}

		HttpServletRequestImpl requestImpl = new HttpServletRequestImpl(
				request, application, service, applicationParamName, serviceParamName);
        filterChain.doFilter(requestImpl, servletResponse);
	}

	/**
	 * フィルタを初期化します。
	 * @param filterConfig フィルタ設定オブジェクト
	 * @throws ServletException フィルタを初期化時に例外が発生。
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * フィルタを破棄します。
	 */
	public void destroy() {
	}
	
	private class HttpServletRequestImpl extends HttpServletRequestWrapper{

		String application;
		String service;
		
		String applicationParamName;
		String serviceParamName;
		
		public HttpServletRequestImpl(
				HttpServletRequest request,
				String application,
				String service,
				String applicationParamName,
				String serviceParamName) {
			
			super(request);
			this.application = application;
			this.service = service;
			this.applicationParamName = applicationParamName;
			this.serviceParamName = serviceParamName;
		}
		
		public String getParameter(String name) {
			if(name.equals(applicationParamName)) {
				if(super.getParameter(applicationParamName) == null) {
					return application;
				}
			} else if(name.equals(serviceParamName)) {
				if(super.getParameter(serviceParamName) == null) {
					return service;
				}
			}
			return super.getParameter(name);
		}

		public Map getParameterMap() {
			Map map = new HashMap(super.getParameterMap());
			if(map.get(applicationParamName) == null) {
				map.put(applicationParamName, application);
			}
			if(map.get(serviceParamName) == null) {
				map.put(serviceParamName, service);
			}			
			return map;
		}

		public Enumeration getParameterNames() {
			Vector vector = new Vector();
			Enumeration enu = super.getParameterNames();
			while(enu.hasMoreElements()) {
				vector.add(enu.nextElement());
			}
			vector.add(applicationParamName);
			vector.add(serviceParamName);
			return vector.elements();
		}

		public String[] getParameterValues(String name) {
			if (name.equals(applicationParamName) || name.equals(serviceParamName)) {
				ArrayList list = new ArrayList();
				if (super.getParameterValues(name) != null) {
					String[] params = super.getParameterValues(name);
					for(int i = 0; i < params.length; i ++) {
						list.add(params[i]);
					}
				}
				if (name.equals(applicationParamName)) {
					list.add(application);
				} else if(name.equals(serviceParamName)) {
					list.add(service);
				}
				return (String[])list.toArray(new String[list.size()]);
			} else {
				return super.getParameterValues(name);
			}
		}
	}
}
