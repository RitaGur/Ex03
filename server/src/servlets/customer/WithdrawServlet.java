package servlets.customer;

import DTO.client.ClientInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static constants.Constants.AMOUNT;

@WebServlet(name = "WithdrawCustomerServlet",urlPatterns = "/customer/withdraw")
public class WithdrawServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String customerFromSession = SessionUtils.getCustomer(request);
        List<ClientInformationDTO> customersList = ServletUtils.getCustomerList(getServletContext());
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        if (customerFromSession != null) {
            try {
                String sumToWithdrawFromParameter = request.getParameter(AMOUNT);
                int sumToWithdraw = getAmountFromUser(sumToWithdrawFromParameter);
                bankingSystem.withdrawMoneyFromAccount(customerFromSession, sumToWithdraw);
                response.setStatus(HttpServletResponse.SC_OK);
                out.println("The money was withdrawn successfully!\n");
            } catch (NumberFormatException exception) {
                out.println("Incorrect input,please note that you entered an integer number!!\n");
            } catch (RuntimeException exception) {
                out.println(exception.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            out.println("There is not a username with this name in the system!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public int getAmountFromUser(String amount) {
        int amountFromUser = Integer.parseInt(amount);

        if (amountFromUser < 0) {
            throw new NumberFormatException();
        }

        return amountFromUser;
    }
}
