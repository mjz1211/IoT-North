using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MyWeb.Models
{
    public class AppUtility
    {
        //共用的功能 傳遞一個key 取出內容appSettings 
        public static String getKey(String keyName)
        {
            return System.Web.Configuration.WebConfigurationManager.AppSettings[keyName];
        }
    }
}