package pl.taskyers.taskybase.dashboard.project.converter;

import pl.taskyers.taskybase.dashboard.project.dto.EntryDTO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;

public class EntryConverter {
    
    public static EntryDTO convertToDTO(EntryEntity entryEntity) {
        return new EntryDTO(entryEntity.getValue(), entryEntity.getTextColor(), entryEntity.getBackgroundColor());
    }
    
}
