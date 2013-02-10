package databeans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
	
	private int transaction_id = 0;
	private int customer_id = 0;
	private int fund_id = 0;
	private double shares = 0.0;
	private Date execute_date = null;
	private String transaction_type = null;
	private double amount = 0.0;
	private String status = "";
	
	private String fundName = "";
	private double fundPrice = 0.0;
	
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getFund_id() {
		return fund_id;
	}
	public void setFund_id(int fund_id) {
		this.fund_id = fund_id;
	}
	public double getShares() {
		return shares;
	}
	public void setShares(double shares) {
		this.shares = Math.round(shares*1000)*1.0/1000;
	}
	public Date getExecute_date() {
		return execute_date;
	}
	public void setDate(Date execute_date) {
		this.execute_date = execute_date;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = Math.round(amount*100)*1.0/100;
	}
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFundName() {
        return fundName;
    }
    public void setFundName(String fundName) {
        this.fundName = fundName;
    }
    public double getFundPrice() {
        return fundPrice;
    }
    public void setFundPrice(double fundPrice) {
        this.fundPrice = Math.round(fundPrice*100)*1.0/100;
    }
	
    public String getExecute_dateStr() {
        if(execute_date==null) return "";
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(execute_date);
    }
	
}
