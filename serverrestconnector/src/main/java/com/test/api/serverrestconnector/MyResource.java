package com.test.api.serverrestconnector;

import java.sql.Connection;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.test.api.serverrestconnector.model.Employee;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
	
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     * @throws Exception 
     */
    @GET
    @Path("getdata")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws Exception {
    	
    	Connection conn = DbManager.connectToDb();
    	StringBuffer emp = DbManager.readDataFromDb(conn);
    	DbManager.closeDbConnection(conn);
        return emp.toString();
    }
    
    
    /**
     * Method handling HTTP POST requests. The data is accepted in URLENCODED format.
     * 
     * @return Returns status.
     * @throws Exception 
     */
    @POST
    @Path("setdata")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setIt(@FormParam("emp_id")int id,@FormParam("name") String name) throws Exception{
    	Connection conn = DbManager.connectToDb();
    	Employee emp = new Employee(id,name);
    	DbManager.insertDataToDb(emp,conn);
    	DbManager.closeDbConnection(conn);
    	
    	return Response.status(200).entity("Successfull").build();
    }
}
