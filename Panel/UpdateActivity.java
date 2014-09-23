package panel.dzienny;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import database.entities.ApkData;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends Activity {

    private AlertDialog dialog;
    private String user;
    private String pass;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        File dir = new File(Environment.getExternalStorageDirectory(),
                "/.paneldzienny/samba/");
        if (!dir.exists())
            dir.mkdirs();
        List<ApkData> apkData = ApkData.listAll(ApkData.class);
        user = apkData.get(0).login;
        pass = apkData.get(0).password;
        new GetFormulaSpec().execute();
    }

    private class GetFormulaSpec extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(
                    UpdateActivity.this)
                    .setTitle("Aktualizacja")
                    .setMessage("Trwa aktualizacja. Proszę czekać. Widok ten zostanie automatycznie wyłączony po aktualizacji.")
                    .setCancelable(false).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).formulaspecadres;
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                    "", user, pass);
            SmbFile smbFile;
            try {
                smbFile = new SmbFile(path, auth);
                if (smbFile.exists()) {
                    InputStream inputStream = smbFile.getInputStream();
                    String data1 = String.valueOf(String
                            .format(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/"
                                    + "POL_FormulaSpec.xlsx"));
                    OutputStream out = new FileOutputStream(data1);
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    out.close();
                    inputStream.close();
                    System.out.println("completed ...nice !");
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SmbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            new GetProductDB().execute();
        }
    }

    private class GetProductDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).xlsadres;
           
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                    "", user, pass);
            SmbFile smbFile;
            try {
                smbFile = new SmbFile(path, auth);
                if (smbFile.exists()) {
                    InputStream inputStream = smbFile.getInputStream();
                    String data1 = String.valueOf(String
                            .format(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/Product database.xlsx"));
                    OutputStream out = new FileOutputStream(data1);
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    out.close();
                    inputStream.close();
                    System.out.println("completed ...nice !");
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SmbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            new GetWykresy().execute();
        }
    }

    private class GetWykresy extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).formulaspecadres;

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                    "", user, pass);
            SmbFile smbFile;
            try {
                smbFile = new SmbFile(path, auth);
                if (smbFile.exists()) {
                    InputStream inputStream = smbFile.getInputStream();
                    String data1 = String.valueOf(String
                            .format(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/WYKRESY PRODUKCYJNE.xlsx"));
                    OutputStream out = new FileOutputStream(data1);
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    out.close();
                    inputStream.close();
                    System.out.println("completed ...nice !");
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SmbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            new GetStdDev().execute();
        }
    }

    private class GetStdDev extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            String user = apkData.get(0).login;
            String pass = apkData.get(0).password;
            String path = apkData.get(0).formulaspecadres2;

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                    "", user, pass);
            SmbFile smbFile;
            try {
                smbFile = new SmbFile(path, auth);
                if (smbFile.exists()) {
                    InputStream inputStream = smbFile.getInputStream();
                    String data1 = String.valueOf(String
                            .format(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/Stand deviation.xlsx"));
                    OutputStream out = new FileOutputStream(data1);
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    out.close();
                    inputStream.close();
                    System.out.println("completed ...nice !");
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SmbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            new SendFileFormula().execute();
        }
    }

    private class SendFileFormula extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(client.getParams(), 30000); // Timeout
                                                                      // Limit
            HttpResponse response;
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).serveradres;
            HttpPost post = new HttpPost(path);
            JSONObject json = new JSONObject();

            JSONObject json2 = new JSONObject();
            try {
                // POL_FormulaSpec.xlsx = type 1
                // Product database.xlsx = type 2
                // wykresy.xlsx = type 3
                json2.put("filename", "POL_FormulaSpec.xlsx");
                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/POL_FormulaSpec.xlsx");
                json2.put("content", ExcelToBase64(dir));
                json2.put("type", 1);

                JSONObject json3 = new JSONObject();
                json3.put("data", json2);

                json.put("method", "update.send");
                json.put("id", "0");
                json.put("jsonrpc", "2.0");
                json.put("params", json3);

                // System.out.println(json.toString());
                StringEntity se = new StringEntity(json.toString(), "UTF-8");
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                post.setEntity(se);
                response = client.execute(post);
                if (response != null) {
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity()
                                    .getContent()));
                    String line = "";
                    ArrayList<JSONObject> JSONList = new ArrayList<JSONObject>();

                    while ((line = rd.readLine()) != null) {
                        JSONObject obj = new JSONObject(line);
                        JSONList.add(obj);
                        //System.out.println(line);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        private String ExcelToBase64(File file) throws IOException {
            // System.out.println("ImageToBase64 LOADING!");
            FileInputStream in = null;

            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }

            in.close();
            byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
            return new String(encoded);
        }

        @Override
        protected void onPostExecute(Void result) {
            new SendFileDB().execute();
        }
    }

    private class SendFileDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(client.getParams(), 30000); // Timeout
                                                                      // Limit
            HttpResponse response;
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).serveradres;
            HttpPost post = new HttpPost(path);
            JSONObject json = new JSONObject();

            JSONObject json2 = new JSONObject();
            try {
                // POL_FormulaSpec.xlsx = type 1
                // Product database.xlsx = type 2
                // wykresy.xlsx = type 3
                json2.put("filename", "Product database.xlsx");
                File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/.paneldzienny/samba/Product database.xlsx");
                json2.put("content", ExcelToBase64(dir));
                json2.put("type", 2);

                JSONObject json3 = new JSONObject();
                json3.put("data", json2);

                json.put("method", "update.send");
                json.put("id", "0");
                json.put("jsonrpc", "2.0");
                json.put("params", json3);

                System.out.println(json.toString());
                StringEntity se = new StringEntity(json.toString(), "UTF-8");
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                post.setEntity(se);
                response = client.execute(post);
                if (response != null) {
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity()
                                    .getContent()));
                    String line = "";
                    ArrayList<JSONObject> JSONList = new ArrayList<JSONObject>();

                    while ((line = rd.readLine()) != null) {
                        JSONObject obj = new JSONObject(line);
                        JSONList.add(obj);
                        // System.out.println(line);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        private String ExcelToBase64(File file) throws IOException {
            FileInputStream in = null;

            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }

            in.close();
            byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
            return new String(encoded);
        }

        @Override
        protected void onPostExecute(Void result) {
            new SendFileCharts().execute();
        }
    }

    private class SendFileCharts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(client.getParams(), 30000); // Timeout
                                                                      // Limit
            HttpResponse response;
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).serveradres;
            HttpPost post = new HttpPost(path);
            JSONObject json = new JSONObject();

            JSONObject json2 = new JSONObject();
            try {
                // POL_FormulaSpec.xlsx = type 1
                // Product database.xlsx = type 2
                // wykresy.xlsx = type 3
                json2.put("filename", "WYKRESY PRODUKCYJNE.xlsx");
                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/WYKRESY PRODUKCYJNE.xlsx");
                json2.put("content", ExcelToBase64(dir));
                json2.put("type", 3);

                JSONObject json3 = new JSONObject();
                json3.put("data", json2);

                json.put("method", "update.send");
                json.put("id", "0");
                json.put("jsonrpc", "2.0");
                json.put("params", json3);

                System.out.println(json.toString());
                StringEntity se = new StringEntity(json.toString(), "UTF-8");
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                post.setEntity(se);
                response = client.execute(post);
                if (response != null) {
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity()
                                    .getContent()));
                    String line = "";
                    ArrayList<JSONObject> JSONList = new ArrayList<JSONObject>();

                    while ((line = rd.readLine()) != null) {
                        JSONObject obj = new JSONObject(line);
                        JSONList.add(obj);
                        // System.out.println(line);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        private String ExcelToBase64(File file) throws IOException {
            // System.out.println("ImageToBase64 LOADING!");
            FileInputStream in = null;

            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }

            in.close();
            byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
            return new String(encoded);
        }

        @Override
        protected void onPostExecute(Void result) {
            new SendFileDEV().execute();
        }
    }

    private class SendFileDEV extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(client.getParams(), 30000); // Timeout
                                                                      // Limit
            HttpResponse response;
            List<ApkData> apkData = ApkData.listAll(ApkData.class);
            path = apkData.get(0).serveradres;
            HttpPost post = new HttpPost(path);
            JSONObject json = new JSONObject();

            JSONObject json2 = new JSONObject();
            try {
                // POL_FormulaSpec.xlsx = type 1
                // Product database.xlsx = type 2
                // wykresy.xlsx = type 3
                json2.put("filename", "Stand deviation.xlsx");
                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/.paneldzienny/samba/Stand deviation.xlsx");
                json2.put("content", ExcelToBase64(dir));
                json2.put("type", 4);

                JSONObject json3 = new JSONObject();
                json3.put("data", json2);

                json.put("method", "update.send");
                json.put("id", "0");
                json.put("jsonrpc", "2.0");
                json.put("params", json3);

                System.out.println(json.toString());
                StringEntity se = new StringEntity(json.toString(), "UTF-8");
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                post.setEntity(se);
                response = client.execute(post);
                if (response != null) {
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity()
                                    .getContent()));
                    String line = "";
                    ArrayList<JSONObject> JSONList = new ArrayList<JSONObject>();

                    while ((line = rd.readLine()) != null) {
                        JSONObject obj = new JSONObject(line);
                        JSONList.add(obj);
                        // System.out.println(line);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        private String ExcelToBase64(File file) throws IOException {
            // System.out.println("ImageToBase64 LOADING!");
            FileInputStream in = null;

            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }

            in.close();
            byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
            return new String(encoded);
        }

        @Override
        protected void onPostExecute(Void result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    finish();
                }
            });
        }
    }
}
