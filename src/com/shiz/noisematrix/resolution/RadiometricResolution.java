package com.shiz.noisematrix.resolution;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import android.graphics.Point;
import android.util.Log;


/**
 * Methods of this class calculate radiometric resolution for the various mirers.
 * @author ultra
 *
 */
public class RadiometricResolution {
	private final static String LOG_TAG = "RadiometricResolution";
	
	/**
	 * @param byteArray Bytes of mirer
	 * @return
	 */
	public static double radiometricResolutionFor6Circles(int[][] byteArray) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int ringWidth = 1;
		int r = 1;
		for (int i = 0; i < 5; i ++) {
//			calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean = stats.getMean();
		Log.d(LOG_TAG, "background mean = " + backgroundMean);
		
		stats.clear();
		r = 1;
		ringWidth = 1;
		for (int i = 0; i < 5; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		for (int i = 0; i < stats.getN(); i++) {
			Log.d(LOG_TAG, "[" + i + "] = " + stats.getElement(i));
		}
		Log.d(LOG_TAG, "circle mean = " + stats.getMean());
		
		double radiometricResolution = (stats.getSum() / backgroundMean) / stats.getN();
		Log.d(LOG_TAG, "rad = " + radiometricResolution);
		
		return radiometricResolution;
	}
	
	public static double radiometricResolutionFor10Circles(int[][] byteArray) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		Circles circles = Circles.getInstance();
		Point center = circles.getCircleCenter10(1);
		int ringWidth = 1;
		int r = 1;
		
		for (int i = 0; i < 5; i ++) {
//			calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean = stats.getMean();
		Log.d(LOG_TAG, "background mean = " + backgroundMean);
		
		stats.clear();
		r = 1;
		ringWidth = 1;
		for (int i = 0; i < 5; i ++) {
			CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		for (int i = 0; i < stats.getN(); i++) {
			Log.d(LOG_TAG, "[" + i + "] = " + stats.getElement(i));
		}
		Log.d(LOG_TAG, "circle mean = " + stats.getMean());
		
		double radiometricResolution = (stats.getSum() / backgroundMean) / stats.getN();
		Log.d(LOG_TAG, "rad = " + radiometricResolution);
		
		return radiometricResolution;
	}
	
	public static void radiometricResolutionForFukoMirer(int[][] byteArray) {
		int row = 0, column = 0;
		ArrayList<Float> rawMeans = new ArrayList<Float>();
		ArrayList<Integer> crossValues = new ArrayList<Integer>();
		int dimension = 256;

		for (row = 0; row < ((dimension -1) / 2) - 5; row++) {
			float mean = 0;
			int count = 0;
			for (column = 60; column < (dimension - 1) / 2; column++) {
				mean += byteArray[row][column];
				count++;
			}
			for (column = dimension / 2; column < (dimension - 59); column++) {
				mean += byteArray[row][column];
				count++;
			}
			crossValues.add((int) byteArray[row][127]);
			rawMeans.add(mean / count);
		}
		for (row = (dimension / 2) + 5; row < dimension; row++) {
			float mean = 0;
			int count = 0;
			for (column = 60; column < (dimension - 1) / 2; column++) {
				mean += byteArray[row][column];
				count++;
			}
			for (column = dimension / 2; column < (dimension - 59); column++) {
				mean += byteArray[row][column];
				count++;
			}
			crossValues.add((int) byteArray[row][127]);
			rawMeans.add(mean / count);
		}
		
		for (int idx = 0; idx < rawMeans.size(); idx++) {
			Log.d(LOG_TAG, " = " + idx + " - " + rawMeans.get(idx) 
					+ " - " + crossValues.get(idx));
			rawMeans.set(idx, crossValues.get(idx) /  rawMeans.get(idx));
			Log.d(LOG_TAG, " - " + idx + " - " + rawMeans.get(idx));
		}
		
		Float radiometricresolution = Float.valueOf(0);
		Log.d(LOG_TAG, "rawMeans.size: " + rawMeans.size());
		for (int idx = 0; idx < rawMeans.size(); idx++) {
			radiometricresolution += rawMeans.get(idx);
			Log.d(LOG_TAG, " - " + idx + " - " 
					+ radiometricresolution + " - " + rawMeans.get(idx));
		}
		radiometricresolution /= rawMeans.size();
		Log.d(LOG_TAG, "radiometricresolution = " + radiometricresolution);
//		textView.setText(String.format("%1$,1.3f", radiometricresolution));
	}
}
