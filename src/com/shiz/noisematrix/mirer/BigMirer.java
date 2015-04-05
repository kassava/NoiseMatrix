package com.shiz.noisematrix.mirer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
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

import com.shiz.noisematrix.R;
import com.shiz.noisematrix.SettingsActivity;
import com.shiz.noisematrix.SimpleFileDialog;
import com.shiz.noisematrix.resolution.Circles;

/**
 * Class for creation a big mirer (3840pix x 3840pix).
 * @author ultra
 *
 */
public class BigMirer extends Fragment implements OnClickListener {
	private final String LOG_TAG = "BigMirer";
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
	
	public BigMirer(Context ctx) {
		mContext = ctx;
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.screen_bigmirer,
				container, false);

		mMirer = (ImageView) rootView.findViewById(R.id.imageView1);
		mSaveButton = (Button) rootView.findViewById(R.id.button1);
		mSaveButton.setOnClickListener(this);

//		createMirer(mMirer);
		
		MirerCreationTask mcTask = new MirerCreationTask();
		mcTask.execute(mMirer);
		try {
			Boolean result = false;
			result = mcTask.get();
			if (result == true) {
				Log.d(LOG_TAG, "result true");
			} else {
				Log.d(LOG_TAG, "result false");
			}
		} catch (InterruptedException e) {
			Log.d(LOG_TAG, "InterruptedException");
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.d(LOG_TAG, "ExecutionException");
			e.printStackTrace();
		}
		

		return rootView;
	}
	
	private void createMirer(ImageView mirer){
		// Mirer parameters.
		mirerDimension = 3840;
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
		
		draw10Circles(byteArray);
		
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
	
	private void draw10Circles(int[][] byteArray) {
		Circles circles = Circles.getInstance();
		DrawUtils du = new DrawUtils();
		du.setContext(mContext);
		Point center; 
		int ringWidth = 1;
		int r = 1;
		
		for (int idx = 0; idx < 10; idx++) {
			ringWidth = idx + 1;
			r = idx + 1;
			center = circles.getCircleCenter10(idx + 1);
			
			for (int i = 0; i < 5; i ++) {
				DrawUtils.drawRing(byteArray, center.x, center.y, r, r + ringWidth - 1, 0);
				r += (ringWidth);
				DrawUtils.drawRing(byteArray, center.x, center.y, r, r + ringWidth - 1, 1);
				r += (ringWidth);
			}
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
	
	class MirerCreationTask extends AsyncTask<ImageView, Void, Boolean> {

		@Override
		protected Boolean doInBackground(ImageView... params) {
			createMirer(params[0]);
			return true;
		}
		
		@Override
	    protected void onPostExecute(Boolean result) {
	      super.onPostExecute(result);
	      Log.d(LOG_TAG, "End. Result = " + result);
	    }
	}
}
