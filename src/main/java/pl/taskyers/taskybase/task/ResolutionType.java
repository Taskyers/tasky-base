package pl.taskyers.taskybase.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ResolutionType {
    
    DONE("Done"),
    FIXED("Fixed"),
    DUPLICATED("Duplicated"),
    NO_ACTION_REQUIRED("No action required"),
    WONT_FIX("Won't fix");
    
    private final String value;
    
    public static ResolutionType getByValue(String value) {
        return Arrays.stream(ResolutionType.values()).filter(type -> type.getValue().equals(value)).findFirst().orElse(null);
    }
    
    @Override
    public String toString() {
        return value;
    }
    
}
