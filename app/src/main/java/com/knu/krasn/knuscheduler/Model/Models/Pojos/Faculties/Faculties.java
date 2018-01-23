package com.knu.krasn.knuscheduler.Model.Models.Pojos.Faculties;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krasn on 1/17/2018.
 */

public class Faculties {

    @SerializedName("faculties")
    @Expose
    private List<Faculty> faculties = new ArrayList<Faculty>();

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

}
