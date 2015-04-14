package com.shiz.noisematrix.resolution;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import android.graphics.Point;
import android.util.Log;

/**
 * Methods of this class calculate linear resolution for the various mirers.
 * @author ultra
 *
 */
public class LinearResolution {
	private final static String LOG_TAG = "LinearResolution";
	
	public static double[] linearResolutionFor4Circles(int[][] byteArray) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		int ringWidth = 10;
		int r = 10;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 189, 189, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 189, 189, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double maxMirerMean = stats.getMean();
		Log.d(LOG_TAG, "maxMirerMean = " + maxMirerMean);
		
		stats.clear();
		for (int i = 100; i < 120; i++) {
			for (int j = 127; j < 256; j++) {
				stats.addValue(byteArray[i][j]);
			}
		}
		for (int i = 0; i < 100; i++) {
			for (int j = 125; j < 150; j++) {
				stats.addValue(byteArray[i][j]);
			}
		}
		double backgroundMean = stats.getMean();
		Log.d(LOG_TAG, "background mean: " + backgroundMean);
		double max = maxMirerMean / backgroundMean;
		Log.d(LOG_TAG, "max: " + max);
		
		stats.clear();
		ringWidth = 7;
		r = 7;
		for (int i = 0; i < 3; i ++) {
//			calculateRing(byteArray, stats, 47, 208, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 47, 208, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean7 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean7: " + backgroundMean7);
		stats.clear();
		ringWidth = 7;
		r = 7;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 47, 208, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 47, 208, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double[] Kcp = new double[4];
		Kcp[0] = (stats.getSum() / backgroundMean7) / stats.getN();
		Log.d(LOG_TAG, "Kcp7 = " + Kcp[0]);
		Log.d(LOG_TAG, "Kcp7/max = " + (Kcp[0] / max));
		
		stats.clear();
		ringWidth = 8;
		r = 8;
		for (int i = 0; i < 3; i ++) {
//			calculateRing(byteArray, stats, 200, 55, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 200, 55, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean8 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean8: " + backgroundMean8);
		stats.clear();
		ringWidth = 8;
		r = 8;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 200, 55, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 200, 55, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[1] = (stats.getSum() / backgroundMean8) / stats.getN();
		Log.d(LOG_TAG, "Kcp8 = " + Kcp[1]);
		Log.d(LOG_TAG, "Kcp8/max = " + (Kcp[1] / max));
		
		stats.clear();
		ringWidth = 9;
		r = 9;
		for (int i = 0; i < 3; i ++) {
//			calculateRing(byteArray, stats, 63, 63, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 63, 63, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean9 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean9: " + backgroundMean9);
		
		stats.clear();
		ringWidth = 9;
		r = 9;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 63, 63, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 63, 63, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[2] = (stats.getSum() / backgroundMean9) / stats.getN();
		Log.d(LOG_TAG, "Kcp9 = " + Kcp[2]);
		Log.d(LOG_TAG, "Kcp9/max = " + (Kcp[2] / max));
		
		stats.clear();
		ringWidth = 10;
		r = 10;
		for (int i = 0; i < 3; i ++) {
//			calculateRing(byteArray, stats, 189, 189, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 189, 189, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean10 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean10: " + backgroundMean10);
		stats.clear();
		ringWidth = 10;
		r = 10;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 189, 189, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 189, 189, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[3] = (stats.getSum() / backgroundMean10) / stats.getN();
		Log.d(LOG_TAG, "Kcp10 = " + Kcp[3]);
		Log.d(LOG_TAG, "Kcp10/max = " + (Kcp[3] / max));
		
		for (int i = 0; i < 4; i++) {
			Kcp[i] /= max;
		}
		
		return Kcp;
	}
	
	public static double[] linearResolutionFor6Circles(int[][] byteArray) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		int ringWidth = 6;
		int r = 6;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double maxMirerMean = stats.getMean();
		Log.d(LOG_TAG, "maxMirerMean = " + maxMirerMean);
		
		stats.clear();
		for (int i = 156; i < 256; i++) {
			for (int j = 102; j < 152; j++) {
				stats.addValue(byteArray[i][j]);
			}
		}
		for (int i = 0; i < 100; i++) {
			for (int j = 102; j < 152; j++) {
				stats.addValue(byteArray[i][j]);
			}
		}
		for (int i = 105; i < 150; i++) {
			for (int j = 0; j < 60; j++) {
				stats.addValue(byteArray[i][j]);
			}
		}
		double backgroundMean = stats.getMean();
		Log.d(LOG_TAG, "background mean: " + backgroundMean);
		double max = maxMirerMean / backgroundMean;
		Log.d(LOG_TAG, "max: " + max);
		
		stats.clear();
		ringWidth = 1;
		r = 1;
		for (int i = 0; i < 5; i ++) {
//			calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean1 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean1: " + backgroundMean1);
		stats.clear();
		ringWidth = 1;
		r = 1;
		for (int i = 0; i < 5; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 127, 95, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double[] Kcp = new double[6];
		Kcp[0] = (stats.getSum() / backgroundMean1) / stats.getN();
		Log.d(LOG_TAG, "Kcp1 = " + Kcp[0]);
		Log.d(LOG_TAG, "Kcp1/max = " + (Kcp[0] / max));
		
		stats.clear();
		ringWidth = 2;
		r = 2;
		for (int i = 0; i < 5; i ++) {
//			calculateRing(byteArray, stats, 110, 180, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 110, 180, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean2 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean2: " + backgroundMean2);
		stats.clear();
		ringWidth = 2;
		r = 2;
		for (int i = 0; i < 5; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 110, 180, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 110, 180, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[1] = (stats.getSum() / backgroundMean2) / stats.getN();
		Log.d(LOG_TAG, "Kcp2 = " + Kcp[1]);
		Log.d(LOG_TAG, "Kcp2/max = " + (Kcp[1] / max));
		
		stats.clear();
		ringWidth = 3;
		r = 3;
		for (int i = 0; i < 4; i ++) {
//			calculateRing(byteArray, stats, 40, 225, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 40, 225, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean3 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean3: " + backgroundMean3);
		
		stats.clear();
		ringWidth = 3;
		r = 3;
		for (int i = 0; i < 4; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 40, 225, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 40, 225, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[2] = (stats.getSum() / backgroundMean3) / stats.getN();
		Log.d(LOG_TAG, "Kcp3 = " + Kcp[2]);
		Log.d(LOG_TAG, "Kcp3/max = " + (Kcp[2] / max));
		
		stats.clear();
		ringWidth = 4;
		r = 4;
		for (int i = 0; i < 4; i ++) {
//			calculateRing(byteArray, stats, 210, 45, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 210, 45, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean4 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean4: " + backgroundMean4);
		stats.clear();
		ringWidth = 4;
		r = 4;
		for (int i = 0; i < 4; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 210, 45, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 210, 45, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[3] = (stats.getSum() / backgroundMean4) / stats.getN();
		Log.d(LOG_TAG, "Kcp4 = " + Kcp[3]);
		Log.d(LOG_TAG, "Kcp4/max = " + (Kcp[3] / max));
		
		stats.clear();
		ringWidth = 5;
		r = 5;
		for (int i = 0; i < 4; i ++) {
//			calculateRing(byteArray, stats, 50, 50, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 50, 50, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean5 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean5: " + backgroundMean5);
		stats.clear();
		ringWidth = 5;
		r = 5;
		for (int i = 0; i < 4; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 50, 50, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 50, 50, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[4] = (stats.getSum() / backgroundMean5) / stats.getN();
		Log.d(LOG_TAG, "Kcp5 = " + Kcp[4]);
		Log.d(LOG_TAG, "Kcp5/max = " + (Kcp[4] / max));
		
		stats.clear();
		ringWidth = 6;
		r = 6;
		for (int i = 0; i < 3; i ++) {
//			calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			CalcUtils.calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		double backgroundMean6 = stats.getMean();
		Log.d(LOG_TAG, "backgeroundmean6: " + backgroundMean6);
		stats.clear();
		ringWidth = 6;
		r = 6;
		for (int i = 0; i < 3; i ++) {
			CalcUtils.calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		Kcp[5] = (stats.getSum() / backgroundMean6) / stats.getN();
		Log.d(LOG_TAG, "Kcp6 = " + Kcp[5]);
		Log.d(LOG_TAG, "Kcp6/max = " + (Kcp[5] / max));
		
		for (int i = 0; i < 6; i++) {
			Kcp[i] /= max;
		}
		
		return Kcp;
	}
	
	public static double[] linearResolutionFor10Circles(int[][] byteArray) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		Circles circles = Circles.getInstance();
		Point center = circles.getCircleCenter10(10);
		
		int ringWidth = 10;
		int r = 10;
		for (int i = 0; i < 5; i ++) {
			CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 0);
			r += (ringWidth);
//			calculateRing(byteArray, stats, 210, 210, r, r + ringWidth - 1, 1);
			r += (ringWidth);
			Log.d(LOG_TAG, "N = " + stats.getN());
		}
		double maxMirerMean = stats.getMean();
		Log.d(LOG_TAG, "N = " + stats.getN());
		Log.d(LOG_TAG, "maxMirerMean = " + maxMirerMean);
		
		stats.clear();
		for (int i = 1800; i < 2800; i++) {
			for (int j = 1800; j < 2800; j++) {
				stats.addValue(byteArray[i][j]);
			}
		}
		double backgroundMean = stats.getMean();
		Log.d(LOG_TAG, "background mean: " + backgroundMean);
		double max = maxMirerMean / backgroundMean;
		Log.d(LOG_TAG, "max: " + max);
		
		ringWidth = 1;
		r = 1;
		double[] Kcp = new double[10];
		double backgroundMeanI = 0;
		for (int idx = 0; idx < 10; idx++) {
			ringWidth = idx + 1;
			r = idx + 1;
			center = circles.getCircleCenter10(idx + 1);
			
//			Log.d(LOG_TAG, "ringWidth = " + ringWidth + ", r = " + r);
//			Log.d(LOG_TAG, "center: " + center.x + " - " + center.y);
			stats.clear();
			for (int i = 0; i < 5; i++) {
//				Log.d(LOG_TAG, "r: " + r + ", r + ringWidth: " + (r + ringWidth));
//				CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 0);
				r += (ringWidth);
				CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 1);
				r += (ringWidth);
			}
			backgroundMeanI = stats.getMean();
			Log.d(LOG_TAG, "backgeroundmean[:" + idx + "] = " + backgroundMeanI);
			
			ringWidth = idx + 1;
			r = idx + 1;
			stats.clear();
			for (int i = 0; i < 5; i++) {
//				Log.d(LOG_TAG, "r: " + r + ", r + ringWidth: " + (r + ringWidth));
				CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 0);
				r += (ringWidth);
//				CalcUtils.calculateRing(byteArray, stats, center.x, center.y, r, r + ringWidth - 1, 1);
				r += (ringWidth);
			}
			Kcp[idx] = (stats.getSum() / backgroundMeanI) / stats.getN();
			Log.d(LOG_TAG, "Kcp[" + idx + "] = " + Kcp[0]);
			Log.d(LOG_TAG, "Kcp[" + idx + "]/max = " + (Kcp[0] / max));
		}
		
		for (int i = 0; i < 10; i++) {
			Kcp[i] /= max;
		}
		
		return Kcp;
	}
	
	public static void linearResolutionForFukoMirer(int[][] byteArray) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int row = 0, column = 0;
		for (row = 146; row <= 165; row++) {
			for (column = 10; column < 22; column++) {
				stats.addValue(byteArray[row][column]);
			}
			for (column = 34; column < 46; column++) {
				stats.addValue(byteArray[row][column]);
			}
		}
		double mean12 = stats.getMean();
		Log.d(LOG_TAG, "mean 12: " + mean12);
		
		stats = new DescriptiveStatistics();
		for (row = 0; row <= 115; row++) {
			for (column = 60; column <= 115; column++) {
				stats.addValue(byteArray[row][column]);
			}
			for (column = 140; column <= 195; column++) {
				stats.addValue(byteArray[row][column]);
			}
		}
		for (row = 140; row < 256; row++) {
			for (column = 60; column <= 115; column++) {
				stats.addValue(byteArray[row][column]);
			}
			for (column = 140; column <= 195; column++) {
				stats.addValue(byteArray[row][column]);
			}
		}
		double backgroundMean = stats.getMean();
		double max = mean12 / backgroundMean;
		Log.d(LOG_TAG, "backgeound mean: " + backgroundMean);
		Log.d(LOG_TAG, "max: " + max);
		
		stats = new DescriptiveStatistics();
		for (row = 10; row < 30; row++) {
			for (column = 9; column < 30; column += 2) {
				stats.addValue(byteArray[row][column]);
			}
		}
		double mirer1BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 1 background: " + mirer1BackgroundMean);
		
		stats = new DescriptiveStatistics();
		for (row = 10; row < 30; row++) {
			for (column = 10; column < 30; column += 2) {
				stats.addValue(byteArray[row][column] / mirer1BackgroundMean);
			}
		}
		double[] Kcp = new double[12];
//		double KcpM1 = stats.getMean();
		Kcp[0] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m1: " + Kcp[0]);
		Log.d(LOG_TAG, "Kcp m1 / Max: " + Kcp[0] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 10; row < 30; row++) {
			for (column = 226; column < 248; column++) {
				if (column >= 246 && column < 248) {
					stats.addValue(byteArray[row][column]);
				}
				if (column >= 242 && column < 244) {
					stats.addValue(byteArray[row][column]);
				}
				if (column >= 238 && column < 240) {
					stats.addValue(byteArray[row][column]);
				}
				if (column >= 234 && column < 236) {
					stats.addValue(byteArray[row][column]);
				}
				if (column >= 230 && column < 232) {
					stats.addValue(byteArray[row][column]);
				}
				if (column >= 226 && column < 228) {
					stats.addValue(byteArray[row][column]);
				}
			}
		}
		double mirer2BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 2 background: " + mirer2BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 10; row < 30; row++) {
			for (column = 228; column < 245; column++) {
				if (column >= 244 && column < 245) {
					stats.addValue(byteArray[row][column] / mirer2BackgroundMean);
				}
				if (column >= 240 && column < 241) {
					stats.addValue(byteArray[row][column] / mirer2BackgroundMean);
				}
				if (column >= 236 && column < 237) {
					stats.addValue(byteArray[row][column] / mirer2BackgroundMean);
				}
				if (column >= 232 && column < 233) {
					stats.addValue(byteArray[row][column] / mirer2BackgroundMean);
				}
				if (column >= 228 && column < 229) {
					stats.addValue(byteArray[row][column] / mirer2BackgroundMean);
				}
			}
		}
//		double KcpM2 = stats.getMean();
		Kcp[1] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m2: " + Kcp[1]);	
		Log.d(LOG_TAG, "Kcp m2 / Max: " + Kcp[1] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 226; row < 246; row++) {
			for (column = 222; column < 249; column++) {
    				if (column >= 246 && column <= 248) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 240 && column <= 242) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 234 && column <= 236) {  
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 228 && column <= 230) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 222 && column <= 224) {
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer3BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 3 background: " + mirer3BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 226; row < 246; row++) {
			for (column = 228; column < 245; column++) {
				if (column >= 243 && column <= 245) {
					stats.addValue(byteArray[row][column] / mirer3BackgroundMean);
				}
				if (column >= 237 && column <= 239) {
					stats.addValue(byteArray[row][column] / mirer3BackgroundMean);
				}
				if (column >= 231 && column <= 233) {
					stats.addValue(byteArray[row][column] / mirer3BackgroundMean);
				}
				if (column >= 225 && column <= 227) {
					stats.addValue(byteArray[row][column] / mirer3BackgroundMean);
				}
			}
		}
//		double KcpM3 = stats.getMean();
		Kcp[2] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m3: " + Kcp[2]);	
		Log.d(LOG_TAG, "Kcp m3 / Max: " + Kcp[2] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 226; row < 246; row++) {
			for (column = 6; column < 34; column++) {
    				if (column >= 6 && column <= 9) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 14 && column <= 17) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 22 && column <= 25) {  
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 30 && column <= 33) {
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer4BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 4 background: " + mirer4BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 226; row < 246; row++) {
			for (column = 10; column < 30; column++) {
				if (column >= 10 && column <= 13) {
					stats.addValue(byteArray[row][column] / mirer4BackgroundMean);
				}
				if (column >= 18 && column <= 21) {
					stats.addValue(byteArray[row][column] / mirer4BackgroundMean);
				}
				if (column >= 26 && column <= 29) {
					stats.addValue(byteArray[row][column] / mirer4BackgroundMean);
				}
			}
		}
//		double KcpM4 = stats.getMean();
		Kcp[3] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m4: " + Kcp[3]);	
		Log.d(LOG_TAG, "Kcp m4 / Max: " + Kcp[3] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 45; row < 65; row++) {
			for (column = 6; column < 34; column++) {
    				if (column >= 5 && column <= 9) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 15 && column <= 19) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 25 && column <= 29) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer5BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 5 background: " + mirer5BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 45; row < 65; row++) {
			for (column = 10; column < 30; column++) {
				if (column >= 10 && column <= 14) {
					stats.addValue(byteArray[row][column] / mirer5BackgroundMean);
				}
				if (column >= 20 && column <= 24) {
					stats.addValue(byteArray[row][column] / mirer5BackgroundMean);
				}
			}
		}
//		double KcpM5 = stats.getMean();
		Kcp[4] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m5: " + Kcp[4]);	
		Log.d(LOG_TAG, "Kcp m5 / Max: " + Kcp[4] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 45; row < 65; row++) {
			for (column = 222; column < 252; column++) {
    				if (column >= 222 && column <= 227) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 234 && column <= 239) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 246 && column <= 251) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer6BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 6 background: " + mirer6BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 45; row < 65; row++) {
			for (column = 228; column < 246; column++) {
				if (column >= 228 && column <= 233) {
					stats.addValue(byteArray[row][column] / mirer6BackgroundMean);
				}
				if (column >= 240 && column <= 245) {
					stats.addValue(byteArray[row][column] / mirer6BackgroundMean);
				}
			}
		}
//		double KcpM6 = stats.getMean();
		Kcp[5] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m6: " + KcpM6);	
		Log.d(LOG_TAG, "Kcp m6 / Max: " + Kcp[5] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 191; row < 211; row++) {
			for (column = 218; column < 251; column++) {
    				if (column >= 218 && column <= 224) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 232 && column <= 238) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 246 && column <= 250) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer7BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 7 background: " + mirer7BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 191; row < 211; row++) {
			for (column = 225; column < 246; column++) {
				if (column >= 225 && column <= 231) {
					stats.addValue(byteArray[row][column] / mirer7BackgroundMean);
				}
				if (column >= 239 && column <= 245) {
					stats.addValue(byteArray[row][column] / mirer7BackgroundMean);
				}
			}
		}
//		double KcpM7 = stats.getMean();
		Kcp[6] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m7: " + KcpM7);	
		Log.d(LOG_TAG, "Kcp m7 / Max: " + Kcp[6] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 191; row < 211; row++) {
			for (column = 2; column < 42; column++) {
    				if (column >= 2 && column <= 9) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 18 && column <= 25) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 34 && column <= 41) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer8BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 8 background: " + mirer8BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 191; row < 211; row++) {
			for (column = 10; column < 34; column++) {
				if (column >= 10 && column <= 17) {
					stats.addValue(byteArray[row][column] / mirer8BackgroundMean);
				}
				if (column >= 26 && column <= 33) {
					stats.addValue(byteArray[row][column] / mirer8BackgroundMean);
				}
			}
		}
//		double KcpM8 = stats.getMean();
		Kcp[7] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m8: " + KcpM8);	
		Log.d(LOG_TAG, "Kcp m8 / Max: " + Kcp[7] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 90; row < 110; row++) {
			for (column = 1; column < 46; column++) {
    				if (column >= 1 && column <= 9) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 19 && column <= 27) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 37 && column <= 45) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer9BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 9 background: " + mirer9BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 90; row < 110; row++) {
			for (column = 10; column < 37; column++) {
				if (column >= 10 && column <= 18) {
					stats.addValue(byteArray[row][column] / mirer9BackgroundMean);
				}
				if (column >= 28 && column <= 36) {
					stats.addValue(byteArray[row][column] / mirer9BackgroundMean);
				}
			}
		}
//		double KcpM9 = stats.getMean();
		Kcp[8] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m9: " + KcpM9);	
		Log.d(LOG_TAG, "Kcp m9 / Max: " + Kcp[8] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 90; row < 110; row++) {
			for (column = 206; column < 256; column++) {
    				if (column >= 206 && column <= 215) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 226 && column <= 235) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 246 && column <= 255) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer10BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 10 background: " + mirer10BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 90; row < 110; row++) {
			for (column = 216; column < 246; column++) {
				if (column >= 216 && column <= 225) {
					stats.addValue(byteArray[row][column] / mirer10BackgroundMean);
				}
				if (column >= 236 && column <= 245) {
					stats.addValue(byteArray[row][column] / mirer10BackgroundMean);
				}
			}
		}
//		double KcpM10 = stats.getMean();
		Kcp[9] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m10: " + KcpM10);	
		Log.d(LOG_TAG, "Kcp m10 / Max: " + Kcp[9] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 146; row < 166; row++) {
			for (column = 202; column < 256; column++) {
    				if (column >= 202 && column <= 212) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 224 && column <= 234) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 246 && column <= 255) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer11BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 11 background: " + mirer11BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 146; row < 166; row++) {
			for (column = 213; column < 246; column++) {
				if (column >= 213 && column <= 223) {
					stats.addValue(byteArray[row][column] / mirer11BackgroundMean);
				}
				if (column >= 235 && column <= 245) {
					stats.addValue(byteArray[row][column] / mirer11BackgroundMean);
				}
			}
		}
//		double KcpM11 = stats.getMean();
		Kcp[10] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m11: " + KcpM11);	
		Log.d(LOG_TAG, "Kcp m11 / Max: " + Kcp[10] / max);
		
		stats = new DescriptiveStatistics();
		for (row = 146; row < 166; row++) {
			for (column = 0; column < 58; column++) {
    				if (column >= 0 && column <= 9) {
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 22 && column <= 33) {   
    					stats.addValue(byteArray[row][column]);
    				}
    				if (column >= 46 && column <= 57) {  
    					stats.addValue(byteArray[row][column]);
    				}
			}
		}
		double mirer12BackgroundMean = stats.getMean();
//		Log.d(LOG_TAG, "mirer 12 background: " + mirer12BackgroundMean);	
		
		stats = new DescriptiveStatistics();
		for (row = 146; row < 166; row++) {
			for (column = 10; column < 46; column++) {
				if (column >= 10 && column <= 21) {
					stats.addValue(byteArray[row][column] / mirer12BackgroundMean);
				}
				if (column >= 34 && column <= 45) {
					stats.addValue(byteArray[row][column] / mirer12BackgroundMean);
				}
			}
		}
//		double KcpM12 = stats.getMean();
		Kcp[11] = stats.getMean();
//		Log.d(LOG_TAG, "Kcp m12: " + KcpM12);	
		Log.d(LOG_TAG, "Kcp m12 / Max: " + Kcp[11] / max);
		
//		int VALUES_ROWS = 3;
//		int VALUES_COLUMNS = 4;

//		for (int i = 0; i < VALUES_ROWS; i++) {
//
//			TableRow tableRow = new TableRow(mContext);
//			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 
//					TableRow.LayoutParams.WRAP_CONTENT);
//			params.gravity = Gravity.CENTER_HORIZONTAL;
//			tableRow.setLayoutParams(params);
////			tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
////					LayoutParams.WRAP_CONTENT));
////			tableRow.setBackgroundResource(R.drawable.shelf);
//
//			for (int j = 0; j < VALUES_COLUMNS; j++) {
//				TextView textView = new TextView(mContext);
//				textView.setText("m" + (i * 4 + j + 1) + "= " 
//						+ String.format("%1$,1.3f", Kcp[i * 4  + j] / max));
//				textView.setPadding(10, 10, 10, 10);
//				tableRow.addView(textView, j);
//			}
//			
//			tableLayout.addView(tableRow, i);
//		}
	}
}
