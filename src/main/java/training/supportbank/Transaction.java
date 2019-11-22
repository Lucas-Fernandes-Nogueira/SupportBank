package training.supportbank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Transaction {
    String fromAccount;
    String toAccount;
    int valueP;
    String date;
    String narrative;
    private static final Logger LOGGER = LogManager.getLogger(Transaction.class);

    public Transaction(String date, String fromName, String toName, String narrative, int valueP){
        LOGGER.info("Writing transaction...");
        this.fromAccount = fromName;
        this.toAccount = toName;
        this.valueP = valueP;
        this.date = date;
        this.narrative = narrative;
    }
    public Transaction(String fullEntry){
        LOGGER.info("Writing transaction...");
        fullEntry = fullEntry.replaceAll("\r", "").replaceAll("\n", "");
        String[] chunks = fullEntry.split(",");
        this.date = chunks[0];
        this.fromAccount = chunks[1];
        this.toAccount = chunks[2];
        this.narrative = chunks[3];
        try {
            BigDecimal safeValue = new BigDecimal(chunks[4]);
            safeValue = safeValue.multiply(new BigDecimal("100"));
            this.valueP = safeValue.intValue();
        }
        catch (Exception e) {
            LOGGER.error("Transaction value not convertible to BigDecimal.\nFaulty transaction: " + fullEntry);
            throw e;
        }
    }
    void printTransaction(){
        printTransaction(false);
    }

    void printTransaction(boolean header){
        if (header) System.out.println("Date | From | To | Narrative | Amount");
        System.out.printf(this.date + " " + this.fromAccount + " " + this.toAccount + " " + this.narrative + " £%.2f\n", (float) valueP/100);
    }
}
