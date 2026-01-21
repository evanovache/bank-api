package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class IllegalArgumentMapper  
    implements ExceptionMapper<IllegalArgumentException> {
    
    @Override
    public Response toResponse(IllegalArgumentException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(new ErrorResponse(ex.getMessage()))
                       .build();
    }
}
