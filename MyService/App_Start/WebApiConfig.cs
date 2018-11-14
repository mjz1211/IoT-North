using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;

namespace MyService
{
    public static class WebApiConfig
    {
        //共用 誰載入進行共用地圖配置???
        public static void Register(HttpConfiguration config)
        {
            // Web API 設定和服務

            // Web API 路由
            config.MapHttpAttributeRoutes();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "cht/{controller}/{action}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
        }
    }
}
