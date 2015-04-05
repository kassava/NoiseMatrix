package com.shiz.noisematrix.resolution;

import android.graphics.Point;
import android.util.Log;


public class Circles {
	 private static Circles instance; 
	 
	 private Circles (){ 
	 } 
	 
	 public static Circles getInstance(){ 
		 if (null == instance){ 
			 instance = new Circles(); 
		 } 
		 
		 return instance; 
	 } 
	 
	 private int[][] FourCirclesCenters = {{47, 208}, 
			 					   {200, 55}, 
			 					   {63, 63}, 
			 					   {189, 189}};
	 private int[][] SixCirclesCenters = {{127, 95}, 
			 					  {110, 180}, 
			 					  {40, 225}, 
			 					  {210, 45}, 
			 					  {50, 50},
			 				      {210, 210}};
	 private int[][] TenCirclesCenters = {{40, 40},
			 					          {100, 100}, 
			 					          {300, 300},
			 					          {1000, 1000},
			 					          {300, 300},
			 					          {1300, 300}, 
			 					          {2300, 300},
			 					          {300, 1300},
			 					          {300, 2300},
			 					          {2300, 3300}};
	 
	 /**
	 * @param i i: 7 <= i <= 10
	 * @return null when i < 7 and i > 10
	 */
	public Point getCircleCenter4(int i) {
		if (i > 10 || i < 7) {
			return null;
		}
		Point center = new Point(FourCirclesCenters[i - 7][0], 
				FourCirclesCenters[i - 7][1]);
		return center;
	}
	
	/**
	 * @param i i: 1 <= i <= 6
	 * @return null when i < 1 and i > 6
	 */
	public Point getCircleCenter6(int i) {
		if (i > 6 || i < 1) {
			return null;
		}
		Point center = new Point(SixCirclesCenters[i - 1][0], 
				SixCirclesCenters[i - 1][1]);
		return center;
	}
	
	/**
	 * @param i i: 1 <= i <= 10
	 * @return null when i < 1 and i > 10
	 */
	public Point getCircleCenter10(int i) {
		if (i < 1 || i > 10) {
			return null;
		}
		Log.d("circles", "i = " + i);
		Point center = new Point(TenCirclesCenters[i - 1][0], 
				TenCirclesCenters[i - 1][1]);
		Log.d("circles", "Point: " + center.x + " - " + center.y);
		return center;
	}
}
