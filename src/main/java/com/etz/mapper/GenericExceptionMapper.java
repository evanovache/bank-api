package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper 
    implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(new ErrorResponse("Internal server error"))
                       .build();
    }
}
