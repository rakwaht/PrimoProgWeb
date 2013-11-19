/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.layout;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Davide
 */
public class MainLayout {

    public static void printHeader(PrintWriter out) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel='stylesheet' type='text/css' href='/PrimoProgettoWeb/res/css/application.css' />");
            out.println("<link rel='stylesheet' type='text/css' href='/PrimoProgettoWeb/res/css/semantic.min.css' />");
            out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/jquery_v2.0.3.js'></script>");
            out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/semantic.min.js'></script>");         
            out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/application.js'></script>");
            out.println("<title>Servlet HomeServlet cul</title>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<form method='POST' action='/PrimoProgettoWeb/secure/logout'>");
            out.println("<button type='submit'>Logout</button>");
            out.println("</form>");
    }

    public static void printFooter(PrintWriter out) {
            out.println("</body>");
            out.println("</html>");
    }
}
