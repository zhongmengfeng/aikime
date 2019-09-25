package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * Created by ekar01 on 2017/5/19.
 */

public class Bean {

    private List<CatsBean> cats;
    private List<DecksBean> decks;
    private List<ClassCreatedByMeBean> classCreatedByMe;
    private List<ProfessionalsBean> professionals;

    public List<CatsBean> getCats() {
        return cats;
    }

    public void setCats(List<CatsBean> cats) {
        this.cats = cats;
    }

    public List<DecksBean> getDecks() {
        return decks;
    }

    public void setDecks(List<DecksBean> decks) {
        this.decks = decks;
    }

    public List<ClassCreatedByMeBean> getClassCreatedByMe() {
        return classCreatedByMe;
    }

    public void setClassCreatedByMe(List<ClassCreatedByMeBean> classCreatedByMe) {
        this.classCreatedByMe = classCreatedByMe;
    }

    public List<ProfessionalsBean> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<ProfessionalsBean> professionals) {
        this.professionals = professionals;
    }

    public static class CatsBean {
        /**
         * cat_name : 英语
         * cat_id : 2
         */

        private String cat_name;
        private String cat_id;

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }
    }

    public static class DecksBean {
        /**
         * title : 六级单词01
         * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg
         * goods_id : 1952
         * addtime : 2017-04-07 09:28:00
         * short_desc : 英语6级_01
         * price : 0
         * xia : 543
         */

        private String title;
        private String url;
        private String goods_id;
        private String addtime;
        private String short_desc;
        private String price;
        private String xia;

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

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getShort_desc() {
            return short_desc;
        }

        public void setShort_desc(String short_desc) {
            this.short_desc = short_desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getXia() {
            return xia;
        }

        public void setXia(String xia) {
            this.xia = xia;
        }
    }

    public static class ClassCreatedByMeBean {
        /**
         * classname : 英语001班
         * decks : [{"title":"1ban","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg","goods_id":"1952","addtime":"2017-04-07 09:28:00","short_desc":"英语6级_01","price":"0","xia":"543"},{"title":"六级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","goods_id":"1971","addtime":"2017-04-07 16:31:50","short_desc":"英语6级_02","price":"0","xia":"21"},{"title":"02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","goods_id":"1971","addtime":"2017-04-07 16:31:50","short_desc":"英语6级_02","price":"0","xia":"21"},{"title":"六级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","goods_id":"1971","addtime":"2017-04-07 16:31:50","short_desc":"英语6级_02","price":"0","xia":"21"}]
         */

        private String classname;
        private List<DecksBeanX> decks;

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public List<DecksBeanX> getDecks() {
            return decks;
        }

        public void setDecks(List<DecksBeanX> decks) {
            this.decks = decks;
        }

        public static class DecksBeanX {
            /**
             * title : 1ban
             * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg
             * goods_id : 1952
             * addtime : 2017-04-07 09:28:00
             * short_desc : 英语6级_01
             * price : 0
             * xia : 543
             */

            private String title;
            private String url;
            private String goods_id;
            private String addtime;
            private String short_desc;
            private String price;
            private String xia;

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

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getShort_desc() {
                return short_desc;
            }

            public void setShort_desc(String short_desc) {
                this.short_desc = short_desc;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getXia() {
                return xia;
            }

            public void setXia(String xia) {
                this.xia = xia;
            }
        }
    }

    public static class ProfessionalsBean {
        /**
         * classname : 舞蹈班
         * decks : [{"title":"舞蹈班一单元","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg","goods_id":"1952","addtime":"2017-04-07 09:28:00","short_desc":"英语6级_01","price":"0","xia":"543"},{"title":"wudao02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","goods_id":"1971","addtime":"2017-04-07 16:31:50","short_desc":"英语6级_02","price":"0","xia":"21"},{"title":"舞蹈班","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","goods_id":"1971","addtime":"2017-04-07 16:31:50","short_desc":"英语6级_02","price":"0","xia":"21"},{"title":"六级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","goods_id":"1971","addtime":"2017-04-07 16:31:50","short_desc":"英语6级_02","price":"0","xia":"21"}]
         */

        private String classname;
        private List<DecksBeanXX> decks;

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public List<DecksBeanXX> getDecks() {
            return decks;
        }

        public void setDecks(List<DecksBeanXX> decks) {
            this.decks = decks;
        }

        public static class DecksBeanXX {
            /**
             * title : 舞蹈班一单元
             * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg
             * goods_id : 1952
             * addtime : 2017-04-07 09:28:00
             * short_desc : 英语6级_01
             * price : 0
             * xia : 543
             */

            private String title;
            private String url;
            private String goods_id;
            private String addtime;
            private String short_desc;
            private String price;
            private String xia;

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

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getShort_desc() {
                return short_desc;
            }

            public void setShort_desc(String short_desc) {
                this.short_desc = short_desc;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getXia() {
                return xia;
            }

            public void setXia(String xia) {
                this.xia = xia;
            }
        }
    }
}
