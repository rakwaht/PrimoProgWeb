/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.util;

import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class CookiesManager {

    public static void createNewDateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        Cookie cookie = null;
        if (cookies == null) {
            cookie = new Cookie("ultimo_login", CurrentDate.getCurrentDate() + "?" + session.getAttribute("user_id"));
            cookie.setMaxAge(604800);
            response.addCookie(cookie);
        } else {
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("ultimo_login") && CookiesManager.getIdFromCookie(cookie.getValue()).equals(session.getAttribute("user_id").toString())) {
                    CookiesManager.createOldDateCookie(cookie.getValue(), response);
                    cookie.setMaxAge(0);
                    cookie = new Cookie("ultimo_login", CurrentDate.getCurrentDate() + "?" + session.getAttribute("user_id"));
                    cookie.setMaxAge(604800);
                    response.addCookie(cookie);
                } else {
                    cookie = new Cookie("ultimo_login", CurrentDate.getCurrentDate() + "?" + session.getAttribute("user_id"));
                    cookie.setMaxAge(604800);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static void createOldDateCookie(String date, HttpServletResponse response) {

        Cookie cookie = new Cookie("old_cookie", date);
        cookie.setMaxAge(604800);
        response.addCookie(cookie);
    }

    public static String getOldDateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        String res = null;
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            if (cookie.getName().equals("old_cookie")) {
                res = cookie.getValue();
            }
        }
        return res;
    }

    public static String getIdFromCookie(String value) {
        String result = value.substring(value.indexOf("?") + 1, value.length());
        return result;
    }
}
