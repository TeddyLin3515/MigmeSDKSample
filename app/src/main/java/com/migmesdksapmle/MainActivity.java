package com.migmesdksapmle;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.migmesdk.dyson.Dyson;
import com.migmesdk.dyson.DysonEventBuilders;
import com.migmesdk.dyson.DysonTracker;
import com.migmesdk.dyson.data.DysonParameter;
import com.migmesdksapmle.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ActivityMainBinding binding;
    DysonTracker dysonTracker;
    String logMessage = "";
    String selection = DysonParameter.ACTION.TYPE.ACHIEVEMENT;
    final String[] actionType = {DysonParameter.ACTION.TYPE.ACHIEVEMENT,
            DysonParameter.ACTION.TYPE.BILLING,
            DysonParameter.ACTION.TYPE.INVITE,
            DysonParameter.ACTION.TYPE.SHARE,
            DysonParameter.ACTION.TYPE.TOPUP};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> actionList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                actionType);
        spinner.setAdapter(actionList);
        spinner.setOnItemSelectedListener(this);
        initDyson();
    }

    private void initDyson() {
        Dyson.setDebugMode(true); //Default value is 'false'
        dysonTracker = Dyson.getInstance().newTracker(getApplicationContext(), "Migme Game", "00000001");
        buildLog("Initialize done...");
        buildLog("Debug mode >>> " + Dyson.getInstance().DEBUG_MODE);
    }

    public void loginMigme(View view) {
        String userId = "Tester000001";
        dysonTracker.setMigmeId(userId);
        buildLog("Login Migme with ID : "+userId);
    }

    public void logoutMigme(View view) {
        dysonTracker.cleanMigmeId();
        buildLog("Logout Migme");
    }

    public void send(View view) {
        dysonTracker.send(new DysonEventBuilders.ActionEventBuilder()
                .setActionType(selection)
                .setActionValue(1)
                .setActionDescription("event description"));
        buildLog("Send dyson event >>> " + selection);
    }

    public void changeMode(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        Dyson.setDebugMode(isChecked);
        buildLog((isChecked ? "Enable" : "Disable") + " debug mode.");
    }

    public void buildLog(String log) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");
        Date date = new Date();
        logMessage = String.format("%s%s: %s%s", logMessage, format.format(date), log, "\n");
        binding.setLog(logMessage);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selection = actionType[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
