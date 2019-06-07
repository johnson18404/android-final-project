package com.example.final_project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.final_project", appContext.getPackageName());

        String TAG = "uploadTest";

        Log.d(TAG, "world.");
        Log.d(TAG, appContext.getFilesDir().getAbsolutePath());


        String attachmentName = "image";
        String attachmentFileName = "3.jpg";

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        File FileToUpload = new File(appContext.getFilesDir(), attachmentFileName);

        //try {
            // setup
            HttpURLConnection conn = (HttpURLConnection) new URL("http://imt2019.iamss.cc:5000/lasso").openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // content wrapper
            DataOutputStream request = new DataOutputStream(conn.getOutputStream());
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);

            // write data
            FileInputStream inputStreamToLogFile = new FileInputStream(FileToUpload);
            int bytesRead;
            byte[] dataBuffer = new byte[1024];
            while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
                request.write(dataBuffer, 0, bytesRead);
            }
            request.flush();

            // end content wrapper
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

            // flush output buffer
            request.flush();
            request.close();



            // get response
            InputStream responseStream = new BufferedInputStream(conn.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            Log.d(TAG, response);

            // close response stream & close connection
            responseStream.close();
            conn.disconnect();


//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
