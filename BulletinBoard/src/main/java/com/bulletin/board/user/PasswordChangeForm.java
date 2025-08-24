package com.bulletin.board.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeForm {
	@NotEmpty(message = "현재 비밀번호를 입력하세요")
	private String currentPassword;
	
	@NotEmpty(message = "새 비밀번호를 입력하세요")
	private String newPassword;
	
	@NotEmpty(message = "새 비밀번호 확인을 입력하세요")
	private String newPasswordConfirm;
}
