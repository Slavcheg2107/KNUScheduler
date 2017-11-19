package com.knu.krasn.knuscheduler.Utils;


import io.realm.DynamicRealm;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by krasn on 11/15/2017.
 */

public class RealmMigration implements io.realm.RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        final RealmSchema schema = realm.getSchema();
        if(oldVersion == 0) {
            if (!schema.contains("ScheduleTime")) {
                schema.create("ScheduleTime").addField("begin", String.class).addField("end", String.class);
            }
            final RealmObjectSchema scheduleSchema = schema.get("Schedule");
            if (!scheduleSchema.hasField("time")) {
                RealmObjectSchema scheduleTimeSchema = schema.get("ScheduleTime");
                if (scheduleTimeSchema != null) {
                    scheduleSchema.addRealmObjectField("time", scheduleTimeSchema);
                }
            }
        }
    }
}


