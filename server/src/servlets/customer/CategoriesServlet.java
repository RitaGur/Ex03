package servlets.customer;

import DTO.lists.CategoriesList;
import DTO.lists.PaymentNotificationsListDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name ="CategoriesServlet", urlPatterns = "/customer/categoriesList")
public class CategoriesServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerFromSession = SessionUtils.getCustomer(request);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        CategoriesList categoriesList = new CategoriesList();
        try {
            categoriesList.setCategories(bankingSystem.getLoanCategoryList());
            String json = GSON_INSTANCE.toJson(categoriesList);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
