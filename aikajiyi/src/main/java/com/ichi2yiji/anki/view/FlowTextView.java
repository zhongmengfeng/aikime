package com.ichi2yiji.anki.view;


import android.content.Context;
import android.support.v7.widget.AppCompatTextView;

import com.ichi2yiji.anki.bean.SharedDecksBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/28.
 */

public class FlowTextView extends AppCompatTextView {
    public static final int DeckGroupType_catType = 0;  //0:普通的分类
    public static final int DeckGroupType_classCreateByme = 1;  //1:classCreateByMe
    public static final int DeckGroupType_professional = 2;  //2:professional
    private List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> decksProfessional;
    private List<SharedDecksBean.DataBean.ClassCreatedByMeBean.DecksBean> decksClassCreateByme;
    private int collectionType; //0:普通的分类;  1:classCreateByMe    2:professional
    private String classOrCourseName;
    private int classId;
    private int catId;
    private int tagIndex;

    private int itsBackgroundColor;
    private int itsFocusBackgroundColor;
    private int itsTextColor;
    private int itsFocusTextColor;
    /**
     * @TODO---6.29
     * by:xz
     */
    private String isPay;
    private String free_course_number;
    private String classname;
    private String class_price;
    private String total_course;

    public FlowTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public String getIsPay() {
        return isPay;
    }
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    public String getFree_course_number() {
        return free_course_number;
    }
    public void setFree_course_number(String free_course_number) {
        this.free_course_number = free_course_number;
    }
    public String getClassname() {
        return classname;
    }
    public void setClassname(String classname) {
        this.classname = classname;
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

    public static int getDeckGroupType_catType() {
        return DeckGroupType_catType;
    }

    public static int getDeckGroupType_classCreateByme() {
        return DeckGroupType_classCreateByme;
    }

    public static int getDeckGroupType_professional() {
        return DeckGroupType_professional;
    }

    public List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> getDecksProfessional() {
        return decksProfessional;
    }

    public void setDecksProfessional(List<SharedDecksBean.DataBean.ProfessionalsBean.DecksBean> decksProfessional) {
        this.decksProfessional = decksProfessional;
    }

    public List<SharedDecksBean.DataBean.ClassCreatedByMeBean.DecksBean> getDecksClassCreateByme() {
        return decksClassCreateByme;
    }

    public void setDecksClassCreateByme(List<SharedDecksBean.DataBean.ClassCreatedByMeBean.DecksBean> decksClassCreateByme) {
        this.decksClassCreateByme = decksClassCreateByme;
    }

    public int getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(int collectionType) {
        this.collectionType = collectionType;
    }

    public String getClassOrCourseName() {
        return classOrCourseName;
    }

    public void setClassOrCourseName(String classOrCourseName) {
        this.classOrCourseName = classOrCourseName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public int getTagIndex() {
        return tagIndex;
    }

    public void setTagIndex(int tagIndex) {
        this.tagIndex = tagIndex;
    }

    public int getItsBackgroundColor() {
        return itsBackgroundColor;
    }

    public void setItsBackgroundColor(int itsBackgroundColor) {
        this.itsBackgroundColor = itsBackgroundColor;
    }

    public int getItsFocusBackgroundColor() {
        return itsFocusBackgroundColor;
    }

    public void setItsFocusBackgroundColor(int itsFocusBackgroundColor) {
        this.itsFocusBackgroundColor = itsFocusBackgroundColor;
    }

    public int getItsTextColor() {
        return itsTextColor;
    }

    public void setItsTextColor(int itsTextColor) {
        this.itsTextColor = itsTextColor;
    }

    public int getItsFocusTextColor() {
        return itsFocusTextColor;
    }

    public void setItsFocusTextColor(int itsFocusTextColor) {
        this.itsFocusTextColor = itsFocusTextColor;
    }
}
