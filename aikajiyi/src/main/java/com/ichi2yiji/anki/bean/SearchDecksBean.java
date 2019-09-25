package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * 共享牌组标签搜索
 * Created by Administrator on 2017/5/12.
 */

public class SearchDecksBean {


    /**
     * code : 1000
     * data : [{"title":"日本语红宝书__N5","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704121806/%E6%97%A5%E6%9C%AC%E8%AF%AD%E7%BA%A2%E5%AE%9D%E4%B9%A6__N5.apkg","goods_id":"2000","addtime":"2017-04-11 09:43:06","short_desc":"日本语红宝书N5","price":"30","xia":"17"}]
     */

    private int code;
    private List<DataBean> data;
    private List<DataBean> decks;
    private int colorNum;
    private List<SharedDecksBean.DataBean.CatsBean> cats;

    @Override
    public String toString() {
        return "SearchDecksBean{" +
                "code=" + code +
                ", data=" + data +
                ", decks=" + decks +
                ", colorNum=" + colorNum +
                ", cats=" + cats +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public int getColorNum() {
        return colorNum;
    }

    public void setColorNum(int colorNum) {
        this.colorNum = colorNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<DataBean> getDecks() {
        return decks;
    }

    public void setDecks(List<DataBean> decks) {
        this.decks = decks;
    }

    public void setCats(List<SharedDecksBean.DataBean.CatsBean> cats) {
        this.cats = cats;
    }

    public List<SharedDecksBean.DataBean.CatsBean> getCats() {
        return cats;
    }

    public static class DataBean {
        /**
         * title : 日本语红宝书__N5
         * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/201704121806/%E6%97%A5%E6%9C%AC%E8%AF%AD%E7%BA%A2%E5%AE%9D%E4%B9%A6__N5.apkg
         * goods_id : 2000
         * addtime : 2017-04-11 09:43:06
         * short_desc : 日本语红宝书N5
         * price : 30
         * xia : 17
         */

        private String title;
        private String url;
        private String goods_id;
        private String addtime;
        private String short_desc;
        private String price;
        private String xia;

        @Override
        public String toString() {
            return "DataBean{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", goods_id='" + goods_id + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", short_desc='" + short_desc + '\'' +
                    ", price='" + price + '\'' +
                    ", xia='" + xia + '\'' +
                    '}';
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
