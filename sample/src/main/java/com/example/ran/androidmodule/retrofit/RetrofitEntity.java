package com.example.ran.androidmodule.retrofit;


import com.tool.network.retrofit.api.BaseResultEntity;

import java.util.List;

/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 10:33
 * @描述          直接请求返回数据格式
 **/
public class RetrofitEntity extends BaseResultEntity<List<RetrofitEntity.ResultBean>> {

    /**
     * code : 200
     * message : 成功!
     * result : [{"title":"日诗","content":"欲出未出光辣达，千山万山如火发。|须臾走向天上来，逐却残星赶却月。","authors":"宋太祖"},{"title":"句","content":"未离海底千山黑，才到天中万国明。","authors":"宋太祖"},{"title":"登戎州江楼闲望","content":"满目江山四望幽，白云高卷嶂烟收。|日回禽影穿疏木，风递猿声入小楼。|远岫似屏横碧落，断帆如叶截中流。","authors":"幸夤逊"},{"title":"雪","content":"片片飞来静又闲，楼头江上复山前。|飘零尽日不归去，帖破清光万里天。","authors":"幸夤逊"},{"title":"云","content":"因登巨石知来处，勃勃元生绿藓痕。|静即等闲藏草木，动时顷刻遍乾坤。|横天未必朋元恶，捧日还曾瑞至尊。|不独朝朝在巫峡，楚王何事谩劳魂。","authors":"幸夤逊"},{"title":"句  其一","content":"若教作镇居中国，争得泥金在泰山。","authors":"幸夤逊"},{"title":"句  其二","content":"才闻暖律先偷眼，既待和风始展眉。","authors":"幸夤逊"},{"title":"句  其三","content":"嚼处春冰敲齿冷，咽时雪液沃心寒。","authors":"幸夤逊"},{"title":"句  其四","content":"蒙君知重惠琼实，薄起金刀钉玉深。","authors":"幸夤逊"},{"title":"句  其五","content":"深妆玉瓦平无垅，乱拂芦花细有声。","authors":"幸夤逊"},{"title":"句  其六","content":"片逐银蟾落醉觥。","authors":"幸夤逊"},{"title":"句  其七","content":"巧剪银花乱，轻飞玉叶狂。","authors":"幸夤逊"},{"title":"句  其八","content":"寒艳芳姿色尽明。","authors":"幸夤逊"},{"title":"金陵览古 秦淮","content":"一气东南王斗牛，祖龙潜为子孙忧。|金陵地脉何曾断，不觉真人已姓刘。","authors":"朱存"},{"title":"金陵览古 石头城","content":"五城楼雉各相望，山水英灵宅帝王。|此地定由天造险，古来长恃作金汤。","authors":"朱存"},{"title":"金陵览古 段石冈","content":"孙吴纪德旧刊碑，草没蟠螭与伏龟。|惆怅冈头三段石，至今犹似鼎分时。","authors":"朱存"},{"title":"金陵览古 北渠","content":"金殿分来玉砌流，黑龙湖彻凤池头。|后庭花落恩波断，翻与南唐作御沟。","authors":"朱存"},{"title":"金陵览古 新亭","content":"满目江山异洛阳，昔人何必重悲伤。|倘能戮力扶王室，当自新亭复故乡。","authors":"朱存"},{"title":"金陵览古 天阙山","content":"牛头天际碧凝岚，王导无稽亦妄谈。|若指远山为上阙，长安应合指终南。","authors":"朱存"},{"title":"金陵览古 东山","content":"镇物高情济世才，欲随猿鹤老岩隈。|山花处处红妆面，髣髴如初拥妓来。","authors":"朱存"}]
     */
    public static class ResultBean {
        /**
         * title : 日诗
         * content : 欲出未出光辣达，千山万山如火发。|须臾走向天上来，逐却残星赶却月。
         * authors : 宋太祖
         */

        private String title;
        private String content;
        private String authors;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthors() {
            return authors;
        }

        public void setAuthors(String authors) {
            this.authors = authors;
        }
    }
}
