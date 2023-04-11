package ru.clevertec.ecl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.EntityException;
import ru.clevertec.ecl.exception.ExceptionCode;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.validator.UserValidator;

import java.util.List;

import static ru.clevertec.ecl.util.PaginationUtil.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserValidator validator;

    @Autowired
    public UserServiceImpl(UserRepository userDao, UserValidator validator) {
        this.repository = userDao;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User create(User user) {
        isUserNameValid(user);
        return repository.save(user);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityException(ExceptionCode.USER_NOT_FOUND.getErrorCode()));
    }

    @Override
    public List<User> findAll(Integer pageSize, Integer page) {
        page = checkPage(page);
        pageSize = checkPageSize(pageSize);
        return repository.findAll(PageRequest.of(page, pageSize)).toList();
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (isUserExist(userId)){
            repository.deleteById(userId);
        }
    }

    @Override
    @Transactional
    public User update(User user, Long userId) {
        if (isUserExist(userId) && isUserNameValid(user)){
            user.setId(userId);
        }
        return repository.save(user);
    }

    @Override
    public boolean isUserExist(Long userId){
        if (!repository.existsById(userId)){
            throw new EntityException(ExceptionCode.USER_NOT_FOUND.getErrorCode());
        }
        return true;
    }


    private boolean isUserNameValid(User user){
        if(!validator.isValid(user)){
            throw new EntityException(ExceptionCode.NOT_VALID_USER_NAME.getErrorCode());
        }
        return true;
    }

}
