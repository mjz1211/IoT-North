using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
//封裝回傳回來的json object
//Entity Class
namespace MyService.Models
{
    //[SerializableAttribute] //配合實際規劃data field
    public class SensorData
    {
       
        //快速規劃屬性
        public String id { set; get; }
        public String deviceId { set; get; }
        public DateTime time { set; get; }
        public Double lon { set; get; }
        public Double lat { set; get; }
        public Object value { set; get; }
    }
}