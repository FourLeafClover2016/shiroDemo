package com.hwx.config;

import com.hwx.dao.SysRoleMapper;
import com.hwx.dao.SysUserMapper;
import com.hwx.model.SysRole;
import com.hwx.model.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: Huawei Xie
 * @date: 2019/5/27
 */
public class CustomShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 认证成功后添加角色和权限
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser user = (SysUser)getAvailablePrincipal(principalCollection);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(user.getRoleId());
        Set<String> roles = new HashSet<>();
        roles.add(sysRole.getRoleName());
        info.setRoles(roles);
        return null;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (null == authenticationToken.getPrincipal()) {
            return null;
        }
        String userName = authenticationToken.getPrincipal().toString();
        if (null == userName) {
            return null;
        }

        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(sysUser.getUsername(), sysUser.getPasswd(), getName());
        return simpleAuthenticationInfo;
    }
}
