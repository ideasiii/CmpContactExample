package api;

import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cmp.client.ContactClient;
import common.Controller;

@Path("/deidentify")
@Produces("application/json;charset=utf8")
public class Contact
{
    final private String strJSON =
            "{  \n" + "   \"file_info\":{  \n" + "      \"src_type\":0,\n" + "      \"path\":\"C" +
                    ":/Users/R16026/Git/DataAnonymization/data/input/bankSimData_8000000.csv\"," +
                    "\n" + "      \"encoding\":\"UTF-8\",\n" + "      \"header\":\"true\"\n" + " " +
                    "  },\n" + "   \"field_info\":[  \n" + "      {  \n" + "         " +
                    "\"column_name\":\"deposit\",\n" + "         \"column_type\":\"integer\",\n" + "         \"de_id_info\":[  \n" + "            {  \n" + "               \"method\":\"internalization\",\n" + "               \"target\":\"auto\",\n" + "               \"target_info\":{  \n" + "                  \"separate_number\":5\n" + "               }\n" + "            }\n" + "         ]\n" + "      },\n" + "      {  \n" + "         \"column_name\":\"income\",\n" + "         \"column_type\":\"integer\",\n" + "         \"de_id_info\":[  \n" + "            {  \n" + "               \"method\":\"internalization\",\n" + "               \"target\":\"auto\",\n" + "               \"target_info\":{  \n" + "                  \"separate_number\":5\n" + "               }\n" + "            }\n" + "         ]\n" + "      },\n" + "      {  \n" + "         \"column_name\":\"login_time\",\n" + "         \"column_type\":\"string\",\n" + "         \"de_id_info\":[  \n" + "            {  \n" + "               \"method\":\"upgrading\",\n" + "               \"target\":\"default\",\n" + "               \"target_info\":{  \n" + "                  \"transform_type\":\"default\",\n" + "                  \"before_date_format\":\"yyyy/mm/dd\",\n" + "                  \"after_date_format\":\"yyyy/mm\"\n" + "               }\n" + "            }\n" + "         ]\n" + "      },\n" + "      {  \n" + "         \"column_name\":\"consumption\",\n" + "         \"column_type\":\"string\",\n" + "         \"de_id_info\":[  \n" + "            {  \n" + "               \"method\":\"upgrading\",\n" + "               \"target\":\"default\",\n" + "               \"target_info\":{  \n" + "                  \"transform_type\":\"default\",\n" + "                  \"before_date_format\":\"yyyy-mm-dd\",\n" + "                  \"after_date_format\":\"yyyy/mm\"\n" + "               }\n" + "            }\n" + "         ]\n" + "      }\n" + "   ],\n" + "   \"finish\":{  \n" + "      \"callback\":\"ftp\",\n" + "      \"URL\":\"http://frontend/callback?file=ftp://ssss.ssss\"\n" + "   }\n" + "}";
    
    @GET
    public String contact(@QueryParam("ip") String strIP)
    {
        int nResult = 0;
        ContactClient contactClient = new ContactClient(strIP, 1414, false);
        if (-1 != contactClient.start())
        {
            JSONObject jsonWord = new JSONObject(strJSON);
            Controller.CMP_PACKET resp = new Controller.CMP_PACKET();
            nResult = contactClient.send(jsonWord, resp);
            contactClient.stop();
            if (-1 != nResult)
            {
                return "Socket Send OK, Receive:" + resp.cmpBody;
            }
        }
        return "cmp contact test Fail";
    }
    
    @POST
    public String deidentify(@QueryParam("ip") String strIP, @QueryParam("data") String strData)
    {
        int nResult = 0;
        JSONObject jresponse = new JSONObject();
        ContactClient contactClient = new ContactClient(strIP, 1414, false);
        if (-1 != contactClient.start())
        {
            JSONObject jsonData = new JSONObject(strData);
            System.out.println(strData);
            if(jsonData.isEmpty())
            {
                jresponse.put("code",ErrorCode.ERROR_JSON);
                jresponse.put("message","Invalid JSON Data");
            }
            else
            {
                Controller.CMP_PACKET resp = new Controller.CMP_PACKET();
                nResult = contactClient.send(jsonData, resp);
                contactClient.stop();
                if (-1 != nResult)
                {
                    return "Socket Send OK, Receive:" + resp.cmpBody;
                }
            }
        }
        else
        {
            jresponse.put("code", ErrorCode.ERROR_CONNECT);
            jresponse.put("message","Server connect fail");
        }
        return jresponse.toString();
    }
    
    @POST
    @Path("/status")
    public String status(@QueryParam("ip") String strIP, @QueryParam("data") String strData)
    {
        return "status";
    }
    
}
