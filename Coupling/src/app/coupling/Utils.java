package app.coupling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static Date toDate(String template){
		Date date = null;
		try {
			SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = inFormat.parse(template);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
