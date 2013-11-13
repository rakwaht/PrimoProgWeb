/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.Gruppo_UtenteController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Davide
 */
public class TuoiGruppiServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        List<Gruppo> gruppi = null;
        try {
            gruppi = Gruppo_UtenteController.getGruppiByUserId(request);
        } catch (SQLException ex) {
            Logger.getLogger(TuoiGruppiServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<h1>MIEI GRUPPI: " + request.getContextPath() + "</h1>");
            out.println("<a href='home'>Indietro</a><br />");
            if (gruppi != null && gruppi.size() > 0) {
                out.println("<table>");
                for (int i = 0; i < gruppi.size(); i++) {
                    Gruppo gruppo = gruppi.get(i);
                    Utente proprietario = null;
                    try {
                        proprietario = UtenteController.getUserById(request, gruppo.getId_proprietario());
                    } catch (SQLException ex) {
                        Logger.getLogger(TuoiGruppiServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("<tr>");
                    out.println("<td>Nome: "+ gruppo.getNome() + " Autore: "+ proprietario.getUsername() +"</td>");
                    out.println("<td>Descrizione:  "+ gruppo.getDescrizione() +"</td>");
                    out.println("<td><a href='gruppo/show?id_gruppo=" + gruppo.getId_gruppo() + "'>VAI </a></td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<p>Nessun gruppo iscritto!</p>");
            }
            MainLayout.printFooter(out);
        } finally {
            out.close();
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
        processRequest(request, response);
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
