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

public class Mirer extends Fragment implements OnClickListener {
	
	private final String LOG_TAG = "Mirer";
	private ImageView mMirer;
	private Button mSaveButton;
	private String mSaveFileName;
	private Context mContext;
	private byte[] bytes; // Mirer bytes.
	private int mirerDimension;
	private int mirerCrossEV;
	private int mirerCrossSD;
	private int mirerBackgroundEV;
	private int mirerBackgroundSD;
	private int mirerFieldsEV;
	private int mirerFieldsSD;
	
	public Mirer(Context context) {
		mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.screen_mirer, container,
                false);
        
        mMirer = (ImageView) rootView.findViewById(R.id.imageView1);
        mSaveButton = (Button) rootView.findViewById(R.id.button1);
        mSaveButton.setOnClickListener(this);
        
//        createMirer(mMirer);
        createMirer2(mMirer);

        return rootView;
    }
    
    /**
     * Create mirer: grayscale image with a specified parameters.
     * @param mirer
     */
    private void createMirer(ImageView mirer) {
    	// Mirer parameters.
    	mirerDimension = 256;
    	mirerCrossEV = 180;
    	mirerCrossSD = 30;
    	mirerBackgroundEV = 90;
    	mirerBackgroundSD = 30;
    	mirerFieldsEV = 200;
    	mirerFieldsSD = 30;
    	
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		 
		String keyValue = pref.getString(SettingsActivity.CROSS_EV, "");
		mirerCrossEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.CROSS_SD, "");
		mirerCrossSD = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.BACKGROUND_EV, "");
		mirerBackgroundEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.BACKGROUND_SD, "");
		mirerBackgroundSD = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.FIELDS_EV, "");
		mirerFieldsEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.FIELDS_SD, "");
		mirerFieldsSD = SettingsActivity.checkValue(keyValue);
    	
    	Log.d(LOG_TAG, "CEV = " + mirerCrossEV + ", CSD = " + mirerCrossSD);
    	Log.d(LOG_TAG, "BEV = " + mirerBackgroundEV + ", BSD = " + mirerBackgroundSD);
    	
    	int[][] mirerBytes = new int[mirerDimension][mirerDimension];
    	
    	Random rand = new Random();
    	double nextValue = 0;
    	
    	// Initialization mirer matrix.
    	for (int row = 0; row < mirerDimension; row++) {
    		for (int column = 0; column < mirerDimension; column++) {
    			if (row == ((mirerDimension - 1) / 2) || column == ((mirerDimension - 1) / 2)) {
    				nextValue = rand.nextGaussian() * mirerCrossSD + mirerCrossEV;
        			mirerBytes[row][column] = (int) Math.round(nextValue);
        			
        			if (mirerBytes[row][column] > 255) {
        				mirerBytes[row][column] = 255;
        			}
        			if (mirerBytes[row][column] < 0) {
        				mirerBytes[row][column] = 0;
        			}
        			continue;
    			}
    			
    			nextValue = rand.nextGaussian() * mirerBackgroundSD 
    					+ mirerBackgroundEV;
    			mirerBytes[row][column] = (int) Math.round(nextValue);
    			
    			if (mirerBytes[row][column] < 0) {
    				mirerBytes[row][column] = 0;
    			}
    			if (mirerBytes[row][column] > 255) {
    				mirerBytes[row][column] = 255;
    			}
    		}
    	}
    	
    	firstQuarter(mirerBytes, mirerDimension);
    	secondQuarter(mirerBytes, mirerDimension);
    	thirdQuarter(mirerBytes, mirerDimension);
    	fourthQuarter(mirerBytes, mirerDimension);
    	
    	bytes = new byte[mirerDimension * mirerDimension * 4];
    	int index = 0;
    	for (int row = 0; row < mirerDimension; row++) {
    		for (int column = 0; column < mirerDimension; column++) {	
    			bytes[index] = (byte) mirerBytes[row][column];
    			bytes[index + 1] = (byte) mirerBytes[row][column];
    			bytes[index + 2] = (byte) mirerBytes[row][column];
    			bytes[index + 3] = 0;
    			index += 4;
    		}
    	}
    		
    	ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.rewind();
		Bitmap bitmap = Bitmap.createBitmap(mirerDimension, mirerDimension, Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(buffer);
		mirer.setImageBitmap(bitmap);		
		
		bytes = new byte[mirerDimension * mirerDimension];
    	index = 0;
    	for (int row = 0; row < mirerDimension; row++) {
    		for (int column = 0; column < mirerDimension; column++) {	
    			bytes[index] = (byte) mirerBytes[row][column];
    			index++;
    		}
    	}
    	Log.d(LOG_TAG, "Mirer created");
    }
    
    private void createMirer2(ImageView mirer) {
    	mirerDimension = 256;
    	mirerCrossEV = 180;
    	mirerCrossSD = 30;
    	mirerBackgroundEV = 90;
    	mirerBackgroundSD = 30;
    	mirerFieldsEV = 200;
    	mirerFieldsSD = 30;
    	
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		 
		String keyValue = pref.getString(SettingsActivity.CROSS_EV, "");
		mirerCrossEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.CROSS_SD, "");
		mirerCrossSD = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.BACKGROUND_EV, "");
		mirerBackgroundEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.BACKGROUND_SD, "");
		mirerBackgroundSD = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.FIELDS_EV, "");
		mirerFieldsEV = SettingsActivity.checkValue(keyValue);
		keyValue = pref.getString(SettingsActivity.FIELDS_SD, "");
		mirerFieldsSD = SettingsActivity.checkValue(keyValue);
    	
    	Log.d(LOG_TAG, "CEV = " + mirerCrossEV + ", CSD = " + mirerCrossSD);
    	Log.d(LOG_TAG, "BEV = " + mirerBackgroundEV + ", BSD = " + mirerBackgroundSD);
    	
    	int[][] mirerBytes = new int[mirerDimension][mirerDimension];
    	
    	Random rand = new Random();
    	double nextValue = 0;
    	
    	// Initialization mirer matrix.
    	for (int row = 0; row < mirerDimension; row++) {
    		for (int column = 0; column < mirerDimension; column++) {    			
    			nextValue = rand.nextGaussian() * mirerBackgroundSD 
    					+ mirerBackgroundEV;
    			mirerBytes[row][column] = (int) Math.round(nextValue);
    			
    			if (mirerBytes[row][column] < 0) {
    				mirerBytes[row][column] = 0;
    			}
    			if (mirerBytes[row][column] > 255) {
    				mirerBytes[row][column] = 255;
    			}
    		}
    	}
    	
    	for (int row = 87; row < 127; row++) {
    		for (int column =  row; column < row + 80; column += 2) {   
    			Log.d(LOG_TAG, "row = " + row + ", column = " + column);
    			nextValue = rand.nextGaussian() * mirerCrossSD 
    					+ mirerCrossEV;
    			mirerBytes[row][column] = (int) Math.round(nextValue);
    			
    			if (mirerBytes[row][column] < 0) {
    				mirerBytes[row][column] = 0;
    			}
    			if (mirerBytes[row][column] > 255) {
    				mirerBytes[row][column] = 255;
    			}
    		}
    	}
    	
    	bytes = new byte[mirerDimension * mirerDimension * 4];
    	int index = 0;
    	for (int row = 0; row < mirerDimension; row++) {
    		for (int column = 0; column < mirerDimension; column++) {	
    			bytes[index] = (byte) mirerBytes[row][column];
    			bytes[index + 1] = (byte) mirerBytes[row][column];
    			bytes[index + 2] = (byte) mirerBytes[row][column];
    			bytes[index + 3] = 0;
    			index += 4;
    		}
    	}
    		
    	ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.rewind();
		Bitmap bitmap = Bitmap.createBitmap(mirerDimension, mirerDimension, Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(buffer);
		mirer.setImageBitmap(bitmap);		
		
		bytes = new byte[mirerDimension * mirerDimension];
    	index = 0;
    	for (int row = 0; row < mirerDimension; row++) {
    		for (int column = 0; column < mirerDimension; column++) {	
    			bytes[index] = (byte) mirerBytes[row][column];
    			index++;
    		}
    	}
    	Log.d(LOG_TAG, "Mirer created");
    }
    
    /**
     * The first quarter of mirer with additional fields.
     */
    private void firstQuarter(int[][] mirerBytes, int dimension) {
    	Random rand = new Random();
    	for (int column = 10; column < dimension / 2; column++) {
    		for (int row = 10; row < dimension / 2; row++) {
    			// the 1st ser.
    			if (row >= 10 && row < 30) {
    				if (column >= 10 && column < 30) {
    					if ((column % 2) == 0) {
    						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    								* mirerFieldsSD + mirerFieldsEV);
    					}
    				}
    			}
    			
    			// the 2nd ser.
    			if (row >= 45 && row < 65) {
    				if (column >= 10 && column < 15) {
    						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column >= 20 && column < 25) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 3rd ser.
    			if (row >= 90 && row < 110) {
    				if (column >= 10 && column < 19) {
						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column >= 28 && column < 37) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    		}
    	}
    }
    
    /**
     * The second quarter of mirer with additional fields.
     */
    private void secondQuarter(int[][] mirerBytes, int dimension) {
    	Random rand = new Random();
    	for (int column = 245; column > dimension / 2; column--) {
    		for (int row = 10; row < dimension / 2; row++) {
    			// the 1st ser.
    			if (row >= 10 && row < 30) {
    				if (column > 243 && column <= 245) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 239 && column <= 241) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 235 && column <= 237) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 231 && column <= 233) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 227 && column <= 229) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 2nd ser.
    			if (row >= 45 && row < 65) {
    				if (column > 239  && column <= 245) {
    						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 227 && column <= 233) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 3rd ser.
    			if (row >= 90 && row < 110) {
    				if (column > 235 && column <= 245) {
						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 215 && column <= 225) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    		}
    	}
    }
    
    /**
     * The third quarter of mirer with additional fields.
     */
    private void thirdQuarter(int[][] mirerBytes, int dimension) {
    	Random rand = new Random();
    	for (int column = 245; column > dimension / 2; column--) {
    		for (int row = 245; row > dimension / 2; row--) {
    			// the 1st ser.
    			if (row <= 245 && row > 225) {
    				if (column > 242 && column <= 245) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 236 && column <= 239) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 230 && column <= 233) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 224 && column <= 227) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 2nd ser.
    			if (row <= 210 && row > 190) {
    				if (column > 238 && column <= 245) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 224 && column <= 231) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 3rd ser.
    			if (row <= 165 && row > 145) {
    				if (column > 234 && column <= 245) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column > 212 && column <= 223) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    		}
    	}
    }
    
    /**
     * The fourth quarter of mirer with additional fields.
     */
    private void fourthQuarter(int[][] mirerBytes, int dimension) {
    	Random rand = new Random();
    	for (int column = 10; column < dimension / 2; column++) {
    		for (int row = 245; row > dimension / 2; row--) {
    			// the 1st ser.
    			if (row <= 245 && row > 225) {
    				if (column >= 10 && column < 14) {
						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column >= 18 && column < 22) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column >= 26 && column < 30) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 2nd ser.
    			if (row <= 210 && row > 190) {
    				if (column >= 10 && column < 18) {
    						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column >= 26 && column < 34) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    			
    			// the 3rd ser.
    			if (row <= 165 && row > 145) {
    				if (column >= 10 && column < 22) {
						mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
								* mirerFieldsSD + mirerFieldsEV);
    				}
    				if (column >= 34 && column < 46) {
    					mirerBytes[row][column] = (int) Math.round(rand.nextGaussian() 
    							* mirerFieldsSD + mirerFieldsEV);
    				}
    			}
    		}
    	}
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1 :
			SimpleFileDialog FileSaveDialog =  new SimpleFileDialog(mContext, "FileSave",
					new SimpleFileDialog.SimpleFileDialogListener()
			{
				@Override
				public void onChosenDir(String chosenDir) 
				{
					// The code in this function will be executed when the dialog OK button is pushed
					mSaveFileName = chosenDir;
					saveMirerInFile();
					Toast.makeText(mContext, "Chosen FileOpenDialog File: " + 
							mSaveFileName, Toast.LENGTH_LONG).show();
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
			out.write(bytes, 0, bytes.length);
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
