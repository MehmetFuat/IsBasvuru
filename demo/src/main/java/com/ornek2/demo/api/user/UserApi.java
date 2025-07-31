package com.ornek2.demo.api.user;

import com.ornek2.demo.model.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "PdfGeneration", description = "PDF Generation Management API")
public interface UserApi
{
    String PATH = "users";

    @Operation(
            operationId = "generatePdf",
            summary = "Generate PDF document",
            description = "This operation generates a PDF document based on the provided request.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json")
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    })
            }
    )
    @PostMapping(value = PATH + "/register", consumes = {"multipart/form-data"})
    public String registerUser(
            @RequestPart("user") UserRequest userRequest,
            @RequestPart("cv") MultipartFile cvFile
    );


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
    @GetMapping(value = PATH + "/pending")
    public List<UserRequest> getPendingUsers();

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
    @PostMapping(value = PATH + "/approve")
    public String approveUser(@RequestParam String email, @RequestParam(required = false) String note);

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
    @PostMapping(value = PATH + "/reject")
    public String rejectUser(@RequestParam String email);

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
    @GetMapping(value = PATH + "/approved")
    public List<UserRequest> getApprovedUsers();

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
    @GetMapping(value = PATH + "/rejected")
    public List<UserRequest> getRejectedUsers();

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
    @GetMapping(value = PATH + "/check-status")
    public UserRequest checkStatus(@RequestParam String code);

}
