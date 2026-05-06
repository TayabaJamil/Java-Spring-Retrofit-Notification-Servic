package com.portfolio.onezapp.common;

import com.portfolio.api.AlertServiceApi;
import com.portfolio.dto.ApiResponse;
import com.portfolio.dto.MakerCheckerEmailDTO;
import com.portfolio.entity.MakerCheckerOzSlab;
import com.portfolio.entities.OfficeUser;
import com.portfolio.manager.RetrofitManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailUtils {


    public static void sendUserEmail(MakerCheckerOzSlab checkerUserAccount, List<OfficeUser> officeUsers, String emailType) {
        List<MakerCheckerEmailDTO> sendEmailRequestDTO = buildMakerCheckerEmailData(officeUsers, checkerUserAccount, emailType);
        new Thread((Runnable) () -> {
            try {

                Call<ApiResponse> call = ((AlertServiceApi) RetrofitManager.getAlertServiceAPI(AlertServiceApi.class)).sendMakerCheckerEmail(sendEmailRequestDTO);
                Response<ApiResponse> response = call.execute();
                ApiResponse apiResponse = response.body();

                if (apiResponse != null && apiResponse.getStatusCode().equals("0")) {
                    log.info("Email sent successfully! ");
                }
            } catch (IOException e) {
                log.error("Alert service is not responding");
            }
        }).start();
    }

    private static List<MakerCheckerEmailDTO> buildMakerCheckerEmailData(List<OfficeUser> officeUsers, MakerCheckerOzSlab checkerUserAccount, String emailType){
        List<MakerCheckerEmailDTO> emailDTOList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(officeUsers)) {
            for (OfficeUser officeUser : officeUsers) {
                MakerCheckerEmailDTO email = new MakerCheckerEmailDTO();
                email.setSubject("Request for Slab Approval"); // Fee service context
                email.setEmailType(emailType);
                email.setTo(officeUser.getEmail());
                email.setOfficeUserId(officeUser.getOfficeUserId());


                Map<String, Object> templateData = new HashMap<>();
                templateData.put("name", StringUtils.isNotBlank(officeUser.getFullName()) ? officeUser.getFullName() : " ");
                templateData.put("serviceName", "Slab Management");
                templateData.put("requestId", checkerUserAccount.getChangeId());
                templateData.put("cashId", checkerUserAccount.getCashId());


                templateData.put("makerAccountId", checkerUserAccount.getMakerId());

                email.setEmailData(templateData);
                emailDTOList.add(email);
            }
        } else {
            emailDTOList.add(populateMakerEmailData(checkerUserAccount, emailType));
        }

        return emailDTOList;
    }

    private static MakerCheckerEmailDTO populateMakerEmailData(MakerCheckerOzSlab checkerOfficeUser, String emailType){
        Map<String, Object> templateData = new HashMap<>();
        MakerCheckerEmailDTO email = new MakerCheckerEmailDTO();

        email.setSubject("Slab Change Request Update");
        email.setEmailType(emailType);

        templateData.put("serviceName", "Slab Management");
        templateData.put("requestId", checkerOfficeUser.getChangeId());

        if(checkerOfficeUser.getCheckerId() != null){
            templateData.put("checkerAccountId", checkerOfficeUser.getCheckerId());
        }

        email.setEmailData(templateData);
        return email;
    }
}
