package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tbea.ic.operation.model.dao.account.AccountDao;
import com.tbea.ic.operation.model.entity.jygk.Account;

@Service
@Transactional("transactionManager")
public class serviceImpl implements AccountService {


}
