package com.bluestone.app.core.popup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PopUpHandlerInterceptor extends HandlerInterceptorAdapter
{
	private static final Logger log = LoggerFactory.getLogger(PopUpHandlerInterceptor.class);
	
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 
	{
/*
		try
		{
			String ipAddress = HttpRequestParser.getClientIp(request);  
			String requestedURI = request.getRequestURL().toString();
			HttpSession httpSession = request.getSession();
			Boolean isShowAdd = (Boolean)httpSession.getAttribute(SHOW_ADD_KEY);
			
			if(isShowAdd == null)
			{
				if(cityService.isShowAdForPage(requestedURI) && cityService.isShowAdForIP(ipAddress))
				{
					httpSession.setAttribute(SHOW_ADD_KEY, true);
				}
			}
			
		}
		catch(Exception e)
		{
			final Throwable rootCause = Throwables.getRootCause(e);
			log.error("Error occurred while executing preHandle in AddsHandlerInterceptor: Cause={} ", e.getLocalizedMessage(), rootCause);
			throw e;
		}*/
		return true;
	}
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		/*if(modelAndView != null) {
			try
			{
				HttpSession httpSession = request.getSession();
				Boolean isShowAdd = (Boolean)httpSession.getAttribute(SHOW_ADD_KEY);
				Boolean isAdShown = (Boolean)httpSession.getAttribute(IS_AD_SHOWN);
				if(isShowAdd != null && isShowAdd.equals(true))
				{
					if(isAdShown == null)
					{
						modelAndView.addObject(IS_SHOW_ADD, true);
						httpSession.setAttribute(IS_AD_SHOWN, true);
						if(log.isTraceEnabled())
						{
							String ipAddress = (String) request.getAttribute(HttpRequestParser.CLIENT_IP);
							log.trace("PopUpHandlerInterceptor.postHandle(): Pop up for the IP: <"+ipAddress+"> has been shown.");	
						}
					}
					else
					{
						modelAndView.addObject(IS_SHOW_ADD, false);//false
					}
				}
				else
				{
					modelAndView.addObject(IS_SHOW_ADD, false);//false
				}
			}
			catch(Exception e)
			{
				final Throwable rootCause = Throwables.getRootCause(e);
				log.error("Error occurred while executing postHandle in AddsHandlerInterceptor: Cause={} ", e.getLocalizedMessage(), rootCause);
				throw e;
			}	
		}*/
		
	}
}
