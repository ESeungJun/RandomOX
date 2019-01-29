package com.seungjun.randomox.utils;

public class CommonUtils {

    /**
     * 비밀번호 유효성 체크
     * 영어 + 숫자 + 특수문자 조합 4 ~ 10자
     *
     * @param pw
     *         체크할 비밀번호
     * @return 유효성 여부
     */
    public static boolean isValidPw(String pw) {
        String stricterFilterString = "^((?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^*+=-])).{4,10}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(stricterFilterString);
        java.util.regex.Matcher m = p.matcher(pw);
        return m.matches();
    }

    /**
     * 닉네임 유효성 체크
     * 한글 2 ~ 12자
     *
     * @param nickname 체크할 이름
     * @return 유효성 여부
     */
    public static boolean isValidName(String nickname) {
        String stricterFilterString = "^[ㄱ-힣\\s]*$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(stricterFilterString);
        java.util.regex.Matcher m = p.matcher(nickname);
        return m.matches();
    }
}
