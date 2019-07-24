package api;

import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cmp.client.ContactClient;
import cmp.client.Controller;


@Path("/contact")
@Produces("application/json;charset=utf8")
public class Contact
{
    @GET
    public String contact(@QueryParam("ip") String strIP)
    {
        int nResult = 0;
        ContactClient contactClient = new ContactClient(strIP, 2310, false);
        if (-1 != contactClient.start())
        {
            JSONObject jsonWord = new JSONObject();
            jsonWord.put("id", 1);
            jsonWord.put("type", 4);
            jsonWord.put("word", "try it");
            jsonWord.put("total", 0);
            jsonWord.put("number", 0);
            
            Controller.CMP_PACKET resp = new Controller.CMP_PACKET();
            nResult = contactClient.send(jsonWord, resp);
            contactClient.stop();
            if (-1 != nResult)
            {
                return resp.cmpBody;
            }
        }
        return "cmp contact test Fail";
    }
    
    @GET
    @Path("/{name}")
    public String service(@PathParam("name") String name)
    {
        return "Hello, " + name;
    }
}
