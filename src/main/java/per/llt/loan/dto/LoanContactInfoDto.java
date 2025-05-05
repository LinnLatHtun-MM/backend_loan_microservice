package per.llt.loan.dto;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * ConfigurationProperties Usage need to add this annotation in LoanApplication
 *
 * @EnableConfigurationProperties(value = {LoanContactInfoDto.class})
 **/
@ConfigurationProperties(prefix = "loans")
public record LoanContactInfoDto(
        String message, Map<String, String> contactDetails, List<String> onCallSupport) {

}
