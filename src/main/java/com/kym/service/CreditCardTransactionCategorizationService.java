package com.kym.service;

import com.kym.dto.CreditCardTransactionCategorization;
import com.kym.entity.CreditCardTransaction;
import com.kym.entity.StatementDetail;
import com.kym.entity.Transaction;
import com.kym.repository.CreditCardTransactionJdbcRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditCardTransactionCategorizationService implements TransactionCategorizationService<CreditCardTransaction> {

    private final CreditCardTransactionJdbcRepository creditCardTransactionJdbcRepository;

    public CreditCardTransactionCategorizationService(CreditCardTransactionJdbcRepository creditCardTransactionJdbcRepository) {
        this.creditCardTransactionJdbcRepository = creditCardTransactionJdbcRepository;
    }

    @Override
    public int[] categorize(List<CreditCardTransaction> creditCardTransactions, StatementDetail statementDetail) {
        List<CreditCardTransactionCategorization> creditCardTransactionCategorizations = categorize(creditCardTransactions);
        return creditCardTransactionJdbcRepository.updateTransactionCategorization(creditCardTransactionCategorizations);
    }

    private List<CreditCardTransactionCategorization> categorize(List<CreditCardTransaction> creditCardTransactions) {
        List<CreditCardTransactionCategorization> creditCardTransactionCategorizations = new ArrayList<>();
        for (CreditCardTransaction creditCardTransaction : creditCardTransactions) {
            StringBuilder transactionCategorization = new StringBuilder();
            String description = creditCardTransaction.getDescription();
            if (description.contains("Instamart") ||
                    description.contains("Payu*Instamart Grocery Bangalore ") ||
                    description.contains("ZEPTO MARKETPLACE PRIV Bangalore ") ||
                    description.contains("ZEPTO") ||
                    description.contains("BLINKIT GURGAON ") ||
                    description.contains("BLINKIT") ||
                    description.contains("RSP*INSTAMART BANGALORE ") ||
                    description.contains("INSTAMART")) {
                transactionCategorization.append("OnlineGrocery");
                transactionCategorization.append(";");
            }

            if (description.contains("SHOP EXPRESS 24 X7 PRI FARIDABAD")) {
                transactionCategorization.append("OfflineGrocery");
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
                    description.contains("SHIV SHAKTI PETRO GURGAON ") ||
                    description.contains("SHREE BALAJI FUELS PALWAL") ||
                    description.contains("FUEL")) {
                transactionCategorization.append("Fuel");
                transactionCategorization.append(";");
            }
            if (description.contains("AMAZON WEB SERVICES") ||
                    description.contains("AMAZON WEB SERVICES MUMBAI ")) {
                transactionCategorization.append("Study");
                transactionCategorization.append(";");
            }

            if (description.contains("Payu*Swiggy Food Bangalore ") ||
                    description.contains("WWW SWIGGY IN BANGALORE") ||
                    description.contains("Swiggy") ||
                    description.contains("BUNDL TECHNOLOGIES BENGALURU ") ||
                    description.contains("BUNDL TECHNOLOGIES")) {
                transactionCategorization.append("Food");
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
                    description.contains("BREW VILLA UDAIPUR ") ||
                    description.contains("RESTAURANT AMBRAI A UN UDAIPUR ") ||
                    description.contains("PPSL*Jubilant Foodwork Noida ") ||
                    description.contains("Jubilant") ||
                    description.contains("Arcelia BANGALORE") ||
                    description.contains("BBIEGE 15 BANGALORE")) {
                transactionCategorization.append("Dineout");
                transactionCategorization.append(";");
            }

            if (description.contains("URBANCLAP TECHNOLOGIES GURGAON")) {
                transactionCategorization.append("HouseMaintenance");
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
                    description.contains("BHARTI AIRTEL")) {
                transactionCategorization.append("Telecom");
                transactionCategorization.append(";");
            }

            if ((description.contains("BHARTI AIRTEL LTD GURGAON ") ||
                    description.contains("BHARTI AIRTEL")) &&
                    (creditCardTransaction.getAmt() != null &&
                            creditCardTransaction.getAmt().compareTo(new BigDecimal("1169")) == 1 &&
                            creditCardTransaction.getAmt().compareTo(new BigDecimal("1181")) == -1)) {
                transactionCategorization.append("Monthly");
                transactionCategorization.append(";");
                transactionCategorization.append("Internet");
                transactionCategorization.append(";");
            }

            if (description.contains("HOUSE OF DIAGNOSTICS H ENEW DELHI ") ||
                    description.contains("HOUSE OF DIAGNOSTICS") ||
                    description.contains("IMPERIAL SMILES DENTA GURGAON")) {
                transactionCategorization.append("Medical");
                transactionCategorization.append(";");
            }

            if (description.contains("Chandigarh") ||
                    description.contains("MMT HOTEL VIA SMARTBU BANGALORE") ||
                    description.contains("SHIMLA") ||
                    description.contains("UDAIPUR")) {
                transactionCategorization.append("Vacation");
                transactionCategorization.append(";");
            }

            if (description.contains("CALCUTTA SAREE SELECTI OGURGAON") ||
                    description.contains("MEENA BAZAAR GURGAON")) {
                transactionCategorization.append("OfflineShopping");
                transactionCategorization.append(";");
            }

            if (description.contains("Myntra Designs Pvt Ltd BANGALORE") ||
                    description.contains("GYFTR VIA SMARTBUY NEW DELHI") ||
                    description.contains("AMAZON PAY INDIA PRIVA Bangalore") ||
                    description.contains("Cashfree*Myntra BANGALORE") ||
                    description.contains("MYNTRA DESIGNS BANGALORE") ||
                    description.contains("WWW AMAZON IN GURGAON") ||
                    description.contains("AMAZON PAY INDIA PRIVA www.amazon.i")) {
                transactionCategorization.append("OnlineShopping");
                transactionCategorization.append(";");
            }

            if (creditCardTransaction.getTxnType().contains("International")) {
                transactionCategorization.append("InternationalTransaction");
                transactionCategorization.append(";");
            }

            if (description.contains("DISTRICT MOVIE TICKE NEW DELHI") ||
                    description.contains("Movie") ||
                    description.contains("PVR") ||
                    description.contains("INOX") ||
                    description.contains("RSP*DISTRICT MOVIE TI GURUGRAM")) {
                transactionCategorization.append("Movie");
                transactionCategorization.append(";");
            }

            if (description.contains("UBER INDIA SYSTE PVT NOIDA")) {
                transactionCategorization.append("Cab");
                transactionCategorization.append(";");
            }


            creditCardTransactionCategorizations.add(new CreditCardTransactionCategorization(
                    creditCardTransaction.getStatementFileId(),
                    creditCardTransaction.getId(),
                    transactionCategorization.toString()
            ));
        }
        return creditCardTransactionCategorizations;
    }
}
