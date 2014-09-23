package panel.dzienny;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import database.entities.ApkData;
import database.entities.RaportProduct;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import panel.dzienny.entities.ChartData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends Activity {

    private static final String MyPREFERENCES = "session";
    public static final String productListSize = "productListSizeKey";
    private static final String panelID = "panelIdKey";
    private SharedPreferences sharedpreferences;

    private GraphViewData[] data;
    private GraphViewData[] minData;
    private GraphViewData[] maxData;
    private GraphViewData[] targetData;
    private boolean CheckedH2O;
    private boolean CheckedAW;
    private boolean CheckedMG;
    private boolean CheckedDensity;
    private boolean CheckedRehydra;
    private boolean CheckedD1;
    private boolean CheckedD2;
    private boolean CheckedCL;
    private int productNum;
    private String productName;
    private String formulaCode;
    private int amount;
    private int current;
    private Button btn_prev;
    private Button btn_next;
    private final ArrayList<String> fullList = new ArrayList<String>();
    private final ArrayList<ChartData> chartDataList = new ArrayList<ChartData>();
    private CheckBox checkresult;
    private CheckBox checktarget;
    private CheckBox checkmin;
    private CheckBox checkmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charts);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            CheckedH2O = extras.getBoolean("H2O");
            CheckedAW = extras.getBoolean("AW");
            CheckedMG = extras.getBoolean("MG");
            CheckedDensity = extras.getBoolean("DEN");
            CheckedRehydra = extras.getBoolean("REHYDRA");
            CheckedD1 = extras.getBoolean("D1");
            CheckedD2 = extras.getBoolean("D2");
            CheckedCL = extras.getBoolean("CL");
            productNum = extras.getInt("PRODUCTNUM", 0);
            productName = extras.getString("PRODUCTNAME", "");
            formulaCode = extras.getString("FORMULACODE", "");
        }
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        amount = 0;
        current = 0;
        if (CheckedH2O) {
            fullList.add("H2O");
            amount += 1;
        }
        if (CheckedAW) {
            fullList.add("AW");
            amount += 1;
        }
        if (CheckedMG) {
            fullList.add("MG");
            amount += 1;
        }
        if (CheckedDensity) {
            fullList.add("DENSITY");
            amount += 1;
        }
        if (CheckedRehydra) {
            fullList.add("REHYDRATATION");
            amount += 1;
        }
        if (CheckedD1) {
            fullList.add("D1");
            amount += 1;
        }
        if (CheckedD2) {
            fullList.add("D2");
            amount += 1;
        }
        if (CheckedCL) {
            fullList.add("CL");
            amount += 1;
        }

        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_prev.setVisibility(View.INVISIBLE);
        checkresult = (CheckBox)findViewById(R.id.checkbox_result);
        checktarget = (CheckBox)findViewById(R.id.checkbox_target);
        checkmin = (CheckBox)findViewById(R.id.checkbox_min);
        checkmax = (CheckBox)findViewById(R.id.checkbox_max);

        checkresult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CreateChart();
            }
        });
        checktarget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CreateChart();
            }
        });
        checkmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CreateChart();
            }
        });
        checkmax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CreateChart();
            }
        });

        if (amount == 0) {
            finish();
            return;
        }
        if (amount > 1) {
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setText(fullList.get(1));
        } else
            btn_next.setVisibility(View.INVISIBLE);

        Typeface robotoBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto/Bold.ttf");

        btn_next.setTypeface(robotoBold);
        btn_next.getBackground().setColorFilter(0xFF111315,
                PorterDuff.Mode.SRC);
        btn_prev.setTypeface(robotoBold);
        btn_prev.getBackground().setColorFilter(0xFF1e2124,
                PorterDuff.Mode.SRC);
        new GetProductDetails().execute();
    }

    private class GetProductDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(),
                        10000); // Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                String adres = "";
                List<ApkData> apkData = ApkData.listAll(ApkData.class);
                adres = "http://" + apkData.get(0).serveradres;
                HttpPost post = new HttpPost(adres);
                json.put("method", "chart.getDataChartsForProduct");
                json.put("id", "0");
                json.put("jsonrpc", "2.0");

                List<RaportProduct> RaportProductList = RaportProduct.find(
                        RaportProduct.class, "panelid = ? AND mainid = ?", ""
                                + sharedpreferences.getLong(panelID, 0), ""
                                + productNum);

                JSONObject object = new JSONObject();
                if (!RaportProductList.isEmpty())
                    object.put("code", "" + RaportProductList.get(0).code);
                else
                    object.put("code", "" + 0);
                JSONObject json2 = new JSONObject();
                json2.put("data", object);
                json.put("params", json2);

                System.out.println(json);

                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                post.setEntity(se);
                response = client.execute(post);

                /* Checking response */
                if (response != null) {
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity()
                                    .getContent()));
                    String line = "";
                    ArrayList<JSONObject> JSONList = new ArrayList<JSONObject>();
                    while ((line = rd.readLine()) != null) {
                        JSONObject obj = new JSONObject(line);
                        JSONList.add(obj);
                    }

                    JSONArray myJSONArray = JSONList.get(0).getJSONArray(
                            "result");

                    for (int x = 0; x < myJSONArray.length(); x++) {
                        Integer id = (Integer) myJSONArray.getJSONObject(x)
                                .get("id");
                        String H20_AC = (String) myJSONArray.getJSONObject(x)
                                .get("H20_AC").toString();
                        String H20_AC_LCL = myJSONArray.getJSONObject(x)
                                .get("H20_AC_LC").toString();
                        String H20_AC_LCU = myJSONArray.getJSONObject(x)
                                .get("H20_AC_LCUL").toString();
                        String AW_AC = myJSONArray.getJSONObject(x)
                                .get("AW_AC").toString();
                        String AW_AC_LCL = myJSONArray.getJSONObject(x)
                                .get("AW_AC_LCL").toString();
                        String AW_AC_LCU = myJSONArray.getJSONObject(x)
                                .get("AW_AC_LCU").toString();
                        String MG_AC = myJSONArray.getJSONObject(x)
                                .get("MG_AC").toString();
                        String MG_AC_LCL = myJSONArray.getJSONObject(x)
                                .get("MG_AC_LCL").toString();
                        String MG_AC_LCU = myJSONArray.getJSONObject(x)
                                .get("MG_AC_LCU").toString();
                        String D1 = myJSONArray.getJSONObject(x).get("D1")
                                .toString();
                        String D1_LCL = myJSONArray.getJSONObject(x)
                                .get("D1_LCL").toString();
                        String D1_LCU = myJSONArray.getJSONObject(x)
                                .get("D1_LCU").toString();
                        String D2 = myJSONArray.getJSONObject(x).get("D2")
                                .toString();
                        String D2_LCL = myJSONArray.getJSONObject(x)
                                .get("D2_LCL").toString();
                        String D2_LCU = myJSONArray.getJSONObject(x)
                                .get("D2_LCU").toString();
                        String CL = myJSONArray.getJSONObject(x).get("CL")
                                .toString();
                        String CL_LCL = myJSONArray.getJSONObject(x)
                                .get("CL_LCL").toString();
                        String CL_LCU = myJSONArray.getJSONObject(x)
                                .get("CL_LCU").toString();
                        String Density_AC = myJSONArray.getJSONObject(x)
                                .get("Density_AC").toString();
                        String Density_AC_LCL = myJSONArray.getJSONObject(x)
                                .get("Density_AC_LCL").toString();
                        String Density_AC_LCU = myJSONArray.getJSONObject(x)
                                .get("Density_AC_LCU").toString();
                        String REHYDRATATION = myJSONArray.getJSONObject(x)
                                .get("REHYDRATATION").toString();
                        String REHYDRATATION_LCL = myJSONArray.getJSONObject(x)
                                .get("REHYDRATATION_LCL").toString();
                        String REHYDRATATION_LCU = myJSONArray.getJSONObject(x)
                                .get("REHYDRATATION_LCU").toString();
                        String DATE = myJSONArray.getJSONObject(x)
                                .getJSONObject("date_filter").get("date")
                                .toString();
                        ChartData data = new ChartData(id, H20_AC, H20_AC_LCL,
                                H20_AC_LCU, AW_AC, AW_AC_LCL, AW_AC_LCU, MG_AC,
                                MG_AC_LCL, MG_AC_LCU, D1, D1_LCL, D1_LCU, D2,
                                D2_LCL, D2_LCU, CL, CL_LCL, CL_LCU, Density_AC,
                                Density_AC_LCL, Density_AC_LCU, REHYDRATATION,
                                REHYDRATATION_LCL, REHYDRATATION_LCU,
                                DATE.substring(2, 10));
                        chartDataList.add(data);
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

        @Override
        protected void onPostExecute(Void result) {
            CreateChart();
        }
    }

    void CreateChart() {
        if (chartDataList.size() == 0)
            return;

        if (amount >= current) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
            layout.removeAllViews();
            // Creat Chart for fullList.get(current)
            data = new GraphViewData[chartDataList.size()];
            minData = new GraphViewData[chartDataList.size()];
            maxData = new GraphViewData[chartDataList.size()];
            targetData = new GraphViewData[chartDataList.size()];
            final String[] array = new String[chartDataList.size()];

            int index = 0;
            float min = 0;
            float max = 0;

            if (!chartDataList.isEmpty()) {
                if (fullList.get(current).equals("H2O")) {
                    min = Float.parseFloat(chartDataList.get(0).getH2OMin());
                    max = Float.parseFloat(chartDataList.get(0).getH2OMax());
                } else if (fullList.get(current).equals("AW")) {
                    min = Float.parseFloat(chartDataList.get(0).getAWMin());
                    max = Float.parseFloat(chartDataList.get(0).getAWMax());
                } else if (fullList.get(current).equals("MG")) {
                    min = Float.parseFloat(chartDataList.get(0).getMGMin());
                    max = Float.parseFloat(chartDataList.get(0).getMGMax());
                } else if (fullList.get(current).equals("DENSITY")) {
                    min = Float.parseFloat(chartDataList.get(0).getDensityMin());
                    max = Float.parseFloat(chartDataList.get(0).getDensityMax());
                } else if (fullList.get(current).equals("REHYDRATATION")) {
                    min = Float.parseFloat(chartDataList.get(0).getRehydraMin());
                    max = Float.parseFloat(chartDataList.get(0).getRehydraMax());
                } else if (fullList.get(current).equals("D1")) {
                    min = Float.parseFloat(chartDataList.get(0).getD1Min());
                    max = Float.parseFloat(chartDataList.get(0).getD1Max());
                } else if (fullList.get(current).equals("D2")) {
                    min = Float.parseFloat(chartDataList.get(0).getD2Min());
                    max = Float.parseFloat(chartDataList.get(0).getD2Max());
                } else if (fullList.get(current).equals("CL")) {
                    min = Float.parseFloat(chartDataList.get(0).getCLMin());
                    max = Float.parseFloat(chartDataList.get(0).getCLMax());
                }
            }

            float min2 = min;
            float max2 = max;
            for (ChartData chartData : chartDataList) {
                float value = 0;
                if (fullList.get(current).equals("H2O"))
                    value = Float.parseFloat(chartData.getH2O());
                else if (fullList.get(current).equals("AW"))
                    value = Float.parseFloat(chartData.getAW());
                else if (fullList.get(current).equals("MG"))
                    value = Float.parseFloat(chartData.getMG());
                else if (fullList.get(current).equals("DENSITY"))
                    value = Float.parseFloat(chartData.getDensity());
                else if (fullList.get(current).equals("REHYDRATATION"))
                    value = Float.parseFloat(chartData.getRehydra());
                else if (fullList.get(current).equals("D1"))
                    value = Float.parseFloat(chartData.getD1());
                else if (fullList.get(current).equals("D2"))
                    value = Float.parseFloat(chartData.getD2());
                else if (fullList.get(current).equals("CL"))
                    value = Float.parseFloat(chartData.getCL());

                data[index] = new GraphViewData(index, value);
                minData[index] = new GraphViewData(index, min2);
                maxData[index] = new GraphViewData(index, max2);
                targetData[index] = new GraphViewData(index, (max2+min2)/2);
                    if (value < min)
                        min = value;

                    if (value > max)
                        max = value;
                array[index] = "" + index;
                index++;
            }

            // add data
            GraphView graphView = new LineGraphView(this, "X" + formulaCode + " " + productName + " - " + fullList.get(current));
            if (checkresult.isChecked()) {
                GraphViewSeries seriesWyniki = new GraphViewSeries("Wyn",
                        new GraphViewSeriesStyle(Color.rgb(0, 0, 0), 3), data);
                graphView.addSeries(seriesWyniki);
            }
            if (checkmin.isChecked()) {
                GraphViewSeries seriesMin = new GraphViewSeries("Min",
                        new GraphViewSeriesStyle(Color.parseColor("#ffff4444"), 2), minData);
                graphView.addSeries(seriesMin);
            }
            if (checkmax.isChecked()) {
                GraphViewSeries seriesMax = new GraphViewSeries("Max",
                        new GraphViewSeriesStyle(Color.parseColor("#ffff4444"), 2), maxData);
                graphView.addSeries(seriesMax);
            }

            if (checktarget.isChecked()) {
                GraphViewSeries seriesTarget = new GraphViewSeries("Target",
                        new GraphViewSeriesStyle(Color.parseColor("#ff99cc00"), 2), targetData);
                graphView.addSeries(seriesTarget);
            }

            graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
            graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
            graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        if (chartDataList.size() > value)
                            return chartDataList.get((int) value).getDate();
                        else
                            return "0";
                    } else
                        return String.format("%.1f", value);
                }
            });
            graphView.getGraphViewStyle().setNumHorizontalLabels(chartDataList.size());
            graphView.getGraphViewStyle().setTextSize(18);
            graphView.setViewPort(0, chartDataList.size()-1);
            graphView.setScrollable(true);
            if (min - 1 <= 0)
                graphView.setManualYAxisBounds(max+1, 0);
            else
                graphView.setManualYAxisBounds(max+1, min-1);
            graphView.getGraphViewStyle().setNumVerticalLabels(5);
            graphView.setShowLegend(false);
            graphView.rotateHorizontalLabels(-90);
            layout.addView(graphView);
        }
    }

    public void PrevChart(View paramView) {
        current -= 1;
        CreateChart();

        if (current - 1 == -1) {
            btn_prev.setVisibility(View.INVISIBLE);
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setText(fullList.get(current + 1));
        } else if (current - 1 >= 0) {
            btn_prev.setVisibility(View.VISIBLE);
            btn_prev.setText(fullList.get(current - 1));
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setText(fullList.get(current + 1));
        }
    }

    public void NextChart(View paramView) {
        current += 1;
        CreateChart();

        if (current + 1 == amount) {
            btn_prev.setVisibility(View.VISIBLE);
            btn_prev.setText(fullList.get(current - 1));
            btn_next.setVisibility(View.INVISIBLE);
        } else if (current + 1 < amount) {
            btn_prev.setVisibility(View.VISIBLE);
            btn_prev.setText(fullList.get(current - 1));
            btn_next.setVisibility(View.VISIBLE);
            btn_next.setText(fullList.get(current + 1));
        }
    }
}
