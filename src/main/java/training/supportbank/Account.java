package training.supportbank;

public class Account {
    String name;
    int creditP;
    public Account(String name, int creditP){
        this.name = name;
        this.creditP = creditP;
    }
    void printStatement(){
        System.out.printf(this.name + " £%.2f\n", (float) creditP/100);
    }
}
