/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.FileController;
import com.deadormi.controller.GruppoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.FileApp;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import static com.deadormi.servlet.HomeServlet.AVATAR_RESOURCE_PATH;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Timbu
 */
public class CreaPostServlet extends HttpServlet {

    private Integer error = 0;
    static Logger log = Logger.getLogger(CreaPostServlet.class);

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
        Integer id_gruppo = Integer.parseInt(request.getParameter("id_gruppo"));
        List<FileApp> files = null;
        Utente utente = null;
        String securePath = "/PrimoProgettoWeb/secure/";
        Gruppo gruppo = null;
        try {
            files = FileController.getFilesByGroupId(request, id_gruppo);
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));
            gruppo = GruppoController.getGruppoById(request, id_gruppo);
        } catch (SQLException ex) {
            log.error(ex);
        }
        String avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();

        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

            out.println("<a href='" + securePath + "home' class='item center'>");
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

            if (utente.getId_utente().equals(gruppo.getId_proprietario())) {
                out.println("<a href='" + securePath + "modifica_gruppo?id_gruppo=" + gruppo.getId_gruppo() + "' class='item'>");
                out.println("<i class='wrench icon'></i>Modifica Gruppo");
                out.println("</a>");
            }

            out.println("<a href='" + securePath + "nuovo_post?id_gruppo=" + gruppo.getId_gruppo() + "' class='item active'>");
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
            out.println("<div class='item'><i class='outline chat icon'></i>Crea Post</div>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div id='main-container' class='main container center'>");
            out.println("<div class='ui grid'>");
            out.println("<div class='row'>");
            out.println("<div class='four wide column'></div>");
            out.println("<div class='eight wide column'>");
            if (error == 1) {
                out.println("<div class='ui icon red message'>");
                out.println("<i class='remove sign icon'></i>");
                out.println("<div class='content'>");
                out.println("<div class='header'>");
                out.println("Errore!");
                out.println("</div>");
                out.println("<p>I file caricati superano il peso totale massimo (10MB)!</p>");
                out.println("</div>");
                out.println("</div>");

                
            }
            else if (error == 2||error==3) {
                out.println("<div class='ui icon red message'>");
                out.println("<i class='remove sign icon'></i>");
                out.println("<div class='content'>");
                out.println("<div class='header'>");
                out.println("Errore!");
                out.println("</div>");
                if(error ==2)out.println("<p>Campo di testo vuoto!</p>");
                else if(error==3)out.println("<p>Hai superato il limite massimo di parole!</p>");
                out.println("</div>");
                out.println("</div>");

              
            }
            out.println("<form class='ui form segment blue' method='POST' action='/PrimoProgettoWeb/secure/nuovo_post?id_gruppo=" + id_gruppo + "' enctype='multipart/form-data'>");
            if(error==2||error==3)out.println("<div class='field error'>");
            else out.println("<div class='field'>");
            out.println("<div class='ui blue ribbon label'><i class='icon comment'></i>Testo</div>");
            out.println("<div class='ui login-input'>");
            out.println("<textarea id='testo_post' name='testo' type='text'></textarea>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div class='field '>");
            out.println("<div class='ui blue ribbon label'><i class='icon save'></i>FILES</div><small>(Peso Totale Massimo: 10 MB)</small>");
            out.println("<div class='ui login-input center'>");
            out.println("<input type='file' name='file' onchange='add_upload_file();'> <br />");
            out.println("</div>");

            if (files != null && files.size() > 0) {
                out.println("<div class='ui divider'></div>");

                out.println("<div id='scelte'>");
                for (int i = 0; i < files.size(); i++) {
                    out.println("<div class='ui checkbox' style='margin:5px'>");
                    out.println("<input type='checkbox' name='file' value='" + files.get(i).getId_file() + "-" + files.get(i).getNome_file() + "'>");
                    out.println("<label>" + files.get(i).getNome_file() + ", del post" + files.get(i).getId_post() + "</label>");
                    out.println("</div><br/>");
                }
                out.println("<button type='button' class='ui small blue button' value='Linka Selezionati' onclick='linka_selezionati()' ><i class='icon attachment'></i>Linka Selezionati</button>");
                out.println("</div>");
            } else {
                out.println("<p>Non ci sono files in questo gruppo</p>");
            }

            out.println("<div class='ui divider'></div>");
            out.println("<div class='center'>");
            out.println("<button type='submit' class='ui blue button ' name='creapost' value='CREA POST' /><i class='icon plus sign'></i>Crea Post</button>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");

            out.println("</form>");

            out.println("</div>");
            out.println("<div class='four wide column'></div>");
            out.println("</div>");
            out.println("</div>");
              error = 0;
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
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            String url = request.getQueryString();
            Integer gruppo_id = Integer.parseInt(url.substring(url.indexOf("=") + 1, url.length()));
            try {
                error = PostController.creaPost(request);
               
            } catch (SQLException ex) {
                error = 3;
                log.debug("String troppo grande: "+ex);
            } catch (org.apache.commons.fileupload.FileUploadException ex) {
                log.error(ex);
            }
             if (error > 0) {
                    processRequest(request, response);
                }
            response.sendRedirect("/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + gruppo_id);
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
    }// </editor-fold
}
