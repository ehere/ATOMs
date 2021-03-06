package com.example.myapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;

public class FileManager
{
	private String filename;
	private Context context;

	public FileManager(String filename, Context context)
	{
		this.filename = filename;
		this.context = context;
	}
	public boolean isexists()
	{
		File file = this.context.getFileStreamPath(this.filename);
		if(file.exists()) 
		{
			return true;
		}
		return false;
	}
	public void write(String data) 
	{
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.context.openFileOutput(filename, Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
		
	}
	public String read() {

	    String ret = "";

	    try {
	        InputStream inputStream = context.openFileInput(this.filename);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
	public boolean delete()
	{
		File dir = context.getFilesDir();
		File file = new File(dir, this.filename);
		boolean deleted = file.delete();
		return deleted;
	}
}
