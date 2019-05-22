
package Pass2;

public class IntermediateFileLine {
    private int lineNumber;
    private String address;
    private String label;
    private String mnemonic;
    private String operand;

    public IntermediateFileLine(String lineNumber, String address, String label, String mnemonic, String operand) {
        this.lineNumber = Integer.parseInt(lineNumber);
        this.address = address;
        this.label = label;
        this.mnemonic = mnemonic;
        this.operand = operand;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getLabel() {
        return label;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getOperand() {
        return operand;
    }
    
    @Override
    public String toString() {
        return lineNumber + "-" + address + "-" + label + "-" + mnemonic + "-" + operand;
    }
    
}
