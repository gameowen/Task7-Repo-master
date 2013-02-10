package utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Format {
    public static String formatShares(double shares) {
        DecimalFormat nf2 = new DecimalFormat("#,##0.000");
        nf2.setMaximumFractionDigits(3);
        nf2.setMinimumFractionDigits(3);
        return nf2.format(shares);
    }
    
    public static String formatMoney(double money) {
        DecimalFormat nf2 = new DecimalFormat("#,##0.00");
        nf2.setMaximumFractionDigits(2);
        nf2.setMinimumFractionDigits(2);
        return "$ "+nf2.format(money);
    }
    
    public static String formatDate(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }
    
    public static String formatDate(java.sql.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }
}
