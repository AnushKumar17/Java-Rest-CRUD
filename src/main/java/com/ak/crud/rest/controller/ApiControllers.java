package com.ak.crud.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ak.crud.rest.models.User;
import com.ak.crud.rest.repo.UserRepo;

@RestController
public class ApiControllers {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String greet(){
        System.out.println("In -> /");
        return "Welcome to crud app!";
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        System.out.println("In -> /user");
        return userRepo.findAll();
    }

    @PostMapping("/create")
    public String createUser(@RequestBody User user){
        System.out.println("In -> /create");
        userRepo.save(user);
        return "User created successfully!";
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user){
        System.out.println("In -> /update/{id}");
        User updated = userRepo.findById(id).get();
        System.out.println("updated -> " + updated);
        
        updated.setFirstName(user.getFirstName());
        updated.setLastName(user.getLastName());
        updated.setAge(user.getAge());
        updated.setOccupation(user.getOccupation());
        userRepo.save(updated);
        return "User updated!";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id){

        System.out.println("In -> /delete/{id}");
        User deleted = userRepo.findById(id).get();
        System.out.println("deleted -> " + deleted);
        userRepo.delete(deleted);
        return "User Deleted!";
    }

}
