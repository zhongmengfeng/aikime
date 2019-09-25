package com.ichi2yiji.anki.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 共享牌组数据
 * Created by Administrator on 2017/5/12.
 */

public class SharedDecksBean implements Serializable{


    /**
     * code : 1000
     * data : {"cats":[{"cat_id":"2","cat_name":"英语"},{"cat_id":"4","cat_name":"日语"},{"cat_id":"22","cat_name":"语文"},{"cat_id":"23","cat_name":"地理"},{"cat_id":"25","cat_name":"政治"},{"cat_id":"27","cat_name":"化学"},{"cat_id":"28","cat_name":"音乐"}],"classCreatedByMe":[{"class_id":"1","classname":"英语001班","decks":[{"addtime":"1480523153","class_id":"1","goods_id":"1326","price":"0","short_desc":"化学知识点","title":"必修一化学琐碎知识点","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/2017-01-20/%E5%BF%85%E4%BF%AE%E4%B8%80%E5%8C%96%E5%AD%A6%E7%90%90%E7%A2%8E%E7%9F%A5%E8%AF%86%E7%82%B9.apkg","xia":"54"}]},{"class_id":"2","classname":"英语002班","decks":[]},{"class_id":"3","classname":"英语003班","decks":[{"addtime":"1492502015","class_id":"3","goods_id":"2065","price":"0","short_desc":"","title":"药理学","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704181553/%E8%8D%AF%E7%90%86%E5%AD%A6.apkg","xia":"15"}]}],"decks":[{"addtime":"2017-04-07 09:28:00","goods_id":"1952","price":"0","short_desc":"英语6级_01","title":"六级单词01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg","xia":"550"},{"addtime":"2017-04-07 16:31:50","goods_id":"1971","price":"0","short_desc":"英语6级_02","title":"六级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","xia":"25"},{"addtime":"2017-04-07 17:16:39","goods_id":"1972","price":"0","short_desc":"四级英语单词","title":"四级单词01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg","xia":"63"},{"addtime":"2017-04-07 17:19:12","goods_id":"1973","price":"0","short_desc":"四级英语单词02","title":"四级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","xia":"22"},{"addtime":"2017-04-07 17:45:13","goods_id":"1974","price":"0","short_desc":"四级英语单词03","title":"四级单词03","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__03.apkg","xia":"19"},{"addtime":"2017-04-07 17:45:38","goods_id":"1975","price":"0","short_desc":"四级英语单词04","title":"四级单词04","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__04.apkg","xia":"16"},{"addtime":"2017-04-07 17:46:10","goods_id":"1976","price":"0","short_desc":"雅思英语01","title":"雅思单词01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E9%9B%85%E6%80%9D%E5%8D%95%E8%AF%8D__01.apkg","xia":"23"},{"addtime":"2017-04-07 17:49:55","goods_id":"1977","price":"0","short_desc":"雅思英语02","title":"雅思单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E9%9B%85%E6%80%9D%E5%8D%95%E8%AF%8D__02.apkg","xia":"14"},{"addtime":"2017-04-07 17:50:55","goods_id":"1978","price":"0","short_desc":"古诗词背诵","title":"古诗背诵","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%8F%A4%E8%AF%97%E8%83%8C%E8%AF%B5.apkg","xia":"23"},{"addtime":"2017-04-07 17:55:21","goods_id":"1979","price":"0","short_desc":"汉字听写","title":"汉字听写","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99.apkg","xia":"6"},{"addtime":"2017-04-08 10:39:36","goods_id":"1980","price":"0","short_desc":"","title":"collection","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704081039/collection.apkg","xia":"3"},{"addtime":"2017-04-10 08:55:26","goods_id":"1982","price":"0","short_desc":"汉字听写题库","title":"汉字听写题库A_level01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level01.apkg","xia":"4"},{"addtime":"2017-04-10 08:56:03","goods_id":"1983","price":"0","short_desc":"汉字听写题库02","title":"汉字听写题库A__level02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level02.apkg","xia":"1"},{"addtime":"2017-04-10 08:57:25","goods_id":"1984","price":"0","short_desc":"汉字听写题库03","title":"汉字听写题库A__level03","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level03.apkg","xia":"1"},{"addtime":"2017-04-10 09:00:05","goods_id":"1985","price":"0","short_desc":"","title":"考研词汇5500","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704100859/%E8%80%83%E7%A0%94%E8%AF%8D%E6%B1%875500.apkg","xia":"3"},{"addtime":"2017-04-10 10:53:19","goods_id":"1986","price":"0","short_desc":"汉字听写题库04","title":"汉字听写题库A__level04","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level04.apkg","xia":"1"},{"addtime":"2017-04-10 10:57:10","goods_id":"1987","price":"0","short_desc":"汉字听写题库05","title":"汉字听写题库A__level05","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level05.apkg","xia":"3"},{"addtime":"2017-04-10 10:58:06","goods_id":"1988","price":"0","short_desc":"汉字听写题库06","title":"汉字听写题库A__level06","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level06.apkg","xia":"2"},{"addtime":"2017-04-10 10:59:54","goods_id":"1989","price":"0","short_desc":"汉字听写题库07","title":"汉字听写题库A__level07","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level07.apkg","xia":"1"},{"addtime":"2017-04-10 11:15:04","goods_id":"1990","price":"0","short_desc":"汉字听写题库08","title":"汉字听写题库A__level08","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level08.apkg","xia":"2"},{"addtime":"2017-04-10 11:35:31","goods_id":"1991","price":"0","short_desc":"汉字听写题库09","title":"汉字听写题库A__level09","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level09.apkg","xia":"2"},{"addtime":"2017-04-10 11:41:17","goods_id":"1992","price":"0","short_desc":"汉字听写题库10","title":"汉字听写题库A__level10","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level10.apkg","xia":"1"},{"addtime":"2017-04-10 11:51:29","goods_id":"1993","price":"0","short_desc":"汉字听写题库11","title":"汉字听写题库A__level11","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level11.apkg","xia":"1"},{"addtime":"2017-04-10 12:11:48","goods_id":"1994","price":"0","short_desc":"汉字听写题库12","title":"汉字听写题库A__level12","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E6%B1%89%E5%AD%97%E5%90%AC%E5%86%99%E9%A2%98%E5%BA%93A__level12.apkg","xia":"4"},{"addtime":"2017-04-10 12:21:52","goods_id":"1995","price":"0","short_desc":"化学","title":"化学","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%8C%96%E5%AD%A6.apkg","xia":"11"},{"addtime":"2017-04-10 12:34:06","goods_id":"1996","price":"0","short_desc":"化学知识点","title":"必修一化学琐碎知识点","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/2017-01-20/%E5%BF%85%E4%BF%AE%E4%B8%80%E5%8C%96%E5%AD%A6%E7%90%90%E7%A2%8E%E7%9F%A5%E8%AF%86%E7%82%B9.apkg","xia":"13"},{"addtime":"2017-04-10 18:06:25","goods_id":"1997","price":"0","short_desc":"","title":"ESL Podcast","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704101806/ESL%20Podcast.apkg","xia":"0"},{"addtime":"2017-04-11 09:41:46","goods_id":"1998","price":"0","short_desc":"地理","title":"地理","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/2016-12-24/%E5%9C%B0%E7%90%86.apkg","xia":"188"},{"addtime":"2017-04-11 09:42:18","goods_id":"1999","price":"0","short_desc":"钢琴","title":"钢琴","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E9%92%A2%E7%90%B4.apkg","xia":"26"},{"addtime":"2017-04-11 09:43:06","goods_id":"2000","price":"30","short_desc":"日本语红宝书N5","title":"日本语红宝书__N5","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704121806/%E6%97%A5%E6%9C%AC%E8%AF%AD%E7%BA%A2%E5%AE%9D%E4%B9%A6__N5.apkg","xia":"17"},{"addtime":"2017-04-11 09:43:56","goods_id":"2001","price":"0","short_desc":"地震地层学","title":"地震地层学-绪论","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704110943/%E5%9C%B0%E9%9C%87%E5%9C%B0%E5%B1%82%E5%AD%A6-%E7%BB%AA%E8%AE%BA.apkg","xia":"12"},{"addtime":"2017-05-04 19:04:02","goods_id":"2002","price":"0","short_desc":"刮刮乐政治填空","title":"政治填空题刮刮乐","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201705041904/%E6%94%BF%E6%B2%BB%E5%A1%AB%E7%A9%BA%E9%A2%98%E5%88%AE%E5%88%AE%E4%B9%90.apkg","xia":"9"}],"professionals":[{"class_id":"6","classname":"语文003班","decks":[]},{"class_id":"9","classname":"班级2","decks":[]},{"class_id":"2001002","classname":"安卓基础班","decks":[]},{"class_id":"2001003","classname":"公共班","decks":[]}]}
     */

    private static final long serialVersionUID = 1L;
    private int code;
    private DataBean data;

    @Override
    public String toString() {
        return "SharedDecksBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

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

    public static class DataBean implements Serializable{
        private List<CatsBean> cats;
        private List<ClassCreatedByMeBean> classCreatedByMe;
        private List<DecksBeanX> decks;
        private List<ProfessionalsBean> professionals;
        private int colorNum;

        @Override
        public String toString() {
            return "DataBean{" +
                    "cats=" + cats +
                    ", classCreatedByMe=" + classCreatedByMe +
                    ", decks=" + decks +
                    ", professionals=" + professionals +
                    ", colorNum=" + colorNum +
                    '}';
        }

        public List<CatsBean> getCats() {
            return cats;
        }

        public void setCats(List<CatsBean> cats) {
            this.cats = cats;
        }

        public List<ClassCreatedByMeBean> getClassCreatedByMe() {
            return classCreatedByMe;
        }

        public void setClassCreatedByMe(List<ClassCreatedByMeBean> classCreatedByMe) {
            this.classCreatedByMe = classCreatedByMe;
        }

        public List<DecksBeanX> getDecks() {
            return decks;
        }

        public void setDecks(List<DecksBeanX> decks) {
            this.decks = decks;
        }

        public List<ProfessionalsBean> getProfessionals() {
            return professionals;
        }

        public void setProfessionals(List<ProfessionalsBean> professionals) {
            this.professionals = professionals;
        }

        public void setColorNum(int colorNum) {
            this.colorNum = colorNum;
        }

        public int getColorNum() {
            return colorNum;
        }

        public static class CatsBean implements Serializable{
            /**
             * cat_id : 2
             * cat_name : 英语
             */

            private String cat_id;
            private String cat_name;

            @Override
            public String toString() {
                return "CatsBean{" +
                        "cat_id='" + cat_id + '\'' +
                        ", cat_name='" + cat_name + '\'' +
                        '}';
            }

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }
        }

        public static class ClassCreatedByMeBean implements Serializable{
            /**
             * class_id : 1
             * classname : 英语001班
             * decks : [{"addtime":"1480523153","class_id":"1","goods_id":"1326","price":"0","short_desc":"化学知识点","title":"必修一化学琐碎知识点","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/2017-01-20/%E5%BF%85%E4%BF%AE%E4%B8%80%E5%8C%96%E5%AD%A6%E7%90%90%E7%A2%8E%E7%9F%A5%E8%AF%86%E7%82%B9.apkg","xia":"54"}]
             */

            private String class_id;
            private String classname;
            private List<DecksBean> decks;

            @Override
            public String toString() {
                return "ClassCreatedByMeBean{" +
                        "class_id='" + class_id + '\'' +
                        ", classname='" + classname + '\'' +
                        ", decks=" + decks +
                        '}';
            }

            public String getClass_id() {
                return class_id;
            }

            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            public String getClassname() {
                return classname;
            }

            public void setClassname(String classname) {
                this.classname = classname;
            }

            public List<DecksBean> getDecks() {
                return decks;
            }

            public void setDecks(List<DecksBean> decks) {
                this.decks = decks;
            }

            public static class DecksBean implements Serializable{
                /**
                 * addtime : 1480523153
                 * class_id : 1
                 * goods_id : 1326
                 * price : 0
                 * short_desc : 化学知识点
                 * title : 必修一化学琐碎知识点
                 * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/2017-01-20/%E5%BF%85%E4%BF%AE%E4%B8%80%E5%8C%96%E5%AD%A6%E7%90%90%E7%A2%8E%E7%9F%A5%E8%AF%86%E7%82%B9.apkg
                 * xia : 54
                 */

                private String addtime;
                private String class_id;
                private String goods_id;
                private String price;
                private String short_desc;
                private String title;
                private String url;
                private String xia;

                @Override
                public String toString() {
                    return "DecksBean{" +
                            "addtime='" + addtime + '\'' +
                            ", class_id='" + class_id + '\'' +
                            ", goods_id='" + goods_id + '\'' +
                            ", price='" + price + '\'' +
                            ", short_desc='" + short_desc + '\'' +
                            ", title='" + title + '\'' +
                            ", url='" + url + '\'' +
                            ", xia='" + xia + '\'' +
                            '}';
                }

                public String getAddtime() {
                    return addtime;
                }

                public void setAddtime(String addtime) {
                    this.addtime = addtime;
                }

                public String getClass_id() {
                    return class_id;
                }

                public void setClass_id(String class_id) {
                    this.class_id = class_id;
                }

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getShort_desc() {
                    return short_desc;
                }

                public void setShort_desc(String short_desc) {
                    this.short_desc = short_desc;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getXia() {
                    return xia;
                }

                public void setXia(String xia) {
                    this.xia = xia;
                }
            }
        }



        public static class DecksBeanX implements Serializable{
            /**
             * addtime : 2017-04-07 09:28:00
             * goods_id : 1952
             * price : 0
             * short_desc : 英语6级_01
             * title : 六级单词01
             * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg
             * xia : 550
             */

            private String addtime;
            private String goods_id;
            private String price;
            private String short_desc;
            private String title;
            private String url;
            private String xia;

            @Override
            public String toString() {
                return "DecksBeanX{" +
                        "addtime='" + addtime + '\'' +
                        ", goods_id='" + goods_id + '\'' +
                        ", price='" + price + '\'' +
                        ", short_desc='" + short_desc + '\'' +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        ", xia='" + xia + '\'' +
                        '}';
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getShort_desc() {
                return short_desc;
            }

            public void setShort_desc(String short_desc) {
                this.short_desc = short_desc;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getXia() {
                return xia;
            }

            public void setXia(String xia) {
                this.xia = xia;
            }
        }

        public static class ProfessionalsBean implements Serializable{
            /**
             * class_id : 6
             * classname : 语文003班
             * decks : []
             */


            /**
             * "professionals": [
                             {
                             "classname": "考研政治2018",
                             "class_id": "2001018",
                             "free_course_number": "3",
                             "is_pay": "0",
                             "total_course": "0",
                             "class_price": "20",
                             "decks": [

             */
            private String class_id;
            private String classname;
            private String free_course_number;
            private String is_pay;
            private String total_course;
            private String class_price;
            private List<DecksBean> decks;

            public String getClass_id() {
                return class_id;
            }

            public void setClass_id(String class_id) {
                this.class_id = class_id;
            }

            public String getClassname() {
                return classname;
            }

            public void setClassname(String classname) {
                this.classname = classname;
            }

            public List<DecksBean> getDecks() {
                return decks;
            }

            public void setDecks(List<DecksBean> decks) {
                this.decks = decks;
            }

            public String getFree_course_number() {
                return free_course_number;
            }

            public void setFree_course_number(String free_course_number) {
                this.free_course_number = free_course_number;
            }

            public String getIs_pay() {
                return is_pay;
            }

            public void setIs_pay(String is_pay) {
                this.is_pay = is_pay;
            }

            public String getTotal_course() {
                return total_course;
            }

            public void setTotal_course(String total_course) {
                this.total_course = total_course;
            }

            public String getClass_price() {
                return class_price;
            }

            public void setClass_price(String class_price) {
                this.class_price = class_price;
            }

            public static class DecksBean implements Serializable{
                /**
                 * addtime : 1480523153
                 * class_id : 1
                 * goods_id : 1326
                 * price : 0
                 * short_desc : 化学知识点
                 * title : 必修一化学琐碎知识点
                 * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/2017-01-20/%E5%BF%85%E4%BF%AE%E4%B8%80%E5%8C%96%E5%AD%A6%E7%90%90%E7%A2%8E%E7%9F%A5%E8%AF%86%E7%82%B9.apkg
                 * xia : 54
                 */

                private String addtime;
                private String class_id;
                private String goods_id;
                private String price;
                private String short_desc;
                private String title;
                private String url;
                private String xia;

                @Override
                public String toString() {
                    return "DecksBean{" +
                            "addtime='" + addtime + '\'' +
                            ", class_id='" + class_id + '\'' +
                            ", goods_id='" + goods_id + '\'' +
                            ", price='" + price + '\'' +
                            ", short_desc='" + short_desc + '\'' +
                            ", title='" + title + '\'' +
                            ", url='" + url + '\'' +
                            ", xia='" + xia + '\'' +
                            '}';
                }

                public String getAddtime() {
                    return addtime;
                }

                public void setAddtime(String addtime) {
                    this.addtime = addtime;
                }

                public String getClass_id() {
                    return class_id;
                }

                public void setClass_id(String class_id) {
                    this.class_id = class_id;
                }

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getShort_desc() {
                    return short_desc;
                }

                public void setShort_desc(String short_desc) {
                    this.short_desc = short_desc;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getXia() {
                    return xia;
                }

                public void setXia(String xia) {
                    this.xia = xia;
                }
            }

            @Override
            public String toString() {
                return "ProfessionalsBean{" +
                        "class_id='" + class_id + '\'' +
                        ", classname='" + classname + '\'' +
                        ", free_course_number='" + free_course_number + '\'' +
                        ", is_pay='" + is_pay + '\'' +
                        ", total_course='" + total_course + '\'' +
                        ", class_price='" + class_price + '\'' +
                        ", decks=" + decks +
                        '}';
            }
        }
    }
}
