using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace MyWeb
{
    public partial class GetParkData : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        //事件程序 前端瀏覽器按鈕觸發之後 進行post back 物件來過 引發按鈕事件
        //delegate EventHandler結構
        protected async void btnGetParking_Click(object sender, EventArgs e)
        {
            if(sender is Button)
            {
                //回應一個資訊response
                //HttpResponse response=this.Response;
                //寫出文字到瀏覽器去
                //response.Write("按鈕被觸發..."+((Button)sender).ID);
            }
            //如何採用HTTP Client GET傳送方式 指向一個端點 去要一個停車場最後紀錄??
            HttpClient client= new HttpClient();
            //帶金鑰apikey(CK)
            System.Net.Http.Headers.HttpRequestHeaders headers=client.DefaultRequestHeaders;
            //使用Http Header帶一把金鑰
            headers.Add("CK", Models.AppUtility.getKey("ck"));
            //HttpResponse base on Http
            HttpResponseMessage response=await client.GetAsync(Models.AppUtility.getKey("park01"));
            if (response.IsSuccessStatusCode)
            {
                //txtStatus.Text = response.StatusCode.ToString();
               String json= await response.Content.ReadAsStringAsync(); //回應一個Task<String> 等待callback
                //txtStatus.Text = json; //純文字
                //純文字Json format 轉換成Json Object?
                Object obj=Newtonsoft.Json.JsonConvert.DeserializeObject(json);
                Newtonsoft.Json.Linq.JObject jsonObject = obj as Newtonsoft.Json.Linq.JObject;
                Newtonsoft.Json.Linq.JArray  jsonarray= jsonObject.GetValue("value") as Newtonsoft.Json.Linq.JArray;
                txtStatus.Text=jsonarray[0].ToString(); //jsonarray[0] 內含一個Collection物件 使用[indexer] 索引子

            }
          

        }
    }
}