package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.MyUserDetailsService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Optional;

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
    public  String index(Model model) {
        model.addAttribute("users", userDetailsService.getAllUsers());
        //model.addAttribute("roles", roleService.getAllRoles());
        return "index";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("rolesList", roleService.getAllRoles());
        return "/newUser";
    }

    @GetMapping("/details/{id}")
    public String showUserDetails(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userDetailsService.getUserById(id);

        if (user.isEmpty()) {
            model.addAttribute("id", id);
            return "/index";
        } else {
            model.addAttribute("user", user.get());
            return "/user-admin";
        }
    }

    @GetMapping("/showEdit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userDetailsService.getUserById(id);
        if (user.isEmpty()) {
            model.addAttribute("id", id);
            return "/index";
        } else {
            model.addAttribute("user", user.get());
            model.addAttribute("rolesList", roleService.getAllRoles());
            return "/edit";
        }
    }
// ======= for bootstrap =======
    @GetMapping("/bootstrap")
    public String showBootstrapPage(Model model) {
        System.out.println("+++++++++HERE+++++++++++++++++");
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("newUser", new User());
        model.addAttribute("rolesList", roleService.getAllRoles());
        model.addAttribute("authUser",  authUser);
        model.addAttribute("users",  userDetailsService.getAllUsers());
        return "/bootstrap-page";
    }

    // ======= END for bootstrap =======


    @PostMapping("/create")
    public String crateUserFromForm(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) { // get ready person from view
        userValidator.validate(user, bindingResult);
        System.out.println("In @PostMapping(\"/create\"): bindingResult" + bindingResult.getAllErrors().toString());
        // in case bad validation:
        if (bindingResult.hasErrors()) { return  "redirect:/admin/new"; }
        userDetailsService.registration(user);
        return "redirect:/admin/"; // go to /user
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        System.out.println("In PATCH user:" + user.toString());
        System.out.println("In PATCH role:" + user.getRoles());
        userValidator.validate(user, bindingResult); // inner validation
        if (bindingResult.hasErrors()) {
            //return "/edit";
            System.out.println("In Error--------> " +  bindingResult.getAllErrors());
            return "redirect:/admin/showEdit/{id}";
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
