package servlets.admin;

import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "IsRewindServlet",urlPatterns = "/admin/isRewind")
public class IsRewindServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        synchronized (this) {
            try {
                Boolean isRewind = bankingSystem.getRewind();
                out.println(isRewind);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
