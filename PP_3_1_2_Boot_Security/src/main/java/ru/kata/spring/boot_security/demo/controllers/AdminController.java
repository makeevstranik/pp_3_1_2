package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.MyUserDetailsService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.AdvanceInfo;
import ru.kata.spring.boot_security.demo.util.BasicInfo;
import ru.kata.spring.boot_security.demo.util.UserValidator;


@Controller
@RequestMapping("/admin")
public class AdminController {
    final private UserValidator userValidator;
    final private MyUserDetailsService userDetailsService;
    final private RoleService roleService;
    @Autowired
    public AdminController(UserValidator userValidator, MyUserDetailsService userDetailsService, RoleService roleService) {
        this.userValidator = userValidator;
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
    }

    @GetMapping
    public String indexAdmin(Model model) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("newUser", new User());
        model.addAttribute("rolesList", roleService.getAllRoles());
        model.addAttribute("authUser",  authUser);
        model.addAttribute("users",  userDetailsService.getAllUsers());
        return "/index";
    }

    // validate new password - AdvanceInfo
    @PostMapping("/create")
    public String crateUserFromForm(@ModelAttribute("user") @Validated({BasicInfo.class, AdvanceInfo.class}) User user,
                                    BindingResult bindingResult, Model model) { // get ready person from view
        userValidator.validate(user, bindingResult);
        // in case bad validation:
        if (bindingResult.hasErrors()) {
            model.addAttribute("err", bindingResult);
            return  "/error-update";
        }
        userDetailsService.registration(user);
        return "redirect:/admin/";
    }

    // dont validate password(encoded) in @Validated (empty if it was not changed) - BasicInfo
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Validated(BasicInfo.class) User user,
                         BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult); // inner validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("err", bindingResult);
            return  "/error-update";
        }
        userDetailsService.updateUser(user);
        return "redirect:/admin";
    }


    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        userDetailsService.deleteUserById(id);
        return "redirect:/admin";
    }

}
