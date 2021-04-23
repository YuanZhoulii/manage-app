package com.example.app.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class Activity {
    /**
     * msg : 查询成功
     * data : [{"activity_id":1002,"content":"创E社团在南二407教室以学术论文撰写基本格式为题进行了一场专业讲座活动，信息工程学院姜攀老师为创E社团全体同学进行了讲解。通过讲解，学生熟悉了撰写学术论文的基本格式，培养了学生的学术素养和学术能力，学生收获满满。","endDate":"2021-04-01","startDate":"2019-11-01","title":"论文撰写讲座活动"},{"activity_id":1002,"content":"信息工程学院姜攀老师在南二506教室为信息工程学院创E社团学生讲授网上绘图工具的使用，创E社团共60名学生积极参加，学生收获很大。","endDate":"2021-03-25","startDate":"2019-10-24","title":"讲授网上绘图工具"},{"activity_id":1002,"content":"信息工程学院创E社团在南二401会议室举办了2019年度创E社团新生见面会。本次新生见面会由杨爽同学主持，创E社团新社员61位同学和部分老社员共73人参加此新生次见面会。结合\u201c不忘初心、牢记使命\u201d主题教育，信息工程学院创E社团在为全面提升学生教科研的参与度上下功夫，抓落实。","endDate":"2021-03-18","startDate":"2019-10-12","title":"不忘初心、牢记使命"},{"activity_id":1002,"content":"哦哦哦","endDate":"2021-03-29","startDate":"2021-03-29","title":"哦哦哦"}]
     */

    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    @ToString
    public static class DataDTO {
        /**
         * activity_id : 1002
         * content : 创E社团在南二407教室以学术论文撰写基本格式为题进行了一场专业讲座活动，信息工程学院姜攀老师为创E社团全体同学进行了讲解。通过讲解，学生熟悉了撰写学术论文的基本格式，培养了学生的学术素养和学术能力，学生收获满满。
         * endDate : 2021-04-01
         * startDate : 2019-11-01
         * title : 论文撰写讲座活动
         */

        @SerializedName("id")
        private Integer id;
        @SerializedName("activity_id")
        private Integer activityId;
        @SerializedName("content")
        private String content;
        @SerializedName("endDate")
        private String endDate;
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("title")
        private String title;
        @SerializedName("flag")
        private String flag = "1";
    }
}
