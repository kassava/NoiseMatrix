package com.shiz.noisematrix.mirer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import com.shiz.noisematrix.R;
import com.shiz.noisematrix.SettingsActivity;
import com.shiz.noisematrix.SimpleFileDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CircleMirer extends Fragment implements OnClickListener {

	private final String LOG_TAG = "circlemirer";
	private Context mContext;
	private Button mSaveButton;
	private String mSaveFileName;
	private byte[] mMirerBytes;
	private ImageView mMirer;

	private int mirerDimension;
	private int mirerCrossEV;
	private int mirerCrossSD;
	private int mirerBackgroundEV;
	private int mirerBackgroundSD;
	private int circlesCount;

	public CircleMirer(Context ctx) {
		mContext = ctx;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.screen_circle_mirer,
				container, false);

		mMirer = (ImageView) rootView.findViewById(R.id.imageView1);
		mSaveButton = (Button) rootView.findViewById(R.id.button1);
		mSaveButton.setOnClickListener(this);

		createMirer(mMirer);

		return rootView;
	}

	private void createMirer(ImageView mirer) {
		// Mirer parameters.
		mirerDimension = 256;
		mirerCrossEV = 180;
		mirerCrossSD = 30;
		mirerBackgroundEV = 90;
		mirerBackgroundSD = 30;
		circlesCount = 6;

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
		circlesCount = SettingsActivity.checkValue(keyValue);
		
		switch (circlesCount) {
		case 4:
			break;
		case 6:
			break;
		default:
			circlesCount = 6;
			break;	
		}

		Log.d(LOG_TAG, "CEV = " + mirerCrossEV + ", CSD = " + mirerCrossSD);
		Log.d(LOG_TAG, "BEV = " + mirerBackgroundEV + ", BSD = "
				+ mirerBackgroundSD);

		int[][] byteArray = new int[mirerDimension][mirerDimension];

		Random rand = new Random();
		double nextRandValue = 0;

		// Initialization mirer matrix.
		for (int row = 0; row < mirerDimension; row++) {
			for (int column = 0; column < mirerDimension; column++) {
				nextRandValue = rand.nextGaussian() * mirerBackgroundSD
						+ mirerBackgroundEV;
				byteArray[row][column] = (int) Math.round(nextRandValue);

				if (byteArray[row][column] < 0) {
					byteArray[row][column] = 0;
				}
				if (byteArray[row][column] > 255) {
					byteArray[row][column] = 255;
				}
			}
		}
		
		if (circlesCount == 6) {
			draw6Circles(byteArray);
		}
		if (circlesCount == 4) {
			draw4Circles(byteArray);
//			draw2Circle(byteArray);
		}
				
		mMirerBytes = new byte[mirerDimension * mirerDimension * 4];
		int index = 0;
		for (int row = 0; row < mirerDimension; row++) {
			for (int column = 0; column < mirerDimension; column++) {
				mMirerBytes[index] = (byte) byteArray[row][column];
				mMirerBytes[index + 1] = (byte) byteArray[row][column];
				mMirerBytes[index + 2] = (byte) byteArray[row][column];
				mMirerBytes[index + 3] = 0;
				index += 4;
			}
		}

		ByteBuffer buffer = ByteBuffer.allocate(mMirerBytes.length);
		buffer.put(mMirerBytes);
		buffer.rewind();
		Bitmap bitmap = Bitmap.createBitmap(mirerDimension, mirerDimension,
				Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(buffer);
		mirer.setImageBitmap(bitmap);

		mMirerBytes = new byte[mirerDimension * mirerDimension];
		index = 0;
		for (int row = 0; row < mirerDimension; row++) {
			for (int column = 0; column < mirerDimension; column++) {
				mMirerBytes[index] = (byte) byteArray[row][column];
				index++;
			}
		}
		Log.d(LOG_TAG, "Mirer created");
	}
	
//	private void draw2Circle(int[][] byteArray) {
//		int ringWidth = 1;
//		int r = 100;
//		for (int i = 0; i < 2; i ++) {
//			drawRing(byteArray, 127, 95, r, r + ringWidth - 1, 0);
//			r += (ringWidth);
//			drawRing(byteArray, 127, 95, r, r + ringWidth - 1, 1);
//			r += (ringWidth);
//		}
//		ringWidth = 2;
//		r = 2;
//		for (int i = 0; i < 5; i ++) {
//			drawRing(byteArray, 110, 180, r, r + ringWidth - 1, 0);
//			r += (ringWidth);
//			drawRing(byteArray, 110, 180, r, r + ringWidth - 1, 1);
//			r += (ringWidth);
//		}
//	}
	
	private void draw6Circles(int[][] byteArray) {
		int ringWidth = 1;
		int r = 1;
		for (int i = 0; i < 5; i ++) {
			drawRing(byteArray, 127, 95, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 127, 95, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 2;
		r = 2;
		for (int i = 0; i < 5; i ++) {
			drawRing(byteArray, 110, 180, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 110, 180, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 3;
		r = 3;
		for (int i = 0; i < 4; i ++) {
			drawRing(byteArray, 40, 225, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 40, 225, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 4;
		r = 4;
		for (int i = 0; i < 4; i ++) {
			drawRing(byteArray, 210, 45, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 210, 45, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 5;
		r = 5;
		for (int i = 0; i < 4; i ++) {
			drawRing(byteArray, 50, 50, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 50, 50, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 6;
		r = 6;
		for (int i = 0; i < 3; i ++) {
			drawRing(byteArray, 210, 210, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 210, 210, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
	}
	
	private void draw4Circles(int[][] byteArray) {
		int ringWidth = 7;
		int r = 7;
		for (int i = 0; i < 3; i ++) {
			drawRing(byteArray, 47, 208, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 47, 208, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 8;
		r = 8;
		for (int i = 0; i < 3; i ++) {
			drawRing(byteArray, 200, 55, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 200, 55, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 9;
		r = 9;
		for (int i = 0; i < 3; i ++) {
			drawRing(byteArray, 63, 63, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 63, 63, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
		ringWidth = 10;
		r = 10;
		for (int i = 0; i < 3; i ++) {
			drawRing(byteArray, 189, 189, r, r + ringWidth - 1, 0);
			r += (ringWidth);
			drawRing(byteArray, 189, 189, r, r + ringWidth - 1, 1);
			r += (ringWidth);
		}
	}
	
	private void drawRing(int[][] byteArray, int x, int y, int r1, int r2, int color) {
		circle_a(byteArray, x, y, r1, color);
		circle_a(byteArray, x, y, r2, color);
		
		if (r1 < r2) {
			int r = r2;
			r2 = r1;
			r1 = r;
		}
		
		for (int i = x - r1; i < x + r1; i++) {
			for (int j = y - r1; j < y + r1; j++) {
				double l = Math.sqrt(Math.pow(x - i, 2) + Math.pow(y - j, 2));
				if (l >= r2 && l <= r1) {
					drawPoint(byteArray, i, j, color);
				}
			}
		}
	}
	
	private void drawPoint(int[][] byteArray, int x, int y, int color) {
		if (x < 0 || y < 0) {
			Log.d(LOG_TAG, "x = " + x + " y = " + y);
			return;
		}
		if (x > 255 || y > 255) {
			Log.d(LOG_TAG, "x = " + x + " y = " + y);
			return;
		}
		Random rand = new Random();
		double nextRandValue;
		
		nextRandValue = rand.nextGaussian() * mirerCrossSD
				+ mirerCrossEV;	
		if (color == 0) byteArray[x][y] = (int) Math.round(nextRandValue);
		nextRandValue = rand.nextGaussian() * mirerBackgroundSD
				+ mirerBackgroundEV;
		if (color == 1) byteArray[x][y] = (int) Math.round(nextRandValue);
		
		if (byteArray[x][y] < 0) {
			byteArray[x][y] = 0;
		}
		if (byteArray[x][y] > 255) {
			byteArray[x][y] = 255;
		}
	}	
	
	private void circle_a(int[][] byteArray, int x, int y, int r, int color) {
        int sx = 0;
        int sy = r;
        int d= 3 - 2 * r;
        while(sx <= sy) {
        	drawPoint(byteArray, x + sx, y - sy, color); // g.drawLine(x+sx, y-sy, x+sx, y-sy);
        	drawPoint(byteArray, x + sx, y + sy, color); // g.drawLine(x+sx, y+sy, x+sx, y+sy);
        	drawPoint(byteArray, x - sx, y - sy, color); //g.drawLine(x-sx, y-sy, x-sx, y-sy);
        	drawPoint(byteArray, x - sx, y + sy, color); // g.drawLine(x-sx, y+sy, x-sx, y+sy);
 
        	drawPoint(byteArray, x + sy, y + sx, color); // g.drawLine(x+sy, y+sx, x+sy, y+sx);
        	drawPoint(byteArray, x - sy, y + sx, color); // g.drawLine(x-sy, y+sx, x-sy, y+sx);
        	drawPoint(byteArray, x + sy, y - sx, color); // g.drawLine(x+sy, y-sx, x+sy, y-sx);
        	drawPoint(byteArray, x - sy, y - sx, color); // g.drawLine(x-sy, y-sx, x-sy, y-sx);
 
            if (d < 0) {
                d = d + 4 * sx + 6;
            } else {
                d = d + 4 * (sx - sy) + 10;
                sy = sy - 1;
            }
            sx += 1;   
        }
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			SimpleFileDialog FileSaveDialog = new SimpleFileDialog(mContext,
					"FileSave",
					new SimpleFileDialog.SimpleFileDialogListener() {
						@Override
						public void onChosenDir(String chosenDir) {
							// The code in this function will be executed when
							// the dialog OK button is pushed
							mSaveFileName = chosenDir;
							saveMirerInFile();
							Toast.makeText(mContext,
									"Saved file: " + mSaveFileName,
									Toast.LENGTH_LONG).show();
						}
					});
			FileSaveDialog.Default_File_Name = "default.raw";
			FileSaveDialog.chooseFile_or_Dir();
			break;
		default:
			break;
		}
	}

	private void saveMirerInFile() {
		if (mSaveFileName == null) {
			return;
		}

		File file = new File(mSaveFileName);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(mMirerBytes, 0, mMirerBytes.length);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			Log.d(LOG_TAG, "FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(LOG_TAG, "IOException");
			e.printStackTrace();
		}
	}
}
