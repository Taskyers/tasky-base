package pl.taskyers.taskybase.task;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ResolutionType {
    
    DONE("Done"),
    FIXED("Fixed"),
    DUPLICATED("Duplicated"),
    NO_ACTION_REQUIRED("No action required"),
    WONT_FIX("Won't fix");
    
    private final String value;
    
    @Override
    public String toString() {
        return value;
    }
    
}
