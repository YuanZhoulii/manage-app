package com.example.app.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JoinData implements Serializable {

    /**
     * msg :
     * code : 0
     * data : [{"clubName":"武术协会","clubid":"3","id":176,"time":"2021-03-24 00:29:42","userid":"333333"}]
     * count : 1
     */

    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;
    @SerializedName("count")
    private String count;
    @SerializedName("data")
    private List<DataDTO> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        /**
         * clubName : 武术协会
         * clubid : 3
         * id : 176
         * time : 2021-03-24 00:29:42
         * userid : 333333
         */

        @SerializedName("clubName")
        private String clubName;
        @SerializedName("clubid")
        private String clubid;
        @SerializedName("id")
        private Integer id;
        @SerializedName("time")
        private String time;
        @SerializedName("userid")
        private String userid;

        public String getClubName() {
            return clubName;
        }

        public void setClubName(String clubName) {
            this.clubName = clubName;
        }

        public String getClubid() {
            return clubid;
        }

        public void setClubid(String clubid) {
            this.clubid = clubid;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
