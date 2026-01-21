package com.etz.controller;

import java.util.List;

import com.etz.dao.AccountDAO;
import com.etz.dao.UserDAO;
import com.etz.dto.CreateAccountRequest;
import com.etz.exception.InsufficientFundsException;
import com.etz.exception.UserAlreadyExistsException;
import com.etz.model.Account;
import com.etz.model.AccountType;
import com.etz.model.CurrentAccount;
import com.etz.model.SavingsAccount;
import com.etz.model.User;
import com.etz.security.PasswordUtil;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    private UserDAO userDAO;

    @Inject
    private AccountDAO accountDAO;


    @POST
    public Response createUser(User user) {

        if(userDAO.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User Already Exists");
        }

        user.setPassword(
            PasswordUtil.hash(user.getPassword())
        );

        long userId = userDAO.create(user);
        user.setUserId(userId);

        user.setPassword(null);

        return Response.status(Response.Status.CREATED)
                       .entity(user)
                       .build();
    }


    @POST
    @Path("/{userId}/accounts")
    public Response createAccount(
        @PathParam("userId") long userId,
        CreateAccountRequest request
     ) {

        if (request.getType() == null) {
            throw new IllegalArgumentException("Account type is required");
        }

        if (request.getInitialDeposit() < 50)
            throw new InsufficientFundsException("Initial deposit must be $50 or more.");

        Account account =  
            (request.getType() == AccountType.SAVINGS)
                ? new SavingsAccount()
                : new CurrentAccount();

        account.setUserId(userId);
        account.setAccountType(request.getType());
        account.setBalance(request.getInitialDeposit());
        account.setPin(request.getPin());

        long accountNumber = accountDAO.create(account);
        account.setAccountNumber(accountNumber);

        return Response.status(Response.Status.CREATED)
                       .entity(account)
                       .build();
     }

     @GET
     @Path("/{userId}/accounts")
     public Response listAccounts(@PathParam("userId") long userId) {

        List<Account> accounts = accountDAO.findByUserId(userId);
        return Response.ok(accounts).build();
     }
}
