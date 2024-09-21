package com.example.practicesecurity.service;

import com.example.practicesecurity.dto.JoinDto;
import com.example.practicesecurity.entity.UserEntity;
import com.example.practicesecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto){
        // db에 이미 동일한 username을 가진 회원이 존재하는지 검증하는 로직


        UserEntity data = new UserEntity();

        data.setUsername(joinDto.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword()));  // password 암호화
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }
}
