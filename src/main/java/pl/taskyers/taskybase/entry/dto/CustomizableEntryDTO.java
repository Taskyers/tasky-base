package pl.taskyers.taskybase.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizableEntryDTO {
    
    private Long id;
    
    private String value;
    
    private String textColor;
    
    private String backgroundColor;
    
    private String entryType;
    
}
