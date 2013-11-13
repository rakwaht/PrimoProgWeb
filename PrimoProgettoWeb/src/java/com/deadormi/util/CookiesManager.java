/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class CookiesManager {

    public static String getLastOnline(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("ultimo_login")) {
                cookie = cookies[i];
            }
        }
        return cookie.getValue();
    }

    public static void setLastOnline(HttpServletRequest request,  HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        Cookie [] cookies = request.getCookies();
        Cookie cookie = null;
        for(int i = 0; i<cookies.length; i++){
            if(cookies[i].getName().equals("ultimo_login")){
                cookie = cookies[i];
                cookie.setValue(CurrentDate.getCurrentDate());
                cookie.setMaxAge(604800);
                response.addCookie(cookie);
            }
        }
        if(cookie == null){
            cookie = new Cookie("ultimo_login",CurrentDate.getCurrentDate());
            response.addCookie(cookie);
        }
    }
    
}
