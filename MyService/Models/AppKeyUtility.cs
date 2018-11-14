using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MyService.Models
{
    public class AppKeyUtility
    {
        //共用的方法 取得指定的key的值
        public static String getKeyValue(String keyName)
        {
            return System.Web.Configuration.WebConfigurationManager.AppSettings[keyName];
        }
    }
}