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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnGetNGNStatus;
    private Button btnGetNGNId;
    private Button btnGetNGNError;
    private Button btnGetNGNLastUpdate;
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
        btnGetNGNStatus = findViewById(R.id.btnGetNGNStatus);
        btnGetNGNId = findViewById(R.id.btnGetNGNId);
        btnGetNGNError = findViewById(R.id.btnGetNGNError);
        btnGetNGNLastUpdate = findViewById(R.id.btnGetNGNLastUpdate);
        btnBind = findViewById(R.id.btnBind);
        btnUnbind = findViewById(R.id.btnUnbind);
        btnGetCalls = findViewById(R.id.btnGetCalls);
        btnStopGetCalls = findViewById(R.id.btnStopGetCalls);
        txtViewResult = findViewById(R.id.txtViewResult);

        txtViewResult.setText("");
        setButtonsState(false);
    }

    protected void setListeners() {
        btnGetNGNStatus.setOnClickListener(view -> getNGNStatus());
        btnGetNGNId.setOnClickListener(view -> getNGNId());
        btnGetNGNError.setOnClickListener(view -> getNGNError());
        btnGetNGNLastUpdate.setOnClickListener(view -> getNGNLastUpdate());
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

    private void getNGNStatus() {
        String response = sendRequest("NGN.Status");
        txtViewResult.setText("RESPONSE:" + response);
    }

    private void getNGNId() {
        String response = sendRequest("NGN.Id");
        txtViewResult.setText("RESPONSE:" + response);
    }

    private void getNGNLastUpdate() {
        String response = sendRequest("NGN.LastUpdate");
        txtViewResult.setText("RESPONSE:" + response);
    }

    private void getNGNError() {
        String response = sendRequest("NGN.Error");
        txtViewResult.setText("RESPONSE:" + response);
    }

    protected String sendRequest(String param) {
        String response = "";
        if (service != null) {
            try {
                response = service.getParameterValues(param);
            } catch (RemoteException e) {
                Log.d(this.getClass().getSimpleName(), "sendRequest [param:" + param +
                        "] error. " + e.getMessage());
                response = e.getMessage();
            }
        }
        return response;
    }

    protected void setButtonsState(boolean isBinding) {
        if (isBinding) {
            btnGetNGNStatus.setEnabled(true);
            btnGetNGNId.setEnabled(true);
            btnGetNGNError.setEnabled(true);
            btnGetNGNLastUpdate.setEnabled(true);
            btnBind.setEnabled(false);
            btnUnbind.setEnabled(true);
            btnGetCalls.setEnabled(true);
            btnStopGetCalls.setEnabled(false);
        } else {
            btnGetNGNStatus.setEnabled(false);
            btnGetNGNId.setEnabled(false);
            btnGetNGNError.setEnabled(false);
            btnGetNGNLastUpdate.setEnabled(false);
            btnGetCalls.setEnabled(false);
            btnBind.setEnabled(true);
            btnUnbind.setEnabled(false);
            btnStopGetCalls.setEnabled(false);
        }
    }
}
