package se.BTH.webservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Role {
    @Id
    private String id;
    private RoleName name;

}
