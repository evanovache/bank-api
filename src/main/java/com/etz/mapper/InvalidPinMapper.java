package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;
import com.etz.exception.InvalidPinException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class InvalidPinMapper 
    implements ExceptionMapper<InvalidPinException> {
    
    @Override
    public Response toResponse(InvalidPinException ex) {
        return Response.status(Response.Status.UNAUTHORIZED)
                       .entity(new ErrorResponse(ex.getMessage()))
                       .build();
    }
}
