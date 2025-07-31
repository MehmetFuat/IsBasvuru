package com.ornek2.demo.api.backoffice;

import com.ornek2.demo.model.BackofficeTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PdfGeneration", description = "PDF Generation Management API")
public interface BackofficeApi
{

    String PATH = "backoffice";

    @Operation(
            operationId = "generatePdf",
            summary = "Generate PDF document",
            description = "This operation generates a PDF document based on the provided request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/pdf")
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @GetMapping(value = PATH)
    public List<BackofficeTask> getAllTasks();


    @Operation(
            operationId = "generatePdf",
            summary = "Generate PDF document",
            description = "This operation generates a PDF document based on the provided request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/pdf")
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping(value = PATH + "/approve/{taskId}")
    public void approve(@PathVariable String taskId);

    @Operation(
            operationId = "generatePdf",
            summary = "Generate PDF document",
            description = "This operation generates a PDF document based on the provided request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/pdf")
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping(value = PATH + "/reject/{taskId}")
    public void reject(@PathVariable String taskId);


}
