package com.paymybuddy.security.oauth2.user;

import java.util.Collection;
import java.util.Map;

import com.paymybuddy.security.oauth2.user.user_info.OAuth2UserInfo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;

/**
 * Class that represent your CustomOauth2User after his authentication and filters of
 * spriongSecurity. It's {@link com.paymybuddy.security.oauth2.services.CustomOAuth2UserService}
 * that create entity from request of OAuth2login.
 */
public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;

    private OAuth2UserInfo oAuth2UserInfo;

    public CustomOAuth2User(OAuth2User oAuth2User, OAuth2UserInfo oAuth2UserInfo) {
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getFullName();
    }

    public String getLastName() {
        return StringUtils.capitalize(oAuth2UserInfo.getLastName());
    }

    public String getFirstName() {
        return StringUtils.capitalize(oAuth2UserInfo.getFirstName());
    }

    public String getClientRegistrationId() {
        return oAuth2UserInfo.getRegistrationId();
    }

    public String getEmail() {
        return oAuth2UserInfo.getEmail();
    }

    public String getClientId() {
        return oAuth2UserInfo.getClientId();
    }

    @Override
    public String toString() {
        return "CustomOAuth2User: \n [oAuth2User=" + oAuth2User + ",\n oAuth2UserInfo=" + oAuth2UserInfo + "]";
    }

}
