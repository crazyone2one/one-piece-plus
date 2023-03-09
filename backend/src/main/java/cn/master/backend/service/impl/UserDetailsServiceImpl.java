package cn.master.backend.service.impl;

import cn.master.backend.entity.SysUser;
import cn.master.backend.mapper.SysUserGroupMapper;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author create by 11's papa on 2022/12/27-14:40
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final SysUserMapper sysUserMapper;
    final SysUserGroupMapper sysUserGroupMapper;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.findByName(username);
        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户名错误/不存在");
        }
        SecurityUser user = new SecurityUser(sysUser.getName(),
                sysUser.getPassword(),
                getAuthorities(sysUser),
                sysUser.getLastProjectId(),
                sysUser.getLastWorkspaceId());
        user.setUserId(sysUser.getId());
        return user;
    }

    /**
     * desc: 获取用户的角色权限
     *
     * @param sysUser 当前用户信息
     * @return java.util.Collection<org.springframework.security.core.GrantedAuthority>
     */
    private Collection<GrantedAuthority> getAuthorities(SysUser sysUser) {
        List<String> groups = sysUserGroupMapper.getUserGroupByUserId((sysUser.getId()));
        List<GrantedAuthority> authList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groups)) {
            for (String group : groups) {
                authList.add(new SimpleGrantedAuthority(group));
            }
        }
        return authList;
    }
}
