package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;
import com.etz.exception.AccountNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AccountNotFoundMapper 
    implements ExceptionMapper<AccountNotFoundException> {
    
    @Override
    public Response toResponse(AccountNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                       .entity(new ErrorResponse(ex.getMessage()))
                       .build();
    }
}
