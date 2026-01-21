package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;
import com.etz.exception.UserAlreadyExistsException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class UserAlreadyExistsMapper 
    implements ExceptionMapper<UserAlreadyExistsException> {
    
    @Override
    public Response toResponse(UserAlreadyExistsException e) {
        
        return Response.status(Response.Status.CONFLICT)
                       .entity(new ErrorResponse(e.getMessage()))
                       .build();
    }
}
