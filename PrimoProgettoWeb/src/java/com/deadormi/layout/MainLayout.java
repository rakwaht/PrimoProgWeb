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
            out.println("<title>Servlet HomeServlet cul</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<form method='POST' action='logout'>");
            out.println("<button type='submit'>Logout</button>");
            out.println("</form>");
    }

    public static void printFooter(PrintWriter out) {
            out.println("</body>");
            out.println("</html>");
    }
}
