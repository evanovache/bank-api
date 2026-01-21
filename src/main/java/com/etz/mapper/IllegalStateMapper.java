package com.etz.mapper;

import com.etz.controller.response.ErrorResponse;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;


@Provider
public class IllegalStateMapper
    implements ExceptionMapper<IllegalStateException> {
    
        @Override
        public Response toResponse(IllegalStateException ex) {
            return Response.status(Response.Status.CONFLICT)
                   .entity(new ErrorResponse(ex.getMessage()))
                   .build();
        }
}
