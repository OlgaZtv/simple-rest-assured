package tests.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

@Data
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class LombokUserData {
    @JsonProperty("data")
    private User user;

   }
