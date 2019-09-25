package com.ichi2yiji.anki.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 英语简约版 - 模板
 * Created by Administrator on 2017/4/6.
 */

public class EnglishModel {


    private long id;
    private String name;
    private long did;
    private int usn;
    private int sortf;
    private int mod;
    private String latexPre;
    private String latexPost;
    private int type;
    private String css;
    private List<?> vers;
    private List<?> tags;
    private List<List<Integer>> req;
    private List<FldsBean> flds;
    private List<TmplsBean> tmpls;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getSortf() {
        return sortf;
    }

    public void setSortf(int sortf) {
        this.sortf = sortf;
    }

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public String getLatexPost() {
        return latexPost;
    }

    public void setLatexPost(String latexPost) {
        this.latexPost = latexPost;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getLatexPre() {
        return latexPre;
    }

    public void setLatexPre(String latexPre) {
        this.latexPre = latexPre;
    }

    public List<?> getVers() {
        return vers;
    }

    public void setVers(List<?> vers) {
        this.vers = vers;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
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

    public List<TmplsBean> getTmpls() {
        return tmpls;
    }

    public void setTmpls(List<TmplsBean> tmpls) {
        this.tmpls = tmpls;
    }

    public static class FldsBean {
        /**
         * name : 正面
         * media : []
         * sticky : false
         * rtl : false
         * ord : 0
         * font : Arial
         * size : 20
         */

        private String name;
        private boolean sticky;
        private boolean rtl;
        private int ord;
        private String font;
        private int size;
        private List<?> media;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public String getFont() {
            return font;
        }

        public void setFont(String font) {
            this.font = font;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<?> getMedia() {
            return media;
        }

        public void setMedia(List<?> media) {
            this.media = media;
        }
    }

    public static class TmplsBean {
        /**
         * name : 卡片1
         * qfmt : <div id="data" style="display:none">{{背面}}</div>
         <div class="section">

         <div class="playSound" style='font-family: Arial; font-size: 20px;di'>{{wordMp3}}</div>

         <div class="sanjiaoxing"></div>
         <div id="front" class="items">
         </div>

         <audio id="audiotag" src=""></audio>
         <div id="accent" class="items yinbiao"></div>
         <div class="playAudio"><img src="_play.png"  onclick="playAudio(this);" height="100%"></div>
         </div>
         <script type="text/javascript">
         var data = document.querySelector("#data").textContent;
         data = data.replace(/''/g, '"');
         var fields = JSON.parse(data);
         document.querySelector("#front").innerHTML = fields.word;
         document.querySelector("#accent").innerHTML = fields.accent;

         function playAudio(){
         var word = fields.audio;
         try {
         with (document.getElementById("audiotag")) {
         setAttribute("src", word);
         play();
         }
         } catch (e) {
         }
         }


         </script>


         * did : null
         * bafmt :
         * afmt : <div class="sectionBack">
         <div class="playSound" style='font-family: Arial; font-size: 20px;di'>{{wordMp3}}</div>
         <div class="sanjiaoxing3"></div>
         <div id="back" class="items">
         </div>
         <div id="data" style="display:none">{{背面}}</div>
         <audio id="audiotag" src=""></audio>
         <div id="accent" class="items yinbiao"></div>
         <div id="mean" class="items mean"></div>
         <div class="playAudio"><img src="_play.png" onClick="playAudio();" height="100%"></div>
         <div class="example">例句：</div>
         <div id="example" class="example example1"></div>
         </div>
         <script type="text/javascript">

         var clientHeight=window.screen.height;
         console.log(clientHeight);
         document.getElementsByClassName('sectionBack')[0].style.height=(clientHeight-187)+'px';
         var data = document.querySelector("#data").textContent;
         data = data.replace(/''/g, '"');
         var fields = JSON.parse(data);
         document.querySelector("#back").innerHTML = fields.word;
         document.querySelector("#accent").innerHTML = fields.accent;
         document.querySelector("#mean").innerHTML = fields.mean_cn;
         document.querySelector("#example").innerHTML =fields.st+ '<br>' + fields.sttr +'</div>';

         function playAudio(){
         var word = fields.audio;
         try {
         with (document.getElementById("audiotag")) {
         setAttribute("src", word);
         play();
         }
         } catch (e) {
         }
         }



         </script>
         * ord : 0
         * bqfmt :
         */

        private String name;
        private String qfmt;
        private Object did;
        private String bafmt;
        private String afmt;
        private int ord;
        private String bqfmt;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQfmt() {
            return qfmt;
        }

        public void setQfmt(String qfmt) {
            this.qfmt = qfmt;
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

        public int getOrd() {
            return ord;
        }

        public void setOrd(int ord) {
            this.ord = ord;
        }

        public String getBqfmt() {
            return bqfmt;
        }

        public void setBqfmt(String bqfmt) {
            this.bqfmt = bqfmt;
        }
    }
}
