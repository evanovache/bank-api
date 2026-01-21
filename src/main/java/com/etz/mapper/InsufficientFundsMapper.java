package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;
import com.etz.exception.InsufficientFundsException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class InsufficientFundsMapper
    implements ExceptionMapper<InsufficientFundsException> {
    
    @Override
    public Response toResponse(InsufficientFundsException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(new ErrorResponse(ex.getMessage()))
                       .build();
    }
}
