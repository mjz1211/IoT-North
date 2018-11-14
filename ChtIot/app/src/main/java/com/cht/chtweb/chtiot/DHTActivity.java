package com.cht.chtweb.chtiot;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cht.entity.AppUtility;
import com.cht.entity.DHT;
import com.cht.entity.DHTEntity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public  class DHTActivity extends AppCompatActivity {
    //Attribute
    //定義 不要立即產生物件???
    private RequestQueue queue;
    private ListView listDHT;
    private List<DHTEntity> data; //採有順序收集DHTEntity物件
    private View.OnClickListener loadDHTDataHandler=
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlString=
                            String.format(AppUtility.HOSTED+AppUtility.BEDDHTRANGE,
                                    AppUtility.getISO8601DateString(5),
                                    AppUtility.getISO8601DateString(0));
                    Log.i("url",urlString);
                    //CHT IoT REST Service EndPoint
                    //進行Web Client進行Request 請求服務(採用非同步Async--自建多執行緒的程式 )
                    //DHTReaderHandler hander=new DHTReaderHandler();
                    //hander.execute(urlString); //進入非同步執行

                    //需要一個JsonArrayRequest物件 new 是一個匿名類別 繼承了JsonArrayRequest物件
                    JsonArrayRequest request=new JsonArrayRequest(
                            urlString,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {
                                    Log.i("結果",jsonArray.length()+"");
                                    Log.i("Result",jsonArray.toString());
                                    Toast.makeText(DHTActivity.this,jsonArray.length()+"",Toast.LENGTH_LONG).show();
                                    //資料整理 產生一個新的集合物件
                                    data=new ArrayList<>();

                                    //走訪Json Array
                                    for(int s=0;s<jsonArray.length();s++)
                                    {
                                        try {
                                            JSONObject obj =jsonArray.getJSONObject(s);
                                            //封裝成DHTEntity物件 再讓集合參考
                                            DHTEntity entity=new DHTEntity();
                                            entity.setSensorID(obj.getString("id"));
                                            entity.setDatetime(obj.getString("time"));
                                            //取溫溼度 value:["{}"]
                                            JSONArray values=obj.getJSONArray("value");
                                            String value=values.getString(0);
                                            Gson gson=new Gson();
                                            DHT dht=gson.fromJson(value,DHT.class);
                                            //Log.i("DHT",dht.getTemper()+"");
                                            entity.setTemper(dht.getTemper());
                                            entity.setHumi(dht.getHumi());
                                            //讓集合參考
                                            data.add(entity);

                                        } catch (JSONException e) {
                                            Log.e("錯誤",e.getMessage());
                                        }

                                    }
                                    Log.i("Collection",data.size()+"");

                                       //TODO UI如何整理
                                    listDHT.setAdapter(new DHTListAdapter());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.i("錯誤結果",volleyError.getMessage());
                                }
                            }
                    ){
                        //Overriding Method

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            //自訂一個HashMap<Sting,String>
                            HashMap<String,String> headers=new HashMap<>();
                            headers.put("CK",AppUtility.READAPIKEY);
                            return headers;
                        }
                    };
                    //送至Request Queue去排隊 進行非同步處理
                    queue.add(request);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue=Volley.newRequestQueue(this);;
        //取出傳遞進來的資訊
        Intent intent=this.getIntent();
        //取物件(取出序列化界面) 明確轉型
        DHTEntity dhtEntity=(DHTEntity)intent.getSerializableExtra("entity");
        Toast.makeText(this,dhtEntity.getDatetime(),Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dht);
        //參考出成員
        Button btnDHT=this.findViewById(R.id.btnLoadDHT);
        //設定事件委派
        btnDHT.setOnClickListener(loadDHTDataHandler);

        listDHT=this.findViewById(R.id.listDHT);


        //如何取出資源檔中設定的陣程式化
        Resources resource=this.getResources();
        //透過資源物件來問特定id 內容
        String[] items=resource.getStringArray( R.array.items);
        Log.i("陣列",items.length+"");
        //建構一個ListView背後轉換資料來源產生配合畫面相對順序View(項目) 配接器物件
        ArrayAdapter<String> adapter=
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,items);
//        //派上去影武了
      listDHT.setAdapter(adapter);

    }

    //Nested Class
    public class DHTReaderHandler extends AsyncTask<String,Integer,JSONArray>
    {
        private AlertDialog dialog;
        //before after

        //UI Thread
        @Override
        protected void onPreExecute() {
            String dateString=AppUtility.getISO8601DateString(1);
            Log.i("iso8601",dateString);
            //呈現對話盒
            dialog=AppUtility.createAlertDialog(DHTActivity.this,"對話盒","資料下載中...");
            dialog.show();

        }

        //UI Thread
        @Override
        protected void onPostExecute(JSONArray jsonObject) {
            //關閉對話盒
            dialog.dismiss();
            //進行資料處理
        }

        //第二條執行緒採用背後併行作業
        @Override
        protected JSONArray doInBackground(String... strings) {
            //上URL 提出request
            //封裝URLString變成URL物件 透過URL開啟一個連接物件Request
            try {
                URL url=new URL(strings[0]);
                //使用工廠模式 要一個連接
                HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
                //帶一把金鑰
                connection.setRequestProperty("CK",AppUtility.READAPIKEY);
                //透過Response取回串流
                InputStream is=connection.getInputStream();
                //2.接軌 讀取器
                InputStreamReader reader=new InputStreamReader(is,"UTF-8");
                //讀取步輻
                char[] step=new char[20];
                //設定緩存區
                StringBuilder builder=new StringBuilder();
                int length=0;
                while((length=reader.read(step,0,step.length))>-1)
                {
                    //放緩存區
                    builder.append(step,0,length);
                }
                String content=builder.toString();
                Log.i("結果",content);


            } catch (MalformedURLException e) {
               Log.i("錯誤",e.getMessage());
            } catch (IOException e) {
                Log.i("錯誤",e.getMessage());
            }

//            //放慢
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return null;
        }
    }

    //inner class
    public class DHTListAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater;
        //自訂建構子
        public DHTListAdapter()
        {
            layoutInflater=LayoutInflater.from(DHTActivity.this);
        }

        //回應多少資料操作(多少View(項目)操作)
        @Override
        public int getCount() {
            return data.size();
        }

        //幕後(配合前面幕前演戲的ListView/XXXView as AdapterView) 執行一個取畫面相對順序項目的代表資料???getItemAtPoasition method
        @Override
        public Object getItem(int position) {
            return data.get(position).getDatetime();
        }

        //畫面項目的順序 從零開始  順序對應到配接器掌握資料來源 的相關順序對應關係
        @Override
        public long getItemId(int position) {
            return position;
        }

        //第一個參數 畫面的項目位置 (是否有既定的View??)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("位置:",position+"");
            //判斷是否為第一列(沒有View)
            ViewTag tag=null;
            if(convertView==null){
                //要生產一個View出來(從layout xml file->如何轉換成一個View物件)LayoutInflater
                convertView=layoutInflater.inflate(R.layout.dhtitem,null);
                //建構畫面成員群組
                tag=new ViewTag();
                tag.txtDate=convertView.findViewById(R.id.txtDate);
                tag.txtTemper=convertView.findViewById(R.id.txtTepmer);
                //讓lauout(item)參考畫面成員群組
                convertView.setTag(tag);

            }else
            {
                //取出傳遞進來的View(內容是上一個處理的)
                //問出來畫面群組
                tag=(ViewTag)convertView.getTag();

            }
           //鋪畫面成員內容(data)

            tag.txtDate.setText(data.get(position).getDatetime());
            String msg=String.format("溫度:%.2f 濕度:%.2f"
                    ,data.get(position).getTemper(),data.get(position).getHumi());
            tag.txtTemper.setText(msg);
            return convertView;
        }

        //inner class Group群組
        public class ViewTag
        {
            public TextView txtDate;
            public TextView txtTemper;
        }
    }


}
