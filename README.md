# Kindle Publishing Service

Created Class Diagrams for 

* `com.amazon.ata.kindlepublishingservice.activity` (except ExecuteTctActivity.java)
* `com.amazon.ata.kindlepublishingservice.clients`
* `com.amazon.ata.kindlepublishingservice.dao`
* `com.amazon.ata.kindlepublishingservice.dynamob.models`
* `com.amazon.ata.kindlepublishingservice.exceptions`
* `com.amazon.ata.kindlepublishingservice.metrics` (except MetricsConstants.java)

Created sequence diagrams for different tasks.

Created a BookPublishRequest manager to allow books to be requested and published.

Changed logic for published books to be put in a queue for approval.  

Implemented a ConcurrentLinkedQueue to process BookPublishRequests to improve efficiency
