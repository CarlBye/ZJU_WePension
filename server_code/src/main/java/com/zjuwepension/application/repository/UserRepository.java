package com.zjuwepension.application.repository;

import com.zjuwepension.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByUserNameAndUserPwd(String name, String pwd);
    List<User> findUsersByUserPhoneNumAndUserPwd(String phoneNum, String pwd);
    List<User> findUsersByUserEmailAndUserPwd(String mailName, String pwd);
    List<User> findUsersByUserPhoneNum(String phoneNum);
    List<User> findUsersByUserEmail(String mailName);
    List<User> findUsersByUserId(Long id);
}
