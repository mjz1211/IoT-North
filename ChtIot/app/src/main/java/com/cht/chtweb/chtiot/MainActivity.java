package com.cht.chtweb.chtiot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cht.entity.DHTEntity;

//Controller Activity as Context
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //attribute
    private  EditText editMessage;
    private View.OnClickListener handler=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this,"我是另一種事件程序寫法",Toast.LENGTH_LONG).show();
        }
    };
    //Initializer onXxxx() as Life Cycle Event
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //參考出UI成員
        editMessage=this.findViewById(R.id.editMessage);
        Button btnShow=this.findViewById(R.id.btnShow);
        Button btnIot=this.findViewById(R.id.btnIoT);
        Button btnSearch=this.findViewById(R.id.btnSearch);
        //設計一個沒有名稱的類別(匿名類別) 實作介面View.OnClickListener 將方法實作  同時建構成一個物件 委派到按鈕去聽Click
        //TODO 查詢按鈕的事件程序
        btnSearch.setOnClickListener(
                //new 匿名類別 的物件 MainActivity$1.class
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Log.i("訊息",editMessage.getText().toString());
                    }
                }
        );
        // 委派一個物件來聽按鈕的click event source
        btnShow.setOnClickListener(this);
        btnIot.setOnClickListener(handler);
        editMessage.setText("02-");
        //Java事件程序(Method-規範(No delegate類型)

        //執行階段建立一個物件
//        DatePicker datePicker=new DatePicker(this);
//        this.setContentView(datePicker); //類別多型化
    }

    @Override
    public void onClick(View v) {
        //判斷Event Source來源
        if(v.getId()==R.id.btnShow) {
            //Toast
            Toast.makeText(this, "Hello!!", Toast.LENGTH_LONG).show();
        }else if(v.getId()==R.id.btnIoT)
        {
            Toast.makeText(this, "Hello IoT!!", Toast.LENGTH_LONG).show();
        }
    }

    public void changeHDT(View view)
    {
        //Toast.makeText(this,"切換視窗",Toast.LENGTH_LONG).show();
        //1.建構Intent 設定目標(新DHT Activity) 來源哪裡?
        //第二參數載入Meta 採用later binding
        Intent intent=new Intent(MainActivity.this,DHTActivity.class);
        //處理程序...傳遞資訊
        intent.putExtra("mag",editMessage.getText().toString());
        Integer it=new Integer(1000);
        intent.putExtra("value",1000); //Autoboxing Integer (Wrapper class)物件類型
        //一個物件或者多個物件的傳遞
        //建構物件
        DHTEntity entity=new DHTEntity();
        entity.setSensorID("0001");
        entity.setTemper(20.05);
        entity.setHumi(30.05);
        entity.setDatetime("2018-11-08T12:00:00Z");
        intent.putExtra("entity",entity); //介面多型化
        //動起來
        this.startActivity(intent);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("週期","start");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("週期","stop");
    }

    public void changeMQQTT(View view)
    {
        //建構Intent
        Intent intent=new Intent(MainActivity.this,MQTTActivity.class);
        startActivity(intent);
    }
}
