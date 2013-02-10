package databeans;

import java.sql.Date;

public class Fund {
	private int id = -1; // fund_id, primary key
	private String name = null; // the full name for the fund
	private String symbol = null; // the short name for the fund
	private Date date; // the last update date for this fund
	private double price; // the latest price for this fund and converted to String
	private double shares;
	
	public double getShares() {
		return shares;
	}

	public void setShares(double shares) {
		this.shares = shares;
	}

	public int compareTo(Fund other) {
		int c = name.compareTo(other.name);
		if (c != 0) return c;
		return symbol.compareTo(other.symbol);
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Fund) {
			Fund other = (Fund) obj;
			return name.equals(other.name) || symbol.equals(other.symbol);
		}
		return false;
	}
	
	public String getName()      { return name;       }
	public String getSymbol()    { return symbol;     }
	public int    getId()        { return id;         }
	public Date   getDate()      { return date;       }
	public double getPrice()     { return price;      }
	
	public void setName(String a)    { name = a;   }
	public void setSymbol(String a)  { symbol = a; }
	public void setId(int a)         { id = a;     }
	public void setDate(Date d)      { date = d;   }
	public void setPrice(double pr)   { price = Math.round(pr*100)*1.0/100;  }
	
	public String toString() {
		return "Fund("+getName()+","+getSymbol()+")";
	}
}
