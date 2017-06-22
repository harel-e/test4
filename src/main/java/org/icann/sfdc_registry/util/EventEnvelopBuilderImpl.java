package org.icann.sfdc_registry.util;

import org.icann.secure_data.api.EventEnvelope;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EventEnvelopBuilderImpl implements EventEnvelopBuilder {

    public static final String prefix = "registry.";
    public static final String EVENT_SOURCE = "sfdc-registry";

    @Override
    public EventEnvelope build(Object payload) {
        String type = prefix + payload.getClass().getSimpleName();

        return new EventEnvelope().withPayload(payload)
                .withEventId(UUID.randomUUID().toString())
                .withEventSource(EVENT_SOURCE)
                .withEventType(type)
                .withEventTime(DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now()));
    }

}
