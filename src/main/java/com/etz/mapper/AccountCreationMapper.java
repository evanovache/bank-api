package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;
import com.etz.exception.AccountCreationException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class AccountCreationMapper 
    implements ExceptionMapper<AccountCreationException> {
        
        @Override
        public Response toResponse(AccountCreationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorResponse(e.getMessage()))
                           .build();
        }
}
