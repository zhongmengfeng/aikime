package com.ichi2yiji.anki.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 选择题 - 模板
 * Created by Administrator on 2017/4/6.
 */

public class ChoiceModel {


    private String id;
    private String name;
    private String did;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatexPre() {
        return latexPre;
    }

    public void setLatexPre(String latexPre) {
        this.latexPre = latexPre;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public int getSortf() {
        return sortf;
    }

    public void setSortf(int sortf) {
        this.sortf = sortf;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
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

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
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
         * afmt : <div id="data" style="display:none">{{testData}}</div>
         <div class="box option">
         <div id="myContent" class="mui-content" >
         <div class="" style="background: white;">
         <div class="question" id="question">

         </div>
         <div id="img" class="bakimg">

         </div>
         </div>

         <div id="answer" class="answer">
         <div class="answerList">
         <a href="javascript:;" class="select_a">										<p>
         <span class="first Icon"><img src="_noSelect.png" /></span>
         <span class="fontPadding">&nbsp;A:&nbsp;
         </span>
         </p>
         </a>
         </div>
         <div class="answerList">
         <ahref="javascript:;" class="select_a">										<p>
         <span class="first Icon"><img src="_noSelect.png" />
         </span>
         <span class="fontPadding">&nbsp;B:&nbsp;
         </span>
         </p>
         </a>
         </div>
         <div class="answerList">
         <ahref="javascript:;" class="select_a">										<p>
         <span class="first Icon"><img src="_noSelect.png" />
         </span>
         <span class="fontPadding">&nbsp;C:&nbsp;
         </span>
         </p>
         </a>
         </div>
         <div class="answerList">
         <ahref="javascript:;" class="select_a">										<p>
         <span class="first Icon"><img src="_noSelect.png" />
         </span>
         <span class="fontPadding">&nbsp;D:&nbsp;
         </span>
         </p>
         </a>
         </div>
         </div>
         </div>

         </div>
         </div>


         <div class="box box2 option active" id="box2">
         <div id="adjust">
         <div id="adjust_err">
         <div style="height: 24px;line-height: 24px;margin-left:10%;">
         <span class="leftIcon"><img src="_left.png" /></span>
         <span>答案解析</span>
         <span class="leftIcon"><img src="_right.png" /></span>
         </div>
         <span class="adjust adjust-err">
         <span class="tishi">正确答案</span>
         <span id="right-answer" style="color: #3696e9;font-size: 15px;"></span>
         <span class="tishi" style="padding-left: 26px;">你的答案</span>
         <span id="right-answer" style="color: #ed5565;font-size: 15px;" class="errChoose">D</span>
         <button id="show_explain1" type="button" class="mui-btn mui-btn-blue mui-btn-outlined" style="background-color:#fff"><a>试题解释</a></button>
         </span>
         </div>

         <div id="adjust_suc">
         <div style="height: 24px;line-height: 24px;">
         <span class="leftIcon"><img src="_left.png" /></span>
         <span>答案解析</span>
         <span class="leftIcon"><img src="_right.png" /></span>
         </div>
         <span class="adjust adjust-suc">
         <span class="tishi">恭喜你，答对了！</span>
         <button id="show_explain2" type="button" class="mui-btn mui-btn-blue mui-btn-outlined"><a>试题解释</a></button>
         </span>
         </div>
         </div>

         <div id="explain1">
         <div class="mui-collapse-content" style="padding-bottom: 0; color: black;">
         <div class="">
         <div class="" style="color: black;">
         <p style="color: #0c0e10;font-size: 13px;line-height: 17px;margin-bottom: 47px;">
         <!--<span class="label" style="color: green;">解析:</span>-->
         <span id="exp" style="line-height: 17px;"></span>
         </p>
         </div>
         </div>
         </div>
         </div>

         </div>
         <div type="button" id="btn" style="position: absolute;top:0;right:30px;width:54px;"><img src="_changeIcon.png" style="width:100%"/></div>
         <script>
         var btn=document.getElementById('btn');

         var options=document.getElementsByClassName('option');
         var page = 0;
         for(var i=0;i<options.length;i++){
         document.getElementById('btn').onclick=function(){
         if(page==0){

         options[1].setAttribute('class','box box2 option turn-down');

         options[0].setAttribute('class','box option turn-up');
         document.getElementsByClassName('answer')[0].style.height=0;
         page=1;
         }else{
         options[1].setAttribute('class','box box2 option turn-up');
         options[0].setAttribute('class','box option turn-down');
         page=0;
         }
         }
         }

         var clientHeight=window.screen.height;
         var clientWidth=window.screen.width;
         var box2=document.getElementById('box2');
         box2.style.bottom=(clientHeight-156)+'px';
         box2.style.height=(clientHeight-185)+'px';
         btn.style.top=(clientHeight-175)+'px';
         var good_answer;
         var adjust_err = document.getElementById("adjust_err");
         var adjust_suc = document.getElementById("adjust_suc");
         var explain = document.getElementById("explain");
         var data = document.querySelector("#data").textContent;
         var test = data.replace(/''/g, '"');
         var timu = JSON.parse(test);

         function showQuestionAndAnswer(){
         var content = document.getElementById("myContent");
         var answer = document.getElementById("answer");
         content.removeChild(answer);

         var newNode = document.createElement("div");
         newNode.setAttribute("id", "answer");
         newNode.setAttribute("class", "answer");
         content.appendChild(newNode);
         var bottomBar = document.getElementById("bottomBar");

         answer.setAttribute("state", "1");

         adjust_err.style.display = "none";
         adjust_suc.style.display = "none";

         var answerCount = 4;
         if (timu.type == 0) {
         answerCount = 2;
         }else if(timu.type == 2){
         answerCount = 5;
         }
         good_answer = timu.answer;
         var question = document.getElementById("question");
         answer = document.getElementById("answer");


         var answerArray = [timu.first, timu.second, timu.third, timu.fourth,timu.fifth];

         var indexArray = ['&nbsp;A:&nbsp;', '&nbsp;B:&nbsp;', '&nbsp;C:&nbsp;', '&nbsp;D:&nbsp;','&nbsp;E:&nbsp;'];

         question.innerHTML = timu.question;

         var img_temp = '';
         if (timu.img != '') {
         img_temp = '<img src="'+timu.img+'" width="100%"/>';
         document.getElementsByClassName('bakimg')[0].style.padding='0 12px 30px 12px';
         }else{
         document.getElementsByClassName('bakimg')[0].style.padding=0;
         }
         document.getElementById("img").innerHTML = img_temp;

         for (var i = 0; i < answerCount; i++) {
         var temp = '<div class="answerList">'
         +'		<aclass="select_a">'
         +'			<p>'
         +'				<span class="first Icon"><img src="_noSelect.png" /></span>'
         +'		<span class="fontPadding">'+indexArray[i] + answerArray[i];+'</span>'
         +'			</p>'
         +'		</a>'
         +'	</div>';
         answer.innerHTML += temp;
         }

         var clientHeight=window.screen.height;
         var box=document.getElementsByClassName('box')[0];
         box.style.height=(clientHeight-185)+'px';

         document.getElementById("exp").innerHTML = timu.explain;

         ////////////////addlistener//////////
         answer = document.getElementById("answer");
         var answerP=answer.getElementsByTagName('p');
         for(var i=0;i<answerP.length;i++){
         answerP[i].onclick=function(){
         var state = answer.getAttribute("state");
         if (state == 0) {
         return;
         };
         var nodeSpan = 	this.getElementsByClassName('fontPadding')[0];

         var sel = nodeSpan.innerHTML.substring(6,7);

         handleAfterSel(sel);

         }
         }

         }

         function handleAfterSel(sel){
         var index;
         if (sel == 'A') {
         index = 0;
         } else if (sel == 'B') {
         index = 1;
         } else if (sel == 'C') {
         index = 2;
         } else if (sel == 'D') {
         index = 3;
         } else if (sel == 'E') {
         index = 4;
         } else {
         }

         document.getElementsByClassName("errChoose")[0].innerHTML = sel;

         var nodes = document.getElementsByClassName(" Icon");

         if (sel !== good_answer) {
         for (var i = 0; i < nodes.length; i++) {
         //alert(22);
         if (i == index) {
         nodes[i].children[0].setAttribute("src", "_false.png");
         answer.setAttribute("state", "0");
         }
         }
         adjust_err.style.display = "block";
         document.getElementById("right-answer").innerHTML = good_answer;
         } else {
         for (var i = 0; i < nodes.length; i++) {
         //alert(22);
         if (i == index) {

         nodes[i].children[0].setAttribute("src", "_true.png");
         answer.setAttribute("state", "0");
         }
         }
         adjust_suc.style.display = "block";
         }

         //alert(22);

         }
         showQuestionAndAnswer();

         function panduan(sel){
         good_answer = timu.answer;
         document.getElementsByClassName("errChoose")[0].innerHTML = sel;

         var nodes = document.getElementsByClassName("Icon");
         document.getElementById("right-answer").innerHTML = good_answer;
         if (sel !== good_answer) {

         adjust_err.style.display = "block";
         explain.style.display = "block";
         document.getElementById("right-answer").innerHTML = good_answer;
         } else {
         adjust_suc.style.display = "block";
         explain.style.display = "block";
         }

         //alert(22);

         }





         </script>
         * qfmt : <div id="data" style="display:none">{{testData}}</div>
         <div class="box">
         <div class="outDiv headerbar">
         <div id="myContent" class="mui-content">
         <div class="" style="background: #fff;">
         <div class="question" id="question">

         </div>
         <div id="img" class="bakimg">
         <span width="100%"></span>
         </div>
         </div>

         <div id="answer" class="answer">
         <div class="answerList">
         <a href="javascript:;" class="select_a">										<p><a href="showAnswer://A" >
         <span class="first Icon"><img src="_noSelect.png" /></span>
         <span class="fontPadding">&nbsp;A:&nbsp;
         </span>
         </a></p>
         </a>

         </div>
         <div class="answerList">
         <ahref="javascript:;" class="select_a">										<p><a href="showAnswer://B" >
         <span class="first Icon">
         <img src="_noSelect.png" /></span>
         <span class="fontPadding">&nbsp;B:&nbsp;
         </span>
         </a></p>
         </a>
         </div>
         <div class="answerList">
         <ahref="javascript:;" class="select_a">										<p><a href="showAnswer://C" >
         <span class="first Icon">
         <img src="_noSelect.png" /></span>
         <span class="fontPadding">&nbsp;C:&nbsp;
         </span>
         </a></p>
         </a>
         </div>
         <div class="answerList">
         <ahref="javascript:;" class="select_a">										<p><a href="showAnswer://D" >
         <span class="first Icon">
         <img src="_noSelect.png" /></span>
         <span class="fontPadding">&nbsp;D:&nbsp;
         </span>
         </a></p>
         </a>
         </div>
         </div>
         </div>
         <div id="adjust" style="display:none">

         <div id="adjust_err">
         <div style="height: 24px;line-height: 24px;">
         <span class="leftIcon"><img src="_left.png" /></span>
         <span>答案解析</span>
         <span class="leftIcon"><img src="_right.png" /></span>
         </div>
         <span class="adjust adjust-err">
         <span class="tishi">正确答案</span>
         <span id="right-answer" style="color: #3696e9;font-size: 15px;"></span>
         <span class="tishi" style="padding-left: 26px;">你的答案</span>
         <span id="right-answer" style="color: #ed5565;font-size: 15px;" class="errChoose">D</span>
         <button id="show-explain1" type="button" class="mui-btn mui-btn-blue mui-btn-outlined"><a>试题解释</a></button>
         </span>
         </div>

         <div id="adjust_suc">
         <div style="height: 24px;line-height: 24px;">
         <span class="leftIcon"><img src="choose_images/left.png" /></span>
         <span>答案解析</span>
         <span class="leftIcon"><img src="choose_images/right.png" /></span>
         </div>
         <span class="adjust adjust-suc">
         <span class="tishi">恭喜你，答对了！</span>
         <button id="show-explain2" type="button" class="mui-btn mui-btn-blue mui-btn-outlined"><a>试题解释</a></button>
         </span>
         </div>
         </div>

         <div id="explain">
         <div class="mui-collapse-content" style="padding-bottom: 0; color: black;">
         <div class="">
         <div class="" style="color: black;">
         <p style="color: #0c0e10;font-size: 13px;line-height: 17px;margin-bottom: 47px;">
         <!--<span class="label" style="color: green;">解析:</span>-->
         <span id="exp" style="line-height: 17px;">
         机动车在道路上发生故障难以移动以后要将车后50米设置警告标志。正确，机动车在道路上发生故障难以移动以后要将车后50米设置警告标志。
         </span>
         </p>
         </div>
         </div>
         </div>
         </div>
         </div>
         </div>
         <script>

         var adjust_err = document.getElementById("adjust_err");
         var adjust_suc = document.getElementById("adjust_suc");
         var explain = document.getElementById("explain");
         var data = document.querySelector("#data").textContent;
         var test = data.replace(/''/g, '"');
         var timu = JSON.parse(test);

         showQuestionAndAnswer();
         function showQuestionAndAnswer(){
         var content = document.getElementById("myContent");
         var answer = document.getElementById("answer");
         content.removeChild(answer);

         var newNode = document.createElement("div");
         newNode.setAttribute("id", "answer");
         newNode.setAttribute("class", "answer");
         content.appendChild(newNode);
         var bottomBar = document.getElementById("bottomBar");

         answer.setAttribute("state", "1");

         adjust_err.style.display = "none";
         adjust_suc.style.display = "none";
         //explain.style.display = "none";

         var answerCount = 4;
         if (timu.type == 0) {
         answerCount = 2;
         }else if(timu.type == 2){
         answerCount = 5;
         }
         good_answer = timu.answer;
         var question = document.getElementById("question");
         answer = document.getElementById("answer");


         var answerArray = [timu.first, timu.second, timu.third, timu.fourth,timu.fifth];

         var indexArray = ['&nbsp;A:&nbsp;', '&nbsp;B:&nbsp;', '&nbsp;C:&nbsp;', '&nbsp;D:&nbsp;','&nbsp;E:&nbsp;'];

         var hrefIndex = ['A', 'B', 'C', 'D','E'];
         question.innerHTML = timu.question;

         var img_temp = '';
         if (timu.img != '') {
         img_temp = '<img src="'+timu.img+'" width="100%"/>';
         document.getElementsByClassName('bakimg')[0].style.padding='0 12px 30px 12px';
         }else{
         document.getElementsByClassName('bakimg')[0].style.padding=0;
         }
         document.getElementById("img").innerHTML = img_temp;

         for (var i = 0; i < answerCount; i++) {
         var temp = '<div class="answerList">'
         +'		<a href="showAnswer://'+hrefIndex[i]+'" class="select_a">'
         +'			<p>'
         +'				<span class="first Icon"><img src="_noSelect.png" /></span>'
         +'		<span class="fontPadding">'+indexArray[i] + answerArray[i];+'</span>'
         +'			</p>'
         +'		</a>'
         +'	</div>';
         answer.innerHTML += temp;
         }

         var clientHeight=window.screen.height;
         var box=document.getElementsByClassName('box')[0];
         box.style.height=(clientHeight-140)+'px';

         document.getElementById("exp").innerHTML = timu.explain;

         ////////////////addlistener//////////
         answer = document.getElementById("answer");
         var answerP=answer.getElementsByTagName('p');
         for(var i=0;i<answerP.length;i++){
         answerP[i].onclick=function(){
         var state = answer.getAttribute("state");
         if (state == 0) {
         return;
         };
         var nodeSpan = 	this.getElementsByClassName('fontPadding')[0];

         var sel = nodeSpan.innerHTML.substring(6,7);

         handleAfterSel(sel);

         }
         }

         }



         function handleAfterSel(sel){
         var index;
         if (sel == 'A') {
         index = 0;
         } else if (sel == 'B') {
         index = 1;
         } else if (sel == 'C') {
         index = 2;
         } else if (sel == 'D') {
         index = 3;
         } else if (sel == 'E') {
         index = 4;
         } else {
         }

         document.getElementsByClassName("errChoose")[0].innerHTML = sel;

         var nodes = document.getElementsByClassName("Icon");

         if (sel !== good_answer) {
         for (var i = 0; i < nodes.length; i++) {
         if (i == index) {

         nodes[i].children[0].setAttribute("src", "_false.png");

         }
         }
         adjust_err.style.display = "block";
         document.getElementById("right-answer").innerHTML = good_answer;
         } else {
         for (var i = 0; i < nodes.length; i++) {
         //alert(22);
         if (i == index) {

         nodes[i].children[0].setAttribute("src", "_true.png");
         answer.setAttribute("state", "0");
         }
         }
         adjust_suc.style.display = "block";
         }



         }
         var clientHeight=window.screen.height;
         var box=document.getElementsByClassName('box')[0];			box.style.height=(clientHeight-185)+'px';
         </script>
         * bqfmt :
         * name : 卡片1
         */

        private String name;
        private String qfmt;
        private Object did;
        private String bafmt;
        private String afmt;
        private int ord;
        private String bqfmt;

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
         * name : testData
         */

        private String name;
        private boolean sticky;
        private boolean rtl;
        private int ord;
        private String font;
        private int size;
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
