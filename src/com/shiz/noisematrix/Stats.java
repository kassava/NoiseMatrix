package com.shiz.noisematrix;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.shiz.noisematrix.resolution.LinearResolution;
import com.shiz.noisematrix.resolution.RadiometricResolution;

/**
 * Class calculates radiometric and linear resolutions for different mirers.
 * @author ultra
 *
 */
public class Stats extends Fragment {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ImageView mirer1;
    private ImageView mirer2;
    private TextView textView;
    private TableLayout tableLayout;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private final static String LOG_TAG = "stats";
    private Context mContext;
    private String mMirorFileName;
//    private final int dimension = 256;
    private int stat = 0; // 1 - r4, 2 - r10, 3 - l4, 4 - l6, 5 - l5

    public Stats(Context context) {
    	mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.screen_stat, container, false);

        return rootView;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setOffscreenPageLimit(0);
//        mViewPager.setAdapter(new MyAdapter(getFragmentManager()));

        // Give the SlidingTabLayout the ViewPager, this must be 
        // done AFTER the ViewPager has had it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
    
    public void openImage() {
		SimpleFileDialog FileSaveDialog =  new SimpleFileDialog(mContext, "FileOpen",
				new SimpleFileDialog.SimpleFileDialogListener()
		{
			@Override
			public void onChosenDir(String chosenDir) 
			{
				// The code in this function will be executed when the dialog OK button is pushed
				mMirorFileName = chosenDir;
				String fileExtension = getFileNameExtension(chosenDir);
				switch (fileExtension) {
				case "jpg":
					Toast.makeText(mContext, "Chosen FileOpenDialog File: " + 
							mMirorFileName, Toast.LENGTH_SHORT).show();
					openImageMirerFile(chosenDir);
					break;
				case "raw":
					Toast.makeText(mContext, "Chosen FileOpenDialog File: " + 
							mMirorFileName, Toast.LENGTH_SHORT).show();
					openRAWMirerFile(chosenDir);
					break;
				case "png":
					Toast.makeText(mContext, "Chosen FileOpenDialog File: " + 
							mMirorFileName, Toast.LENGTH_SHORT).show();
					openImageMirerFile(chosenDir);
					break;
				default:
					Toast.makeText(mContext, "Chosen FileOpenDialog File: " + 
							mMirorFileName + " this not right!", Toast.LENGTH_SHORT).show();
					break;	
				}
			}
		});
		FileSaveDialog.Default_File_Name = "default.raw";
		FileSaveDialog.chooseFile_or_Dir();
	}
    
	private String getFileNameExtension(String filePath) {
	    String filenameArray[] = filePath.split("\\.");
	    String extension = filenameArray[filenameArray.length-1];
	    return extension;
	}
	
	/**
	 * Open supported image formats (now png, jpg).
	 * @param filePath
	 */
	private void openImageMirerFile(String filePath) {
		Log.d(LOG_TAG, "filePath: " + filePath);
		File imgFile = new  File(filePath);
		Bitmap mirerBitmap = null;
		if(imgFile.exists()){
		    mirerBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    Log.d(LOG_TAG, "open image stat = " + stat);
		    switch (stat) {
		    case 1:
		    	mirer1.setImageBitmap(mirerBitmap);
		    	break;
		    case 2:
		    	mirer1.setImageBitmap(mirerBitmap);
		    	break;
		    case 3:
		    	mirer2.setImageBitmap(mirerBitmap);
		    	break;
		    case 4:
		    	mirer2.setImageBitmap(mirerBitmap);
		    	break;
		    case 5:
		    	mirer2.setImageBitmap(mirerBitmap);
		    	break;
		    default:
		    	Log.d(LOG_TAG, "openImageMirerFile default case");
		    	break;
		    }
		}
		
		int bytes = mirerBitmap.getByteCount();
		ByteBuffer buffer = ByteBuffer.allocate(bytes);
		mirerBitmap.copyPixelsToBuffer(buffer);
		byte[] originalByteArray = buffer.array();
		
//		Log.d(LOG_TAG, "original image bytes: " + originalByteArray.length);
		byte[] rawBytes = new byte[(originalByteArray.length) /4];
		int i = 0;
		for (int idx = 0; idx < originalByteArray.length / 4; idx++) {
			rawBytes[idx] = originalByteArray[i];
			i += 4;
		}
		
		int fileSize = (int) imgFile.length();
		
		Log.d(LOG_TAG, "imgFile.length = " + fileSize);
		
		int dimension = (int) Math.sqrt(fileSize);
		int[][] byteArray = new int[dimension][dimension];
		int idx = 0;
		for (i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				byteArray[i][j] =  0xFF & (int)rawBytes[idx];
				idx++;
			}
		}
		switch (stat) {
		case 1:
			Log.d(LOG_TAG, "1 stat = " + stat);
			radiometricResolutionFor6Circles(byteArray);
			break;
		case 2:
			Log.d(LOG_TAG, "2 stat = " + stat);
			radiometricResolutionFor10Circles(byteArray);
			break;
		case 3:
			Log.d(LOG_TAG, "3 stat = " + stat);
			linearResolutionFor4Circles(byteArray);
			break;
		case 4:
			Log.d(LOG_TAG, "4 stat = " + stat);
			linearResolutionFor6Circles(byteArray);
			break;
		case 5:
			Log.d(LOG_TAG, "5 stat = " + stat);
			linearResolutionFor10Circles(byteArray);
			break;
		default:
			Log.d(LOG_TAG, "Switch stat default case.");
			break;
		}
	}
	
	/**
	 * Read data from *.raw file with something features.
	 * @param filePath
	 */
	private void openRAWMirerFile(String filePath) {
		File mirerFile = new File(filePath);
		
		int fileSize = (int) mirerFile.length();
		fileSize /= 3; // in Raw file data recorded three times

	    byte[] mirerBytes = new byte[fileSize];
	    try {
	        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(mirerFile));
	        buf.read(mirerBytes, 0, mirerBytes.length);
	        buf.close();
	    } catch (FileNotFoundException e) {
	        Log.d(LOG_TAG, "FileNotFoundException");
	        e.printStackTrace();
	    } catch (IOException e) {
	    	Log.d(LOG_TAG, "IOException");
	        e.printStackTrace();
	    }
	    
	    byte[] bytes = new byte[(int) (fileSize * 4)];
	    int i = 0;
    	for (int index = 0; index < fileSize; index++) {	
    			bytes[i] = (byte) mirerBytes[index];
    			bytes[i + 1] = (byte) mirerBytes[index];
    			bytes[i + 2] = (byte) mirerBytes[index];
    			bytes[i + 3] = 0; 
    			i += 4;
    	}
    		
    	ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		buffer.put(bytes);
		buffer.rewind();
		Bitmap bitmap = Bitmap.createBitmap((int)Math.abs(Math.sqrt(fileSize)), 
				(int)Math.abs(Math.sqrt(fileSize)), Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(buffer);
		switch (stat) {
	    case 1:
	    	mirer1.setImageBitmap(bitmap);
	    	break;
	    case 2:
	    	mirer1.setImageBitmap(bitmap);
	    	break;
	    case 3:
	    	mirer2.setImageBitmap(bitmap);
	    	break;
	    case 4:
	    	mirer2.setImageBitmap(bitmap);
	    	break;
	    case 5:
	    	mirer2.setImageBitmap(bitmap);
	    	break;
	    default:
	    	Log.d(LOG_TAG, "openImageMirerFile default case");
	    	break;
	    }			
    	
    	int dimension = (int) Math.sqrt(fileSize);
    	int[][] statData = new int[dimension][dimension];
    	int index = 0;
    	for (int row = 0; row < dimension; row++) {
    		for (int column = 0; column < dimension; column++) {
    			statData[row][column] = 0xFF & (int)mirerBytes[index];
    			index++;
    		}
    	}
    	
    	switch (stat) {
		case 1:
			Log.d(LOG_TAG, "raw 1 stat = " + stat);
			radiometricResolutionFor6Circles(statData);
			break;
		case 2:
			Log.d(LOG_TAG, "raw 2 stat = " + stat);
			radiometricResolutionFor10Circles(statData);
			break;
		case 3:
			Log.d(LOG_TAG, "raw 3 stat = " + stat);
			linearResolutionFor4Circles(statData);
			break;
		case 4:
			Log.d(LOG_TAG, "raw 4 stat = " + stat);
			linearResolutionFor6Circles(statData);
			break;
		case 5:
			Log.d(LOG_TAG, "raw 5 stat = " + stat);
			linearResolutionFor10Circles(statData);
			break;
		default:
			Log.d(LOG_TAG, "Switch stat default case.");
			break;
		}
	}
	
	private void radiometricResolutionFor6Circles(int[][] byteArray) {		
		double radiometricResolution = RadiometricResolution.radiometricResolutionFor6Circles(
				byteArray);
		textView.setText(String.format("%1$,1.3f", radiometricResolution));
		
		DataWriter.writeData(new File(mMirorFileName).getName(), String.format("%1$,1.3f", radiometricResolution),
				String.format("%1$,1.3f", radiometricResolution));
	}
	
	private void radiometricResolutionFor10Circles(int[][] byteArray) {		
		double radiometricResolution = RadiometricResolution.radiometricResolutionFor10Circles(
				byteArray);
		textView.setText(String.format("%1$,1.3f", radiometricResolution));
	}
	
	private void linearResolutionFor4Circles(int[][] byteArray) {		
		double[] Kcp = LinearResolution.linearResolutionFor4Circles(byteArray);
		
		int VALUES_ROWS = 2;
		int VALUES_COLUMNS = 2;
		tableLayout.removeAllViews();

		for (int i = 0; i < VALUES_ROWS; i++) {
			TableRow tableRow = new TableRow(mContext);
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 
					TableRow.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL;
			tableRow.setLayoutParams(params);
			for (int j = 0; j < VALUES_COLUMNS; j++) {
				TextView textView = new TextView(mContext);
				textView.setText("m" + (i * VALUES_COLUMNS + j + 1) + "= " 
						+ String.format("%1$,1.3f", Kcp[i * VALUES_COLUMNS  + j]));
				textView.setPadding(10, 10, 10, 10);
				tableRow.addView(textView, j);
			}	
			tableLayout.addView(tableRow, i);
		}
	}
	
	private void linearResolutionFor6Circles(int[][] byteArray) {	
		
		Log.d(LOG_TAG, "linearResolutionFor6Circles");
		
		double[] Kcp = LinearResolution.linearResolutionFor6Circles(byteArray);
		int VALUES_ROWS = 2;
		int VALUES_COLUMNS = 3;
		tableLayout.removeAllViews();

		for (int i = 0; i < VALUES_ROWS; i++) {

			TableRow tableRow = new TableRow(mContext);
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 
					TableRow.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL;
			tableRow.setLayoutParams(params);
			for (int j = 0; j < VALUES_COLUMNS; j++) {
				TextView textView = new TextView(mContext);
				textView.setText("m" + (i * VALUES_COLUMNS + j + 1) + "= " 
						+ String.format("%1$,1.3f", Kcp[i * VALUES_COLUMNS  + j]));
				textView.setPadding(10, 10, 10, 10);
				tableRow.addView(textView, j);
			}
			
			tableLayout.addView(tableRow, i);
		}
		
		String dataToFile = "";
		for (int idx = 0; idx < Kcp.length; idx++) {
			dataToFile += (String.format("%1$,1.3f", Kcp[idx]) + " ");
		}
		
		DataWriter.writeData(new File(mMirorFileName).getName(), dataToFile, dataToFile);
	}
	
	private void linearResolutionFor10Circles(int[][] byteArray) {		
		double[] Kcp = LinearResolution.linearResolutionFor10Circles(byteArray);
		int VALUES_ROWS = 2;
		int VALUES_COLUMNS = 5;
		tableLayout.removeAllViews();

		for (int i = 0; i < VALUES_ROWS; i++) {

			TableRow tableRow = new TableRow(mContext);
			TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 
					TableRow.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL;
			tableRow.setLayoutParams(params);
			for (int j = 0; j < VALUES_COLUMNS; j++) {
				TextView textView = new TextView(mContext);
				textView.setText("m" + (i * VALUES_COLUMNS + j + 1) + "= " 
						+ String.format("%1$,1.3f", Kcp[i * VALUES_COLUMNS  + j]));
				textView.setPadding(10, 10, 10, 10);
				tableRow.addView(textView, j);
			}
			
			tableLayout.addView(tableRow, i);
		}
	}
		
	public static class ArrayListFragment extends ListFragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = null;
        	if (mNum == 0) {
        		Log.d(LOG_TAG, "nNum = 0");
            	v = inflater.inflate(R.layout.stat1, container, false);
            }
        	if (mNum == 1) {
        		Log.d(LOG_TAG, "nNum = 1");
        		v = inflater.inflate(R.layout.stat2, container, false);
        	}
        	if (mNum == 2) {
        		Log.d(LOG_TAG, "nNum = 2");
        		v = inflater.inflate(R.layout.default_pager_item, container, false);
        	}
            
            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, R.array.screen_array));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }
    
	// Adapter
    class SamplePagerAdapter extends PagerAdapter implements OnClickListener, OnCheckedChangeListener {
    	int pagesCount = 2;
        /**
         * Return the number of pages to display
         */
        @Override
        public int getCount() {
            return pagesCount;
        }

        /**
         * Return true if the value returned from is the same object as the View
         * added to the ViewPager.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at position. This is important as what
         * this method returns is what is displayed in the SlidingTabLayout.
         */
        @Override
        public CharSequence getPageTitle(int position) {
        	String pageTitle = "title";
        	switch (position) {
        	case 0:
        		pageTitle = "stat 1";
        		break;
        	case 1:
        		pageTitle = "stat 2";
        		break;
        	case 2:
        		pageTitle = "stat 3";
        		break;
        	default:
        		Log.d(LOG_TAG, "getPageTitle default case");
        		break;
        	}
            return pageTitle;
        }

        /**
         * Instantiate the View which should be displayed at position. Here we
         * inflate a layout from the apps resources and then change the text
         * view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = null;
            Log.d(LOG_TAG, "position = " + position);
            switch (position) {
            case 0:
            	Log.d(LOG_TAG, "stat1");
            	view = getActivity().getLayoutInflater().inflate(R.layout.stat1,
                        container, false);
            	container.addView(view);
            	stat1(view);
            	break;
            case 1:
            	Log.d(LOG_TAG, "stat2");
            	view = getActivity().getLayoutInflater().inflate(R.layout.stat2,
            			container, false);
            	container.addView(view);
            	stat2(view);
            	break;
            case 2:
            	Log.d(LOG_TAG, "stat3");
            	view = getActivity().getLayoutInflater().inflate(R.layout.default_pager_item,
                        container, false);
                // Add the newly created View to the ViewPager
                container.addView(view);
//                stat3(view);
            	break;
            default:
            	Log.d(LOG_TAG, "instantiateItem deafult break");
            	break;
            }
            
            // Return the View
            if (view == null) {
            	view = getActivity().getLayoutInflater().inflate(R.layout.default_pager_item,
                        container, false);
            	container.addView(view);
            	TextView title = (TextView) view.findViewById(R.id.item_title);
                title.setText(String.valueOf(-1));
            }
            return view;
        }

        private void stat1(View view) {
			mirer1 = (ImageView) view.findViewById(R.id.imageView1);
			textView = (TextView) view.findViewById(R.id.textView1);
			mirer1.setOnClickListener(this);
			radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
        	radioGroup1.setOnCheckedChangeListener(this);
        	radioGroup1.clearCheck();
		}
        
        private void stat2(View view) {
        	mirer2 = (ImageView) view.findViewById(R.id.imageView2);
        	tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        	mirer2.setOnClickListener(this);
        	radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);
        	radioGroup2.setOnCheckedChangeListener(this);
        	radioGroup2.clearCheck();
        }
        
//        private void stat3(View view) {
//        	mirer3 = (ImageView) view.findViewById(R.id.imageView3);
//        	textView = (TextView) view.findViewById(R.id.textView1);
//        	tableLayout2 = (TableLayout) view.findViewById(R.id.tableLayout2);
//        	mirer3.setOnClickListener(this);
//        	radioGroup3 = (RadioGroup) view.findViewById(R.id.radioGroup3);
//        	radioGroup3.setOnCheckedChangeListener(this);
//        	radioGroup3.clearCheck();
//        }
        
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView1:			
	        	Log.d(LOG_TAG, "imageView1 stat = " + stat);
				openImage();
				break;
			case R.id.imageView2:	        	
	        	Log.d(LOG_TAG, "imageView2 stat = " + stat);
				openImage();
				break;
			case R.id.imageView3:
				Log.d(LOG_TAG, "imageView3 stat = " + stat);
//				open3RawFile();
			default:
				Log.d(LOG_TAG, "onClick default case");
				break;
			}
		}

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio1:
				Log.d(LOG_TAG, "radio1");
				stat = 1;
				break;
			case R.id.radio2:
				Log.d(LOG_TAG, "radio2");
				stat = 2;
				break;
			case R.id.radio3:
				Log.d(LOG_TAG, "radio3");
				stat = 3;
				break;
			case R.id.radio4:
				Log.d(LOG_TAG, "radio4");
				stat = 4;
				break;
			case R.id.radio5:
				Log.d(LOG_TAG, "radio5");
				stat = 5;
				break;
			default:
				stat = 0;
				Log.d(LOG_TAG, "onChangeCheck default case");
				break;
			}
		}
		
		/**
         * Destroy the item from the ViewPager. In our case this is simply
         * removing the View.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }	
}
