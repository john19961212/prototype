package com.prototype.service.impl;

import com.prototype.dao.PersonDao;
import com.prototype.dto.PersonExecution;
import com.prototype.entity.Person;
import com.prototype.enums.PersonStateEnum;
import com.prototype.exception.PersonException;
import com.prototype.service.PersonService;
import com.prototype.utils.MDfiveUtil;
import com.prototype.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Person人员信息接口实现
 */
@Service
public class PersonServiceImpl implements PersonService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PersonDao personDao;

    /**
     * 登录功能接口实现
     * @param loginName
     * @param password
     * @return
     */
    public PersonExecution personExecution(String loginName, String password) {
        try {
            Person person = personDao.queryByLoginName(loginName);
            if (person == null){
                return new PersonExecution(PersonStateEnum.PERSON_FAIL_0);
            }
            String md5 = MDfiveUtil.getMD5(password);
            boolean flag = md5.equals(person.getPassword());
            if(flag){
                return new PersonExecution(person.getId(),PersonStateEnum.PERSON_SUCCESS,person);
            }else if (!flag){
                return new PersonExecution(PersonStateEnum.PERSON_FIAL_1);
            }else {
                return new PersonExecution(PersonStateEnum.INNER_PERSON);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }

    /**
     *  验证老密码，修改密码功能接口实现
     * @param loginName
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public PersonExecution personUpdatePassword(String loginName, String oldPassword, String newPassword) {
        try {
            Person person = personDao.queryByLoginName(loginName);
            if (person == null){
                return new PersonExecution(PersonStateEnum.PERSON_FAIL_0);
            }
            boolean passwordFlag = MDfiveUtil.getMD5(oldPassword).equals(person.getPassword());
            if(passwordFlag){
                person.setPassword(MDfiveUtil.getMD5(newPassword));
                int updateCount = personDao.UpdatePersonPassword(person);
                if (updateCount == 1){
                    return new PersonExecution(person.getId(),PersonStateEnum.PERSON_UPDATE_PASSWORD_SUCCESS);
                }else if (updateCount == 0){
                    return new PersonExecution(person.getId(),PersonStateEnum.PERSON_UPDATE_PASSWORD_FAIL);
                }else {
                    logger.error(PersonStateEnum.INNER_PERSON.getStateInfo());
                    throw new PersonException(PersonStateEnum.INNER_PERSON.getStateInfo());
                }
            }else if (!passwordFlag){
                return new PersonExecution(PersonStateEnum.PERSON_FIAL_1);
            }else {
                return new PersonExecution(PersonStateEnum.INNER_PERSON);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }

    /**
     * 直接修改密码
     * @param loginName
     * @param newPassword
     * @return
     */
    public PersonExecution personUpdatePassword(String loginName, String newPassword) {
        try {
            Person person = personDao.queryByLoginName(loginName);
            if (person == null){
                return new PersonExecution(PersonStateEnum.PERSON_FAIL_0);
            }
            person.setPassword(MDfiveUtil.getMD5(newPassword));
            int updateCount = personDao.UpdatePersonPassword(person);
            if (updateCount == 1){
                return new PersonExecution(person.getId(),PersonStateEnum.PERSON_UPDATE_PASSWORD_SUCCESS);
            }else if (updateCount == 0){
                return new PersonExecution(person.getId(),PersonStateEnum.PERSON_UPDATE_PASSWORD_FAIL);
            }else {
                logger.error(PersonStateEnum.INNER_PERSON.getStateInfo());
                throw new PersonException(PersonStateEnum.INNER_PERSON.getStateInfo());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }

    /**
     * 插入人员信息接口实现
     * @param person
     * @return
     */
    public PersonExecution personInsertExecution(Person person) {
        try {
            if (person == null || person.getLoginName() == null){
                return new PersonExecution(PersonStateEnum.PERSON_REQUEST_ILLEGAL);
            }
            person.setId(UUIDUtil.getUUID());
            int insertCount = personDao.insertPerson(person);
            if (insertCount == 1){
                return new PersonExecution(person.getId(),PersonStateEnum.PERSON_INSERT_SUCCESS,person);
            }else if(insertCount == 0){
                return new PersonExecution(PersonStateEnum.PERSON_INSERT_FAIL);
            }else {
                throw new PersonException(PersonStateEnum.INNER_PERSON.getStateInfo());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }

    /**
     * 人员信息修改接口实现
     * @param person
     * @return
     */
    public PersonExecution personUpdateExecution(Person person) {
        try {
            if (person == null || person.getLoginName() == null){
                return new PersonExecution(PersonStateEnum.PERSON_REQUEST_ILLEGAL);
            }
            int updateCount = personDao.updatePerson(person);
            if (updateCount == 1){
                return new PersonExecution(person.getId(), PersonStateEnum.PERSON_UPDATE_SUCCESS,person);
            }else if (updateCount == 0){
                return new PersonExecution(PersonStateEnum.PERSON_UPDATE_FAIL);
            }else {
                throw new PersonException(PersonStateEnum.INNER_PERSON.getStateInfo());
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }

    /**
     * 查询所有人员信息接口实现
     * @return
     */
    public List<Person> queryPerson() {
        try {
            List<Person> person = personDao.queryPerson();
            return person;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }

    /**
     * 删除人员信息接口实现
     * @param loginName
     * @return
     */
    public PersonExecution deletePerson(String loginName) {
        try {
            Person person = personDao.queryByLoginName(loginName);
            if (person == null){
                return new PersonExecution(PersonStateEnum.PERSON_FAIL_0);
            }
            int deleteCount = personDao.deletePerson(loginName);
            if (deleteCount == 1){
                return new PersonExecution(PersonStateEnum.PERSON_DELETE_SUCCESS);
            }else if (deleteCount == 0){
                return new PersonExecution(PersonStateEnum.PERSON_DELETE_FAIL);
            }else {
                return new PersonExecution(PersonStateEnum.INNER_PERSON);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new PersonException("运行时的内部错误:"+e.getMessage());
        }
    }
}
