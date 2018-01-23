package com.knu.krasn.knuscheduler.Presenter.Utils;


import android.support.annotation.NonNull;

import io.realm.DynamicRealm;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by krasn on 11/15/2017.
 */

public class RealmMigration implements io.realm.RealmMigration {

    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {

        final RealmSchema schema = realm.getSchema();
        if(oldVersion == 0) {
            final RealmObjectSchema scheduleSchema = schema.get("Schedule");
            if (scheduleSchema != null && !scheduleSchema.hasField("beginTime")) {
                scheduleSchema.addField("beginTime", String.class);
                scheduleSchema.addField("endTime", String.class);
            }
            final RealmObjectSchema facultySchema = schema.get("Faculty");
            if (facultySchema != null && !facultySchema.hasField("id")) {
                facultySchema.addField("id", String.class);
            }
        }
    }
}


