package com.example.app.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JoinActivityData {

    /**
     * msg : 查询成功
     * data : [{"activity_id":1002,"flag":"\u0000","userid":333333}]
     */

    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        /**
         * activity_id : 1002
         * flag :  
         * userid : 333333
         */

        @SerializedName("activity_id")
        private Integer activityId;
        @SerializedName("flag")
        private String flag;
        @SerializedName("userid")
        private Integer userid;
        @SerializedName("title")
        private String title;
    }
}
