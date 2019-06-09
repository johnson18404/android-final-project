package com.example.final_project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context appContext;

    class Person {
        public String name;
        public int sex;
        public double ratio;
        public String url_face;
        public String url_pic;

        Person(String name, int sex, double ratio, String url_face, String url_pic) {
            this.name = name;
            this.sex = sex;
            this.ratio = ratio;
            this.url_face = url_face;
            this.url_pic = url_pic;
        }
    }

    ArrayList<Person> list=new ArrayList<>();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.final_project", appContext.getPackageName());

        String res = UploadImage("2.jpg");

        Log.d("res", res);

        if (res.equals("fail")) {
            Log.d("test", "toast.");
            Log.d("test", "fail");

            return;
        }
        else {
            Log.d("test", "keep going...");
        }

        JSONObject obj = new JSONObject(res);

        int offset = obj.getInt("offset");
        Log.d("offset", String.valueOf(offset));

        JSONArray arr = obj.getJSONArray("list");


        for (int i = 0; i < arr.length(); i++) {
            JSONObject t = arr.getJSONObject(i);

            String name = t.getString("name");
            int sex = t.getInt("sex");
            double ratio = t.getDouble("ratio");
            String url_face = t.getString("url_face");
            String url_pic = t.getString("url_pic");

            list.add(new Person(name, sex, ratio, url_face, url_pic));

            Log.d("name", name);
            Log.d("url_pic", url_pic);
        }
    }


    private void ReArrange(int newoffset) {
        int male = newoffset;
        int female = 6 - newoffset;

        ArrayList<Person> newList = new ArrayList<>();

        for (Person p : list) {
            if (male==0 && female==0) break;

            if (male>0 && p.sex==1) {
                newList.add(p);
                male -= 1;
            }
            else if (female>0 && p.sex==0) {
                newList.add(p);
                female -= 1;
            }

        }


    }

    private String UploadImage(String attachmentFileName) throws Exception {
        String TAG = "uploadTest";

       //  Log.d(TAG, "world.");
       // Log.d(TAG, appContext.getFilesDir().getAbsolutePath());


        String attachmentName = "image";
        // String attachmentFileName = filename;

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
        // Log.d(TAG, response);

        // close response stream & close connection
        responseStream.close();
        conn.disconnect();


//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return response;
    }

    @Test
    public void testJSON() throws Exception {

//
//        JSONObject obj = new JSONObject(res);
//
//        int offset = obj.getInt("offset");
//        Log.d("offset", String.valueOf(offset));
//
//        JSONArray arr = obj.getJSONArray("list");
    }
}
