package io.rverb.feedback.data.api;

import android.app.IntentService;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.rverb.feedback.model.EndUser;
import io.rverb.feedback.model.Patch;
import io.rverb.feedback.utility.DataUtils;
import io.rverb.feedback.utility.RverbioUtils;

public class UserService extends IntentService {
    public UserService() {
        super("UserService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        Serializable userObject = intent.getSerializableExtra(DataUtils.EXTRA_DATA);
        String tempFileName = intent.getStringExtra(DataUtils.EXTRA_TEMPORARY_FILE_NAME);

        if (userObject == null) {
            throw new NullPointerException("Intent's data object is null");
        }

        if (!(userObject instanceof EndUser)) {
            throw new ClassCastException("Intent's data object is not the expected type (EndUser)");
        }

        EndUser endUser = (EndUser) userObject;
        if (RverbioUtils.isNullOrWhiteSpace(endUser.endUserId)) {
            throw new IllegalStateException("Intent EndUser object must contain EndUserId");
        }

        if (RverbioUtils.isNullOrWhiteSpace(endUser.emailAddress) && RverbioUtils.isNullOrWhiteSpace(endUser.userIdentifier)) {
            ApiManager.post(this, tempFileName, endUser);
        } else {
            patchUser(endUser, tempFileName);
        }
    }

    void patchUser(EndUser endUser, String tempFileName) {
        List<Patch> patches = new ArrayList<>();

        if (!RverbioUtils.isNullOrWhiteSpace(endUser.emailAddress)) {
            Patch patch = new Patch("replace", "/emailAddress", endUser.emailAddress);
            patches.add(patch);
        }

        if (!RverbioUtils.isNullOrWhiteSpace(endUser.userIdentifier)) {
            Patch patch = new Patch("replace", "/userIdentifier", endUser.userIdentifier);
            patches.add(patch);
        }

        ApiManager.patch(this, tempFileName, endUser.getDataTypeDescriptor(), patches, endUser.endUserId);
    }
}
