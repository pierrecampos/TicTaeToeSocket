package model.constants;

public enum Token {
    CROSS((boolean) true),
    CIRCLE((boolean) false);

    public boolean value;
    Token(boolean value){
        this.value = value;
    }
}
