package gov.cdc.epiinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public class Launcher extends Activity {

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();

		Uri u = intent.getData(); 
		String fileName = "";

		if (u.getScheme().equals("file"))
		{
			fileName = new File(u.getPath()).getName();
			try
			{
				FileInputStream in = new FileInputStream(u.getPath());
				File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/EpiInfo/Questionnaires");
				File outFile = new File(path,"/" + fileName);

				if (!u.getPath().equals(outFile.getPath()))
				{
					FileOutputStream out = new FileOutputStream(outFile);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
				}
			}
			catch (Exception ex)
			{

			}
		}
		else
		{

			fileName = "form.xml";
			Cursor c = getContentResolver().query(u, null, null, null, null);
			c.moveToFirst();
			final int fileNameColumnId = c.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
			if (fileNameColumnId >= 0)
				fileName = c.getString(fileNameColumnId);

			try
			{
				InputStream s = getContentResolver().openInputStream(u);
				File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/EpiInfo/Questionnaires");
				File outFile = new File(path,"/" + fileName);
				FileOutputStream out = new FileOutputStream(outFile);
				copyFile(s, out);
				s.close();
				s = null;
				out.flush();
				out.close();
				out = null;
			}
			catch (Exception ex)
			{

			}
		}

		int idx = fileName.indexOf(".");
		String name = fileName.substring(0, idx);

		Intent main = new Intent(this, MainActivity.class);
		main.putExtra("ViewName", name);		
		startActivity(main);


	}

}
