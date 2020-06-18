package com.jdxl.modules.utils.vo;

import com.jdxl.common.utils.DateUtil;
import com.jdxl.modules.utils.enums.ExceptionWarnTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlarmMessage {
    // what，where，when，why （what -> who）
    // 通知给某人，在什么地方，什么时候，发生了什么报警，异常原因是什么。
    private String title;
    private Integer noticeType;
    private String errorNoticeRole;
    private ExceptionWarnTypeEnum errorWhat;
    private String errorWhen = DateUtil.getCurrentDate();
    private String errorWhere;
    private String errorWhy;
    private String errorHow;
    private String atMobiles;
    private String isAtAll;
    private Exception e;

    public AlarmMessage(String title, Integer noticeType, ExceptionWarnTypeEnum errorWhat,
                        String errorWhere, String errorWhy, String errorHow) {
        this.title = title;
        this.noticeType = noticeType;
        this.errorWhat = errorWhat;
        this.errorWhere = errorWhere;
        this.errorWhy = errorWhy;
        this.errorHow = errorHow;
    }

    public AlarmMessage(String title, Integer noticeType, ExceptionWarnTypeEnum errorWhat,
                        String errorWhere, Exception e) {
        this.title = title;
        this.noticeType = noticeType;
        this.errorWhat = errorWhat;
        this.errorWhere = errorWhere;
        this.e = e;
    }
}
