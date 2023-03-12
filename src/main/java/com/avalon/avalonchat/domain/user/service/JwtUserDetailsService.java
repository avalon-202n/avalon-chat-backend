package com.avalon.avalonchat.domain.user.service;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.dto.SecurityUser;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
		User findUser = userRepository.findByEmail(Email.of(email))
			.orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없었습니다."));
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
		return new SecurityUser(
			findUser.getId(),
			findUser.getEmail().getValue(),
			findUser.getPassword(),
			Collections.singleton(grantedAuthority)
		);
	}
}
