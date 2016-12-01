package com.rcolaco.boilerplate;

import com.amazonaws.util.IOUtils;
import main.java.com.rcolaco.boilerplate.model.WorkRequest;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 *
 */
public class Main
{
    public static HttpServer createServer()
    {
        return HttpServer.createSimpleServer("/gws", "localhost", 5000);
    }

    public static void main(String[] args) throws Throwable
    {
        final HttpServer server = createServer();

        server.getServerConfiguration().addHttpHandler(new HttpHandler(){
           @Override
           public void service(Request request, Response response) throws Exception
           {
               System.out.println("Received SQS message; processing...");
               try
               {
                   String s = IOUtils.toString(request.getInputStream());
                   //final WorkRequest wr = WorkRequest.fromJson(s);
                   System.out.println(s);
                   response.setStatus(200);
                   response.sendAcknowledgement();
               } catch (Throwable th)
               {
                   th.printStackTrace();
               }
           }
        }, "/");

        server.start();
        Thread.currentThread().join();
    }
}
