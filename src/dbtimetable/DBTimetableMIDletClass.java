package dbtimetable;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;

import java.io.*;
import javax.microedition.io.*;

import java.util.*;

import org.json.me.*; import org.json.me.util.*; 
import dbtimetable.DateHelper;

public class DBTimetableMIDletClass extends MIDlet implements CommandListener {
	private Display display;
	
	Hashtable routelistTable = new Hashtable();
	Hashtable subroutelistTable = new Hashtable();
	
	private List routeList;
	private List routeSelectionList;
	private List timetableList;
	private Command exit = new Command("Exit", Command.EXIT, 1);
	private Command back = new Command("Back", Command.BACK, 2);
	private Command back_to_main = new Command("Back", Command.BACK, 2);
	private Command about = new Command("About", Command.HELP, 3);
	Alert alert;
	
	Form form;
	
	boolean mainscreen = true;		// flag for main screen selection, false for subroutes
	boolean timetablescreen = false;	// flag for overwriting timetable screen
	
	String help_text = "DBTimetable is a the new electronic timetable for the Discovery Bay community. You can view all the buses and ferries connecting the Discovery Bay community. \r\n\r\nFor more information, please go to http://db.tdw.hk/\r\n\r\nProgram developed by:\r\nEmil Chan,\r\nKenneth Anguish";
	String hk_public_holidays;
	
	// Check if today is weekdays
	private boolean is_weekdays(Calendar the_date) {
		
		// MONDAY -> 2
		// TUESDAY -> 3
		// WEDNESDAY -> 4
		// THURSDAY -> 5
		// FRIDAY -> 6
		
		if (( the_date.get( Calendar.DAY_OF_WEEK ) >= Calendar.MONDAY) &&
			( the_date.get( Calendar.DAY_OF_WEEK ) <= Calendar.FRIDAY) ) {
			return true;
		} else {
			return false;
		}
	}
	
	// Check if today is Saturday
	private boolean is_saturday( Calendar the_date ) {
		if (the_date.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY) {
			return true;
		} else {
			return false;
		}
	}
	
	// Check if today is Saturday
	private boolean is_sunday( Calendar the_date ) {
		if (the_date.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}

	// Check if today is public holiday
	private boolean is_public_holiday_check( Calendar the_date ) {
		boolean is_holiday = false;
		
		String[] holidays_readlines = DateHelper.split(hk_public_holidays);
		
		for (int i=0; i<holidays_readlines.length; i++ ) {
			
			if (DateHelper.arbitraryDateInString( the_date ).trim().equals( holidays_readlines[i].trim() ) ) {
				// System.out.println("Today is holiday");
				
				is_holiday = true;
			}
		}
	    return is_holiday;
	}
	
	public DBTimetableMIDletClass() {
		// Init J2ME UI List
		routeList = new List("DBTimetable", Choice.IMPLICIT);
		
		// Read JSON table
		String ji = readFile( "/outline.json" );
		
		try {
			JSONObject outer = new JSONObject(ji);
	        if (outer != null) {
	        	JSONArray ja = outer.getJSONArray("tableList");
	        	
                if (ja != null) {
                    for (int i=0; i<ja.length(); i++) {
                        JSONObject inner = ja.getJSONObject(i);
                        String routename = inner.getString("routename");
                        
                        JSONArray ia = inner.getJSONArray("routes");
                        
                        String[] subroutes = new String[2];
                        for (int j=0; j<ia.length(); j++) {
                        	JSONObject innermost = ia.getJSONObject(j);
                        	String subroutename = innermost.getString("routename");
                        	String subroutefilename = innermost.getString("filename");
                        	subroutes[j] = subroutename;
                        	// System.out.println( subroutename );
                        	// System.out.println( "---------------------------" );
                        	subroutelistTable.put(subroutename, subroutefilename);
                        }
                        
                        // Appending table
                        routeList.append( routename, null);
                        routelistTable.put( routename, subroutes);
                    }
                }
	        }
			
		} catch (Exception e) {
			System.out.println("Fail to init with JSON reader");
			e.printStackTrace();
		}
		
		// Read Hong Kong Public Holidays
		hk_public_holidays = readFile( "/hk_public_holidays.txt" );
		
		/*
		System.out.println( "today_is_public_holiday_check: " + is_public_holiday_check( DateHelper.timenow() ) );
		
		System.out.println( "today_is_weekdays: " + is_weekdays( DateHelper.timenow() ) );
		System.out.println( "today_is_saturday: " +  is_saturday( DateHelper.timenow() ) );
		*/
		
		routeList.addCommand(exit);
		routeList.addCommand(about);
		routeList.setCommandListener(this);
	}

	protected void destroyApp(boolean unconditional) {
			// throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}
	
	String bufferString;
	
	private String readFile(String filename) {
	    try {
		    InputStream is = getClass().getResourceAsStream(filename);

	    	StringBuffer sb = new StringBuffer();
	    	int chr = 0;
	    	while ((chr = is.read()) != -1)
	    		sb.append((char) chr);
	 
	    	// System.out.println( sb.toString());
	    	bufferString = sb.toString();
	    	
	    	return bufferString;
	    } catch (Exception e) {         
	    	System.out.println("Unable to create stream");
	    	e.printStackTrace();
	    	
	    	return "";
	    }
	}
	
	protected void startApp() throws MIDletStateChangeException {
		display = Display.getDisplay(this);
		display.setCurrent(routeList);
	}
	
	public void commandAction(Command command, Displayable displayable) {
		
	    if (command == List.SELECT_COMMAND) {
	    	if (mainscreen) {
	    		String selection = routeList.getString(routeList.getSelectedIndex());
	    		
	    		routeSelectionList = new List(selection, Choice.IMPLICIT);
	    		routeSelectionList.addCommand(back);
	    		routeSelectionList.setCommandListener(this);
	    		
	    		String[] subroutes = (String[]) routelistTable.get(selection);
	    		
	    		for (int i=0; i<2; i++) {
	    			routeSelectionList.append( subroutes[i], null);
	    		}
	    		display.setCurrent(routeSelectionList);
		      
	    		mainscreen = false;
	    	} else {
	    		String selection = routeSelectionList.getString(routeSelectionList.getSelectedIndex());
	    		
	    		String filename = (String) subroutelistTable.get(selection);
	    		String the_filename = "";
	    		
	    		form = new Form( selection );
	    		form.addCommand(back_to_main);
	    		form.setCommandListener(this);
	    		
	    		Calendar current_time = DateHelper.timenow();

	    		// Fetching the 1st day's listing
	    		if ( is_weekdays( DateHelper.timenow() ) && (! (is_public_holiday_check( DateHelper.timenow() )))) {
	    			the_filename = filename + "_Weekdays.txt";
	    		} else if ( is_sunday( DateHelper.timenow() ) ||is_public_holiday_check( DateHelper.timenow() ) ) {
	    			the_filename = filename + "_Sunday_and_Public_Holiday.txt";
	    		} else if ( is_saturday( DateHelper.timenow() ) ) {
	    			the_filename = filename + "_Saturday.txt";
	    		}
	    		
	    		String timetable_file = readFile( "/" + the_filename );
	    		String[] timetable_readlines = DateHelper.splitWithUnix( timetable_file );
	    		
	    		int counter = 0;
	    		
	    		for (int i=0; i<timetable_readlines.length; i++) {
	    			Calendar the_timeslot = DateHelper.time_of_slot( timetable_readlines[i].substring(0, 2), timetable_readlines[i].substring(3, 5) );
	    			
	    			if ( the_timeslot.after( current_time )) {
	    				/*
	    				System.out.println("Time should be: " + 
	    						timetable_readlines[i].substring(0, 2) + ":" + 
	    						timetable_readlines[i].substring(3, 5) + " with " + 
	    						( the_timeslot.getTime().getTime() - current_time.getTime().getTime() ) / 1000 / 60 + 
	    						" minutes    |" + the_timeslot);
	    				*/
	    				if (counter < 5) {
	    					form.append( timetable_readlines[i] + " (" +
	    					( the_timeslot.getTime().getTime() - current_time.getTime().getTime() ) / 1000 / 60 
	    					+ " mins left)\n");
	    				} else {
	    					form.append( timetable_readlines[i] + "\n" );
	    				}
	    				counter++;
	    			}
	    		}	    		
	    		
	    		if (counter < 10) {
		    		
		    		// Fetching the second's day listing
		    		Calendar tomorrow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		    		long difftime = DateHelper.timenow().getTime().getTime() + 24 * 60 * 60 * 1000;
		    		tomorrow.setTime( new Date(difftime) );
		    		
		    		if ( is_weekdays( tomorrow ) && (! (is_public_holiday_check( tomorrow )))) {
		    			the_filename = filename + "_Weekdays.txt";
		    		} else if ( is_sunday( tomorrow ) ||is_public_holiday_check( tomorrow ) ) {
		    			the_filename = filename + "_Sunday_and_Public_Holiday.txt";
		    		} else if ( is_saturday( tomorrow ) ) {
		    			the_filename = filename + "_Saturday.txt";
		    		}
		    		
		    		timetable_file = readFile( "/" + the_filename );
		    		timetable_readlines = DateHelper.splitWithUnix( timetable_file );
		    		
		    		for (int i=0; i<timetable_readlines.length; i++) {
		    			Calendar the_timeslot = DateHelper.time_of_slot_tomorrow( timetable_readlines[i].substring(0, 2), timetable_readlines[i].substring(3, 5) );
		    			
		    			if ( the_timeslot.after( current_time )) {
		    				/*
		    				System.out.println("Time should be: " + 
		    						timetable_readlines[i].substring(0, 2) + ":" + 
		    						timetable_readlines[i].substring(3, 5) + " with " + 
		    						( the_timeslot.getTime().getTime() - current_time.getTime().getTime() ) / 1000 / 60 + 
		    						" minutes    |" + the_timeslot);
		    				*/
		    				if (counter < 5) {
		    					form.append( timetable_readlines[i] + " (" +
		    					( the_timeslot.getTime().getTime() - current_time.getTime().getTime() ) / 1000 / 60 
		    					+ " mins left)\n");
		    				} else {
		    					form.append( timetable_readlines[i] + "\n" );
		    				}
		    				counter++;
		    			}
		    		}	    		
	    		}
	    		display.setCurrent( form );

	    		mainscreen = true;
	    		
	    		// System.out.println( table_content);
	    	}
	    } else if (command == back) {
	    	System.out.println("--> Back triggered");
	    	
			display = Display.getDisplay(this);
			display.setCurrent(routeList);
			mainscreen = true;
	    } else if (command == back_to_main) {
	    	display = Display.getDisplay(this);
			display.setCurrent(routeList);
			mainscreen = true;
		} else if (command == about) {
		      alert = new Alert("About DBTimetable", help_text, null, null);
		      alert.setTimeout(Alert.FOREVER);
		      alert.setType(AlertType.INFO);
		      display.setCurrent(alert);
		} else if (command == exit) {
	    	destroyApp(false);
	    	notifyDestroyed();
	    }
	}
}
