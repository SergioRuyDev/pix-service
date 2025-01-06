package com.sergioruy.api;

import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.records.Pix;
import com.sergioruy.model.records.Typableline;
import com.sergioruy.service.DictService;
import com.sergioruy.service.PixService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.io.IOException;
import java.util.Objects;

@Path("/v1/pix")
public class PixResource {

    private final DictService dictService;
    private final PixService pixService;

    public PixResource(DictService dictService, PixService pixService) {
        this.dictService = dictService;
        this.pixService = pixService;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("line-legal-person")
    @POST
    @Operation(description = "API for creates a Pix Typableline from cpnj.")
    @APIResponseSchema(Typableline.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200",  description = "Return OK"),
            @APIResponse(responseCode = "201",  description = "Return OK with created transaction."),
            @APIResponse(responseCode = "401", description = "Authenticate error from API."),
            @APIResponse(responseCode = "403", description = "Authorization error from API."),
            @APIResponse(responseCode = "404", description = "Resource Not Found."),
    })
    public Response generateTypablelineWithLegalPersonDocument(final Pix pix) {
        var pixKey = dictService.findKeyByLegalPersonDocument(pix.key());

        if (Objects.nonNull(pixKey)) {
            return Response.ok(pixService.generateTypableline(pixKey, pix.value(), pix.originCity())).build();
        }

        return null;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("line-natural-person")
    @POST
    @Operation(description = "API for creates a Pix Typableline from cpf.")
    @APIResponseSchema(Typableline.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200",  description = "Return OK"),
            @APIResponse(responseCode = "201",  description = "Return OK with created transaction."),
            @APIResponse(responseCode = "401", description = "Authenticate error from API."),
            @APIResponse(responseCode = "403", description = "Authorization error from API."),
            @APIResponse(responseCode = "404", description = "Resource Not Found."),
    })
    public Response generateTypablelineWithNaturalPersonDocument(final Pix pix) {
        var chave = dictService.findKeyByNaturalPersonDocument(pix.key());

        if (Objects.nonNull(chave)) {
            return Response.ok(pixService.generateTypableline(chave, pix.value(), pix.originCity())).build();
        }

        return null;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}")
    @GET
    @Operation(description = "API responsible for find pix payment")
    @APIResponseSchema(PixTransaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200",  description = "Return OK"),
            @APIResponse(responseCode = "201",  description = "Return OK with created transaction."),
            @APIResponse(responseCode = "401", description = "Authenticate error from API."),
            @APIResponse(responseCode = "403", description = "Authorization error from API."),
            @APIResponse(responseCode = "404", description = "Resource Not Found."),
    })
    public Response searchPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.findById(uuid)).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}/approve")
    @PATCH
    @Operation(description = "API responsible for approve pix payment")
    @APIResponseSchema(PixTransaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200",  description = "Return OK"),
            @APIResponse(responseCode = "201",  description = "Return OK with created transaction."),
            @APIResponse(responseCode = "401", description = "Authenticate error from API."),
            @APIResponse(responseCode = "403", description = "Authorization error from API."),
            @APIResponse(responseCode = "404", description = "Resource Not Found."),
    })
    public Response approvePix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.approvePixTransaction(uuid).get()).build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{uuid}/reject")
    @PATCH
    @Operation(description = "API responsible for reject pix payment")
    @APIResponseSchema(PixTransaction.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200",  description = "Return OK"),
            @APIResponse(responseCode = "201",  description = "Return OK with created transaction."),
            @APIResponse(responseCode = "401", description = "Authenticate error from API."),
            @APIResponse(responseCode = "403", description = "Authorization error from API."),
            @APIResponse(responseCode = "404", description = "Resource Not Found."),
    })
    public Response rejectPix(@PathParam("uuid") String uuid) {

        return Response.ok(pixService.rejectPixTransaction(uuid).get()).build();
    }


    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    @Path("qrcode/{uuid}")
    @GET
    @Operation(description = "API for search for a QRCode from a specific UUID")
    @APIResponseSchema(Response.class)
    @APIResponses(value = {
            @APIResponse(responseCode = "200",  description = "Return OK"),
            @APIResponse(responseCode = "201",  description = "Return OK with created transaction."),
            @APIResponse(responseCode = "401", description = "Authenticate error from API."),
            @APIResponse(responseCode = "403", description = "Authorization error from API."),
            @APIResponse(responseCode = "404", description = "Resource Not Found."),
    })
    public Response generateQRCode(@PathParam("uuid") String uuid) throws IOException {
        // todo adicionar controle de exceptions
        return Response.ok(pixService.generateQrCode(uuid)).build();
    }
}
