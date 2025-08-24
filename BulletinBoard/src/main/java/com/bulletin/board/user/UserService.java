package com.bulletin.board.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bulletin.board.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
    
    public SiteUser getUser(String username) {
    	Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
    	if (siteUser.isPresent()) {
    		return siteUser.get();
    	} else {
    		throw new DataNotFoundException("siteuser not found");
    	}
    }

	public void changePassword(String username, String currentPassword, String newPassword) {
		SiteUser user = userRepository.findByusername(username)
				.orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
		
		//현재 비밀번호 확인
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		this.userRepository.save(user);
	}
    
    
}
