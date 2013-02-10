package databeans;

import java.sql.Date;

public class History {
	
	private int id = -1; //fund_id
	private Date date = null; //price date
	private double price = 0.00; //daily price
	
	public int getId()       { return id;    }
	public Date getDate()    { return date;  }
	public double getPrice() { return price; }
	
	public void setId(int a)       { id = a;       }
	public void setDate(Date a)    { date = a;     }
	public void setPrice(double a) { price = Math.round(a*100)*1.0/100;    }
	
}
