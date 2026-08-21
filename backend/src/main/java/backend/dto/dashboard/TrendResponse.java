package backend.dto.dashboard;

import java.math.BigDecimal;

public class TrendResponse {

    private String label;

    private BigDecimal amount;

    public TrendResponse() {
    }

    public TrendResponse(String label, BigDecimal amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
