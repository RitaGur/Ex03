package servlets.admin;

import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "YazServlet",urlPatterns = "/admin/increaseYaz")
public class YazServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        synchronized (this) {
            try {
                bankingSystem.promoteTimeline();
                out.println("Current Yaz: " + bankingSystem.getCurrentTimeUnit().getCurrentTimeUnit());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
