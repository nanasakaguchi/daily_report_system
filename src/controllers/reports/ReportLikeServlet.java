package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportLikeServlet
 */
@WebServlet("/reports/like")

public class ReportLikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportLikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        int like_count = r.getLike_count() + 1;
        r.setLike_count(like_count);

        // いいねテーブル登録
        Like l = new Like();
        l.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
        l.setReport_id(r);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.setCreated_at(currentTime);
        l.setUpdated_at(currentTime);

        // 日報テーブル更新
        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();

        request.getSession().setAttribute("flush", "いいね！しました。");
        response.sendRedirect(request.getContextPath() + "/reports/index");

        }
}