package com.ichi2yiji.anki.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */
public class TestGroupBean {
    // 组名
    private String groupName;
    // 该组的试题集合
    private List<TestBean> testList;

    public static class TestBean {
        // 文件全名
        private String fullName;
        // 试题类型名称
        private String typeName;
        // 试题名称
        private String testName;
        // 文件路径
        private String filePath;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<TestBean> getTestList() {
        return testList;
    }

    public void setTestList(List<TestBean> testList) {
        this.testList = testList;
    }
}
