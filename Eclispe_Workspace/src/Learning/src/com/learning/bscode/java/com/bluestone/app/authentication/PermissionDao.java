package com.bluestone.app.authentication;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Permission;
import com.bluestone.app.core.dao.BaseDao;

@Repository("permissionDao")
public class PermissionDao extends BaseDao {
    
    public List<Permission> getEnabledPermissionListByEntityName(String entity) {
        Query getNonDeletedPermissionListByEntityName = getEntityManagerForActiveEntities().createNamedQuery("permission.getPermissionsListByEntityName");
        getNonDeletedPermissionListByEntityName.setParameter("entity", entity);
        return getNonDeletedPermissionListByEntityName.getResultList();
    }
    
    public List<String> getPermissionsList(boolean forActive) {
        EntityManager entityManager = null;
        if(forActive) {
            entityManager = getEntityManagerForActiveEntities();
        } else {
            entityManager = getEntityManagerForNonActiveEntities();
        }
        Query getPermissionsListQuery = entityManager.createNamedQuery("permission.getAllDistinctPermissionEntityList");
        return getPermissionsListQuery.getResultList();
    }
    
    public Permission getSinglePermissionByEntityName(String entity) {
        Query getSinglePermissionByEntityNameQuery = getEntityManagerForActiveEntities().createNamedQuery("permission.getSinglePermissionByEntityName");
        getSinglePermissionByEntityNameQuery.setParameter("entity", entity);
        getSinglePermissionByEntityNameQuery.setMaxResults(1);
        return (Permission) getSinglePermissionByEntityNameQuery.getSingleResult();
    }
    
    public List<Permission> getPermissionsListByEntityName(String entity) {
        Query getPermissionsListByEntityNameQuery = getEntityManagerForActiveEntities().createNamedQuery("permission.getPermissionsListByEntityName");
        getPermissionsListByEntityNameQuery.setParameter("entity", entity);
        return getPermissionsListByEntityNameQuery.getResultList();
        
    }
    
    public void updatePermissionsByEntityName(String entity, String oldEntityValue) {
        Query updatePermissionsByEntityNameQuery = getEntityManagerForActiveEntities().createNamedQuery("permission.updatePermissionsByEntityName");
        oldEntityValue = oldEntityValue.trim();
        updatePermissionsByEntityNameQuery.setParameter("entity", entity);
        updatePermissionsByEntityNameQuery.setParameter("oldEntityValue", oldEntityValue);
        updatePermissionsByEntityNameQuery.executeUpdate();
    }
    
    public void markPermissionAsEnabled(String entity) {
        Query markPermissionAsEnabledQuery = getEntityManagerForNonActiveEntities().createNamedQuery("permission.markPermissionAsEnabled");
        markPermissionAsEnabledQuery.setParameter("entity", entity);
        markPermissionAsEnabledQuery.executeUpdate();
    }
    
    public void markPermissionAsDisabled(String entity) {
        Query markPermissionAsDisabledQuery = getEntityManagerForActiveEntities().createNamedQuery("permission.markPermissionAsDisabled");
        markPermissionAsDisabledQuery.setParameter("entity", entity);
        markPermissionAsDisabledQuery.executeUpdate();
    }
    
    public List<Permission> getPermissionListByEntityNamesAndTypes(String entityName, Set<String> typesSet) {
        Query getPermissionListByEntityNamesAndTypesQuery = getEntityManagerForActiveEntities().createNamedQuery("permission.getPermissionsListByEntityNameAndTypes");
        getPermissionListByEntityNamesAndTypesQuery.setParameter("entityName", entityName);
        getPermissionListByEntityNamesAndTypesQuery.setParameter("typesSet", typesSet);
        return getPermissionListByEntityNamesAndTypesQuery.getResultList();
    }
}
