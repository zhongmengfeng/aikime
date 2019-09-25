package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * Created by ekar01 on 2017/5/22.
 */

public class SearchBean {

    /**
     * code : 1000
     * data : [{"addtime":"2017-04-07 09:28:00","goods_id":"1952","price":"0","short_desc":"英语6级_01","title":"六级单词01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg","xia":"560"},{"addtime":"2017-04-07 16:31:50","goods_id":"1971","price":"0","short_desc":"英语6级_02","title":"六级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","xia":"31"},{"addtime":"2017-04-07 17:16:39","goods_id":"1972","price":"0","short_desc":"四级英语单词","title":"四级单词01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg","xia":"65"},{"addtime":"2017-04-07 17:19:12","goods_id":"1973","price":"0","short_desc":"四级英语单词02","title":"四级单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__02.apkg","xia":"24"},{"addtime":"2017-04-07 17:45:13","goods_id":"1974","price":"0","short_desc":"四级英语单词03","title":"四级单词03","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__03.apkg","xia":"20"},{"addtime":"2017-04-07 17:45:38","goods_id":"1975","price":"0","short_desc":"四级英语单词04","title":"四级单词04","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%9B%9B%E7%BA%A7%E5%8D%95%E8%AF%8D__04.apkg","xia":"16"},{"addtime":"2017-04-07 17:46:10","goods_id":"1976","price":"0","short_desc":"雅思英语01","title":"雅思单词01","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E9%9B%85%E6%80%9D%E5%8D%95%E8%AF%8D__01.apkg","xia":"30"},{"addtime":"2017-04-07 17:49:55","goods_id":"1977","price":"0","short_desc":"雅思英语02","title":"雅思单词02","url":"http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E9%9B%85%E6%80%9D%E5%8D%95%E8%AF%8D__02.apkg","xia":"16"}]
     */

    private int code;
    private List<DataBean> data;


    @Override
    public String toString() {
        return "SearchBean{" +
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addtime : 2017-04-07 09:28:00
         * goods_id : 1952
         * price : 0
         * short_desc : 英语6级_01
         * title : 六级单词01
         * url : http://anki.oss-cn-hangzhou.aliyuncs.com/Uploads/%E5%85%AD%E7%BA%A7%E5%8D%95%E8%AF%8D__01.apkg
         * xia : 560
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
            return "DataBean{" +
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
}
