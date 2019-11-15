package pl.taskyers.taskybase.core.roles.slo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.taskyers.taskybase.core.roles.entity.RoleEntity;
import pl.taskyers.taskybase.core.roles.entity.RoleLinkerEntity;
import pl.taskyers.taskybase.core.roles.repository.RoleLinkerRepository;
import pl.taskyers.taskybase.core.roles.repository.RoleRepository;
import pl.taskyers.taskybase.core.users.entity.UserEntity;
import pl.taskyers.taskybase.project.entity.ProjectEntity;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RoleSLOImpl implements RoleSLO {
    
    private final RoleRepository roleRepository;
    
    private final RoleLinkerRepository roleLinkerRepository;
    
    @Override
    public void createAllRolesForUser(UserEntity userEntity, ProjectEntity projectEntity) {
        createLinker(userEntity, projectEntity, false);
    }
    
    @Override
    public void createAllRolesForOwner(UserEntity userEntity, ProjectEntity projectEntity) {
        createLinker(userEntity, projectEntity, true);
    }
    
    @Override
    public void setRole(UserEntity userEntity, ProjectEntity projectEntity, RoleEntity roleEntity, boolean value) {
        Optional<RoleLinkerEntity> roleLinkerEntityOptional = roleLinkerRepository.findByUserAndProjectAndRole(userEntity, projectEntity, roleEntity);
        if ( roleLinkerEntityOptional.isPresent() ) {
            RoleLinkerEntity roleLinkerEntity = roleLinkerEntityOptional.get();
            roleLinkerEntity.setChecked(value);
            roleLinkerRepository.save(roleLinkerEntity);
        }
    }
    
    @Override
    public boolean hasPermission(UserEntity userEntity, ProjectEntity projectEntity, String role) {
        Optional<RoleLinkerEntity> roleLinkerEntity = roleLinkerRepository.findByUserAndProjectAndRole_Key(userEntity, projectEntity, role);
        if ( roleLinkerEntity.isPresent() ) {
            return roleLinkerEntity.get().isChecked();
        }
        log.warn("Role linker not found, returning false");
        return false;
    }
    
    @Override
    public Optional<RoleEntity> getEntityByKey(String key) {
        return roleRepository.findByKey(key);
    }
    
    @Override
    public List<RoleLinkerEntity> getRoleLinkersByUserAndProject(UserEntity userEntity, ProjectEntity projectEntity) {
        return roleLinkerRepository.findAllByUserAndProject(userEntity, projectEntity);
    }
    
    private void createLinker(UserEntity userEntity, ProjectEntity projectEntity, boolean checked) {
        List<RoleEntity> allRoles = roleRepository.findAll();
        for ( RoleEntity roleEntity : allRoles ) {
            createNewRoleLinker(userEntity, projectEntity, roleEntity, checked);
        }
    }
    
    private void createNewRoleLinker(UserEntity userEntity, ProjectEntity projectEntity, RoleEntity roleEntity, boolean checked) {
        RoleLinkerEntity roleLinkerEntity = new RoleLinkerEntity();
        roleLinkerEntity.setUser(userEntity);
        roleLinkerEntity.setProject(projectEntity);
        roleLinkerEntity.setRole(roleEntity);
        roleLinkerEntity.setChecked(checked);
        roleLinkerRepository.save(roleLinkerEntity);
    }
    
}
