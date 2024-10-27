package com.rsaserver.RSA.encryption;

import com.rsaserver.RSA.encryption.dto.InitSecureConnectionResponse;
import com.rsaserver.RSA.encryption.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/init")
    public ResponseEntity<InitSecureConnectionResponse> initSecureConnection() {
        return ResponseEntity.ok(loginService.initSecureConnection());
    }

    @PostMapping
    public ResponseEntity<LoginDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(loginService.login(loginDto));
    }

}
