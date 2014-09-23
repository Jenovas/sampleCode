package panel.dzienny;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import database.entities.RaportProductMeasure;
import database.entities.RaportProductMeasureAVGDEV;
import panel.dzienny.entities.CustomEditText;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class UserPickedProductMeasureActivity extends Activity {

    private static final String MyPREFERENCES = "session";
    private static final String panelID = "panelIdKey";
    private final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805f9b34fb");
    private SharedPreferences sharedpreferences;
    private String D1MIN;
    private String D1MAX;
    private String D1DEV;
    private String D2MIN;
    private String D2MAX;
    private String D2DEV;
    private String CLMIN;
    private String CLMAX;
    private String CLDEV;
    private String DENMIN;
    private String DENMAX;
    private String D1TAR;
    private String D2TAR;
    private String CLTAR;
    private int productnum;
    private String productname;
    private String formulacode;
    private int status;
    private int waiting;
    private int stop;
    private AlertDialog dialog;
    private View currentView;
    private InputStream aStream = null;
    private InputStreamReader aReader = null;
    private AsyncTask<View, Void, Void> mTask;


    ListView listViewPaired;
    ListView listViewDetected;
    private BluetoothSocket mSocket = null;
    private BufferedReader mBufferedReader = null;
    private BluetoothDevice dev = null;

    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PAIRED_DEVICE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userproductmeasure);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            D1TAR = extras.getString("D1TAR", "");
            D2TAR = extras.getString("D2TAR", "");
            CLTAR = extras.getString("CLTAR", "");
            D1MIN = extras.getString("D1MIN", "");
            D1MAX = extras.getString("D1MAX", "");
            D1DEV = extras.getString("D1DEV", "");
            D2MIN = extras.getString("D2MIN", "");
            D2MAX = extras.getString("D2MAX", "");
            D2DEV = extras.getString("D2DEV", "");
            CLMIN = extras.getString("CLMIN", "");
            CLMAX = extras.getString("CLMAX", "");
            CLDEV = extras.getString("CLDEV", "");
            DENMIN = extras.getString("DENMIN", "");
            DENMAX = extras.getString("DENMAX", "");
            productnum = extras.getInt("PRODUCTNUM", 0);
            productname = extras.getString("PRODUCTNAME", "");
            formulacode = extras.getString("FORMULACODE", "");
        }
        currentView = null;
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        TextView dane = (TextView) findViewById(R.id.text_name);
        dane.setText(productname + " (X" + formulacode + ")");
        waiting = 1;
        status = 0;
        stop = 1;

        Typeface robotoBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto/Bold.ttf");

        Button btn_return_to_grade = (Button) findViewById(R.id.button2);
        btn_return_to_grade.setTypeface(robotoBold);
        btn_return_to_grade.getBackground().setColorFilter(0xFF1e2124,
                PorterDuff.Mode.SRC);

        Button btn_return_to_list = (Button) findViewById(R.id.button3);
        btn_return_to_list.setTypeface(robotoBold);
        btn_return_to_list.getBackground().setColorFilter(0xFF111315,
                PorterDuff.Mode.SRC);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        SetupView();
        CheckBlueToothState();
    }

    public void SetupView() {
        List<RaportProductMeasure> MeasureList = null;
        MeasureList = RaportProductMeasure.find(RaportProductMeasure.class,
                "panelid = ? AND productid = ?",
                "" + sharedpreferences.getLong(panelID, 0), "" + productnum);
        if (!MeasureList.isEmpty()) {
            for (int i = 0; i < MeasureList.size(); i++) {
                RaportProductMeasure measure = MeasureList.get(i);
                int helper = measure.controler;
                switch (helper) {
                    case 1: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_1);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_1);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_1);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 2: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_2);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_2);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_2);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 3: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_3);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_3);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_3);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 4: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_4);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_4);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_4);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 5: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_5);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_5);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_5);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 6: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_6);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_6);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_6);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 7: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_7);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_7);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_7);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 8: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_8);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_8);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_8);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 9: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_9);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_9);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_9);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 10: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_10);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_10);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_10);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 11: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_11);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_11);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_11);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                    case 12: {
                        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_12);
                        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_12);
                        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_12);
                        d1.setText(measure.d);
                        d2.setText(measure.dd);
                        cl.setText(measure.cl);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < 12; i++)
                CreateMeasureList(i + 1);
        }

        AddOnChangeListners();
        UpdateAVGDEV();
        EnableButtons();
        EnableFocusForAll();
    }

    public void ReturnToList(View paramView) {
        if (stop == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
            }
            close(mSocket);
            close(aStream);
            close(aReader);
            close(mBufferedReader);
            Intent newIntent;
            newIntent = new Intent().setClass(
                    UserPickedProductMeasureActivity.this,
                    UserPickedProductListActivity.class);
            startActivity(newIntent);
            finish();
        }
    }

    public void ReturnToGrade(View paramView) {
        if (stop == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
            }
            close(mSocket);
            close(aStream);
            close(aReader);
            close(mBufferedReader);
            Intent newIntent;
            newIntent = new Intent().setClass(
                    UserPickedProductMeasureActivity.this,
                    UserProductDetailActivity.class);
            newIntent.putExtra("D1MIN", D1MIN);
            newIntent.putExtra("D1MAX", D1MAX);
            newIntent.putExtra("D1DEV", D1DEV);
            newIntent.putExtra("D2MIN", D2MIN);
            newIntent.putExtra("D2MAX", D2MAX);
            newIntent.putExtra("D2DEV", D2DEV);
            newIntent.putExtra("CLMIN", CLMIN);
            newIntent.putExtra("CLMAX", CLMAX);
            newIntent.putExtra("CLDEV", CLDEV);
            newIntent.putExtra("DENMIN", DENMIN);
            newIntent.putExtra("DENMAX", DENMAX);
            newIntent.putExtra("PRODUCTNUM", productnum);
            newIntent.putExtra("D1TAR", D1TAR);
            newIntent.putExtra("D2TAR", D2TAR);
            newIntent.putExtra("CLTAR", CLTAR);
            startActivity(newIntent);
            finish();
        }
    }

    public void Measure1(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask = null;
                currentView = null;
                EnableButtons();
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_1);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_2);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_1);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_1);
        d1.setTag(1);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure2(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_2);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_3);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_2);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_2);
        d1.setTag(2);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure3(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_3);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_4);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_3);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_3);
        d1.setTag(3);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure4(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_4);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_5);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_4);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_4);
        d1.setTag(4);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure5(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_5);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_6);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_5);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_5);
        d1.setTag(5);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure6(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_6);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_7);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_6);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_6);
        d1.setTag(6);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure7(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_7);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_8);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_7);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_7);
        d1.setTag(7);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure8(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_8);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_9);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_8);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_8);
        d1.setTag(8);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure9(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_9);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_10);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_9);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_9);
        d1.setTag(9);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure10(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_10);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_11);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_10);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_10);
        d1.setTag(10);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure11(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_11);
        CustomEditText nextLine = (CustomEditText) findViewById(R.id.btn_d1_12);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_11);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_11);
        d1.setTag(11);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl,
                nextLine);
        waiting = 1;
    }

    public void Measure12(View paramView) {
        if (status == 0)
            return;

        if (waiting == 1) {
            if (mTask != null) {
                mTask.cancel(true);
                mTask = null;
                EnableButtons();
                currentView = null;
            }
            return;
        }

        DisableButtons(paramView);
        CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_12);
        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_12);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_12);
        d1.setTag(12);
        currentView = paramView;
        mTask = new BluetoothConnectionMeasure().execute(paramView, d1, d2, cl);
        waiting = 1;
    }

    // ANDROID FUNCTIONS
    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
        // replaces the default 'Back' button action
        if (pKeyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(pKeyCode, pEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumeasure, menu);
        // Set up your ActionBar
        return true;
    }

    void ShowInfo() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.tabledialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        GridView gridView1 = (GridView) dialoglayout.findViewById(R.id.gridView1);
        GridView gridView2 = (GridView) dialoglayout.findViewById(R.id.gridView2);
        String[] text1 = new String[]{
                "    ",
                "MIN: ",
                "TAR: ",
                "MAX: "};

        String[] text2 = new String[]{
                "D1", "D2", "CL",
                "" + D1MIN, "" + D2MIN, "" + CLMIN,
                "" + D1TAR, "" + D2TAR, "" + CLTAR,
                "" + D1MAX, "" + D2MAX, "" + CLMAX};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(UserPickedProductMeasureActivity.this,
                android.R.layout.simple_list_item_1, text1);
        gridView1.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(UserPickedProductMeasureActivity.this,
                android.R.layout.simple_list_item_1, text2);
        gridView2.setAdapter(adapter2);
        builder.setView(dialoglayout);
        builder.setTitle("Specyfikacja AV");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent newIntent;
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_specyfikacja:
                ShowInfo();
                return true;
            case R.id.menu_reset:
                new ResetMeasure().execute();
                return true;
            case R.id.menu_mainmenu:
                newIntent = new Intent().setClass(
                        UserPickedProductMeasureActivity.this,
                        UserMainMenuActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(newIntent);
                finish();
                return true;
            case R.id.menu_panelmenu:
                newIntent = new Intent().setClass(
                        UserPickedProductMeasureActivity.this,
                        UserNewPanelActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(newIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // BLUETOOTH FUNCTIONS

    public void ConnectCaliper(View paramView) {
        waiting = 0;
        status = 0;
        stop = 0;
        Intent intent = new Intent();
        intent.setClass(UserPickedProductMeasureActivity.this, BluetoothListDeviceActivity.class);
        startActivityForResult(intent, REQUEST_PAIRED_DEVICE);
    }

    private void CheckBlueToothState() {
        TextView txtstatus = (TextView) findViewById(R.id.text_caliper_status);
        if (bluetoothAdapter == null) {
            txtstatus.setText("STATUS: NO ADAPTER");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    txtstatus.setText("STATUS: DISCOVERY");
                } else {
                    txtstatus.setText("STATUS: ENABLED");
                }
            } else {
                txtstatus.setText("STATUS: DISABLED");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBlueToothState();
        }
        if (requestCode == REQUEST_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {
                String name = data.getExtras().getString("DEVICENAME");
                BluetoothAdapter bluetoothAdapter
                        = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> pairedDevices
                        = bluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceBTName = device.getName();
                        if (name.equals(deviceBTName)) {
                            try {
                                openDeviceConnection(device);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void openDeviceConnection(BluetoothDevice aDevice)
            throws IOException {

        try {
            mSocket = aDevice.createRfcommSocketToServiceRecord(MY_UUID);
            mSocket.connect();
            aStream = mSocket.getInputStream();
            aReader = new InputStreamReader(aStream);
            mBufferedReader = new BufferedReader(aReader);
            status = 1;
            waiting = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView txtstatus = (TextView) findViewById(R.id.text_caliper_status);
                    txtstatus.setText("STATUS: CONNECTED");
                    DisableFocusForAll();
                    stop = 1;
                }
            });

        } catch (IOException e) {
            // Log.e("BLUETOOOOOOOOOOTH", "Could not connect to device", e);
            status = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView txtstatus = (TextView) findViewById(R.id.text_caliper_status);
                    txtstatus.setText("STATUS: DISCONNECTED");
                    EnableFocusForAll();
                    stop = 1;
                    if (dialog != null)
                        if (dialog.isShowing())
                            return;
                    dialog = new AlertDialog.Builder(
                            UserPickedProductMeasureActivity.this)
                            .setTitle("Brak polaczenia")
                            .setMessage(
                                    "Wystąpił błąd przy komunikacji z urządzeniem mierzącym, należy: \n"
                                            + "1.Odczekać chwilę i zobaczyć czy urządzenie automatycznie nie nawiąże połączenia. \n"
                                            + "2.Wymusić próbę połączenia klikając 'Connect with caliper' \n"
                                            + "3.Zresetować urządzenie mierzące i ponownie wykonać kroki 1 i 2.")
                            .setPositiveButton(R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                        }
                                    }).show();
                }
            });
            close(mBufferedReader);
            close(aReader);
            close(aStream);
            close(mSocket);
        }
    }

    private void close(Closeable aConnectedObject) {
        if (aConnectedObject == null)
            return;
        try {
            aConnectedObject.close();
        } catch (IOException e) {
        }
        aConnectedObject = null;
    }

    // EDITTEXT FUNCTIONS
    void DisableFocusForAll() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_userproductmeasure2);
        for (int i = 0; i < rl.getChildCount(); i++) {
            View view = rl.getChildAt(i);
            LinearLayout ll = (LinearLayout) view;
            for (int i2 = 0; i2 < ll.getChildCount(); i2++) {
                View view2 = ll.getChildAt(i2);
                // System.out.println(view2.toString());
                if (view2 instanceof CustomEditText) {
                    CustomEditText b = (CustomEditText) view2;
                    b.setFocusable(false);
                    b.setFocusableInTouchMode(false);
                }
            }
        }
    }

    void EnableFocusForAll() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_userproductmeasure2);
        for (int i = 0; i < rl.getChildCount(); i++) {
            View view = rl.getChildAt(i);
            LinearLayout ll = (LinearLayout) view;
            for (int i2 = 0; i2 < ll.getChildCount(); i2++) {
                View view2 = ll.getChildAt(i2);
                // System.out.println(view2.toString());
                if (view2 instanceof CustomEditText) {
                    CustomEditText b = (CustomEditText) view2;
                    b.setFocusable(true);
                    b.setFocusableInTouchMode(true);
                    b.setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                ((CustomEditText) v).setText("");
                            }
                        }
                    });
                }
            }
        }
    }

    void DisableButtons(View v) {
        v.getBackground().setColorFilter(0xeffffaff, PorterDuff.Mode.SRC_ATOP);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_userproductmeasure2);
        for (int i = 0; i < rl.getChildCount(); i++) {
            View view = rl.getChildAt(i);
            LinearLayout ll = (LinearLayout) view;
            for (int i2 = 0; i2 < ll.getChildCount(); i2++) {
                View view2 = ll.getChildAt(i2);
                // System.out.println(view2.toString());
                if (view2 instanceof CustomEditText) {
                    CustomEditText b = (CustomEditText) view2;
                    CustomEditText b2 = (CustomEditText) v;
                    if (!b.equals(b2)) {
                        b.setClickable(false);
                        b.setEnabled(false);
                        b.setTextColor(Color.WHITE);
                    }
                }
            }
        }
    }

    void EnableButtons() {
        waiting = 0;
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_userproductmeasure2);
        for (int i = 0; i < rl.getChildCount(); i++) {
            View view = rl.getChildAt(i);
            LinearLayout ll = (LinearLayout) view;
            for (int i2 = 0; i2 < ll.getChildCount(); i2++) {
                View view2 = ll.getChildAt(i2);
                // System.out.println(view2.toString());
                if (view2 instanceof CustomEditText) {
                    CustomEditText b = (CustomEditText) view2;
                    b.setClickable(true);
                    b.setEnabled(true);
                    b.setTextColor(Color.WHITE);
                    if (!b.getText().toString().equals("")
                            && !b.getText().toString().equals("0")
                            && (!b.getText().toString().equals("D1")
                            || !b.getText().toString().equals("D2") || !b
                            .getText().toString().equals("CL"))) {
                        b.setBackgroundResource(R.drawable.btn_default_holo_dark2);
                        b.getBackground().setColorFilter(0xFFc1e066,
                                PorterDuff.Mode.SRC_ATOP);
                    } else {
                        b.setBackgroundResource(R.drawable.btn_default_holo_dark);
                        b.getBackground().clearColorFilter();
                    }
                }
            }
        }

    }

    void AddOnChangeListners() {
        CustomEditText d = (CustomEditText) findViewById(R.id.btn_d1_1);
        CustomEditText dd = (CustomEditText) findViewById(R.id.btn_d2_1);
        CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_1);
        d.setTag(1);
        dd.setTag(1);
        cl.setTag(1);

        d.addTextChangedListener(new GenericTextWatcher(d, dd, cl));
        dd.addTextChangedListener(new GenericTextWatcher(d, dd, cl));
        cl.addTextChangedListener(new GenericTextWatcher(d, dd, cl));

        CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d1_2);
        CustomEditText dd2 = (CustomEditText) findViewById(R.id.btn_d2_2);
        CustomEditText cl2 = (CustomEditText) findViewById(R.id.btn_cl_2);
        d2.setTag(2);
        dd2.setTag(2);
        cl2.setTag(2);
        d2.addTextChangedListener(new GenericTextWatcher(d2, dd2, cl2));
        dd2.addTextChangedListener(new GenericTextWatcher(d2, dd2, cl2));
        cl2.addTextChangedListener(new GenericTextWatcher(d2, dd2, cl2));

        CustomEditText d3 = (CustomEditText) findViewById(R.id.btn_d1_3);
        CustomEditText dd3 = (CustomEditText) findViewById(R.id.btn_d2_3);
        CustomEditText cl3 = (CustomEditText) findViewById(R.id.btn_cl_3);
        d3.setTag(3);
        dd3.setTag(3);
        cl3.setTag(3);
        d3.addTextChangedListener(new GenericTextWatcher(d3, dd3, cl3));
        dd3.addTextChangedListener(new GenericTextWatcher(d3, dd3, cl3));
        cl3.addTextChangedListener(new GenericTextWatcher(d3, dd3, cl3));

        CustomEditText d4 = (CustomEditText) findViewById(R.id.btn_d1_4);
        CustomEditText dd4 = (CustomEditText) findViewById(R.id.btn_d2_4);
        CustomEditText cl4 = (CustomEditText) findViewById(R.id.btn_cl_4);
        d4.setTag(4);
        dd4.setTag(4);
        cl4.setTag(4);
        d4.addTextChangedListener(new GenericTextWatcher(d4, dd4, cl4));
        dd4.addTextChangedListener(new GenericTextWatcher(d4, dd4, cl4));
        cl4.addTextChangedListener(new GenericTextWatcher(d4, dd4, cl4));

        CustomEditText d5 = (CustomEditText) findViewById(R.id.btn_d1_5);
        CustomEditText dd5 = (CustomEditText) findViewById(R.id.btn_d2_5);
        CustomEditText cl5 = (CustomEditText) findViewById(R.id.btn_cl_5);
        d5.setTag(5);
        dd5.setTag(5);
        cl5.setTag(5);
        d5.addTextChangedListener(new GenericTextWatcher(d5, dd5, cl5));
        dd5.addTextChangedListener(new GenericTextWatcher(d5, dd5, cl5));
        cl5.addTextChangedListener(new GenericTextWatcher(d5, dd5, cl5));

        CustomEditText d6 = (CustomEditText) findViewById(R.id.btn_d1_6);
        CustomEditText dd6 = (CustomEditText) findViewById(R.id.btn_d2_6);
        CustomEditText cl6 = (CustomEditText) findViewById(R.id.btn_cl_6);
        d6.setTag(6);
        dd6.setTag(6);
        cl6.setTag(6);
        d6.addTextChangedListener(new GenericTextWatcher(d6, dd6, cl6));
        dd6.addTextChangedListener(new GenericTextWatcher(d6, dd6, cl6));
        cl6.addTextChangedListener(new GenericTextWatcher(d6, dd6, cl6));

        CustomEditText d7 = (CustomEditText) findViewById(R.id.btn_d1_7);
        CustomEditText dd7 = (CustomEditText) findViewById(R.id.btn_d2_7);
        CustomEditText cl7 = (CustomEditText) findViewById(R.id.btn_cl_7);
        d7.setTag(7);
        dd7.setTag(7);
        cl7.setTag(7);
        d7.addTextChangedListener(new GenericTextWatcher(d7, dd7, cl7));
        dd7.addTextChangedListener(new GenericTextWatcher(d7, dd7, cl7));
        cl7.addTextChangedListener(new GenericTextWatcher(d7, dd7, cl7));

        CustomEditText d8 = (CustomEditText) findViewById(R.id.btn_d1_8);
        CustomEditText dd8 = (CustomEditText) findViewById(R.id.btn_d2_8);
        CustomEditText cl8 = (CustomEditText) findViewById(R.id.btn_cl_8);
        d8.setTag(8);
        dd8.setTag(8);
        cl8.setTag(8);
        d8.addTextChangedListener(new GenericTextWatcher(d8, dd8, cl8));
        dd8.addTextChangedListener(new GenericTextWatcher(d8, dd8, cl8));
        cl8.addTextChangedListener(new GenericTextWatcher(d8, dd8, cl8));

        CustomEditText d9 = (CustomEditText) findViewById(R.id.btn_d1_9);
        CustomEditText dd9 = (CustomEditText) findViewById(R.id.btn_d2_9);
        CustomEditText cl9 = (CustomEditText) findViewById(R.id.btn_cl_9);
        d9.setTag(9);
        dd9.setTag(9);
        cl9.setTag(9);
        d9.addTextChangedListener(new GenericTextWatcher(d9, dd9, cl9));
        dd9.addTextChangedListener(new GenericTextWatcher(d9, dd9, cl9));
        cl9.addTextChangedListener(new GenericTextWatcher(d9, dd9, cl9));

        CustomEditText d10 = (CustomEditText) findViewById(R.id.btn_d1_10);
        CustomEditText dd10 = (CustomEditText) findViewById(R.id.btn_d2_10);
        CustomEditText cl10 = (CustomEditText) findViewById(R.id.btn_cl_10);
        d10.setTag(10);
        dd10.setTag(10);
        cl10.setTag(10);
        d10.addTextChangedListener(new GenericTextWatcher(d10, dd10, cl10));
        dd10.addTextChangedListener(new GenericTextWatcher(d10, dd10, cl10));
        cl10.addTextChangedListener(new GenericTextWatcher(d10, dd10, cl10));

        CustomEditText d11 = (CustomEditText) findViewById(R.id.btn_d1_11);
        CustomEditText dd11 = (CustomEditText) findViewById(R.id.btn_d2_11);
        CustomEditText cl11 = (CustomEditText) findViewById(R.id.btn_cl_11);
        d11.setTag(11);
        dd11.setTag(11);
        cl11.setTag(11);
        d11.addTextChangedListener(new GenericTextWatcher(d11, dd11, cl11));
        dd11.addTextChangedListener(new GenericTextWatcher(d11, dd11, cl11));
        cl11.addTextChangedListener(new GenericTextWatcher(d11, dd11, cl11));

        CustomEditText d12 = (CustomEditText) findViewById(R.id.btn_d1_12);
        CustomEditText dd12 = (CustomEditText) findViewById(R.id.btn_d2_12);
        CustomEditText cl12 = (CustomEditText) findViewById(R.id.btn_cl_12);
        d12.setTag(12);
        dd12.setTag(12);
        cl12.setTag(12);
        d12.addTextChangedListener(new GenericTextWatcher(d12, dd12, cl12));
        dd12.addTextChangedListener(new GenericTextWatcher(d12, dd12, cl12));
        cl12.addTextChangedListener(new GenericTextWatcher(d12, dd12, cl12));

        // Set Next Focus
        d.SetNextFocusView(dd);
        dd.SetNextFocusView(cl);
        cl.SetNextFocusView(d2);

        d2.SetNextFocusView(dd2);
        dd2.SetNextFocusView(cl2);
        cl2.SetNextFocusView(d3);

        d3.SetNextFocusView(dd3);
        dd3.SetNextFocusView(cl3);
        cl3.SetNextFocusView(d4);

        d4.SetNextFocusView(dd4);
        dd4.SetNextFocusView(cl4);
        cl4.SetNextFocusView(d5);

        d5.SetNextFocusView(dd5);
        dd5.SetNextFocusView(cl5);
        cl5.SetNextFocusView(d6);

        d6.SetNextFocusView(dd6);
        dd6.SetNextFocusView(cl6);
        cl6.SetNextFocusView(d7);

        d7.SetNextFocusView(dd7);
        dd7.SetNextFocusView(cl7);
        cl7.SetNextFocusView(d8);

        d8.SetNextFocusView(dd8);
        dd8.SetNextFocusView(cl8);
        cl8.SetNextFocusView(d9);

        d9.SetNextFocusView(dd9);
        dd9.SetNextFocusView(cl9);
        cl9.SetNextFocusView(d10);

        d10.SetNextFocusView(dd10);
        dd10.SetNextFocusView(cl10);
        cl10.SetNextFocusView(d11);

        d11.SetNextFocusView(dd11);
        dd11.SetNextFocusView(cl11);
        cl11.SetNextFocusView(d12);

        d12.SetNextFocusView(dd12);
        dd12.SetNextFocusView(cl12);
        cl12.SetNextFocusView(null);
    }

    // DB FUNCTIONS
    void CreateMeasureList(int controler) {
        RaportProductMeasure measure = new RaportProductMeasure(
                UserPickedProductMeasureActivity.this,
                sharedpreferences.getLong(panelID, 0), productnum, "" + 0,
                "" + 0, "" + 0, controler);
        measure.save();
    }

    boolean isFloat(String var) {
        try {
            Float.parseFloat(var);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void UpdateAVGDEV() {
        List<RaportProductMeasure> MeasureList = null;
        MeasureList = RaportProductMeasure.find(RaportProductMeasure.class,
                "panelid = ? AND productid = ?",
                "" + sharedpreferences.getLong(panelID, 0), "" + productnum);
        float Totald = 0;
        float Totald2 = 0;
        float Totalcl = 0;

        if (!MeasureList.isEmpty()) {
            for (int i = 0; i < MeasureList.size(); i++) {
                RaportProductMeasure measure = MeasureList.get(i);
                if (isFloat(measure.d))
                    Totald += Float.parseFloat(measure.d);
                if (isFloat(measure.dd))
                    Totald2 += Float.parseFloat(measure.dd);
                if (isFloat(measure.cl))
                    Totalcl += Float.parseFloat(measure.cl);
            }
        }

        DecimalFormat df = new DecimalFormat("000.00");
        TextView d1 = (TextView) findViewById(R.id.text_avg_d1);
        TextView d2 = (TextView) findViewById(R.id.text_avg_d2);
        TextView cl = (TextView) findViewById(R.id.text_avg_cl);
        d1.setText("" + df.format(Totald / 12));
        d2.setText("" + df.format(Totald2 / 12));
        cl.setText("" + df.format(Totalcl / 12));

        float D1min = 0;
        if (!D1MIN.equals(""))
            D1min = Float.parseFloat(D1MIN);

        float D1max = 0;
        if (!D1MAX.equals(""))
            D1max = Float.parseFloat(D1MAX);

        float D1DEVMax = 0;
        if (!D1DEV.equals(""))
            D1DEVMax = Float.parseFloat(D1DEV);

        float D2min = 0;
        if (!D2MIN.equals(""))
            D2min = Float.parseFloat(D2MIN);

        float D2max = 0;
        if (!D2MAX.equals(""))
            D2max = Float.parseFloat(D2MAX);

        float D2DEVMax = 0;
        if (!D2DEV.equals(""))
            D2DEVMax = Float.parseFloat(D2DEV);

        float CLmin = 0;
        if (!CLMIN.equals(""))
            CLmin = Float.parseFloat(CLMIN);

        float CLmax = 0;
        if (!CLMAX.equals(""))
            CLmax = Float.parseFloat(CLMAX);

        float CLDEVMax = 0;
        if (!CLDEV.equals(""))
            CLDEVMax = Float.parseFloat(CLDEV);

        if ((Totald / 12) > D1min && (Totald / 12) < D1max)
            d1.setTextColor(Color.GREEN);
        else
            d1.setTextColor(Color.RED);
        if ((Totald2 / 12) > D2min && (Totald2 / 12) < D2max)
            d2.setTextColor(Color.GREEN);
        else
            d2.setTextColor(Color.RED);
        if ((Totalcl / 12) > CLmin && (Totalcl / 12) < CLmax)
            cl.setTextColor(Color.GREEN);
        else
            cl.setTextColor(Color.RED);

        float avgd = Totald / 12;
        float avgdd = Totald2 / 12;
        float avgcl = Totalcl / 12;

        double tempd = 0;
        double tempd2 = 0;
        double tempcl = 0;
        if (!MeasureList.isEmpty()) {
            for (int i = 0; i < MeasureList.size(); i++) {
                RaportProductMeasure measure = MeasureList.get(i);
                if (isFloat(measure.d))
                    tempd += (avgd - Float.parseFloat(measure.d))
                            * (avgd - Float.parseFloat(measure.d));
                if (isFloat(measure.dd))
                    tempd2 += (avgdd - Float.parseFloat(measure.dd))
                            * (avgdd - Float.parseFloat(measure.dd));
                if (isFloat(measure.cl))
                    tempcl += (avgcl - Float.parseFloat(measure.cl))
                            * (avgcl - Float.parseFloat(measure.cl));
            }
        }

        double vard = Math.sqrt(tempd / 12);
        double vard2 = Math.sqrt(tempd2 / 12);
        double vardcl = Math.sqrt(tempcl / 12);
        TextView dvd1 = (TextView) findViewById(R.id.text_dev_d1);
        TextView dvd2 = (TextView) findViewById(R.id.text_dev_d2);
        TextView dvcl = (TextView) findViewById(R.id.text_dev_cl);
        dvd1.setText("" + df.format(vard));
        dvd2.setText("" + df.format(vard2));
        dvcl.setText("" + df.format(vardcl));
        if (vard > 0 && vard < D1DEVMax)
            dvd1.setTextColor(Color.GREEN);
        else
            dvd1.setTextColor(Color.RED);
        if (vard2 > 0 && vard2 < D2DEVMax)
            dvd2.setTextColor(Color.GREEN);
        else
            dvd2.setTextColor(Color.RED);
        if (vardcl > 0 && vardcl < CLDEVMax)
            dvcl.setTextColor(Color.GREEN);
        else
            dvcl.setTextColor(Color.RED);
        waiting = 0;

        List<RaportProductMeasureAVGDEV> MeasureAVGDEVList = null;
        MeasureAVGDEVList = RaportProductMeasureAVGDEV.find(
                RaportProductMeasureAVGDEV.class,
                "panelid = ? AND productid = ?",
                "" + sharedpreferences.getLong(panelID, 0), "" + productnum);
        if (MeasureAVGDEVList.isEmpty()) {
            RaportProductMeasureAVGDEV measureAVGDEV = new RaportProductMeasureAVGDEV(
                    UserPickedProductMeasureActivity.this);
            measureAVGDEV.panelid = sharedpreferences.getLong(panelID, 0);
            measureAVGDEV.productid = productnum;
            measureAVGDEV.davg = "" + df.format(avgd);
            measureAVGDEV.ddavg = "" + df.format(avgdd);
            measureAVGDEV.clavg = "" + df.format(avgcl);
            measureAVGDEV.ddev = "" + df.format(vard);
            measureAVGDEV.dddev = "" + df.format(vard2);
            measureAVGDEV.cldev = "" + df.format(vardcl);
            measureAVGDEV.save();
        } else {
            RaportProductMeasureAVGDEV measureAVGDEV = MeasureAVGDEVList.get(0);
            measureAVGDEV.panelid = sharedpreferences.getLong(panelID, 0);
            measureAVGDEV.productid = productnum;
            measureAVGDEV.davg = "" + df.format(avgd);
            measureAVGDEV.ddavg = "" + df.format(avgdd);
            measureAVGDEV.clavg = "" + df.format(avgcl);
            measureAVGDEV.ddev = "" + df.format(vard);
            measureAVGDEV.dddev = "" + df.format(vard2);
            measureAVGDEV.cldev = "" + df.format(vardcl);
            measureAVGDEV.save();
        }
    }

    private class BluetoothConnectionMeasure extends
            AsyncTask<View, Void, Void> {

        CustomEditText btn1;
        CustomEditText btn2;
        CustomEditText btn3;
        int controler;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(final View... params) {

            btn1 = (CustomEditText) params[1];
            btn2 = (CustomEditText) params[2];
            btn3 = (CustomEditText) params[3];
            controler = (Integer) btn1.getTag();

            String bString = "";
            try {
                StringBuilder response = new StringBuilder();
                int line;
                int counter = 0;
                while ((line = mBufferedReader.read()) != -1) {
                    if (counter >= 1) {
                        response.append((char) line);
                    }
                    counter++;
                    if (counter == 8) {
                        break;
                    }
                }
                String myString = response.toString();
                bString = myString.substring(myString.length() - 7,
                        myString.length());
                System.out.flush();
            } catch (IOException e1) {
                bString = "";
                Log.e("BUFFERREADER", "mBufferedReader", e1);
            }

            final String cString = bString;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (currentView == null)
                        return;

                    if (!cString.equals(""))
                        ((CustomEditText) currentView).setText(cString);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            currentView.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    View nextView = ((CustomEditText) currentView)
                            .GetNextFocusView();
                    if (nextView != null)
                        nextView.performClick();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Timer timer = new Timer("mytimer", true);
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            EnableButtons();
                                        }
                                    });
                                }
                            }).start();
                        }
                    };
                    timer.schedule(task, 500);

                    List<RaportProductMeasure> MeasureList = null;
                    MeasureList = RaportProductMeasure.find(
                            RaportProductMeasure.class,
                            "panelid = ? AND productid = ? AND controler = ?",
                            "" + sharedpreferences.getLong(panelID, 0), ""
                                    + productnum, "" + controler);
                    if (!MeasureList.isEmpty()) {
                        RaportProductMeasure measure = MeasureList.get(0);
                        measure.d = btn1.getText().toString();
                        measure.dd = btn2.getText().toString();
                        measure.cl = btn3.getText().toString();
                        measure.save();
                    } else {
                        RaportProductMeasure measure = new RaportProductMeasure(
                                UserPickedProductMeasureActivity.this,
                                sharedpreferences.getLong(panelID, 0),
                                productnum, btn1.getText().toString(), btn2
                                .getText().toString(), btn3.getText()
                                .toString(), controler);
                        measure.save();
                    }
                    UpdateAVGDEV();
                }
            });
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private CustomEditText viewd;
        private CustomEditText viewdd;
        private CustomEditText viewcl;

        private GenericTextWatcher(View viewd, View viewdd, View viewcl) {
            this.viewd = (CustomEditText) viewd;
            this.viewdd = (CustomEditText) viewdd;
            this.viewcl = (CustomEditText) viewcl;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1,
                                  int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            List<RaportProductMeasure> MeasureList = null;
            MeasureList = RaportProductMeasure.find(RaportProductMeasure.class,
                    "panelid = ? AND productid = ? AND controler = ?", ""
                            + sharedpreferences.getLong(panelID, 0), ""
                            + productnum, "" + viewd.getTag().toString());
            if (!MeasureList.isEmpty()) {
                RaportProductMeasure measure = MeasureList.get(0);
                measure.d = viewd.getText().toString();
                measure.dd = viewdd.getText().toString();
                measure.cl = viewcl.getText().toString();
                measure.save();
            } else {
                RaportProductMeasure measure = new RaportProductMeasure(
                        UserPickedProductMeasureActivity.this,
                        sharedpreferences.getLong(panelID, 0), productnum,
                        viewd.getText().toString(),
                        viewdd.getText().toString(), viewcl.getText()
                        .toString(), Integer.parseInt(viewd.getTag()
                        .toString()));
                measure.save();
            }
            UpdateAVGDEV();
            EnableButtons();
        }
    }

    private class ResetMeasure extends AsyncTask<Void, Void, Void> {
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            ShowWarning();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<RaportProductMeasure> MeasureList = null;
            MeasureList = RaportProductMeasure
                    .find(RaportProductMeasure.class,
                            "panelid = ? AND productid = ?", ""
                                    + sharedpreferences.getLong(panelID, 0), ""
                                    + productnum);
            if (!MeasureList.isEmpty()) {
                for (int i = 0; i < MeasureList.size(); i++) {
                    final RaportProductMeasure measure = MeasureList.get(i);
                    int helper = measure.controler;
                    switch (helper) {
                        case 1: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_1);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_1);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_1);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 2: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_2);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_2);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_2);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 3: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_3);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_3);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_3);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 4: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_4);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_4);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_4);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 5: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_5);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_5);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_5);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 6: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_6);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_6);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_6);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 7: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_7);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_7);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_7);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 8: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();
                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_8);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_8);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_8);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 9: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();

                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_9);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_9);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_9);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 10: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();
                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_10);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_10);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_10);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 11: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();
                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_11);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_11);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_11);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                        case 12: {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    measure.d = "0";
                                    measure.dd = "0";
                                    measure.cl = "0";
                                    measure.save();
                                    CustomEditText d1 = (CustomEditText) findViewById(R.id.btn_d1_12);
                                    CustomEditText d2 = (CustomEditText) findViewById(R.id.btn_d2_12);
                                    CustomEditText cl = (CustomEditText) findViewById(R.id.btn_cl_12);
                                    d1.setText(measure.d);
                                    d2.setText(measure.dd);
                                    cl.setText(measure.cl);
                                }
                            });
                            break;
                        }
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateAVGDEV();
                    EnableButtons();
                }
            });

            return null;
        }

        protected void ShowWarning() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog = new AlertDialog.Builder(
                            UserPickedProductMeasureActivity.this)
                            .setTitle("Loading...")
                            .setMessage("Trwa zerowanie wyników proszę czekać")
                            .show();
                    dialog.setCancelable(false);
                }
            });
        }

        @Override
        protected void onPostExecute(Void result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                }
            });
        }
    }
}
