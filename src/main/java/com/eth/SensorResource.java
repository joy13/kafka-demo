package com.eth;

import com.eth.model.Message;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Root resource (exposed at "SensorResource" path)
 */
@Path("/sensor")
public class SensorResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return Response object.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorData() {
        List<Message> messages = new ArrayList<>();

        //Reading from file now, but should call the service layer here to read from database.
        try (Stream<String> stream = Files.lines(Paths.get("/Users/UdayanMac2013/msg.txt"))) {
            stream.forEach(line -> {
                String[] parts = line.split(",");
                Message m = new Message(Integer.valueOf(parts[0]), parts[1]);
                messages.add(m);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Quick fix for CORS issue
        GenericEntity<List<Message>> list = new GenericEntity<List<Message>>(messages){};
        return Response.status(Response.Status.OK)
                .entity(list)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }
}