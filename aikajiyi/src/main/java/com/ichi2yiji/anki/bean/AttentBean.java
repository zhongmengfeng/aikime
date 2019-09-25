package com.ichi2yiji.anki.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ekar01 on 2017/5/31.
 */

public class AttentBean implements Serializable{
    /**
     * "code":1000
     * "data":[{"class_id":"2001004","class_name":"\u5730\u9707\u5730\u5c42\u5b661703","tea_id":"7913","face":"http:\/\/anki.oss-cn-hangzhou.aliyuncs.com\/Logos\/58e6f3ee463d8.jpg","class_desc":"\u5730\u9707\u5730\u5c42\u5b661703\u7684\u63cf\u8ff0","in_class":"1"}]
     */
    private static final long serialVersionUID = 1L;

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * "class_id":"2001007"
         * "class_name":"marry\u7684\u65b0\u73ed\u7ea7"
         * "tea_id":"7914"
         * "face":"\/Application\/Logo\/Class_logo\/2017-05-12\/591562df71a68.png"
         * "class_desc":"marry\u7684\u65b0\u73ed\u7ea7\u6d4b\u8bd5"
         * "in_class":"0"----->是否关注
         *
         *
         * 新的返回值:
         * "class_id": "2001004",
             "class_name": "地震地层学1703",
             "tea_id": "7913",
             "teacher_name": "linken",
             "face": "http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58e6f3ee463d8.jpg",
             "class_desc": "地震地层学1703的描述",
             "in_class": "1",
             "number": "19"
         */

//        @Column(unique = true, defaultValue = "unknown")

//        private static final long serialVersionUID = 2L;

        private String class_id;
        private String class_name;
        private String tea_id;
        private String face;
        private String class_desc;
        private int in_class;
        private String number;
        private String teacher_name;

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getTea_id() {
            return tea_id;
        }

        public void setTea_id(String tea_id) {
            this.tea_id = tea_id;
        }

        public String getFace() {
            return face;
        }

        public void setFace(String face) {
            this.face = face;
        }

        public String getClass_desc() {
            return class_desc;
        }

        public void setClass_desc(String class_desc) {
            this.class_desc = class_desc;
        }

        public int getIn_class() {
            return in_class;
        }

        public void setIn_class(int in_class) {
            this.in_class = in_class;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "class_id='" + class_id + '\'' +
                    ", class_name='" + class_name + '\'' +
                    ", tea_id='" + tea_id + '\'' +
                    ", face='" + face + '\'' +
                    ", class_desc='" + class_desc + '\'' +
                    ", in_class=" + in_class +
                    ", number=" + number +
                    ", teacher_name='" + teacher_name + '\'' +
                    '}';
        }
    }

}
