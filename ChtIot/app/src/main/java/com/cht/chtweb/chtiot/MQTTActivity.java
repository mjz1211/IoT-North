package com.cht.chtweb.chtiot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTActivity extends AppCompatActivity {
    //attribute
    private MqttAndroidClient mqttAndroidClient;
    //連接狀態設定TCP HOSTED/username/password/id
    private String serverUri="tcp://iot.cht.com.tw";
    private String clientID="eric1234";
    private String userName="PKWVLF9JM7MOIEFS40";
    private String password="PKWVLF9JM7MOIEFS40";
    //訂閱主題
    private String subTopic="/v1/device/5604483035/sensor/bedroom/rawdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        //1.建立MQTT Client物件(包含前端的id/username/password)
        clientID+=System.currentTimeMillis();
        //建構MQTT Client物件
        mqttAndroidClient=new MqttAndroidClient(getApplicationContext(),
                serverUri,
                clientID
                );
        //建構連接選項物件(username password)
        MqttConnectOptions options=new MqttConnectOptions();
        //options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName(userName);
        options.setPassword(password.toCharArray()); //設定字元陣列
        //進行連接
        try {

            IMqttToken token = mqttAndroidClient.connect(options,
                    //建構匿名類別的物件
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Toast.makeText(MQTTActivity.this, "連接成功!!", Toast.LENGTH_LONG).show();
                            try {
                                mqttAndroidClient.subscribe(subTopic, 0,null,
                                        new IMqttActionListener() {
                                            @Override
                                            public void onSuccess(IMqttToken asyncActionToken) {
                                                Toast.makeText(MQTTActivity.this, "訂閱成功!!", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                                Toast.makeText(MQTTActivity.this, "連接失敗!!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("連接錯誤",exception.getMessage());
                            Toast.makeText(MQTTActivity.this, "連接失敗!!", Toast.LENGTH_LONG).show();
                        }
                    }
                    );


            //訂閱
            mqttAndroidClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    byte[] msg=message.getPayload();
                    String mess=new String(msg,"UTF-8");
                        Toast.makeText(getApplicationContext(),mess,Toast.LENGTH_LONG).show();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });




        }catch (Exception ex)
        {
            Log.e("錯誤",ex.getMessage());
        }



    }
}
