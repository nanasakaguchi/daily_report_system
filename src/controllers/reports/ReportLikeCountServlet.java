package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportLikeCountServlet
 */
@WebServlet("/reports/likecount")

public class ReportLikeCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportLikeCountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        List<Like> likes = em.createNamedQuery("getAllLikes", Like.class)
                .setParameter("report", r)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        long like_page = (long) em.createNamedQuery("getLikesCount", Long.class)
                .setParameter("report", r)
                .getSingleResult();
        em.close();
        request.setAttribute("likes", likes);
        request.setAttribute("like_page", like_page);
        request.setAttribute("page", page);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/like.jsp");
        rd.forward(request, response);
    }
}