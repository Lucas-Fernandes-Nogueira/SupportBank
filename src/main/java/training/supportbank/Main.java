package training.supportbank;
import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main(String args[]) {
        LOGGER.info("Starting...");
        Bank supportBank = new Bank();
        supportBank.addTransactionFromCSVFile("C:\\Work\\Training\\SupportBank\\SupportBank\\Transactions2014.csv");
        supportBank.addTransactionFromCSVFile("C:\\Work\\Training\\SupportBank\\SupportBank\\DodgyTransactions2015.csv");
        supportBank.bankInterface();
    }
}
