/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.GruppoController;
import com.deadormi.controller.Gruppo_UtenteController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import static com.deadormi.servlet.HomeServlet.AVATAR_RESOURCE_PATH;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Timbu
 */
public class ModificaGruppoServlet extends HttpServlet {

    static Logger log = Logger.getLogger(ModificaGruppoServlet.class);
    final static String AVATAR_RESOURCE_PATH = "/resource/avatar";

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
        try {
            String securePath = "/PrimoProgettoWeb/secure/";

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Integer id_gruppo = Integer.parseInt(request.getParameter("id_gruppo"));
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            Gruppo gruppo = GruppoController.getGruppoById(request, id_gruppo);
            List<Utente> iscritti = UtenteController.getUserByGroupId(request, id_gruppo);
            List<Utente> non_iscritti = UtenteController.getUserNotInGroupByGroupId(request, id_gruppo);
            Utente utente = null;
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));
            String avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();

            try {
                MainLayout.printHeader(out);
                out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

                out.println("<a href='/PrimoProgettoWeb/secure/home' class='item center'>");
                if (utente.getNome_avatar() == null) {
                    out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
                } else {
                    out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
                }
                out.println("<h3>" + utente.getUsername().toUpperCase() + "</h3>");
                out.println("</a>");

                out.println("<a href='" + securePath + "tuoi_gruppi' class='item'>");
                out.println("<i class='users icon'></i> Gruppi");
                out.println("</a>");

                out.println("<a href='" + securePath + "/gruppo/show?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'>");
                if (utente.getId_utente().equals(gruppo.getId_proprietario())) {
                    out.println("<i class='book icon'></i><div class='ui blue label'>admin</div>" + gruppo.getNome());
                } else {
                    out.println("<i class='book icon'></i>" + gruppo.getNome());
                }
                out.println("</a>");

                out.println("<a href='" + securePath + "modifica_gruppo?id_gruppo=" + gruppo.getId_gruppo() + "' class='item active'>");
                out.println("<i class='wrench icon'></i>Modifica Gruppo");
                out.println("</a>");
               
                out.println("<a href='/PrimoProgettoWeb/secure/crea_pdf?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'>");
                out.println("<i class='copy icon'></i>");
                out.println("Genera PDF");
                out.println("</a>");
               

                out.println("<a href='" + securePath + "nuovo_post?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'>");
                out.println("<i class='outline chat icon'></i>");
                out.println("Nuovo Post");
                out.println("</a>");

                out.println("<a href='" + securePath + "logout' class='item'>");
                out.println("<i class='sign out icon'></i>");
                out.println("Logout");
                out.println("</a>");

                out.println("</div>");

                out.println("<div class=\"ui fixed transparent inverted main menu\">");
                out.println("<div class='container'>");
                out.println("<div id='buffo' class='item' style='cursor: pointer'><i class=\"icon list\"></i></div>");
                out.println("<a href='" + securePath + "/gruppo/show?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'><i class=\"left arrow icon\"></i>INDIETRO</a>");

                out.println("<div class='item'><i class='wrench icon'></i>Modifica Gruppo</div>");
                out.println("</div>");
                out.println("</div>");

                out.println("<div id='main-container' class='main container'>");
                out.println("<div class='ui grid'>");
                out.println("<div class='row'>");
                out.println("<div class='four wide column'></div>");
                out.println("<div class='eight wide column'>");
                //FORM
                out.println("<form method='POST' class='ui form blue segment' action='/PrimoProgettoWeb/secure/modifica_gruppo?id_gruppo=" + id_gruppo + "''>");
                out.println("<div class='field'>");
                out.println("<div class='ui blue ribbon label'>Titolo</div>");
                out.println("<div class='ui left icon input login-input'>");
                out.println("<input placeholder='Titolo' value='" + gruppo.getNome() + "' name='titolo' />");
                out.println("<i class='align justify icon'></i>");
                out.println("</div>");
                out.println("</div>");

                out.println("<div class='field'>");
                out.println("<div class='ui blue ribbon label'>Descrizione</div>");
                out.println("<div class='ui login-input'>");
                out.println("<textarea type='text' name='descrizione'>" + gruppo.getDescrizione() + "</textarea>");
                out.println("</div>");
                out.println("</div>");

                if (non_iscritti.size() > 0) {

                    out.println("<div class='ui blue ribbon label'><i class='icon users '></i>Invita Utenti</div><br/>");

                    for (int i = 0; i < non_iscritti.size(); i++) {
                        out.println("<p>");
                        if (!InvitoController.checkInvitoByUserId(request, non_iscritti.get(i).getId_utente(), id_gruppo)) {

                            out.println("<div class='ui toggle checkbox'>");
                            out.println("<input type='checkbox' name='non_utenti_selezionati' value='" + non_iscritti.get(i).getId_utente() + "'>");
                            out.println("<label>" + non_iscritti.get(i).getUsername() + "</label></div>");
                        } else {
                            out.println("<div class='ui small green label'><i class=\"checkmark icon\"></i>INVITATO</div><label>" + non_iscritti.get(i).getUsername() + "</label>");

                        }
                        out.println("</p>");
                    }

                }
                if (iscritti.size() > 1) {
                    out.println("<div class='ui blue ribbon label'><i class='icon delete'></i>&nbsp;Elimina Utenti</div><br/>");

                    for (int i = 0; i < iscritti.size(); i++) {
                        out.println("<p>");
                        if (!iscritti.get(i).getId_utente().equals(user_id)) {
                            out.println("<div class='ui toggle checkbox'>");
                            out.println("<input type='checkbox' name='utenti_selezionati' value='" + iscritti.get(i).getId_utente() + "'/>");
                            out.println("<label>" + iscritti.get(i).getUsername() + "</label></div>");
                        }
                        out.println("<p>");
                    }

                }

                out.println("<div class='ui divider'></div>");
                out.println("<div class='center '>");
                out.println("<button class='ui blue button' type='submit' name='modifica' value='MODIFICA'/><i class='icon save'></i>SALVA</button>");
                out.println("</form>");
                out.println("</div>");
                out.println("<div class='four wide column'></div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                MainLayout.printFooter(out);
            } finally {
                out.close();
            }
        } catch (SQLException ex) {
            log.error(ex);
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
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
        }
        if (request.getParameter("modifica") != null) {
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            String url = request.getQueryString();
            Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));
            try {
                GruppoController.modificaGruppo(request, gruppo_id);
                Gruppo_UtenteController.eliminaUser(request, gruppo_id);
                InvitoController.processaRe_Inviti(request, gruppo_id);
                response.sendRedirect("/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + gruppo_id);
            } catch (SQLException ex) {
                log.error(ex);
            }
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
