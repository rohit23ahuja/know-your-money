package com.kym.service;

import com.kym.model.CreditCardTransaction;
import com.kym.model.CreditCardTransactionCategorization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreditCardTransactionCategorizationService {

    public List<CreditCardTransactionCategorization> categorize(List<CreditCardTransaction> creditCardTransactions) {
        List<CreditCardTransactionCategorization> creditCardTransactionCategorizations = new ArrayList<>();
        for (CreditCardTransaction creditCardTransaction : creditCardTransactions) {
            StringBuilder transactionCategorization = new StringBuilder();
            String description = creditCardTransaction.description();
            if (description.contains("Instamart") ||
                    description.contains("Payu*Instamart Grocery Bangalore ") ||
                    description.contains("ZEPTO MARKETPLACE PRIV Bangalore ") ||
                    description.contains("ZEPTO") ||
                    description.contains("BLINKIT GURGAON ") ||
                    description.contains("BLINKIT") ||
                    description.contains("RSP*INSTAMART BANGALORE ") ||
                    description.contains("INSTAMART")) {
                transactionCategorization.append("Grocery");
                transactionCategorization.append(";");
            }
            if (description.contains("FASTAG") ||
                    description.contains("HDFC BANK FASTAG MUMBAI ")) {
                transactionCategorization.append("Travel");
                transactionCategorization.append(";");
                transactionCategorization.append("Parking");
                transactionCategorization.append(";");
            }
            if (description.contains("FILLING STATION") ||
                    description.contains("filling stati") ||
                    description.contains("PETRO") ||
                    description.contains("PETROL") ||
                    description.contains("ANAND FILLING STATION DELHI ") ||
                    description.contains("SHIV SHAKTI PETRO GURGAON ")) {
                transactionCategorization.append("Fuel");
                transactionCategorization.append(";");
            }
            if (description.contains("AMAZON WEB SERVICES") ||
                    description.contains("AMAZON WEB SERVICES MUMBAI ")) {
                transactionCategorization.append("Study");
                transactionCategorization.append(";");
            }

            if (description.contains("CAFE") ||
                    description.contains("cafe") ||
                    description.contains("restaurant") ||
                    description.contains("RESTAURANT") ||
                    description.contains("Cafe De Flora") ||
                    description.contains("INDRA COFFEE ROASTER P RGURUGRAM ") ||
                    description.contains("KAMATHS NATURAL RETAIL NEW DELHI ") ||
                    description.contains("ICE CREAM COMPANY Gurgaon ") ||
                    description.contains("Payu*Swiggy Food Bangalore ") ||
                    description.contains("Swiggy") ||
                    description.contains("BUNDL TECHNOLOGIES BENGALURU ") ||
                    description.contains("BUNDL TECHNOLOGIES") ||
                    description.contains("BREW VILLA UDAIPUR ") ||
                    description.contains("RESTAURANT AMBRAI A UN UDAIPUR ") ||
                    description.contains("PPSL*Jubilant Foodwork Noida ") ||
                    description.contains("Jubilant")) {
                transactionCategorization.append("Dineout");
                transactionCategorization.append(";");
                transactionCategorization.append("Food");
                transactionCategorization.append(";");
            }

            if (description.contains("M S MICROTEK GREENBURG GURUGRAM ") ||
                    description.contains("MICROTEK GREENBURG")) {
                transactionCategorization.append("HouseMaintenance");
                transactionCategorization.append(";");
                transactionCategorization.append("Electricity");
                transactionCategorization.append(";");
            }

            if (description.contains("YOUTUBEGOOGLE MUMBAI ") ||
                    description.contains("YOUTUBE")) {
                transactionCategorization.append("MonthlySubscription");
                transactionCategorization.append(";");
                transactionCategorization.append("Entertainment");
                transactionCategorization.append(";");
                transactionCategorization.append("Study");
                transactionCategorization.append(";");
            }

            if (description.contains("BHARTI AIRTEL LTD GURGAON ") ||
                    description.contains("BHARTI AIRTEL") &&
                            (creditCardTransaction.amt() != null &&
                                    creditCardTransaction.amt().compareTo(new BigDecimal("1169")) == 1 &&
                                    creditCardTransaction.amt().compareTo(new BigDecimal("1181")) == -1)) {
                transactionCategorization.append("Monthly");
                transactionCategorization.append(";");
                transactionCategorization.append("Internet");
                transactionCategorization.append(";");
            }

            if (description.contains("HOUSE OF DIAGNOSTICS H ENEW DELHI ") ||
                    description.contains("HOUSE OF DIAGNOSTICS")) {
                transactionCategorization.append("Medical");
                transactionCategorization.append(";");
                transactionCategorization.append("Health");
                transactionCategorization.append(";");
            }

            if (description.contains("UDAIPUR")) {
                transactionCategorization.append("Travel");
                transactionCategorization.append(";");
            }

            if (description.contains("AMAZON PAY INDIA PRIVA www.amazon.i ") ||
                    description.contains("www.amazon.i") ||
                    description.contains("AMAZON PAY INDIA PRIVA Bangalore ") ||
                    description.contains("AMAZON PAY INDIA") ||
                    description.contains("NYKAA E RETAIL LIMI BANGALORE ") ||
                    description.contains("MYNTRA DESIGNS PRIVATE BANGALORE ") ||
                    description.contains("MYNTRA") ||
                    description.contains("Myntra Designs Pvt Ltd BANGALORE ")) {
                transactionCategorization.append("OnlineShopping");
                transactionCategorization.append(";");
            }

            creditCardTransactionCategorizations.add(new CreditCardTransactionCategorization(
                    creditCardTransaction.statementFileId(),
                    creditCardTransaction.id(),
                    transactionCategorization.toString()
            ));
        }
        return creditCardTransactionCategorizations;
    }
}
