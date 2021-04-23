package com.example.app.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Vote {


    /**
     * msg : 查询成功
     * data : [{"clubid":"2","endDate":"2021-03-24","no":0,"userid":"123","value":"你好荆轲荆轲","yes":0}]
     */

    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataDTO> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        /**
         * clubid : 2
         * endDate : 2021-03-24
         * no : 0
         * userid : 123
         * value : 你好荆轲荆轲
         * yes : 0
         */
        @SerializedName("id")
        private String id;
        @SerializedName("clubid")
        private String clubid;
        @SerializedName("endDate")
        private String endDate;
        @SerializedName("no")
        private Integer no;
        @SerializedName("userid")
        private String userid;
        @SerializedName("value")
        private String value;
        @SerializedName("yes")
        private Integer yes;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getYes() {
            return yes;
        }

        public void setYes(Integer yes) {
            this.yes = yes;
        }
    }
}
