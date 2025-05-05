package per.llt.loan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import per.llt.loan.constants.LoanConstants;
import per.llt.loan.dto.ErrorResponseDto;
import per.llt.loan.dto.LoanContactInfoDto;
import per.llt.loan.dto.LoansDto;
import per.llt.loan.dto.ResponseDto;
import per.llt.loan.service.ILoansService;

@Tag(name = "CRUD Rest APIs for Loan", description = "CREATE, FETCH, UPDATE, DELETE APIs for Loan")
@RequestMapping(value = "/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RestController
public class LoanController {


    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment env;

    @Autowired
    private LoanContactInfoDto loanContactInfoDto;

    @Autowired
    private ILoansService loansService;

    @Operation(summary = "Create a new loan", description = "Creation of new loan with mobile number")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Http Status Created!!"),
            @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        loansService.createLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(LoanConstants.STATUS_201, LoanConstants.MESSAGE_201));
    }

    @Operation(summary = "Fetch loan details", description = "Fetch loan information based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK!!"),
            @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        LoansDto loansDto = loansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }

    @Operation(summary = "Update loan details", description = "Update Customer's Loan Details based on a account number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK!!"),
            @ApiResponse(responseCode = "417", description = "Update failed!!"),
            @ApiResponse(responseCode = "500",
                    description = "Http Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = loansService.updateLoan(loansDto);
        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).
                    body(new ResponseDto(LoanConstants.STATUS_417, LoanConstants.MESSAGE_417_UPDATE));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
        }

    }

    @Operation(summary = "Delete Loan details", description = "Delete Customer's Loan Details based on a mobile number")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK!!"),
            @ApiResponse(responseCode = "417", description = "Delete failed!!"),
            @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error")})
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoan(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = loansService.deleteLoan(mobileNumber);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).
                    body(new ResponseDto(LoanConstants.STATUS_417, LoanConstants.MESSAGE_417_DELETE));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
        }
    }

    /**
     * @This is 3 types how to get values from application.yaml
     * 1. Using @Value Annotation /build-info
     * 2. Using Environment class /java-version
     * 3. Using @ConfigurationProperties and Record DTO /contact-info
     */
    @Operation(summary = "Get Build Information", description = "Get Build Information that is deployed into account microservice")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK!!"),
            @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    @Operation(summary = "Get JAVA version", description = "Get Java Version that is deployed into account microservice")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK!!"),
            @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(env.getProperty("JAVA_HOME"));
    }

    @Operation(summary = "Get Contact Info", description = "Get Contact Info when application reached out any issues")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK!!"),
            @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<LoanContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(loanContactInfoDto);
    }

}
