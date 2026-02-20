package tech.treeentertainment.camera;

import android.app.Application;

import org.acra.ACRA;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.data.StringFormat;

public class RycApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this, new CoreConfigurationBuilder()
            .withBuildConfigClass(BuildConfig.class)
            .withReportFormat(StringFormat.JSON)
            .build()
        );
    }
}