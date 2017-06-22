package org.icann.sfdc_registry.util;

import org.icann.secure_data.api.EventEnvelope;

public interface EventEnvelopBuilder {
    EventEnvelope build(Object payload);
}
