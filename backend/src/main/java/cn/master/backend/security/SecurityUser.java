package cn.master.backend.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author create by 11's papa on 2022/12/27-14:36
 */
@Data
public class SecurityUser implements UserDetails {
    private String username;
    private String password;
    private String nickname;
    private String token;
    private String userId;
    private String lastProjectId;
    private String lastWorkspaceId;
    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities,String projectId,String workspaceId) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.lastProjectId = projectId;
        this.lastWorkspaceId = workspaceId;
    }
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
