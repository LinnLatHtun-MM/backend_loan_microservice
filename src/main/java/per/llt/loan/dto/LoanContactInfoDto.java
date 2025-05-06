package per.llt.loan.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * ConfigurationProperties Usage need to add this annotation in LoanApplication
 *
 * @EnableConfigurationProperties(value = {LoanContactInfoDto.class})
 **/
@ConfigurationProperties(prefix = "loans")
@Getter
@Setter
public class LoanContactInfoDto {
    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}
