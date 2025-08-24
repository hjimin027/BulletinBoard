package com.bulletin.board.user;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), 
            		userCreateForm.getEmail(), userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/menu")
    public String menu() {
    	return "menu_main";
    }
    
    @GetMapping("/menu/myinfo")
    public String myInfo(Model model, Principal principal) {
    	String username = principal.getName();
    	SiteUser user = userService.getUser(username);
    	model.addAttribute("username", user.getUsername());
    	model.addAttribute("email", user.getEmail());
    	return "menu_myinfo";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/password")
    public String passwordForm(Model model) {
    	model.addAttribute("form", new PasswordChangeForm());
    	return "menu_password";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public String changePassword(@Valid @ModelAttribute("form") PasswordChangeForm form,
    		BindingResult bindingResult, Principal principal, Model model) {
    	//새 비밀번호 확인
    	if (!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
    		bindingResult.rejectValue("newPasswordConfirm", "password.mismatch", "새 비밀번호와 일치하지 않습니다.");
        	return "menu_password";
    	}
    	//현재 비밀번호와 새 비밀번호 동일 여부
    	if (form.getCurrentPassword().equals(form.getNewPassword())) {
    		bindingResult.rejectValue("newPassword", "password.same", "입력하신 현재 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
    		return "menu_password";
    	}
    	
    	//에러
    	if (bindingResult.hasErrors()) { return "menu_password"; }
    	
    	try {
    		userService.changePassword(principal.getName(), form.getCurrentPassword(), form.getNewPassword());
    	} catch (IllegalArgumentException e) {
    		bindingResult.reject("currentPasswordInvalid", "현재 비밀번호가 올바르지 않습니다.");
    		return "menu_password";
    	} catch (Exception e) {
    		bindingResult.reject("passwordChangeFailed", "비밀번호 변경 중 오류가 발생했습니다.");
    		return "menu_password";
    	}
    	
    	//성공
    	model.addAttribute("success", true);
    	return "menu_password";
    }
}
