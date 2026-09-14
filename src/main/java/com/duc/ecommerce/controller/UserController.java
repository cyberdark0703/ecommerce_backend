package com.duc.ecommerce.controller;


import com.duc.ecommerce.dto.request.UserCreateRequest;
import com.duc.ecommerce.dto.request.UserLoginRequest;
import com.duc.ecommerce.dto.request.UserUpdateRequest;
import com.duc.ecommerce.dto.response.UserCursorResponse;
import com.duc.ecommerce.dto.response.UserResponse;
import com.duc.ecommerce.entity.User;
import com.duc.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    @Autowired
    UserService service;
    @PostMapping("/create")
    UserResponse create (@Valid @RequestBody UserCreateRequest request){
        return service.create(request);
    }

    @GetMapping("/read_one/{id}")
    UserResponse read (@PathVariable String id){
        return service.read(id);
    }

    @GetMapping("/readAll")
    List<UserResponse> readAll(){
        return service.readAll();
    }

    @DeleteMapping("/delete/{id}")
    void delete (@PathVariable String id){
        service.delete(id);
    }

    @PutMapping("/update/{id}")
    UserResponse update (@PathVariable String id, @RequestBody UserUpdateRequest request){
        return service.update(id,request);
    }

    // những thg có chức năng cụ thể hơn
    // tìm tất cả có chứa cụm từ đấy
    @GetMapping("/readallcontainingignorecase/{keyword}")
    List<UserResponse> findByNameContainingIgnoreCase(@PathVariable String keyword){
        return service.findByNameContainingIgnoreCase(keyword);

    }

    //Tìm tất cả theo thứ tự tăng dần
    @GetMapping("/readallbyorderbynameasc")
    List<UserResponse> findAllByOrderByNameAsc(){
        return service.findAllByOrderByNameAsc();
    }

    //Pagination
    //OffsetPagination
    @GetMapping("/readalloffsetpage")
    Page<UserResponse> readAllOffsetPage (Pageable pageable){
        return service.readAllOffsetPage(pageable);
    }

    //CursorPagination
    @GetMapping("/readallcursorpage")
    UserCursorResponse readAllCursorPage (@RequestParam(required = false) Integer cursor,
                                          @RequestParam(defaultValue = "2") int limit){
        return service.readAllCursorPage(cursor,limit);
    }

    //login
    @PostMapping("/login")
    boolean login(@RequestBody UserLoginRequest request) {
        return service.login(request);
    }

}
