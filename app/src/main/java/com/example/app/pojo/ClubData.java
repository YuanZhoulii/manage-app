package com.example.app.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClubData implements Serializable {

    /**
     * msg : 查询成功
     * data : [{"c_date":"2003-10-01","c_img":"1.png","c_info":"1","c_name":"海天文学社","c_rules":"1、参与会议者必须准时到会，到会者要签到登记，不得迟到，不得无故缺席。个别人员因事不能按时参加例会需提前请假，社长批准方可免责，到会迟到一次扣1分，无故缺席一次扣2分，三次无故缺席者予以自动退社处分。\\n2、编辑部成员每月义务交稿2篇，其他部门成员每月义务交稿一篇，于月初部门例会时上交，缺稿一篇扣2分，评出最佳稿件3篇，作品当选者加3分。（稿件需原创，且积极向上，稿件将会传至海天群内供交流）\\n3、活动内容和范围要与社团宗旨，章程相符，不宜脱离文学中心。\\n4、各个部门每个月交一份活动安排方案，活动内容必须与当月的文学主题相关，方案需切实可行。\\n5、社委员应当有组织性的开会交流，会议内容可以是关于个人的生活，工作总结以及文学社的发展规程。\\n6、对于违背社团纪律的社员，将在例会上予以通报批评，屡教不改者，将劝其退社。\\n7、由个人意向而导致的退社举动应当报告社委会，并由秘书长更新社员信息。\\n8、每次大型例会，将由部长汇报社团开展活动的情况，分配成员的任务，并总结社团活动的优点及不足。\\n9、例会时各社员应将手机及其他的通讯工具调至无声或者震动状态，对会议可以提出自己的意见，并认真执行会议布置的各项决议和任务。\\n10、对于社团安排的有组织性的大型活动要积极参与，每参加一次加3分，缺席一次扣2分，请假不得超过2次。","c_school_id":"1001","declaration":"海阔凭鱼跃，天高任鸟飞","id":1},{"c_date":"2015-03-11","c_img":"2.png","c_info":"1","c_name":"创E协会","c_school_id":"1001","declaration":"只要你有创意，创E就给你一个舞台","id":2},{"c_date":"2015-07-11","c_img":"3.png","c_info":"1","c_name":"武术协会","c_school_id":"1001","declaration":"崇文尚武，修身养性，承传武学，光大国术","id":3}]
     */

    @SerializedName("msg")
    private String msg;

    @SerializedName("flag")
    private String flag;

    @SerializedName("no")
    private String no;

    @SerializedName("data")
    private List<DataDTO> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        /**
         * c_date : 2003-10-01
         * c_img : 1.png
         * c_info : 1
         * c_name : 海天文学社
         * c_rules : 1、参与会议者必须准时到会，到会者要签到登记，不得迟到，不得无故缺席。个别人员因事不能按时参加例会需提前请假，社长批准方可免责，到会迟到一次扣1分，无故缺席一次扣2分，三次无故缺席者予以自动退社处分。\n2、编辑部成员每月义务交稿2篇，其他部门成员每月义务交稿一篇，于月初部门例会时上交，缺稿一篇扣2分，评出最佳稿件3篇，作品当选者加3分。（稿件需原创，且积极向上，稿件将会传至海天群内供交流）\n3、活动内容和范围要与社团宗旨，章程相符，不宜脱离文学中心。\n4、各个部门每个月交一份活动安排方案，活动内容必须与当月的文学主题相关，方案需切实可行。\n5、社委员应当有组织性的开会交流，会议内容可以是关于个人的生活，工作总结以及文学社的发展规程。\n6、对于违背社团纪律的社员，将在例会上予以通报批评，屡教不改者，将劝其退社。\n7、由个人意向而导致的退社举动应当报告社委会，并由秘书长更新社员信息。\n8、每次大型例会，将由部长汇报社团开展活动的情况，分配成员的任务，并总结社团活动的优点及不足。\n9、例会时各社员应将手机及其他的通讯工具调至无声或者震动状态，对会议可以提出自己的意见，并认真执行会议布置的各项决议和任务。\n10、对于社团安排的有组织性的大型活动要积极参与，每参加一次加3分，缺席一次扣2分，请假不得超过2次。
         * c_school_id : 1001
         * declaration : 海阔凭鱼跃，天高任鸟飞
         * id : 1
         */
        @SerializedName("c_date")
        private String cDate;
        @SerializedName("c_img")
        private String cImg;
        @SerializedName("c_info")
        private String cInfo;
        @SerializedName("c_name")
        private String cName;
        @SerializedName("c_rules")
        private String cRules;
        @SerializedName("c_activity_id")
        private int cActivityId;
        @SerializedName("c_school_id")
        private String cSchoolId;
        @SerializedName("declaration")
        private String declaration;
        @SerializedName("id")
        private Integer id;

        public String getCDate() {
            return cDate;
        }

        public void setCDate(String cDate) {
            this.cDate = cDate;
        }

        public String getCImg() {
            return cImg;
        }

        public void setCImg(String cImg) {
            this.cImg = cImg;
        }

        public String getCInfo() {
            return cInfo;
        }

        public void setCInfo(String cInfo) {
            this.cInfo = cInfo;
        }

        public String getCName() {
            return cName;
        }

        public void setCName(String cName) {
            this.cName = cName;
        }

        public String getCRules() {
            return cRules;
        }

        public void setCRules(String cRules) {
            this.cRules = cRules;
        }
        public int getCActivityId() {
            return cActivityId;
        }
        public void setCActivityId(int cActivityId) {
            this.cActivityId = cActivityId;
        }
        public String getCSchoolId() {
            return cSchoolId;
        }

        public void setCSchoolId(String cSchoolId) {
            this.cSchoolId = cSchoolId;
        }

        public String getDeclaration() {
            return declaration;
        }

        public void setDeclaration(String declaration) {
            this.declaration = declaration;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "DataDTO{" +
                    "cDate='" + cDate + '\'' +
                    ", cImg='" + cImg + '\'' +
                    ", cInfo='" + cInfo + '\'' +
                    ", cName='" + cName + '\'' +
                    ", cRules='" + cRules + '\'' +
                    ", cActivityId=" + cActivityId +
                    ", cSchoolId='" + cSchoolId + '\'' +
                    ", declaration='" + declaration + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}
