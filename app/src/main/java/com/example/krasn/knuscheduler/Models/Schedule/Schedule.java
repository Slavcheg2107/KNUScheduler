package com.example.krasn.knuscheduler.Models.Schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by krasn on 9/26/2017.
 */

public class Schedule extends RealmObject {
public Schedule(){}

    public Schedule(String teachers, Integer day, String room, String discipline, String group, Integer week, Integer lesson, String lessontype, String subgroup) {
        this.teachers = teachers;
        this.day = day;
        this.room = room;
        this.discipline = discipline;
        this.group = group;
        this.week = week;
        this.lesson = lesson;
        this.lessontype = lessontype;
        this.subgroup = subgroup;
    }

    @SerializedName("teachers")
        @Expose
        private String teachers;
        @SerializedName("day")
        @Expose
        private Integer day;
        @SerializedName("room")
        @Expose
        private String room;
        @SerializedName("discipline")
        @Expose
        private String discipline;
        @SerializedName("group")
        @Expose
        private String group;
        @SerializedName("week")
        @Expose
        private Integer week;
        @SerializedName("lesson")
        @Expose
        private Integer lesson;
        @SerializedName("lessontype")
        @Expose
        private String lessontype;
        @SerializedName("subgroup")
        @Expose
        private String subgroup;

        public String getTeachers() {
            return teachers;
        }

        public void setTeachers(String teachers) {
            this.teachers = teachers;
        }

        public Schedule withTeachers(String teachers) {
            this.teachers = teachers;
            return this;
        }

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public Schedule withDay(Integer day) {
            this.day = day;
            return this;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public Schedule withRoom(String room) {
            this.room = room;
            return this;
        }

        public String getDiscipline() {
            return discipline;
        }

        public void setDiscipline(String discipline) {
            this.discipline = discipline;
        }

        public Schedule withDiscipline(String discipline) {
            this.discipline = discipline;
            return this;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Schedule withGroup(String group) {
            this.group = group;
            return this;
        }

        public Integer getWeek() {
            return week;
        }

        public void setWeek(Integer week) {
            this.week = week;
        }

        public Schedule withWeek(Integer week) {
            this.week = week;
            return this;
        }

        public Integer getLesson() {
            return lesson;
        }

        public void setLesson(Integer lesson) {
            this.lesson = lesson;
        }

        public Schedule withLesson(Integer lesson) {
            this.lesson = lesson;
            return this;
        }

        public String getLessontype() {
            return lessontype;
        }

        public void setLessontype(String lessontype) {
            this.lessontype = lessontype;
        }

        public Schedule withLessontype(String lessontype) {
            this.lessontype = lessontype;
            return this;
        }

        public Object getSubgroup() {
            return subgroup;
        }

        public void setSubgroup(String subgroup) {
            this.subgroup = subgroup;
        }

        public Schedule withSubgroup(String subgroup) {
            this.subgroup = subgroup;
            return this;
        }


}
