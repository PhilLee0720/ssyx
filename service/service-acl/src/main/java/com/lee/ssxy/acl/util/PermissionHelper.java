package com.lee.ssxy.acl.util;

import com.lee.ssxy.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {
    public static List<Permission> buildPermission(List<Permission> list){
        List<Permission> trees = new ArrayList<>();
        for (Permission permission : list) {
            if(permission.getPid() == 0){
                permission.setLevel(1);
                trees.add(findChildren(permission,list));
            }
        }
        return trees;
    }

    private static Permission findChildren(Permission permission, List<Permission> list) {
        permission.setChildren(new ArrayList<Permission>());
        for (Permission p : list) {
            if(p.getPid().longValue() == permission.getId().longValue()){
                int level  = permission.getLevel()+1;
                p.setLevel(level);
                permission.getChildren().add(findChildren(p,list));
            }
        }
        return permission;
    }
}
