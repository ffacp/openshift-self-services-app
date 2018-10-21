package org.samir.openshift.selfservices.utils;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebUtils {

	public static HttpSession getSession() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
	}

	public static void setSessionAttribute(String name, Object value) {
		getSession().setAttribute(name, value);
	}

	public static Object getSessionAttribute(String name) {
		return getSession().getAttribute(name);
	}
}
