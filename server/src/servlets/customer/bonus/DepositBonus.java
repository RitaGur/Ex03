package servlets.customer.bonus;

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

@WebServlet(name = "DepositBonus",urlPatterns = "/customer/bonus/depositBonus")
public class DepositBonus extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        List<ClientInformationDTO> customersList = ServletUtils.getCustomerList(getServletContext());
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        String errorMessage = "";
        try {
            String sumToDepositFromParameter = request.getParameter(AMOUNT);
            String customerName = request.getParameter("username");

            if (!checkUserName(customersList, customerName)) {
                errorMessage = "username " + customerName + " does not exists in banking system.\n";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
                return;
            }

            int sumToDeposit = getAmountFromUser(sumToDepositFromParameter);
            bankingSystem.addMoneyToAccount(customerName, sumToDeposit);
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("The money was added successfully!\n");
        } catch (NumberFormatException exception) {
            out.println("Incorrect input,please note that you entered an integer number!!\n");
        } catch (RuntimeException exception) {
            out.println(exception.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAmountFromUser(String amount) {
        int amountFromUser = Integer.parseInt(amount);

        if (amountFromUser < 0) {
            throw new NumberFormatException();
        }

        return amountFromUser;
    }

    private Boolean checkUserName(List<ClientInformationDTO> clientList, String userName) {
        for (ClientInformationDTO customer : clientList) {
            if (customer.getClientName().equals(userName)) {
                return true;
            }
        }

        return false;
    }
}
