/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.layout;

import java.io.PrintWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class MainLayout {

    static Logger log = Logger.getLogger(MainLayout.class);

    public static void printHeader(PrintWriter out) {
        /* TODO output your page here. You may use following sample code. */
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<link href=\"http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700|Open+Sans:300italic,400,300,700\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("<link rel='stylesheet' type='text/css' href='/PrimoProgettoWeb/res/css/semantic.css' />");
        out.println("<link rel='stylesheet' type='text/css' href='/PrimoProgettoWeb/res/css/application.css' />");
        out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/jquery_v2.0.3.js'></script>");
        out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/semantic.js'></script>");
        out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/application.js'></script>");
        out.println("<title>Primo Progetto Web</title>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
        out.println("</head>");
        out.println("<body class='pushed'>");
    }

    public static void printFooter(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
    }
}
