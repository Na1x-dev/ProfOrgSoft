package com.example.demo.controllers.user;

import com.example.demo.models.*;
import com.example.demo.security.SecurityService;


import com.example.demo.services.gender.GenderService;
import com.example.demo.services.meetingMinute.MeetingMinuteService;
import com.example.demo.services.phoneNumber.PhoneNumberService;
import com.example.demo.services.position.PositionService;
import com.example.demo.services.role.RoleService;
import com.example.demo.services.unionMember.UnionMemberService;
import com.example.demo.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private RoleService roleService;

    @Autowired
    private GenderService genderService;

    @Autowired
    private UnionMemberService unionMemberService;

    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private PositionService positionService;
    @Autowired
    private MeetingMinuteService meetingMinuteService;

    @GetMapping("/signUpPage/index")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        User user = new User();
        model.addAttribute("userForm", user);
        return "signUpPage/index";
    }

    @PostMapping("/signUpPage/index")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signUpPage/index";
        }
        userService.create(userForm);
//        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return "redirect:/mainPage/index";
    }

    @GetMapping("/logInPage/index")
    public String login(Model model, String error, String logout) {
        autoCreateRoles();
        autoRegisterAdmin();
        autoCreateGenders();
        autoCreateEmptyPosition();
        autoCreateEmptyParent();
        autoCreateEmptyMeetingMinute();
        autoCreateRetireePosition();
//        autoCreateEmptyUnionMember();
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "logInPage/index";
    }

    @GetMapping("/logout")
    public String logout(Model model, String error, String logout) {
        return "logInPage/index";
    }

    @GetMapping({"/"})
    public String startPage(Model model) {
        return "redirect:mainPage/index";
    }

    public void autoRegisterAdmin() {
        if (userService.findByUsername("admin") == null) {
            Role role = roleService.readByRoleName("ROLE_ADMIN");
            User admin = new User("admin", "admin", role, "admin");
            userService.create(admin);
            securityService.autoLogin(admin.getUsername(), admin.getPassword());
        }
    }

    public void autoCreateRoles() {
        if (roleService.readByRoleName("ROLE_ADMIN") == null) {
            Role role = new Role("ROLE_ADMIN");
            roleService.create(role);
        }
        if (roleService.readByRoleName("ROLE_USER") == null) {
            Role role = new Role("ROLE_USER");
            roleService.create(role);
        }
    }

    public void autoCreateGenders() {
        if (genderService.readByGenderTitle("Мужской") == null) {
            Gender gender = new Gender("Мужской");
            genderService.create(gender);
        }
        if (genderService.readByGenderTitle("Женский") == null) {
            Gender gender = new Gender("Женский");
            genderService.create(gender);
        }
    }

    public void autoCreateEmptyPosition() {
        if (positionService.readByTitle("") == null) {
            positionService.create(new Position());
        }
    }

    public void autoCreateEmptyUnionMember() {
        if (unionMemberService.readByName("") == null) {
            unionMemberService.create(new UnionMember());
        }
    }

    public void autoCreateRetireePosition() {
        if (positionService.readByTitle("Пенсионер") == null) {
            positionService.create(new Position("Пенсионер"));
        }
    }

    public void autoCreateEmptyMeetingMinute() {
        if (meetingMinuteService.readByMeetingMinuteNumber(0) == null) {
            meetingMinuteService.create(new MeetingMinute());
        }
    }

    public void autoCreateEmptyParent() {
        if (unionMemberService.readByName("") == null) {
            UnionMember unionMember = new UnionMember();
            unionMember.getPhoneNumbers().get(0).setUnionMember(unionMember);
            unionMember.setGender(genderService.readByGenderTitle("Мужской"));
            unionMember.setPosition(positionService.readByTitle(""));
            System.out.println(unionMember);
            unionMemberService.create(unionMember);
            phoneNumberService.create(unionMember.getPhoneNumbers().get(0));
        }
    }

    public void autoCreatePhoneNumber() {
        if (phoneNumberService.readById(0L) == null) {
        }
    }
}