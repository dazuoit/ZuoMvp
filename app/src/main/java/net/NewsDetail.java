package net;

import java.io.Serializable;
import java.util.List;

/**
 * Description: <NewsDetail><br>
 * Author:      mxdl<br>
 * Date:        2019/5/27<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class NewsDetail {

    /**
     * list : [{"is_broad":"0","name":"测试新增预约直播课0415","live_code":"L2020041537053","short_name":"测试新增预约直播课0415","cover":"http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/15/2b31563b031d266228164e342737552f.png","new_cover":"http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/15/4efb5b3946b84bea6d32c2e3bb99740f.png","price":"1","original_price":"1","tags":["测试1"],"learn_num":"29","read_count":"62","liveSub":"10","sort":0,"start_at":"2020-04-15 18:45:00","is_vip_free":"0","is_pay":"0","status":"1","type":"1","teacher":"小哥哥","push_status":""}]
     * pageCount : 1
     * totalCount : 1
     * is_last_page : 1
     * is_vip : 0
     * is_svip : 0
     */

    private String pageCount;
    private String totalCount;
    private String is_last_page;
    private String is_vip;
    private String is_svip;
    private List<ListBean> list;

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getIs_last_page() {
        return is_last_page;
    }

    public void setIs_last_page(String is_last_page) {
        this.is_last_page = is_last_page;
    }

    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }

    public String getIs_svip() {
        return is_svip;
    }

    public void setIs_svip(String is_svip) {
        this.is_svip = is_svip;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * is_broad : 0
         * name : 测试新增预约直播课0415
         * live_code : L2020041537053
         * short_name : 测试新增预约直播课0415
         * cover : http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/15/2b31563b031d266228164e342737552f.png
         * new_cover : http://rightinhome.oss-cn-hangzhou.aliyuncs.com/uploads/2020/04/15/4efb5b3946b84bea6d32c2e3bb99740f.png
         * price : 1
         * original_price : 1
         * tags : ["测试1"]
         * learn_num : 29
         * read_count : 62
         * liveSub : 10
         * sort : 0
         * start_at : 2020-04-15 18:45:00
         * is_vip_free : 0
         * is_pay : 0
         * status : 1
         * type : 1
         * teacher : 小哥哥
         * push_status :
         */

        private String is_broad;
        private String name;
        private String live_code;
        private String short_name;
        private String cover;
        private String new_cover;
        private String price;
        private String original_price;
        private String learn_num;
        private String read_count;
        private String liveSub;
        private int sort;
        private String start_at;
        private String is_vip_free;
        private String is_pay;
        private String status;
        private String type;
        private String teacher;
        private String push_status;
        private List<String> tags;

        public String getIs_broad() {
            return is_broad;
        }

        public void setIs_broad(String is_broad) {
            this.is_broad = is_broad;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLive_code() {
            return live_code;
        }

        public void setLive_code(String live_code) {
            this.live_code = live_code;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getNew_cover() {
            return new_cover;
        }

        public void setNew_cover(String new_cover) {
            this.new_cover = new_cover;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }

        public String getLearn_num() {
            return learn_num;
        }

        public void setLearn_num(String learn_num) {
            this.learn_num = learn_num;
        }

        public String getRead_count() {
            return read_count;
        }

        public void setRead_count(String read_count) {
            this.read_count = read_count;
        }

        public String getLiveSub() {
            return liveSub;
        }

        public void setLiveSub(String liveSub) {
            this.liveSub = liveSub;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getIs_vip_free() {
            return is_vip_free;
        }

        public void setIs_vip_free(String is_vip_free) {
            this.is_vip_free = is_vip_free;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getPush_status() {
            return push_status;
        }

        public void setPush_status(String push_status) {
            this.push_status = push_status;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
