package com.thronekeeper.fun.persistence;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private String name;
    private int recordedScore;
    private long datePlayed;

    public Score() {
    }

    public Score(String name, int recordedScore, LocalDateTime datePlayed) {
        this.name = name;
        this.recordedScore = recordedScore;
        this.datePlayed = localDateToLong(datePlayed);
    }

    public Score(String name, int recordedScore, long datePlayed) {
        this.name = name;
        this.recordedScore = recordedScore;
        this.datePlayed = datePlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecordedScore() {
        return recordedScore;
    }

    public void setRecordedScore(int score) {
        this.recordedScore = score;
    }

    public long getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(long datePlayed) {
        this.datePlayed = datePlayed;
    }

    private long localDateToLong(LocalDateTime localDateTime) {
        return Long.parseLong(localDateTime.format(DATE_TIME_FORMATTER));
    }

    private LocalDateTime longToLocalDate(long value) {
        return LocalDateTime.from(DATE_TIME_FORMATTER.parse(String.valueOf(value)));
    }

}
