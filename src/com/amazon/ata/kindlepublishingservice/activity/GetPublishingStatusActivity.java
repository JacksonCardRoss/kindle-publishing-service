package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatus;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {

    private final PublishingStatusDao publishingStatusDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        List<PublishingStatusItem> itemList = publishingStatusDao.getPublishingStatuses(publishingStatusRequest.getPublishingRecordId());
        List<PublishingStatusRecord> recordList = new ArrayList<>();

        for (PublishingStatusItem item : itemList) {
            PublishingStatusRecord publishingStatusRecord =
                    new PublishingStatusRecord(String.valueOf(PublishingRecordStatus.valueOf(item.getStatus().name())),
                            item.getStatusMessage(), item.getBookId());
            recordList.add(publishingStatusRecord);
        }

        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(recordList)
                .build();
    }
}
