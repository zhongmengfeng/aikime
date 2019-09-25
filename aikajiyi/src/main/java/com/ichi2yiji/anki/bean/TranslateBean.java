package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * 英文制卡翻译结果
 * Created by Administrator on 2017/4/19.
 */

public class TranslateBean {


    /**
     * code : 1000
     * ret : {"from":"en","to":"zh","trans_result":[{"src":"China","dst":"中国"}]}
     */

    private int code;
    private RetBean ret;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public static class RetBean {
        /**
         * from : en
         * to : zh
         * trans_result : [{"src":"China","dst":"中国"}]
         */

        private String from;
        private String to;
        private List<TransResultBean> trans_result;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public List<TransResultBean> getTrans_result() {
            return trans_result;
        }

        public void setTrans_result(List<TransResultBean> trans_result) {
            this.trans_result = trans_result;
        }

        public static class TransResultBean {
            /**
             * src : China
             * dst : 中国
             */

            private String src;
            private String dst;

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public String getDst() {
                return dst;
            }

            public void setDst(String dst) {
                this.dst = dst;
            }
        }
    }
}
