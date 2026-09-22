package com.duc.ecommerce.service;

import com.duc.ecommerce.dto.request.*;
import com.duc.ecommerce.dto.response.UserCursorResponse;
import com.duc.ecommerce.dto.response.UserResponse;
import com.duc.ecommerce.entity.Product;
import com.duc.ecommerce.entity.User;
import com.duc.ecommerce.exception.EcommerceException;
import com.duc.ecommerce.exception.ErrorCode;
import com.duc.ecommerce.mapper.UserMapper;
import com.duc.ecommerce.repository.UserRepository;
import com.duc.ecommerce.security.JwtService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserService {
    @Autowired
    UserRepository repository;
    UserMapper mapper;
    PasswordEncoder encoder;
    AuthenticationManager manager;
    JwtService jwtService;

    public UserResponse create (UserCreateRequest request){
        User user = mapper.toUser(request);
        String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        repository.save(user);

        UserResponse response = mapper.toUserResponse(user);
        String id = "user_"+user.getId();

        response.setId(id);

        return response;
    }
    public UserResponse read(String id){
        User user = repository.findById(id).orElseThrow(() -> new EcommerceException(ErrorCode.USER_NOT_FOUND));

        UserResponse response = mapper.toUserResponse(user);
        String user_id = "user_"+user.getId();
        response.setId(id);

        return response;
    }

    public List<UserResponse> readAll(){
        return repository.findAll()
                .stream()
                .map(user -> {
                    UserResponse response = mapper.toUserResponse(user);
                    String id = "user_"+user.getId();
                    response.setId(id);
                    return response;
                })
                .toList();
    }

    public UserResponse update(String id,UserUpdateRequest request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        System.out.println("CURRENT USER = " + username);

        User user = repository.findById(id).orElseThrow(() -> new EcommerceException(ErrorCode.USER_NOT_FOUND));

        if (!username.equalsIgnoreCase(user.getName())){
            throw new EcommerceException(ErrorCode.ACCESS_DENIED);
        }

        user.setName(request.getName());
        String hashedPassword = encoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        repository.save(user);
        return mapper.toUserResponse(user);

    }

    public void delete(String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("CURRENT USER = " + username);

        User user = repository.findById(id).orElseThrow((() -> new EcommerceException(ErrorCode.USER_NOT_FOUND)));

        if (!username.equalsIgnoreCase(user.getName())){
            throw new EcommerceException(ErrorCode.ACCESS_DENIED);
        }

        repository.delete(user);

    }

    // những thằng cụ thể hơn
    // tim theo keyword bo qua thap,cao, dau
    public List<UserResponse> findByNameContainingIgnoreCase(String keyword){
        List<User> users = repository.findByNameContainingIgnoreCase(keyword);
        return users.stream()
                .map(user -> {
                    UserResponse response = mapper.toUserResponse(user);
                    String id = "user_"+user.getId();
                    response.setId(id);
                    return response;
                })
                .toList();
    }

    // sap xep tang dan
    public List<UserResponse> findAllByOrderByNameAsc(){
        List<User> users = repository.findAllByOrderByNameAsc();
        return users.stream()
                .map(user -> {
                    UserResponse response = mapper.toUserResponse(user);
                    String id = "user_"+user.getId();
                    response.setId(id);
                    return response;
                })
                .toList();
    }
    //Pagination
    public Page<UserResponse> readAllOffsetPage(Pageable pageable){
        return repository.findAll(pageable)
                .map(user -> {
                    UserResponse response = mapper.toUserResponse(user);
                    String id = "user_"+user.getId();
                    response.setId(id);
                    return response;
                });
    }

    public UserCursorResponse readAllCursorPage(Integer cursor,
                                                int limit){
        if (cursor == null)
            cursor = 0;

        List<User> users = repository.findNextUsers(
                cursor,
                PageRequest.of(0,limit + 1)
        );

        //hasNext
        boolean hasNext = users.size() > limit;

        if (hasNext)
            users = users.subList(0,limit);

        //nextCursor
        String nextCursor = null;
        if (!users.isEmpty())
            nextCursor = String.valueOf(users.get(users.size()-1).getId());

        //content
        List<UserResponse> content = users.stream().map(user -> {
            UserResponse response = mapper.toUserResponse(user);
            String id = "user_"+user.getId();
            response.setId(id);
            return response;
        })
                .toList();

        UserCursorResponse userCursorResponse = new UserCursorResponse();
        userCursorResponse.setContent(content);
        userCursorResponse.setNextCursor(nextCursor);
        userCursorResponse.setHasNext(hasNext);

        return userCursorResponse;
    }
    // login
    public String login (UserLoginRequest request){
        /*User user = repository.findByName(request.getName())
                .orElseThrow(()-> new EcommerceException(ErrorCode.USER_NOT_FOUND));*/

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword());

        Authentication authentication =
                manager.authenticate(token);

        String username = authentication.getName();

        return jwtService.generateToken(username);
    }

}
