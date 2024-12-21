package com.sergioruy.api;

import com.sergioruy.model.Pix;
import com.sergioruy.service.DictService;
import com.sergioruy.service.PixService;
import io.quarkus.logging.Log;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("line-legal-person")
    public Response generateTypablelineWithLegalPersonDocument(final Pix pix) {
        var pixKey = dictService.findKeyByLegalPersonDocument(pix.key());

        if (Objects.nonNull(pixKey)) {
            return Response.ok(pixService.generateTypableline(pixKey, pix.value(), pix.originCity())).build();
        }

        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("line-natural-person")
    public Response generateTypablelineWithNaturalPersonDocument(final Pix pix) {
        var chave = dictService.findKeyByNaturalPersonDocument(pix.key());

        if (Objects.nonNull(chave)) {
            return Response.ok(pixService.generateTypableline(chave, pix.value(), pix.originCity())).build();
        }

        return null;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("image/png")
    @Path("qrcode/{uuid}")
    public Response gerarQRCode(@PathParam("uuid") String uuid) throws IOException {
        // todo adicionar controle de exceptions
        return Response.ok(pixService.generateQrCode(uuid)).build();
    }
}
