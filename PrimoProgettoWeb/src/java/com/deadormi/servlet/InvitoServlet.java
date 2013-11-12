/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.dbmanager.DbManager;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Gruppo_Utente;
import com.deadormi.entity.Invito;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class InvitoServlet extends HttpServlet {

    private DbManager manager;

    @Override
    public void init() {
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
    }

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
        List<Invito> inviti = null;
        Invito invito = null;
        Integer id_gruppo = null;
        Integer id_invitante = null;
        Utente invitante = null;
        Gruppo gruppo = null;
        
        HttpSession session = request.getSession();
        try {
            inviti = manager.getInvitiByUserId((Integer) session.getAttribute("user_id"));
        } catch (SQLException ex) {
            Logger.getLogger(InvitoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<h1>INVITI: " + request.getContextPath() + "</h1>");
            out.println("<a href='home'>Indietro</a><br />");
            if (inviti != null) {
                out.println("<h2>Inviti</h2>");
                out.println("<form method='POST'>");
                out.println("<table>");
                
                for (int i = 0; i < inviti.size(); i++) {
                    invito = inviti.get(i);
                    id_gruppo = invito.getId_gruppo();
                    try {
                        gruppo = manager.getGruppoById(id_gruppo);
                    } catch (SQLException ex) {
                        Logger.getLogger(InvitoServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    id_invitante = invito.getId_invitante();
                    try {
                        invitante = manager.getUserById(id_invitante);
                    } catch (SQLException ex) {
                        Logger.getLogger(InvitoServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    out.println("<tr>");
                    out.println("<td>" + gruppo.getNome() + " da " + invitante.getUsername() + "</td>");
                    out.println("<td><input type='checkbox' name='gruppi_selezionati' value='"+ gruppo.getId_gruppo()+"'/></td>");
                    out.println("</tr>");
                }
                
                
                out.println("</table>");
                out.println("<input type='submit' value='ISCRIVITI'/>");
                out.println("</form>");
            } else {
                out.println("Nessun invito nel db!");
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
        String[] gruppi_selezionati = request.getParameterValues("gruppi_selezionati");
        String id_gruppo;
        HttpSession session = request.getSession();
        Gruppo_Utente gu = null;
        
        for(int i=0; i<gruppi_selezionati.length; i++){
        
            id_gruppo = gruppi_selezionati[i];
            gu = new Gruppo_Utente();
            gu.setId_gruppo(Integer.parseInt(id_gruppo));System.out.println(gu.getId_gruppo());
            gu.setId_utente((Integer) session.getAttribute("user_id"));System.out.println(gu.getId_utente());
            try {
                manager.creaGruppo_Utente(gu);
            } catch (SQLException ex) {
                Logger.getLogger(InvitoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
