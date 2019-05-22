
package Pass2;

public class SymbolTableLine {
    private String label;
    private String address;

    public SymbolTableLine(String label, String address) {
        this.label = label;
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public String getAddress() {
        return address;
    }
    
    @Override
    public String toString() {
        return label + "-" + address;
    }
}
