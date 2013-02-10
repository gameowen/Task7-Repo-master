package databeans;

public class Portfolio {
    
    private int fundId = 0;
    public int getFundId() {
        return fundId;
    }
    public void setFundId(int fundId) {
        this.fundId = fundId;
    }

    private String fundName = "";
    private double shares = 0.0;
    private double price = 0.0;
    private double total = 0.0;
    
    public String getFundName() {
        return fundName;
    }
    public void setFundName(String fundName) {
        this.fundName = fundName;
    }
    public double getShares() {
        return shares;
    }
    public void setShares(double shares) {
        this.shares = Math.round(shares*1000)*1.0/1000;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = Math.round(price*100)*1.0/100;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = Math.round(total*100)*1.0/100;
    }
    
    @Override
    public String toString() {
        return "Portfolio [fundName=" + fundName + ", shares=" + shares
                + ", price=" + price + ", total=" + total + "]";
    }
    
    
}
