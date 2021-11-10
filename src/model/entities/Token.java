package model.entities;

public enum Token {
    CROSS((short) 0),
    CIRCLE((short) 1);

    public short value;
    Token(short value){
        this.value = value;
    }
}
