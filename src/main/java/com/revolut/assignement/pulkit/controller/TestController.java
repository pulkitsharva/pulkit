package com.revolut.assignement.pulkit.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TestController {

  @GET
  public Response test(){
    return Response.ok().build();
  }
}
