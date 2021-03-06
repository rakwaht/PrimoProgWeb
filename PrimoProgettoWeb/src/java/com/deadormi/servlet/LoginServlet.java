/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private Integer error = 0;
    static Logger log = Logger.getLogger(LoginServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if (session.getAttribute("logged") != null && session.getAttribute("logged").equals(true)) {
            response.sendRedirect(request.getContextPath() + "/secure/home");
        } else {

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<link rel='stylesheet' type='text/css' href='/PrimoProgettoWeb/res/css/semantic.css' />");
                out.println("<link rel='stylesheet' type='text/css' href='/PrimoProgettoWeb/res/css/application.css' />");
                out.println("<link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro' rel='stylesheet' type='text/css'>");
                out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/jquery_v2.0.3.js'></script>");
                out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/semantic.js'></script>");
                out.println("<script type='text/javascript' src='/PrimoProgettoWeb/res/js/application.js'></script>");
                out.println("<title>Primo Progetto Web</title>");
                out.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='ui message attached top' style='border-bottom:3px #bbb solid'>");
                out.println("<h1 class='center ui header' style='font-size:40px;'><i class='coffee icon blue'></i>Coffee ClassRoom</h1>");
                out.println("</div>");
                out.println("<div class='ui grid'>");
                out.println("<div class='five wide column'></div>");

                out.println("<div class='six wide column'>");
               if (error == 3) {
                    out.println("<br/><div class='ui icon red message'>");
                    out.println("<i class='remove sign icon'></i>");
                    out.println("<div class='content'>");
                    out.println("<div class='header'>");
                    out.println("Errore!");
                    out.println("</div>");
                    out.println("<p>Username e/o Password non corretti!</p>");
                    out.println("</div>");
                    out.println("</div>");

                }
                out.println("<form class='ui fluid form segment blue' method='POST' style='margin-top:25px;'");
                out.println("<div class='three fields'>");
                out.println("<h2 class='center ui header' id='login-title'><i class='circular inverted blue icon sign in'></i>Login</h2>");
                out.println("<div class='ui divider'></div>");
                if(error==3) out.println("<div class='field error'>");
                else out.println("<div class='field'>");
                out.println("<div class='ui blue ribbon label'>Username</div>");
                out.println("<div class='ui left icon input login-input'>");
                out.println("<input id='userName' placeholder='Username' type='text' name='username'/>");
                out.println("<i class='icon user'></i>");
                out.println("</div>");
                out.println("</div>");
                if(error==3) out.println("<div class='field error'>");
                else out.println("<div class='field'>");
                out.println("<div class='ui blue ribbon label'>Password</div>");
                out.println("<div class='ui left icon input login-input'>");
                out.println("<i class='icon key'></i>");
                out.println("<input id='passWord' placeholder='Password' type='password' name='password'/>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='field'>");
                out.println("<button id='login-btn' type='submit' class='ui blue submit fluid button'>Invia</button>");
                out.println("</div>");
                out.println("</form>");
                out.println("</div>");

                out.println("<div class='five wide column'></div>");

                out.println("</div>");
                error=0;
                MainLayout.printFooter(out);
            } finally {
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("richiesta doGet");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("richiesta doPost");
        Utente utente = null;
        if (request.getParameter("username") == null || request.getParameter("username").equals("")) {
            error = 1; // error 1 significa username mancante
        } else if (request.getParameter("password") == null || request.getParameter("password").equals("")) {
            error = 2; // error 2 significa password mancante
        } else {
            try {
                utente = UtenteController.authenticate(request, response);
            } catch (SQLException ex) {
                log.warn("Authenticate Excepion" + ex);
            }
        }
        String redirect = "/secure/home";
        if (utente != null) {
            log.info("Login Effettuato " + utente.getUsername());
            response.sendRedirect(request.getContextPath() + redirect);
        } else {
            error = 3; //coppia utente password non combaciano
            processRequest(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
