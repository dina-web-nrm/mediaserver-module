package se.nrm.bio.mediaserver.rs.demo;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import se.nrm.bio.mediaserver.business.StartupBean;

/**
 *
 * @author ingimar
 */
@WebServlet(urlPatterns = {"/DemoTransform"})
public class DemoTransform extends HttpServlet {

    @EJB
    private StartupBean envBean;

    ConcurrentHashMap envMap = null;

    /**
     * @Path("/stream/image/{uuid}/{height}/{width}") Processes requests for
     * both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String uuid = (String) request.getParameter("uuid");
        String height = (String) request.getParameter("height");
        
        envMap = envBean.getEnvironment();
        String basePath = (String) envMap.get("base_url");
        String rel_stream = (String) envMap.get("relative_stream_url");

        String url = basePath.concat(rel_stream).concat("/image/").concat(uuid).concat("/").concat(height);
        response.sendRedirect(url);

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
