package pl.taskyers.taskybase.core.emails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressDTO {
    
    private String email;
    
    private String name;
    
    private String surname;
    
}
