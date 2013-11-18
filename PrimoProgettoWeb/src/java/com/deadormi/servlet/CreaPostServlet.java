/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deadormi.servlet;

import com.deadormi.controller.FileController;
import com.deadormi.controller.PostController;
import com.deadormi.entity.FileApp;
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
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Timbu
 */
public class CreaPostServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
        Integer id_gruppo = (Integer) request.getAttribute("id_gruppo");
        List<FileApp> files = null;
        try {
            files = FileController.getFilesByGroupId(request, id_gruppo);
        } catch (SQLException ex) {
            Logger.getLogger(CreaPostServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            /* TODO output your page here. You may use following sample code. */
            MainLayout.printHeader(out);
            out.println("<h1>Crea Post da gruppo: " + id_gruppo + "</h1>");
            out.println("<form method='POST' action='/PrimoProgettoWeb/secure/nuovo_post?id_gruppo=" + id_gruppo + "' enctype='multipart/form-data'>");
            out.println("<textarea id='testo_post' name='testo'></textarea><br />");
            out.println("Aggiungi file?<INPUT TYPE='FILE' NAME='file' onchange='add_upload_file();'> <BR />");
            out.println("<input type='submit' name='creapost' value='CREA POST' />");
            out.println("</form>");
            if(files != null && files.size()>0){
            out.println("<div id='scelte'>");
            for(int i = 0; i<files.size(); i++){
                out.println("<input type='checkbox' name='file' value='" + files.get(i).getId_file() + "-" + files.get(i).getNome_file() + "'>");
                out.println(files.get(i).getNome_file() + ", del post" + files.get(i).getId_post() +"<br>");
                
            }
            out.println("<input type='button' value='Linka Selezionati' onclick='linka_selezionati()' />");
            out.println("</div>");
            }
            else{
                out.println("<p>Non ci sono files in questo gruppo</p>");
            }
            out.println("<a href='/PrimoProgettoWeb/secure/gruppo/show?id_gruppo=" + id_gruppo + "'>Indietro</a>");
            MainLayout.printFooter(out);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
                PostController.creaPost(request);
            } catch (SQLException ex) {
                Logger.getLogger(CreaPostServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (org.apache.commons.fileupload.FileUploadException ex) {
                Logger.getLogger(CreaPostServlet.class.getName()).log(Level.SEVERE, null, ex);
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
