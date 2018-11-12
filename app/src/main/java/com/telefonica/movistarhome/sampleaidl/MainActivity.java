package com.telefonica.movistarhome.sampleaidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.telefonica.movistarhome.comms.ICommsTRService;
import com.telefonica.movistarhome.comms.service.CommsCall;
import com.telefonica.movistarhome.comms.service.CommsLastRegisterState;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btnGetLastRegisterState;
    private Button btnBind;
    private Button btnUnbind;
    private Button btnGetCalls;
    private Button btnStopGetCalls;

    private TextView txtViewResult;

    private ICommsTRService service;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = ICommsTRService.Stub.asInterface(iBinder);
            setButtonsState(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            setButtonsState(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListeners();
    }

    protected void initView() {
        btnGetLastRegisterState = findViewById(R.id.btnGetLastRegisterState);
        btnBind = findViewById(R.id.btnBind);
        btnUnbind = findViewById(R.id.btnUnbind);
        btnGetCalls = findViewById(R.id.btnGetCalls);
        btnStopGetCalls = findViewById(R.id.btnStopGetCalls);
        txtViewResult = findViewById(R.id.txtViewResult);

        txtViewResult.setText("");
        setButtonsState(false);
    }

    protected void setListeners() {
        btnGetLastRegisterState.setOnClickListener(view -> getLastRegisterState());
        btnGetCalls.setOnClickListener(view -> getCalls());
        btnStopGetCalls.setOnClickListener(view -> stopGetCalls());
        btnBind.setOnClickListener(view -> bind());
        btnUnbind.setOnClickListener(view -> unbind());
    }

    protected void bind() {
        Intent intent = new Intent(ICommsTRService.class.getName())
                .setPackage(ICommsTRService.class.getPackage().getName());

        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    protected void unbind() {
        unbindService(connection);
    }

    protected Runnable checkCalls;
    protected boolean keepChecking = true;
    protected void getCalls() {
        final Handler handler = new Handler(Looper.getMainLooper());
        checkCalls = () -> {
            if (service != null) {
                try {
                    List<CommsCall> callList = service.getCallState();
                    String response = "";
                    for (CommsCall commsCall: callList) {
                        String result = "State: " + commsCall.getState() +
                                ", StateText: " + commsCall.getStateText() +
                                ", local: " + commsCall.getLocal() +
                                ", remote: " + commsCall.getRemote() +
                                ", isIncomingCall: " + commsCall.isIncoming() +
                                ", connectedTime: " + commsCall.getConnectedTime() +
                                ", totalTime: " + commsCall.getTotalTime();
                        response += "\r\n" + result;
                    }
                    txtViewResult.setText(response);
                    Log.d(this.getClass().getSimpleName(), "getCalls. " + response);
                } catch (RemoteException e) {
                    Log.d(this.getClass().getSimpleName(), "Error. " + e.getMessage());
                    txtViewResult.setText("Error. " + e.getMessage());
                }
            }
            if (keepChecking) {
                handler.postDelayed(checkCalls, 2000);
            } else {
                btnGetCalls.setEnabled(true);
            }
        };
        btnGetCalls.setEnabled(false);
        btnStopGetCalls.setEnabled(true);
        keepChecking = true;
        handler.post(checkCalls);
    }

    protected void stopGetCalls() {
        keepChecking = false;
        btnStopGetCalls.setEnabled(false);
    }

    private void getLastRegisterState() {
        if (service != null) {
            try {
                CommsLastRegisterState commsLastRegisterState = service.getLastRegisterState();
                txtViewResult.setText("RESPONSE --> ID:" + commsLastRegisterState.getId() + ", Status:" +
                        commsLastRegisterState.getStatus() + ", Error:" + commsLastRegisterState.getError() +
                        ", Timestamp:" + dateInMillisecondsToString(commsLastRegisterState.getLastUpdate()));
            } catch (RemoteException e) {
                Log.d(this.getClass().getSimpleName(), "getLastRegisterState error: " + e.getMessage());
                txtViewResult.setText("RESPONSE --> error:" + e.getMessage());
            }
        }
    }

    protected void setButtonsState(boolean isBinding) {
        if (isBinding) {
            btnGetLastRegisterState.setEnabled(true);
            btnBind.setEnabled(false);
            btnUnbind.setEnabled(true);
            btnGetCalls.setEnabled(true);
            btnStopGetCalls.setEnabled(false);
        } else {
            btnGetLastRegisterState.setEnabled(false);
            btnGetCalls.setEnabled(false);
            btnBind.setEnabled(true);
            btnUnbind.setEnabled(false);
            btnStopGetCalls.setEnabled(false);
        }
    }

    public String dateInMillisecondsToString(long dateInMilliseconds) {
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                new Locale("es", "ES"));
        return dateFormat.format(new Date(dateInMilliseconds));
    }
}
