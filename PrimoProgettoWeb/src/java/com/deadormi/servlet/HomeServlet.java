/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.FileController;
import com.deadormi.controller.GruppoController;
import com.deadormi.controller.InvitoController;
import com.deadormi.controller.PostController;
import com.deadormi.controller.UtenteController;
import com.deadormi.entity.FileApp;
import com.deadormi.entity.Gruppo;
import com.deadormi.entity.Invito;
import com.deadormi.entity.Post;
import com.deadormi.entity.Utente;
import com.deadormi.layout.MainLayout;
import com.deadormi.util.CookiesManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Davide
 */
public class HomeServlet extends HttpServlet {

    //initializing the logger
    static Logger log = Logger.getLogger(HomeServlet.class.getName());
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Utente utente = null;
        List<Invito> inviti = null;
        List<Post> posts = null;
        Integer MAX_NUM_POST = 10;
        String ultimo_login = CookiesManager.getOldDateCookie(request, response);
        try {
            inviti = InvitoController.getInvitiByUserId(request);
            utente = UtenteController.getUserById(request, (Integer) request.getSession().getAttribute("user_id"));
            posts = PostController.getMyGroupsPosts(request);

        } catch (SQLException ex) {
            //Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String avatar_path = request.getContextPath() + AVATAR_RESOURCE_PATH + "/" + utente.getId_utente() + "_" + utente.getNome_avatar();
        try {
            MainLayout.printHeader(out);
            out.println("<div class='ui large inverted vertical demo sidebar menu active fixed'>");

            out.println("<a href='#' class='item center'>");
            if (utente.getNome_avatar() == null) {
                out.println("<img class='circular ui image user-image' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' style='margin:0 auto; width:100px; heigth:100px;' />");
            } else {
                out.println("<img class='circular ui image user-image' src='" + avatar_path + "' alt='Smiley face' style='margin:0 auto; width:100px; height:100px'>");
            }
            out.println("<h3>" + utente.getUsername().toUpperCase() + "</h3>");
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

            out.println("<a href='crea' class='item'>");
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
            out.println("<div class='item'><i class='home icon'></i>Home</div>");
            out.println("</div>");
            out.println("</div>");

            out.println("<div id='main-container' class='main container'>");
            if (ultimo_login != null) {
                out.println("<div class='ui icon message'>");
                out.println("<i class='blue time icon'></i>");
                out.println("<div class='header'>Bentornato " + utente.getUsername().toUpperCase() + "!</div>");
                out.println("<p>Il tuo ultimo login è stato:" + ultimo_login + ".</p>");
                out.println("</div>");

            } else {
                out.println("<div class='ui icon message'>");
                out.println("<i class='blue home icon'></i>");
                out.println("<div class='header'>Benvenuto " + utente.getUsername().toUpperCase() + "!</div>");
                out.println("<p>Usa il menù a fianco per spostarti tra le pagine.</p>");
                out.println("</div>");
            }

            if (posts.size() > 0 && ultimo_login != null) {
                out.println("<h2 class='center ui header'><i class='list layout icon'></i> Nuovi post dall'ultimo login</h2>");
                Integer POST_SIZE = posts.size();
                out.println("<div class='ui grid'>");
                for (int i = 0; i < POST_SIZE; i++) {
                    Post post = posts.get(i);
                    Utente scrivente = null;
                    Gruppo gruppo_post = null;
                    List<FileApp> files = null;
                    try {
                        scrivente = UtenteController.getUserById(request, post.getId_scrivente());
                        gruppo_post = GruppoController.getGruppoById(request, post.getId_gruppo());
                        files = FileController.getFilesByPostId(request, post.getId_post());
                    } catch (SQLException ex) {
                        //  Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Boolean stampa_post = !post.getId_scrivente().equals(request.getSession().getAttribute("user_id"));                    
                    out.println("<div class='row'>");
                    if (i % 2 == 0) {
                        out.println("<div class='three wide column center' >");
                        if (scrivente.getNome_avatar() != null) {
                            out.println("<img class='circular ui image scrivente-image center user-avatar' src='" + request.getContextPath() + "/resource/avatar/" + scrivente.getId_utente() + "_" + scrivente.getNome_avatar() + "' alt='Smiley face' />");
                        } else {
                            out.println("<img class='circular ui image scrivente-image center user-avatar' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' />");
                        }
                        out.println("<h2 style='margin-top:0px;'class='ui header center'>" + scrivente.getUsername() + "</h2>");
                        out.println("</div>");
                        out.println("<div class='thirteen wide column'>");

                        out.println("<div  class='ui blue fluid top attached segment tip-l' >");
                        out.println("<p><i class='quote left icon'></i>" + post.getTesto() + "<i class='quote right icon'></i></p><br/>");

                        out.println("</div>");
                        out.println("<div  class='ui fluid bottom attached segment' >");
                        if (!files.isEmpty()) {
                            FileApp file = null;
                            String filePath = null;
                            for (int j = 0; j < files.size(); j++) {
                                file = files.get(j);
                                filePath = request.getContextPath() + "/resource/files/" + post.getId_gruppo() + "/" + file.getId_file() + "-" + file.getNome_file();
                                out.println("<i class='icon blue attachment'></i>");
                                out.println("<a href='" + filePath + "' target='_blank'>" + file.getId_file() + "-" + file.getNome_file() + "</a>");
                                if (j != files.size() - 1) {
                                    out.println("/");
                                }
                            }

                        }
                        out.println("<p style='text-align:right; color:#ababab; margin-top:0px;'>scritto in <b> <a href='gruppo/show?id_gruppo=" + gruppo_post.getId_gruppo() + "'>" + gruppo_post.getNome() + "</a></b> il <i>" + post.getData_creazione() + "</i></p>");

                        out.println("</div>");

                        out.println("</div>");
                    } else {
                        out.println("<div class='thirteen wide column'>");

                        out.println("<div  class='ui blue fluid top attached segment tip-r' >");
                        out.println("<p><i class='quote left icon'></i>" + post.getTesto() + "<i class='quote right icon'></i></p><br/>");

                        out.println("</div>");
                        out.println("<div  class='ui fluid bottom attached segment' >");
                        if (!files.isEmpty()) {
                            FileApp file = null;
                            String filePath = null;
                            for (int j = 0; j < files.size(); j++) {
                                file = files.get(j);
                                filePath = request.getContextPath() + "/resource/files/" + post.getId_gruppo() + "/" + file.getId_file() + "-" + file.getNome_file();
                                out.println("<i class='icon blue attachment'></i>");
                                out.println("<a href='" + filePath + "' target='_blank'>" + file.getId_file() + "-" + file.getNome_file() + "</a>");
                                if (j != files.size() - 1) {
                                    out.println("/");
                                }
                            }

                        }
                        out.println("<p style='text-align:right; color:#ababab; margin-top:0px;'>scritto in <b> <a href='gruppo/show?id_gruppo=" + gruppo_post.getId_gruppo() + "'>" + gruppo_post.getNome() + "</a></b> il <i>" + post.getData_creazione() + "</i></p>");

                        out.println("</div>");

                        out.println("</div>");
                        out.println("<div class='three wide column center' >");
                        if (scrivente.getNome_avatar() != null) {
                            out.println("<img class='circular ui image center user-avatar' src='" + request.getContextPath() + "/resource/avatar/" + scrivente.getId_utente() + "_" + scrivente.getNome_avatar() + "' alt='Smiley face' />");
                        } else {
                            out.println("<img class='circular ui image center user-avatar' src='" + request.getContextPath() + "/res/images/user_avatar.png' alt='Smiley face' />");
                        }
                        out.println("<h2 style='margin-top:0px;'class='ui header center'>" + scrivente.getUsername() + "</h2>");
                        out.println("</div>");
                    }
                    out.println("</div>");
                }
                //out.println("</div>");
            }
            out.println("</div>");

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
