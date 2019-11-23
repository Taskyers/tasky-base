package pl.taskyers.taskybase.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizableEntryDTO {
    
    private Long id;
    
    private String value;
    
    private String textColor;
    
    private String backgroundColor;
    
}
