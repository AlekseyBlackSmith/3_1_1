package app.springboot.controller;

import app.springboot.model.User;
import app.springboot.service.RoleService;
import app.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class AppController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AppController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String toLogin1() {
        return "login";
    }

    @GetMapping("/login")
    public String toLogin2() {
        return "login";
    }

    @GetMapping(value = "/show")
    public String userPage(Model model, Principal principal) {
        User currentUser = userService.getUserByNameWithRoles(principal.getName());
        model.addAttribute("user", currentUser);
        model.addAttribute("roles", currentUser.getRoles());
        return "show";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserByIdWithRoles(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        return "show";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/new")
    public ModelAndView newUser(ModelAndView mav) {
        mav.addObject("user", new User());
        mav.addObject("allRoles", roleService.getAllRoles());
        mav.setViewName("new");
        return mav;
    }

    @PostMapping("/new")
    public ModelAndView create(ModelAndView mav,
                               @ModelAttribute("user") User user,
                               @RequestParam("rolesSelected") Long[] rolesId) {
        for(Long roleId : rolesId) {
            user.setRole(roleService.getRoleById(roleId));
        }
        userService.addUser(user);
        mav.addObject("users", userService.getAllUsers());
        mav.setViewName("users");
        return mav;
    }


    @GetMapping("/{id}/update")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam("rolesSelected") Long[] rolesId) {
        for(Long roleId : rolesId) {
            user.setRole(roleService.getRoleById(roleId));
        }
        userService.updateUser(user);
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public String delite(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/users";
    }

}
