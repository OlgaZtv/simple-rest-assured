package tests.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Integer id;
    private String email;
    private String password;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("job")
    private String job;
}
