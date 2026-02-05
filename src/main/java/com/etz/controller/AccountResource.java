package com.etz.controller;

import java.util.List;

import com.etz.controller.response.BalanceResponse;
import com.etz.dao.AccountDAO;
import com.etz.dto.MiniStatementRequest;
import com.etz.dto.PinRequest;
import com.etz.dto.TransactionRequest;
import com.etz.model.Account;
import com.etz.model.Transaction;
import com.etz.service.BankService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    
    @Inject 
    private BankService bankService;

    @Inject 
    private AccountDAO accountDAO;


    @POST
    @Path("/{accountNumber}/balance")
    public Response getBalance(
        @PathParam("accountNumber") long accountNumber,
        PinRequest request
    ) {
        double balance = bankService.getBalance(accountNumber, request.getPin());
        return Response.status(Response.Status.OK)
                       .entity(new BalanceResponse(balance))
                       .build();
    }


    @POST 
    @Path("/{accountNumber}/deposit")
    public Response deposit(
        @PathParam("accountNumber") long accountNumber,
        TransactionRequest request
    ) {
        bankService.deposit(
            accountNumber, 
            request.getPin(),
            request.getAmount());
        double balance = bankService.getBalance(accountNumber, request.getPin());
        return Response.status(Response.Status.OK)
                       .entity(new BalanceResponse(balance))
                       .build();
    }


    @POST
    @Path("/{accountNumber}/withdraw")
    public Response withdraw(
        @PathParam("accountNumber") long accountNumber,
        TransactionRequest request
    ) {
        bankService.withdraw(
            accountNumber, 
            request.getPin(), 
            request.getAmount());
        double balance = bankService.getBalance(accountNumber, request.getPin());
        return Response.status(Response.Status.OK)
                       .entity(new BalanceResponse(balance))
                       .build();
    }


    @POST
    @Path("/{accountNumber}/transactions")
    public Response recentTransactions(
        @PathParam("accountNumber") long accountNumber,
        MiniStatementRequest request
    ) {
        List<Transaction> transaction = bankService.getRecentTransactions(accountNumber, request.getPin(), request.getLimit());
        return Response.ok(transaction).build();
    }

    @POST
    @Path("/{accountNumber}/validate")
    public Response validatePin(
        @PathParam("accountNumber") long accountNumber,
        PinRequest req
    ) {
        Account account = accountDAO.findByAccountNumber(accountNumber);
        bankService.validatePin(account, req.getPin());
        return Response.ok().build();
    }
}
