package com.javaguides.bms.service;

import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.helper.JwtUtil;
import com.javaguides.bms.jdbc.repository.*;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.helper.KeyHasher;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.LoginCredsRequest;
import com.javaguides.bms.model.returnmodel.LoginResponse;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    private LoginJDBCRepository loginJDBCRepository;
    private UsersJDBCRepository usersJDBCRepository;
    private HouseholdJDBCRepository householdJDBCRepository;
    private static final String INVALID_LOGIN = "Invalid username or password.";

    @Override
    public LoginResponse login(LoginCredsRequest request) throws Exception {
        String token = null;
        String userCd = null;
        Integer role = null;

        validateRequest(request);
        Optional<LoginCreds> user = authenticate(request);
        if (user.isPresent()) {
            loginJDBCRepository.updateLoginDt(user.get().getUserId());
            token = JwtUtil.generateAccessToken(user.get());
            userCd = user.get().getCd();
            role = user.get().getRole();
        }else{
            throwErrorMessage(INVALID_LOGIN);
        }

        return new LoginResponse(token, userCd, role);
    }

    private void validateRequest(LoginCredsRequest request) {
        List<String> errors = new ArrayList<>();
        if (request.getCd()==null || request.getCd().trim().isEmpty()) {
            errors.add("User ID is required.");

            if (request.getPassword()==null || request.getPassword().isEmpty()) {
                throwErrorMessage("Please sign up with valid credentials.");
            }
        }else{
            if (request.getPassword()==null || request.getPassword().isEmpty()) {
                errors.add("Password is required.");
            }
        }
        if (!errors.isEmpty()) throwErrorMessages(errors);
    }

    private Optional<LoginCreds> authenticate(LoginCredsRequest request) throws Exception {
        String cd = request.getCd().trim().toUpperCase();
        Optional<LoginCreds> user = loginJDBCRepository.getUserByCd(cd);
        if (user.isEmpty()) throwErrorMessage(INVALID_LOGIN);

        String hashedInput = KeyHasher.hashPassword(request.getPassword(), user.get().getSalt());
        if (!hashedInput.equals(user.get().getPassword())) throwErrorMessage(INVALID_LOGIN);

//        String userId = user.get().getUserId();
//        if (userId!=null && !userId.isEmpty()) {
//            Optional<UsersModel> mainUser = usersJDBCRepository.findById(userId);
//            if (mainUser.isPresent()) {
//                String householdId = mainUser.get().getHouseholdKey();
//                if (householdId!=null && !householdId.isEmpty()) {
//                    Optional<HouseholdModel> household = householdJDBCRepository.findById(householdId);
//                    if (household!=null && household.isPresent()) {
//                        if (household.get().getStatus().equals(SystemStatusEnum.INACTIVE.getKey())) {
//                            throwErrorMessage(INVALID_LOGIN);
//                        }
//                    }else{
//                        throwErrorMessage(INVALID_LOGIN);
//                    }
//                }
//            }else{
//                throwErrorMessage(INVALID_LOGIN);
//            }
//        }

        return user;
    }

}
