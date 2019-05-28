package com.hwx.model;

public class SysRole {
    private Integer id;

    private String roleName;

    private Byte avilable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Byte getAvilable() {
        return avilable;
    }

    public void setAvilable(Byte avilable) {
        this.avilable = avilable;
    }
}