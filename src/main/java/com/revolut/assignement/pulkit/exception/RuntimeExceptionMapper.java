package com.revolut.assignement.pulkit.exception;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.eclipse.jetty.http.HttpStatus;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException runtime) {
    Map<String, Object> map = new HashMap<>();
    map.put("code", HttpStatus.INTERNAL_SERVER_ERROR_500);
    map.put("message", "Something went wrong");
    return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(map).build();
  }


}