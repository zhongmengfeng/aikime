package com.ichi2yiji.anki.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 填空题 - 模板
 * Created by Administrator on 2017/4/6.
 */

public class BlankModel {


    @SerializedName("id")
    private String _$Id222; // FIXME check this code
    private String css;
    private int mod;
    private int type;
    private int sortf;
    private long did;
    private int usn;
    private String latexPost;
    private String name;
    private String latexPre;
    private List<TmplsBean> tmpls;
    private List<List<Integer>> req;
    private List<FldsBean> flds;
    private List<?> tags;
    private List<?> vers;

    public String get_$Id222() {
        return _$Id222;
    }

    public void set_$Id222(String _$Id222) {
        this._$Id222 = _$Id222;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSortf() {
        return sortf;
    }

    public void setSortf(int sortf) {
        this.sortf = sortf;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public int getUsn() {
        return usn;
    }

    public void setUsn(int usn) {
        this.usn = usn;
    }

    public String getLatexPost() {
        return latexPost;
    }

    public void setLatexPost(String latexPost) {
        this.latexPost = latexPost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatexPre() {
        return latexPre;
    }

    public void setLatexPre(String latexPre) {
        this.latexPre = latexPre;
    }

    public List<TmplsBean> getTmpls() {
        return tmpls;
    }

    public void setTmpls(List<TmplsBean> tmpls) {
        this.tmpls = tmpls;
    }

    public List<List<Integer>> getReq() {
        return req;
    }

    public void setReq(List<List<Integer>> req) {
        this.req = req;
    }

    public List<FldsBean> getFlds() {
        return flds;
    }

    public void setFlds(List<FldsBean> flds) {
        this.flds = flds;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }

    public List<?> getVers() {
        return vers;
    }

    public void setVers(List<?> vers) {
        this.vers = vers;
    }

    public static class TmplsBean {
        /**
         * ord : 0
         * did : null
         * bafmt :
         * afmt : <!-- 更多模板请访问 http://leaflyer.lofter.com -->
         <div class="comple">
         <div class="h2" id="div0" style="display:bloxk; ">{{问题}}</div>
         <div class="h2" id="daan" style="display:none;">{{答案}}</div>


         <div class="analysis">
         <div class="h2 typeface">答案解析:</div>
         <div class="h2 slide" id='div0' style="display:block; text-indent:2em;">{{答案}}</div>
         </div>
         </div>

         <script type="text/javascript">
         //答案对应位置变样式
         var wenti=document.getElementById('div0');
         var daan=document.getElementById('daan');

         var word=daan.innerText;
         word=word.replace(' ','');
         var juzi = wenti.innerHTML;

         wordnew = '<span class="cloze">'+word+'</span>';
         var new1 = juzi.replace(word,wordnew);
         document.getElementById('div0').innerHTML = new1;


         var clientHeight=window.screen.height;
         var comple=document.getElementsByClassName('comple')[0];
         comple.style.height=(clientHeight-180)+'px';

         </script>
         * qfmt : <!-- 更多模板请访问 http://leaflyer.lofter.com -->
         <div class="comple">

         <div class="h2" id="div0" style="display:block;">{{问题}}</div>
         <div class="h2" id="daan" style="display:none;">{{答案}}</div>
         </div>

         <script type="text/javascript">
         //答案对应位置变填空
         var wenti=document.getElementById('div0');
         var daan=document.getElementById('daan');

         var word=daan.innerText;
         word=word.replace(' ','');
         var juzi = wenti.innerHTML;

         wordnew = '<span style="color:transparent;font-weight: bold;border-bottom: 1px solid #0c0e10;">'+word+'</span>';
         var new1 = juzi.replace(word,wordnew);
         document.getElementById('div0').innerHTML = new1;


         //整个卡片的高度
         var clientHeight=window.screen.height;
         var comple=document.getElementsByClassName('comple')[0];
         comple.style.height=(clientHeight-180)+'px';


         </script>    
         * bqfmt :
         * name : 卡片1
         */

        private int ord;
        private Object did;
        private String bafmt;
        private String afmt;
        private String qfmt;
        private String bqfmt;
        private String name;

        public int getOrd() {
            return ord;
        }

        public void setOrd(int ord) {
            this.ord = ord;
        }

        public Object getDid() {
            return did;
        }

        public void setDid(Object did) {
            this.did = did;
        }

        public String getBafmt() {
            return bafmt;
        }

        public void setBafmt(String bafmt) {
            this.bafmt = bafmt;
        }

        public String getAfmt() {
            return afmt;
        }

        public void setAfmt(String afmt) {
            this.afmt = afmt;
        }

        public String getQfmt() {
            return qfmt;
        }

        public void setQfmt(String qfmt) {
            this.qfmt = qfmt;
        }

        public String getBqfmt() {
            return bqfmt;
        }

        public void setBqfmt(String bqfmt) {
            this.bqfmt = bqfmt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class FldsBean {
        /**
         * sticky : false
         * rtl : false
         * ord : 0
         * media : []
         * size : 20
         * font : Arial
         * name : 问题
         */

        private boolean sticky;
        private boolean rtl;
        private int ord;
        private int size;
        private String font;
        private String name;
        private List<?> media;

        public boolean isSticky() {
            return sticky;
        }

        public void setSticky(boolean sticky) {
            this.sticky = sticky;
        }

        public boolean isRtl() {
            return rtl;
        }

        public void setRtl(boolean rtl) {
            this.rtl = rtl;
        }

        public int getOrd() {
            return ord;
        }

        public void setOrd(int ord) {
            this.ord = ord;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getFont() {
            return font;
        }

        public void setFont(String font) {
            this.font = font;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<?> getMedia() {
            return media;
        }

        public void setMedia(List<?> media) {
            this.media = media;
        }
    }
}
