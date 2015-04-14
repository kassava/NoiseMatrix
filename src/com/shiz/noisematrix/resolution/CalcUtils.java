package com.shiz.noisematrix.resolution;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Class of the utils that calculate rings on mirer.
 * @author ultra
 *
 */
public class CalcUtils {
	
	public static void calculateRing(int[][] byteArray, 
			DescriptiveStatistics sts, int x, int y, int r1, int r2, int color) {
		circle_a(byteArray, sts, x, y, r1, color);
		circle_a(byteArray, sts, x, y, r2, color);
		
		if (r1 < r2) {
			int r = r2;
			r2 = r1;
			r1 = r;
		}
		
		for (int i = x - r1; i < x + r1; i++) {
			for (int j = y - r1; j < y + r1; j++) {
				double l = Math.sqrt(Math.pow(x - i, 2) + Math.pow(y - j, 2));
				if (l >= r2 && l <= r1) {
					calculatePoint(byteArray, sts, i, j, color);
				}
			}
		}
	}
	
	public static void circle_a(int[][] byteArray, 
			DescriptiveStatistics sts, int x, int y, int r, int color) {
        int sx = 0;
        int sy = r;
        int d= 3 - 2 * r;
        while(sx <= sy) {
        	calculatePoint(byteArray, sts, x + sx, y - sy, color); 
        	calculatePoint(byteArray, sts, x + sx, y + sy, color); 
        	calculatePoint(byteArray, sts, x - sx, y - sy, color); 
        	calculatePoint(byteArray, sts, x - sx, y + sy, color); 
 
        	calculatePoint(byteArray, sts, x + sy, y + sx, color); 
        	calculatePoint(byteArray, sts, x - sy, y + sx, color); 
        	calculatePoint(byteArray, sts, x + sy, y - sx, color); 
        	calculatePoint(byteArray, sts, x - sy, y - sx, color); 
 
            if (d < 0) {
                d = d + 4 * sx + 6;
            } else {
                d = d + 4 * (sx - sy) + 10;
                sy = sy - 1;
            }
            sx += 1;   
        }
    }
	
	public static void calculatePoint(int[][] byteArray, 
			DescriptiveStatistics sts, int x, int y, int color) {
		if (x < 0 || y < 0) {
			return;
		}
		if (x > 3840 || y > 3840) {
			return;
		}
		sts.addValue(byteArray[x][y]);
	}
}
