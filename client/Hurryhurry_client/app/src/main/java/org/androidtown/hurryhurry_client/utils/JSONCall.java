package org.androidtown.hurryhurry_client.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONCall {

    JSONObject json;

    public JSONCall(String result) {
        json = JSONUtil.getJsObject(result);
    }

    public JSONObject jsonResult() {
        return json;
    }

    /**
     * get survey list of each diagnoses(depression, fatigue, stress)
     *
     * @return
     */
    public String[] getSurveyList() {

        JSONArray jsonArray = JSONUtil.getJsArray(json, "rsList");
        String[] survey = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonRealObj = JSONUtil.getJsObject(jsonArray, i);
//            String t_cnt = JSONUtil.getJsString(jsonRealObj, "T_CNT", "null");
//            String cnt = JSONUtil.getJsString(jsonRealObj, "CNT", "null");
            survey[i] = JSONUtil.getJsString(jsonRealObj, "SURVEY", "null");
//            String type = JSONUtil.getJsString(jsonRealObj, "TYPE", "null");
        }
        Log.i("healthmax_test", json.toString());
        Log.i("healthmax_test", "S !!!");
        return survey;
    }

    /**
     * get the value from user body information of basic survey
     *
     * @return
     */
    public ArrayList<String> getBasicSurveyBodyInfo() {

        ArrayList<String> bodyInfo = new ArrayList<>();
        JSONObject obj = JSONUtil.getJsObject(json, "rsMapLastestDatas");

        //신체정보
        bodyInfo.add(JSONUtil.getJsString(obj, "WAIST", "null"));
        bodyInfo.add(JSONUtil.getJsString(obj, "HIGHBLOODPR", "null"));
        bodyInfo.add(JSONUtil.getJsString(obj, "LOWBLOODPR", "null"));
        bodyInfo.add(JSONUtil.getJsString(obj, "AGLU", "null"));
        bodyInfo.add(JSONUtil.getJsString(obj, "TG", "null"));
        bodyInfo.add(JSONUtil.getJsString(obj, "HDLC", "null"));

        return bodyInfo;
    }

    /**
     * get the value from user profile information of basic survey
     *
     * @return
     */
    public ArrayList<String> getBasicSurveyProfile() {

        ArrayList<String> profile = new ArrayList<>();
        JSONObject obj = JSONUtil.getJsObject(json, "rsMapProfile");

        //신상정보
        profile.add(JSONUtil.getJsString(obj, "JOBCATEGORY", "null"));
        profile.add(JSONUtil.getJsString(obj, "MARRIAGE", "null"));
        profile.add(JSONUtil.getJsString(obj, "FAMILY", "null"));
        profile.add(JSONUtil.getJsString(obj, "EDUCATION", "null"));
        profile.add(JSONUtil.getJsString(obj, "JOBSTABILITY", "null"));
        profile.add(JSONUtil.getJsString(obj, "RELIGION", "null"));
        profile.add(JSONUtil.getJsString(obj, "HOUSETYPE", "null"));
        profile.add(JSONUtil.getJsString(obj, "LOCATION", "null"));

        return profile;
    }

    /**
     * get the value from user information(environment, medical_history, tendency) of basic survey
     *
     * @return
     */
    public ArrayList<String> getBasicSurvey2() {

        ArrayList<String> userInfo = new ArrayList<>();
        JSONObject jsonRealObj = JSONUtil.getJsObject(json, "rsMapUserInfo");

        //사용자 환경정보
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "C_LIG", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "C_NOI", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "C_DUST", "null"));

        //사용자 병력
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "OBE", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_DM", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "h_HEART", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_HL", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_CAN", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_SMK", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_ALC", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_DYS", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_DEP", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_PI", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_FLU", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_ALG", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "H_NEU", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "HYP", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "GLU", "null"));

        //사용자 성향
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "TEND_STR", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "TEND_DEP", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "TEND_ANG", "null"));
        userInfo.add(JSONUtil.getJsString(jsonRealObj, "TEND_FAT", "null"));


        Log.i("healthmax_test_basic", json.toString());
        Log.i("healthmax_test_basic", userInfo.toString());
        Log.i("healthmax_test", "S !!!");
        return userInfo;
    }

    /**
     * get the value of comprehensive evaluation
     *
     * @return
     */
    public ArrayList getTotalSurvey() {
        ArrayList<String> totalEval = new ArrayList<>();

        totalEval.add(JSONUtil.getJsString(json, "BMI_Judement", "null"));//BMI 지수
        totalEval.add(JSONUtil.getJsString(json, "STEP_JUDE", "null")); //활동량
        totalEval.add(JSONUtil.getJsString(json, "NOISE", "null"));//소음
        totalEval.add(JSONUtil.getJsString(json, "METABOLICSYNDROME", "null"));//대사증후군
        totalEval.add(JSONUtil.getJsString(json, "S_AVG_Q", "null"));//스트레스 진단 설문값
        totalEval.add(JSONUtil.getJsString(json, "D_AVG_Q", "null"));//우울증 진단 설문값
        totalEval.add(JSONUtil.getJsString(json, "F_AVG_Q", "null"));//피로감 진단 설문값

        Log.i("healthmax_test_total", json.toString());
        Log.i("healthmax_test_totEval", totalEval.toString());
        Log.i("healthmax_test", "S !!!");
        return totalEval;
    }

}

