/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Davide
 */
public class GruppoServlet extends HttpServlet {

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
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Integer id_gruppo = Integer.parseInt(request.getParameter("id_gruppo"));
        Gruppo gruppo = null;
        Utente proprietario = null;
        try {
            gruppo = GruppoController.getGruppoById(request, id_gruppo);
            proprietario = UtenteController.getUserById(request, gruppo.getId_proprietario());
        } catch (SQLException ex) {
            Logger.getLogger(GruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        HttpSession session = request.getSession();
        List<Post> posts = null;
        Post post = null;
        Utente utente = null;
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<h1>GRUPPO: " + gruppo.getNome() + " di " + proprietario.getUsername() + "</h1>");
            out.println("<p>DESCRIZIONE: " + gruppo.getDescrizione() + "</p>");
            out.println("<a href='/PrimoProgettoWeb/secure/tuoi_gruppi'>Indietro</a><br />");
            try {
                posts = PostController.getPostByGruppoId(request, id_gruppo);
            } catch (SQLException ex) {
                Logger.getLogger(GruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (posts != null && posts.size() != 0) {
                out.println("<table>");
                for (int i = 0; i < posts.size(); i++) {

                    post = posts.get(i);
                    try {
                        utente = UtenteController.getUserById(request, post.getId_scrivente());
                    } catch (SQLException ex) {
                        Logger.getLogger(GruppoServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    out.println("<tr>");
                    out.println("TESTO:<td>" + post.getTesto() + "</td>");
                    out.println("<td> scritto il " + post.getData_creazione() + "</td>");
                    out.println("<td> da: " + utente.getUsername() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            } else {
                out.println("<p>Non vi sono posts nel db!</p>");
            }
            out.println("<form method='POST'>");
            out.println("<input type='submit' name='bottone' value='NUOVOPOST'/>");
            
            if (proprietario.getId_utente().equals(session.getAttribute("user_id"))) {
                out.println("<input type='submit' name='bottone' value='MODIFICAGRUPPO'/>");
            }
            out.println("</form>");
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
        Integer id = Integer.parseInt(request.getParameter("id_gruppo"));
        request.setAttribute("id_gruppo", id);
        RequestDispatcher rd;
        if (request.getParameter("bottone").equals("NUOVOPOST")) {
            rd = request.getRequestDispatcher("/secure/nuovo_post");
            rd.forward(request, response);
        }
        else if(request.getParameter("bottone").equals("MODIFICAGRUPPO")){
            rd = request.getRequestDispatcher("/secure/modifica_gruppo");
            rd.forward(request, response);
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
