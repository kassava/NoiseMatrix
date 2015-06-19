package com.shiz.noisematrix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

/**
 * Class logging
 * 
 * @author johny homicide
 *
 */
public class DataWriter {

	/**
	 * Write logs in LogCat
	 * 
	 * @param tag
	 * @param inLogCat
	 */
	public static void writeData(String tag, String inLogCat) {
		Log.d(tag, inLogCat);
	}

	/**
	 * Write logs in LogCat and log file
	 * 
	 * @param tag
	 * @param inLogCat
	 * @param inLogFile
	 */
	public static void writeData(String tag, String inLogCat, String inLogFile) {
		Log.d(tag, inLogCat);
		writeLog(tag + " -> " + inLogFile);
	}

	private static void writeLog(String str) {
		File outFile = new File(Environment.getExternalStorageDirectory(), 
				"stats.txt");

		try {
			FileWriter wrt = new FileWriter(outFile, true);
			wrt.append(getCurrentTime() + " : " + str + "\n");
			wrt.flush();
			wrt.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int ms = calendar.get(Calendar.MILLISECOND);
		return String.format("%02d.%02d.%04d %02d:%02d:%02d.%03d", day, month,
				year, hour, minute, second, ms);
	}
}