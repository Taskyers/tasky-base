package pl.taskyers.taskybase.core.emails;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {
    
    private String email;
    
    private String name;
    
    private String surname;
    
}
