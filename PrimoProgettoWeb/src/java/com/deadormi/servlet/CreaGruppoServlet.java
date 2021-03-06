/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Invito;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import static com.deadormi.servlet.AvatarServlet.AVATAR_RESOURCE_PATH;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class CreaGruppoServlet extends HttpServlet {

    
    private Integer errore = 0;
    static Logger log = Logger.getLogger(CreaGruppoServlet.class);

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
        List<Utente> list = null;
        List<Invito> inviti = null;
        Utente utente = null;
        Utente myutente = null;
        HttpSession session = request.getSession();
        try {
            list = UtenteController.getUtenti(request);
            inviti = InvitoController.getInvitiByUserId(request);
            myutente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));

        } catch (SQLException ex) {
            log.error(ex);
        }
        String avatar_path = null;
        if (myutente.getNome_avatar() != null) {
            avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + myutente.getId_utente() + "_" + myutente.getNome_avatar();
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

            out.println("<a href='home' class='item center'>");
            if (myutente.getNome_avatar() == null) {
                out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
            } else {
                out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
            }
            out.println("<h3>" + myutente.getUsername().toUpperCase() + "</h3>");
            out.println("</a>");

            out.println("<a href='cambia_avatar' class='item'>");
            out.println("<i class='edit icon'></i>Cambia Avatar");
            out.println("</a>");

            out.println("<a href='inviti' class='item'>");
            out.println("<div class='ui large blue label'>" + inviti.size() + "</div>");
            out.println("Inviti");
            out.println("</a>");

            out.println("<a href='tuoi_gruppi' class='item'>");
            out.println("<i class=' users icon'></i>");
            out.println("Gruppi");
            out.println("</a>");

            out.println("<a href='crea' class='item active'>");
            out.println("<i class=' add sign icon'></i>");
            out.println("Crea Gruppo");
            out.println("</a>");

            out.println("<a href='logout' class='item'>");
            out.println("<i class='sign out icon'></i>");
            out.println("Logout");
            out.println("</a>");

            out.println("</div>");

            out.println("<div class=\"ui fixed transparent inverted main menu\">");
            out.println("<div class='container'>");
            out.println("<div id='buffo' class='item' style='cursor: pointer'><i class=\"icon list\"></i></div>");
            out.println("<a href='home' class='item'><i class=\"left arrow icon\"></i>INDIETRO</a>");
            out.println("<div class='item'><i class='add sign icon'></i>Crea Gruppo</div>");

            out.println("</div>");
            out.println("</div>");

            out.println("<div id='main-container' class='main container center'>");
            out.println("<div class='ui grid'>");
            out.println("<div class='row'>");
            out.println("<div class='four wide column'></div>");
            out.println("<div class='eight wide column'>");
            if (errore == 1) {
                    out.println("<br/><div class='ui icon red message'>");
                    out.println("<i class='remove sign icon'></i>");
                    out.println("<div class='content'>");
                    out.println("<div class='header'>");
                    out.println("Errore!");
                    out.println("</div>");
                    out.println("<p>Campo titolo vuoto!</p>");
                    out.println("</div>");
                    out.println("</div>");
                }
            else if(errore==2){
                out.println("<br/><div class='ui icon red message'>");
                    out.println("<i class='remove sign icon'></i>");
                    out.println("<div class='content'>");
                    out.println("<div class='header'>");
                    out.println("Errore!");
                    out.println("</div>");
                    out.println("<p>Campo descrizione vuoto!</p>");
                    out.println("</div>");
                    out.println("</div>");
            }
            //FORM
            out.println("<form class='ui fluid form blue segment' method='POST'>");
            if(errore==1) out.println("<div class='field error'>");
            else out.println("<div class='field'>");
            out.println("<div class='ui blue ribbon label'>Titolo</div>");
            out.println("<div class='ui left icon input login-input'>");
            out.println("<input placeholder='Titolo' type='text' name='titolo'/>");
            out.println("<i class='align justify icon'></i>");
            out.println("</div>");
            out.println("</div>");
            
             if(errore==2) out.println("<div class='field error'>");
            else out.println("<div class='field'>");
            out.println("<div class='ui blue ribbon label'>Descrizione</div>");
            out.println("<div class='ui login-input'>");
            out.println("<textarea type='text' name='descrizione'></textarea>");
            out.println("</div>");
            out.println("</div>");

            if (list != null && list.size() != 1) {
                out.println("<div class='ui blue ribbon label'><i class='icon users '></i>Invita</div><br/>");
                for (int i = 0; i < list.size(); i++) {

                    utente = list.get(i);
                    if (!utente.getId_utente().equals(session.getAttribute("user_id"))) {
                        out.println("<p>");
                        out.println("<div class='ui toggle checkbox'>");
                        out.println("<input type='checkbox' name='utenti_selezionati' value='" + utente.getId_utente() + "'>");
                        out.println("<label>" + utente.getUsername() + "</label></div>");
                        out.println("</p>");
                    }
                }
                out.println("<div class='ui divider'></div>");

            } else {
                out.println("<div class='ui blue segment'>Nessuna persona!</div>");
            }
            out.println("<button class='ui button blue fluid' type='submit' style='margin-top:10px;'><i class='add icon'></i>Crea</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class='four wide column'></div>");
            out.println("</div>");
            out.println("</div>");
            errore = 0;
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
            Integer id_gruppo;
            try {
                id_gruppo = GruppoController.creaGruppo(request);
            } catch (Exception ex) {
                if(ex.getMessage().equals("titolo vuoto")){
                    errore = 1; //errore 1 = titolo vuoto
                }
                else if(ex.getMessage().equals("descrizione vuota")){
                    errore = 2; //errore 2 per la descrizione vuota
                }
                id_gruppo = 0;
                log.debug(ex);
            }
            if (id_gruppo > 0) {
                response.sendRedirect("/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + id_gruppo);

            } else {
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
