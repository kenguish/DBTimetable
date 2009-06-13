package dbtimetable;

import java.util.*;

public class DateHelper {
	
	public DateHelper() {
		
	}
	
	public static Calendar timenow() {
		Calendar timenow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		/*
		System.out.println("Today's date is : " + 
				
				timenow.get(Calendar.YEAR) + 
				"-" + 
				( timenow.get(Calendar.MONTH ) + 1 ) + 
				"-" +
				timenow.get(Calendar.DAY_OF_MONTH) + 
				" " +
				timenow.get(Calendar.HOUR_OF_DAY) + 
				":" +
				timenow.get(Calendar.MINUTE) 
		);
		*/
		
		return timenow ;
	}
	
	public static Calendar time_of_slot(String hour_of_day, String minute) {
		Calendar timenow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		int year = timenow.get(Calendar.YEAR);
		int month = timenow.get(Calendar.MONTH );
		int day = timenow.get(Calendar.DAY_OF_MONTH);
		
		timenow.set(Calendar.YEAR, year );
		timenow.set(Calendar.MONTH, month );
		timenow.set(Calendar.DAY_OF_MONTH, day );
		timenow.set(Calendar.HOUR_OF_DAY, Integer.parseInt( hour_of_day ) );
		timenow.set(Calendar.MINUTE, Integer.parseInt( minute ) );
		
		//( year , month, day, hour_of_day, minute);
		
		return timenow ;
	}
	
	public static Calendar time_of_slot_tomorrow(String hour_of_day, String minute) {

		long difftime = timenow().getTime().getTime() + 24 * 60 * 60 * 1000;
		
		Calendar timenow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		timenow.setTime( new Date(difftime) );

		
		int year = timenow.get(Calendar.YEAR);
		int month = timenow.get(Calendar.MONTH );
		int day = timenow.get(Calendar.DAY_OF_MONTH);
		
		timenow.set(Calendar.YEAR, year );
		timenow.set(Calendar.MONTH, month );
		timenow.set(Calendar.DAY_OF_MONTH, day );
		timenow.set(Calendar.HOUR_OF_DAY, Integer.parseInt( hour_of_day ) );
		timenow.set(Calendar.MINUTE, Integer.parseInt( minute ) );
		
		//( year , month, day, hour_of_day, minute);
		
		return timenow ;
	}

	
	public static String todayDateInString() {
		Calendar timenow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		
		return timenow.get(Calendar.YEAR) + 
			"-" + 
			( timenow.get( Calendar.MONTH ) + 1 ) + 
			"-" +
			timenow.get(Calendar.DAY_OF_MONTH);
	}
	
	public static String arbitraryDateInString( Calendar the_date ) {
		return the_date.get(Calendar.YEAR) + 
		"-" + 
		( the_date.get( Calendar.MONTH ) + 1 ) + 
		"-" +
		the_date.get(Calendar.DAY_OF_MONTH);

	}
	
	public static String[] split(String original) {
		Vector nodes = new Vector();
		String separator = "\r\n";
		// Parse nodes into vector
		int index = original.indexOf(separator);
		while(index>=0) {
			nodes.addElement( original.substring(0, index) );
			original = original.substring(index+separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement( original );

		// Create splitted string array
		String[] result = new String[ nodes.size() ];
		if( nodes.size()>0 ) {
			for(int loop=0; loop<nodes.size(); loop++)
			{
				result[loop] = (String)nodes.elementAt(loop);
			}
		}

		return result;
	}
	
	public static String[] splitWithUnix(String original) {
		Vector nodes = new Vector();
		String separator = "\n";
		// Parse nodes into vector
		int index = original.indexOf(separator);
		while(index>=0) {
			nodes.addElement( original.substring(0, index) );
			original = original.substring(index+separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement( original );

		// Create splitted string array
		String[] result = new String[ nodes.size() ];
		if( nodes.size()>0 ) {
			for(int loop=0; loop<nodes.size(); loop++)
			{
				result[loop] = (String)nodes.elementAt(loop);
			}
		}

		return result;
	}

}
