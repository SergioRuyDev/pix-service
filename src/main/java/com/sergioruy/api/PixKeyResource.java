package com.sergioruy.api;

import com.sergioruy.service.DictService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/pix-keys")
public class PixKeyResource {

    private final DictService dictService;

    public PixKeyResource(DictService dictService) {
        this.dictService = dictService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{key}")
    public Response getCachedKey(@PathParam("key") String key) {
        var cachedKeys = dictService.findPixKeyDetailsOnCache(key);
        return Response.ok(cachedKeys).build();
    }
}
