package com.qiniu.repository;

import com.qiniu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

    User findByAccountAndPassword(String account,String password);

    Long deleteById(Long id);

    @Modifying
    @Transactional
    @Query(value="delete from User c where c.account = ?1")
    void deleteByAccount(String account);

    @Modifying
    @Transactional
    @Query(value = "update user u set u.qua_num =?1 and u.un_qua_num =?2 where u.id = ?3",nativeQuery = true)
    void updateQuaNumAndUnQuaNum(int quaNum, int unQuaNum, long id);
}