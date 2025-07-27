package org.example.Model;

import org.example.Constants.Role;

public class UserRoleInfo {
    private final Role role;
    private final String name;
    private final String password;

    public UserRoleInfo(Role role, String name, String password) {
        this.role = role;
        this.name = name;
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
