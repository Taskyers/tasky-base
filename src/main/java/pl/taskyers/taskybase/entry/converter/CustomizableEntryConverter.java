package pl.taskyers.taskybase.entry.converter;

import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.StatusEntryEntity;

public class CustomizableEntryConverter {
    
    public static CustomizableEntryDTO convertEntryStatusToDTO(StatusEntryEntity statusEntryEntity) {
        return new CustomizableEntryDTO(statusEntryEntity.getId(), statusEntryEntity.getValue(), statusEntryEntity.getTextColor(),
                statusEntryEntity.getBackgroundColor());
    }
    
    public static StatusEntryEntity convertEntryStatusFromDTO(CustomizableEntryDTO customizableEntryDTO) {
        StatusEntryEntity statusEntryEntity = new StatusEntryEntity();
        statusEntryEntity.setValue(customizableEntryDTO.getValue());
        statusEntryEntity.setTextColor(customizableEntryDTO.getTextColor());
        statusEntryEntity.setBackgroundColor(customizableEntryDTO.getBackgroundColor());
        return statusEntryEntity;
    }
    
}
