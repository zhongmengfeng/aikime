package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * Created by ekar02 on 2017/6/27.
 */

public class UserInfoBean {

    /**
     * code : 1000
     * data : {"mem":{"user_name":"s03","email":"s03@ankichina.net","face":"http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58e6efe8ea5ff.png","jifen":"20","jyz":"0","honeyname":"s03","gender":"男","telephone":"","qq":"","school_id":"2000","is_teacher":"0","member_level":"1","school_logo":"","deck":"","class_ids":"2001020,2001020"},"decks":[],"books":[],"tests":[]}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "mem=" + mem +
                    ", decks=" + decks +
                    ", books=" + books +
                    ", tests=" + tests +
                    '}';
        }

        /**
         * mem : {"user_name":"s03","email":"s03@ankichina.net","face":"http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58e6efe8ea5ff.png","jifen":"20","jyz":"0","honeyname":"s03","gender":"男","telephone":"","qq":"","school_id":"2000","is_teacher":"0","member_level":"1","school_logo":"","deck":"","class_ids":"2001020,2001020"}
         * decks : []
         * books : []
         * tests : []
         */


        private MemBean mem;
        private List<?> decks;
        private List<?> books;
        private List<?> tests;

        public MemBean getMem() {
            return mem;
        }

        public void setMem(MemBean mem) {
            this.mem = mem;
        }

        public List<?> getDecks() {
            return decks;
        }

        public void setDecks(List<?> decks) {
            this.decks = decks;
        }

        public List<?> getBooks() {
            return books;
        }

        public void setBooks(List<?> books) {
            this.books = books;
        }

        public List<?> getTests() {
            return tests;
        }

        public void setTests(List<?> tests) {
            this.tests = tests;
        }

        public static class MemBean {
            @Override
            public String toString() {
                return "MemBean{" +
                        "user_name='" + user_name + '\'' +
                        ", email='" + email + '\'' +
                        ", face='" + face + '\'' +
                        ", jifen='" + jifen + '\'' +
                        ", jyz='" + jyz + '\'' +
                        ", honeyname='" + honeyname + '\'' +
                        ", gender='" + gender + '\'' +
                        ", telephone='" + telephone + '\'' +
                        ", qq='" + qq + '\'' +
                        ", school_id='" + school_id + '\'' +
                        ", is_teacher='" + is_teacher + '\'' +
                        ", member_level='" + member_level + '\'' +
                        ", school_logo='" + school_logo + '\'' +
                        ", deck='" + deck + '\'' +
                        ", class_ids='" + class_ids + '\'' +
                        '}';
            }

            /**
             * user_name : s03
             * email : s03@ankichina.net
             * face : http://anki.oss-cn-hangzhou.aliyuncs.com/Logos/58e6efe8ea5ff.png
             * jifen : 20
             * jyz : 0
             * honeyname : s03
             * gender : 男
             * telephone :
             * qq :
             * school_id : 2000
             * is_teacher : 0
             * member_level : 1
             * school_logo :
             * deck :
             * class_ids : 2001020,2001020
             */

            private String user_name;
            private String email;
            private String face;
            private String jifen;
            private String jyz;
            private String honeyname;
            private String gender;
            private String telephone;
            private String qq;
            private String school_id;
            private String is_teacher;
            private String member_level;
            private String school_logo;
            private String deck;
            private String class_ids;

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public String getJifen() {
                return jifen;
            }

            public void setJifen(String jifen) {
                this.jifen = jifen;
            }

            public String getJyz() {
                return jyz;
            }

            public void setJyz(String jyz) {
                this.jyz = jyz;
            }

            public String getHoneyname() {
                return honeyname;
            }

            public void setHoneyname(String honeyname) {
                this.honeyname = honeyname;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getSchool_id() {
                return school_id;
            }

            public void setSchool_id(String school_id) {
                this.school_id = school_id;
            }

            public String getIs_teacher() {
                return is_teacher;
            }

            public void setIs_teacher(String is_teacher) {
                this.is_teacher = is_teacher;
            }

            public String getMember_level() {
                return member_level;
            }

            public void setMember_level(String member_level) {
                this.member_level = member_level;
            }

            public String getSchool_logo() {
                return school_logo;
            }

            public void setSchool_logo(String school_logo) {
                this.school_logo = school_logo;
            }

            public String getDeck() {
                return deck;
            }

            public void setDeck(String deck) {
                this.deck = deck;
            }

            public String getClass_ids() {
                return class_ids;
            }

            public void setClass_ids(String class_ids) {
                this.class_ids = class_ids;
            }
        }
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
