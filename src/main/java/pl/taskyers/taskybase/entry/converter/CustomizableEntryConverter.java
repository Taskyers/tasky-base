package pl.taskyers.taskybase.entry.converter;

import pl.taskyers.taskybase.entry.EntryType;
import pl.taskyers.taskybase.entry.dto.CustomizableEntryDTO;
import pl.taskyers.taskybase.entry.entity.EntryEntity;

public class CustomizableEntryConverter {
    
    private CustomizableEntryConverter() {
    }
    
    public static CustomizableEntryDTO convertEntryStatusToDTO(EntryEntity entryEntity) {
        return new CustomizableEntryDTO(entryEntity.getId(), entryEntity.getValue(), entryEntity.getTextColor(),
                entryEntity.getBackgroundColor(), entryEntity.getEntryType()
                .name());
    }
    
    public static EntryEntity convertEntryStatusFromDTO(CustomizableEntryDTO customizableEntryDTO) {
        EntryEntity entryEntity = new EntryEntity();
        entryEntity.setValue(customizableEntryDTO.getValue());
        entryEntity.setTextColor(customizableEntryDTO.getTextColor());
        entryEntity.setBackgroundColor(customizableEntryDTO.getBackgroundColor());
        entryEntity.setEntryType(checkEntryType(customizableEntryDTO.getEntryType()));
        return entryEntity;
    }
    
    public static EntryType checkEntryType(String type) {
        for ( EntryType entryType : EntryType.values() ) {
            if ( entryType.name()
                    .equals(type) ) {
                return EntryType.valueOf(type);
            }
        }
        return null;
    }
    
}
