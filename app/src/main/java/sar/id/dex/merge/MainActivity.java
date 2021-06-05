package sar.id.dex.merge;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import android.Manifest;
import android.content.pm.PackageManager;


public class MainActivity extends Activity {
	
	private String dex1 = "";
	private String dex2 = "";
	private String dex3 = "";
	private String mergedDex = "";
	private String output = "";
	private String jarPath = "";
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private TextView textview1;
	private EditText edittext1;
	private EditText edittext2;
	private EditText edittext3;
	private EditText edittext4;
	private Button button1;
	private TextView textview2;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			|| checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			}
			else {
				initializeLogic();
			}
		}
		else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		textview1 = (TextView) findViewById(R.id.textview1);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		edittext2 = (EditText) findViewById(R.id.edittext2);
		edittext3 = (EditText) findViewById(R.id.edittext3);
		edittext4 = (EditText) findViewById(R.id.edittext4);
		button1 = (Button) findViewById(R.id.button1);
		textview2 = (TextView) findViewById(R.id.textview2);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dex1 = edittext1.getText().toString();
				dex2 = edittext2.getText().toString();
				dex3 = edittext3.getText().toString();
				//your dex1 location is output location
				//below code remove the last segment of directory 
				output = dex1.substring(dex1.lastIndexOf("/")+1);
				mergedDex = dex1.replace(output, "merged.dex");
				edittext4.setText(mergedDex);
				//Async task initiater
				new mergeTask().execute("run");
			}
		});
	}
	
	private void initializeLogic() {
		
		
		if (FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/Jar2Dex"))) {
			
		}
		else {
			FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/Jar2Dex"));
			InsightUtil.showMessage(getApplicationContext(), "Directory created...");
		}
		jarPath = FileUtil.getExternalStorageDir().concat("/Jar2Dex/merge.jar");
		if (FileUtil.isExistFile(FileUtil.getExternalStorageDir().concat("/Jar2Dex/merge.jar"))) {
			
		}
		else {
			try{
						copyAssetFile("fonts/merge.jar", jarPath);
						InsightUtil.showMessage(getApplicationContext(), "merge.jar copied successfully!!");
				}catch (java.io.IOException e){
						textview1.setText(e.toString());
				}
			
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	public void _async () {
	}
	
	private class mergeTask extends AsyncTask<String, String, String>
		
		
	    {
		        ProgressDialog pd;
		        @Override
		        protected void onPreExecute()
		        {
			            pd = new ProgressDialog(MainActivity.this);
						pd.setTitle("Please wait");
			            pd.setMessage("merging...");
			            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			            pd.setCancelable(false);
						pd.setIndeterminate(true);
					    
						
			            pd.show();
						
			            
					}
		
		     
				
				
		        @Override
		        protected String doInBackground(String[] p1)
		        {
			            // add code which need to be done in background
			java.io.File jars = new java.io.File(Environment.getExternalStorageDirectory(),"/Jar2Dex/merge.jar");
			
			
			//merge.jar is nothing but dalvik dx library 
			
			List<String> cmd= new ArrayList<String>();
			
			
			    cmd.add("dalvikvm");
			      cmd.add("-Xcompiler-option");
			        cmd.add("--compiler-filter=" + "speed");
			        cmd.add("-Xmx512m");
			cmd.add("-cp");
			cmd.add(jars.toString());
			cmd.add("com.android.dx.merge.DexMerger");
			cmd.add(mergedDex);
			
			if (dex1.equals("")) {
					
			}
			else {
					cmd.add(dex1);
			}
			
			if (dex2.equals("")) {
					
			}
			else {
					cmd.add(dex2);
			}
			if (dex3.equals("")) {
					
			}
			else {
					cmd.add(dex3);
			}
			        
			        
			try{
				
				java.lang.ProcessBuilder pb = new java.lang.ProcessBuilder(cmd); 
						java.lang.Process process = pb.start();
						
						
						//this below code is for writing input process
						
				java.io.BufferedReader stdInput= new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
						String s = null;
				while ((s = stdInput.readLine()) != null) {
							    FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/Jar2Dex/process.txt"), s);
							    
							    } 
			}catch(Exception e){
				
				e.printStackTrace();
				textview1.setText(e.toString());
				
			}
			
			return null;
			            
			            
			
			        
			        }
		        
		
			
				
				
		        @Override
		        protected void onPostExecute(String string2)
		        {
			            super.onPostExecute(string2);
			            
						Toast.makeText(MainActivity.this, "Completed!",
									   Toast.LENGTH_LONG).show();
			            pd.dismiss();
						
			        }
	}
	
	
	public void _copy_assets () {
	}
	
	public void copyAssetFile(String assetFilePath, String destinationFilePath) throws java.io.IOException {
		java.io.InputStream in = getApplicationContext().getAssets().open(assetFilePath);
		java.io.OutputStream out = new java.io.FileOutputStream(destinationFilePath);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
		in.close();
		out.close();
	}
	
	{
	}
	
	
	
}