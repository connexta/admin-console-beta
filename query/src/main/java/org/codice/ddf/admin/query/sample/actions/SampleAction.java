package org.codice.ddf.admin.query.sample.actions;

import org.codice.ddf.admin.query.api.fields.ReportField;
import org.codice.ddf.admin.query.commons.actions.GetAction;
import org.codice.ddf.admin.query.commons.fields.common.BaseReportField;
import org.codice.ddf.admin.query.commons.fields.common.message.FailureMessageField;
import org.codice.ddf.admin.query.commons.fields.common.message.BaseMessageField;
import org.codice.ddf.admin.query.commons.fields.common.message.SuccessMessageField;
import org.codice.ddf.admin.query.commons.fields.common.message.WarningMessageField;

public class SampleAction extends GetAction<ReportField> {

    public static final String ACTION_ID = "sampleAction";
    public static final String DESCRIPTION = "Sample action for testing purposes only.";

    public SampleAction() {
        super(ACTION_ID, DESCRIPTION, new BaseReportField());
    }

    @Override
    public BaseReportField process() {
        BaseMessageField SUCCESS_SAMPLE_MSG_1 = new SuccessMessageField("PASS", "First success message");
        BaseMessageField SUCCESS_SAMPLE_MSG_2 = new SuccessMessageField("PASS_2", "Second success message");

        BaseMessageField WARNING_SAMPLE_MSG_1 = new WarningMessageField("WARN", "First warning message");
        BaseMessageField WARNING_SAMPLE_MSG_2 = new WarningMessageField("WARN_2", "Second warning message");

        BaseMessageField FAILURE_SAMPLE_MSG_1 = new FailureMessageField("FAIL", "First fail message");
        BaseMessageField FAILURE_SAMPLE_MSG_2 = new FailureMessageField("FAIL_2", "Second fail message");

        return new BaseReportField().messages(SUCCESS_SAMPLE_MSG_1,
                SUCCESS_SAMPLE_MSG_2,
                WARNING_SAMPLE_MSG_1,
                WARNING_SAMPLE_MSG_2,
                FAILURE_SAMPLE_MSG_1,
                FAILURE_SAMPLE_MSG_2);
    }

}