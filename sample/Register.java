package sample;

public class Register {
    private String mnemonic;
    private String number;
    private int value;

    public Register(String mnemonic, String number) {
        this.mnemonic = mnemonic;
        this.number = number;
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getNumber() {
        return number;
    }

}
