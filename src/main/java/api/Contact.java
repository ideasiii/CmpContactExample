package api;

import org.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import api.cmp.client.ContactClient;
import api.common.Controller;

@Path("/client")
@Produces("application/json;charset=utf8")
public class Contact
{
    final int PORT = 1414;
    
    // example: http://127.0.0.1:8080/cmp/client/deidentify?ip=127.0.0.1&data={"key":"value"}
    @POST
    @Path("/deidentify")
    public String deidentify(@QueryParam("ip") String strIP, @QueryParam("data") String strData)
    {
        return send(strIP,strData,Controller.deidentify_request);
    }
    
    // example: http://127.0.0.1:8080/cmp/client/status?ip=127.0.0.1
    @POST
    @Path("/status")
    public String status(@QueryParam("ip") String strIP)
    {
       return send(strIP,null,Controller.status_request);
    }
    
    // example: http://127.0.0.1:8080/cmp/client/option?ip=127.0.0.1&data={"key":"value"}
    @POST
    @Path("/option")
    public String option(@QueryParam("ip") String strIP, @QueryParam("data") String strData)
    {
        return send(strIP,strData,Controller.option_request);
    }
    
    private String send(String strIP, String strData,int nCommand)
    {
        System.out.println("[Contact] send IP = " + strIP + " Data = " + strData + " Command = " + nCommand);
        JSONObject jresponse = new JSONObject();
        ContactClient contactClient = new ContactClient(strIP, PORT, false);
        JSONObject jsonData = null;
        if(null != strData && 0 < strData.length())
        {
            jsonData = new JSONObject(strData);
            System.out.println(strData);
        }
        
        try
        {
            if (-1 != contactClient.start())
            {
                Controller.CMP_PACKET resp = new Controller.CMP_PACKET();
                int nResult = 0;
                switch(nCommand)
                {
                    case Controller.deidentify_request:
                        if (null == jsonData || jsonData.isEmpty())
                        {
                            jresponse.put("code", ErrorCode.ERROR_JSON);
                            jresponse.put("message", "Invalid JSON Data");
                        }
                        else
                        {
                            nResult = contactClient.deidentify(jsonData, resp);
                        }
                        break;
                    case Controller.status_request:
                        nResult = contactClient.status(resp);
                        break;
                    case Controller.option_request:
                        if (null == jsonData || jsonData.isEmpty())
                        {
                            jresponse.put("code", ErrorCode.ERROR_JSON);
                            jresponse.put("message", "Invalid JSON Data");
                        }
                        else
                        {
                            nResult = contactClient.option(jsonData, resp);
                        }
                        break;
                }
                contactClient.stop();
                if (-1 != nResult)
                {
                    jresponse.put("code", ErrorCode.ERROR_SUCCESS);
                    jresponse.put("message", "Socket Send OK, Receive:" + resp.cmpBody);
                }
                else
                {
                    jresponse.put("code", ErrorCode.ERROR_SEND);
                    jresponse.put("message", "Socket Send Fail, Receive:" + resp.cmpBody);
                }
            }
            else
            {
                jresponse.put("code", ErrorCode.ERROR_CONNECT);
                jresponse.put("message", "Server connect fail");
            }
        }
        catch(Exception e)
        {
            jresponse.put("code", ErrorCode.ERROR_EXCEPTION);
            jresponse.put("message", e.getMessage());
        }
        
        return jresponse.toString();
    }
    
}
