package com.shiz.noisematrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class NoiseMatrix extends Fragment implements OnClickListener {
	
	private final static String LOG_TAG = "NoiseMatrix";
	
	private Button button;
	private ImageView originalImage;
	private ImageView compressImage;
	private ImageView noiseMatrix;
	
	private Uri originalFileUri;
	private Uri compressFileUri;
	private Context mContext;
	
	public NoiseMatrix(Context ctx) {
		mContext = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.screen_noisematrix, container,
                false);
        
        
        button = (Button) rootView.findViewById(R.id.button1);
        originalImage = (ImageView) rootView.findViewById(R.id.imageView1);
        compressImage = (ImageView) rootView.findViewById(R.id.imageView2);
        noiseMatrix = (ImageView) rootView.findViewById(R.id.imageView3);
        
        button.setOnClickListener(this);
        originalImage.setOnClickListener(this);
        compressImage.setOnClickListener(this);
        noiseMatrix.setOnClickListener(this);

        return rootView;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1 :
			Toast.makeText(mContext, "Режим всевластия включён...", Toast.LENGTH_SHORT).show();
			try {
				makeNoiseNatrix();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case R.id.imageView1 :
			openImage(15);
			break;
		case R.id.imageView2 :
			openImage(25);
			break;
		case R.id.imageView3 :
			break;
		}
	}
	
	private void makeNoiseNatrix() throws IOException {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		String filePath = getRealPathFromURI(mContext, originalFileUri);
		Log.d(LOG_TAG, "filePath = " + filePath);
		Log.d(LOG_TAG, "originalFileUri: " + originalFileUri.toString());
		
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		
		int bytes = bitmap.getByteCount();
		ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

		byte[] originalByteArray = buffer.array();
		Log.d(LOG_TAG, "original image bytes: " + originalByteArray.length);
		
		filePath = getRealPathFromURI(mContext, compressFileUri);
		Log.d(LOG_TAG, "filePath = " + filePath);
		
		bitmap = BitmapFactory.decodeFile(filePath, options);
		
		bytes = bitmap.getByteCount();
		buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

		byte[] compressByteArray = buffer.array();
		Log.d(LOG_TAG, "compress image bytes: " + compressByteArray.length);
		
		byte[] noiseMatrixByteArray = new byte[compressByteArray.length];
		for (int i = 0; i < originalByteArray.length; i++) {
			noiseMatrixByteArray[i] = (byte) Math.abs(originalByteArray[i] - compressByteArray[i]);
		}
//		bitmap = BitmapFactory.decodeByteArray(noiseMatrixByteArray , 0, noiseMatrixByteArray.length);
		buffer = ByteBuffer.allocate(bytes);
		buffer.put(noiseMatrixByteArray);
		buffer.rewind();
		bitmap.copyPixelsFromBuffer(buffer);
		
		File file = new File(Environment.getExternalStorageDirectory() + "/out.png");
		FileOutputStream out = new FileOutputStream(file);
		bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		out.flush();
        out.close();
		noiseMatrix.setImageBitmap(bitmap);
		
		Log.d(LOG_TAG, "end");
		Toast.makeText(mContext, "end", Toast.LENGTH_SHORT).show();
	}
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}


	private void openImage(int code) {
		
		Log.d(LOG_TAG, "code = " + code);
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse(
				 "content://media/internal/images/media")); 
//	    intent.setType("image/*");
	    startActivityForResult(intent, code);
	}
	
	public void onActivityResult(int requestCode, int resultCode, 
			Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == -1) {
		    if (requestCode == 15){ 
		        Uri ChossefileUri = data.getData(); 
		        if(ChossefileUri !=null){ 
		            originalFileUri = ChossefileUri;
		            Log.d(LOG_TAG, "Result: " + originalFileUri.toString());
		            originalImage.setImageURI(originalFileUri);
		        }
		    }
		    if (requestCode == 25) { 
		        Uri ChossefileUri = data.getData(); 
		        if(ChossefileUri !=null){ 
		            compressFileUri = ChossefileUri;
		            Log.d(LOG_TAG, compressFileUri.toString());
		            compressImage.setImageURI(compressFileUri);
		        }
		    }
		}	
	}

}
