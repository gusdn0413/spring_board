package com.nc13.spring_board.restController;

import com.nc13.spring_board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    @GetMapping("/validateUsername")
    public Map<String, Object> validateUsername(String username) {

        HashMap<String , Object> resultMap = new HashMap<>();
        boolean result = userService.validateUsername(username);

        if (result) {
            resultMap.put("result", "success");
        } else {
            resultMap.put("result", "fail");

        }
        return resultMap;
    }
}
