package org.androidtown.hurryhurry_client.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class HttpPostSend {
    public static final String MP4_PLAY_URL = "http://106.255.237.10:3333/video.html";
    public static final String MEDIA_URL = "http://106.255.237.10:3/CadyDoc/nexacro/FileUpload/";
    public static final String API_SERVER_URL = "http://106.255.237.10:3333/app/";
    //public static final String API_SERVER_URL = "http://www.icthvn.or.kr:1111/app/";
    //public static final String TST_SERVER_URL = "http://192.168.23.26:8088/app/";    // test server url

    private static String executeClient(String urlString, String postParams) {
        try {
            /*String query = "";
            try {
				query = URLEncoder.encode(postParams, "utf-8");
			} catch(Exception e) {
				e.printStackTrace();
			}*/
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.write(postParams.getBytes("utf-8"));
            //dStream.writeBytes(postParams);
            dStream.flush();
            dStream.close();
            int responseCode = connection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();
            return responseOutput.toString();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    // 17.07.04
    public static String executeLogin(String login_id, String password) {
        return executeClient(API_SERVER_URL + "login/login.json", "MANAGER_ID=" + login_id + "&MANAGER_PASSWORD=" + password);
    }

    // 17.07.05
    public static String executeGetReservationList(String manager_id, String date) {
        return executeClient(API_SERVER_URL + "sch/getVisitUserList.json", "MANAGER_ID=" + manager_id + "&RESERVED_DATE=" + date);
    }

    // 17.07.06
    public static String executeGetReservationDetailInfo(String manager_id, String member_id, String seq, String reservation_seq) {
        return executeClient(API_SERVER_URL + "sch/getVisitUserDetail.json", "MANAGER_ID=" + manager_id + "&MEMBER_ID=" + member_id + "&SEQ=" + seq + "&RESERVATION_SEQ=" + reservation_seq);
    }

    // 17.07.06
    public static String executeSetReservationPosition(String reservation_list) {
        return executeClient(API_SERVER_URL + "sch/modVisitUserList.json", "STR_VISIT_SEQ=" + reservation_list);
    }

    public static String executeGetRecipient_Dialog(String manager_id, String reservation_seq) {
        return executeClient(API_SERVER_URL + "sch/getModMemberInfo.json", "MANAGER_ID=" + manager_id + "&RESERVATION_SEQ=" + reservation_seq);
    }

    // 17.07.11
    public static String executeAddReservation(String manager_id, String member_id, String seq, String reservation_date, String visit_yn, String visit_time, String remote_yn, String remote_time) {
        return executeClient(API_SERVER_URL + "sch/regMemberReservation.json", "MANAGER_ID=" + manager_id + "&MEMBER_ID=" + member_id + "&SEQ=" + seq + "&RESERVED_DATE=" + reservation_date + "&VN_RSV_YN=" + visit_yn
                + "&VN_RESERVED_TIME=" + visit_time + "&JT_RSV_YN=" + remote_yn + "&JT_RESERVED_TIME=" + remote_time);
    }

    public static String executeGetRecipientList_Dialog(String manager_id) {
        return executeClient(API_SERVER_URL + "sch/getMemberSearchList.json", "MANAGER_ID=" + manager_id);
    }

    // 17.07.12
    public static String executeModifyReservation(String manager_id, String member_id, String seq, String reservation_date, String visit_yn, String visit_time, String remote_yn, String remote_time, String reservation_seq) {
        return executeClient(API_SERVER_URL + "sch/modMemberReservation.json", "MANAGER_ID=" + manager_id + "&MEMBER_ID=" + member_id + "&SEQ=" + seq + "&RESERVED_DATE=" + reservation_date + "&VN_RSV_YN=" + visit_yn
                + "&VN_RESERVED_TIME=" + visit_time + "&JT_RSV_YN=" + remote_yn + "&JT_RESERVED_TIME=" + remote_time + "&RESERVATION_SEQ=" + reservation_seq);
    }

    // 17.07.12
    public static String executeDeleteRecipient(String reservation_seq) {
        return executeClient(API_SERVER_URL + "sch/modMemberReservationCancel.json", "RESERVATION_SEQ=" + reservation_seq);
    }

    // 17.07.13
    public static String executeGetRecipientList(String manager_id) {
        return executeClient(API_SERVER_URL + "sch/getMemberManagementList.json", "MANAGER_ID=" + manager_id);
    }

    // 17.07.14
    public static String executeGetVisitNursedirection_Dialog(String member_id) {
        return executeClient(API_SERVER_URL + "sch/getVisitNurseOrderList.json", "MEMBER_ID=" + member_id);
    }

    //기본정보조회
    public static String exeGetCustBasicInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getCustBasicInfo.json", params);
    }

    //기본정보등록/수정
    public static String exeRegCustBasicInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regCustBasicInfo.json", params);
    }

    //기본정보삭제
    public static String exeDelCustBasicInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delCustBasicInfo.json", params);
    }

    //현 병력/과거 등록/수정
    public static String exeModMedicalHistoryInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modMedicalHistoryInfo.json", params);

    }

    //현 병력/과거 삭제
    public static String exeDelMedicalHistoryInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delMedicalHistoryInfo.json", params);
    }

    //투약정보 등록/수정
    public static String exeModMedicinePlanInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modMedicinePlanInfo.json", params);

    }

    //투약정보 삭제
    public static String exeDelMedicinePlanInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delMedicinePlanInfo.json", params);

    }

    //활력징후 및 혈당 정보 조회
    public static String exeGetBloodPressure(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getBloodPressure.json", params);

    }

    //활력징후 및 혈당 정보 등록
    public static String exeRegBloodPressure(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regBloodPressure.json", params);

    }

    //활력징후 및 혈당 정보 수정
    public static String exeModBloodPressure(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modBloodPressure.json", params);

    }

    //활력징후 및 혈당 정보 삭제
    public static String exeDelBloodPressure(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delBloodPressure.json", params);

    }

    //신경계 조회
    public static String exeGetNerve(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getNerve.json", params);
    }

    //신경계 조회 상세
    public static String exeGetNerveDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getNerveDetail.json", params);
    }

    //신경계 등록
    public static String exeRegNerve(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regNerve.json", params);
    }

    //신경계 수정
    public static String exeModNerve(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modNerve.json", params);
    }

    //신경계 삭제
    public static String exeDelNerve(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delNerve.json", params);
    }

    //정서상태 조회
    public static String exeGetEmotion(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getEmotion.json", params);
    }

    //정서상태 조회 상세
    public static String exeGetEmotionDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getEmotionDetail.json", params);
    }

    //정서상태 등록
    public static String exeRegEmotion(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regEmotion.json", params);
    }

    //정서상태 수정
    public static String exeModEmotion(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modEmotion.json", params);
    }

    //정서상태 삭제
    public static String exeDelEmotion(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delEmotion.json", params);
    }

    //감각기계 조회
    public static String exeGetSense(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getSense.json", params);
    }

    //감각기계 조회 상세
    public static String exeGetSenseDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getSenseDetail.json", params);
    }

    //감각기계 등록
    public static String exeRegSense(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regSense.json", params);
    }

    //감각기계 수정
    public static String exeModSense(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modSense.json", params);
    }

    //감각기계 삭제
    public static String exeDelSense(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delSense.json", params);
    }

    //심혈관계 조회
    public static String exeGetCardiovascular(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getCardiovascular.json", params);
    }

    //심혈관계 조회 상세
    public static String exeGetCardiovascularDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getCardiovascularDetail.json", params);
    }

    //심혈관계 등록
    public static String exeRegCardiovascular(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regCardiovascular.json", params);
    }

    //심혈관계 수정
    public static String exeModCardiovascular(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modCardiovascular.json", params);
    }

    //심혈관계 삭제
    public static String exeDelCardiovascular(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delCardiovascular.json", params);
    }

    //호흡기계 조회
    public static String exeGetRespirator(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getRespirator.json", params);
    }

    //호흡기계 조회 상세
    public static String exeGetRespiratorDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getRespiratorDetail.json", params);
    }

    //호흡기계 등록
    public static String exeRegRespirator(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regRespirator.json", params);
    }

    //호흡기계 수정
    public static String exeModRespirator(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modRespirator.json", params);
    }

    //호흡기계 삭제
    public static String exeDelRespirator(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delRespirator.json", params);
    }

    //위장관계 조회
    public static String exeGetDigest(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDigest.json", params);
    }

    //위장관계 조회 상세
    public static String exeGetDigestDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDigestDetail.json", params);
    }

    //위장관계 등록
    public static String exeRegDigest(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regDigest.json", params);
    }

    //위장관계 수정
    public static String exeModDigest(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modDigest.json", params);
    }

    //위장관계 삭제
    public static String exeDelDigest(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delDigest.json", params);
    }


    //인지능력평가 정보 조회
    public static String exeGetDisorder(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDisorder.json", params);
    }

    //행동변화 날짜별 상세 조회
    public static String exeGetDisorderActionDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDisorderActionDetail.json", params);
    }

    // 행동변화 정보 등록
    public static String exeRegDisorderAction(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regDisorderAction.json", params);
    }

    //행동변화 정보 수정
    public static String exeModDisorderAction(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modDisorderAction.json", params);
    }

    //행동변화 정보 삭제
    public static String exeDelDisorderAction(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delDisorderAction.json", params);
    }

    //MMSE-KC 날짜별 상세 조회
    public static String exeGetDisorderMMSEDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDisorderMMSEDetail.json", params);
    }

    //MMSE-KC 정보 등록
    public static String exeRegDisorderMMSE(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regDisorderMMSE.json", params);
    }

    //MMSE-KC 정보 수정
    public static String exeModDisorderMMSE(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modDisorderMMSE.json", params);
    }

    //MMSE-KC 정보 삭제
    public static String exeDelDisorderMMSE(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delDisorderMMSE.json", params);
    }

    //영양상태조회
    public static String exeGetNutriture(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getNutriture.json", params);
    }

    //영양상태 조회 상세
    public static String exeGetNutritureDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getNutritureDetail.json", params);
    }

    //영양상태 등록
    public static String exeRegNutriture(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regNutriture.json", params);
    }

    //영양상태 수정
    public static String exeModNutriture(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modNutriture.json", params);
    }

    //영양상태 삭제
    public static String exeDelNutriture(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delNutriture.json", params);
    }

    //비뇨기계 조회
    public static String exeGetUrination(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getUrination.json", params);
    }

    //비뇨기계 조회 상세
    public static String exeGetUrinationDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getUrinationDetail.json", params);
    }

    //비뇨기계 등록
    public static String exeRegUrination(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regUrination.json", params);
    }

    //비뇨기계 수정
    public static String exeModUrination(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modUrination.json", params);
    }

    //비뇨기계 삭제
    public static String exeDelUrination(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delUrination.json", params);
    }

    //근골격계 조회
    public static String exeGetMusculoskeletal(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getMusculoskeletal.json", params);
    }

    //근골격계 조회 상세
    public static String exeGetMusculoskeletalDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getMusculoskeletalDetail.json", params);
    }

    //근골력계 등록
    public static String exeRegMusculoskeletal(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regMusculoskeletal.json", params);
    }

    //근골격계 수정
    public static String exeModMusculoskeletal(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modMusculoskeletal.json", params);
    }

    //근골격계 삭제
    public static String exeDelMusculoskeletal(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delMusculoskeletal.json", params);
    }

    //통증 조회
    public static String exeGetVas(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getVas.json", params);
    }

    //통증 조회 상세
    public static String exeGetVasDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getVasDetail.json", params);
    }

    //통증 등록
    public static String exeRegVas(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regVas.json", params);
    }

    //통증 수정
    public static String exeModVas(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modVas.json", params);
    }

    //통증 삭제
    public static String exeDelVas(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delVas.json", params);
    }

    //일상생활수행능력 정보조회
    public static String exeGetDailyLife(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDailyLife.json", params);
    }

    //일상생활수행능력 정보날짜별 상세조회
    public static String exeGetDailyLifeDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getDailyLifeDetail.json", params);
    }

    //일상생활수행능력 등록
    public static String exeRegDailyLife(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regDailyLife.json", params);
    }

    //일상생활수행능력 수정
    public static String exeModDailyLife(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modDailyLife.json", params);
    }

    //일상생활수행능력 삭제
    public static String exeDelDailyLife(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delDailyLife.json", params);
    }

    //복지용구 정보조회
    public static String exeGetWelfareKit(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getWelfareKit.json", params);
    }

    //복지용구 정보날짜별 상세조회
    public static String exeGetWelfareKitDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getWelfareKitDetail.json", params);
    }

    //복지용구 등록
    public static String exeRegWelfareKit(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regWelfareKit.json", params);
    }

    //복지용구 수정
    public static String exeModWelfareKit(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modWelfareKit.json", params);
    }

    //복지용구 삭제
    public static String exeDelWelfareKit(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delWelfareKit.json", params);
    }

    //EQ-5D-5L 정보조회
    public static String exeGetEQ5D5L(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getEQ5D5L.json", params);
    }

    //EQ-5D-5L 정보날짜별 상세조회
    public static String exeGetEQ5D5LDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getEQ5D5LDetail.json", params);
    }

    //EQ-5D-5L 등록
    public static String exeRegEQ5D5L(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regEQ5D5L.json", params);
    }

    //EQ-5D-5L 수정
    public static String exeModEQ5D5L(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modEQ5D5L.json", params);
    }

    //EQ-5D-5L 삭제
    public static String exeDelEQ5D5L(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delEQ5D5L.json", params);
    }

    //낙상위험도 정보조회
    public static String exeGetFallRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getFallRisk.json", params);
    }

    //낙상위험도 정보날짜별 상세조회
    public static String exeGetFallRiskDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getFallRiskDetail.json", params);
    }

    //낙상위험도 등록
    public static String exeRegFallRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regFallRisk.json", params);
    }

    //낙상위험도 수정
    public static String exeModFallRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modFallRisk.json", params);
    }

    //낙상위험도 삭제
    public static String exeDelFallRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delFallRisk.json", params);
    }

    //욕창위험도 정보조회
    public static String exeGetBedsoreRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getBedsoreRisk.json", params);
    }

    //욕창위험도 정보날짜별 상세조회
    public static String exeGetBedsoreRiskDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getBedsoreRiskDetail.json", params);
    }

    //욕창위험도 등록
    public static String exeRegBedsoreRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regBedsoreRisk.json", params);
    }

    //욕창위험도 수정
    public static String exeModBedsoreRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modBedsoreRisk.json", params);
    }

    //욕창위험도 삭제
    public static String exeDelBedsoreRisk(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delBedsoreRisk.json", params);
    }

    //평가목적/총평 정보 정보조회
    public static String exeGetGeneralReview(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getGeneralReview.json", params);
    }

    //평가목적/총평 정보 정보날짜별 상세조회
    public static String exeGetGeneralReviewDetail(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/getGeneralReviewDetail.json", params);
    }

    //평가목적/총평 정보 등록
    public static String exeRegGeneralReview(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/regGeneralReview.json", params);
    }

    //평가목적/총평 정보 수정
    public static String exeModGeneralReview(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/modGeneralReview.json", params);
    }

    //평가목적/총평 정보 삭제
    public static String exeDelGeneralReview(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/healthenquiry/delGeneralReview.json", params);
    }

    //간호과정 리스트 조회
    public static String exeGetNurseProcessList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/getNurseProcessList.json", params);
    }

    //간호판단 및 계획 조회
    public static String exegGetNurseDecisionPlanList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/getNurseDecisionPlanList.json", params);
    }

    //간호판단 등록
    public static String exegRegNursePlanInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/regNursePlanInfo.json", params);
    }

    //간호판단 수정
    public static String exegModNursePlanInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/modNursePlanInfo.json", params);
    }

    //간호판단 삭제
    public static String exegDelNursePlanInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/delNursePlanInfo.json", params);
    }

    //간호목료조회
    public static String exegGetNursePlanCenterGoalInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/getNursePlanCenterGoalInfo.json", params);
    }

    //간호중재 리스트 일자 조회
    public static String exeGetVisitNurseServiceList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/getVisitNurseServiceList.json", params);
    }

    //간호중재 리스트 과거내역 조회
    public static String exeGetVisitNurseServicePastList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/getVisitNurseServicePastList.json", params);
    }

    //간호중재 등록 및 수정
    public static String exeRegVisitNurseServiceList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/regVisitNurseServiceList.json", params);
    }

    //간호중재 삭제
    public static String exeDelVisitNurseServiceList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/delVisitNurseServiceList.json", params);
    }

    //간호평가 리스트 일자 조회
    public static String exeGetVisitNurseEvaluationList(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/doc/getVisitNurseEvaluationList.json", params);
    }

    //간호평가 등록
    public static String executeRegVisitNurseEvaluationInfo(JSONObject object, String filepath_1, String filepath_2) {
        String url = API_SERVER_URL + "visitnurse/doc/regVisitNurseEvaluationInfo.json";

        String responseString = "";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        DataOutputStream dos = null;
        InputStream is = null;

        String member_id = "";
        String manager_id = "";
        String seq = "";
        String service_date = "";
        String start_time = "";
        String end_time = "";
        String revisit = "";
        String stop_reason = "";
        String stop_etc = "";
        String einfo = "";


        try {
            manager_id = object.getString("MANAGER_ID");
            member_id = object.getString("MEMBER_ID");
            seq = object.getString("SEQ");
            service_date = object.getString("SERVICE_DATE");
            start_time = object.getString("VN_START_TIME");
            end_time = object.getString("VN_END_TIME");
            revisit = object.getString("REVISIT_YN");
            stop_reason = object.getString("SERVICE_STOP_REASON");
            stop_etc = object.getString("VN_CLOSE_ETC");
            einfo = object.getJSONArray("EVALUATION_INFO").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fstrm = new FileInputStream(filepath_1);
            String filename_1 = new File(filepath_1).getName();

            URL connectUrl = new URL(url);

            // connection set
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());

            //MANAGER_ID
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"MANAGER_ID\"");
            dos.writeBytes(lineEnd + lineEnd + manager_id + lineEnd);

            //MEMBER_ID
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"MEMBER_ID\"");
            dos.writeBytes(lineEnd + lineEnd + member_id + lineEnd);

            //SEQ
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"SEQ\"");
            dos.writeBytes(lineEnd + lineEnd + seq + lineEnd);

            //SERVICE_DATE
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"SERVICE_DATE\"");
            dos.writeBytes(lineEnd + lineEnd + service_date + lineEnd);

            //VN_START_TIM
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"VN_START_TIME\"");
            dos.writeBytes(lineEnd + lineEnd + start_time + lineEnd);

            //VN_END_TIME
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"VN_END_TIME\"");
            dos.writeBytes(lineEnd + lineEnd + end_time + lineEnd);

            //REVISIT_YN
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"REVISIT_YN\"");
            dos.writeBytes(lineEnd + lineEnd + revisit + lineEnd);

            //SERVICE_STOP_REASON
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"SERVICE_STOP_REASON\"");
            dos.writeBytes(lineEnd + lineEnd + URLEncoder.encode(stop_reason, "utf-8") + lineEnd);

            //VN_CLOSE_ETC
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"VN_CLOSE_ETC\"");
            dos.writeBytes(lineEnd + lineEnd + stop_etc + lineEnd);

            //EVALUATION_INFO
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"EVALUATION_INFO\"");
            dos.writeBytes(lineEnd + lineEnd + URLEncoder.encode(einfo, "utf-8") + lineEnd);

            // first image send
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"file1\";filename=\"" + filename_1 + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = fstrm.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fstrm.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fstrm.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fstrm.read(buffer, 0, bufferSize);
            }
            fstrm.close();

            dos.writeBytes(lineEnd);
            dos.flush();

            // second image 2 send
            dos = new DataOutputStream(conn.getOutputStream());
            fstrm = new FileInputStream(filepath_2);

            String filename_2 = new File(filepath_2).getName();

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"file2\";filename=\"" + filename_2 + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fstrm.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            int bytesRead2 = fstrm.read(buffer, 0, bufferSize);
            while (bytesRead2 > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fstrm.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead2 = fstrm.read(buffer, 0, bufferSize);
            }
            fstrm.close();

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush();

            // response
            int ch;
            is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String str = b.toString();
            responseString = URLDecoder.decode(str, "utf-8");
//			responseString = URLDecoder.decode(str, HTTP.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
    }

    //협진의뢰기록 리스트 조회
    public static String exeGetJointTreatmentAllListt(String params) {
        return executeClient(API_SERVER_URL + "/visitnurse/jt/getJointTreatmentAllList.json", params);
    }

    //협진 이미지&동영상 삭제
    public static String exeDelJointTreatmentFileInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/jt/delJointTreatmentFileInfo.json", params);
    }

    //협진의뢰기록 저장
    public static String exeRegJointTreatmentInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/jt/regJointTreatmentInfo.json", params);
    }

    //협진의뢰기록 임시저장
    public static String exeRegTempJointTreatmentInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/jt/regTempJointTreatmentInfo.json", params);
    }

    //협진의뢰기록 삭제
    public static String exeDelJointTreatmentInfo(String params) {
        return executeClient(API_SERVER_URL + "visitnurse/jt/delJointTreatmentInfo.json", params);
    }

    // 17.08.08 send video chat state
    public static String executeVideoChatState(String login_id, String member_id, String doc_id, String seq, String chat_room_id, String state) {
        return executeClient(API_SERVER_URL + "visitnurse/jt/modVnVideoChatStateInfo.json", "SENDER_ID=" + login_id + "&MEMBER_ID=" + member_id + "&RECEIVER_ID=" + doc_id
                + "&SEQ=" + seq + "&CHATROOMID=" + chat_room_id + "&STATE=" + state);
    }

    public static String executeVideoChatState(String chat_room_id, String state) {
        return executeClient(API_SERVER_URL + "visitnurse/jt/modVnVideoChatStateInfo.json", "CHATROOMID=" + chat_room_id + "&STATE=" + state);
    }

    //협진 이미지&동영상 업로드
    public static String executeSendPictureImage_Video(String cfm_file_seq, int type, InputStream inputStream) {
        String url = API_SERVER_URL + "visitnurse/jt/getJointTreatmentFileUploadInfo.json";
        String responseString = "";

        DataOutputStream dos = null;
        InputStream is = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            URL connectUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"CFM_FILESEQ\"");
            dos.writeBytes(lineEnd + lineEnd + cfm_file_seq + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            if (type == Constants.PICK_FROM_CAMERA) {
                dos.writeBytes("Content-Disposition:form-data;name=\"file\";filename=\"" + "visitnurse.jpg" + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
            } else {
                dos.writeBytes("Content-Disposition:form-data;name=\"file\";filename=\"" + "visitnurse.mp4" + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
            }

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = inputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesRead = inputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            dos.flush();

            //---------------------------

            int ch;
            is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String str = b.toString();
            responseString = URLDecoder.decode(str, "utf-8");
//			responseString = URLDecoder.decode(str, HTTP.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }


    // 2017.07.28 disuse
    public static String setValue(String key, String value) {
        return "Content-Disposition:form-data;name=\"" + key + "\"\r\n\r\n" + value;
    }

    // 2017.07.28 disuse
    public static String setFile(String key, String fileName) {
        return "Content-Disposition:form-data;name=\"" + key + "\";filename=\"" + fileName + "\"\r\n";
    }

    // 2016.11.29 image upload
    public static String executeUploadPictureImage(String member_id, String filepath) {
        String url = API_SERVER_URL + "visitnurse/doc/regVisitNurseEvaluationInfo1.json";
        String responseString = "";

        DataOutputStream dos = null;
        InputStream is = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            FileInputStream fstrm = new FileInputStream(filepath);
            String filename = new File(filepath).getName();
            URL connectUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"MANAGER_ID\"");
            dos.writeBytes(lineEnd + lineEnd + member_id + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data;name=\"file\";filename=\"" + filename + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = fstrm.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fstrm.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fstrm.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fstrm.read(buffer, 0, bufferSize);
            }
            fstrm.close();

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            dos.flush();

            //---------------------------

            int ch;
            is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String str = b.toString();
            responseString = URLDecoder.decode(str, "utf-8");
//			responseString = URLDecoder.decode(str, HTTP.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }

    // 2016.12.01 image set from server
    public static Bitmap executeSetPictureImage(String filepath) {
//        String url = API_SERVER_URL + filepath;
        String url = filepath;
        URL myFileUrl = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
//             image.setImageBitmap(bitmap);
            return bitmap;
        } catch (IOException e) {
            System.out.println("error=" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}