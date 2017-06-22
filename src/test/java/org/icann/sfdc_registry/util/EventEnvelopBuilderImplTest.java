package org.icann.sfdc_registry.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.icann.secure_data.api.EventEnvelope;
import org.icann.secure_data.api.TldUpdateEvent;
import org.testng.annotations.Test;

@Test
public class EventEnvelopBuilderImplTest {

    @Test
    public void testEvent() throws Exception {
        TldUpdateEvent payload = new TldUpdateEvent().withTldName("gmail").withDownloadMethod(TldUpdateEvent.DownloadMethod.HTTP).withStatus("active");
        EventEnvelope envelope = new EventEnvelopBuilderImpl().build(payload);
        System.out.println(new ObjectMapper().writeValueAsString(envelope));
    }
}