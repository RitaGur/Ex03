package servlets.customer;

import bankingSystem.BankingSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

import static constants.Constants.LOAN;

@WebServlet(name = "PaymentServlet",urlPatterns = "/customer/loanPayment")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class PaymentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {//loans reciver
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        Collection<Part> Parts = request.getParts();
        PrintWriter out = response.getWriter();
        String partValue = null;
        String customerAmount = "";
        for (Part part : Parts) {
            if (Objects.equals(part.getName(), LOAN))
                partValue = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            else
                customerAmount = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
        }
        try {
            bankingSystem.addPayment(bankingSystem.getLoanDTOByLoanID(partValue, -1), Integer.parseInt(customerAmount));
            out.println("The payment paid!");
        } catch (NumberFormatException exception) {
            out.println("Incorrect input,please note that you entered an integer number!!\n");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
