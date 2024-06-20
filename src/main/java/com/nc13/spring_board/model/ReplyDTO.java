package com.nc13.spring_board.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyDTO {
    public int id;
    private String content;
    private Date entryDate;
    private Date modifyDate;
    private int writerId;
    private int boardId;
    private String nickname;
}
