package databeans;

public class Position {

	private int customer_id = 0;
	private int fund_id = 0;
	private double shares = 0;

	public int getCustomer_id() {
		return customer_id;
	}
	
	public int getFund_id() {
		return fund_id;
	}
	
	public double getShares() {
		return shares;
	}


	public void setCustomer_id(int x) {
		customer_id = x;
	}

	public void setFund_id(int x) {
		fund_id = x;
	}
	
	public void setShares(double x) {
		shares = Math.round(x*1000)*1.0/1000;
	}
}
