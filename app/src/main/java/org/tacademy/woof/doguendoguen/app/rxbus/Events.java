package org.tacademy.woof.doguendoguen.app.rxbus;

/**
 * Created by Tak on 2017. 8. 15..
 */

public class Events {
    public Events(){
    }

    //견종 선택 다이얼로그
    public static class TypeMsgEvents {
        private String type;
        public TypeMsgEvents(String type) {
            this.type = type;
        }
        public String getTag() {
            return type;
        }
    }

    //성별 선택 다이얼로그
    public static class GenderMsgEvents {
        private String gender;
        public GenderMsgEvents(String gender) {
            this.gender = gender;
        }
        public String getTag() {
            return gender;
        }
    }

    //나이 선택 다이얼로그
    public static class AgeMsgEvents {
        private String age;
        public AgeMsgEvents(String age) {
            this.age = age;
        }
        public String getTag() {
            return age;
        }
    }

    //지역 선택 다이얼로그
    public static class RegionMsgEvents {
        private String city;
        private String district;

        public RegionMsgEvents(String city, String district) {
            this.city = city;
            this.district = district;
        }
        public String getCityTag() {
            return city;
        }
        public String getDistrictTag() {
            return district;
        }
    }
}
