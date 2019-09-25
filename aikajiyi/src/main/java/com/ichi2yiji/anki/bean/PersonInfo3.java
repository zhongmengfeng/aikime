package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * 用户信息 + 模考列表
 * Created by Administrator on 2017/5/11.
 */

public class PersonInfo3 {


    /**
     * code : 1000
     * data : {"mem":{"class_ids":"3,4,1","deck":"","email":"123@qq.com","face":"http://anki.oss-cn-hangzhou.aliyuncs.com/Faces/2017-03-15/anki13426063086_ankichina.net.png","gender":"男","honeyname":"起名纠结症","is_teacher":"1","jifen":"20","jyz":"0","member_level":"1","qq":"5555555","school_id":"999","school_logo":"","telephone":"13426063086","user_name":"22222"},"tests":[{"deckname":"造价师考试__试题一","decksize":"12.00","goods_id":"1636","goods_name":"造价师考试__试题一","images":"","is_share":"0","logo":""}]}
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
        /**
         * mem : {"class_ids":"3,4,1","deck":"","email":"123@qq.com","face":"http://anki.oss-cn-hangzhou.aliyuncs.com/Faces/2017-03-15/anki13426063086_ankichina.net.png","gender":"男","honeyname":"起名纠结症","is_teacher":"1","jifen":"20","jyz":"0","member_level":"1","qq":"5555555","school_id":"999","school_logo":"","telephone":"13426063086","user_name":"22222"}
         * tests : [{"deckname":"造价师考试__试题一","decksize":"12.00","goods_id":"1636","goods_name":"造价师考试__试题一","images":"","is_share":"0","logo":""}]
         */

        private MemBean mem;
        private List<TestsBean> tests;

        public MemBean getMem() {
            return mem;
        }

        public void setMem(MemBean mem) {
            this.mem = mem;
        }

        public List<TestsBean> getTests() {
            return tests;
        }

        public void setTests(List<TestsBean> tests) {
            this.tests = tests;
        }

        public static class MemBean {
            /**
             * class_ids : 3,4,1
             * deck :
             * email : 123@qq.com
             * face : http://anki.oss-cn-hangzhou.aliyuncs.com/Faces/2017-03-15/anki13426063086_ankichina.net.png
             * gender : 男
             * honeyname : 起名纠结症
             * is_teacher : 1
             * jifen : 20
             * jyz : 0
             * member_level : 1
             * qq : 5555555
             * school_id : 999
             * school_logo :
             * telephone : 13426063086
             * user_name : 22222
             */

            private String class_ids;
            private String deck;
            private String email;
            private String face;
            private String gender;
            private String honeyname;
            private String is_teacher;
            private String jifen;
            private String jyz;
            private String member_level;
            private String qq;
            private String school_id;
            private String school_logo;
            private String telephone;
            private String user_name;

            public String getClass_ids() {
                return class_ids;
            }

            public void setClass_ids(String class_ids) {
                this.class_ids = class_ids;
            }

            public String getDeck() {
                return deck;
            }

            public void setDeck(String deck) {
                this.deck = deck;
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

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getHoneyname() {
                return honeyname;
            }

            public void setHoneyname(String honeyname) {
                this.honeyname = honeyname;
            }

            public String getIs_teacher() {
                return is_teacher;
            }

            public void setIs_teacher(String is_teacher) {
                this.is_teacher = is_teacher;
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

            public String getMember_level() {
                return member_level;
            }

            public void setMember_level(String member_level) {
                this.member_level = member_level;
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

            public String getSchool_logo() {
                return school_logo;
            }

            public void setSchool_logo(String school_logo) {
                this.school_logo = school_logo;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }
        }

        public static class TestsBean {
            /**
             * deckname : 造价师考试__试题一
             * decksize : 12.00
             * goods_id : 1636
             * goods_name : 造价师考试__试题一
             * images :
             * is_share : 0
             * logo :
             */

            private String deckname;
            private String decksize;
            private String goods_id;
            private String goods_name;
            private String images;
            private String is_share;
            private String logo;

            public String getDeckname() {
                return deckname;
            }

            public void setDeckname(String deckname) {
                this.deckname = deckname;
            }

            public String getDecksize() {
                return decksize;
            }

            public void setDecksize(String decksize) {
                this.decksize = decksize;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public String getIs_share() {
                return is_share;
            }

            public void setIs_share(String is_share) {
                this.is_share = is_share;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }
        }
    }
}
