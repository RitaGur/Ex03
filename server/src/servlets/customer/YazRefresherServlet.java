package servlets.customer;

import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "YazRefresherServlet",urlPatterns = "/customer/yazRefresher")
public class YazRefresherServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        synchronized (this) {
            try {
                int currentRefresherYaz = bankingSystem.getYazOfRefresher();
                out.println(currentRefresherYaz); //current yaz
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
