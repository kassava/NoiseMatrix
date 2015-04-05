package com.shiz.noisematrix.mirer;

import java.util.Random;

import com.shiz.noisematrix.SettingsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DrawUtils {
//	private final static String LOG_TAG = "DrawUtils";

	private static int mirerCrossEV;
	private static int mirerCrossSD;
	private static int mirerBackgroundEV;
	private static int mirerBackgroundSD;
	private static Context mContext;

	public void setContext(Context ctx) {
		mContext = ctx;
	}

	public static void drawRing(int[][] byteArray, int x, int y, int r1,
			int r2, int color) {
		circle_a(byteArray, x, y, r1, color);
		circle_a(byteArray, x, y, r2, color);

		if (r1 < r2) {
			int r = r2;
			r2 = r1;
			r1 = r;
		}
		
		// Mirer parameters.
		mirerCrossEV = 180;
		mirerCrossSD = 30;
		mirerBackgroundEV = 90;
		mirerBackgroundSD = 30;

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(mContext);

		String keyValue = pref.getString(SettingsActivity.CROSS_EV, "");
		mirerCrossEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.CROSS_SD, "");
		mirerCrossSD = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.BACKGROUND_EV, "");
		mirerBackgroundEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.BACKGROUND_SD, "");
		mirerBackgroundSD = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.CIRCLES_COUNT, "");

		for (int i = x - r1; i < x + r1; i++) {
			for (int j = y - r1; j < y + r1; j++) {
				double l = Math.sqrt(Math.pow(x - i, 2) + Math.pow(y - j, 2));
				if (l >= r2 && l <= r1) {
					drawPoint(byteArray, i, j, color);
				}
			}
		}
	}

	public static void drawPoint(int[][] byteArray, int x, int y, int color) {
		if (x < 0 || y < 0) {
//			Log.d(LOG_TAG, "x = " + x + " y = " + y);
			return;
		}
		if (x > 255 || y > 255) {
//			Log.d(LOG_TAG, "x = " + x + " y = " + y);
			return;
		}
		Random rand = new Random();
		double nextRandValue;

		nextRandValue = rand.nextGaussian() * mirerCrossSD + mirerCrossEV;
		if (color == 0)
			byteArray[x][y] = (int) Math.round(nextRandValue);
		nextRandValue = rand.nextGaussian() * mirerBackgroundSD
				+ mirerBackgroundEV;
		if (color == 1)
			byteArray[x][y] = (int) Math.round(nextRandValue);

		if (byteArray[x][y] < 0) {
			byteArray[x][y] = 0;
		}
		if (byteArray[x][y] > 255) {
			byteArray[x][y] = 255;
		}
	}

	public static void circle_a(int[][] byteArray, int x, int y, int r,
			int color) {
		int sx = 0;
		int sy = r;
		int d = 3 - 2 * r;
		while (sx <= sy) {
			drawPoint(byteArray, x + sx, y - sy, color);
			drawPoint(byteArray, x + sx, y + sy, color);
			drawPoint(byteArray, x - sx, y - sy, color);
			drawPoint(byteArray, x - sx, y + sy, color);

			drawPoint(byteArray, x + sy, y + sx, color);
			drawPoint(byteArray, x - sy, y + sx, color);
			drawPoint(byteArray, x + sy, y - sx, color);
			drawPoint(byteArray, x - sy, y - sx, color);

			if (d < 0) {
				d = d + 4 * sx + 6;
			} else {
				d = d + 4 * (sx - sy) + 10;
				sy = sy - 1;
			}
			sx += 1;
		}
	}
}
