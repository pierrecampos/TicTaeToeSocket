public class Campos {
    private String symbol;
    public Campos(){
        this.symbol = " ";
    }

    public String getSymbol(){
        return this.symbol;
    }

    public void setSymbol(String symbol){
        if(this.symbol == " "){
            this.symbol = symbol;
        }else{
            System.out.println("Campo preenchido");
        }
    }
}
