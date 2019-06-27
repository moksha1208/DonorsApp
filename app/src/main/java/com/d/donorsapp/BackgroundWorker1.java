package com.d.donorsapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker1 extends AsyncTask<String, Void, String> {
    Context context;

    BackgroundWorker1(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String type = strings[0];
        String checkphonenumber_url = "http://10.0.0.146/checkphonenumber.php";
        String adddonordetails_url = "http://10.0.0.146/adddonordetails.php";
        String sendmessage_url = "http://10.0.0.146/sendmessage.php";
        String outtracking_url = "http://10.0.0.146/outtracking.php";
        if (type.equals("checkphonenumber")) {
            try {
                String phoneno = strings[1];
                URL url = new URL(checkphonenumber_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setInstanceFollowRedirects(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phoneno", "UTF-8") + "=" + URLEncoder.encode(phoneno, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line = bufferedReader.readLine();
                System.out.println("Buffered"+line);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println("Line "+line);
                return line;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("adddonordetails")) {
            try {
                String donor_phone_no = strings[1];
                String donor_name = strings[2];
                String donor_address = strings[3];
                String donor_password = strings[4];
                String donor_latitude = strings[5];
                String donor_longitude = strings[6];
                URL url = new URL(adddonordetails_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setInstanceFollowRedirects(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("donor_phone_no", "UTF-8") + "=" + URLEncoder.encode(donor_phone_no, "UTF-8") + "&" +
                        URLEncoder.encode("donor_name", "UTF-8") + "=" + URLEncoder.encode(donor_name, "UTF-8") + "&" +
                            URLEncoder.encode("donor_address", "UTF-8") + "=" + URLEncoder.encode(donor_address, "UTF-8") + "&" +
                            URLEncoder.encode("donor_password", "UTF-8") + "=" + URLEncoder.encode(donor_password, "UTF-8") + "&" +
                            URLEncoder.encode("donor_latitude", "UTF-8") + "=" + URLEncoder.encode(donor_latitude, "UTF-8") + "&" +
                            URLEncoder.encode("donor_longitude", "UTF-8") + "=" + URLEncoder.encode(donor_longitude, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Donor Added";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("sendmessage")) {
            try {
                URL url = new URL(sendmessage_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setInstanceFollowRedirects(true);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line = bufferedReader.readLine();
                System.out.println("Buffered"+line);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println("Line "+line);
                return line;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("outtracking")) {
            try {
                System.out.println("in bggg");
                String donor_phone_no = strings[1];
                String ngo_name = strings[2];
                URL url = new URL(outtracking_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setInstanceFollowRedirects(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("donor_phone_no", "UTF-8") + "=" + URLEncoder.encode(donor_phone_no, "UTF-8") + "&" +
                        URLEncoder.encode("ngo_name", "UTF-8") + "=" + URLEncoder.encode(ngo_name, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String line = bufferedReader.readLine();
                System.out.println("Buffered"+line);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println("Line "+line);
                return line;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
