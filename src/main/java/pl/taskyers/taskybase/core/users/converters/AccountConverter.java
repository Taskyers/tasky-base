package pl.taskyers.taskybase.core.users.converters;

import pl.taskyers.taskybase.core.users.dto.AccountDTO;
import pl.taskyers.taskybase.core.users.entity.UserEntity;

public class AccountConverter {
    
    public static UserEntity convertFromDTO(AccountDTO accountDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(accountDTO.getUsername());
        userEntity.setEmail(accountDTO.getEmail());
        userEntity.setPassword(accountDTO.getPassword());
        userEntity.setName(accountDTO.getName());
        userEntity.setSurname(accountDTO.getSurname());
        return userEntity;
    }
    
    public static AccountDTO convertToDTO(UserEntity userEntity) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUsername(userEntity.getUsername());
        accountDTO.setEmail(userEntity.getEmail());
        accountDTO.setPassword(userEntity.getPassword());
        accountDTO.setName(userEntity.getName());
        accountDTO.setSurname(userEntity.getSurname());
        return accountDTO;
    }
    
}
