/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class CookiesManager {
    
    static Logger  log = Logger.getLogger(CookiesManager.class);
    
    public static void createNewDateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        Cookie cookie;
        
        if (cookies == null) {
            cookie = new Cookie("ultimo_login" + session.getAttribute("user_id"), CurrentDate.getCurrentDate());
            cookie.setMaxAge(60*60*200);
            cookie.setHttpOnly(true);
            cookie.setPath("/PrimoProgettoWeb/");
            response.addCookie(cookie);
        } else {
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("ultimo_login"+session.getAttribute("user_id").toString())) {
                    CookiesManager.createOldDateCookie(cookie.getValue(), response, request);
                    cookie.setMaxAge(0);
                    cookie = new Cookie("ultimo_login" + session.getAttribute("user_id"), CurrentDate.getCurrentDate());
                    cookie.setPath("/PrimoProgettoWeb/");
                    cookie.setMaxAge(604800);
                    response.addCookie(cookie);
                } else {
                    cookie = new Cookie("ultimo_login" + session.getAttribute("user_id"), CurrentDate.getCurrentDate());
                    cookie.setMaxAge(604800);
                     cookie.setPath("/PrimoProgettoWeb/");
                     cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static void createOldDateCookie(String date, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cookie cookie = new Cookie("old_cookie" + session.getAttribute("user_id"), date);
        cookie.setMaxAge(604800);
        response.addCookie(cookie);
    }

    public static String getOldDateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        HttpSession session = request.getSession();
        Cookie cookie = null;
        String res = null;
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            if (cookie.getName().equals("old_cookie" + session.getAttribute("user_id"))) {
                res = cookie.getValue();
            }
        }
        return res;
    }

    /*public static String getIdFromCookie(String value) {
        String result = value.substring(value.indexOf("?") + 1, value.length());
        return result;
    }*/
}
